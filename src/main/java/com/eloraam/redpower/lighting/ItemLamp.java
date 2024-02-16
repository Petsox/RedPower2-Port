
package com.eloraam.redpower.lighting;

import com.eloraam.redpower.core.*;
import net.minecraft.block.*;

public class ItemLamp extends ItemExtended
{
    public ItemLamp(final Block block) {
        super(block);
    }
    
    public int getMetadata(final int meta) {
        return meta << 10;
    }
}
