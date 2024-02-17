package com.eloraam.redpower.machine;

import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;
import net.minecraftforge.event.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraftforge.common.util.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;

public class TileMotor extends TileExtended implements IBluePowerConnectable, IRotatable, IFrameLink, IFrameSupport
{
    BluePowerEndpoint cond;
    public int Rotation;
    public int MoveDir;
    public int MovePos;
    public boolean Powered;
    public boolean Active;
    public boolean Charged;
    public int LinkSize;
    public int ConMask;
    
    public TileMotor() {
        this.cond = new BluePowerEndpoint() {
            public TileEntity getParent() {
                return (TileEntity)TileMotor.this;
            }
        };
        this.Rotation = 0;
        this.MoveDir = 4;
        this.MovePos = -1;
        this.Powered = false;
        this.Active = false;
        this.Charged = false;
        this.LinkSize = -1;
        this.ConMask = -1;
    }
    
    public int getConnectableMask() {
        return 0x3FFFFFFF ^ RedPowerLib.getConDirMask(this.Rotation >> 2 ^ 0x1);
    }
    
    public int getConnectClass(final int side) {
        return 65;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public WorldCoord getFrameLinkset() {
        return null;
    }
    
    public BluePowerConductor getBlueConductor(final int side) {
        return (BluePowerConductor)this.cond;
    }
    
    public int getPartMaxRotation(final int part, final boolean sec) {
        return (this.MovePos >= 0) ? 0 : (sec ? 5 : 3);
    }
    
    public int getPartRotation(final int part, final boolean sec) {
        return sec ? (this.Rotation >> 2) : (this.Rotation & 0x3);
    }
    
    public void setPartRotation(final int part, final boolean sec, final int rot) {
        if (this.MovePos < 0) {
            if (sec) {
                this.Rotation = ((this.Rotation & 0x3) | rot << 2);
            }
            else {
                this.Rotation = ((this.Rotation & 0xFFFFFFFC) | (rot & 0x3));
            }
            this.updateBlockChange();
        }
    }
    
    public boolean isFrameMoving() {
        return false;
    }
    
    public boolean canFrameConnectIn(final int dir) {
        return dir != (this.Rotation >> 2 ^ 0x1);
    }
    
    public boolean canFrameConnectOut(final int dir) {
        return dir == (this.Rotation >> 2 ^ 0x1);
    }
    
    public int getExtendedID() {
        return 7;
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    public void updateEntity() {
        super.updateEntity();
        if (this.MovePos >= 0 && this.MovePos < 16) {
            ++this.MovePos;
            this.markDirty();
        }
        if (!this.worldObj.isRemote) {
            if (this.MovePos >= 0) {
                this.cond.drawPower((double)(100 + 10 * this.LinkSize));
            }
            if (this.MovePos >= 16) {
                this.dropFrame(true);
                this.MovePos = -1;
                this.Active = false;
                this.updateBlock();
            }
            if (this.ConMask < 0) {
                this.ConMask = RedPowerLib.getConnections((IBlockAccess)super.worldObj, (IConnectable)this, super.xCoord, super.yCoord, super.zCoord);
                this.cond.recache(this.ConMask, 0);
            }
            this.cond.iterate();
            this.markDirty();
            if (this.MovePos < 0) {
                if (this.cond.getVoltage() < 60.0) {
                    if (this.Charged && this.cond.Flow == 0) {
                        this.Charged = false;
                        this.updateBlock();
                    }
                }
                else if (!this.Charged) {
                    this.Charged = true;
                    this.updateBlock();
                }
            }
        }
    }
    
    private int getDriveSide() {
        short n = 0;
        switch (this.Rotation >> 2) {
            case 0: {
                n = 13604;
                break;
            }
            case 1: {
                n = 13349;
                break;
            }
            case 2: {
                n = 20800;
                break;
            }
            case 3: {
                n = 16720;
                break;
            }
            case 4: {
                n = 8496;
                break;
            }
            default: {
                n = 12576;
                break;
            }
        }
        int n2 = n >> ((this.Rotation & 0x3) << 2);
        n2 &= 0x7;
        return n2;
    }
    
    private void pickFrame() {
        this.MoveDir = this.getDriveSide();
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        final FrameLib.FrameSolver fs = new FrameLib.FrameSolver(super.worldObj, wc.coordStep(this.Rotation >> 2 ^ 0x1), wc, this.MoveDir);
        if (fs.solveLimit(RedPowerMachine.FrameLinkSize) && fs.addMoved()) {
            this.LinkSize = fs.getFrameSet().size();
            this.MovePos = 0;
            this.Active = true;
            this.updateBlock();
            for (final WorldCoord sp : fs.getClearSet()) {
                super.worldObj.setBlockToAir(sp.x, sp.y, sp.z);
            }
            for (final WorldCoord sp : fs.getFrameSet()) {
                final Block tfm = super.worldObj.getBlock(sp.x, sp.y, sp.z);
                final int ifs = super.worldObj.getBlockMetadata(sp.x, sp.y, sp.z);
                final TileEntity te = super.worldObj.getTileEntity(sp.x, sp.y, sp.z);
                if (te != null) {
                    super.worldObj.removeTileEntity(sp.x, sp.y, sp.z);
                }
                final boolean ir = super.worldObj.isRemote;
                super.worldObj.isRemote = true;
                super.worldObj.setBlock(sp.x, sp.y, sp.z, (Block)RedPowerMachine.blockFrame, 1, 2);
                super.worldObj.isRemote = ir;
                final TileFrameMoving tfm2 = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)super.worldObj, sp, (Class)TileFrameMoving.class);
                if (tfm2 != null) {
                    tfm2.setContents(tfm, ifs, super.xCoord, super.yCoord, super.zCoord, te);
                }
            }
            for (final WorldCoord sp : fs.getFrameSet()) {
                super.worldObj.markBlockForUpdate(sp.x, sp.y, sp.z);
                CoreLib.markBlockDirty(super.worldObj, sp.x, sp.y, sp.z);
                final TileFrameMoving tfm3 = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)super.worldObj, sp, (Class)TileFrameMoving.class);
                if (tfm3 != null && tfm3.movingTileEntity instanceof IFrameSupport) {
                    final IFrameSupport ifs2 = (IFrameSupport)tfm3.movingTileEntity;
                    ifs2.onFramePickup(tfm3.getFrameBlockAccess());
                }
            }
        }
    }
    
    private void dropFrame(final boolean fw) {
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        final FrameLib.FrameSolver fs = new FrameLib.FrameSolver(super.worldObj, wc.coordStep(this.Rotation >> 2 ^ 0x1), wc, -1);
        if (fs.solve()) {
            this.LinkSize = 0;
            fs.sort(this.MoveDir);
            final List<BlockSnapshot> snapshots = new ArrayList<BlockSnapshot>();
            final FakePlayer player = CoreLib.getRedpowerPlayer(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.Rotation >> 2, this.Owner);
            for (final WorldCoord sp : fs.getFrameSet()) {
                final TileFrameMoving ifs = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)super.worldObj, sp, (Class)TileFrameMoving.class);
                if (ifs != null) {
                    final WorldCoord s2 = sp.copy();
                    if (fw) {
                        s2.step(this.MoveDir);
                    }
                    if (!CoreLib.hasEditPermission((EntityPlayerMP)player, s2.x, s2.y, s2.z)) {
                        return;
                    }
                    if (ifs.movingTileEntity != null) {
                        final NBTTagCompound compound = new NBTTagCompound();
                        ifs.movingTileEntity.writeToNBT(compound);
                        snapshots.add(new BlockSnapshot(this.worldObj, s2.x, s2.y, s2.z, ifs.movingBlock, ifs.movingBlockMeta, compound));
                    }
                    else {
                        snapshots.add(new BlockSnapshot(this.worldObj, sp.x, sp.y, sp.z, ifs.movingBlock, ifs.movingBlockMeta));
                    }
                }
            }
            if (!snapshots.isEmpty() && !ForgeEventFactory.onPlayerMultiBlockPlace((EntityPlayer)player, (List)snapshots, ForgeDirection.getOrientation(this.Rotation >> 2 ^ 0x1)).isCanceled()) {
                for (final WorldCoord sp : fs.getFrameSet()) {
                    final TileFrameMoving ifs = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)super.worldObj, sp, (Class)TileFrameMoving.class);
                    if (ifs != null) {
                        ifs.pushEntities(this);
                        final WorldCoord s2 = sp.copy();
                        if (fw) {
                            s2.step(this.MoveDir);
                        }
                        if (ifs.movingBlock != Blocks.air) {
                            final boolean ir = super.worldObj.isRemote;
                            super.worldObj.isRemote = true;
                            super.worldObj.setBlock(s2.x, s2.y, s2.z, ifs.movingBlock, ifs.movingBlockMeta, 2);
                            super.worldObj.isRemote = ir;
                            if (ifs.movingTileEntity != null) {
                                ifs.movingTileEntity.xCoord = s2.x;
                                ifs.movingTileEntity.yCoord = s2.y;
                                ifs.movingTileEntity.zCoord = s2.z;
                                ifs.movingTileEntity.validate();
                                super.worldObj.setTileEntity(s2.x, s2.y, s2.z, ifs.movingTileEntity);
                            }
                        }
                        if (!fw) {
                            continue;
                        }
                        super.worldObj.setBlockToAir(sp.x, sp.y, sp.z);
                    }
                }
                for (final WorldCoord sp : fs.getFrameSet()) {
                    final IFrameSupport frameSupport = (IFrameSupport)CoreLib.getTileEntity((IBlockAccess)super.worldObj, sp, (Class)IFrameSupport.class);
                    if (frameSupport != null) {
                        frameSupport.onFrameDrop();
                    }
                    super.worldObj.markBlockForUpdate(sp.x, sp.y, sp.z);
                    CoreLib.markBlockDirty(super.worldObj, sp.x, sp.y, sp.z);
                    RedPowerLib.updateIndirectNeighbors(super.worldObj, sp.x, sp.y, sp.z, super.worldObj.getBlock(sp.x, sp.y, sp.z));
                }
            }
        }
    }
    
    float getMoveScaled() {
        return this.MovePos / 16.0f;
    }
    
    public void onBlockRemoval() {
        if (this.MovePos >= 0) {
            this.dropFrame(this.Active = false);
        }
        this.MovePos = -1;
    }
    
    public void onBlockNeighborChange(final Block block) {
        this.ConMask = -1;
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, 16777215, 63)) {
            if (this.Charged && !this.Powered && this.MovePos < 0) {
                this.Powered = true;
                this.updateBlockChange();
                if (this.Powered) {
                    this.pickFrame();
                }
            }
        }
        else if (this.Powered) {
            this.Powered = false;
            this.updateBlockChange();
        }
    }
    
    public int getFacing(final EntityLivingBase ent) {
        final int yawrx = (int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        if (Math.abs(ent.posX - super.xCoord) < 2.0 && Math.abs(ent.posZ - super.zCoord) < 2.0) {
            final double p = ent.posY + 1.82 - ent.yOffset - super.yCoord;
            if (p > 2.0) {
                return 0x0 | yawrx;
            }
            if (p < 0.0) {
                return 0x4 | yawrx;
            }
        }
        switch (yawrx) {
            case 0: {
                return 12;
            }
            case 1: {
                return 16;
            }
            case 2: {
                return 8;
            }
            default: {
                return 20;
            }
        }
    }
    
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = this.getFacing(ent);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("mdir", (byte)this.MoveDir);
        tag.setByte("mpos", (byte)(this.MovePos + 1));
        final int ps = (this.Powered ? 1 : 0) | (this.Active ? 2 : 0) | (this.Charged ? 4 : 0);
        tag.setByte("ps", (byte)ps);
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.MoveDir = tag.getByte("mdir");
        this.MovePos = tag.getByte("mpos") - 1;
        final int ps = tag.getByte("ps");
        this.Powered = ((ps & 0x1) > 0);
        this.Active = ((ps & 0x2) > 0);
        this.Charged = ((ps & 0x4) > 0);
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.Rotation = data.getByte("rot");
        this.MoveDir = data.getByte("mdir");
        this.MovePos = data.getByte("mpos");
        this.LinkSize = data.getInteger("links");
        this.cond.readFromNBT(data);
        final byte k = data.getByte("ps");
        this.Powered = ((k & 0x1) > 0);
        this.Active = ((k & 0x2) > 0);
        this.Charged = ((k & 0x4) > 0);
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("rot", (byte)this.Rotation);
        data.setByte("mdir", (byte)this.MoveDir);
        data.setByte("mpos", (byte)this.MovePos);
        data.setInteger("links", this.LinkSize);
        this.cond.writeToNBT(data);
        final int ps = (this.Powered ? 1 : 0) | (this.Active ? 2 : 0) | (this.Charged ? 4 : 0);
        data.setByte("ps", (byte)ps);
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        this.MoveDir = tag.getByte("mdir");
        this.MovePos = tag.getByte("mpos") - 1;
        final int ps = tag.getByte("ps");
        this.Powered = ((ps & 0x1) > 0);
        this.Active = ((ps & 0x2) > 0);
        this.Charged = ((ps & 0x4) > 0);
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("mdir", (byte)this.MoveDir);
        tag.setByte("mpos", (byte)(this.MovePos + 1));
        final int ps = (this.Powered ? 1 : 0) | (this.Active ? 2 : 0) | (this.Charged ? 4 : 0);
        tag.setByte("ps", (byte)ps);
    }
}
