
package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileSorter extends TileTranspose implements IInventory, ISidedInventory, IBluePowerConnectable
{
    BluePowerEndpoint cond;
    public int ConMask;
    private ItemStack[] contents;
    public byte[] colors;
    public byte mode;
    public byte automode;
    public byte defcolor;
    public byte draining;
    public byte column;
    public int pulses;
    private MachineLib.FilterMap filterMap;
    private TubeBuffer[] channelBuffers;
    
    public TileSorter() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileSorter.this;
            }
        };
        this.ConMask = -1;
        this.contents = new ItemStack[40];
        this.colors = new byte[8];
        this.mode = 0;
        this.automode = 0;
        this.defcolor = 0;
        this.draining = -1;
        this.column = 0;
        this.pulses = 0;
        this.filterMap = null;
        this.channelBuffers = new TubeBuffer[8];
        for (int i = 0; i < 8; ++i) {
            this.channelBuffers[i] = new TubeBuffer();
        }
    }
    
    private void regenFilterMap() {
        this.filterMap = MachineLib.makeFilterMap(this.contents);
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
        return new int[0];
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (!super.Powered) {
                super.Delay = false;
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
            if ((this.automode == 1 || (this.automode == 2 && this.pulses > 0)) && !this.isTickScheduled()) {
                this.scheduleTick(10);
            }
        }
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 5, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    @Override
    public int getExtendedID() {
        return 5;
    }
    
    @Override
    public void onBlockRemoval() {
        super.onBlockRemoval();
        for (int i = 0; i < 8; ++i) {
            this.channelBuffers[i].onRemove((TileEntity)this);
        }
        for (int i = 0; i < 40; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (this.automode == 0) {
            super.onBlockNeighborChange(block);
        }
        if (this.automode == 2) {
            if (!RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
                super.Powered = false;
                this.markDirty();
                return;
            }
            if (super.Powered) {
                return;
            }
            super.Powered = true;
            this.markDirty();
            if (super.Delay) {
                return;
            }
            super.Delay = true;
            ++this.pulses;
        }
    }
    
    protected int getColumnMatch(final ItemStack ist) {
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            return -2;
        }
        final int i = this.filterMap.firstMatch(ist);
        return (i < 0) ? i : (i & 0x7);
    }
    
    protected void fireMatch() {
        super.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
    }
    
    protected boolean tryDrainBuffer(final TubeBuffer buf) {
        if (buf.isEmpty()) {
            return false;
        }
        while (!buf.isEmpty()) {
            final TubeItem ti = buf.getLast();
            if (this.stuffCart(ti.item)) {
                buf.pop();
            }
            else {
                if (!this.handleItem(ti)) {
                    return buf.plugged = true;
                }
                buf.pop();
                if (buf.plugged) {
                    return true;
                }
                continue;
            }
        }
        return true;
    }
    
    protected boolean tryDrainBuffer() {
        for (int i = 0; i < 9; ++i) {
            ++this.draining;
            TubeBuffer buf;
            if (this.draining > 7) {
                this.draining = -1;
                buf = super.buffer;
            }
            else {
                buf = this.channelBuffers[this.draining];
            }
            if (this.tryDrainBuffer(buf)) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean isBufferEmpty() {
        if (!super.buffer.isEmpty()) {
            return false;
        }
        for (int i = 0; i < 8; ++i) {
            if (!this.channelBuffers[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void drainBuffer() {
        this.tryDrainBuffer();
    }
    
    private boolean autoTick() {
        if (super.Active) {
            return false;
        }
        if (this.automode == 2 && this.pulses == 0) {
            return false;
        }
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        wc.step(this.Rotation ^ 0x1);
        if (this.handleExtract(wc)) {
            super.Active = true;
            this.updateBlock();
            this.scheduleTick(5);
        }
        else {
            this.scheduleTick(10);
        }
        return true;
    }
    
    @Override
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            if (this.automode == 1 && super.Powered) {
                super.Powered = false;
                this.updateBlock();
            }
            if ((this.automode <= 0 || !this.autoTick()) && super.Active) {
                if (!this.tryDrainBuffer()) {
                    if (this.isBufferEmpty()) {
                        this.scheduleTick(5);
                    }
                    else {
                        this.scheduleTick(10);
                    }
                }
                else {
                    if (!super.Powered || this.automode == 2) {
                        super.Active = false;
                        this.updateBlock();
                    }
                    if (this.automode == 1 || (this.automode == 2 && this.pulses > 0)) {
                        this.scheduleTick(5);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side == this.Rotation && state == 2) {
            final int cm = this.getColumnMatch(item.item);
            TubeBuffer buf = super.buffer;
            if (cm >= 0 && this.mode > 1) {
                buf = this.channelBuffers[cm];
            }
            buf.addBounce(item);
            this.fireMatch();
            return true;
        }
        if (side != (this.Rotation ^ 0x1) || state != 1) {
            return false;
        }
        if (item.priority > 0) {
            return false;
        }
        if (this.automode == 0 && super.Powered) {
            return false;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        final int cm = this.getColumnMatch(item.item);
        TubeBuffer buf = super.buffer;
        if (cm >= 0 && this.mode > 1) {
            buf = this.channelBuffers[cm];
        }
        if (!buf.isEmpty()) {
            return false;
        }
        if (cm >= 0) {
            this.cond.drawPower((double)(25 * item.item.stackSize));
            buf.addNewColor(item.item, (int)this.colors[cm]);
            this.fireMatch();
            this.tryDrainBuffer(buf);
            return true;
        }
        if (this.mode == 4 || this.mode == 6) {
            this.cond.drawPower((double)(25 * item.item.stackSize));
            buf.addNewColor(item.item, (int)this.defcolor);
            this.fireMatch();
            this.tryDrainBuffer(buf);
            return true;
        }
        if (cm == -2) {
            this.cond.drawPower((double)(25 * item.item.stackSize));
            buf.addNewColor(item.item, 0);
            this.fireMatch();
            this.tryDrainBuffer(buf);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        if (side == this.Rotation && state == 2) {
            return true;
        }
        if (side != (this.Rotation ^ 0x1) || state != 1) {
            return false;
        }
        if (item.priority > 0) {
            return false;
        }
        if (this.automode == 0 && super.Powered) {
            return false;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        final int cm = this.getColumnMatch(item.item);
        TubeBuffer buf = super.buffer;
        if (cm >= 0 && this.mode > 1) {
            buf = this.channelBuffers[cm];
        }
        return buf.isEmpty() && (cm >= 0 || this.mode == 4 || this.mode == 6 || cm == -2);
    }
    
    @Override
    protected void addToBuffer(final ItemStack ist) {
        final int cm = this.getColumnMatch(ist);
        TubeBuffer buf = super.buffer;
        if (cm >= 0 && this.mode > 1) {
            buf = this.channelBuffers[cm];
        }
        if (cm < 0) {
            if (this.mode != 4 && this.mode != 6) {
                buf.addNewColor(ist, 0);
            }
            else {
                buf.addNewColor(ist, (int)this.defcolor);
            }
        }
        else {
            buf.addNewColor(ist, (int)this.colors[cm]);
        }
    }
    
    private void stepColumn() {
        for (int i = 0; i < 8; ++i) {
            ++this.column;
            if (this.column > 7) {
                if (this.pulses > 0) {
                    --this.pulses;
                }
                this.column = 0;
            }
            for (int a = 0; a < 5; ++a) {
                final ItemStack ct = this.contents[a * 8 + this.column];
                if (ct != null && ct.stackSize != 0) {
                    return;
                }
            }
        }
        this.column = 0;
    }
    
    private void checkColumn() {
        for (int a = 0; a < 5; ++a) {
            final ItemStack ct = this.contents[a * 8 + this.column];
            if (ct != null && ct.stackSize != 0) {
                return;
            }
        }
        this.stepColumn();
        this.markDirty();
    }
    
    @Override
    protected boolean handleExtract(final IInventory inv, final int[] slots) {
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        if (this.filterMap.size() == 0) {
            final ItemStack var8 = MachineLib.collectOneStack(inv, slots, (ItemStack)null);
            if (var8 == null) {
                return false;
            }
            if (this.mode != 4 && this.mode != 6) {
                super.buffer.addNew(var8);
            }
            else {
                super.buffer.addNewColor(var8, (int)this.defcolor);
            }
            this.cond.drawPower((double)(25 * var8.stackSize));
            this.drainBuffer();
            return true;
        }
        else {
            switch (this.mode) {
                case 0: {
                    this.checkColumn();
                    final int sm = MachineLib.matchAnyStackCol(this.filterMap, inv, slots, (int)this.column);
                    if (sm < 0) {
                        return false;
                    }
                    final ItemStack coll = MachineLib.collectOneStack(inv, slots, this.contents[sm]);
                    super.buffer.addNewColor(coll, (int)this.colors[sm & 0x7]);
                    this.cond.drawPower((double)(25 * coll.stackSize));
                    this.stepColumn();
                    this.drainBuffer();
                    return true;
                }
                case 1: {
                    this.checkColumn();
                    if (!MachineLib.matchAllCol(this.filterMap, inv, slots, (int)this.column)) {
                        return false;
                    }
                    for (int n = 0; n < 5; ++n) {
                        final ItemStack match = this.contents[n * 8 + this.column];
                        if (match != null && match.stackSize != 0) {
                            final ItemStack coll = MachineLib.collectOneStack(inv, slots, match);
                            super.buffer.addNewColor(coll, (int)this.colors[this.column]);
                            this.cond.drawPower((double)(25 * coll.stackSize));
                        }
                    }
                    this.stepColumn();
                    this.drainBuffer();
                    return true;
                }
                case 2: {
                    int sm;
                    for (sm = 0; sm < 8 && !MachineLib.matchAllCol(this.filterMap, inv, slots, sm); ++sm) {}
                    if (sm == 8) {
                        return false;
                    }
                    for (int n = 0; n < 5; ++n) {
                        final ItemStack match = this.contents[n * 8 + sm];
                        if (match != null && match.stackSize != 0) {
                            final ItemStack coll = MachineLib.collectOneStack(inv, slots, match);
                            this.channelBuffers[sm].addNewColor(coll, (int)this.colors[sm]);
                            this.cond.drawPower((double)(25 * coll.stackSize));
                        }
                    }
                    if (this.pulses > 0) {
                        --this.pulses;
                    }
                    this.drainBuffer();
                    return true;
                }
                case 3: {
                    final int sm = MachineLib.matchAnyStack(this.filterMap, inv, slots);
                    if (sm < 0) {
                        return false;
                    }
                    final ItemStack coll = MachineLib.collectOneStack(inv, slots, this.contents[sm]);
                    this.channelBuffers[sm & 0x7].addNewColor(coll, (int)this.colors[sm & 0x7]);
                    this.cond.drawPower((double)(25 * coll.stackSize));
                    if (this.pulses > 0) {
                        --this.pulses;
                    }
                    this.drainBuffer();
                    return true;
                }
                case 4: {
                    final int sm = MachineLib.matchAnyStack(this.filterMap, inv, slots);
                    ItemStack coll;
                    if (sm < 0) {
                        coll = MachineLib.collectOneStack(inv, slots, (ItemStack)null);
                        if (coll == null) {
                            return false;
                        }
                        super.buffer.addNewColor(coll, (int)this.defcolor);
                    }
                    else {
                        coll = MachineLib.collectOneStack(inv, slots, this.contents[sm]);
                        this.channelBuffers[sm & 0x7].addNewColor(coll, (int)this.colors[sm & 0x7]);
                    }
                    this.cond.drawPower((double)(25 * coll.stackSize));
                    if (this.pulses > 0) {
                        --this.pulses;
                    }
                    this.drainBuffer();
                    return true;
                }
                case 5: {
                    final int sm = MachineLib.matchAnyStack(this.filterMap, inv, slots);
                    if (sm < 0) {
                        return false;
                    }
                    final ItemStack coll = MachineLib.collectOneStackFuzzy(inv, slots, this.contents[sm]);
                    this.channelBuffers[sm & 0x7].addNewColor(coll, (int)this.colors[sm & 0x7]);
                    this.cond.drawPower((double)(25 * coll.stackSize));
                    if (this.pulses > 0) {
                        --this.pulses;
                    }
                    this.drainBuffer();
                    return true;
                }
                case 6: {
                    final int sm = MachineLib.matchAnyStack(this.filterMap, inv, slots);
                    ItemStack coll;
                    if (sm < 0) {
                        coll = MachineLib.collectOneStack(inv, slots, (ItemStack)null);
                        if (coll == null) {
                            return false;
                        }
                        super.buffer.addNewColor(coll, (int)this.defcolor);
                    }
                    else {
                        coll = MachineLib.collectOneStackFuzzy(inv, slots, this.contents[sm]);
                        this.channelBuffers[sm & 0x7].addNewColor(coll, (int)this.colors[sm & 0x7]);
                    }
                    this.cond.drawPower((double)(25 * coll.stackSize));
                    if (this.pulses > 0) {
                        --this.pulses;
                    }
                    this.drainBuffer();
                    return true;
                }
                default: {
                    return false;
                }
            }
        }
    }
    
    @Override
    protected boolean suckFilter(final ItemStack ist) {
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (this.filterMap == null) {
            this.regenFilterMap();
        }
        final int cm = this.getColumnMatch(ist);
        TubeBuffer buf = super.buffer;
        if (cm >= 0 && this.mode > 1) {
            buf = this.channelBuffers[cm];
        }
        if (buf.plugged) {
            return false;
        }
        if (cm >= 0) {
            this.cond.drawPower((double)(25 * ist.stackSize));
            return true;
        }
        if (this.mode != 4 && this.mode != 6 && cm != -2) {
            return false;
        }
        this.cond.drawPower((double)(25 * ist.stackSize));
        return true;
    }
    
    public int getSizeInventory() {
        return 40;
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
        return "tile.rpsorter.name";
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
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int cols = 0; cols < items.tagCount(); ++cols) {
            final NBTTagCompound bufs = items.getCompoundTagAt(cols);
            final int i = bufs.getByte("Slot") & 0xFF;
            if (i >= 0 && i < this.contents.length) {
                this.contents[i] = ItemStack.loadItemStackFromNBT(bufs);
            }
        }
        this.column = data.getByte("coln");
        final byte[] cols2 = data.getByteArray("cols");
        if (cols2.length >= 8) {
            System.arraycopy(cols2, 0, this.colors, 0, 8);
        }
        this.mode = data.getByte("mode");
        this.automode = data.getByte("amode");
        this.draining = data.getByte("drain");
        if (this.mode == 4 || this.mode == 6) {
            this.defcolor = data.getByte("defc");
        }
        this.pulses = data.getInteger("pulses");
        this.cond.readFromNBT(data);
        int i;
        NBTTagList buffers;
        NBTTagCompound buf;
        for (buffers = data.getTagList("buffers", 10), i = 0; i < buffers.tagCount(); ++i) {
            buf = buffers.getCompoundTagAt(i);
            this.channelBuffers[i].readFromNBT(buf);
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        final NBTTagList items = new NBTTagList();
        for (int bufs = 0; bufs < this.contents.length; ++bufs) {
            if (this.contents[bufs] != null) {
                final NBTTagCompound i = new NBTTagCompound();
                i.setByte("Slot", (byte)bufs);
                this.contents[bufs].writeToNBT(i);
                items.appendTag((NBTBase)i);
            }
        }
        data.setByte("coln", this.column);
        data.setTag("Items", (NBTBase)items);
        data.setByteArray("cols", this.colors);
        data.setByte("mode", this.mode);
        data.setByte("amode", this.automode);
        data.setByte("drain", this.draining);
        data.setInteger("pulses", this.pulses);
        if (this.mode == 4 || this.mode == 6) {
            data.setByte("defc", this.defcolor);
        }
        this.cond.writeToNBT(data);
        final NBTTagList buffers = new NBTTagList();
        for (int j = 0; j < 8; ++j) {
            final NBTTagCompound buf = new NBTTagCompound();
            this.channelBuffers[j].writeToNBT(buf);
            buffers.appendTag((NBTBase)buf);
        }
        data.setTag("buffers", (NBTBase)buffers);
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return false;
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return false;
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return true;
    }
}
