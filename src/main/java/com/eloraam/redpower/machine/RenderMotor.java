package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import com.eloraam.redpower.core.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderMotor extends RenderCustomBlock
{
    protected RenderContext context;
    
    public RenderMotor(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileMotor motor = (TileMotor)tile;
        final World world = motor.getWorldObj();
        final Tessellator tess = Tessellator.instance;
        GL11.glDisable(2896);
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, motor.xCoord, motor.yCoord, motor.zCoord);
        this.context.setTexFlags(64);
        if (motor.Active) {
            this.context.setIcon(RedPowerMachine.motorBottom, RedPowerMachine.motorTopActive, RedPowerMachine.motorFrontActive, RedPowerMachine.motorFrontActive, RedPowerMachine.motorSide, RedPowerMachine.motorSide);
        }
        else {
            final IIcon tx = motor.Charged ? RedPowerMachine.motorFrontCharged : RedPowerMachine.motorFront;
            this.context.setIcon(RedPowerMachine.motorBottom, RedPowerMachine.motorTop, tx, tx, RedPowerMachine.motorSide, RedPowerMachine.motorSide);
        }
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        this.context.orientTextureNew(motor.Rotation);
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
        this.context.setIcon(RedPowerMachine.motorBottom, RedPowerMachine.motorTop, RedPowerMachine.motorFront, RedPowerMachine.motorFront, RedPowerMachine.motorSide, RedPowerMachine.motorSide);
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
    }
}
