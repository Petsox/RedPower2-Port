package com.eloraam.redpower.core;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.*;

@SideOnly(Side.CLIENT)
public abstract class RenderCovers extends RenderCustomBlock
{
    protected CoverRenderer coverRenderer;
    protected RenderContext context;
    
    public RenderCovers(final Block block) {
        super(block);
        this.context = new RenderContext();
        this.coverRenderer = new CoverRenderer(this.context);
    }
    
    public void renderCovers(final int uc, final short[] covs) {
        this.coverRenderer.render(uc, covs);
    }
}
