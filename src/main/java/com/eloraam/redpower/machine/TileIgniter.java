package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.*;

public class TileIgniter extends TileMachine
{
    public int getExtendedID() {
        return 12;
    }
    
    private void fireAction() {
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        wc.step(this.Rotation ^ 0x1);
        if (super.Active) {
            if (super.worldObj.isAirBlock(wc.x, wc.y, wc.z)) {
                super.worldObj.setBlock(wc.x, wc.y, wc.z, (Block)Blocks.fire);
            }
        }
        else {
            final Block block = super.worldObj.getBlock(wc.x, wc.y, wc.z);
            if (block == Blocks.fire || block == Blocks.portal) {
                super.worldObj.setBlockToAir(wc.x, wc.y, wc.z);
            }
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (!RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (!super.Powered) {
                return;
            }
            super.Powered = false;
            if (super.Delay) {
                return;
            }
            super.Active = false;
            super.Delay = true;
            this.fireAction();
        }
        else {
            if (super.Powered) {
                return;
            }
            super.Powered = true;
            if (super.Delay) {
                return;
            }
            if (super.Active) {
                return;
            }
            super.Active = true;
            super.Delay = true;
            this.fireAction();
        }
        this.scheduleTick(5);
        this.updateBlock();
    }
    
    public boolean isOnFire(final ForgeDirection face) {
        return this.Rotation == face.ordinal() && super.Active;
    }
    
    public void onTileTick() {
        super.Delay = false;
        if (super.Active != super.Powered) {
            super.Active = super.Powered;
            this.fireAction();
            this.updateBlock();
        }
    }
}
