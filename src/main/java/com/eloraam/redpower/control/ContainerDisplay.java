package com.eloraam.redpower.control;

import com.eloraam.redpower.RedPowerCore;
import com.eloraam.redpower.core.IHandleGuiEvent;
import com.eloraam.redpower.core.PacketGuiEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.io.ByteArrayOutputStream;

public class ContainerDisplay extends Container implements IHandleGuiEvent {
    private TileDisplay tileDisplay;
    private byte[] screen = new byte[4000];
    int cursx = 0;
    int cursy = 0;
    int cursmode = 0;

    void decompress(byte[] compress, byte[] out) {
        int opos = 0;
        int i = 0;

        while (i < compress.length) {
            if (opos >= out.length) {
                return;
            }

            int cmd = compress[i++] & 255;

            if ((cmd & 128) == 0) {
                opos += cmd & 127;
            } else {
                int ln;
                int j;

                if (cmd == 255) {
                    if (i + 2 > compress.length) {
                        return;
                    }

                    ln = Math.min(compress[i] & 255, out.length - opos);

                    for (j = 0; j < ln; ++j) {
                        out[opos + j] = compress[i + 1];
                    }

                    opos += ln;
                    i += 2;
                } else {
                    ln = Math.min(Math.min(cmd & 127, out.length - opos), compress.length - i);

                    for (j = 0; j < ln; ++j) {
                        out[opos + j] = compress[i + j];
                    }

                    opos += ln;
                    i += ln;
                }
            }
        }
    }

    public ContainerDisplay(IInventory var1, TileDisplay var2) {
        this.tileDisplay = var2;
    }

    public boolean canInteractWith(EntityPlayer var1) {
        return this.tileDisplay.isUseableByPlayer(var1);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
        return null;
    }

    byte[] getDisplayRLE() {
        ContainerDisplay$RLECompressor var1 = new ContainerDisplay$RLECompressor(this);

        for (int var2 = 0; var2 < 4000; ++var2) {
            var1.addByte(this.tileDisplay.screen[var2], this.screen[var2] != this.tileDisplay.screen[var2]);
            this.screen[var2] = this.tileDisplay.screen[var2];
        }

        var1.flush();
        return var1.getByteArray();
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        byte[] var1 = this.getDisplayRLE();

        for (int var2 = 0; var2 < this.crafters.size(); ++var2) {
            ICrafting var3 = (ICrafting) this.crafters.get(var2);

            if (this.tileDisplay.cursX != this.cursx) {
                var3.sendProgressBarUpdate(this, 0, this.tileDisplay.cursX);
            }

            if (this.tileDisplay.cursY != this.cursy) {
                var3.sendProgressBarUpdate(this, 1, this.tileDisplay.cursY);
            }

            if (this.tileDisplay.cursMode != this.cursmode) {
                var3.sendProgressBarUpdate(this, 2, this.tileDisplay.cursMode);
            }

            if (var1 != null) {
                RedPowerCore.sendPacketToCrafting(var3, new PacketGuiEvent.GuiMessageEvent(2, super.windowId, var1));
            }
        }

        this.cursx = this.tileDisplay.cursX;
        this.cursy = this.tileDisplay.cursY;
        this.cursmode = this.tileDisplay.cursMode;
    }

    public void updateProgressBar(int var1, int var2) {
        switch (var1) {
            case 0:
                this.tileDisplay.cursX = var2;
                return;

            case 1:
                this.tileDisplay.cursY = var2;
                return;

            case 2:
                this.tileDisplay.cursMode = var2;
                return;

            default:
        }
    }

    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent var1) {
        try {
            switch (var1.eventId) {
                case 1:
                    this.tileDisplay.pushKey(var1.parameters[0]);
                    this.tileDisplay.updateBlock();
                    break;

                case 2:
                    byte[] var2 = var1.parameters;
                    this.decompress(var2, this.tileDisplay.screen);
            }
        } catch (Throwable t) {
        }
    }

    public class ContainerDisplay$RLECompressor {
        ByteArrayOutputStream bas;
        byte[] datbuf;
        byte srledat;
        int rleoffs;
        int srleoffs;
        int datpos;
        boolean changed;

        final ContainerDisplay this$0;

        public ContainerDisplay$RLECompressor(ContainerDisplay var1) {
            this.this$0 = var1;
            this.bas = new ByteArrayOutputStream();
            this.datbuf = new byte[256];
            this.srledat = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
            this.datpos = 0;
            this.changed = false;
        }

        public void writeRLE() {
            this.bas.write((byte) this.rleoffs);
            this.datpos = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
        }

        public void writeSRLE() {
            this.bas.write(-1);
            this.bas.write((byte) this.srleoffs);
            this.bas.write(this.srledat);
            this.datpos = 0;
            this.rleoffs = 0;
            this.srleoffs = 0;
        }

        public void writeDat(int var1) {
            if (var1 != 0) {
                this.bas.write((byte) (128 | var1));
                this.bas.write(this.datbuf, 0, var1);
                this.datpos -= var1;
            }
        }

        public void addByte(byte var1, boolean var2) {
            if (var2) {
                this.changed = true;

                if (this.rleoffs > 5 && this.rleoffs >= this.srleoffs) {
                    this.writeDat(this.datpos - this.rleoffs);
                    this.writeRLE();
                }

                this.rleoffs = 0;
            } else {
                ++this.rleoffs;

                if (this.rleoffs >= 127) {
                    ++this.datpos;
                    this.writeDat(this.datpos - this.rleoffs);
                    this.writeRLE();
                    return;
                }
            }

            if (this.srleoffs == 0) {
                this.srledat = var1;
                this.srleoffs = 1;
            } else if (var1 == this.srledat) {
                ++this.srleoffs;

                if (this.srleoffs >= 127) {
                    ++this.datpos;
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                    return;
                }
            } else {
                if (this.srleoffs > 5 && this.srleoffs >= this.rleoffs) {
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                }

                this.srledat = var1;
                this.srleoffs = 1;
            }

            this.datbuf[this.datpos] = var1;
            ++this.datpos;
            int var3 = Math.max(this.srleoffs, this.rleoffs);

            if (var3 <= 5 && this.datpos >= 126) {
                this.writeDat(this.datpos);
                this.srleoffs = 0;
                this.rleoffs = 0;
            } else if (this.datpos - var3 >= 126) {
                this.writeDat(this.datpos - var3);
            }
        }

        public void flush() {
            this.datpos -= this.rleoffs;
            this.srleoffs = Math.max(0, this.srleoffs - this.rleoffs);

            if (this.datpos != 0) {
                if (this.srleoffs > 5) {
                    this.writeDat(this.datpos - this.srleoffs);
                    this.writeSRLE();
                } else {
                    this.writeDat(this.datpos);
                }
            }
        }

        byte[] getByteArray() {
            return !this.changed ? null : this.bas.toByteArray();
        }
    }
}