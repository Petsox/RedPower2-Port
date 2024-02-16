
package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraftforge.common.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraftforge.fluids.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import java.util.*;

public class TileGrate extends TileMachinePanel implements IPipeConnectable
{
    private FluidBuffer gratebuf;
    private GratePathfinder searchPath;
    private int searchState;
    private int pressure;
    
    public TileGrate() {
        this.gratebuf = new FluidBuffer() {
            public TileEntity getParent() {
                return (TileEntity)TileGrate.this;
            }
            
            public void onChange() {
                TileGrate.this.markDirty();
            }
            
            public int getMaxLevel() {
                return 1000;
            }
        };
        this.searchState = 0;
    }
    
    @Override
    public int getPartMaxRotation(final int part, final boolean sec) {
        return sec ? 0 : 5;
    }
    
    public int getPipeConnectableSides() {
        return 1 << this.Rotation;
    }
    
    public int getPipeFlangeSides() {
        return 1 << this.Rotation;
    }
    
    public int getPipePressure(final int side) {
        return this.pressure;
    }
    
    public FluidBuffer getPipeBuffer(final int side) {
        return this.gratebuf;
    }
    
    @Override
    public void onFramePickup(final IBlockAccess iba) {
        this.restartPath();
    }
    
    public int getExtendedID() {
        return 3;
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ForgeDirection.getOrientation(this.getFacing(ent)).getOpposite().ordinal();
        this.updateBlockChange();
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void onBlockNeighborChange(final Block block) {
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (!this.isTickScheduled()) {
                this.scheduleTick(5);
            }
            final WorldCoord wc = new WorldCoord((TileEntity)this);
            wc.step(this.Rotation);
            final Integer pr = PipeLib.getPressure(super.worldObj, wc, this.Rotation ^ 0x1);
            if (pr != null) {
                this.pressure = pr - Integer.signum(pr);
            }
            if (this.searchState == 1) {
                this.searchPath.tryMapFluid(400);
            }
            PipeLib.movePipeLiquid(super.worldObj, (IPipeConnectable)this, new WorldCoord((TileEntity)this), 1 << this.Rotation);
        }
    }
    
    public void restartPath() {
        this.searchPath = null;
        this.searchState = 0;
    }
    
    public void onTileTick() {
        if (!this.worldObj.isRemote) {
            if (this.pressure == 0) {
                this.restartPath();
            }
            else if (this.pressure < -100) {
                if (this.gratebuf.getLevel() >= this.gratebuf.getMaxLevel()) {
                    return;
                }
                if (this.searchState == 2) {
                    this.restartPath();
                }
                if (this.searchState == 0) {
                    this.searchState = 1;
                    this.searchPath = new GratePathfinder(false);
                    if (this.gratebuf.Type == null) {
                        if (!this.searchPath.startSuck(new WorldCoord((TileEntity)this), 0x3F ^ 1 << this.Rotation)) {
                            this.restartPath();
                            return;
                        }
                    }
                    else {
                        this.searchPath.start(new WorldCoord((TileEntity)this), this.gratebuf.Type, 0x3F ^ 1 << this.Rotation);
                    }
                }
                if (this.searchState == 1) {
                    if (!this.searchPath.tryMapFluid(400)) {
                        return;
                    }
                    final Fluid ty = this.searchPath.fluidClass;
                    final int fluid = this.searchPath.trySuckFluid(ty.getDensity());
                    if (fluid == 0) {
                        return;
                    }
                    this.gratebuf.addLevel(ty, fluid);
                }
            }
            else if (this.pressure > 100) {
                final Fluid fluid2 = this.gratebuf.getFluidClass();
                if (fluid2 == null) {
                    return;
                }
                final int fq = fluid2.getDensity();
                if (fq == 0) {
                    return;
                }
                if (this.gratebuf.getLevel() < fq) {
                    return;
                }
                if (this.gratebuf.Type == null) {
                    return;
                }
                if (this.searchState == 1) {
                    this.restartPath();
                }
                if (this.searchState == 0) {
                    this.searchState = 2;
                    (this.searchPath = new GratePathfinder(true)).start(new WorldCoord((TileEntity)this), this.gratebuf.Type, 0x3F ^ 1 << this.Rotation);
                }
                if (this.searchState == 2 && RedPowerMachine.AllowGrateDump) {
                    final int fr = this.searchPath.tryDumpFluid(fq, 2000);
                    if (fr != fq) {
                        this.gratebuf.addLevel(this.gratebuf.Type, -fq);
                    }
                }
            }
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.gratebuf.readFromNBT(data, "buf");
        this.pressure = data.getShort("pres");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.gratebuf.writeToNBT(data, "buf");
        data.setShort("pres", (short)this.pressure);
    }
    
    public static class FluidCoord implements Comparable<FluidCoord>
    {
        public WorldCoord wc;
        public int dist;
        
        public FluidCoord(final WorldCoord w, final int d) {
            this.wc = w;
            this.dist = d;
        }
        
        @Override
        public int compareTo(final FluidCoord wr) {
            return (this.wc.y == wr.wc.y) ? (this.dist - wr.dist) : (this.wc.y - wr.wc.y);
        }
    }
    
    public class GratePathfinder
    {
        WorldCoord startPos;
        Map<WorldCoord, WorldCoord> backlink;
        Queue<FluidCoord> workset;
        Queue<FluidCoord> allset;
        public Fluid fluidClass;
        
        public GratePathfinder(final boolean checkVertical) {
            this.backlink = new HashMap<WorldCoord, WorldCoord>();
            this.allset = new PriorityQueue<FluidCoord>(1024, Collections.reverseOrder());
            if (checkVertical) {
                this.workset = new PriorityQueue<FluidCoord>();
            }
            else {
                this.workset = new PriorityQueue<FluidCoord>(1024, Comparator.comparingInt(a -> a.dist));
            }
        }
        
        public void start(final WorldCoord wc, final Fluid tp, final int sides) {
            this.fluidClass = tp;
            this.startPos = wc;
            for (int i = 0; i < 6; ++i) {
                if ((sides & 1 << i) != 0x0) {
                    final WorldCoord wc2 = wc.coordStep(i);
                    this.backlink.put(wc2, wc);
                    this.workset.add(new FluidCoord(wc2, 0));
                }
            }
        }
        
        public boolean startSuck(final WorldCoord wc, final int sides) {
            this.fluidClass = null;
            this.startPos = wc;
            for (int i = 0; i < 6; ++i) {
                if ((sides & 1 << i) != 0x0) {
                    final WorldCoord wc2 = wc.coordStep(i);
                    this.backlink.put(wc2, wc);
                    this.workset.add(new FluidCoord(wc2, 0));
                    final Fluid fl = PipeLib.getFluid(TileGrate.this.worldObj, wc2);
                    if (fl != null) {
                        this.fluidClass = fl;
                    }
                }
            }
            return this.fluidClass != null;
        }
        
        public boolean isConnected(WorldCoord wc) {
            if (wc.compareTo(this.startPos) == 0) {
                return true;
            }
            do {
                wc = this.backlink.get(wc);
                if (wc == null) {
                    return false;
                }
                if (wc.compareTo(this.startPos) == 0) {
                    return true;
                }
            } while (PipeLib.getFluid(TileGrate.this.worldObj, wc) == this.fluidClass);
            return false;
        }
        
        public void stepAdd(final FluidCoord nc) {
            for (int i = 0; i < 6; ++i) {
                final WorldCoord wc2 = nc.wc.coordStep(i);
                if (!this.backlink.containsKey(wc2)) {
                    this.backlink.put(wc2, nc.wc);
                    this.workset.add(new FluidCoord(wc2, nc.dist + 1));
                }
            }
        }
        
        public void stepMap(final FluidCoord nc) {
            for (int i = 0; i < 6; ++i) {
                final WorldCoord wc2 = nc.wc.coordStep(i);
                if (PipeLib.getFluid(TileGrate.this.worldObj, wc2) == this.fluidClass && !this.backlink.containsKey(wc2)) {
                    this.backlink.put(wc2, nc.wc);
                    this.workset.add(new FluidCoord(wc2, nc.dist + 1));
                }
            }
        }
        
        public int tryDumpFluid(int level, final int tries) {
            for (int i = 0; i < tries; ++i) {
                final FluidCoord nc = this.workset.poll();
                if (nc == null) {
                    TileGrate.this.restartPath();
                    return level;
                }
                if (!this.isConnected(nc.wc)) {
                    TileGrate.this.restartPath();
                    return level;
                }
                if (TileGrate.this.worldObj.isAirBlock(nc.wc.x, nc.wc.y, nc.wc.z)) {
                    if (level == this.fluidClass.getDensity() && TileGrate.this.worldObj.setBlock(nc.wc.x, nc.wc.y, nc.wc.z, this.fluidClass.getBlock())) {
                        this.stepAdd(nc);
                        return 0;
                    }
                }
                else if (PipeLib.getFluid(TileGrate.this.worldObj, nc.wc) == this.fluidClass) {
                    this.stepAdd(nc);
                    final int lv1 = this.fluidClass.getDensity(TileGrate.this.worldObj, nc.wc.x, nc.wc.y, nc.wc.z);
                    if (lv1 < 1000) {
                        final int lv2 = Math.min(lv1 + level, this.fluidClass.getDensity());
                        if (lv2 == this.fluidClass.getDensity() && TileGrate.this.worldObj.setBlock(nc.wc.x, nc.wc.y, nc.wc.z, this.fluidClass.getBlock())) {
                            level -= lv2 - lv1;
                            if (level == 0) {
                                return 0;
                            }
                        }
                    }
                }
            }
            return level;
        }
        
        public boolean tryMapFluid(final int tries) {
            if (this.allset.size() > 32768) {
                return true;
            }
            for (int i = 0; i < tries; ++i) {
                final FluidCoord nc = this.workset.poll();
                if (nc == null) {
                    return true;
                }
                final Fluid fluid = PipeLib.getFluid(TileGrate.this.worldObj, nc.wc);
                if (fluid != null) {
                    this.stepMap(nc);
                    if (fluid == this.fluidClass) {
                        final int lvl = PipeLib.getFluidAmount(TileGrate.this.worldObj, nc.wc);
                        if (lvl > 0) {
                            this.allset.add(nc);
                        }
                    }
                }
            }
            return false;
        }
        
        public int trySuckFluid(final int level) {
            int tr = 0;
            while (!this.allset.isEmpty()) {
                final FluidCoord nc = this.allset.peek();
                if (!this.isConnected(nc.wc)) {
                    TileGrate.this.restartPath();
                    return tr;
                }
                if (PipeLib.getFluid(TileGrate.this.worldObj, nc.wc) != this.fluidClass) {
                    this.allset.poll();
                }
                else {
                    final Fluid fluid = PipeLib.getFluid(TileGrate.this.worldObj, nc.wc);
                    if (fluid != null) {
                        final int lvl = PipeLib.getFluidAmount(TileGrate.this.worldObj, nc.wc);
                        if (lvl <= 0) {
                            this.allset.poll();
                        }
                        else {
                            if (tr + lvl > level) {
                                continue;
                            }
                            tr += lvl;
                            TileGrate.this.worldObj.setBlockToAir(nc.wc.x, nc.wc.y, nc.wc.z);
                            this.allset.poll();
                            if (tr == level) {
                                return level;
                            }
                            continue;
                        }
                    }
                    else {
                        this.allset.poll();
                    }
                }
            }
            TileGrate.this.restartPath();
            return tr;
        }
    }
}
