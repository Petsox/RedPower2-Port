
package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraftforge.common.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.*;
import net.minecraft.init.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.world.*;

public abstract class TileDeployBase extends TileMachine implements IFrameLink, IConnectable
{
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
    
    @Override
    public Block getBlockType() {
        return (Block)RedPowerMachine.blockMachine;
    }
    
    protected static Entity traceEntities(final World world, final Entity exclude, final Vec3 vs, final Vec3 vlook) {
        final AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(vs.xCoord, vs.yCoord, vs.zCoord, vs.xCoord, vs.yCoord, vs.zCoord);
        final List<? extends Entity> elist = (List<? extends Entity>)world.getEntitiesWithinAABBExcludingEntity(exclude, aabb.addCoord(vlook.xCoord, vlook.yCoord, vlook.zCoord).expand(1.0, 1.0, 1.0));
        final Vec3 v2 = vs.addVector(vlook.xCoord, vlook.yCoord, vlook.zCoord);
        Entity entHit = null;
        double edis = 0.0;
        for (final Entity ent : elist) {
            if (ent.canBeCollidedWith()) {
                final float cbs = ent.getCollisionBorderSize();
                final AxisAlignedBB ab2 = ent.boundingBox.expand((double)cbs, (double)cbs, (double)cbs);
                if (ab2.isVecInside(vs)) {
                    entHit = ent;
                    break;
                }
                final MovingObjectPosition mop = ab2.calculateIntercept(vs, v2);
                if (mop == null) {
                    continue;
                }
                final double d = vs.distanceTo(mop.hitVec);
                if (d >= edis && edis != 0.0) {
                    continue;
                }
                entHit = ent;
                edis = d;
            }
        }
        return entHit;
    }
    
    protected boolean useOnEntity(final Entity ent, final FakePlayer player) {
        if (ent.interactFirst((EntityPlayer)player)) {
            return true;
        }
        final ItemStack ist = player.getCurrentEquippedItem();
        if (ist != null && ent instanceof EntityLiving) {
            final int iss = ist.stackSize;
            ist.interactWithEntity((EntityPlayer)player, (EntityLivingBase)ent);
            if (ist.stackSize != iss) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean tryUseItemStack(final ItemStack ist, final int x, final int y, final int z, final int slot, final FakePlayer player) {
        player.inventory.currentItem = slot;
        final WorldCoord wc = new WorldCoord((TileEntity)this);
        wc.step(this.Rotation ^ 0x1);
        if (!ForgeEventFactory.onPlayerInteract((EntityPlayer)player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, wc.x, wc.y, wc.z, this.Rotation ^ 0x1, this.worldObj).isCanceled()) {
            if (ist.getItem() != Items.dye && ist.getItem() != Items.minecart && ist.getItem() != Items.furnace_minecart && ist.getItem() != Items.chest_minecart) {
                if (ist.getItem().onItemUseFirst(ist, (EntityPlayer)player, super.worldObj, x, y, z, 1, 0.5f, 0.5f, 0.5f)) {
                    return true;
                }
                if (ist.getItem().onItemUse(ist, (EntityPlayer)player, super.worldObj, x, y - 1, z, 1, 0.5f, 0.5f, 0.5f)) {
                    return true;
                }
            }
            else if (ist.getItem().onItemUse(ist, (EntityPlayer)player, super.worldObj, x, y, z, 1, 0.5f, 0.5f, 0.5f)) {
                return true;
            }
            final int iss = ist.stackSize;
            final PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract((EntityPlayer)player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1, this.worldObj);
            if (event.useItem != Event.Result.DENY) {
                final ItemStack ost = ist.useItemRightClick(super.worldObj, (EntityPlayer)player);
                if (ost == ist && ost.stackSize == iss) {
                    final Vec3 getLook;
                    final Vec3 lv = getLook = player.getLook(1.0f);
                    getLook.xCoord *= 2.5;
                    final Vec3 vec3 = lv;
                    vec3.yCoord *= 2.5;
                    final Vec3 vec4 = lv;
                    vec4.zCoord *= 2.5;
                    final Vec3 sv = Vec3.createVectorHelper(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5);
                    final Entity ent = traceEntities(super.worldObj, (Entity)player, sv, lv);
                    return ent != null && this.useOnEntity(ent, player);
                }
                player.inventory.setInventorySlotContents(slot, ost);
                return true;
            }
        }
        return false;
    }
    
    public abstract void enableTowards(final WorldCoord p0);
    
    public void onBlockNeighborChange(final Block block) {
        final int cm = this.getConnectableMask();
        if (!RedPowerLib.isPowered((IBlockAccess)super.worldObj, super.xCoord, super.yCoord, super.zCoord, cm, cm >> 24)) {
            if (super.Active) {
                this.scheduleTick(5);
            }
        }
        else if (!super.Active) {
            super.Active = true;
            this.updateBlock();
            final WorldCoord wc = new WorldCoord((TileEntity)this);
            wc.step(this.Rotation ^ 0x1);
            this.enableTowards(wc);
        }
    }
    
    public void onTileTick() {
        super.Active = false;
        this.updateBlock();
    }
}
