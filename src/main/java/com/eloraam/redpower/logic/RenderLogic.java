//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.logic;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import java.util.*;
import net.minecraftforge.client.*;
import com.eloraam.redpower.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import org.lwjgl.opengl.*;
import net.minecraft.item.*;
import net.minecraft.init.*;

@SideOnly(Side.CLIENT)
public abstract class RenderLogic extends RenderCovers
{
    public RenderLogic(final Block block) {
        super(block);
    }
    
    public void renderCovers(final IBlockAccess iba, final TileLogic tileLogic) {
        if (tileLogic.Cover != 255) {
            this.context.readGlobalLights(iba, tileLogic.xCoord, tileLogic.yCoord, tileLogic.zCoord);
            this.renderCover(tileLogic.Rotation, tileLogic.Cover);
        }
    }
    
    public TileLogic getTileEntity(final IBlockAccess iba, final int i, final int j, final int k) {
        final TileEntity te = iba.getTileEntity(i, j, k);
        return (te instanceof TileLogic) ? (TileLogic) te : null;
    }
    
    public void setMatrixDisplayTick(final int i, final int j, final int k, final int rot, final Random random) {
        final float x = i + 0.5f + (random.nextFloat() - 0.5f) * 0.2f;
        final float y = j + 0.7f + (random.nextFloat() - 0.5f) * 0.2f;
        final float z = k + 0.5f + (random.nextFloat() - 0.5f) * 0.2f;
        this.context.setOrientation(0, rot);
        this.context.setPos((double)x, (double)y, (double)z);
    }
    
    public void setMatrixInv(final IItemRenderer.ItemRenderType type) {
        this.context.setOrientation(0, 3);
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
    }
    
    public void renderWafer(final int tx) {
        IIcon[] icons = null;
        switch (tx >> 8) {
            default: {
                icons = RedPowerLogic.logicOne;
                break;
            }
            case 1: {
                icons = RedPowerLogic.logicTwo;
                break;
            }
            case 2: {
                icons = RedPowerLogic.logicSensor;
                break;
            }
        }
        this.context.setRelPos(0.0, 0.0, 0.0);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setTexFlags(0);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        this.context.setIcon(icons[0], icons[tx & 0xFF], icons[0], icons[0], icons[0], icons[0]);
        this.context.calcBounds();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.renderFaces(62);
    }
    
    public void renderInvWafer(final int tx) {
        this.context.useNormal = true;
        IIcon[] icons = null;
        switch (tx >> 8) {
            default: {
                icons = RedPowerLogic.logicOne;
                break;
            }
            case 1: {
                icons = RedPowerLogic.logicTwo;
                break;
            }
            case 2: {
                icons = RedPowerLogic.logicSensor;
                break;
            }
        }
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setTexFlags(0);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        this.context.setIcon(icons[0], icons[tx & 0xFF], icons[0], icons[0], icons[0], icons[0]);
        this.context.calcBounds();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.renderFaces(63);
        tess.draw();
        this.context.useNormal = false;
    }
    
    public void renderCover(int rot, final int cov) {
        if (cov != 255) {
            rot >>= 2;
            rot ^= 0x1;
            final short[] rs = { 0, 0, 0, 0, 0, 0 };
            rs[rot] = (short)cov;
            this.context.setTint(1.0f, 1.0f, 1.0f);
            this.renderCovers(1 << rot, rs);
        }
    }
    
    public void renderRedstoneTorch(final double x, final double y, final double z, final double h, final boolean state) {
        this.context.setTexFlags(0);
        this.context.setRelPos(x, y, z);
        this.context.setIcon(state ? RedPowerLogic.torchOn : RedPowerLogic.torch);
        this.context.setLocalLights(1.0f);
        this.context.setTint(1.0f, 1.0f, 1.0f);
        this.context.setSize(0.4375, 1.0 - h, 0.0, 0.5625, 1.0, 1.0);
        this.context.calcBounds();
        this.context.renderFaces(48);
        this.context.setSize(0.0, 1.0 - h, 0.4375, 1.0, 1.0, 0.5625);
        this.context.calcBounds();
        this.context.renderFaces(12);
        this.context.setSize(0.375, 0.0, 0.4375, 0.5, 1.0, 0.5625);
        this.context.setRelPos(x + 0.0625, y - 0.375, z);
        this.context.calcBounds();
        this.context.setTexFlags(24);
        this.context.renderFaces(2);
        this.context.setRelPos(0.0, 0.0, 0.0);
    }
    
    public void renderTorchPuff(final World world, final String name, final double x, final double y, final double z) {
        final Vector3 v = new Vector3(x, y, z);
        this.context.basis.rotate(v);
        v.add(this.context.globalOrigin);
        world.spawnParticle(name, v.x, v.y, v.z, 0.0, 0.0, 0.0);
    }
    
    public void renderChip(final double x, final double y, final double z, final int tex) {
        this.context.setTexFlags(0);
        this.context.setRelPos(x, y, z);
        this.context.setIcon(RedPowerLogic.logicOne[tex]);
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.renderBox(62, 0.375, 0.0625, 0.375, 0.625, 0.1875, 0.625);
    }
    
    protected int getTorchState(final TileLogic tileLogic) {
        return 0;
    }
    
    protected int getInvTorchState(final int metadata) {
        return 0;
    }
    
    protected TorchPos[] getTorchVectors(final TileLogic tileLogic) {
        return null;
    }
    
    protected TorchPos[] getInvTorchVectors(final int metadata) {
        return null;
    }
    
    protected void renderWorldPart(final IBlockAccess iba, final TileLogic tileLogic, final double x, final double y, final double z, final float partialTicks) {
    }
    
    protected void renderInvPart(final int metadata) {
    }
    
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final TileLogic logic = (TileLogic)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileLogic.class);
        if (logic != null) {
            final int ts = this.getTorchState(logic);
            if (ts != 0) {
                this.setMatrixDisplayTick(x, y, z, logic.Rotation, random);
                final TorchPos[] tpv = this.getTorchVectors(logic);
                if (tpv != null) {
                    final int rv = random.nextInt(tpv.length);
                    if ((ts & 1 << rv) != 0x0) {
                        this.renderTorchPuff(world, "reddust", tpv[rv].x, tpv[rv].y, tpv[rv].z);
                    }
                }
            }
        }
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileLogic logic = (TileLogic)tile;
        final World world = logic.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        tess.startDrawingQuads();
        this.renderCovers((IBlockAccess)world, logic);
        tess.draw();
        this.context.setBrightness(this.getMixedBrightness((TileEntity)logic));
        this.context.setOrientation(logic.Rotation >> 2, logic.Rotation & 0x3);
        this.context.setPos(x, y, z);
        tess.startDrawingQuads();
        this.renderWorldPart((IBlockAccess)world, logic, x, y, z, partialTicks);
        tess.draw();
        this.context.bindBlockTexture();
        final int ts = this.getTorchState(logic);
        final TorchPos[] tpv = this.getTorchVectors(logic);
        if (tpv != null) {
            tess.startDrawingQuads();
            for (int n = 0; n < tpv.length; ++n) {
                this.renderRedstoneTorch(tpv[n].x, tpv[n].y, tpv[n].z, tpv[n].h, (ts & 1 << n) > 0);
            }
            tess.draw();
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final int meta = item.getItemDamage();
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        this.setMatrixInv(type);
        this.renderInvPart(meta);
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        final int ts = this.getInvTorchState(meta);
        final TorchPos[] tpv = this.getInvTorchVectors(meta);
        if (tpv != null) {
            for (int n = 0; n < tpv.length; ++n) {
                this.renderRedstoneTorch(tpv[n].x, tpv[n].y, tpv[n].z, tpv[n].h, (ts & 1 << n) > 0);
            }
        }
        tess.draw();
        GL11.glEnable(2896);
    }
    
    protected IIcon getParticleIconForSide(final World world, final int x, final int y, final int z, final TileEntity tile, final int side, final int meta) {
        return Blocks.stone_slab.getIcon(0, 0);
    }
    
    public static class TorchPos
    {
        double x;
        double y;
        double z;
        double h;
        
        public TorchPos(final double x, final double y, final double z, final double h) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.h = h;
        }
    }
}
