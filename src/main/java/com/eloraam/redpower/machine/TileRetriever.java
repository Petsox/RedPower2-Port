
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import java.util.stream.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;
import java.util.*;
import net.minecraft.entity.player.*;
import com.eloraam.redpower.*;
import net.minecraft.nbt.*;

public class TileRetriever extends TileFilter implements IBluePowerConnectable
{
    BluePowerEndpoint cond;
    public int ConMask;
    public byte select;
    public byte mode;
    
    public TileRetriever() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileRetriever.this;
            }
        };
        this.ConMask = -1;
        this.select = 0;
        this.mode = 0;
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
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (side != (this.Rotation ^ 0x1) || state != 3) {
            return side == this.Rotation && state == 2 && super.tubeItemEnter(side, state, item);
        }
        if (!super.buffer.isEmpty()) {
            return false;
        }
        if (super.filterMap == null) {
            this.regenFilterMap();
        }
        if (super.filterMap.size() > 0 && !super.filterMap.containsKey(item.item)) {
            return false;
        }
        super.buffer.addNewColor(item.item, (int)super.color);
        super.Delay = true;
        this.updateBlock();
        this.scheduleTick(5);
        this.drainBuffer();
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        if (side != (this.Rotation ^ 0x1) || state != 3) {
            return side == this.Rotation && state == 2 && super.tubeItemCanEnter(side, state, item);
        }
        if (!super.buffer.isEmpty()) {
            return false;
        }
        if (super.filterMap == null) {
            this.regenFilterMap();
        }
        return super.filterMap.size() == 0 || super.filterMap.containsKey(item.item);
    }
    
    private void stepSelect() {
        for (int i = 0; i < 9; ++i) {
            ++this.select;
            if (this.select > 8) {
                this.select = 0;
            }
            final ItemStack ct = super.contents[this.select];
            if (ct != null && ct.stackSize > 0) {
                return;
            }
        }
        this.select = 0;
    }
    
    protected boolean handleExtract(final WorldCoord wc) {
        final ITubeConnectable itc = (ITubeConnectable)CoreLib.getTileEntity((IBlockAccess)super.getWorldObj(), wc, (Class)ITubeConnectable.class);
        if (itc == null || !itc.canRouteItems()) {
            return super.handleExtract(wc);
        }
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (super.filterMap == null) {
            this.regenFilterMap();
        }
        final TubeLib.InRouteFinder irf = new TubeLib.InRouteFinder(super.worldObj, super.filterMap);
        if (this.mode == 0) {
            irf.setSubFilt((int)this.select);
        }
        final int sm = irf.find(new WorldCoord((TileEntity)this), 1 << (this.Rotation ^ 0x1));
        if (sm < 0) {
            return false;
        }
        final WorldCoord dest = irf.getResultPoint();
        final IInventory inv = MachineLib.getInventory(super.worldObj, dest);
        if (inv == null) {
            return false;
        }
        final int side = irf.getResultSide();
        int[] slots;
        if (inv instanceof ISidedInventory) {
            final ISidedInventory tt = (ISidedInventory)inv;
            slots = tt.getAccessibleSlotsFromSide(side);
        }
        else {
            slots = IntStream.range(0, inv.getSizeInventory()).toArray();
        }
        dest.step(side);
        final TileTube tt2 = (TileTube)CoreLib.getTileEntity((IBlockAccess)super.worldObj, dest, (Class)TileTube.class);
        if (tt2 == null) {
            return false;
        }
        final ItemStack ist = MachineLib.collectOneStack(inv, slots, super.contents[sm]);
        if (ist == null) {
            return false;
        }
        final TubeItem ti = new TubeItem(side, ist);
        this.cond.drawPower((double)(25 * ist.stackSize));
        ti.mode = 3;
        tt2.addTubeItem(ti);
        if (this.mode == 0) {
            this.stepSelect();
        }
        return true;
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
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        super.onBlockNeighborChange(block);
    }
    
    public void onTileTick() {
        super.onTileTick();
        if (super.Delay) {
            super.Delay = false;
            this.updateBlock();
        }
    }
    
    protected void doSuck() {
        this.suckEntities(this.getSizeBox(2.55, 5.05, -0.95));
    }
    
    protected boolean suckFilter(final ItemStack ist) {
        if (this.cond.getVoltage() < 60.0) {
            return false;
        }
        if (!super.suckFilter(ist)) {
            return false;
        }
        this.cond.drawPower((double)(25 * ist.stackSize));
        return true;
    }
    
    protected int suckEntity(final Entity ent) {
        if (!(ent instanceof EntityMinecartContainer)) {
            return super.suckEntity(ent);
        }
        if (this.cond.getVoltage() < 60.0) {
            return 0;
        }
        if (super.filterMap == null) {
            this.regenFilterMap();
        }
        final EntityMinecartContainer em = (EntityMinecartContainer)ent;
        final int[] slots = IntStream.range(0, em.getSizeInventory()).toArray();
        if (!MachineLib.emptyInventory((IInventory)em, slots)) {
            return super.suckEntity(ent);
        }
        final List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Items.minecart, 1));
        if (em.func_145820_n().getMaterial() != Material.air) {
            items.add(new ItemStack(em.func_145820_n(), 1, em.getDisplayTileData()));
        }
        for (final ItemStack ist : items) {
            super.buffer.addNewColor(ist, (int)super.color);
        }
        em.setDead();
        this.cond.drawPower(200.0);
        return 2;
    }
    
    public boolean onBlockActivated(final EntityPlayer player) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.worldObj.isRemote) {
            player.openGui((Object)RedPowerMachine.instance, 7, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        }
        return true;
    }
    
    public int getExtendedID() {
        return 8;
    }
    
    public String getInventoryName() {
        return "tile.rpretriever.name";
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cond.readFromNBT(data);
        this.mode = data.getByte("mode");
        this.select = data.getByte("sel");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cond.writeToNBT(data);
        data.setByte("mode", this.mode);
        data.setByte("sel", this.select);
    }
}
