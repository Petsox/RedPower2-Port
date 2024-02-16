
package com.eloraam.redpower.world;

import net.minecraft.creativetab.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraftforge.common.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.init.*;
import net.minecraftforge.common.util.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.nbt.*;

public class ItemSeedBag extends Item
{
    private IIcon emptyIcon;
    private IIcon fullIcon;
    
    public ItemSeedBag() {
        this.setMaxDamage(576);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("rpSeedBag");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        return (meta > 0) ? this.fullIcon : this.emptyIcon;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister register) {
        this.emptyIcon = register.registerIcon("rpworld:seedBagEmpty");
        this.fullIcon = register.registerIcon("rpworld:seedBagFull");
    }
    
    public static IInventory getBagInventory(final ItemStack ist, final EntityPlayer host) {
        return (ist.getItem() instanceof ItemSeedBag) ? new InventorySeedBag(ist, host) : null;
    }
    
    public static boolean canAdd(final IInventory inv, final ItemStack ist) {
        if (!(ist.getItem() instanceof IPlantable)) {
            return false;
        }
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack is2 = inv.getStackInSlot(i);
            if (is2 != null && is2.stackSize != 0 && CoreLib.compareItemStack(is2, ist) != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static ItemStack getPlant(final IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack is2 = inv.getStackInSlot(i);
            if (is2 != null && is2.stackSize != 0) {
                return is2;
            }
        }
        return null;
    }
    
    private static void decrPlant(final IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack is2 = inv.getStackInSlot(i);
            if (is2 != null && is2.stackSize != 0) {
                inv.decrStackSize(i, 1);
                break;
            }
        }
    }
    
    public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
        return 1;
    }
    
    public ItemStack onItemRightClick(final ItemStack ist, final World world, final EntityPlayer player) {
        if (!world.isRemote && player.isSneaking()) {
            player.openGui(RedPowerWorld.instance, 1, world, 0, 0, 0);
        }
        return ist;
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float par8, final float par9, final float par10) {
        if (side != 1) {
            return false;
        }
        if (world.isRemote) {
            return false;
        }
        if (player.isSneaking()) {
            return false;
        }
        final IInventory baginv = getBagInventory(ist, player);
        final SpiralSearch search = new SpiralSearch(new WorldCoord(x, y, z), 5);
        boolean st = false;
        while (search.again()) {
            final Block soil = world.getBlock(search.point.x, search.point.y, search.point.z);
            if (soil == Blocks.air) {
                if (!st) {
                    break;
                }
            }
            else {
                final ItemStack plantstk = getPlant(baginv);
                if (plantstk == null) {
                    break;
                }
                if (!(plantstk.getItem() instanceof IPlantable)) {
                    break;
                }
                final IPlantable plant = (IPlantable)plantstk.getItem();
                if (soil != Blocks.air && soil.canSustainPlant(world, search.point.x, search.point.y, search.point.z, ForgeDirection.UP, plant)) {
                    if (!world.isAirBlock(search.point.x, search.point.y + 1, search.point.z)) {
                        if (!st) {
                            break;
                        }
                    }
                    else {
                        st = true;
                        world.setBlock(search.point.x, search.point.y + 1, search.point.z, plant.getPlant(world, search.point.x, search.point.y + 1, search.point.z), plant.getPlantMetadata(world, search.point.x, search.point.y + 1, search.point.z), 3);
                        if (!player.capabilities.isCreativeMode) {
                            decrPlant(baginv);
                        }
                    }
                }
                else if (!st) {
                    break;
                }
            }
            search.step();
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack ist, final EntityPlayer player, final List lines, final boolean par4) {
        if (ist.stackTagCompound != null && ist.getItemDamage() != 0) {
            final IInventory baginv = getBagInventory(ist, player);
            for (int i = 0; i < baginv.getSizeInventory(); ++i) {
                final ItemStack is2 = baginv.getStackInSlot(i);
                if (is2 != null && is2.stackSize != 0) {
                    lines.add(StatCollector.translateToLocal("item." + is2.getItem().getUnlocalizedName(is2) + ".name"));
                    return;
                }
            }
        }
    }
    
    public static class InventorySeedBag implements IInventory
    {
        ItemStack bagitem;
        ItemStack[] items;
        EntityPlayer player;
        
        InventorySeedBag(final ItemStack ist, final EntityPlayer host) {
            this.bagitem = ist;
            this.player = host;
            this.unpackInventory();
        }
        
        void unpackInventory() {
            this.items = new ItemStack[9];
            if (this.bagitem.stackTagCompound != null) {
                final NBTTagList list = this.bagitem.stackTagCompound.getTagList("contents", 10);
                for (int i = 0; i < list.tagCount(); ++i) {
                    final NBTTagCompound item = list.getCompoundTagAt(i);
                    final byte slt = item.getByte("Slot");
                    if (slt < 9) {
                        this.items[slt] = ItemStack.loadItemStackFromNBT(item);
                    }
                }
            }
        }
        
        private void packInventory() {
            if (this.bagitem.stackTagCompound == null) {
                this.bagitem.setTagCompound(new NBTTagCompound());
            }
            int itc = 0;
            final NBTTagList contents = new NBTTagList();
            for (int i = 0; i < 9; ++i) {
                if (this.items[i] != null) {
                    itc += this.items[i].stackSize;
                    final NBTTagCompound cpd = new NBTTagCompound();
                    this.items[i].writeToNBT(cpd);
                    cpd.setByte("Slot", (byte)i);
                    contents.appendTag(cpd);
                }
            }
            this.bagitem.stackTagCompound.setTag("contents", contents);
            this.bagitem.setItemDamage((itc == 0) ? 0 : (577 - itc));
        }
        
        public int getSizeInventory() {
            return 9;
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
            this.markDirty();
        }
        
        public String getInventoryName() {
            return "item.rpSeedBag.name";
        }
        
        public int getInventoryStackLimit() {
            return 64;
        }
        
        public void markDirty() {
            this.packInventory();
        }
        
        public boolean isUseableByPlayer(final EntityPlayer pl) {
            return true;
        }
        
        public void openInventory() {
        }
        
        public void closeInventory() {
        }
        
        public boolean hasCustomInventoryName() {
            return true;
        }
        
        public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
            return false;
        }
    }
    
    public static class SpiralSearch
    {
        int curs;
        int rem;
        int ln;
        int steps;
        public WorldCoord point;
        
        public SpiralSearch(final WorldCoord start, final int size) {
            this.point = start;
            this.curs = 0;
            this.rem = 1;
            this.ln = 1;
            this.steps = size * size;
        }
        
        public boolean again() {
            return this.steps > 0;
        }
        
        public boolean step() {
            final int steps = this.steps - 1;
            this.steps = steps;
            if (steps == 0) {
                return false;
            }
            --this.rem;
            switch (this.curs) {
                case 0: {
                    this.point.step(2);
                    break;
                }
                case 1: {
                    this.point.step(4);
                    break;
                }
                case 2: {
                    this.point.step(3);
                    break;
                }
                default: {
                    this.point.step(5);
                    break;
                }
            }
            if (this.rem > 0) {
                return true;
            }
            this.curs = (this.curs + 1 & 0x3);
            this.rem = this.ln;
            if ((this.curs & 0x1) > 0) {
                ++this.ln;
            }
            return true;
        }
    }
}
