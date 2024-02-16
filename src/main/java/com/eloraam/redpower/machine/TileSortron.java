
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileSortron extends TileTranspose implements IBluePowerConnectable, IRedbusConnectable
{
    BluePowerEndpoint cond;
    public int ConMask;
    private int rbaddr;
    private int cmdDelay;
    private int command;
    private int itemSlot;
    private int itemType;
    private int itemDamage;
    private int itemDamageMax;
    private int itemQty;
    private int itemColor;
    private int itemInColor;
    
    public TileSortron() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileSortron.this;
            }
        };
        this.ConMask = -1;
        this.rbaddr = 4;
        this.cmdDelay = 0;
        this.command = 0;
        this.itemSlot = 0;
        this.itemType = 0;
        this.itemDamage = 0;
        this.itemDamageMax = 0;
        this.itemQty = 0;
        this.itemColor = 0;
        this.itemInColor = 0;
    }
    
    public int getConnectableMask() {
        return 1073741823;
    }
    
    public int getConnectClass(final int side) {
        return 67;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)this.cond;
    }
    
    public int rbGetAddr() {
        return this.rbaddr;
    }
    
    public void rbSetAddr(final int addr) {
        this.rbaddr = addr;
    }
    
    public int rbRead(final int reg) {
        switch (reg) {
            case 0: {
                return this.command & 0xFF;
            }
            case 1: {
                return this.itemQty & 0xFF;
            }
            case 2: {
                return this.itemSlot & 0xFF;
            }
            case 3: {
                return this.itemSlot >> 8 & 0xFF;
            }
            case 4: {
                return this.itemType & 0xFF;
            }
            case 5: {
                return this.itemType >> 8 & 0xFF;
            }
            case 6: {
                return this.itemType >> 16 & 0xFF;
            }
            case 7: {
                return this.itemType >> 24 & 0xFF;
            }
            case 8: {
                return this.itemDamage & 0xFF;
            }
            case 9: {
                return this.itemDamage >> 8 & 0xFF;
            }
            case 10: {
                return this.itemDamageMax & 0xFF;
            }
            case 11: {
                return this.itemDamageMax >> 8 & 0xFF;
            }
            case 12: {
                return this.itemColor & 0xFF;
            }
            case 13: {
                return this.itemInColor & 0xFF;
            }
            default: {
                return 0;
            }
        }
    }
    
    public void rbWrite(final int reg, final int dat) {
        this.markDirty();
        switch (reg) {
            case 0: {
                this.command = dat;
                this.cmdDelay = 2;
                break;
            }
            case 1: {
                this.itemQty = dat;
                break;
            }
            case 2: {
                this.itemSlot = ((this.itemSlot & 0xFF00) | dat);
                break;
            }
            case 3: {
                this.itemSlot = ((this.itemSlot & 0xFF) | dat << 8);
                break;
            }
            case 4: {
                this.itemType = ((this.itemType & 0xFFFFFF00) | dat);
                break;
            }
            case 5: {
                this.itemType = ((this.itemType & 0xFFFF00FF) | dat << 8);
                break;
            }
            case 6: {
                this.itemType = ((this.itemType & 0xFF00FFFF) | dat << 16);
                break;
            }
            case 7: {
                this.itemType = ((this.itemType & 0xFFFFFF) | dat << 24);
                break;
            }
            case 8: {
                this.itemDamage = ((this.itemDamage & 0xFF00) | dat);
                break;
            }
            case 9: {
                this.itemDamage = ((this.itemDamage & 0xFF) | dat << 8);
                break;
            }
            case 10: {
                this.itemDamageMax = ((this.itemDamageMax & 0xFF00) | dat);
                break;
            }
            case 11: {
                this.itemDamageMax = ((this.itemDamageMax & 0xFF) | dat << 8);
                break;
            }
            case 12: {
                this.itemColor = dat;
                break;
            }
            case 13: {
                this.itemInColor = dat;
                break;
            }
        }
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.cmdDelay > 0 && --this.cmdDelay == 0) {
                this.processCommand();
            }
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
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine2;
    }
    
    @Override
    public int getExtendedID() {
        return 0;
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
    }
    
    @Override
    public void onTileTick() {
        if (!this.worldObj.isRemote && super.Active) {
            if (!super.buffer.isEmpty()) {
                this.drainBuffer();
                if (!super.buffer.isEmpty()) {
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
    }
    
    public static int hashItem(final ItemStack ist) {
        final String in = ist.getItem().getUnlocalizedName();
        int hc;
        if (in == null) {
            hc = ist.getItem().hashCode();
        }
        else {
            hc = in.hashCode();
        }
        if (ist.getHasSubtypes()) {
            hc ^= ist.getItemDamage();
        }
        return hc;
    }
    
    private void processCommand() {
        if (this.cond.getVoltage() < 60.0) {
            this.cmdDelay = 20;
        }
        else {
            switch (this.command) {
                case 0: {
                    break;
                }
                case 1: {
                    final IInventory inv = this.getConnectedInventory(false);
                    if (inv == null) {
                        this.command = 255;
                        break;
                    }
                    this.itemSlot = inv.getSizeInventory();
                    this.command = 0;
                    break;
                }
                case 2: {
                    final IInventory inv = this.getConnectedInventory(false);
                    if (inv == null) {
                        this.command = 255;
                        break;
                    }
                    if (this.itemSlot >= inv.getSizeInventory()) {
                        this.command = 255;
                        break;
                    }
                    final ItemStack ist = inv.getStackInSlot(this.itemSlot);
                    if (ist != null && ist.stackSize != 0) {
                        this.itemQty = ist.stackSize;
                        this.itemType = hashItem(ist);
                        if (ist.isItemStackDamageable()) {
                            this.itemDamage = ist.getItemDamage();
                            this.itemDamageMax = ist.getMaxDamage();
                        }
                        else {
                            this.itemDamage = 0;
                            this.itemDamageMax = 0;
                        }
                        this.command = 0;
                        break;
                    }
                    this.itemQty = 0;
                    this.itemType = 0;
                    this.itemDamage = 0;
                    this.itemDamageMax = 0;
                    this.command = 0;
                    break;
                }
                case 3: {
                    if (super.Active) {
                        this.cmdDelay = 2;
                        return;
                    }
                    final IInventory inv = this.getConnectedInventory(false);
                    if (inv == null) {
                        this.command = 255;
                        break;
                    }
                    if (this.itemSlot >= inv.getSizeInventory()) {
                        this.command = 255;
                        break;
                    }
                    final ItemStack ist = inv.getStackInSlot(this.itemSlot);
                    if (ist != null && ist.stackSize != 0) {
                        final int i = Math.min(this.itemQty, ist.stackSize);
                        this.itemQty = i;
                        if (this.itemColor > 16) {
                            this.itemColor = 0;
                        }
                        super.buffer.addNewColor(inv.decrStackSize(this.itemSlot, i), this.itemColor);
                        this.cond.drawPower((double)(50 * ist.stackSize));
                        this.drainBuffer();
                        super.Active = true;
                        this.command = 0;
                        this.updateBlock();
                        this.scheduleTick(5);
                        break;
                    }
                    this.itemQty = 0;
                    this.command = 0;
                    break;
                }
                case 4: {
                    if (this.itemQty == 0) {
                        this.command = 0;
                        break;
                    }
                    break;
                }
                default: {
                    this.command = 255;
                    break;
                }
            }
        }
    }
    
    @Override
    protected boolean handleExtract(final IInventory inv, final int[] slots) {
        return false;
    }
    
    @Override
    protected void addToBuffer(final ItemStack ist) {
        if (this.itemColor > 16) {
            this.itemColor = 0;
        }
        super.buffer.addNewColor(ist, this.itemColor);
    }
    
    @Override
    protected int suckEntity(final Entity ent) {
        if (!(ent instanceof EntityItem)) {
            return 0;
        }
        final EntityItem ei = (EntityItem)ent;
        final ItemStack ist = ei.getEntityItem();
        if (ist.stackSize == 0 || ei.isDead) {
            return 0;
        }
        final int st = ist.stackSize;
        if (!this.suckFilter(ist)) {
            return (st == ist.stackSize) ? 0 : 2;
        }
        this.addToBuffer(ist);
        ei.setDead();
        return 1;
    }
    
    @Override
    protected boolean suckFilter(ItemStack ist) {
        if (this.command != 4) {
            return false;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (this.itemType != 0 && this.itemType != hashItem(ist)) {
            return false;
        }
        boolean tr = true;
        if (this.itemQty < ist.stackSize) {
            tr = false;
            ist = ist.splitStack(this.itemQty);
            if (this.itemColor > 16) {
                this.itemColor = 0;
            }
            super.buffer.addNewColor(ist, this.itemColor);
        }
        this.itemQty -= ist.stackSize;
        if (this.itemQty == 0) {
            this.command = 0;
        }
        this.cond.drawPower((double)(50 * ist.stackSize));
        return tr;
    }
    
    @Override
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side == this.Rotation && state == 2) {
            return super.tubeItemEnter(side, state, item);
        }
        if (side != (this.Rotation ^ 0x1) || state != 1) {
            return false;
        }
        if (this.command != 4) {
            return false;
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (this.itemType != 0 && this.itemType != hashItem(item.item)) {
            return false;
        }
        if (this.itemInColor != 0 && this.itemInColor != item.color) {
            return false;
        }
        boolean tr = true;
        ItemStack ist = item.item;
        if (this.itemQty < ist.stackSize) {
            tr = false;
            ist = ist.splitStack(this.itemQty);
        }
        this.itemQty -= ist.stackSize;
        if (this.itemQty == 0) {
            this.command = 0;
        }
        if (this.itemColor > 16) {
            this.itemColor = 0;
        }
        super.buffer.addNewColor(ist, this.itemColor);
        this.cond.drawPower((double)(50 * ist.stackSize));
        this.drainBuffer();
        super.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
        return tr;
    }
    
    @Override
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return (side == this.Rotation && state == 2) || (side == (this.Rotation ^ 0x1) && state == 1 && this.command == 4 && this.cond.getVoltage() >= 60.0 && (this.itemType == 0 || this.itemType == hashItem(item.item)) && (this.itemInColor == 0 || this.itemInColor == item.color));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        this.rbaddr = (data.getByte("rbaddr") & 0xFF);
        this.cmdDelay = (data.getByte("cmddelay") & 0xFF);
        this.command = (data.getByte("cmd") & 0xFF);
        this.itemSlot = data.getShort("itemslot");
        this.itemType = data.getInteger("itemtype");
        this.itemDamage = data.getShort("itemdmg");
        this.itemDamageMax = data.getShort("itemdmgmax");
        this.itemQty = (data.getByte("itemqty") & 0xFF);
        this.itemInColor = (data.getByte("itemincolor") & 0xFF);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        data.setByte("rbaddr", (byte)this.rbaddr);
        data.setByte("cmddelay", (byte)this.cmdDelay);
        data.setByte("cmd", (byte)this.command);
        data.setShort("itemslot", (short)this.itemSlot);
        data.setInteger("itemtype", this.itemType);
        data.setShort("itemdmg", (short)this.itemDamage);
        data.setShort("itemdmgmax", (short)this.itemDamageMax);
        data.setByte("itemqty", (byte)this.itemQty);
        data.setByte("itemcolor", (byte)this.itemColor);
        data.setByte("itemincolor", (byte)this.itemInColor);
    }
}
