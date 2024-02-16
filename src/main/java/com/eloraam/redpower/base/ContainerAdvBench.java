
package com.eloraam.redpower.base;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.crafting.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;

public class ContainerAdvBench extends Container implements IHandleGuiEvent
{
    SlotCraftRefill slotCraft;
    private final TileAdvBench tileAdvBench;
    public InventorySubCraft craftMatrix;
    public IInventory craftResult;
    public InventoryCrafting fakeInv;
    public int satisfyMask;
    
    public ContainerAdvBench(final InventoryPlayer inv, final TileAdvBench td) {
        this.tileAdvBench = td;
        this.craftMatrix = new InventorySubCraft(this, td);
        this.craftResult = new InventoryCraftResult();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 48 + j * 18, 18 + i * 18));
            }
        }
        this.addSlotToContainer(new SlotPlan(new InventorySubUpdate(td, 9, 1), 0, 17, 36));
        this.addSlotToContainer(this.slotCraft = new SlotCraftRefill(inv.player, this.craftMatrix, this.craftResult, td, this, 0, 143, 36));
        final InventorySubUpdate ingrid = new InventorySubUpdate(td, 10, 18);
        for (int k = 0; k < 2; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(ingrid, l + k * 9, 8 + l * 18, 90 + k * 18));
            }
        }
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(inv, l + k * 9 + 9, 8 + l * 18, 140 + k * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(inv, k, 8 + k * 18, 198));
        }
        this.fakeInv = new InventoryCrafting(new ContainerNull(), 3, 3);
        this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    public void putStackInSlot(final int num, final ItemStack ist) {
        super.putStackInSlot(num, ist);
    }
    
    public static ItemStack[] getShadowItems(final ItemStack ist) {
        if (ist.stackTagCompound == null) {
            return null;
        }
        final NBTTagList require = ist.stackTagCompound.getTagList("requires", 10);
        if (require == null) {
            return null;
        }
        final ItemStack[] tr = new ItemStack[9];
        for (int i = 0; i < require.tagCount(); ++i) {
            final NBTTagCompound item = require.getCompoundTagAt(i);
            final ItemStack is2 = ItemStack.loadItemStackFromNBT(item);
            final byte sl = item.getByte("Slot");
            if (sl >= 0 && sl < 9) {
                tr[sl] = is2;
            }
        }
        return tr;
    }
    
    public boolean canInteractWith(final EntityPlayer player) {
        return this.tileAdvBench.isUseableByPlayer(player);
    }
    
    public ItemStack[] getPlanItems() {
        final ItemStack plan = this.tileAdvBench.getStackInSlot(9);
        return (plan == null) ? null : getShadowItems(plan);
    }
    
    public int getSatisfyMask() {
        final ItemStack plan = this.tileAdvBench.getStackInSlot(9);
        ItemStack[] items = null;
        if (plan != null) {
            items = getShadowItems(plan);
        }
        int bits = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack test = this.tileAdvBench.getStackInSlot(i);
            if (test != null) {
                bits |= 1 << i;
            }
            else if (items == null || items[i] == null) {
                bits |= 1 << i;
            }
        }
        if (bits == 511) {
            return 511;
        }
        for (int i = 0; i < 18; ++i) {
            final ItemStack test = this.tileAdvBench.getStackInSlot(10 + i);
            if (test != null && test.stackSize != 0) {
                int sc = test.stackSize;
                for (int j = 0; j < 9; ++j) {
                    if ((bits & 1 << j) <= 0) {
                        ItemStack st = this.tileAdvBench.getStackInSlot(j);
                        if (st == null) {
                            st = items[j];
                            if (st != null && CoreLib.matchItemStackOre(st, test)) {
                                bits |= 1 << j;
                                if (--sc == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return bits;
    }
    
    private int findMatch(final ItemStack a) {
        for (int i = 0; i < 18; ++i) {
            final ItemStack test = this.tileAdvBench.getStackInSlot(10 + i);
            if (test != null && test.stackSize != 0 && CoreLib.matchItemStackOre(a, test)) {
                return 10 + i;
            }
        }
        return -1;
    }
    
    public void onCraftMatrixChanged(final IInventory iinventory) {
        final ItemStack plan = this.tileAdvBench.getStackInSlot(9);
        ItemStack[] items = null;
        if (plan != null) {
            items = getShadowItems(plan);
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack tos = this.tileAdvBench.getStackInSlot(i);
            if (tos == null && items != null && items[i] != null) {
                final int j = this.findMatch(items[i]);
                if (j > 0) {
                    tos = this.tileAdvBench.getStackInSlot(j);
                }
            }
            this.fakeInv.setInventorySlotContents(i, tos);
        }
        this.satisfyMask = this.getSatisfyMask();
        if (this.satisfyMask == 511) {
            this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.fakeInv, this.tileAdvBench.getWorldObj()));
        }
        else {
            this.craftResult.setInventorySlotContents(0, null);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) super.inventorySlots.get(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (i == 10) {
                this.mergeCrafting(player, slot, 29, 65);
                return null;
            }

            if (i < 9) {
                if (!this.mergeItemStack(itemstack1, 11, 29, false)) {
                    return null;
                }
            } else if (i < 29) {
                if (!this.mergeItemStack(itemstack1, 29, 65, true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 11, 29, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    protected boolean canFit(ItemStack ist, int st, int ed) {
        int ms = 0;

        for (int i = st; i < ed; ++i) {
            Slot slot = (Slot) super.inventorySlots.get(i);
            ItemStack is2 = slot.getStack();
            if (is2 == null) {
                return true;
            }

            if (CoreLib.compareItemStack(is2, ist) == 0) {
                ms += is2.getMaxStackSize() - is2.stackSize;
                if (ms >= ist.stackSize) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void fitItem(ItemStack ist, int st, int ed) {
        int i;
        Slot slot;
        ItemStack is2;
        if (ist.isStackable()) {
            for (i = st; i < ed; ++i) {
                slot = (Slot) super.inventorySlots.get(i);
                is2 = slot.getStack();
                if (is2 != null && CoreLib.compareItemStack(is2, ist) == 0) {
                    int n = Math.min(ist.stackSize, ist.getMaxStackSize()
                            - is2.stackSize);
                    if (n != 0) {
                        ist.stackSize -= n;
                        is2.stackSize += n;
                        slot.onSlotChanged();
                        if (ist.stackSize == 0) {
                            return;
                        }
                    }
                }
            }
        }

        for (i = st; i < ed; ++i) {
            slot = (Slot) super.inventorySlots.get(i);
            is2 = slot.getStack();
            if (is2 == null) {
                slot.putStack(ist);
                slot.onSlotChanged();
                return;
            }
        }

    }
    
    protected void mergeCrafting(final EntityPlayer player, final Slot cslot, final int st, final int ed) {
        int cc = 0;
        ItemStack ist = cslot.getStack();
        if (ist != null && ist.stackSize != 0) {
            final ItemStack craftas = ist.copy();
            int mss = craftas.getMaxStackSize();
            if (mss == 1) {
                mss = 16;
            }
            while (this.canFit(ist, st, ed)) {
                cc += ist.stackSize;
                this.fitItem(ist, st, ed);
                cslot.onPickupFromSlot(player, ist);
                if (cc >= mss) {
                    return;
                }
                if (this.slotCraft.isLastUse()) {
                    return;
                }
                ist = cslot.getStack();
                if (ist == null || ist.stackSize == 0) {
                    return;
                }
                if (CoreLib.compareItemStack(ist, craftas) != 0) {
                    return;
                }
            }
        }
    }
    
    public void handleGuiEvent(final PacketGuiEvent.GuiMessageEvent message) {
        if (this.tileAdvBench.getWorldObj() != null && !this.tileAdvBench.getWorldObj().isRemote) {
            try {
                if (message.eventId == 1) {
                    final ItemStack blank = this.tileAdvBench.getStackInSlot(9);
                    if (blank != null && blank.getItem() == RedPowerBase.itemPlanBlank) {
                        final ItemStack plan = new ItemStack(RedPowerBase.itemPlanFull);
                        plan.stackTagCompound = new NBTTagCompound();
                        final NBTTagCompound result = new NBTTagCompound();
                        this.craftResult.getStackInSlot(0).writeToNBT(result);
                        plan.stackTagCompound.setTag("result", result);
                        final NBTTagList requires = new NBTTagList();
                        for (int i = 0; i < 9; ++i) {
                            final ItemStack is1 = this.craftMatrix.getStackInSlot(i);
                            if (is1 != null) {
                                final ItemStack ist = CoreLib.copyStack(is1, 1);
                                final NBTTagCompound item = new NBTTagCompound();
                                ist.writeToNBT(item);
                                item.setByte("Slot", (byte)i);
                                requires.appendTag(item);
                            }
                        }
                        plan.stackTagCompound.setTag("requires", requires);
                        this.tileAdvBench.setInventorySlotContents(9, plan);
                    }
                }
            }
            catch (Throwable t) {}
        }
    }
    
    public static class ContainerNull extends Container
    {
        public boolean canInteractWith(final EntityPlayer player) {
            return false;
        }
        
        public void onCraftMatrixChanged(final IInventory inv) {
        }
        
        public ItemStack slotClick(final int a, final int b, final int c, final EntityPlayer player) {
            if (!this.canInteractWith(player)) {
                return null;
            }
            return super.slotClick(a, b, c, player);
        }
    }
    
    public class InventorySubUpdate implements IInventory
    {
        int size;
        int start;
        IInventory parent;
        
        public InventorySubUpdate(final IInventory par, final int st, final int sz) {
            this.parent = par;
            this.start = st;
            this.size = sz;
        }
        
        public int getSizeInventory() {
            return this.size;
        }
        
        public ItemStack getStackInSlot(final int idx) {
            return this.parent.getStackInSlot(idx + this.start);
        }
        
        public ItemStack decrStackSize(final int idx, final int num) {
            final ItemStack tr = this.parent.decrStackSize(idx + this.start, num);
            if (tr != null) {
                ContainerAdvBench.this.onCraftMatrixChanged(this);
            }
            return tr;
        }
        
        public ItemStack getStackInSlotOnClosing(final int idx) {
            return this.parent.getStackInSlotOnClosing(idx + this.start);
        }
        
        public void setInventorySlotContents(final int idx, final ItemStack ist) {
            this.parent.setInventorySlotContents(idx + this.start, ist);
            ContainerAdvBench.this.onCraftMatrixChanged(this);
        }
        
        public String getInventoryName() {
            return this.parent.getInventoryName();
        }
        
        public int getInventoryStackLimit() {
            return this.parent.getInventoryStackLimit();
        }
        
        public void markDirty() {
            ContainerAdvBench.this.onCraftMatrixChanged(this);
            this.parent.markDirty();
        }
        
        public boolean isUseableByPlayer(final EntityPlayer player) {
            return false;
        }
        
        public void openInventory() {
        }
        
        public void closeInventory() {
        }
        
        public boolean hasCustomInventoryName() {
            return true;
        }
        
        public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
            return true;
        }
    }
    
    public static class SlotPlan extends Slot
    {
        public SlotPlan(final IInventory inv, final int i, final int j, final int k) {
            super(inv, i, j, k);
        }
        
        public boolean isItemValid(final ItemStack ist) {
            return ist.getItem() == RedPowerBase.itemPlanBlank || ist.getItem() == RedPowerBase.itemPlanFull;
        }
        
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
