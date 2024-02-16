
package com.eloraam.redpower.machine;

import net.minecraft.item.*;
import net.minecraftforge.common.util.*;
import net.minecraft.world.*;
import net.minecraft.tileentity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.inventory.*;
import java.util.stream.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;

public class TileTranspose extends TileMachine implements ITubeConnectable
{
    TubeBuffer buffer;
    
    public TileTranspose() {
        this.buffer = new TubeBuffer();
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
        if (super.Powered) {
            return false;
        }
        if (!this.buffer.isEmpty()) {
            return false;
        }
        this.addToBuffer(item.item);
        super.Active = true;
        this.updateBlock();
        this.scheduleTick(5);
        this.drainBuffer();
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return (side == this.Rotation && state == 2) || (side == (this.Rotation ^ 0x1) && state == 1 && this.buffer.isEmpty() && !super.Powered);
    }
    
    public int tubeWeight(final int side, final int state) {
        return (side == this.Rotation && state == 2) ? this.buffer.size() : 0;
    }
    
    protected void addToBuffer(final ItemStack ist) {
        this.buffer.addNew(ist);
    }
    
    public boolean canSuck(final int i, final int j, final int k) {
        if (super.worldObj.getBlock(i, j, k).isSideSolid((IBlockAccess)this.worldObj, i, j, k, ForgeDirection.getOrientation(this.Rotation))) {
            return false;
        }
        final TileEntity te = super.worldObj.getTileEntity(i, j, k);
        return te == null || (!(te instanceof IInventory) && !(te instanceof ITubeConnectable));
    }
    
    public void onBlockNeighborChange(final Block block) {
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (!super.Powered) {
                super.Powered = true;
                this.markDirty();
                if (!super.Active) {
                    super.Active = true;
                    final WorldCoord wc = new WorldCoord(super.xCoord, super.yCoord, super.zCoord);
                    wc.step(this.Rotation ^ 0x1);
                    if (this.canSuck(wc.x, wc.y, wc.z)) {
                        this.doSuck();
                        this.updateBlock();
                    }
                    else if (this.handleExtract(wc)) {
                        this.updateBlock();
                    }
                }
            }
        }
        else {
            if (super.Active && !this.isTickScheduled()) {
                this.scheduleTick(5);
            }
            super.Powered = false;
            this.markDirty();
        }
    }
    
    protected IInventory getConnectedInventory(final boolean push) {
        final WorldCoord pos = new WorldCoord((TileEntity)this);
        pos.step(this.Rotation ^ 0x1);
        return MachineLib.getSideInventory(super.worldObj, pos, this.Rotation, push);
    }
    
    protected boolean handleExtract(final WorldCoord wc) {
        final IInventory inv = MachineLib.getInventory(super.worldObj, wc);
        if (inv == null) {
            return false;
        }
        int[] slots;
        if (inv instanceof ISidedInventory) {
            final ISidedInventory isi = (ISidedInventory)inv;
            slots = isi.getAccessibleSlotsFromSide(this.Rotation);
        }
        else {
            slots = IntStream.range(0, inv.getSizeInventory()).toArray();
        }
        return this.handleExtract(inv, slots);
    }
    
    protected boolean handleExtract(final IInventory inv, final int[] slots) {
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                this.addToBuffer(inv.decrStackSize(n, 1));
                this.drainBuffer();
                return true;
            }
        }
        return false;
    }
    
    protected boolean handleExtract(final IInventory inv, final List<Integer> slots) {
        for (final int n : slots) {
            final ItemStack ist = inv.getStackInSlot(n);
            if (ist != null && ist.stackSize != 0) {
                this.addToBuffer(inv.decrStackSize(n, 1));
                this.drainBuffer();
                return true;
            }
        }
        return false;
    }
    
    protected AxisAlignedBB getSizeBox(final double bw, final double bf, final double bb) {
        final double fx = super.xCoord + 0.5;
        final double fy = super.yCoord + 0.5;
        final double fz = super.zCoord + 0.5;
        switch (this.Rotation) {
            case 0: {
                return AxisAlignedBB.getBoundingBox(fx - bw, super.yCoord - bb, fz - bw, fx + bw, super.yCoord + bf, fz + bw);
            }
            case 1: {
                return AxisAlignedBB.getBoundingBox(fx - bw, super.yCoord + 1 - bf, fz - bw, fx + bw, super.yCoord + 1 + bb, fz + bw);
            }
            case 2: {
                return AxisAlignedBB.getBoundingBox(fx - bw, fy - bw, super.zCoord - bb, fx + bw, fy + bw, super.zCoord + bf);
            }
            case 3: {
                return AxisAlignedBB.getBoundingBox(fx - bw, fy - bw, super.zCoord + 1 - bf, fx + bw, fy + bw, super.zCoord + 1 + bb);
            }
            case 4: {
                return AxisAlignedBB.getBoundingBox(super.xCoord - bb, fy - bw, fz - bw, super.xCoord + bf, fy + bw, fz + bw);
            }
            default: {
                return AxisAlignedBB.getBoundingBox(super.xCoord + 1 - bf, fy - bw, fz - bw, super.xCoord + 1 + bb, fy + bw, fz + bw);
            }
        }
    }
    
    protected void doSuck() {
        this.suckEntities(this.getSizeBox(1.55, 3.05, -0.95));
    }
    
    protected boolean suckFilter(final ItemStack ist) {
        return true;
    }
    
    protected int suckEntity(final Entity ent) {
        if (!(ent instanceof EntityItem)) {
            if (ent instanceof EntityMinecartContainer) {
                if (super.Active) {
                    return 0;
                }
                final EntityMinecartContainer em = (EntityMinecartContainer)ent;
                final List<Integer> slots = new ArrayList<Integer>(em.getSizeInventory());
                for (int i = 0; i < em.getSizeInventory(); ++i) {
                    slots.add(i);
                }
                if (this.handleExtract((IInventory)em, slots)) {
                    return 2;
                }
            }
            return 0;
        }
        final EntityItem em2 = (EntityItem)ent;
        final ItemStack ist = em2.getEntityItem();
        if (ist.stackSize == 0 || em2.isDead) {
            return 0;
        }
        if (!this.suckFilter(ist)) {
            return 0;
        }
        this.addToBuffer(ist);
        em2.setDead();
        return 1;
    }
    
    protected void suckEntities(final AxisAlignedBB bb) {
        boolean trig = false;
        final List<Entity> el = (List<Entity>)super.worldObj.getEntitiesWithinAABB((Class)Entity.class, bb);
        for (final Entity ent : el) {
            final int i = this.suckEntity(ent);
            if (i != 0) {
                trig = true;
                if (i == 2) {
                    break;
                }
                continue;
            }
        }
        if (trig) {
            if (!super.Active) {
                super.Active = true;
                this.updateBlock();
            }
            this.drainBuffer();
            this.scheduleTick(5);
        }
    }
    
    public boolean stuffCart(final ItemStack ist) {
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        wc.step(this.Rotation);
        final Block bl = super.worldObj.getBlock(wc.x, wc.y, wc.z);
        if (!(bl instanceof BlockRail)) {
            return false;
        }
        final List<EntityMinecartContainer> el = (List<EntityMinecartContainer>)super.worldObj.getEntitiesWithinAABB((Class)EntityMinecartContainer.class, this.getSizeBox(0.8, 0.05, 1.05));
        for (final EntityMinecartContainer em : el) {
            final int[] slots = IntStream.range(0, em.getSizeInventory()).toArray();
            if (MachineLib.addToInventoryCore((IInventory)em, ist, slots, true)) {
                return true;
            }
        }
        return false;
    }
    
    public void drainBuffer() {
        while (!this.buffer.isEmpty()) {
            final TubeItem ti = this.buffer.getLast();
            if (this.stuffCart(ti.item)) {
                this.buffer.pop();
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
    
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getSizeBox(0.5, 0.95, 0.0);
    }
    
    public void onEntityCollidedWithBlock(final Entity ent) {
        if (!this.worldObj.isRemote && !super.Powered && this.buffer.isEmpty()) {
            this.suckEntities(this.getSizeBox(0.55, 1.05, -0.95));
        }
    }
    
    public void onBlockRemoval() {
        this.buffer.onRemove((TileEntity)this);
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            if (!this.buffer.isEmpty()) {
                this.drainBuffer();
                if (!this.buffer.isEmpty()) {
                    this.scheduleTick(10);
                }
                else {
                    this.scheduleTick(5);
                }
            }
            else if (!super.Powered) {
                super.Active = false;
                this.updateBlock();
            }
        }
    }
    
    public int getExtendedID() {
        return 2;
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.buffer.readFromNBT(data);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.buffer.writeToNBT(data);
    }
}
