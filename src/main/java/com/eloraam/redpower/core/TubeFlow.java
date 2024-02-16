
package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import java.util.*;

public abstract class TubeFlow
{
    public LinkedList<TubeItem> contents;
    
    public TubeFlow() {
        this.contents = new LinkedList<TubeItem>();
    }
    
    public abstract boolean schedule(final TubeItem p0, final TubeScheduleContext p1);
    
    public boolean handleItem(final TubeItem item, final TubeScheduleContext context) {
        return false;
    }
    
    public abstract TileEntity getParent();
    
    public boolean update() {
        boolean hasChanged = false;
        if (this.contents.size() == 0) {
            return false;
        }
        final TubeScheduleContext tsc = new TubeScheduleContext(this.getParent());
        tsc.tii = this.contents.iterator();
        while (tsc.tii.hasNext()) {
            final TubeItem tubeItem = tsc.tii.next();
            tubeItem.progress = (short)(tubeItem.progress + tubeItem.power + 16);
            if (tubeItem.progress >= 128) {
                if (tubeItem.power > 0) {
                    final TubeItem tubeItem2 = tubeItem;
                    --tubeItem2.power;
                }
                hasChanged = true;
                if (!tubeItem.scheduled) {
                    if (this.schedule(tubeItem, tsc)) {
                        continue;
                    }
                    tsc.tii.remove();
                }
                else {
                    tsc.tii.remove();
                    if (tsc.world.isRemote) {
                        continue;
                    }
                    tsc.tir.add(tubeItem);
                }
            }
        }
        if (tsc.world.isRemote) {}
        for (final TubeItem ti : tsc.tir) {
            if (ti.side >= 0 && (tsc.cons & 1 << ti.side) != 0x0) {
                (tsc.dest = tsc.wc.copy()).step(ti.side);
                final ITubeConnectable itc = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)tsc.world, tsc.dest, (Class)ITubeConnectable.class);
                if (itc instanceof ITubeFlow) {
                    final ITubeFlow itf = (ITubeFlow)itc;
                    itf.addTubeItem(ti);
                }
                else {
                    if ((itc != null && itc.tubeItemEnter((ti.side ^ 0x1) & 0x3F, (int)ti.mode, ti)) || this.handleItem(ti, tsc)) {
                        continue;
                    }
                    ti.progress = 0;
                    ti.scheduled = false;
                    ti.mode = 2;
                    this.contents.add(ti);
                }
            }
            else if (tsc.cons == 0) {
                MachineLib.ejectItem(tsc.world, tsc.wc, ti.item, 1);
            }
            else {
                ti.side = (byte)Integer.numberOfTrailingZeros(tsc.cons);
                ti.progress = 128;
                ti.scheduled = false;
                this.contents.add(ti);
                hasChanged = true;
            }
        }
        return hasChanged;
    }
    
    public void add(final TubeItem ti) {
        ti.progress = 0;
        ti.scheduled = false;
        this.contents.add(ti);
    }
    
    public void onRemove() {
        final TileEntity te = this.getParent();
        for (final TubeItem ti : this.contents) {
            if (ti != null && ti.item.stackSize > 0) {
                CoreLib.dropItem(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord, ti.item);
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        final NBTTagList items = tag.getTagList("Items", 10);
        if (items.tagCount() > 0) {
            this.contents = new LinkedList<TubeItem>();
            for (int i = 0; i < items.tagCount(); ++i) {
                final NBTTagCompound item = items.getCompoundTagAt(i);
                this.contents.add(TubeItem.newFromNBT(item));
            }
        }
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        final NBTTagList items = new NBTTagList();
        if (this.contents != null) {
            for (final TubeItem ti : this.contents) {
                final NBTTagCompound item = new NBTTagCompound();
                ti.writeToNBT(item);
                items.appendTag((NBTBase)item);
            }
        }
        tag.setTag("Items", (NBTBase)items);
    }
    
    public static class TubeScheduleContext
    {
        public World world;
        public WorldCoord wc;
        public int cons;
        public List<TubeItem> tir;
        public Iterator<TubeItem> tii;
        public WorldCoord dest;
        
        public TubeScheduleContext(final TileEntity te) {
            this.tir = new ArrayList<TubeItem>();
            this.dest = null;
            this.world = te.getWorldObj();
            this.wc = new WorldCoord(te);
            this.cons = TubeLib.getConnections((IBlockAccess)this.world, this.wc.x, this.wc.y, this.wc.z);
        }
    }
}
