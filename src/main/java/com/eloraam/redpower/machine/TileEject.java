
package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.item.*;

public class TileEject extends TileEjectBase
{
    public int getExtendedID() {
        return 14;
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (!super.Powered) {
                super.Powered = true;
                this.markDirty();
                if (!super.Active) {
                    super.Active = true;
                    if (this.handleExtract()) {
                        this.updateBlock();
                    }
                }
            }
        }
        else {
            if (super.Active && !this.isTickScheduled()) {
                this.scheduleTick(5);
            }
            super.Powered = false;
            this.markDirty();
        }
    }
    
    protected boolean handleExtract() {
        for (int n = 0; n < this.getSizeInventory(); ++n) {
            final ItemStack ist = this.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                this.addToBuffer(this.decrStackSize(n, 1));
                this.drainBuffer();
                return true;
            }
        }
        return false;
    }
}
