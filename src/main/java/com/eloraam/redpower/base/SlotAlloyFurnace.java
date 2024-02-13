//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import com.eloraam.redpower.core.*;

public class SlotAlloyFurnace extends Slot
{
    private EntityPlayer thePlayer;
    int totalCrafted;
    
    public SlotAlloyFurnace(final EntityPlayer player, final IInventory inv, final int x, final int y, final int z) {
        super(inv, x, y, z);
        this.thePlayer = player;
    }
    
    public boolean isItemValid(final ItemStack ist) {
        return false;
    }
    
    public ItemStack decrStackSize(final int num) {
        if (this.getHasStack()) {
            this.totalCrafted += Math.min(num, this.getStack().stackSize);
        }
        return super.decrStackSize(num);
    }
    
    public void onPickupFromSlot(final EntityPlayer player, final ItemStack ist) {
        this.onCrafting(ist);
        super.onPickupFromSlot(player, ist);
    }
    
    protected void onCrafting(final ItemStack ist, final int num) {
        this.totalCrafted += num;
        this.onCrafting(ist);
    }
    
    protected void onCrafting(final ItemStack ist) {
        ist.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.totalCrafted);
        this.totalCrafted = 0;
        AchieveLib.onAlloy(this.thePlayer, ist);
    }
}
