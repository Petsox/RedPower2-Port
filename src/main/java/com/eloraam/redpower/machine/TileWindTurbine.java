package com.eloraam.redpower.machine;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import com.eloraam.redpower.core.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.nbt.*;

public class TileWindTurbine extends TileMachine implements IInventory, IBluePowerConnectable, IMultiblock
{
    BluePowerConductor cond;
    private byte[] rayTrace;
    private int efficiency;
    private int tracer;
    public int windSpeed;
    public int speed;
    public int phase;
    private int power;
    private int propTicks;
    public boolean hasBlades;
    public boolean hasBrakes;
    public byte windmillType;
    protected ItemStack[] contents;
    public int ConMask;
    public int EConMask;
    
    public TileWindTurbine() {
        this.cond = new BluePowerConductor() {
            public TileEntity getParent() {
                return (TileEntity)TileWindTurbine.this;
            }
            
            public double getInvCap() {
                return 0.25;
            }
        };
        this.rayTrace = null;
        this.efficiency = 0;
        this.tracer = 0;
        this.windSpeed = 0;
        this.speed = 0;
        this.phase = 0;
        this.power = 0;
        this.propTicks = 0;
        this.hasBlades = false;
        this.hasBrakes = false;
        this.windmillType = 0;
        this.contents = new ItemStack[1];
        this.ConMask = -1;
        this.EConMask = -1;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 65;
    }
    
    public int getCornerPowerMode() {
        return 2;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return this.cond;
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        this.teardownBlades();
        super.setPartRotation(part, sec, rot);
    }
    
    public void onMultiRemoval(final int num) {
        final ItemStack ist = this.contents[0];
        if (ist != null && ist.stackSize > 0) {
            CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord + 1, super.zCoord, ist);
        }
        this.contents[0] = null;
        this.markDirty();
        this.teardownBlades();
    }
    
    public AxisAlignedBB getMultiBounds(final int num) {
        switch (this.windmillType) {
            case 1: {
                return AxisAlignedBB.getBoundingBox(-2.5, 1.3, -2.5, 3.5, 9.0, 3.5);
            }
            case 2: {
                final WorldCoord wc = new WorldCoord(0, 0, 0);
                final int right = WorldCoord.getRightDir(this.Rotation);
                wc.step(this.Rotation ^ 0x1);
                final WorldCoord wc2 = wc.coordStep(this.Rotation ^ 0x1);
                wc.step(right, 8);
                wc2.step(right, -8);
                return AxisAlignedBB.getBoundingBox(Math.min(wc.x, wc2.x) + 0.5, -7.5, Math.min(wc.z, wc2.z + 0.5), Math.max(wc.x, wc2.x) + 0.5, 8.5, Math.max(wc.z, wc2.z) + 0.5);
            }
            default: {
                return AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            }
        }
    }
    
    public float getMultiBlockStrength(final int num, final EntityPlayer player) {
        return 0.08f;
    }
    
    public int getExtendedID() {
        return 9;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    public List<WorldCoord> getRelayBlockList(final int wmt) {
        final List<WorldCoord> tr = new ArrayList<WorldCoord>();
        final int right = WorldCoord.getRightDir(this.Rotation);
        switch (wmt) {
            case 1: {
                for (int x = -3; x <= 3; ++x) {
                    for (int y = -3; y <= 3; ++y) {
                        for (int i = 1; i < 8; ++i) {
                            tr.add(new WorldCoord(x + super.xCoord, i + super.yCoord, y + super.zCoord));
                        }
                    }
                }
                break;
            }
            case 2: {
                for (int x = -8; x <= 8; ++x) {
                    for (int y = -8; y <= 8; ++y) {
                        final WorldCoord nc = new WorldCoord((TileEntity)this);
                        nc.step(this.Rotation ^ 0x1);
                        nc.step(right, x);
                        final WorldCoord worldCoord = nc;
                        worldCoord.y += y;
                        tr.add(nc);
                    }
                }
                break;
            }
        }
        return tr;
    }
    
    private void teardownBlades() {
        this.hasBlades = false;
        this.efficiency = 0;
        this.speed = 0;
        this.rayTrace = null;
        this.updateBlock();
        final List<WorldCoord> rbl = this.getRelayBlockList(this.windmillType);
        MultiLib.removeRelays(super.worldObj, new WorldCoord((TileEntity)this), (List)rbl);
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (!this.isTickScheduled()) {
                this.scheduleTick(5);
            }
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.EConMask = RedPowerLib.getExtConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, this.EConMask);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.hasBlades) {
                if (this.contents[0] == null || !(this.contents[0].getItem() instanceof ItemWindmill)) {
                    this.teardownBlades();
                    return;
                }
                final ItemWindmill iwm = (ItemWindmill)this.contents[0].getItem();
                if (iwm.windmillType != this.windmillType) {
                    this.teardownBlades();
                    return;
                }
                if (this.propTicks <= 0) {
                    this.contents[0].setItemDamage(this.contents[0].getItemDamage() + 1);
                    if (this.contents[0].getItemDamage() > this.contents[0].getMaxDamage()) {
                        this.contents[0] = null;
                        this.markDirty();
                        this.teardownBlades();
                        this.contents[0] = iwm.getBrokenItem();
                        this.markDirty();
                        return;
                    }
                    this.markDirty();
                    this.propTicks += 6600;
                }
                if (this.hasBrakes) {
                    return;
                }
                --this.propTicks;
                if (this.cond.getVoltage() > 130.0) {
                    return;
                }
                this.cond.applyPower((double)(this.power / 5));
            }
        }
        else if (this.hasBrakes) {
            this.phase += (int)(this.speed * 0.1);
        }
        else {
            this.phase += this.speed;
        }
    }
    
    private void traceAir0() {
        final int yh = super.yCoord + 1 + this.tracer / 28;
        final int xp = this.tracer % 7;
        byte var6 = 0;
        WorldCoord tp = null;
        switch (this.tracer / 7 % 4) {
            case 0: {
                var6 = 2;
                tp = new WorldCoord(super.xCoord - 3 + xp, yh, super.zCoord - 4);
                break;
            }
            case 1: {
                var6 = 4;
                tp = new WorldCoord(super.xCoord - 4, yh, super.zCoord - 3 + xp);
                break;
            }
            case 2: {
                var6 = 3;
                tp = new WorldCoord(super.xCoord - 3 + xp, yh, super.zCoord + 4);
                break;
            }
            default: {
                var6 = 5;
                tp = new WorldCoord(super.xCoord + 4, yh, super.zCoord - 3 + xp);
                break;
            }
        }
        int i;
        for (i = 0; i < 10 && super.worldObj.getBlock(tp.x, tp.y, tp.z) == Blocks.air; ++i) {
            tp.step((int)var6);
        }
        if (this.rayTrace == null) {
            this.rayTrace = new byte[224];
        }
        this.efficiency = this.efficiency - this.rayTrace[this.tracer] + i;
        this.rayTrace[this.tracer] = (byte)i;
        ++this.tracer;
        if (this.tracer >= 224) {
            this.tracer = 0;
        }
    }
    
    private void traceAir1() {
        final int yh = this.tracer / 17;
        final int xp = this.tracer % 17;
        final int dir2 = WorldCoord.getRightDir(this.Rotation);
        final WorldCoord tp = new WorldCoord((TileEntity)this);
        tp.step(this.Rotation ^ 0x1, 2);
        tp.step(dir2, xp - 8);
        final WorldCoord worldCoord = tp;
        worldCoord.y += yh;
        int i;
        for (i = 0; i < 20 && super.worldObj.getBlock(tp.x, tp.y, tp.z) == Blocks.air; ++i) {
            tp.step(this.Rotation ^ 0x1);
        }
        if (this.rayTrace == null) {
            this.rayTrace = new byte[289];
        }
        this.efficiency = this.efficiency - this.rayTrace[this.tracer] + i;
        this.rayTrace[this.tracer] = (byte)i;
        ++this.tracer;
        if (this.tracer >= 289) {
            this.tracer = 0;
        }
    }
    
    public int getWindScaled(final int i) {
        return Math.min(i, i * this.windSpeed / 13333);
    }
    
    private void tryDeployBlades() {
        final ItemWindmill iwm = (ItemWindmill)this.contents[0].getItem();
        if (iwm.canFaceDirection(this.Rotation)) {
            final List<WorldCoord> rbl = this.getRelayBlockList(iwm.windmillType);
            if (MultiLib.isClear(super.worldObj, new WorldCoord((TileEntity)this), (List)rbl)) {
                this.windmillType = (byte)iwm.windmillType;
                this.hasBlades = true;
                MultiLib.addRelays(super.worldObj, new WorldCoord((TileEntity)this), 0, (List)rbl);
                this.updateBlock();
            }
        }
    }
    
    public void onTileTick() {
        if (!this.hasBlades && this.contents[0] != null && this.contents[0].getItem() instanceof ItemWindmill) {
            this.tryDeployBlades();
        }
        if (!this.hasBrakes && this.cond.getVoltage() > 110.0) {
            this.hasBrakes = true;
        }
        else if (this.hasBrakes && this.cond.getVoltage() < 100.0) {
            this.hasBrakes = false;
        }
        this.windSpeed = (int)(10000.0 * EnvironLib.getWindSpeed(super.worldObj, new WorldCoord((TileEntity)this)));
        if (this.hasBlades) {
            switch (this.windmillType) {
                case 1: {
                    this.power = 2 * this.windSpeed * this.efficiency / 2240;
                    this.speed = this.power * this.power / 20000;
                    this.traceAir0();
                    break;
                }
                case 2: {
                    this.power = this.windSpeed * this.efficiency / 5780;
                    this.speed = this.power * this.power / 5000;
                    this.traceAir1();
                    break;
                }
            }
            this.updateBlock();
        }
        this.scheduleTick(20);
    }
    
    public int getSizeInventory() {
        return 1;
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
        return "gui.windturbine";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        this.EConMask = -1;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 15, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public void onBlockRemoval() {
        super.onBlockRemoval();
        if (this.hasBlades) {
            this.teardownBlades();
        }
        final ItemStack ist = this.contents[0];
        if (ist != null && ist.stackSize > 0) {
            CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 1048576.0;
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int rt = 0; rt < items.tagCount(); ++rt) {
            final NBTTagCompound i = items.getCompoundTagAt(rt);
            final int j = i.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(i);
            }
        }
        this.windmillType = data.getByte("wmt");
        this.hasBlades = (this.windmillType > 0);
        this.efficiency = 0;
        byte[] rays = data.getByteArray("rays");
        if (rays != null) {
            switch (this.windmillType) {
                case 1: {
                    if (rays.length != 224) {
                        rays = null;
                        break;
                    }
                    break;
                }
                case 2: {
                    if (rays.length != 289) {
                        rays = null;
                        break;
                    }
                    break;
                }
                default: {
                    rays = null;
                    break;
                }
            }
        }
        if ((this.rayTrace = rays) != null) {
            for (final byte b : rays) {
                this.efficiency += b;
            }
        }
        this.tracer = data.getInteger("tracer");
        this.speed = data.getInteger("speed");
        this.power = data.getInteger("spdpwr");
        this.propTicks = data.getInteger("proptick");
        this.cond.readFromNBT(data);
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
        if (!this.hasBlades) {
            this.windmillType = 0;
        }
        data.setByte("wmt", this.windmillType);
        if (this.rayTrace != null) {
            data.setByteArray("rays", this.rayTrace);
        }
        data.setInteger("tracer", this.tracer);
        data.setInteger("speed", this.speed);
        data.setInteger("spdpwr", this.power);
        data.setInteger("proptick", this.propTicks);
        this.cond.writeToNBT(data);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        super.readFromPacket(tag);
        final int ps = tag.getByte("ps");
        this.hasBlades = ((ps & 0x1) > 0);
        this.hasBrakes = ((ps & 0x2) > 0);
        this.windmillType = tag.getByte("wmt");
        this.speed = tag.getInteger("speed");
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        super.writeToPacket(tag);
        final int ps = (this.hasBlades ? 1 : 0) | (this.hasBrakes ? 2 : 0);
        tag.setByte("ps", (byte)ps);
        tag.setByte("wmt", this.windmillType);
        tag.setInteger("speed", this.speed);
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return stack.getItem() == RedPowerMachine.itemWoodWindmill || stack.getItem() == RedPowerMachine.itemWoodTurbine;
    }
}
