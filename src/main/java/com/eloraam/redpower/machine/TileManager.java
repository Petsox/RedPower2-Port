
package com.eloraam.redpower.machine;

import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import java.util.stream.*;
import net.minecraft.inventory.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;

public class TileManager extends TileMachine implements IBluePowerConnectable, ISidedInventory, ITubeConnectable, ITubeRequest
{
    BluePowerEndpoint cond;
    TubeBuffer buffer;
    protected ItemStack[] contents;
    public int ConMask;
    public byte color;
    public byte mode;
    public int priority;
    public byte rqnum;
    protected MachineLib.FilterMap filterMap;
    
    public TileManager() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileManager.this;
            }
        };
        this.buffer = new TubeBuffer();
        this.contents = new ItemStack[24];
        this.ConMask = -1;
        this.color = 0;
        this.mode = 0;
        this.priority = 0;
        this.rqnum = 0;
        this.filterMap = null;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 65;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)this.cond;
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != this.Rotation && side != (this.Rotation ^ 0x1)) ? IntStream.range(0, 24).toArray() : new int[0];
    }
    
    protected IInventory getConnectedInventory(final boolean push) {
        final WorldCoord pos = new WorldCoord((TileEntity)this);
        pos.step(this.Rotation ^ 0x1);
        return MachineLib.getSideInventory(super.worldObj, pos, this.Rotation, push);
    }
    
    protected void regenFilterMap() {
        this.filterMap = MachineLib.makeFilterMap(this.contents, 0, 24);
    }
    
    protected int[] getAcceptCounts() {
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            return null;
        }
        final IInventory inv = this.getConnectedInventory(true);
        if (inv == null) {
            return null;
        }
        final int[] tr = MachineLib.genMatchCounts(this.filterMap);
        final int[] slots = IntStream.range(0, inv.getSizeInventory()).toArray();
        MachineLib.decMatchCounts(this.filterMap, tr, inv, slots);
        return tr;
    }
    
    protected int acceptCount(final ItemStack ist) {
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            return 0;
        }
        if (!this.filterMap.containsKey(ist)) {
            return 0;
        }
        final int[] match = this.getAcceptCounts();
        return (match == null) ? 0 : MachineLib.getMatchCount(this.filterMap, match, ist);
    }
    
    protected void doRequest(final int slot, final int num) {
        final ItemStack rq = CoreLib.copyStack(this.contents[slot], Math.min(64, num));
        final TubeItem tir = new TubeItem(0, rq);
        tir.priority = (short)this.priority;
        tir.color = this.color;
        final TubeLib.RequestRouteFinder rrf = new TubeLib.RequestRouteFinder(super.worldObj, tir);
        if (rrf.find(new WorldCoord((TileEntity)this), 63) >= 0) {
            final WorldCoord wc = rrf.getResultPoint();
            final ITubeRequest itr = (ITubeRequest)CoreLib.getTileEntity((IBlockAccess)super.worldObj, wc, (Class)ITubeRequest.class);
            itr.requestTubeItem(tir, true);
            this.cond.drawPower(100.0);
            this.scheduleTick(20);
        }
    }
    
    protected void scanInventory() {
        final IInventory inv = this.getConnectedInventory(false);
        if (inv != null) {
            if (this.filterMap == null) {
                this.regenFilterMap();
            }
            int[] ac = MachineLib.genMatchCounts(this.filterMap);
            if (ac != null) {
                for (int hrs = 0; hrs < inv.getSizeInventory(); ++hrs) {
                    final ItemStack n = inv.getStackInSlot(hrs);
                    if (n != null && n.stackSize != 0) {
                        if (this.mode == 0) {
                            final int mc = MachineLib.decMatchCount(this.filterMap, ac, n);
                            if (mc < n.stackSize) {
                                final ItemStack rem = inv.decrStackSize(hrs, n.stackSize - mc);
                                this.cond.drawPower((double)(25 * n.stackSize));
                                this.buffer.addNewColor(rem, (int)this.color);
                                super.Active = true;
                                this.scheduleTick(5);
                                this.updateBlock();
                                return;
                            }
                        }
                        else if (this.mode == 1 && !this.filterMap.containsKey(n)) {
                            inv.setInventorySlotContents(hrs, (ItemStack)null);
                            this.cond.drawPower((double)(25 * n.stackSize));
                            this.buffer.addNewColor(n, (int)this.color);
                            super.Active = true;
                            this.scheduleTick(5);
                            this.updateBlock();
                            return;
                        }
                    }
                }
                boolean var7 = false;
                if (this.mode == 0) {
                    ac = this.getAcceptCounts();
                    if (ac != null) {
                        var7 = true;
                        ++this.rqnum;
                        if (this.rqnum >= 24) {
                            this.rqnum = 0;
                        }
                        for (int i = this.rqnum; i < ac.length; ++i) {
                            if (ac[i] != 0) {
                                var7 = false;
                                this.doRequest(i, ac[i]);
                                break;
                            }
                        }
                        for (int i = 0; i < this.rqnum; ++i) {
                            if (ac[i] != 0) {
                                var7 = false;
                                this.doRequest(i, ac[i]);
                                break;
                            }
                        }
                    }
                }
                if (super.Powered != var7) {
                    super.Powered = var7;
                    this.updateBlockChange();
                }
            }
        }
    }
    
    public int getTubeConnectableSides() {
        return 1 << this.Rotation;
    }
    
    public int getTubeConClass() {
        return 0;
    }
    
    public boolean canRouteItems() {
        return false;
    }
    
    private boolean handleTubeItem(final TubeItem ti) {
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (ti.priority > this.priority) {
            return false;
        }
        if (ti.color != this.color && this.color != 0 && ti.color != 0) {
            return false;
        }
        if (this.mode == 1) {
            if (this.filterMap == null) {
                this.regenFilterMap();
            }
            if (this.filterMap.size() == 0) {
                return false;
            }
            if (!this.filterMap.containsKey(ti.item)) {
                return false;
            }
            final IInventory mc1 = this.getConnectedInventory(true);
            final int[] slots = IntStream.range(0, mc1.getSizeInventory()).toArray();
            if (MachineLib.addToInventoryCore(mc1, ti.item, slots, true)) {
                super.Delay = true;
                this.scheduleTick(5);
                this.updateBlock();
                return true;
            }
            return false;
        }
        else {
            final int mc2 = this.acceptCount(ti.item);
            if (mc2 == 0) {
                return false;
            }
            boolean tr = true;
            ItemStack ist = ti.item;
            if (mc2 < ist.stackSize) {
                tr = false;
                ist = ist.splitStack(mc2);
            }
            final IInventory dest = this.getConnectedInventory(true);
            final int[] slots2 = IntStream.range(0, dest.getSizeInventory()).toArray();
            if (MachineLib.addToInventoryCore(dest, ist, slots2, true)) {
                super.Delay = true;
                this.scheduleTick(5);
                this.updateBlock();
                return tr;
            }
            return false;
        }
    }
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side != this.Rotation) {
            return false;
        }
        if (state != 2) {
            return this.handleTubeItem(item);
        }
        if (this.handleTubeItem(item)) {
            return true;
        }
        this.buffer.addBounce(item);
        super.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        if (side != this.Rotation) {
            return false;
        }
        if (state == 2) {
            return true;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (item.priority > this.priority) {
            return false;
        }
        if (item.color != this.color && this.color != 0 && item.color != 0) {
            return false;
        }
        switch (this.mode) {
            case 0: {
                return this.acceptCount(item.item) > 0;
            }
            case 1: {
                if (this.filterMap == null) {
                    this.regenFilterMap();
                }
                return this.filterMap.size() != 0 && this.filterMap.containsKey(item.item);
            }
            default: {
                return false;
            }
        }
    }
    
    public int tubeWeight(final int side, final int state) {
        return (side == this.Rotation && state == 2) ? this.buffer.size() : 0;
    }
    
    public boolean requestTubeItem(final TubeItem rq, final boolean act) {
        if (super.Active) {
            return false;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            return false;
        }
        if (!this.filterMap.containsKey(rq.item)) {
            return false;
        }
        if (rq.priority <= this.priority) {
            return false;
        }
        if (rq.color != this.color && this.color > 0) {
            return false;
        }
        final IInventory inv = this.getConnectedInventory(false);
        if (inv == null) {
            return false;
        }
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            final ItemStack is2 = inv.getStackInSlot(i);
            if (is2 != null && is2.stackSize != 0 && CoreLib.compareItemStack(rq.item, is2) == 0) {
                if (act) {
                    final ItemStack pull = inv.decrStackSize(i, Math.min(rq.item.stackSize, is2.stackSize));
                    final TubeItem ti = new TubeItem(0, pull);
                    this.cond.drawPower((double)(25 * ti.item.stackSize));
                    ti.priority = rq.priority;
                    ti.color = this.color;
                    this.buffer.add(ti);
                    super.Active = true;
                    this.scheduleTick(5);
                    this.updateBlock();
                }
                return true;
            }
        }
        return false;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (!this.isTickScheduled()) {
                this.scheduleTick(10);
            }
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.cond.Flow == 0) {
                if (super.Charged) {
                    super.Charged = false;
                    this.updateBlock();
                }
            }
            else if (!super.Charged) {
                super.Charged = true;
                this.updateBlock();
            }
        }
    }
    
    public boolean isPoweringTo(final int side) {
        return side != (this.Rotation ^ 0x1) && super.Powered;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine2;
    }
    
    public int getExtendedID() {
        return 1;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 16, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
    }
    
    public void drainBuffer() {
        while (!this.buffer.isEmpty()) {
            final TubeItem ti = this.buffer.getLast();
            if (this.handleTubeItem(ti)) {
                this.buffer.pop();
                if (!this.buffer.plugged) {
                    continue;
                }
            }
            else {
                if (!this.handleItem(ti)) {
                    this.buffer.plugged = true;
                    return;
                }
                this.buffer.pop();
                if (!this.buffer.plugged) {
                    continue;
                }
            }
        }
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            boolean r = false;
            if (super.Delay) {
                super.Delay = false;
                r = true;
            }
            if (super.Active) {
                if (!this.buffer.isEmpty()) {
                    this.drainBuffer();
                    if (!this.buffer.isEmpty()) {
                        this.scheduleTick(10);
                    }
                    else {
                        this.scheduleTick(5);
                    }
                }
                else {
                    super.Active = false;
                    this.updateBlock();
                }
            }
            else if (r) {
                this.updateBlock();
            }
            else if (this.cond.getVoltage() >= 60.0) {
                this.scanInventory();
            }
        }
    }
    
    public void onBlockRemoval() {
        super.onBlockRemoval();
        for (final ItemStack ist : this.contents) {
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    public int getSizeInventory() {
        return 24;
    }
    
    public ItemStack getStackInSlot(final int i) {
        return this.contents[i];
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        if (this.contents[i] == null) {
            return null;
        }
        if (this.contents[i].stackSize <= j) {
            final ItemStack tr = this.contents[i];
            this.contents[i] = null;
            this.markDirty();
            return tr;
        }
        final ItemStack tr = this.contents[i].splitStack(j);
        if (this.contents[i].stackSize == 0) {
            this.contents[i] = null;
        }
        this.markDirty();
        return tr;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        if (this.contents[i] == null) {
            return null;
        }
        final ItemStack ist = this.contents[i];
        this.contents[i] = null;
        return ist;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack ist) {
        this.contents[i] = ist;
        if (ist != null && ist.stackSize > this.getInventoryStackLimit()) {
            ist.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public String getInventoryName() {
        return "tile.rpmanager.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void markDirty() {
        this.filterMap = null;
        super.markDirty();
    }
    
    public void closeInventory() {
    }
    
    public void openInventory() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound item = items.getCompoundTagAt(i);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.color = data.getByte("color");
        this.mode = data.getByte("mode");
        this.priority = data.getInteger("prio");
        this.rqnum = data.getByte("rqnum");
        this.buffer.readFromNBT(data);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        final NBTTagList items = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null) {
                final NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(item);
                items.appendTag((NBTBase)item);
            }
        }
        data.setTag("Items", (NBTBase)items);
        data.setByte("color", this.color);
        data.setByte("mode", this.mode);
        data.setInteger("prio", this.priority);
        data.setByte("rqnum", this.rqnum);
        this.buffer.writeToNBT(data);
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return true;
    }
}
