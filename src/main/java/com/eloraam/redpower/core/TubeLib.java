
package com.eloraam.redpower.core;

import net.minecraft.inventory.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.item.*;
import java.util.stream.*;
import java.util.*;

public class TubeLib
{
    private static Set<List<Integer>> tubeClassMapping;
    
    public static void addCompatibleMapping(final int a, final int b) {
        TubeLib.tubeClassMapping.add(Arrays.asList(a, b));
        TubeLib.tubeClassMapping.add(Arrays.asList(b, a));
    }
    
    public static boolean isCompatible(final int a, final int b) {
        return a == b || TubeLib.tubeClassMapping.contains(Arrays.asList(a, b));
    }
    
    private static boolean isConSide(final IBlockAccess iba, final int x, final int y, final int z, final int col, final int side) {
        final TileEntity te = iba.getTileEntity(x, y, z);
        if (!(te instanceof ITubeConnectable)) {
            if (isCompatible(col, 0) && te instanceof IInventory) {
                if (!(te instanceof ISidedInventory)) {
                    return true;
                }
                final ISidedInventory isi = (ISidedInventory)te;
                if (isi.getSizeInventory() > 0) {
                    final int[] slots = isi.getAccessibleSlotsFromSide(side);
                    return slots != null && slots.length > 0;
                }
            }
            return false;
        }
        final ITubeConnectable itc = (ITubeConnectable)te;
        if (!isCompatible(col, itc.getTubeConClass())) {
            return false;
        }
        final int sides = itc.getTubeConnectableSides();
        return (sides & 1 << side) > 0;
    }
    
    public static int getConnections(final IBlockAccess iba, final int x, final int y, final int z) {
        final ITubeConnectable itc = (ITubeConnectable)CoreLib.getTileEntity(iba, x, y, z, (Class)ITubeConnectable.class);
        if (itc == null) {
            return 0;
        }
        int trs = 0;
        final int col = itc.getTubeConClass();
        final int sides = itc.getTubeConnectableSides();
        if ((sides & 0x1) > 0 && isConSide(iba, x, y - 1, z, col, 1)) {
            trs |= 0x1;
        }
        if ((sides & 0x2) > 0 && isConSide(iba, x, y + 1, z, col, 0)) {
            trs |= 0x2;
        }
        if ((sides & 0x4) > 0 && isConSide(iba, x, y, z - 1, col, 3)) {
            trs |= 0x4;
        }
        if ((sides & 0x8) > 0 && isConSide(iba, x, y, z + 1, col, 2)) {
            trs |= 0x8;
        }
        if ((sides & 0x10) > 0 && isConSide(iba, x - 1, y, z, col, 5)) {
            trs |= 0x10;
        }
        if ((sides & 0x20) > 0 && isConSide(iba, x + 1, y, z, col, 4)) {
            trs |= 0x20;
        }
        return trs;
    }
    
    public static int findRoute(final World world, final WorldCoord wc, final TubeItem te, final int sides, final int state) {
        final OutRouteFinder rf = new OutRouteFinder(world, te, state);
        return rf.find(wc, sides);
    }
    
    public static int findRoute(final World world, final WorldCoord wc, final TubeItem te, final int sides, final int state, final int start) {
        final OutRouteFinder rf = new OutRouteFinder(world, te, state);
        rf.startDir = start;
        return rf.find(wc, sides);
    }
    
    public static boolean addToTubeRoute(final World world, final ItemStack ist, final WorldCoord src, final WorldCoord wc, final int side) {
        return addToTubeRoute(world, new TubeItem(0, ist), src, wc, side);
    }
    
    public static boolean addToTubeRoute(final World world, final TubeItem ti, final WorldCoord src, final WorldCoord wc, final int side) {
        final ITubeConnectable ite = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)ITubeConnectable.class);
        if (ite == null) {
            return false;
        }
        ti.mode = 1;
        final int s = findRoute(world, src, ti, 1 << (side ^ 0x1), 1);
        return s >= 0 && ite.tubeItemEnter(side, 0, ti);
    }
    
    static {
        TubeLib.tubeClassMapping = new HashSet<List<Integer>>();
        addCompatibleMapping(0, 17);
        addCompatibleMapping(17, 18);
        for (int i = 0; i < 16; ++i) {
            addCompatibleMapping(0, 1 + i);
            addCompatibleMapping(17, 1 + i);
            addCompatibleMapping(17, 19 + i);
            addCompatibleMapping(18, 19 + i);
        }
    }
    
    public static class InRouteFinder extends RouteFinder
    {
        MachineLib.FilterMap filterMap;
        int subFilt;
        
        public InRouteFinder(final World world, final MachineLib.FilterMap map) {
            super(world);
            this.subFilt = -1;
            this.filterMap = map;
        }
        
        @Override
        public void addPoint(final WorldCoord wc, final int st, final int side, final int weight) {
            final IInventory inv = MachineLib.getInventory(super.worldObj, wc);
            if (inv == null) {
                super.addPoint(wc, st, side, weight);
            }
            else {
                final int opside = (side ^ 0x1) & 0x3F;
                int[] slots;
                if (inv instanceof ISidedInventory) {
                    final ISidedInventory sm = (ISidedInventory)inv;
                    slots = sm.getAccessibleSlotsFromSide(opside);
                }
                else {
                    slots = IntStream.range(0, inv.getSizeInventory()).toArray();
                }
                if (this.filterMap.size() == 0) {
                    if (!MachineLib.emptyInventory(inv, slots)) {
                        final WorldRoute sm2 = new WorldRoute(wc, 0, opside, weight);
                        sm2.solved = true;
                        super.scanpos.add(sm2);
                    }
                    else {
                        super.addPoint(wc, st, side, weight);
                    }
                }
                else {
                    int sm3 = -1;
                    if (this.subFilt < 0) {
                        sm3 = MachineLib.matchAnyStack(this.filterMap, inv, slots);
                    }
                    else if (MachineLib.matchOneStack(this.filterMap, inv, slots, this.subFilt)) {
                        sm3 = this.subFilt;
                    }
                    if (sm3 < 0) {
                        super.addPoint(wc, st, side, weight);
                    }
                    else {
                        final WorldRoute nr = new WorldRoute(wc, sm3, opside, weight);
                        nr.solved = true;
                        super.scanpos.add(nr);
                    }
                }
            }
        }
        
        public void setSubFilt(final int sf) {
            this.subFilt = sf;
        }
        
        public int getResultSide() {
            return super.result.side;
        }
    }
    
    private static class OutRouteFinder extends RouteFinder
    {
        int state;
        TubeItem tubeItem;
        
        public OutRouteFinder(final World world, final TubeItem ti, final int st) {
            super(world);
            this.state = st;
            this.tubeItem = ti;
        }
        
        @Override
        public void addPoint(final WorldCoord wc, final int start, final int side, final int weight) {
            final int opside = (side ^ 0x1) & 0xFF;
            if (this.state != 3 && this.tubeItem.priority == 0 && MachineLib.canAddToInventory(super.worldObj, this.tubeItem.item, wc, opside)) {
                final WorldRoute route = new WorldRoute(wc, start, side, weight);
                route.solved = true;
                super.scanpos.add(route);
            }
            else {
                final ITubeConnectable itc = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc, (Class)ITubeConnectable.class);
                if (itc != null) {
                    if (itc.tubeItemCanEnter(opside, this.state, this.tubeItem)) {
                        final WorldRoute route2 = new WorldRoute(wc, start, opside, weight + itc.tubeWeight(opside, this.state));
                        route2.solved = true;
                        super.scanpos.add(route2);
                    }
                    else if (itc.tubeItemCanEnter(opside, 0, this.tubeItem) && itc.canRouteItems() && !super.scanmap.contains(wc)) {
                        super.scanmap.add(wc);
                        super.scanpos.add(new WorldRoute(wc, start, opside, weight + itc.tubeWeight(opside, this.state)));
                    }
                }
            }
        }
    }
    
    public static class RequestRouteFinder extends RouteFinder
    {
        TubeItem tubeItem;
        
        public RequestRouteFinder(final World world, final TubeItem item) {
            super(world);
            this.tubeItem = item;
        }
        
        @Override
        public void addPoint(final WorldCoord wc, final int st, final int side, final int weight) {
            final ITubeRequest itr = (ITubeRequest)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc, (Class)ITubeRequest.class);
            if (itr != null) {
                if (itr.requestTubeItem(this.tubeItem, false)) {
                    final WorldRoute itc1 = new WorldRoute(wc, 0, side, weight);
                    itc1.solved = true;
                    super.scanpos.add(itc1);
                }
            }
            else {
                final ITubeConnectable itc2 = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc, (Class)ITubeConnectable.class);
                if (itc2 != null) {
                    final int side2 = (side ^ 0x1) & 0xFF;
                    if (itc2.tubeItemCanEnter(side2, 0, this.tubeItem) && itc2.canRouteItems() && !super.scanmap.contains(wc)) {
                        super.scanmap.add(wc);
                        super.scanpos.add(new WorldRoute(wc, st, side2, weight + itc2.tubeWeight(side2, 0)));
                    }
                }
            }
        }
    }
    
    private static class RouteFinder
    {
        int startDir;
        WorldRoute result;
        World worldObj;
        Set<WorldCoord> scanmap;
        PriorityQueue<WorldRoute> scanpos;
        
        public RouteFinder(final World world) {
            this.startDir = 0;
            this.scanmap = new HashSet<WorldCoord>();
            this.scanpos = new PriorityQueue<WorldRoute>();
            this.worldObj = world;
        }
        
        public void addPoint(final WorldCoord wc, final int start, final int side, final int weight) {
            final ITubeConnectable itc = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)this.worldObj, wc, (Class)ITubeConnectable.class);
            if (itc != null && itc.canRouteItems() && !this.scanmap.contains(wc)) {
                this.scanmap.add(wc);
                this.scanpos.add(new WorldRoute(wc, start, side ^ 0x1, weight));
            }
        }
        
        public int find(final WorldCoord wc, final int sides) {
            for (int wr = 0; wr < 6; ++wr) {
                if ((sides & 1 << wr) != 0x0) {
                    final WorldCoord cons = wc.copy();
                    cons.step(wr);
                    this.addPoint(cons, wr, wr, (wr != this.startDir) ? 1 : 0);
                }
            }
            while (this.scanpos.size() > 0) {
                final WorldRoute route = this.scanpos.poll();
                if (route.solved) {
                    this.result = route;
                    return route.start;
                }
                final int cons2 = TubeLib.getConnections((IBlockAccess)this.worldObj, route.wc.x, route.wc.y, route.wc.z);
                for (int side = 0; side < 6; ++side) {
                    if (side != route.side && (cons2 & 1 << side) != 0x0) {
                        final WorldCoord wcp = route.wc.copy();
                        wcp.step(side);
                        this.addPoint(wcp, route.start, side, route.weight + 2);
                    }
                }
            }
            return -1;
        }
        
        public WorldCoord getResultPoint() {
            return this.result.wc;
        }
    }
    
    private static class WorldRoute implements Comparable<WorldRoute>
    {
        public WorldCoord wc;
        public int start;
        public int side;
        public int weight;
        public boolean solved;
        
        public WorldRoute(final WorldCoord w, final int st, final int s, final int wt) {
            this.solved = false;
            this.wc = w;
            this.start = st;
            this.side = s;
            this.weight = wt;
        }
        
        @Override
        public int compareTo(final WorldRoute wr) {
            return this.weight - wr.weight;
        }
    }
}
