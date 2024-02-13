//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.util.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TilePipe extends TileCovered implements IPipeConnectable
{
    public FluidBuffer pipebuf;
    public int Pressure;
    public int ConCache;
    public int Flanges;
    private boolean hasChanged;
    
    public TilePipe() {
        this.pipebuf = new FluidBuffer() {
            public TileEntity getParent() {
                return (TileEntity)TilePipe.this;
            }
            
            public void onChange() {
                TilePipe.this.markDirty();
            }
        };
        this.Pressure = 0;
        this.ConCache = -1;
        this.Flanges = -1;
        this.hasChanged = false;
    }
    
    public int getPipeConnectableSides() {
        int tr = 63;
        for (int i = 0; i < 6; ++i) {
            if ((super.CoverSides & 1 << i) > 0 && super.Covers[i] >> 8 < 3) {
                tr &= ~(1 << i);
            }
        }
        return tr;
    }
    
    public int getPipeFlangeSides() {
        this.cacheCon();
        return (this.ConCache != 3 && this.ConCache != 12 && this.ConCache != 48) ? ((Integer.bitCount(this.ConCache) == 1) ? 0 : this.ConCache) : 0;
    }
    
    public int getPipePressure(final int side) {
        return this.Pressure;
    }
    
    public FluidBuffer getPipeBuffer(final int side) {
        return this.pipebuf;
    }
    
    public boolean tryAddCover(final int side, final int cover) {
        if (!super.tryAddCover(side, cover)) {
            return false;
        }
        this.uncache();
        this.updateBlockChange();
        return true;
    }
    
    public int tryRemoveCover(final int side) {
        final int tr = super.tryRemoveCover(side);
        if (tr < 0) {
            return -1;
        }
        this.uncache();
        this.updateBlockChange();
        return tr;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            int pr = 0;
            int d = 0;
            int min = 0;
            int max = 0;
            this.cacheCon();
            for (int i = 0; i < 6; ++i) {
                if ((this.ConCache & 1 << i) != 0x0) {
                    final WorldCoord wc = new WorldCoord((TileEntity)this);
                    wc.step(i);
                    final Integer p = PipeLib.getPressure(super.worldObj, wc, i ^ 0x1);
                    if (p != null) {
                        min = Math.min(p, min);
                        max = Math.max(p, max);
                        pr += p;
                        ++d;
                    }
                }
            }
            if (d == 0) {
                this.Pressure = 0;
            }
            else {
                if (min < 0) {
                    ++min;
                }
                if (max > 0) {
                    --max;
                }
                this.Pressure = Math.max(min, Math.min(max, pr / d + Integer.signum(pr)));
            }
            PipeLib.movePipeLiquid(super.worldObj, (IPipeConnectable)this, new WorldCoord((TileEntity)this), this.ConCache);
            this.markDirty();
            if ((super.worldObj.getWorldTime() & 0x10L) == 0x0L) {
                this.hasChanged = true;
                this.markForUpdate();
                this.markDirty();
            }
        }
    }
    
    public void uncache() {
        this.ConCache = -1;
        this.Flanges = -1;
    }
    
    public void cacheCon() {
        if (this.ConCache < 0) {
            this.ConCache = PipeLib.getConnections((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
    }
    
    public void cacheFlange() {
        if (this.Flanges < 0) {
            this.cacheCon();
            this.Flanges = this.getPipeFlangeSides();
            this.Flanges |= PipeLib.getFlanges((IBlockAccess)super.worldObj, new WorldCoord((TileEntity)this), this.ConCache);
        }
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
        if (this.ConCache < 0) {
            this.ConCache = PipeLib.getConnections(iba, super.xCoord, super.yCoord, super.zCoord);
        }
        this.Flanges = 0;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerBase.blockMicro;
    }
    
    public int getExtendedID() {
        return 7;
    }
    
    public void onBlockNeighborChange(final Block block) {
        final int pf = this.Flanges;
        final int pc = this.ConCache;
        this.uncache();
        this.cacheFlange();
        if (this.Flanges != pf || pc != this.ConCache) {
            this.updateBlock();
        }
    }
    
    public int getPartsMask() {
        return super.CoverSides | 0x20000000;
    }
    
    public int getSolidPartsMask() {
        return super.CoverSides | 0x20000000;
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == 29) {
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8));
            }
            if (super.CoverSides > 0) {
                this.replaceWithCovers();
            }
            else {
                this.deleteBlock();
            }
            this.uncache();
            this.updateBlockChange();
        }
        else {
            super.onHarvestPart(player, part, willHarvest);
        }
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        super.addHarvestContents((List)ist);
        ist.add(new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8));
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockMachine bl = RedPowerMachine.blockMachine;
        return (part == 29) ? (player.getBreakSpeed((Block)bl, false, 0, this.xCoord, this.yCoord, this.zCoord) / (bl.getHardness() * 30.0f)) : super.getPartStrength(player, part);
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part == 29) {
            block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        }
        else {
            super.setPartBounds(block, part);
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.Pressure = data.getInteger("psi");
        this.pipebuf.readFromNBT(data, "buf");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("psi", this.Pressure);
        this.pipebuf.writeToNBT(data, "buf");
    }
    
    protected void readFromPacket(final NBTTagCompound data) {
        this.pipebuf.readFromPacket(data);
        if (data.hasKey("itm")) {
            this.ConCache = -1;
            this.Flanges = -1;
            super.readFromPacket(data);
        }
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        this.pipebuf.writeToPacket(data);
        if (this.hasChanged) {
            this.hasChanged = false;
            data.setBoolean("itm", true);
            super.writeToPacket(data);
        }
    }
    
    protected ItemStack getBasePickStack() {
        return new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8);
    }
}
