package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileThermopile extends TileExtended implements IBluePowerConnectable
{
    BluePowerConductor cond;
    public int tempHot;
    public int tempCold;
    public int ticks;
    public int ConMask;
    
    public TileThermopile() {
        this.cond = new BluePowerConductor() {
            public TileEntity getParent() {
                return (TileEntity)TileThermopile.this;
            }
            
            public double getInvCap() {
                return 4.0;
            }
        };
        this.tempHot = 0;
        this.tempCold = 0;
        this.ticks = 0;
        this.ConMask = -1;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 64;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    public int getExtendedID() {
        return 11;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    private void updateTemps() {
        int hot = 0;
        int cold = 0;
        for (int side = 0; side < 6; ++side) {
            final WorldCoord wc = new WorldCoord((TileEntity)this);
            wc.step(side);
            final Block bid = super.worldObj.getBlock(wc.x, wc.y, wc.z);
            if (super.worldObj.isAirBlock(wc.x, wc.y, wc.z)) {
                if (super.worldObj.provider.isHellWorld) {
                    ++hot;
                }
                else {
                    ++cold;
                }
            }
            else if (bid == Blocks.snow) {
                cold += 100;
            }
            else if (bid == Blocks.ice) {
                cold += 100;
            }
            else if (bid == Blocks.snow_layer) {
                cold += 50;
            }
            else if (bid == Blocks.torch) {
                hot += 5;
            }
            else if (bid == Blocks.lit_pumpkin) {
                hot += 3;
            }
            else if (bid != Blocks.flowing_water && bid != Blocks.water) {
                if (bid != Blocks.flowing_lava && bid != Blocks.lava) {
                    if (bid == Blocks.fire) {
                        hot += 25;
                    }
                }
                else {
                    hot += 100;
                }
            }
            else {
                cold += 25;
            }
        }
        if (this.tempHot >= 100 && this.tempCold >= 200) {
            for (int side = 0; side < 6; ++side) {
                final WorldCoord wc = new WorldCoord((TileEntity)this);
                wc.step(side);
                final Block bid = super.worldObj.getBlock(wc.x, wc.y, wc.z);
                if ((bid == Blocks.flowing_lava || bid == Blocks.lava) && super.worldObj.rand.nextInt(300) == 0) {
                    final int md = super.worldObj.getBlockMetadata(wc.x, wc.y, wc.z);
                    super.worldObj.setBlock(wc.x, wc.y, wc.z, (md == 0) ? Blocks.obsidian : RedPowerWorld.blockStone, (int)((md > 0) ? 1 : 0), 3);
                    break;
                }
            }
        }
        if (this.tempHot >= 100) {
            for (int side = 0; side < 6; ++side) {
                if (super.worldObj.rand.nextInt(300) == 0) {
                    final WorldCoord wc = new WorldCoord((TileEntity)this);
                    wc.step(side);
                    final Block bid = super.worldObj.getBlock(wc.x, wc.y, wc.z);
                    if (bid == Blocks.snow_layer) {
                        super.worldObj.setBlockToAir(wc.x, wc.y, wc.z);
                        break;
                    }
                    if (bid == Blocks.ice || bid == Blocks.snow) {
                        super.worldObj.setBlock(wc.x, wc.y, wc.z, (Block)(super.worldObj.provider.isHellWorld ? Blocks.air : Blocks.flowing_water), 0, 3);
                        break;
                    }
                }
            }
        }
        this.tempHot = hot;
        this.tempCold = cold;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.cond.getVoltage() <= 100.0) {
                ++this.ticks;
                if (this.ticks > 20) {
                    this.ticks = 0;
                    this.updateTemps();
                }
                final int diff = Math.min(this.tempHot, this.tempCold);
                this.cond.applyDirect(0.005 * diff);
            }
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        this.tempHot = data.getShort("hot");
        this.tempCold = data.getShort("cold");
        this.ticks = data.getByte("ticks");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        data.setShort("hot", (short)this.tempHot);
        data.setShort("cold", (short)this.tempCold);
        data.setByte("ticks", (byte)this.ticks);
    }
}
