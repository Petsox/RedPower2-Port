
package com.eloraam.redpower.world;

import com.eloraam.redpower.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.*;
import net.minecraftforge.event.entity.player.*;
import java.util.stream.*;
import com.eloraam.redpower.core.*;
import net.minecraft.inventory.*;

public class WorldEvents
{
    @SubscribeEvent
    public void onBonemeal(final BonemealEvent ev) {
        if (ev.block == RedPowerWorld.blockCrops) {
            final int md = ev.world.getBlockMetadata(ev.x, ev.y, ev.z);
            if (md == 4 || md == 5) {
                return;
            }
            if (ev.world.isRemote) {
                ev.setResult(Event.Result.ALLOW);
                return;
            }
            if (RedPowerWorld.blockCrops.fertilize(ev.world, ev.x, ev.y, ev.z)) {
                ev.setResult(Event.Result.ALLOW);
            }
        }
    }
    
    @SubscribeEvent
    public void onDeath(final LivingDeathEvent ev) {
        if (ev.source instanceof EntityDamageSource) {
            final EntityDamageSource eds = (EntityDamageSource)ev.source;
            final Entity ent = eds.getEntity();
            if (ent instanceof EntityPlayer) {
                final EntityPlayer epl = (EntityPlayer)ent;
                final ItemStack wpn = epl.getCurrentEquippedItem();
                if (EnchantmentHelper.getEnchantmentLevel(RedPowerWorld.enchantVorpal.effectId, wpn) != 0 && ev.entityLiving.getHealth() <= -20.0f) {
                    if (ev.entityLiving instanceof EntitySkeleton) {
                        final EntitySkeleton ist = (EntitySkeleton)ev.entityLiving;
                        if (ist.getSkeletonType() == 1) {
                            return;
                        }
                        ev.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 0), 0.0f);
                    }
                    else if (ev.entityLiving instanceof EntityZombie) {
                        ev.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0f);
                    }
                    else if (ev.entityLiving instanceof EntityPlayer) {
                        final ItemStack ist2 = new ItemStack(Items.skull, 1, 3);
                        ist2.setTagCompound(new NBTTagCompound());
                        ist2.getTagCompound().setString("SkullOwner", ev.entityLiving.getCommandSenderName());
                        ev.entityLiving.entityDropItem(ist2, 0.0f);
                    }
                    else if (ev.entityLiving instanceof EntityCreeper) {
                        ev.entityLiving.entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0f);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPickupItem(final EntityItemPickupEvent ev) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack ist = ev.entityPlayer.inventory.getStackInSlot(i);
            if (ist != null && ist.getItem() instanceof ItemSeedBag) {
                final IInventory inv = ItemSeedBag.getBagInventory(ist, ev.entityPlayer);
                if (inv != null && ItemSeedBag.getPlant(inv) != null) {
                    final ItemStack tpi = ev.item.getEntityItem();
                    final int[] slots = IntStream.range(0, inv.getSizeInventory()).toArray();
                    if (ItemSeedBag.canAdd(inv, tpi) && MachineLib.addToInventoryCore(inv, tpi, slots, true)) {
                        ev.item.setDead();
                        ev.setResult(Event.Result.ALLOW);
                        return;
                    }
                }
            }
        }
    }
}
