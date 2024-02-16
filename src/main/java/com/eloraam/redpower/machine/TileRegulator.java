
package com.eloraam.redpower.machine;

import net.minecraft.item.*;
import java.util.stream.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.tileentity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileRegulator extends TileMachine implements ITubeConnectable, ISidedInventory
{
    private TubeBuffer buffer;
    public byte mode;
    protected ItemStack[] contents;
    protected MachineLib.FilterMap inputMap;
    protected MachineLib.FilterMap outputMap;
    public int color;
    
    public TileRegulator() {
        this.buffer = new TubeBuffer();
        this.mode = 0;
        this.contents = new ItemStack[27];
        this.inputMap = null;
        this.outputMap = null;
        this.color = 0;
    }
    
    private void regenFilterMap() {
        this.inputMap = MachineLib.makeFilterMap(this.contents, 0, 9);
        this.outputMap = MachineLib.makeFilterMap(this.contents, 18, 9);
    }
    
    public int getTubeConnectableSides() {
        return 3 << (this.Rotation & 0xFFFFFFFE);
    }
    
    public int getTubeConClass() {
        return 0;
    }
    
    public boolean canRouteItems() {
        return false;
    }
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side == this.Rotation && state == 2) {
            this.buffer.addBounce(item);
            super.Active = true;
            this.updateBlock();
            this.scheduleTick(5);
            return true;
        }
        if (side != (this.Rotation ^ 0x1) || state != 1) {
            return false;
        }
        final int ic = this.inCount(item.item);
        if (ic == 0) {
            return false;
        }
        boolean tr = true;
        ItemStack ist = item.item;
        if (ic < ist.stackSize) {
            tr = false;
            ist = ist.splitStack(ic);
        }
        if (MachineLib.addToInventoryCore((IInventory)this, ist, IntStream.range(0, 9).toArray(), true)) {
            this.markDirty();
            this.scheduleTick(2);
            this.markDirty();
            return tr;
        }
        this.markDirty();
        return false;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return (side == this.Rotation && state == 2) || (side == (this.Rotation ^ 0x1) && state == 1 && this.inCount(item.item) != 0 && MachineLib.addToInventoryCore((IInventory)this, item.item, IntStream.range(0, 9).toArray(), false));
    }
    
    public int tubeWeight(final int side, final int state) {
        return (side == this.Rotation && state == 2) ? this.buffer.size() : 0;
    }
    
    public int[] getAccessibleSlotsFromSide(final int side) {
        return (side != this.Rotation && side != (this.Rotation ^ 0x1)) ? new int[] { 9 } : new int[0];
    }
    
    public void drainBuffer() {
        while (!this.buffer.isEmpty()) {
            final TubeItem ti = this.buffer.getLast();
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
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote && !this.isTickScheduled()) {
            this.scheduleTick(10);
        }
    }
    
    public boolean isPoweringTo(final int side) {
        return side != (this.Rotation ^ 0x1) && super.Powered;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 9, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public int getExtendedID() {
        return 10;
    }
    
    public void onBlockRemoval() {
        this.buffer.onRemove((TileEntity)this);
        for (int i = 0; i < 27; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    private int[] scanInput() {
        if (this.inputMap == null) {
            this.regenFilterMap();
        }
        if (this.inputMap.size() == 0) {
            return null;
        }
        final int[] mc = MachineLib.genMatchCounts(this.inputMap);
        MachineLib.decMatchCounts(this.inputMap, mc, (IInventory)this, IntStream.range(0, 9).toArray());
        return mc;
    }
    
    private int inCount(final ItemStack ist) {
        if (this.inputMap == null) {
            this.regenFilterMap();
        }
        if (this.inputMap.size() == 0) {
            return 0;
        }
        if (!this.inputMap.containsKey(ist)) {
            return 0;
        }
        final int[] mc = MachineLib.genMatchCounts(this.inputMap);
        MachineLib.decMatchCounts(this.inputMap, mc, (IInventory)this, IntStream.range(0, 9).toArray());
        return MachineLib.decMatchCount(this.inputMap, mc, ist);
    }
    
    private int[] scanOutput() {
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        wc.step(this.Rotation);
        final IInventory inv = MachineLib.getInventory(super.worldObj, wc);
        if (inv == null) {
            return null;
        }
        int[] slots;
        if (inv instanceof ISidedInventory) {
            final ISidedInventory mc = (ISidedInventory)inv;
            slots = mc.getAccessibleSlotsFromSide((this.Rotation ^ 0x1) & 0xFF);
        }
        else {
            slots = IntStream.range(0, inv.getSizeInventory()).toArray();
        }
        if (this.outputMap == null) {
            this.regenFilterMap();
        }
        if (this.outputMap.size() == 0) {
            return null;
        }
        final int[] mc2 = MachineLib.genMatchCounts(this.outputMap);
        MachineLib.decMatchCounts(this.outputMap, mc2, inv, slots);
        return mc2;
    }
    
    private void handleTransfer(final int[] omc) {
        if (this.mode != 0 && omc != null) {
            boolean var7 = false;
            for (int var8 = 0; var8 < 9; ++var8) {
                while (omc[var8] > 0) {
                    final ItemStack ist = this.contents[18 + var8].copy();
                    final int ss = Math.min(ist.stackSize, omc[var8]);
                    final int n = var8;
                    omc[n] -= ss;
                    ist.stackSize = ss;
                    final ItemStack is2 = MachineLib.collectOneStack((IInventory)this, IntStream.range(0, 9).toArray(), ist);
                    if (is2 != null) {
                        this.buffer.addNewColor(is2, this.color);
                        var7 = true;
                    }
                }
            }
            if (!var7) {
                return;
            }
        }
        else {
            for (int ch = 0; ch < 9; ++ch) {
                final ItemStack i = this.contents[9 + ch];
                if (i != null && i.stackSize != 0) {
                    this.buffer.addNewColor(i, this.color);
                    this.contents[9 + ch] = null;
                }
            }
        }
        this.markDirty();
        super.Powered = true;
        super.Active = true;
        this.updateBlockChange();
        this.drainBuffer();
        if (!this.buffer.isEmpty()) {
            this.scheduleTick(10);
        }
        else {
            this.scheduleTick(5);
        }
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            if (super.Active) {
                if (!this.buffer.isEmpty()) {
                    super.Powered = true;
                    this.drainBuffer();
                    this.updateBlockChange();
                    if (!this.buffer.isEmpty()) {
                        this.scheduleTick(10);
                    }
                    return;
                }
                super.Active = false;
                this.updateBlock();
            }
            if (super.Powered) {
                final int[] omc = this.scanOutput();
                if (omc == null) {
                    super.Powered = false;
                    this.updateBlockChange();
                }
                else if (!MachineLib.isMatchEmpty(omc)) {
                    final int[] imc = this.scanInput();
                    if (imc != null && MachineLib.isMatchEmpty(imc)) {
                        this.handleTransfer(omc);
                    }
                    else {
                        super.Powered = false;
                        this.updateBlockChange();
                    }
                }
            }
            else {
                final int[] omc = this.scanOutput();
                if (omc != null && MachineLib.isMatchEmpty(omc)) {
                    super.Powered = true;
                    this.updateBlockChange();
                }
                else {
                    final int[] imc = this.scanInput();
                    if (imc != null && MachineLib.isMatchEmpty(imc)) {
                        this.handleTransfer(omc);
                    }
                    else if (omc != null && this.mode == 1) {
                        this.handleTransfer(omc);
                    }
                }
            }
        }
    }
    
    public int getSizeInventory() {
        return 27;
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
        return "tile.rpregulate.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void markDirty() {
        this.inputMap = null;
        this.outputMap = null;
        super.markDirty();
    }
    
    public void closeInventory() {
    }
    
    public void openInventory() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound item = items.getCompoundTagAt(i);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.buffer.readFromNBT(data);
        this.mode = data.getByte("mode");
        this.color = data.getByte("col");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
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
        this.buffer.writeToNBT(data);
        data.setByte("mode", this.mode);
        data.setByte("col", (byte)this.color);
    }
    
    public boolean canInsertItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean canExtractItem(final int slotID, final ItemStack itemStack, final int side) {
        return side != this.Rotation && side != (this.Rotation ^ 0x1);
    }
    
    public boolean hasCustomInventoryName() {
        return true;
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack itemStack) {
        return true;
    }
}
