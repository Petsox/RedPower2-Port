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
public class RenderMachine extends RenderCustomBlock
{
    protected RenderContext context;
    
    public RenderMachine(final Block block) {
        super(block);
        this.context = new RenderContext();
    }
    
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        final TileMachine machine = (TileMachine)tile;
        final World world = machine.getWorldObj();
        final int metadata = machine.getBlockMetadata();
        GL11.glDisable(2896);
        final Tessellator tess = Tessellator.instance;
        this.context.bindBlockTexture();
        this.context.setDefaults();
        this.context.setLocalLights(0.5f, 1.0f, 0.8f, 0.8f, 0.6f, 0.6f);
        this.context.setPos(x, y, z);
        this.context.readGlobalLights((IBlockAccess)world, machine.xCoord, machine.yCoord, machine.zCoord);
        this.context.setBrightness(this.getMixedBrightness((TileEntity)machine));
        if (machine.getBlockType() == RedPowerMachine.blockMachine) {
            switch (metadata) {
                case 0: {
                    this.context.setIcon(RedPowerMachine.deployerBack, machine.Active ? RedPowerMachine.deployerFrontOn : RedPowerMachine.deployerFront, RedPowerMachine.deployerSideAlt, RedPowerMachine.deployerSideAlt, RedPowerMachine.deployerSide, RedPowerMachine.deployerSide);
                    break;
                }
                case 4: {
                    final IIcon alt = machine.Active ? RedPowerMachine.detectorSideAltOn : RedPowerMachine.detectorSideAlt;
                    IIcon side;
                    if (machine.Charged) {
                        side = (machine.Active ? RedPowerMachine.detectorSideChargedOn : RedPowerMachine.detectorSideCharged);
                    }
                    else {
                        side = (machine.Active ? RedPowerMachine.detectorSideOn : RedPowerMachine.detectorSide);
                    }
                    this.context.setIcon(RedPowerMachine.regulatorBack, RedPowerMachine.regulatorFront, alt, alt, side, side);
                    break;
                }
                case 5: {
                    IIcon side2;
                    if (machine.Charged) {
                        side2 = (machine.Active ? RedPowerMachine.sorterSideChargedOn : RedPowerMachine.sorterSideCharged);
                    }
                    else {
                        side2 = (machine.Active ? RedPowerMachine.sorterSideOn : RedPowerMachine.sorterSide);
                    }
                    this.context.setIcon(machine.Charged ? (machine.Active ? RedPowerMachine.sorterBackChargedOn : RedPowerMachine.sorterBackCharged) : RedPowerMachine.sorterBack, RedPowerMachine.sorterFront, side2, side2, side2, side2);
                    break;
                }
                case 8: {
                    IIcon side2;
                    if (machine.Charged) {
                        side2 = (machine.Active ? RedPowerMachine.retrieverSideChargedOn : RedPowerMachine.retrieverSideCharged);
                    }
                    else {
                        side2 = (machine.Active ? RedPowerMachine.retrieverSideOn : RedPowerMachine.retrieverSide);
                    }
                    this.context.setIcon(RedPowerMachine.retrieverBack, RedPowerMachine.retrieverFront, side2, side2, side2, side2);
                    break;
                }
                case 10: {
                    final IIcon alt = machine.Active ? RedPowerMachine.regulatorSideAltCharged : RedPowerMachine.regulatorSideAlt;
                    IIcon side;
                    if (machine.Powered) {
                        side = (machine.Active ? RedPowerMachine.regulatorSideChargedOn : RedPowerMachine.regulatorSideCharged);
                    }
                    else {
                        side = (machine.Active ? RedPowerMachine.regulatorSideOn : RedPowerMachine.regulatorSide);
                    }
                    this.context.setIcon(RedPowerMachine.regulatorBack, RedPowerMachine.regulatorFront, alt, alt, side, side);
                    break;
                }
                case 12: {
                    this.context.setIcon(RedPowerMachine.breakerBack, machine.Active ? RedPowerMachine.igniterFrontOn : RedPowerMachine.igniterFront, RedPowerMachine.igniterSideAlt, RedPowerMachine.igniterSideAlt, RedPowerMachine.igniterSide, RedPowerMachine.igniterSide);
                    break;
                }
                case 13: {
                    this.context.setIcon(machine.Active ? RedPowerMachine.assemblerBackOn : RedPowerMachine.assemblerBack, machine.Active ? RedPowerMachine.assemblerFrontOn : RedPowerMachine.assemblerFront, RedPowerMachine.assemblerSideAlt, RedPowerMachine.assemblerSideAlt, RedPowerMachine.assemblerSide, RedPowerMachine.assemblerSide);
                    break;
                }
                case 14: {
                    final IIcon side2 = machine.Active ? RedPowerMachine.ejectorSideOn : RedPowerMachine.ejectorSide;
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.bufferFront, side2, side2, RedPowerMachine.relaySideAlt, RedPowerMachine.relaySideAlt);
                    break;
                }
                case 15: {
                    final IIcon side2 = machine.Active ? RedPowerMachine.relaySideOn : RedPowerMachine.relaySide;
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.bufferFront, side2, side2, RedPowerMachine.relaySideAlt, RedPowerMachine.relaySideAlt);
                    break;
                }
                default: {
                    final IIcon side2 = (metadata == 3) ? (machine.Active ? RedPowerMachine.filterSideOn : RedPowerMachine.filterSide) : (machine.Active ? RedPowerMachine.transposerSideOn : RedPowerMachine.transposerSide);
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.transposerFront, side2, side2, side2, side2);
                    break;
                }
            }
        }
        else if (machine.getBlockType() == RedPowerMachine.blockMachine2) {
            switch (metadata) {
                case 0: {
                    final IIcon side2 = machine.Charged ? (machine.Active ? RedPowerMachine.sortronSideChargedOn : RedPowerMachine.sortronSideCharged) : (machine.Active ? RedPowerMachine.sortronSideOn : RedPowerMachine.sortronSide);
                    final IIcon alt2 = machine.Charged ? RedPowerMachine.sortronSideAltCharged : RedPowerMachine.sortronSideAlt;
                    this.context.setIcon(RedPowerMachine.sortronBack, RedPowerMachine.sortronFront, alt2, alt2, side2, side2);
                    break;
                }
                case 1: {
                    final IIcon side2 = (machine.Charged ? RedPowerMachine.managerSideCharged : RedPowerMachine.managerSide)[(machine.Active ? 1 : 0) + ((machine.Delay || machine.Powered) ? 2 : 0)];
                    this.context.setIcon(RedPowerMachine.managerBack, RedPowerMachine.managerFront, side2, side2, side2, side2);
                    break;
                }
            }
        }
        this.context.setSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        this.context.setupBox();
        this.context.transform();
        this.context.orientTextures(machine.Rotation);
        tess.startDrawingQuads();
        this.context.renderGlobFaces(63);
        tess.draw();
        GL11.glEnable(2896);
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        final Block block = Block.getBlockFromItem(item.getItem());
        final int meta = item.getItemDamage();
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
        if (block == RedPowerMachine.blockMachine) {
            switch (meta) {
                case 0: {
                    this.context.setIcon(RedPowerMachine.deployerBack, RedPowerMachine.deployerFront, RedPowerMachine.deployerSideAlt, RedPowerMachine.deployerSideAlt, RedPowerMachine.deployerSide, RedPowerMachine.deployerSide);
                    break;
                }
                case 2: {
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.transposerFront, RedPowerMachine.transposerSide, RedPowerMachine.transposerSide, RedPowerMachine.transposerSide, RedPowerMachine.transposerSide);
                    break;
                }
                case 4: {
                    this.context.setIcon(RedPowerMachine.regulatorBack, RedPowerMachine.regulatorFront, RedPowerMachine.regulatorSideAlt, RedPowerMachine.regulatorSideAlt, RedPowerMachine.regulatorSide, RedPowerMachine.regulatorSide);
                    break;
                }
                case 5: {
                    this.context.setIcon(RedPowerMachine.sorterBack, RedPowerMachine.sorterFront, RedPowerMachine.sorterSide, RedPowerMachine.sorterSide, RedPowerMachine.sorterSide, RedPowerMachine.sorterSide);
                    break;
                }
                case 8: {
                    this.context.setIcon(RedPowerMachine.retrieverBack, RedPowerMachine.retrieverFront, RedPowerMachine.retrieverSide, RedPowerMachine.retrieverSide, RedPowerMachine.retrieverSide, RedPowerMachine.retrieverSide);
                    break;
                }
                case 10: {
                    this.context.setIcon(RedPowerMachine.regulatorBack, RedPowerMachine.regulatorFront, RedPowerMachine.regulatorSide, RedPowerMachine.regulatorSide, RedPowerMachine.regulatorSideAlt, RedPowerMachine.regulatorSideAlt);
                    break;
                }
                case 12: {
                    this.context.setIcon(RedPowerMachine.deployerBack, RedPowerMachine.igniterFront, RedPowerMachine.igniterSideAlt, RedPowerMachine.igniterSideAlt, RedPowerMachine.igniterSide, RedPowerMachine.igniterSide);
                    break;
                }
                case 13: {
                    this.context.setIcon(RedPowerMachine.assemblerBack, RedPowerMachine.assemblerFront, RedPowerMachine.assemblerSideAlt, RedPowerMachine.assemblerSideAlt, RedPowerMachine.assemblerSide, RedPowerMachine.assemblerSide);
                    break;
                }
                case 14: {
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.bufferFront, RedPowerMachine.ejectorSide, RedPowerMachine.ejectorSide, RedPowerMachine.relaySideAlt, RedPowerMachine.relaySideAlt);
                    break;
                }
                case 15: {
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.bufferFront, RedPowerMachine.relaySide, RedPowerMachine.relaySide, RedPowerMachine.relaySideAlt, RedPowerMachine.relaySideAlt);
                    break;
                }
                default: {
                    this.context.setIcon(RedPowerMachine.breakerBack, RedPowerMachine.transposerFront, RedPowerMachine.filterSide, RedPowerMachine.filterSide, RedPowerMachine.filterSide, RedPowerMachine.filterSide);
                    break;
                }
            }
        }
        else if (block == RedPowerMachine.blockMachine2) {
            switch (meta) {
                case 0: {
                    this.context.setIcon(RedPowerMachine.sortronBack, RedPowerMachine.sortronFront, RedPowerMachine.sortronSideAlt, RedPowerMachine.sortronSideAlt, RedPowerMachine.sortronSide, RedPowerMachine.sortronSide);
                    break;
                }
                case 1: {
                    final IIcon side = RedPowerMachine.managerSide[0];
                    this.context.setIcon(RedPowerMachine.managerBack, RedPowerMachine.managerFront, side, side, side, side);
                    break;
                }
            }
        }
        this.context.renderBox(63, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        tess.draw();
        this.context.useNormal = false;
    }
}
