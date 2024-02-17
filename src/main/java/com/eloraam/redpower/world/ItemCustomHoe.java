package com.eloraam.redpower.world;

import net.minecraft.item.*;
import com.eloraam.redpower.*;

public class ItemCustomHoe extends ItemHoe
{
    public ItemCustomHoe(final Item.ToolMaterial mat) {
        super(mat);
    }
    
    public boolean getIsRepairable(final ItemStack ist1, final ItemStack ist2) {
        return (this.theToolMaterial == RedPowerWorld.toolMaterialRuby && ist2.isItemEqual(RedPowerBase.itemRuby)) || (this.theToolMaterial == RedPowerWorld.toolMaterialSapphire && ist2.isItemEqual(RedPowerBase.itemSapphire)) || (this.theToolMaterial == RedPowerWorld.toolMaterialGreenSapphire && ist2.isItemEqual(RedPowerBase.itemGreenSapphire)) || super.getIsRepairable(ist1, ist2);
    }
}
