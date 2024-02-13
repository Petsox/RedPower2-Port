//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import com.eloraam.redpower.core.*;

public class SlotCraftRefill extends SlotCrafting
{
    private IInventory inv;
    private IInventory craftingMatrix;
    private ContainerAdvBench bench;
    
    public SlotCraftRefill(final EntityPlayer player, final IInventory matrix, final IInventory result, final IInventory all, final ContainerAdvBench evh, final int id, final int x, final int y) {
        super(player, matrix, result, id, x, y);
        this.inv = all;
        this.craftingMatrix = matrix;
        this.bench = evh;
    }
    
    private int findMatch(final ItemStack a) {
        for (int i = 0; i < 18; ++i) {
            final ItemStack test = this.inv.getStackInSlot(10 + i);
            if (test != null && test.stackSize != 0 && CoreLib.matchItemStackOre(a, test)) {
                return 10 + i;
            }
        }
        return -1;
    }
    
    public boolean isLastUse() {
        int bits = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack test = this.inv.getStackInSlot(i);
            if (test == null) {
                bits |= 1 << i;
            }
            else if (!test.isStackable()) {
                bits |= 1 << i;
            }
            else if (test.stackSize > 1) {
                bits |= 1 << i;
            }
        }
        if (bits == 511) {
            return false;
        }
        for (int i = 0; i < 18; ++i) {
            final ItemStack test = this.inv.getStackInSlot(10 + i);
            if (test != null && test.stackSize != 0) {
                int sc = test.stackSize;
                for (int j = 0; j < 9; ++j) {
                    if ((bits & 1 << j) <= 0) {
                        final ItemStack st = this.inv.getStackInSlot(j);
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
        return bits != 511;
    }
    
    public void onPickupFromSlot(final EntityPlayer player, final ItemStack ist) {
        final ItemStack[] plan = this.bench.getPlanItems();
        final ItemStack[] cur = new ItemStack[9];
        for (int i = 0; i < 9; ++i) {
            final ItemStack idx = this.inv.getStackInSlot(i);
            if (idx == null) {
                cur[i] = null;
            }
            else {
                cur[i] = idx.copy();
            }
        }
        final boolean lastUse = this.isLastUse();
        if (plan != null) {
            for (int j = 0; j < 9; ++j) {
                if (cur[j] == null && plan[j] != null) {
                    final int m = this.findMatch(plan[j]);
                    if (m >= 0) {
                        final ItemStack ch = this.inv.getStackInSlot(m);
                        if (ch != null) {
                            this.inv.decrStackSize(m, 1);
                            if (ch.getItem().getContainerItem() != null) {
                                final ItemStack s = ch.getItem().getContainerItem(ch);
                                this.inv.setInventorySlotContents(m, s);
                            }
                        }
                    }
                }
            }
        }
        super.onPickupFromSlot(player, ist);
        if (!lastUse) {
            for (int j = 0; j < 9; ++j) {
                if (cur[j] != null) {
                    final ItemStack nsl = this.inv.getStackInSlot(j);
                    if (plan == null || plan[j] == null) {
                        if (nsl != null) {
                            if (!CoreLib.matchItemStackOre(nsl, cur[j]) && cur[j].getItem().getContainerItem() != null) {
                                final ItemStack ctr = cur[j].getItem().getContainerItem(cur[j]);
                                if (ctr != null && ctr.getItem() == nsl.getItem()) {
                                    final int id = this.findMatch(cur[j]);
                                    if (id >= 0) {
                                        this.inv.setInventorySlotContents(id, nsl);
                                    }
                                }
                            }
                        }
                        else {
                            final int id2 = this.findMatch(cur[j]);
                            if (id2 >= 0) {
                                this.inv.setInventorySlotContents(j, this.inv.decrStackSize(id2, 1));
                            }
                        }
                    }
                }
            }
        }
        this.bench.onCraftMatrixChanged(this.craftingMatrix);
    }
}
