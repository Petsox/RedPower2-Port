package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.init.*;
import com.eloraam.redpower.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import com.eloraam.redpower.core.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraftforge.common.util.*;
import net.minecraft.world.chunk.*;

public class TileFrameMoving extends TileMultipart implements IFrameLink
{
    private FrameBlockAccess frameBlockAccess;
    public int motorX;
    public int motorY;
    public int motorZ;
    public Block movingBlock;
    public int movingBlockMeta;
    public boolean movingCrate;
    public TileEntity movingTileEntity;
    public byte lastMovePos;
    
    public TileFrameMoving() {
        this.frameBlockAccess = new FrameBlockAccess();
        this.movingBlock = Blocks.air;
        this.movingBlockMeta = 0;
        this.movingCrate = false;
        this.movingTileEntity = null;
        this.lastMovePos = 0;
    }
    
    public boolean isFrameMoving() {
        return true;
    }
    
    public boolean canFrameConnectIn(final int dir) {
        return true;
    }
    
    public boolean canFrameConnectOut(final int dir) {
        return true;
    }
    
    public WorldCoord getFrameLinkset() {
        return new WorldCoord(this.motorX, this.motorY, this.motorZ);
    }
    
    public int getExtendedID() {
        return 1;
    }
    
    public void onBlockNeighborChange(final Block block) {
    }
    
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockFrame;
    }
    
    public int getPartsMask() {
        return (this.movingBlock == Blocks.air) ? 0 : 536870912;
    }
    
    public int getSolidPartsMask() {
        return (this.movingBlock == Blocks.air) ? 0 : 536870912;
    }
    
    public boolean blockEmpty() {
        return false;
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        super.addHarvestContents((List)ist);
    }
    
    public float getPartStrength(final EntityPlayer player, final int part) {
        return 0.0f;
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        final TileMotor tm = (TileMotor)CoreLib.getTileEntity((IBlockAccess)super.worldObj, this.motorX, this.motorY, this.motorZ, (Class)TileMotor.class);
        if (tm != null) {
            final float ofs = tm.getMoveScaled();
            switch (tm.MoveDir) {
                case 0: {
                    block.setBlockBounds(0.0f, 0.0f - ofs, 0.0f, 1.0f, 1.0f - ofs, 1.0f);
                    break;
                }
                case 1: {
                    block.setBlockBounds(0.0f, 0.0f + ofs, 0.0f, 1.0f, 1.0f + ofs, 1.0f);
                    break;
                }
                case 2: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f - ofs, 1.0f, 1.0f, 1.0f - ofs);
                    break;
                }
                case 3: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f + ofs, 1.0f, 1.0f, 1.0f + ofs);
                    break;
                }
                case 4: {
                    block.setBlockBounds(0.0f - ofs, 0.0f, 0.0f, 1.0f - ofs, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    block.setBlockBounds(0.0f + ofs, 0.0f, 0.0f, 1.0f + ofs, 1.0f, 1.0f);
                    break;
                }
            }
        }
    }
    
    public IBlockAccess getFrameBlockAccess() {
        return (IBlockAccess)this.frameBlockAccess;
    }
    
    public void setContents(final Block bid, final int md, final int mx, final int my, final int mz, final TileEntity bte) {
        this.movingBlock = bid;
        this.movingBlockMeta = md;
        this.motorX = mx;
        this.motorY = my;
        this.motorZ = mz;
        this.movingTileEntity = bte;
        if (this.movingTileEntity != null) {
            if (RedPowerMachine.FrameAlwaysCrate) {
                this.movingCrate = true;
            }
            if (!(this.movingTileEntity instanceof IFrameSupport)) {
                this.movingCrate = true;
            }
        }
    }
    
    public void doRefresh(final IBlockAccess iba) {
        if (this.movingTileEntity instanceof IFrameSupport) {
            final IFrameSupport ifs = (IFrameSupport)this.movingTileEntity;
            ifs.onFrameRefresh(iba);
        }
    }
    
    public void dropBlock() {
        super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, this.movingBlock, this.movingBlockMeta, 3);
        if (this.movingTileEntity != null) {
            this.movingTileEntity.xCoord = super.xCoord;
            this.movingTileEntity.yCoord = super.yCoord;
            this.movingTileEntity.zCoord = super.zCoord;
            this.movingTileEntity.validate();
            super.worldObj.setTileEntity(super.xCoord, super.yCoord, super.zCoord, this.movingTileEntity);
        }
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        CoreLib.markBlockDirty(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
        RedPowerLib.updateIndirectNeighbors(super.worldObj, super.xCoord, super.yCoord, super.zCoord, this.movingBlock);
    }
    
    private AxisAlignedBB getAABB(final int dir, final float dist) {
        final AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox((double)super.xCoord, (double)super.yCoord, (double)super.zCoord, (double)(super.xCoord + 1), (double)(super.yCoord + 1), (double)(super.zCoord + 1));
        switch (dir) {
            case 0: {
                final AxisAlignedBB axisAlignedBB = aabb;
                axisAlignedBB.minY -= dist;
                final AxisAlignedBB axisAlignedBB2 = aabb;
                axisAlignedBB2.maxY -= dist;
                break;
            }
            case 1: {
                final AxisAlignedBB axisAlignedBB3 = aabb;
                axisAlignedBB3.minY += dist;
                final AxisAlignedBB axisAlignedBB4 = aabb;
                axisAlignedBB4.maxY += dist;
                break;
            }
            case 2: {
                final AxisAlignedBB axisAlignedBB5 = aabb;
                axisAlignedBB5.minZ -= dist;
                final AxisAlignedBB axisAlignedBB6 = aabb;
                axisAlignedBB6.maxZ -= dist;
                break;
            }
            case 3: {
                final AxisAlignedBB axisAlignedBB7 = aabb;
                axisAlignedBB7.minZ += dist;
                final AxisAlignedBB axisAlignedBB8 = aabb;
                axisAlignedBB8.maxZ += dist;
                break;
            }
            case 4: {
                final AxisAlignedBB axisAlignedBB9 = aabb;
                axisAlignedBB9.minX -= dist;
                final AxisAlignedBB axisAlignedBB10 = aabb;
                axisAlignedBB10.maxX -= dist;
                break;
            }
            case 5: {
                final AxisAlignedBB axisAlignedBB11 = aabb;
                axisAlignedBB11.minX += dist;
                final AxisAlignedBB axisAlignedBB12 = aabb;
                axisAlignedBB12.maxX += dist;
                break;
            }
        }
        return aabb;
    }
    
    void pushEntities(final TileMotor tm) {
        final float prev = this.lastMovePos / 16.0f;
        final float cur = tm.MovePos / 16.0f;
        this.lastMovePos = (byte)tm.MovePos;
        float xm = 0.0f;
        float ym = 0.0f;
        float zm = 0.0f;
        switch (tm.MoveDir) {
            case 0: {
                ym -= cur - prev;
                break;
            }
            case 1: {
                ym += cur - prev;
                break;
            }
            case 2: {
                zm -= cur - prev;
                break;
            }
            case 3: {
                zm += cur - prev;
                break;
            }
            case 4: {
                xm -= cur - prev;
                break;
            }
            case 5: {
                xm += cur - prev;
                break;
            }
        }
        final AxisAlignedBB aabb = this.getAABB(tm.MoveDir, cur);
        final List<Entity> entities = new ArrayList<Entity>(super.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, aabb));
        for (final Entity ent : entities) {
            ent.moveEntity((double)xm, (double)ym, (double)zm);
        }
    }
    
    public void updateEntity() {
        super.updateEntity();
        final TileMotor tm = (TileMotor)CoreLib.getTileEntity((IBlockAccess)super.worldObj, this.motorX, this.motorY, this.motorZ, (Class)TileMotor.class);
        if (tm != null && tm.MovePos >= 0) {
            this.pushEntities(tm);
        }
        else if (!this.worldObj.isRemote) {
            this.dropBlock();
        }
    }
    
    public void validate() {
        super.validate();
        if (this.movingTileEntity != null) {
            this.movingTileEntity.setWorldObj(super.worldObj);
        }
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.motorX = data.getInteger("mx");
        this.motorY = data.getInteger("my");
        this.motorZ = data.getInteger("mz");
        this.movingBlock = Block.getBlockById(data.getInteger("mbid"));
        this.movingBlockMeta = data.getInteger("mbmd");
        this.lastMovePos = data.getByte("lmp");
        if (data.hasKey("mte")) {
            final NBTTagCompound mte = data.getCompoundTag("mte");
            this.movingTileEntity = TileEntity.createAndLoadEntity(mte);
        }
        else {
            this.movingTileEntity = null;
        }
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("mx", this.motorX);
        data.setInteger("my", this.motorY);
        data.setInteger("mz", this.motorZ);
        data.setInteger("mbid", Block.getIdFromBlock(this.movingBlock));
        data.setInteger("mbmd", this.movingBlockMeta);
        data.setByte("lmp", this.lastMovePos);
        if (this.movingTileEntity != null) {
            final NBTTagCompound mte = new NBTTagCompound();
            this.movingTileEntity.writeToNBT(mte);
            data.setTag("mte", (NBTBase)mte);
        }
    }
    
    protected void readFromPacket(final NBTTagCompound tag) {
        this.motorX = tag.getInteger("mx");
        this.motorY = tag.getInteger("my");
        this.motorZ = tag.getInteger("mz");
        this.movingBlock = Block.getBlockById(tag.getInteger("mbid"));
        this.movingBlockMeta = tag.getInteger("mbmd");
        if (this.movingBlock != Blocks.air) {
            this.movingTileEntity = this.movingBlock.createTileEntity(super.worldObj, this.movingBlockMeta);
            if (this.movingTileEntity != null) {
                if (!(this.movingTileEntity instanceof IFrameSupport)) {
                    this.movingCrate = true;
                    return;
                }
                this.movingTileEntity.setWorldObj(super.worldObj);
                this.movingTileEntity.xCoord = super.xCoord;
                this.movingTileEntity.yCoord = super.yCoord;
                this.movingTileEntity.zCoord = super.zCoord;
                final IFrameSupport ifs = (IFrameSupport)this.movingTileEntity;
                ifs.readFramePacket(tag);
            }
        }
    }
    
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setInteger("mx", this.motorX);
        tag.setInteger("my", this.motorY);
        tag.setInteger("mz", this.motorZ);
        tag.setInteger("mbid", Block.getIdFromBlock(this.movingBlock));
        tag.setInteger("mbmd", this.movingBlockMeta);
        if (this.movingTileEntity instanceof IFrameSupport) {
            final IFrameSupport ifs = (IFrameSupport)this.movingTileEntity;
            ifs.writeFramePacket(tag);
        }
    }
    
    public AxisAlignedBB getRenderBoundingBox() {
        final TileMotor tm = (TileMotor)CoreLib.getTileEntity((IBlockAccess)super.worldObj, this.motorX, this.motorY, this.motorZ, (Class)TileMotor.class);
        if (tm != null && tm.MovePos >= 0) {
            final float prev = this.lastMovePos / 16.0f;
            final float cur = tm.MovePos / 16.0f;
            this.lastMovePos = (byte)tm.MovePos;
            float xm = 0.0f;
            float ym = 0.0f;
            float zm = 0.0f;
            switch (tm.MoveDir) {
                case 0: {
                    ym -= cur - prev;
                    break;
                }
                case 1: {
                    ym += cur - prev;
                    break;
                }
                case 2: {
                    zm -= cur - prev;
                    break;
                }
                case 3: {
                    zm += cur - prev;
                    break;
                }
                case 4: {
                    xm -= cur - prev;
                    break;
                }
                case 5: {
                    xm += cur - prev;
                    break;
                }
            }
            return super.getRenderBoundingBox().addCoord((double)xm, (double)ym, (double)zm);
        }
        return super.getRenderBoundingBox();
    }
    
    private class FrameBlockAccess implements IBlockAccess
    {
        private TileFrameMoving getFrame(final int x, final int y, final int z) {
            final TileFrameMoving tfm = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)TileFrameMoving.this.worldObj, x, y, z, (Class)TileFrameMoving.class);
            return (tfm == null) ? null : ((tfm.motorX == TileFrameMoving.this.motorX && tfm.motorY == TileFrameMoving.this.motorY && tfm.motorZ == TileFrameMoving.this.motorZ) ? tfm : null);
        }
        
        public Block getBlock(final int x, final int y, final int z) {
            final TileFrameMoving tfm = this.getFrame(x, y, z);
            return (tfm == null) ? Blocks.air : tfm.movingBlock;
        }
        
        public TileEntity getTileEntity(final int x, final int y, final int z) {
            final TileFrameMoving tfm = this.getFrame(x, y, z);
            return (tfm == null) ? null : tfm.movingTileEntity;
        }
        
        public int getLightBrightnessForSkyBlocks(final int x, final int y, final int z, final int value) {
            return TileFrameMoving.this.worldObj.getLightBrightnessForSkyBlocks(x, y, z, value);
        }
        
        public int getBlockMetadata(final int x, final int y, final int z) {
            final TileFrameMoving tfm = this.getFrame(x, y, z);
            return (tfm == null) ? 0 : tfm.movingBlockMeta;
        }
        
        public boolean isAirBlock(final int i, final int j, final int k) {
            final Block bid = this.getBlock(i, j, k);
            return bid == Blocks.air || bid.isAir((IBlockAccess)TileFrameMoving.this.worldObj, i, j, k);
        }
        
        public int getHeight() {
            return TileFrameMoving.this.worldObj.getHeight();
        }
        
        public boolean extendedLevelsInChunkCache() {
            return false;
        }
        
        public BiomeGenBase getBiomeGenForCoords(final int x, final int z) {
            return TileFrameMoving.this.worldObj.getBiomeGenForCoords(x, z);
        }
        
        public int isBlockProvidingPowerTo(final int x, final int y, final int z, final int side) {
            return 0;
        }
        
        public boolean isSideSolid(final int x, final int y, final int z, final ForgeDirection side, final boolean _default) {
            if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
                return _default;
            }
            final Chunk chunk = TileFrameMoving.this.worldObj.getChunkProvider().provideChunk(x >> 4, z >> 4);
            if (chunk == null || chunk.isEmpty()) {
                return _default;
            }
            return this.getBlock(x, y, z).isSideSolid((IBlockAccess)this, x, y, z, side);
        }
    }
}
