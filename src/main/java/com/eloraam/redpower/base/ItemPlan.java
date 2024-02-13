//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import java.util.*;
import net.minecraft.item.*;

public class ItemPlan extends Item
{
    public ItemPlan() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("planFull");
        this.setTextureName("rpbase:planFull");
        this.setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(final ItemStack ist) {
        if (ist.stackTagCompound == null) {
            return super.getItemStackDisplayName(ist);
        }
        if (!ist.stackTagCompound.hasKey("result")) {
            return super.getItemStackDisplayName(ist);
        }
        final NBTTagCompound res = ist.stackTagCompound.getCompoundTag("result");
        final ItemStack result = ItemStack.loadItemStackFromNBT(res);
        return result.getItem().getItemStackDisplayName(result) + " Plan";
    }
    
    public void addInformation(final ItemStack ist, final EntityPlayer player, final List lines, final boolean par4) {
        if (ist.stackTagCompound != null) {
            final NBTTagList require = ist.stackTagCompound.getTagList("requires", 10);
            if (require != null) {
                final HashMap<HashMap<Item, Integer>, Integer> counts = new HashMap<HashMap<Item, Integer>, Integer>();
                for (int i = 0; i < require.tagCount(); ++i) {
                    final NBTTagCompound kv = require.getCompoundTagAt(i);
                    final ItemStack li = ItemStack.loadItemStackFromNBT(kv);
                    final HashMap<Item, Integer> i2d = new HashMap<Item, Integer>();
                    i2d.put(li.getItem(), li.getItemDamage());
                    Integer lc = counts.get(i2d);
                    if (lc == null) {
                        lc = 0;
                    }
                    counts.put(i2d, lc + 1);
                }
                for (final Map.Entry<HashMap<Item, Integer>, Integer> entry : counts.entrySet()) {
                    final HashMap<Item, Integer> keySet = entry.getKey();
                    final ItemStack itemStack = new ItemStack((Item)keySet.keySet().iterator().next(), 1, (int)keySet.values().iterator().next());
                    lines.add(entry.getValue() + " x " + itemStack.getItem().getItemStackDisplayName(itemStack));
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack ist) {
        return EnumRarity.rare;
    }
    
    public boolean getShareTag() {
        return true;
    }
}
