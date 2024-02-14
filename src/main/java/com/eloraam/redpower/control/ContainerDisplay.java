//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import java.util.*;
import java.io.*;

public class ContainerDisplay extends Container implements IHandleGuiEvent
{
    private final TileDisplay tileDisplay;
    private final byte[] screen;
    private int cursx;
    private int cursy;
    private int cursmode;
    
    private void decompress(final byte[] compress, final byte[] out) {
        int opos = 0;
        int i = 0;
        while (i < compress.length) {
            if (opos >= out.length) {
                return;
            }
            final int cmd = compress[i++] & 0xFF;
            if ((cmd & 0x80) == 0x0) {
                opos += (cmd & 0x7F);
            }
            else if (cmd == 255) {
                if (i + 2 > compress.length) {
                    return;
                }
                final int ln = Math.min(compress[i] & 0xFF, out.length - opos);
                for (int j = 0; j < ln; ++j) {
                    out[opos + j] = compress[i + 1];
                }
                opos += ln;
                i += 2;
            }
            else {
                final int ln = Math.min(Math.min(cmd & 0x7F, out.length - opos), compress.length - i);
                System.arraycopy(compress, i, out, opos, ln);
                opos += ln;
                i += ln;
            }
        }
    }
    
    public ContainerDisplay(final IInventory inv, final TileDisplay td) {
        this.screen = new byte[4000];
        this.cursx = 0;
        this.cursy = 0;
        this.cursmode = 0;
        this.tileDisplay = td;
    }
    
    public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
        if (!this.canInteractWith(player)) {
            return null;
        }
        return super.slotClick(a, b, c, player);
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileDisplay.isUseableByPlayer(player);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer player, final int i) {
        return null;
    }
    
    private byte[] getDisplayRLE() {
        final RLECompressor rle = new RLECompressor();
        for (int i = 0; i < 4000; ++i) {
            rle.addByte(this.tileDisplay.screen[i], this.screen[i] != this.tileDisplay.screen[i]);
            this.screen[i] = this.tileDisplay.screen[i];
        }
        rle.flush();
        return rle.getByteArray();
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        final byte[] drl = this.getDisplayRLE();
        for (int i = 0; i < super.crafters.size(); ++i) {
            ICrafting ic = (ICrafting) super.crafters.get(i);
            if (this.tileDisplay.cursX != this.cursx) {
                ic.sendProgressBarUpdate(this, 0, this.tileDisplay.cursX);
            }

            if (this.tileDisplay.cursY != this.cursy) {
                ic.sendProgressBarUpdate(this, 1, this.tileDisplay.cursY);
            }

            if (this.tileDisplay.cursMode != this.cursmode) {
                ic.sendProgressBarUpdate(this, 2, this.tileDisplay.cursMode);
            }
            if (drl != null) {
                RedPowerCore.sendPacketToCrafting(ic, new PacketGuiEvent.GuiMessageEvent(2, super.windowId, drl));
            }
        }
        this.cursx = this.tileDisplay.cursX;
        this.cursy = this.tileDisplay.cursY;
        this.cursmode = this.tileDisplay.cursMode;
    }
    
    public void updateProgressBar(final int id, final int value) {
        switch (id) {
            case 0: {
                this.tileDisplay.cursX = value;
            }
            case 1: {
                this.tileDisplay.cursY = value;
            }
            case 2: {
                this.tileDisplay.cursMode = value;
                break;
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        try {
            switch (message.eventId) {
                case 1: {
                    this.tileDisplay.pushKey(message.parameters[0]);
                    this.tileDisplay.updateBlock();
                    break;
                }
                case 2: {
                    this.decompress(message.parameters, this.tileDisplay.screen);
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
    
    public class RLECompressor
    {
        ByteArrayOutputStream bas;
        byte[] datbuf;
        byte srledat;
        int rleoffs;
        int srleoffs;
        int datpos;
        boolean changed;
        
        public RLECompressor() {
            this.bas = new ByteArrayOutputStream();
            this.datbuf = new byte[256];
            this.srledat = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
            this.datpos = 0;
            this.changed = false;
        }
        
        public void writeRLE() {
            this.bas.write((byte)this.rleoffs);
            this.datpos = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
        }
        
        public void writeSRLE() {
            this.bas.write(-1);
            this.bas.write((byte)this.srleoffs);
            this.bas.write(this.srledat);
            this.datpos = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
        }
        
        public void writeDat(final int bytes) {
            if (bytes != 0) {
                this.bas.write((byte)(0x80 | bytes));
                this.bas.write(this.datbuf, 0, bytes);
                this.datpos -= bytes;
            }
        }
        
        public void addByte(final byte b, final boolean diff) {
            if (diff) {
                this.changed = true;
                if (this.rleoffs > 5 && this.rleoffs >= this.srleoffs) {
                    this.writeDat(this.datpos - this.rleoffs);
                    this.writeRLE();
                }
                this.rleoffs = 0;
            }
            else {
                ++this.rleoffs;
                if (this.rleoffs >= 127) {
                    ++this.datpos;
                    this.writeDat(this.datpos - this.rleoffs);
                    this.writeRLE();
                    return;
                }
            }
            if (this.srleoffs == 0) {
                this.srledat = b;
                this.srleoffs = 1;
            }
            else if (b == this.srledat) {
                ++this.srleoffs;
                if (this.srleoffs >= 127) {
                    ++this.datpos;
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                    return;
                }
            }
            else {
                if (this.srleoffs > 5 && this.srleoffs >= this.rleoffs) {
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                }
                this.srledat = b;
                this.srleoffs = 1;
            }
            this.datbuf[this.datpos] = b;
            ++this.datpos;
            final int rem = Math.max(this.srleoffs, this.rleoffs);
            if (rem <= 5 && this.datpos >= 126) {
                this.writeDat(this.datpos);
                this.srleoffs = 0;
                this.rleoffs = 0;
            }
            else if (this.datpos - rem >= 126) {
                this.writeDat(this.datpos - rem);
            }
        }
        
        public void flush() {
            this.datpos -= this.rleoffs;
            this.srleoffs = Math.max(0, this.srleoffs - this.rleoffs);
            if (this.datpos != 0) {
                if (this.srleoffs > 5) {
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                }
                else {
                    this.writeDat(this.datpos);
                }
            }
        }
        
        byte[] getByteArray() {
            return this.changed ? this.bas.toByteArray() : null;
        }
    }
}
