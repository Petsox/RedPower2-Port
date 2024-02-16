
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.util.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;

public class TileTube extends TileCovered implements ITubeFlow, IPaintable
{
    protected TubeFlow flow;
    public byte lastDir;
    public byte paintColor;
    private boolean hasChanged;
    
    public TileTube() {
        this.flow = new TubeFlow() {
            public TileEntity getParent() {
                return (TileEntity)TileTube.this;
            }
            
            public boolean schedule(final TubeItem item, final TubeFlow.TubeScheduleContext context) {
                item.scheduled = true;
                item.progress = 0;
                final int i = context.cons & ~(1 << item.side);
                if (i == 0) {
                    return true;
                }
                if (Integer.bitCount(i) == 1) {
                    item.side = (byte)Integer.numberOfTrailingZeros(i);
                    return true;
                }
                if (!TileTube.this.worldObj.isRemote) {
                    if (item.mode != 3) {
                        item.mode = 1;
                    }
                    item.side = (byte)TubeLib.findRoute(context.world, context.wc, item, i, (int)item.mode, (int)TileTube.this.lastDir);
                    if (item.side >= 0) {
                        int m = i & ~((2 << TileTube.this.lastDir) - 1);
                        if (m == 0) {
                            m = i;
                        }
                        if (m == 0) {
                            TileTube.this.lastDir = 0;
                        }
                        else {
                            TileTube.this.lastDir = (byte)Integer.numberOfTrailingZeros(m);
                        }
                    }
                    else {
                        if (item.mode == 1 && item.priority > 0) {
                            item.priority = 0;
                            item.side = (byte)TubeLib.findRoute(context.world, context.wc, item, context.cons, 1);
                            if (item.side >= 0) {
                                return true;
                            }
                        }
                        item.side = (byte)TubeLib.findRoute(context.world, context.wc, item, context.cons, 2);
                        if (item.side >= 0) {
                            item.mode = 2;
                            return true;
                        }
                        if (item.mode == 3) {
                            item.side = (byte)TubeLib.findRoute(context.world, context.wc, item, context.cons, 1);
                            item.mode = 1;
                        }
                        if (item.side < 0) {
                            item.side = TileTube.this.lastDir;
                            int m = i & ~((2 << TileTube.this.lastDir) - 1);
                            if (m == 0) {
                                m = i;
                            }
                            if (m == 0) {
                                TileTube.this.lastDir = 0;
                            }
                            else {
                                TileTube.this.lastDir = (byte)Integer.numberOfTrailingZeros(m);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
            
            public boolean handleItem(final TubeItem item, final TubeFlow.TubeScheduleContext context) {
                return MachineLib.addToInventory(TileTube.this.worldObj, item.item, context.dest, (item.side ^ 0x1) & 0x3F);
            }
        };
        this.lastDir = 0;
        this.paintColor = 0;
        this.hasChanged = false;
    }
    
    public int getTubeConnectableSides() {
        int tr = 63;
        for (int i = 0; i < 6; ++i) {
            if ((super.CoverSides & 1 << i) > 0 && super.Covers[i] >> 8 < 3) {
                tr &= ~(1 << i);
            }
        }
        return tr;
    }
    
    public int getTubeConClass() {
        return this.paintColor;
    }
    
    public boolean canRouteItems() {
        return true;
    }
    
    public boolean tubeItemEnter(final int side, final int state, final TubeItem item) {
        if (state != 0) {
            return false;
        }
        if (item.color != 0 && this.paintColor != 0 && item.color != this.paintColor) {
            return false;
        }
        item.side = (byte)side;
        this.flow.add(item);
        this.hasChanged = true;
        this.markDirty();
        return true;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return (item.color == 0 || this.paintColor == 0 || item.color == this.paintColor) && state == 0;
    }
    
    public int tubeWeight(final int side, final int state) {
        return 0;
    }
    
    public void addTubeItem(final TubeItem ti) {
        ti.side ^= 0x1;
        this.flow.add(ti);
        this.hasChanged = true;
        this.markDirty();
    }
    
    public TubeFlow getTubeFlow() {
        return this.flow;
    }
    
    public boolean tryPaint(final int part, final int side, final int color) {
        if (part != 29) {
            return false;
        }
        if (this.paintColor == color) {
            return false;
        }
        this.paintColor = (byte)color;
        this.updateBlockChange();
        return true;
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public void updateEntity() {
        if (this.flow.update()) {
            this.hasChanged = true;
        }
        if (this.hasChanged) {
            if (!this.worldObj.isRemote) {
                this.markForUpdate();
            }
            this.markDirty();
        }
    }
    
    public Block getBlockType() {
        return (Block)RedPowerBase.blockMicro;
    }
    
    public int getExtendedID() {
        return 8;
    }
    
    public void onBlockNeighborChange(final Block block) {
    }
    
    public int getPartsMask() {
        return super.CoverSides | 0x20000000;
    }
    
    public int getSolidPartsMask() {
        return super.CoverSides | 0x20000000;
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (part == 29) {
            if (willHarvest) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8));
            }
            this.flow.onRemove();
            if (super.CoverSides > 0) {
                this.replaceWithCovers();
            }
            else {
                this.deleteBlock();
            }
        }
        else {
            super.onHarvestPart(player, part, willHarvest);
        }
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        super.addHarvestContents((List)ist);
        ist.add(new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8));
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        final BlockMachine bl = RedPowerMachine.blockMachine;
        return (part == 29) ? (player.getBreakSpeed((Block)bl, false, 0) / (bl.getHardness() * 30.0f)) : super.getPartStrength(player, part);
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part == 29) {
            block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        }
        else {
            super.setPartBounds(block, part);
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.flow.readFromNBT(data);
        this.lastDir = data.getByte("lDir");
        this.paintColor = data.getByte("pCol");
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.flow.writeToNBT(data);
        data.setByte("lDir", this.lastDir);
        data.setByte("pCol", this.paintColor);
    }
    
    protected void readFromPacket(final NBTTagCompound data) {
        if (data.hasKey("flw")) {
            this.flow.contents.clear();
            for (int cs = data.getInteger("cs"), i = 0; i < cs; ++i) {
                this.flow.contents.add(TubeItem.newFromPacket((NBTTagCompound)data.getTag("cs" + i)));
            }
        }
        else {
            this.paintColor = data.getByte("pCol");
            super.readFromPacket(data);
        }
    }
    
    protected void writeToPacket(final NBTTagCompound data) {
        if (this.hasChanged) {
            this.hasChanged = false;
            data.setBoolean("flw", true);
            int cs = this.flow.contents.size();
            if (cs > 6) {
                cs = 6;
            }
            data.setInteger("cs", cs);
            for (int i = 0; i < cs; ++i) {
                final TubeItem ti = this.flow.contents.get(i);
                final NBTTagCompound ftag = new NBTTagCompound();
                ti.writeToPacket(ftag);
                data.setTag("cs" + i, (NBTBase)ftag);
            }
        }
        else {
            data.setByte("pCol", this.paintColor);
            super.writeToPacket(data);
        }
    }
    
    protected ItemStack getBasePickStack() {
        return new ItemStack((Block)RedPowerBase.blockMicro, 1, this.getExtendedID() << 8);
    }
}
