package com.eloraam.redpower.world;

import net.minecraft.item.*;
import com.eloraam.redpower.*;

public class ItemCustomSword extends ItemSword
{
    protected Item.ToolMaterial toolMaterial2;
    
    public ItemCustomSword(final Item.ToolMaterial mat) {
        super(mat);
        this.toolMaterial2 = mat;
    }
    
    public boolean getIsRepairable(final ItemStack ist1, final ItemStack ist2) {
        return (this.toolMaterial2 == RedPowerWorld.toolMaterialRuby && ist2.isItemEqual(RedPowerBase.itemRuby)) || (this.toolMaterial2 == RedPowerWorld.toolMaterialSapphire && ist2.isItemEqual(RedPowerBase.itemSapphire)) || (this.toolMaterial2 == RedPowerWorld.toolMaterialGreenSapphire && ist2.isItemEqual(RedPowerBase.itemGreenSapphire)) || super.getIsRepairable(ist1, ist2);
    }
}
