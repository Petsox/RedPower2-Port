package com.eloraam.redpower.core;

import java.util.*;
import net.minecraft.item.*;

public interface IMultipart
{
    boolean isSideSolid(final int p0);
    
    boolean isSideNormal(final int p0);
    
    List<ItemStack> harvestMultipart();
}
