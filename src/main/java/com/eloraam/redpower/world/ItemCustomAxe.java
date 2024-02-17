package com.eloraam.redpower.world;

import net.minecraft.item.*;
import com.eloraam.redpower.*;

public class ItemCustomAxe extends ItemAxe
{
    public ItemCustomAxe(final Item.ToolMaterial mat) {
        super(mat);
    }
    
    public boolean getIsRepairable(final ItemStack ist1, final ItemStack ist2) {
        return (this.toolMaterial == RedPowerWorld.toolMaterialRuby && ist2.isItemEqual(RedPowerBase.itemRuby)) || (this.toolMaterial == RedPowerWorld.toolMaterialSapphire && ist2.isItemEqual(RedPowerBase.itemSapphire)) || (this.toolMaterial == RedPowerWorld.toolMaterialGreenSapphire && ist2.isItemEqual(RedPowerBase.itemGreenSapphire)) || super.getIsRepairable(ist1, ist2);
    }
}
