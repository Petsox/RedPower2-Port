
package com.eloraam.redpower.core;

import net.minecraft.client.particle.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;

@SideOnly(Side.CLIENT)
public class EntityCustomDiggingFX extends EntityFX
{
    public EntityCustomDiggingFX(final World world, final double x, final double y, final double z, final double mx, final double my, final double mz, final IIcon icon, final int color) {
        super(world, x, y, z, mx, my, mz);
        this.setParticleIcon(icon);
        this.particleGravity = 1.0f;
        final float particleRed = 0.6f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale /= 2.0f;
        this.particleRed *= (color >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (color >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (color & 0xFF) / 255.0f;
    }
    
    public int getFXLayer() {
        return 1;
    }
    
    public void renderParticle(final Tessellator tess, final float p_70539_2_, final float p_70539_3_, final float p_70539_4_, final float p_70539_5_, final float p_70539_6_, final float p_70539_7_) {
        float f6 = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float f7 = f6 + 0.015609375f;
        float f8 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float f9 = f8 + 0.015609375f;
        final float f10 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            f6 = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0f * 16.0f));
            f7 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f));
            f8 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0f * 16.0f));
            f9 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f));
        }
        final float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_70539_2_ - EntityCustomDiggingFX.interpPosX);
        final float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_70539_2_ - EntityCustomDiggingFX.interpPosY);
        final float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_70539_2_ - EntityCustomDiggingFX.interpPosZ);
        tess.setColorOpaque_F(this.particleRed, this.particleGreen, this.particleBlue);
        tess.addVertexWithUV((double)(f11 - p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 - p_70539_7_ * f10), (double)f6, (double)f9);
        tess.addVertexWithUV((double)(f11 - p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 - p_70539_5_ * f10 + p_70539_7_ * f10), (double)f6, (double)f8);
        tess.addVertexWithUV((double)(f11 + p_70539_3_ * f10 + p_70539_6_ * f10), (double)(f12 + p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 + p_70539_7_ * f10), (double)f7, (double)f8);
        tess.addVertexWithUV((double)(f11 + p_70539_3_ * f10 - p_70539_6_ * f10), (double)(f12 - p_70539_4_ * f10), (double)(f13 + p_70539_5_ * f10 - p_70539_7_ * f10), (double)f7, (double)f9);
    }
}
