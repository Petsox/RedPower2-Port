
package com.eloraam.redpower.base;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import java.util.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderAlloyFurnace extends RenderCustomBlock
{
    protected RenderContext context;
    
    public RenderAlloyFurnace(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    @Override
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final TileAlloyFurnace tb = CoreLib.getTileEntity(world, x, y, z, TileAlloyFurnace.class);
        if (tb != null && tb.Active) {
            final float f = x + 0.5f;
            final float f2 = y + 0.0f + random.nextFloat() * 6.0f / 16.0f;
            final float f3 = z + 0.5f;
            final float f4 = 0.52f;
            final float f5 = random.nextFloat() * 0.6f - 0.3f;
            switch (tb.Rotation) {
                case 0: {
                    world.spawnParticle("smoke", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
                    world.spawnParticle("flame", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
                    break;
                }
                case 1: {
                    world.spawnParticle("smoke", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
                    world.spawnParticle("flame", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
                    break;
                }
                case 2: {
                    world.spawnParticle("smoke", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
                    world.spawnParticle("flame", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
                    break;
                }
                case 3: {
                    world.spawnParticle("smoke", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
                    world.spawnParticle("flame", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
                    break;
                }
            }
        }
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileAlloyFurnace alloyFurnace = (TileAlloyFurnace)tile;
        final World world = alloyFurnace.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.context.readGlobalLights(world, alloyFurnace.xCoord, alloyFurnace.yCoord, alloyFurnace.zCoord);
        this.context.setIcon(RedPowerBase.alloyFurnaceVert, RedPowerBase.alloyFurnaceVert, alloyFurnace.Active ? RedPowerBase.alloyFurnaceFrontOn : RedPowerBase.alloyFurnaceFront, RedPowerBase.alloyFurnaceSide, RedPowerBase.alloyFurnaceSide, RedPowerBase.alloyFurnaceSide);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        this.context.rotateTextures(alloyFurnace.Rotation);
        tess.startDrawingQuads();
        this.context.renderGlobFaces(63);
        tess.draw();
        GL11.glEnable(2896);
    }
    
    @Override
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.useNormal = true;
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.setIcon(RedPowerBase.alloyFurnaceVert, RedPowerBase.alloyFurnaceVert, RedPowerBase.alloyFurnaceSide, RedPowerBase.alloyFurnaceSide, RedPowerBase.alloyFurnaceSide, RedPowerBase.alloyFurnaceFront);
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
    }
}
