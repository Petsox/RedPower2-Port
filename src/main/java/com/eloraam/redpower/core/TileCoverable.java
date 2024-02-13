//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import net.minecraftforge.common.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public abstract class TileCoverable extends TileMultipart implements ICoverable, IMultipart
{
    public abstract boolean canAddCover(final int p0, final int p1);
    
    public abstract boolean tryAddCover(final int p0, final int p1);
    
    public abstract int tryRemoveCover(final int p0);
    
    public abstract int getCover(final int p0);
    
    public abstract int getCoverMask();
    
    @Override
    public boolean isSideSolid(final int side) {
        final int cm = this.getCoverMask();
        return (cm & 1 << side) > 0;
    }
    
    @Override
    public boolean isSideNormal(final int side) {
        final int cm = this.getCoverMask();
        if ((cm & 1 << side) == 0x0) {
            return false;
        }
        final int c = this.getCover(side);
        final int n = c >> 8;
        return !CoverLib.isTransparent(c & 0xFF) && (n < 3 || (n >= 6 && n <= 9));
    }
    
    public void addCoverableHarvestContents(final List<ItemStack> drops) {
        if (CoverLib.blockCoverPlate != null) {
            for (int i = 0; i < 29; ++i) {
                final int j = this.getCover(i);
                if (j >= 0) {
                    drops.add(CoverLib.convertCoverPlate(i, j));
                }
            }
        }
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        this.addCoverableHarvestContents(ist);
    }
    
    @Override
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        final int i = this.tryRemoveCover(part);
        if (i >= 0) {
            if (willHarvest) {
                this.dropCover(part, i);
            }
            if (this.blockEmpty()) {
                this.deleteBlock();
            }
        }
        this.updateBlock();
    }
    
    @Override
    public float getPartStrength(final EntityPlayer player, final int part) {
        int i = this.getCover(part);
        if (i < 0) {
            return 0.0f;
        }
        i &= 0xFF;
        final float hv = CoverLib.getMiningHardness(i);
        if (hv < 0.0f) {
            return 0.0f;
        }
        final ItemStack ist = CoverLib.getItemStack(i);
        final Block bl = Block.getBlockFromItem(ist.getItem());
        final int md = ist.getItemDamage();
        return ForgeHooks.canHarvestBlock(bl, player, md) ? (player.getBreakSpeed(bl, false, md) / hv / 30.0f) : (1.0f / hv / 100.0f);
    }
    
    @Override
    public void setPartBounds(final BlockMultipart block, final int part) {
        final int i = this.getCover(part);
        final float th = CoverLib.getThickness(part, i);
        switch (part) {
            case 0: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, th, 1.0f);
                break;
            }
            case 1: {
                block.setBlockBounds(0.0f, 1.0f - th, 0.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 2: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, th);
                break;
            }
            case 3: {
                block.setBlockBounds(0.0f, 0.0f, 1.0f - th, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 4: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, th, 1.0f, 1.0f);
                break;
            }
            case 5: {
                block.setBlockBounds(1.0f - th, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 6: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, th, th, th);
                break;
            }
            case 7: {
                block.setBlockBounds(0.0f, 0.0f, 1.0f - th, th, th, 1.0f);
                break;
            }
            case 8: {
                block.setBlockBounds(1.0f - th, 0.0f, 0.0f, 1.0f, th, th);
                break;
            }
            case 9: {
                block.setBlockBounds(1.0f - th, 0.0f, 1.0f - th, 1.0f, th, 1.0f);
                break;
            }
            case 10: {
                block.setBlockBounds(0.0f, 1.0f - th, 0.0f, th, 1.0f, th);
                break;
            }
            case 11: {
                block.setBlockBounds(0.0f, 1.0f - th, 1.0f - th, th, 1.0f, 1.0f);
                break;
            }
            case 12: {
                block.setBlockBounds(1.0f - th, 1.0f - th, 0.0f, 1.0f, 1.0f, th);
                break;
            }
            case 13: {
                block.setBlockBounds(1.0f - th, 1.0f - th, 1.0f - th, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 14: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, th, th);
                break;
            }
            case 15: {
                block.setBlockBounds(0.0f, 0.0f, 1.0f - th, 1.0f, th, 1.0f);
                break;
            }
            case 16: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, th, th, 1.0f);
                break;
            }
            case 17: {
                block.setBlockBounds(1.0f - th, 0.0f, 0.0f, 1.0f, th, 1.0f);
                break;
            }
            case 18: {
                block.setBlockBounds(0.0f, 0.0f, 0.0f, th, 1.0f, th);
                break;
            }
            case 19: {
                block.setBlockBounds(0.0f, 0.0f, 1.0f - th, th, 1.0f, 1.0f);
                break;
            }
            case 20: {
                block.setBlockBounds(1.0f - th, 0.0f, 0.0f, 1.0f, 1.0f, th);
                break;
            }
            case 21: {
                block.setBlockBounds(1.0f - th, 0.0f, 1.0f - th, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 22: {
                block.setBlockBounds(0.0f, 1.0f - th, 0.0f, 1.0f, 1.0f, th);
                break;
            }
            case 23: {
                block.setBlockBounds(0.0f, 1.0f - th, 1.0f - th, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 24: {
                block.setBlockBounds(0.0f, 1.0f - th, 0.0f, th, 1.0f, 1.0f);
                break;
            }
            case 25: {
                block.setBlockBounds(1.0f - th, 1.0f - th, 0.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
            case 26: {
                block.setBlockBounds(0.5f - th, 0.0f, 0.5f - th, 0.5f + th, 1.0f, 0.5f + th);
                break;
            }
            case 27: {
                block.setBlockBounds(0.5f - th, 0.5f - th, 0.0f, 0.5f + th, 0.5f + th, 1.0f);
                break;
            }
            case 28: {
                block.setBlockBounds(0.0f, 0.5f - th, 0.5f - th, 1.0f, 0.5f + th, 0.5f + th);
                break;
            }
        }
    }
    
    @Override
    public int getSolidPartsMask() {
        return this.getCoverMask();
    }
    
    @Override
    public int getPartsMask() {
        return this.getCoverMask();
    }
    
    public void dropCover(final int side, final int cov) {
        final ItemStack ist = CoverLib.convertCoverPlate(side, cov);
        if (ist != null) {
            CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
        }
    }
    
    public ItemStack getCover(final int part, final int side) {
        final int i = this.getCover(part);
        if (i >= 0) {
            return CoverLib.convertCoverPlate(side, i);
        }
        final List<ItemStack> ist = new ArrayList<ItemStack>();
        this.addHarvestContents(ist);
        return (ist.size() >= 1) ? ist.get(0) : null;
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final EntityPlayer player) {
        final int i = this.getCover(target.subHit);
        if (i > 0) {
            return CoverLib.convertCoverPlate(target.sideHit, i);
        }
        return this.getBasePickStack();
    }
    
    protected ItemStack getBasePickStack() {
        return null;
    }
    
    public float getExplosionResistance(final int part, final int side, final Entity exploder) {
        int i = this.getCover(part);
        if (i < 0) {
            return -1.0f;
        }
        i &= 0xFF;
        final ItemStack ist = CoverLib.getItemStack(i);
        return Block.getBlockFromItem(ist.getItem()).getExplosionResistance(exploder);
    }
}
