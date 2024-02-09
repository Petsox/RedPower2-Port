package com.eloraam.redpower.machine;

import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.RenderContext;
import com.eloraam.redpower.core.RenderCustomBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class RenderFilter extends RenderCustomBlock {

	protected RenderContext context = new RenderContext();

	public RenderFilter(Block bl) {
		super(bl);
	}
	
	@Override
	public void randomDisplayTick(World world, int i, int j, int k,
			Random random) {
	}
	
	@Override
	public void renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iba, int i, int j, int k, int md) {
		TileFilter tb = (TileFilter) CoreLib.getTileEntity(iba, i, j, k, TileFilter.class);
		if (tb != null) {
			this.context.setTexRotation(renderblocks, CoreLib.getFacing(tb.Rotation), true);
			renderblocks.renderStandardBlock(block, i, j, k);
			this.context.resetTexRotation(renderblocks);
		}
	}
	
	@Override
	public void renderInvBlock(RenderBlocks renderblocks, int md) {
		this.context.bindBlockTexture();
		super.block.setBlockBoundsForItemRender();
		this.context.setDefaults();
		this.context.setPos(-0.5D, -0.5D, -0.5D);
		this.context.useNormal = true;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		IIcon sideIcon = getIcon(ForgeDirection.UNKNOWN.ordinal(), md);
		IIcon topIcon = getIcon(ForgeDirection.UP.ordinal(), md);
		IIcon bottomIcon = getIcon(ForgeDirection.DOWN.ordinal(), md);
		this.context.setIcon(bottomIcon, topIcon, sideIcon, sideIcon, sideIcon, sideIcon);
		this.context.renderBox(63, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		tessellator.draw();
		this.context.useNormal = false;
	}
}
