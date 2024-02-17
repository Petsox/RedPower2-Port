package com.eloraam.redpower.core;

import net.minecraftforge.event.entity.player.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraftforge.oredict.*;

public class CoreEvents
{
    @SubscribeEvent
    public void toolDestroyed(final PlayerDestroyItemEvent ev) {
        final EntityPlayer player = ev.entityPlayer;
        final ItemStack orig = ev.original;
        final int ci = player.inventory.currentItem;
        final Item oid = orig.getItem();
        final int odmg = orig.getItemDamage();
        final ItemStack in2 = player.inventory.getStackInSlot(ci + 27);
        ItemStack ist = player.inventory.getStackInSlot(ci);
        if (ist != null && ist.stackSize <= 0 && in2 != null && in2.getItem() == oid && (!in2.getHasSubtypes() || in2.getItemDamage() == odmg)) {
            player.inventory.setInventorySlotContents(ci, in2);
            player.inventory.setInventorySlotContents(ci + 27, (ItemStack)null);
            for (int i = 2; i > 0; --i) {
                ist = player.inventory.getStackInSlot(ci + 9 * i);
                if (ist == null) {
                    return;
                }
                if (ist.getItem() != oid) {
                    return;
                }
                if (ist.getHasSubtypes() && ist.getItemDamage() != odmg) {
                    return;
                }
                player.inventory.setInventorySlotContents(ci + 9 * i + 9, ist);
                player.inventory.setInventorySlotContents(ci + 9 * i, (ItemStack)null);
            }
        }
    }
    
    @SubscribeEvent
    public void oreRegister(final OreDictionary.OreRegisterEvent ev) {
        CoreLib.registerOre(ev.Name, ev.Ore);
    }
}
