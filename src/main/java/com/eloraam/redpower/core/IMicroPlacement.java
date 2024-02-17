package com.eloraam.redpower.core;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.creativetab.*;
import java.util.*;

public interface IMicroPlacement
{
    boolean onPlaceMicro(final ItemStack p0, final EntityPlayer p1, final World p2, final WorldCoord p3, final int p4);
    
    String getMicroName(final int p0, final int p1);
    
    void addCreativeItems(final int p0, final CreativeTabs p1, final List<ItemStack> p2);
}
