
package com.eloraam.redpower.core;

import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public interface IMultiblock
{
    void onMultiRemoval(final int p0);
    
    AxisAlignedBB getMultiBounds(final int p0);
    
    float getMultiBlockStrength(final int p0, final EntityPlayer p1);
}
