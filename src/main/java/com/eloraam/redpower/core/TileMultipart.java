package com.eloraam.redpower.core;

import net.minecraft.item.*;
import java.util.*;
import net.minecraft.entity.player.*;

public abstract class TileMultipart extends TileExtended implements IMultipart
{
    public boolean isSideSolid(final int side) {
        return false;
    }
    
    public boolean isSideNormal(final int side) {
        return false;
    }
    
    public List<ItemStack> harvestMultipart() {
        final List<ItemStack> ist = new ArrayList<ItemStack>();
        this.addHarvestContents((List)ist);
        this.deleteBlock();
        return ist;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
    }
    
    public boolean onPartActivateSide(final EntityPlayer player, final int part, final int side) {
        return false;
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        return 0.0f;
    }
    
    public abstract boolean blockEmpty();
    
    public abstract void setPartBounds(final BlockMultipart p0, final int p1);
    
    public abstract int getSolidPartsMask();
    
    public abstract int getPartsMask();
    
    public void deleteBlock() {
        this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
    }
}
