
package com.eloraam.redpower.core;

import net.minecraft.world.*;
import net.minecraft.inventory.*;
import java.util.stream.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.util.*;
import net.minecraft.item.*;
import java.util.*;
import org.apache.commons.lang3.*;
import net.minecraft.entity.player.*;

public class MachineLib
{
    public static IInventory getInventory(final World world, final WorldCoord wc) {
        final IInventory inv = (IInventory)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)IInventory.class);
        if (!(inv instanceof TileEntityChest)) {
            return inv;
        }
        TileEntityChest tec = (TileEntityChest)CoreLib.getTileEntity((IBlockAccess)world, wc.x - 1, wc.y, wc.z, (Class)TileEntityChest.class);
        if (tec != null) {
            return (IInventory)new InventoryLargeChest("Large chest", (IInventory)tec, inv);
        }
        tec = (TileEntityChest)CoreLib.getTileEntity((IBlockAccess)world, wc.x + 1, wc.y, wc.z, (Class)TileEntityChest.class);
        if (tec != null) {
            return (IInventory)new InventoryLargeChest("Large chest", inv, (IInventory)tec);
        }
        tec = (TileEntityChest)CoreLib.getTileEntity((IBlockAccess)world, wc.x, wc.y, wc.z - 1, (Class)TileEntityChest.class);
        if (tec != null) {
            return (IInventory)new InventoryLargeChest("Large chest", (IInventory)tec, inv);
        }
        tec = (TileEntityChest)CoreLib.getTileEntity((IBlockAccess)world, wc.x, wc.y, wc.z + 1, (Class)TileEntityChest.class);
        return (IInventory)((tec != null) ? new InventoryLargeChest("Large chest", inv, (IInventory)tec) : inv);
    }
    
    public static IInventory getSideInventory(final World world, final WorldCoord wc, final int side, final boolean push) {
        final IInventory inv = getInventory(world, wc);
        if (inv == null) {
            return null;
        }
        if (inv instanceof ISidedInventory) {
            final ISidedInventory isi = (ISidedInventory)inv;
            final int[] slots = isi.getAccessibleSlotsFromSide(side);
            return (IInventory)new SubInventory(inv, slots);
        }
        return inv;
    }
    
    public static boolean addToInventoryCore(final World world, final ItemStack ist, final WorldCoord wc, final int side, final boolean act) {
        final IInventory inv = getInventory(world, wc);
        if (inv == null) {
            return false;
        }
        int[] slots;
        if (inv instanceof ISidedInventory) {
            final ISidedInventory isi = (ISidedInventory)inv;
            slots = isi.getAccessibleSlotsFromSide(side);
            if (slots == null || slots.length == 0) {
                return false;
            }
            for (final int n : slots) {
                if (isi.canInsertItem(n, ist, side)) {
                    return addToInventoryCore(inv, ist, slots, act);
                }
            }
            return false;
        }
        else {
            slots = IntStream.range(0, inv.getSizeInventory()).toArray();
        }
        return addToInventoryCore(inv, ist, slots, act);
    }
    
    public static boolean addToInventoryCore(final IInventory inv, final ItemStack ist, final int[] slots, final boolean act) {
        for (final int n : slots) {
            final ItemStack invst = inv.getStackInSlot(n);
            if (invst != null) {
                if (ist.isItemEqual(invst) && ItemStack.areItemStackTagsEqual(ist, invst)) {
                    int dfs = Math.min(invst.getMaxStackSize(), inv.getInventoryStackLimit());
                    dfs -= invst.stackSize;
                    if (dfs > 0) {
                        final int si = Math.min(dfs, ist.stackSize);
                        if (!act) {
                            return true;
                        }
                        final ItemStack itemStack = invst;
                        itemStack.stackSize += si;
                        inv.setInventorySlotContents(n, invst);
                        ist.stackSize -= si;
                        if (ist.stackSize == 0) {
                            return true;
                        }
                    }
                }
            }
            else if (!act) {
                return true;
            }
        }
        if (!act) {
            return false;
        }
        for (final int n : slots) {
            final ItemStack invst = inv.getStackInSlot(n);
            if (invst == null) {
                if (inv.getInventoryStackLimit() >= ist.stackSize) {
                    inv.setInventorySlotContents(n, ist);
                    return true;
                }
                inv.setInventorySlotContents(n, ist.splitStack(inv.getInventoryStackLimit()));
            }
        }
        return false;
    }
    
    public static boolean addToInventory(final World world, final ItemStack ist, final WorldCoord wc, final int side) {
        return addToInventoryCore(world, ist, wc, side, true);
    }
    
    public static boolean canAddToInventory(final World world, final ItemStack ist, final WorldCoord wc, final int side) {
        return addToInventoryCore(world, ist, wc, side, false);
    }
    
    public static void ejectItem(final World world, WorldCoord wc, final ItemStack ist, final int dir) {
        wc = wc.copy();
        wc.step(dir);
        final EntityItem item = new EntityItem(world, wc.x + 0.5, wc.y + 0.5, wc.z + 0.5, ist);
        item.motionX = 0.0;
        item.motionY = 0.0;
        item.motionZ = 0.0;
        switch (dir) {
            case 0: {
                item.motionY = -0.3;
                break;
            }
            case 1: {
                item.motionY = 0.3;
                break;
            }
            case 2: {
                item.motionZ = -0.3;
                break;
            }
            case 3: {
                item.motionZ = 0.3;
                break;
            }
            case 4: {
                item.motionX = -0.3;
                break;
            }
            default: {
                item.motionX = 0.3;
                break;
            }
        }
        item.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld((Entity)item);
    }
    
    public static boolean handleItem(final World world, final ItemStack ist, final WorldCoord wc, final int side) {
        final WorldCoord dest = wc.copy();
        dest.step(side);
        if (ist.stackSize == 0) {
            return true;
        }
        if (TubeLib.addToTubeRoute(world, ist, wc, dest, side ^ 0x1)) {
            return true;
        }
        if (addToInventory(world, ist, dest, (side ^ 0x1) & 0x3F)) {
            return true;
        }
        final TileEntity te = (TileEntity)CoreLib.getTileEntity((IBlockAccess)world, dest, (Class)TileEntity.class);
        if (te instanceof IInventory || te instanceof ITubeConnectable) {
            return false;
        }
        if (world.getBlock(dest.x, dest.y, dest.z).isSideSolid((IBlockAccess)world, dest.x, dest.y, dest.z, ForgeDirection.getOrientation(side ^ 0x1))) {
            return false;
        }
        ejectItem(world, wc, ist, side);
        return true;
    }
    
    public static boolean handleItem(final World world, final TubeItem ti, final WorldCoord wc, final int side) {
        final WorldCoord dest = wc.copy();
        dest.step(side);
        if (ti.item.stackSize == 0) {
            return true;
        }
        if (TubeLib.addToTubeRoute(world, ti, wc, dest, side ^ 0x1)) {
            return true;
        }
        if (addToInventory(world, ti.item, dest, (side ^ 0x1) & 0x3F)) {
            return true;
        }
        final TileEntity te = (TileEntity)CoreLib.getTileEntity((IBlockAccess)world, dest, (Class)TileEntity.class);
        if (te instanceof IInventory || te instanceof ITubeConnectable) {
            return false;
        }
        if (world.getBlock(dest.x, dest.y, dest.z).isSideSolid((IBlockAccess)world, dest.x, dest.y, dest.z, ForgeDirection.getOrientation(side ^ 0x1))) {
            return false;
        }
        ejectItem(world, wc, ti.item, side);
        return true;
    }
    
    public static int compareItem(final ItemStack a, final ItemStack b) {
        if (Item.getIdFromItem(a.getItem()) != Item.getIdFromItem(b.getItem())) {
            return Item.getIdFromItem(a.getItem()) - Item.getIdFromItem(b.getItem());
        }
        if (a.getItemDamage() == b.getItemDamage()) {
            return 0;
        }
        if (a.getItem().getHasSubtypes()) {
            return a.getItemDamage() - b.getItemDamage();
        }
        final int d1 = (a.getItemDamage() <= 1) ? -1 : ((a.getItemDamage() == a.getMaxDamage() - 1) ? 1 : 0);
        final int d2 = (b.getItemDamage() <= 1) ? -1 : ((b.getItemDamage() == b.getMaxDamage() - 1) ? 1 : 0);
        return d1 - d2;
    }
    
    public static FilterMap makeFilterMap(final ItemStack[] ist) {
        return new FilterMap(ist);
    }
    
    public static FilterMap makeFilterMap(final ItemStack[] ist, final int st, final int ln) {
        final ItemStack[] it = new ItemStack[ln];
        System.arraycopy(ist, st, it, 0, ln);
        return new FilterMap(it);
    }
    
    public static int[] genMatchCounts(final FilterMap map) {
        final int[] tr = new int[map.filter.length];
        for (int n = 0; n < map.filter.length; ++n) {
            final ItemStack ist = map.filter[n];
            if (ist != null && ist.stackSize != 0) {
                final List<Integer> arl = map.map.get(ist);
                if (arl != null && arl.get(0) == n) {
                    for (final int m : arl) {
                        final int[] array = tr;
                        final int n2 = n;
                        array[n2] += map.filter[m].stackSize;
                    }
                }
            }
        }
        return tr;
    }
    
    public static int decMatchCount(final FilterMap map, final int[] mc, final ItemStack ist) {
        final List<Integer> arl = map.map.get(ist);
        if (arl == null) {
            return 0;
        }
        final int n = arl.get(0);
        final int tr = Math.min(mc[n], ist.stackSize);
        final int n2 = n;
        mc[n2] -= tr;
        return tr;
    }
    
    public static int getMatchCount(final FilterMap map, final int[] mc, final ItemStack ist) {
        final List<Integer> arl = map.map.get(ist);
        if (arl == null) {
            return 0;
        }
        final int n = arl.get(0);
        return Math.min(mc[n], ist.stackSize);
    }
    
    public static boolean isMatchEmpty(final int[] mc) {
        for (final int i : mc) {
            if (i > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void decMatchCounts(final FilterMap map, final int[] mc, final IInventory inv, final int[] slots) {
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                decMatchCount(map, mc, ist);
            }
        }
    }
    
    public static boolean matchOneStack(final FilterMap map, final IInventory inv, final int[] slots, final int pos) {
        final ItemStack match = map.filter[pos];
        int fc = (match == null) ? 1 : match.stackSize;
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                if (match == null) {
                    return true;
                }
                if (compareItem(match, ist) == 0) {
                    final int m = Math.min(ist.stackSize, fc);
                    fc -= m;
                    if (fc <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static int matchAnyStack(final FilterMap map, final IInventory inv, final int[] slots) {
        final int[] mc = new int[map.filter.length];
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                final List<Integer> arl = map.map.get(ist);
                if (arl != null) {
                    for (final int m : arl) {
                        final int[] array = mc;
                        final int n2 = m;
                        array[n2] += ist.stackSize;
                        if (mc[m] >= map.filter[m].stackSize) {
                            return m;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static int matchAnyStackCol(final FilterMap map, final IInventory inv, final int[] slots, final int col) {
        final int[] mc = new int[5];
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                final List<Integer> arl = map.map.get(ist);
                if (arl != null) {
                    for (final Integer m : arl) {
                        if ((m & 0x7) == col) {
                            final int s = m >> 3;
                            final int[] array = mc;
                            final int n2 = s;
                            array[n2] += ist.stackSize;
                            if (mc[s] >= map.filter[m].stackSize) {
                                return m;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public static boolean matchAllCol(final FilterMap map, final IInventory inv, final int[] slots, final int col) {
        final int[] mc = new int[5];
        for (final int any : slots) {
            final ItemStack n = inv.getStackInSlot(any);
            if (n != null && n.stackSize != 0) {
                final ArrayList<Integer> ct = map.map.get(n);
                if (ct != null) {
                    int ss = n.stackSize;
                    for (final Integer m : ct) {
                        if ((m & 0x7) == col) {
                            final int c = m >> 3;
                            final int s1 = Math.min(ss, map.filter[m].stackSize - mc[c]);
                            final int[] array = mc;
                            final int n2 = c;
                            array[n2] += s1;
                            ss -= s1;
                            if (ss == 0) {
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        boolean match = false;
        for (int i = 0; i < 5; ++i) {
            final ItemStack stack = map.filter[i * 8 + col];
            if (stack != null && stack.stackSize != 0) {
                match = true;
                if (stack.stackSize > mc[i]) {
                    return false;
                }
            }
        }
        return match;
    }
    
    public static boolean emptyInventory(final IInventory inv, final int[] slots) {
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static ItemStack collectOneStack(final IInventory inv, final int[] slots, final ItemStack match) {
        ItemStack tr = null;
        int fc = (match == null) ? 1 : match.stackSize;
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                if (match == null) {
                    inv.setInventorySlotContents(n, (ItemStack)null);
                    return ist;
                }
                if (compareItem(match, ist) == 0) {
                    final int m = Math.min(ist.stackSize, fc);
                    if (tr == null) {
                        tr = inv.decrStackSize(n, m);
                    }
                    else {
                        inv.decrStackSize(n, m);
                        final ItemStack itemStack = tr;
                        itemStack.stackSize += m;
                    }
                    fc -= m;
                    if (fc <= 0) {
                        break;
                    }
                }
            }
        }
        return tr;
    }
    
    public static ItemStack collectOneStackFuzzy(final IInventory inv, final int[] slots, final ItemStack match) {
        ItemStack tr = null;
        int fc = (match == null) ? 1 : match.getMaxStackSize();
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                if (match == null) {
                    inv.setInventorySlotContents(n, (ItemStack)null);
                    return ist;
                }
                if (compareItem(match, ist) == 0) {
                    final int m = Math.min(ist.stackSize, fc);
                    if (tr == null) {
                        tr = inv.decrStackSize(n, m);
                    }
                    else {
                        inv.decrStackSize(n, m);
                        final ItemStack itemStack = tr;
                        itemStack.stackSize += m;
                    }
                    fc -= m;
                    if (fc <= 0) {
                        break;
                    }
                }
            }
        }
        return tr;
    }
    
    public static class FilterMap
    {
        protected TreeMap<ItemStack, ArrayList<Integer>> map;
        protected ItemStack[] filter;
        
        public FilterMap(final ItemStack[] filt) {
            this.filter = filt;
            this.map = new TreeMap<ItemStack, ArrayList<Integer>>(MachineLib::compareItem);
            for (int i = 0; i < filt.length; ++i) {
                if (filt[i] != null && filt[i].stackSize != 0) {
                    this.map.computeIfAbsent(filt[i], k -> new ArrayList()).add(i);
                }
            }
        }
        
        public int size() {
            return this.map.size();
        }
        
        public boolean containsKey(final ItemStack ist) {
            return this.map.containsKey(ist);
        }
        
        public int firstMatch(final ItemStack ist) {
            final ArrayList<Integer> arl = this.map.get(ist);
            return (arl == null) ? -1 : arl.get(0);
        }
    }
    
    public static class SubInventory implements IInventory
    {
        IInventory parent;
        int[] slots;
        
        SubInventory(final IInventory par, final int[] sl) {
            this.parent = par;
            this.slots = sl;
        }
        
        public int getSizeInventory() {
            return this.slots.length;
        }
        
        public ItemStack getStackInSlot(final int idx) {
            if (ArrayUtils.contains(this.slots, idx)) {
                return this.parent.getStackInSlot(idx);
            }
            return null;
        }
        
        public ItemStack decrStackSize(final int idx, final int num) {
            if (ArrayUtils.contains(this.slots, idx)) {
                return this.parent.decrStackSize(idx, num);
            }
            return null;
        }
        
        public ItemStack getStackInSlotOnClosing(final int idx) {
            if (ArrayUtils.contains(this.slots, idx)) {
                return this.parent.getStackInSlotOnClosing(idx);
            }
            return null;
        }
        
        public void setInventorySlotContents(final int idx, final ItemStack ist) {
            if (ArrayUtils.contains(this.slots, idx)) {
                this.parent.setInventorySlotContents(idx, ist);
            }
        }
        
        public String getInventoryName() {
            return this.parent.getInventoryName();
        }
        
        public int getInventoryStackLimit() {
            return this.parent.getInventoryStackLimit();
        }
        
        public void markDirty() {
            this.parent.markDirty();
        }
        
        public boolean isUseableByPlayer(final EntityPlayer player) {
            return this.parent.isUseableByPlayer(player);
        }
        
        public void openInventory() {
        }
        
        public void closeInventory() {
        }
        
        public boolean hasCustomInventoryName() {
            return this.parent.hasCustomInventoryName();
        }
        
        public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
            return this.parent.isItemValidForSlot(slotID, stack);
        }
    }
}
