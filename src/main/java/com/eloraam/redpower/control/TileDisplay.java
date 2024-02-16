
package com.eloraam.redpower.control;

import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileDisplay extends TileExtended implements IRedbusConnectable, IFrameSupport
{
    public byte[] screen;
    public int Rotation;
    public int memRow;
    public int cursX;
    public int cursY;
    public int cursMode;
    public int kbstart;
    public int kbpos;
    public int blitXS;
    public int blitYS;
    public int blitXD;
    public int blitYD;
    public int blitW;
    public int blitH;
    public int blitMode;
    public byte[] kbbuf;
    int rbaddr;
    
    public TileDisplay() {
        this.screen = new byte[4000];
        this.Rotation = 0;
        this.memRow = 0;
        this.cursX = 0;
        this.cursY = 0;
        this.cursMode = 2;
        this.kbstart = 0;
        this.kbpos = 0;
        this.blitXS = 0;
        this.blitYS = 0;
        this.blitXD = 0;
        this.blitYD = 0;
        this.blitW = 0;
        this.blitH = 0;
        this.blitMode = 0;
        this.kbbuf = new byte[16];
        this.rbaddr = 1;
        Arrays.fill(this.screen, (byte)32);
    }
    
    @Override
    public int rbGetAddr() {
        return this.rbaddr;
    }
    
    @Override
    public void rbSetAddr(final int addr) {
        this.rbaddr = addr;
    }
    
    @Override
    public int rbRead(final int reg) {
        if (reg >= 16 && reg < 96) {
            return this.screen[this.memRow * 80 + reg - 16];
        }
        switch (reg) {
            case 0: {
                return this.memRow;
            }
            case 1: {
                return this.cursX;
            }
            case 2: {
                return this.cursY;
            }
            case 3: {
                return this.cursMode;
            }
            case 4: {
                return this.kbstart;
            }
            case 5: {
                return this.kbpos;
            }
            case 6: {
                return this.kbbuf[this.kbstart] & 0xFF;
            }
            case 7: {
                return this.blitMode;
            }
            case 8: {
                return this.blitXS;
            }
            case 9: {
                return this.blitYS;
            }
            case 10: {
                return this.blitXD;
            }
            case 11: {
                return this.blitYD;
            }
            case 12: {
                return this.blitW;
            }
            case 13: {
                return this.blitH;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void rbWrite(final int reg, final int dat) {
        this.markDirty();
        if (reg >= 16 && reg < 96) {
            this.screen[this.memRow * 80 + reg - 16] = (byte)dat;
        }
        else {
            switch (reg) {
                case 0: {
                    this.memRow = dat;
                    if (this.memRow > 49) {
                        this.memRow = 49;
                    }
                }
                case 1: {
                    this.cursX = dat;
                }
                case 2: {
                    this.cursY = dat;
                }
                case 3: {
                    this.cursMode = dat;
                }
                case 4: {
                    this.kbstart = (dat & 0xF);
                }
                case 5: {
                    this.kbpos = (dat & 0xF);
                }
                case 6: {
                    this.kbbuf[this.kbstart] = (byte)dat;
                }
                case 7: {
                    this.blitMode = dat;
                }
                case 8: {
                    this.blitXS = dat;
                }
                case 9: {
                    this.blitYS = dat;
                }
                case 10: {
                    this.blitXD = dat;
                }
                case 11: {
                    this.blitYD = dat;
                }
                case 12: {
                    this.blitW = dat;
                }
                case 13: {
                    this.blitH = dat;
                }
            }
        }
    }
    
    public int getConnectableMask() {
        return 16777215;
    }
    
    public int getConnectClass(final int side) {
        return 66;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) + 1 & 0x3);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    @Override
    public boolean onBlockActivated(final EntityPlayer player) {
        if (!this.worldObj.isRemote) {
            player.openGui(RedPowerControl.instance, 1, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
        return true;
    }
    
    public Block getBlockType() {
        return RedPowerControl.blockPeripheral;
    }
    
    @Override
    public int getExtendedID() {
        return 0;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void pushKey(final byte b) {
        final int np = this.kbpos + 1 & 0xF;
        if (np != this.kbstart) {
            this.kbbuf[this.kbpos] = b;
            this.kbpos = np;
        }
    }
    
    @Override
    public void updateEntity() {
        this.runblitter();
    }
    
    private void runblitter() {
        if (this.blitMode != 0) {
            this.markDirty();
            int w = this.blitW;
            int h = this.blitH;
            w = Math.min(w, 80 - this.blitXD);
            h = Math.min(h, 50 - this.blitYD);
            if (w >= 0 && h >= 0) {
                final int doffs = this.blitYD * 80 + this.blitXD;
                switch (this.blitMode) {
                    case 1: {
                        for (int soffs = 0; soffs < h; ++soffs) {
                            for (int j = 0; j < w; ++j) {
                                this.screen[doffs + 80 * soffs + j] = (byte)this.blitXS;
                            }
                        }
                        this.blitMode = 0;
                    }
                    case 2: {
                        for (int soffs = 0; soffs < h; ++soffs) {
                            for (int j = 0; j < w; ++j) {
                                this.screen[doffs + 80 * soffs + j] ^= (byte)128;
                            }
                        }
                        this.blitMode = 0;
                    }
                    default: {
                        w = Math.min(w, 80 - this.blitXS);
                        h = Math.min(h, 50 - this.blitYS);
                        if (w >= 0 && h >= 0) {
                            final int soffs = this.blitYS * 80 + this.blitXS;
                            if (this.blitMode == 3) {
                                for (int j = 0; j < h; ++j) {
                                    for (int i = 0; i < w; ++i) {
                                        this.screen[doffs + 80 * j + i] = this.screen[soffs + 80 * j + i];
                                    }
                                }
                                this.blitMode = 0;
                                return;
                            }
                        }
                        else {
                            this.blitMode = 0;
                        }
                    }
                }
            }
            else {
                this.blitMode = 0;
            }
        }
    }
    
    @Override
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setInteger("rot", this.Rotation);
    }
    
    @Override
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getInteger("rot");
    }
    
    @Override
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    @Override
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    @Override
    public void onFrameDrop() {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.Rotation = data.getByte("rot");
        this.screen = data.getByteArray("fb");
        if (this.screen.length != 4000) {
            this.screen = new byte[4000];
        }
        this.memRow = (data.getByte("row") & 0xFF);
        this.cursX = (data.getByte("cx") & 0xFF);
        this.cursY = (data.getByte("cy") & 0xFF);
        this.cursMode = (data.getByte("cm") & 0xFF);
        this.kbstart = data.getByte("kbs");
        this.kbpos = data.getByte("kbp");
        this.kbbuf = data.getByteArray("kbb");
        if (this.kbbuf.length != 16) {
            this.kbbuf = new byte[16];
        }
        this.blitXS = (data.getByte("blxs") & 0xFF);
        this.blitYS = (data.getByte("blys") & 0xFF);
        this.blitXD = (data.getByte("blxd") & 0xFF);
        this.blitYD = (data.getByte("blyd") & 0xFF);
        this.blitW = (data.getByte("blw") & 0xFF);
        this.blitH = (data.getByte("blh") & 0xFF);
        this.blitMode = data.getByte("blmd");
        this.rbaddr = (data.getByte("rbaddr") & 0xFF);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("rot", (byte)this.Rotation);
        data.setByteArray("fb", this.screen);
        data.setByte("row", (byte)this.memRow);
        data.setByte("cx", (byte)this.cursX);
        data.setByte("cy", (byte)this.cursY);
        data.setByte("cm", (byte)this.cursMode);
        data.setByte("kbs", (byte)this.kbstart);
        data.setByte("kbp", (byte)this.kbpos);
        data.setByteArray("kbb", this.kbbuf);
        data.setByte("blxs", (byte)this.blitXS);
        data.setByte("blys", (byte)this.blitYS);
        data.setByte("blxd", (byte)this.blitXD);
        data.setByte("blyd", (byte)this.blitYD);
        data.setByte("blw", (byte)this.blitW);
        data.setByte("blh", (byte)this.blitH);
        data.setByte("blmd", (byte)this.blitMode);
        data.setByte("rbaddr", (byte)this.rbaddr);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getInteger("rot");
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("rot", this.Rotation);
    }
}
