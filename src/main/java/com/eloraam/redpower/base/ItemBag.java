//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.util.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.renderer.texture.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;

public class ItemBag extends Item
{
    private IIcon[] icons;
    
    public ItemBag() {
        this.icons = new IIcon[16];
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("rpBag");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public static IInventory getBagInventory(ItemStack ist, EntityPlayer player) {
        return !(ist.getItem() instanceof ItemBag) ? null : new ItemBag.InventoryBag(ist, player);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister registerer) {
        for (int color = 0; color < 16; ++color) {
            this.icons[color] = registerer.registerIcon("rpbase:bag/" + color);
        }
    }
    
    public int getMaxItemUseDuration(final ItemStack ist) {
        return 1;
    }
    
    public IIcon getIconFromDamage(final int meta) {
        return this.icons[meta % this.icons.length];
    }
    
    public ItemStack onItemRightClick(final ItemStack ist, final World world, final EntityPlayer player) {
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui((Object)RedPowerBase.instance, 4, world, 0, 0, 0);
        }
        return ist;
    }
    
    public static class InventoryBag implements IInventory
    {
        ItemStack bagitem;
        ItemStack[] items;
        EntityPlayer player;
        
        InventoryBag(final ItemStack ist, final EntityPlayer host) {
            this.bagitem = ist;
            this.player = host;
            this.unpackInventory();
        }
        
        private void unpackInventory() {
            this.items = new ItemStack[27];
            if (this.bagitem.stackTagCompound != null) {
                final NBTTagList list = this.bagitem.stackTagCompound.getTagList("contents", 10);
                for (int i = 0; i < list.tagCount(); ++i) {
                    final NBTTagCompound item = list.getCompoundTagAt(i);
                    final byte slt = item.getByte("Slot");
                    if (slt < 27) {
                        this.items[slt] = ItemStack.loadItemStackFromNBT(item);
                    }
                }
            }
        }
        
        private void packInventory() {
            if (this.bagitem.stackTagCompound == null) {
                this.bagitem.setTagCompound(new NBTTagCompound());
            }
            final NBTTagList contents = new NBTTagList();
            for (int i = 0; i < 27; ++i) {
                if (this.items[i] != null) {
                    final NBTTagCompound cpd = new NBTTagCompound();
                    this.items[i].writeToNBT(cpd);
                    cpd.setByte("Slot", (byte)i);
                    contents.appendTag((NBTBase)cpd);
                }
            }
            this.bagitem.stackTagCompound.setTag("contents", (NBTBase)contents);
        }
        
        public int getSizeInventory() {
            return 27;
        }
        
        public ItemStack getStackInSlot(final int slot) {
            return this.items[slot];
        }
        
        public ItemStack decrStackSize(final int slot, final int num) {
            if (this.bagitem != this.player.getHeldItem()) {
                this.markDirty();
                this.player.closeScreen();
                return null;
            }
            if (this.items[slot] == null) {
                return null;
            }
            if (this.items[slot].stackSize <= num) {
                final ItemStack tr = this.items[slot];
                this.items[slot] = null;
                this.markDirty();
                return tr;
            }
            final ItemStack tr = this.items[slot].splitStack(num);
            if (this.items[slot].stackSize == 0) {
                this.items[slot] = null;
            }
            this.markDirty();
            return tr;
        }
        
        public ItemStack getStackInSlotOnClosing(final int slot) {
            if (this.items[slot] == null) {
                return null;
            }
            final ItemStack tr = this.items[slot];
            this.items[slot] = null;
            return tr;
        }
        
        public void setInventorySlotContents(final int slot, final ItemStack ist) {
            if (this.bagitem != this.player.getHeldItem()) {
                this.markDirty();
                this.player.closeScreen();
                return;
            }
            this.items[slot] = ist;
            if (ist != null && ist.stackSize > this.getInventoryStackLimit()) {
                ist.stackSize = this.getInventoryStackLimit();
            }
        }
        
        public String getInventoryName() {
            return "item.rpBag.name";
        }
        
        public int getInventoryStackLimit() {
            return 64;
        }
        
        public void markDirty() {
            this.packInventory();
        }
        
        public boolean isUseableByPlayer(final EntityPlayer player) {
            return this.bagitem == this.player.getHeldItem();
        }
        
        public void openInventory() {
        }
        
        public void closeInventory() {
        }
        
        public boolean hasCustomInventoryName() {
            return false;
        }
        
        public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
            return true;
        }
    }
}
