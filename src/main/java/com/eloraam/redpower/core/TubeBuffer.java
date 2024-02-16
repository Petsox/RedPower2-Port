
package com.eloraam.redpower.core;

import net.minecraft.item.*;
import java.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.nbt.*;

public class TubeBuffer
{
    LinkedList<TubeItem> buffer;
    public boolean plugged;
    
    public TubeBuffer() {
        this.buffer = null;
        this.plugged = false;
    }
    
    public boolean isEmpty() {
        return this.buffer == null || this.buffer.size() == 0;
    }
    
    public TubeItem getLast() {
        return (this.buffer == null) ? null : this.buffer.getLast();
    }
    
    public void add(final TubeItem ti) {
        if (this.buffer == null) {
            this.buffer = new LinkedList<TubeItem>();
        }
        this.buffer.addFirst(ti);
    }
    
    public void addNew(final ItemStack ist) {
        if (this.buffer == null) {
            this.buffer = new LinkedList<TubeItem>();
        }
        this.buffer.addFirst(new TubeItem(0, ist));
    }
    
    public void addNewColor(final ItemStack ist, final int col) {
        if (this.buffer == null) {
            this.buffer = new LinkedList<TubeItem>();
        }
        final TubeItem ti = new TubeItem(0, ist);
        ti.color = (byte)col;
        this.buffer.addFirst(ti);
    }
    
    public void addAll(final Collection<ItemStack> col) {
        if (this.buffer == null) {
            this.buffer = new LinkedList<TubeItem>();
        }
        for (final ItemStack ist : col) {
            this.buffer.add(new TubeItem(0, ist));
        }
    }
    
    public void addBounce(final TubeItem ti) {
        if (this.buffer == null) {
            this.buffer = new LinkedList<TubeItem>();
        }
        this.buffer.addLast(ti);
        this.plugged = true;
    }
    
    public void pop() {
        this.buffer.removeLast();
        if (this.buffer.size() == 0) {
            this.plugged = false;
        }
    }
    
    public int size() {
        return (this.buffer == null) ? 0 : this.buffer.size();
    }
    
    public void onRemove(final TileEntity te) {
        if (this.buffer != null) {
            for (final TubeItem ti : this.buffer) {
                if (ti != null && ti.item.stackSize > 0) {
                    CoreLib.dropItem(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord, ti.item);
                }
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        final NBTTagList items = data.getTagList("Buffer", 10);
        if (items.tagCount() > 0) {
            this.buffer = new LinkedList<TubeItem>();
            for (int b = 0; b < items.tagCount(); ++b) {
                final NBTTagCompound item = items.getCompoundTagAt(b);
                this.buffer.add(TubeItem.newFromNBT(item));
            }
        }
        final byte var5 = data.getByte("Plug");
        this.plugged = (var5 > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        final NBTTagList items = new NBTTagList();
        if (this.buffer != null) {
            for (final TubeItem ti : this.buffer) {
                final NBTTagCompound item = new NBTTagCompound();
                ti.writeToNBT(item);
                items.appendTag((NBTBase)item);
            }
        }
        data.setTag("Buffer", (NBTBase)items);
        data.setByte("Plug", (byte)(byte)(this.plugged ? 1 : 0));
    }
}
