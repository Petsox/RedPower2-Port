package com.eloraam.redpower.base;

import com.eloraam.redpower.core.*;
import net.minecraft.creativetab.*;

public class ItemDrawplate extends ItemPartialCraft
{
    public ItemDrawplate() {
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    public boolean isFull3D() {
        return true;
    }
    
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
}
