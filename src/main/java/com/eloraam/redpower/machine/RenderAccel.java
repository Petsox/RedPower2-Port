
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.util.*;
import net.minecraft.entity.item.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.entity.*;
import com.eloraam.redpower.*;
import com.eloraam.redpower.core.*;
import java.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderAccel extends RenderCustomBlock
{
    private RenderModel model;
    private ResourceLocation modelRes;
    private RenderContext context;
    private EntityItem item;
    private int[] paintColors;
    
    public RenderAccel(final Block block) {
        super(block);
        this.model = RenderModel.loadModel("rpmachine:models/accel.obj");
        this.modelRes = new ResourceLocation("rpmachine", "models/machine1.png");
        this.context = new RenderContext();
        this.item = new EntityItem((World)null);
        this.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileAccel accel = (TileAccel)tile;
        final World world = accel.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.setOrientation(accel.Rotation, 0);
        this.context.readGlobalLights((IBlockAccess)world, accel.xCoord, accel.yCoord, accel.zCoord);
        if (accel.Charged) {
            this.context.setBrightness(15728880);
        }
        else {
            this.context.setBrightness(this.getMixedBrightness((TileEntity)accel));
        }
        this.context.bindTexture(this.modelRes);
        tess.startDrawingQuads();
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1 + (accel.Charged ? 1 : 0));
        if (accel.Charged) {
            this.context.setBrightness(this.getMixedBrightness((TileEntity)accel));
        }
        accel.recache();
        if ((accel.conCache & 0x1) > 0) {
            this.context.renderModelGroup(2, 2);
        }
        if ((accel.conCache & 0x2) > 0) {
            this.context.renderModelGroup(2, 1);
        }
        if ((accel.conCache & 0x4) > 0) {
            this.context.renderModelGroup(3, 2);
        }
        if ((accel.conCache & 0x8) > 0) {
            this.context.renderModelGroup(3, 1);
        }
        tess.draw();
        this.item.worldObj = world;
        this.item.setPosition(accel.xCoord + 0.5, accel.yCoord + 0.5, accel.zCoord + 0.5);
        final RenderItem renderitem = (RenderItem)RenderManager.instance.getEntityClassRenderObject((Class)EntityItem.class);
        this.item.age = 0;
        this.item.hoverStart = 0.0f;
        final WorldCoord offset = new WorldCoord(0, 0, 0);
        final TubeFlow flow = accel.getTubeFlow();
        final int lv = accel.getWorldObj().getLightBrightnessForSkyBlocks(accel.xCoord, accel.yCoord, accel.zCoord, 0);
        for (final TubeItem item : flow.contents) {
            this.item.setEntityItemStack(item.item);
            offset.x = 0;
            offset.y = 0;
            offset.z = 0;
            offset.step((int)item.side);
            double d = item.progress / 128.0 * 0.5;
            if (!item.scheduled) {
                d = 0.5 - d;
            }
            double yo = 0.0;
            if (Item.getIdFromItem(item.item.getItem()) >= 256) {
                yo += 0.1;
            }
            renderitem.doRender(this.item, x + 0.5 + offset.x * d, y + 0.5 - this.item.yOffset - yo + offset.y * d, z + 0.5 + offset.z * d, 0.0f, 0.0f);
            if (item.color > 0) {
                this.context.bindBlockTexture();
                tess.startDrawingQuads();
                this.context.useNormal = true;
                this.context.setDefaults();
                tess.setBrightness(lv);
                this.context.setPos(x + offset.x * d, y + offset.y * d, z + offset.z * d);
                this.context.setTintHex(this.paintColors[item.color - 1]);
                this.context.setIcon(RedPowerMachine.tubeItemOverlay);
                this.context.renderBox(63, 0.25999999046325684, 0.25999999046325684, 0.25999999046325684, 0.7400000095367432, 0.7400000095367432, 0.7400000095367432);
                tess.draw();
            }
        }
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        this.block.setBlockBoundsForItemRender();
        this.context.setDefaults();
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            this.context.setPos(-0.5, -0.5, -0.5);
        }
        else {
            this.context.setPos(0.0, 0.0, 0.0);
        }
        this.context.setOrientation(2, 0);
        this.context.bindTexture(this.modelRes);
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        this.context.useNormal = true;
        this.context.bindModelOffset(this.model, 0.5, 0.5, 0.5);
        this.context.renderModelGroup(0, 0);
        this.context.renderModelGroup(1, 1);
        this.context.useNormal = false;
        tess.draw();
    }
}
