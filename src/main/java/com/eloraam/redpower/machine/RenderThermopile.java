package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import com.eloraam.redpower.machine.TileThermopile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RenderThermopile extends RenderCustomBlock {
	
	protected RenderContext context = new RenderContext();
	
	public RenderThermopile(Block bl) {
		super(bl);
	}
	
	@Override
	public void randomDisplayTick(World world, int i, int j, int k,
			Random random) {
	}
	
	@Override
	public void renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iba, int i, int j, int k, int md) {
		TileThermopile tb = (TileThermopile) CoreLib.getTileEntity(iba, i, j, k, TileThermopile.class);
		if (tb != null) {
			renderblocks.renderStandardBlock(block, i, j, k);
			renderblocks.setRenderBoundsFromBlock(block);
		}
	}
	
	@Override
	public void renderInvBlock(RenderBlocks renderblocks, int md) {
		super.block.setBlockBoundsForItemRender();
		this.context.setDefaults();
		this.context.setPos(-0.5D, -0.5D, -0.5D);
		this.context.useNormal = true;
		Tessellator tessellator = Tessellator.instance;
		IIcon topIcon = getIcon(ForgeDirection.UP.ordinal(), md);
		IIcon bottomIcon = getIcon(ForgeDirection.DOWN.ordinal(), md);
		IIcon sideIcon = getIcon(ForgeDirection.UNKNOWN.ordinal(), md);
		IIcon frontIcon = getIcon(ForgeDirection.NORTH.ordinal(), md);
		tessellator.startDrawingQuads();
		this.context.setIcon(bottomIcon, topIcon, getIcon(11, md), sideIcon, sideIcon, frontIcon);
		this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		tessellator.draw();
		this.context.useNormal = false;
	}
}
