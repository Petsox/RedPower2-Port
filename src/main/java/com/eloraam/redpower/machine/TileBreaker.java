//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.machine;

import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.init.*;
import com.eloraam.redpower.core.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraftforge.common.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.nbt.*;

public class TileBreaker extends TileMachine implements ITubeConnectable, IFrameLink, IConnectable
{
    TubeBuffer buffer;
    
    public TileBreaker() {
        this.buffer = new TubeBuffer();
    }
    
    public boolean isFrameMoving() {
        return false;
    }
    
    public boolean canFrameConnectIn(final int dir) {
        return dir != (this.Rotation ^ 0x1);
    }
    
    public boolean canFrameConnectOut(final int dir) {
        return false;
    }
    
    public WorldCoord getFrameLinkset() {
        return null;
    }
    
    public int getConnectableMask() {
        return 0x3FFFFFFF ^ RedPowerLib.getConDirMask(this.Rotation ^ 0x1);
    }
    
    public int getConnectClass(final int side) {
        return 0;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    public int getTubeConnectableSides() {
        return 1 << this.Rotation;
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
            this.scheduleTick(5);
            return true;
        }
        return false;
    }
    
    public boolean tubeItemCanEnter(final int side, final int state, final TubeItem item) {
        return side == this.Rotation && state == 2;
    }
    
    public int tubeWeight(final int side, final int state) {
        return (side == this.Rotation && state == 2) ? this.buffer.size() : 0;
    }
    
    public void onBlockNeighborChange(final Block block) {
        final int cm = this.getConnectableMask();
        if (RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, cm, cm >> 24)) {
            if (!super.Powered) {
                super.Powered = true;
                this.markDirty();
                if (!super.Active) {
                    final WorldCoord wc = new WorldCoord(super.xCoord, super.yCoord, super.zCoord);
                    wc.step(this.Rotation ^ 0x1);
                    final Block bid = super.worldObj.getBlock(wc.x, wc.y, wc.z);
                    if (bid != Blocks.air && bid.getBlockHardness(super.worldObj, wc.x, wc.y, wc.z) != -1.0f && bid != Blocks.bedrock && bid.getBlockHardness(super.worldObj, wc.x, wc.y, wc.z) >= 0.0f) {
                        super.Active = true;
                        this.updateBlock();
                        final int md = super.worldObj.getBlockMetadata(wc.x, wc.y, wc.z);
                        final FakePlayer player = CoreLib.getRedpowerPlayer(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.Rotation, this.Owner);
                        if (CoreLib.hasBreakPermission((EntityPlayerMP)player, wc.x, wc.y, wc.z)) {
                            this.buffer.addAll((Collection)bid.getDrops(super.worldObj, wc.x, wc.y, wc.z, md, 0));
                            this.worldObj.setBlockToAir(wc.x, wc.y, wc.z);
                        }
                        this.drainBuffer();
                        if (!this.buffer.isEmpty()) {
                            this.scheduleTick(5);
                        }
                    }
                }
            }
        }
        else {
            if (super.Active && !this.isTickScheduled()) {
                this.scheduleTick(5);
            }
            if (super.Powered) {
                super.Powered = false;
            }
        }
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
    
    public void onBlockRemoval() {
        this.buffer.onRemove((TileEntity)this);
    }
    
    public void onTileTick() {
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
    
    public int getExtendedID() {
        return 1;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.buffer.readFromNBT(data);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.buffer.writeToNBT(data);
    }
}
