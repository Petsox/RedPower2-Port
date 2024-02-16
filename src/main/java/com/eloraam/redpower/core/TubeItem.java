
package com.eloraam.redpower.core;

import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class TubeItem
{
    public short progress;
    public byte mode;
    public byte side;
    public byte color;
    public short power;
    public boolean scheduled;
    public short priority;
    public ItemStack item;
    
    public TubeItem() {
        this.progress = 0;
        this.mode = 1;
        this.color = 0;
        this.power = 0;
        this.scheduled = false;
        this.priority = 0;
    }
    
    public TubeItem(final int s, final ItemStack stk) {
        this.progress = 0;
        this.mode = 1;
        this.color = 0;
        this.power = 0;
        this.scheduled = false;
        this.priority = 0;
        this.item = stk;
        this.side = (byte)s;
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        this.item = ItemStack.loadItemStackFromNBT(nbt);
        this.side = nbt.getByte("side");
        this.progress = nbt.getShort("pos");
        this.mode = nbt.getByte("mode");
        this.color = nbt.getByte("col");
        this.priority = nbt.getShort("prio");
        if (this.progress < 0) {
            this.scheduled = true;
            this.progress = (short)(-this.progress - 1);
        }
        this.power = (short)(nbt.getByte("pow") & 0xFF);
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        this.item.writeToNBT(nbt);
        nbt.setByte("side", this.side);
        nbt.setShort("pos", (short)(this.scheduled ? (-this.progress - 1) : this.progress));
        nbt.setByte("mode", this.mode);
        nbt.setByte("col", this.color);
        nbt.setByte("pow", (byte)this.power);
        nbt.setShort("prio", this.priority);
    }
    
    public static TubeItem newFromNBT(final NBTTagCompound nbt) {
        final TubeItem ti = new TubeItem();
        ti.readFromNBT(nbt);
        return ti;
    }
    
    public void readFromPacket(final NBTTagCompound tag) {
        this.side = tag.getByte("side");
        this.progress = tag.getByte("pos");
        if (this.progress < 0) {
            this.scheduled = true;
            this.progress = (short)(-this.progress - 1);
        }
        this.color = tag.getByte("col");
        this.power = tag.getByte("pow");
        this.item = ItemStack.loadItemStackFromNBT((NBTTagCompound)tag.getTag("itm"));
    }
    
    public void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("side", this.side);
        tag.setByte("pos", (byte)(this.scheduled ? (-this.progress - 1) : ((long)this.progress)));
        tag.setByte("col", this.color);
        tag.setByte("pow", (byte)this.power);
        final NBTTagCompound itag = new NBTTagCompound();
        this.item.writeToNBT(itag);
        tag.setTag("itm", (NBTBase)itag);
    }
    
    public static TubeItem newFromPacket(final NBTTagCompound tag) {
        final TubeItem ti = new TubeItem();
        ti.readFromPacket(tag);
        return ti;
    }
}
