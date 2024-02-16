
package com.eloraam.redpower.machine;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraftforge.client.*;
import net.minecraft.item.*;

@SideOnly(Side.CLIENT)
public class RenderFrameMoving extends RenderCustomBlock
{
    private RenderBlocks rblocks;
    private RenderContext context;
    
    public RenderFrameMoving(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileFrameMoving frame = (TileFrameMoving)tile;
        final World world = frame.getWorldObj();
        final Tessellator tess = Tessellator.instance;
        if (!tile.isInvalid()) {
            final Block block = frame.movingBlock;
            if (block != null) {
                this.context.bindBlockTexture();
                final int lv = world.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);
                tess.setBrightness(lv);
                RenderHelper.disableStandardItemLighting();
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(2884);
                if (Minecraft.isAmbientOcclusionEnabled()) {
                    GL11.glShadeModel(7425);
                }
                else {
                    GL11.glShadeModel(7424);
                }
                final IBlockAccess wba = this.rblocks.blockAccess;
                this.rblocks.blockAccess = frame.getFrameBlockAccess();
                final TileMotor tm = (TileMotor)CoreLib.getTileEntity((IBlockAccess)frame.getWorldObj(), frame.motorX, frame.motorY, frame.motorZ, (Class)TileMotor.class);
                GL11.glPushMatrix();
                if (tm != null) {
                    final WorldCoord wc = new WorldCoord(0, 0, 0);
                    wc.step(tm.MoveDir);
                    final float ms = tm.getMoveScaled();
                    GL11.glTranslatef(wc.x * ms, wc.y * ms, wc.z * ms);
                }
                tess.setTranslation(x - frame.xCoord, y - frame.yCoord, z - frame.zCoord);
                tess.setColorOpaque(1, 1, 1);
                Label_0510: {
                    if (frame.movingCrate) {
                        this.context.setDefaults();
                        this.context.setBrightness(lv);
                        this.context.setPos((double)frame.xCoord, (double)frame.yCoord, (double)frame.zCoord);
                        this.context.setIcon(RedPowerMachine.crate);
                        tess.startDrawingQuads();
                        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                        tess.draw();
                    }
                    else {
                        frame.doRefresh(frame.getFrameBlockAccess());
                        if (frame.movingTileEntity != null) {
                            final TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(frame.movingTileEntity);
                            if (tesr != null) {
                                try {
                                    final double tileX = frame.xCoord;
                                    final double tileY = frame.yCoord;
                                    final double tileZ = frame.zCoord;
                                    tesr.renderTileEntityAt(frame.movingTileEntity, tileX, tileY, tileZ, partialTicks);
                                }
                                catch (Exception exc) {
                                    try {
                                        tess.draw();
                                    }
                                    catch (Exception ex) {}
                                }
                                break Label_0510;
                            }
                        }
                        tess.startDrawingQuads();
                        this.rblocks.renderAllFaces = true;
                        this.rblocks.renderBlockByRenderType(block, frame.xCoord, frame.yCoord, frame.zCoord);
                        this.rblocks.renderAllFaces = false;
                        tess.draw();
                    }
                }
                tess.setTranslation(0.0, 0.0, 0.0);
                GL11.glPopMatrix();
                this.rblocks.blockAccess = wba;
                RenderHelper.enableStandardItemLighting();
            }
        }
    }
    
    public void func_147496_a(final World world) {
        this.rblocks = new RenderBlocks((IBlockAccess)world);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
    }
}
