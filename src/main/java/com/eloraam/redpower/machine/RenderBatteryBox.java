package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderBatteryBox extends RenderCustomBlock
{
    protected RenderContext context;
    
    public RenderBatteryBox(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileBatteryBox battery = (TileBatteryBox)tile;
        final World world = tile.getWorldObj();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, tile.xCoord, tile.yCoord, tile.zCoord);
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        final IIcon side = RedPowerMachine.batterySide[battery.getStorageForRender()];
        this.context.setIcon(RedPowerMachine.electronicsBottom, RedPowerMachine.batteryTop, side, side, side, side);
        tess.startDrawingQuads();
        this.context.renderGlobFaces(63);
        tess.draw();
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
        this.context.useNormal = true;
        final Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        short bat = 0;
        if (item.stackTagCompound != null) {
            bat = item.stackTagCompound.getShort("batLevel");
        }
        final IIcon side = RedPowerMachine.batterySide[bat * 8 / 6000];
        this.context.setIcon(RedPowerMachine.electronicsBottom, RedPowerMachine.batteryTop, side, side, side, side);
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
    }
}
