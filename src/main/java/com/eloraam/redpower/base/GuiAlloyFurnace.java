//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;

public class GuiAlloyFurnace extends GuiContainer
{
    private static final ResourceLocation res;
    private TileAlloyFurnace furnace;
    
    public GuiAlloyFurnace(final InventoryPlayer pli, final TileAlloyFurnace td) {
        super(new ContainerAlloyFurnace(pli, td));
        this.furnace = td;
    }
    
    public GuiAlloyFurnace(final Container cn) {
        super(cn);
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpafurnace.name"), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory"), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiAlloyFurnace.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        if (this.furnace.burntime > 0) {
            final int i1 = this.furnace.getBurnScaled(12);
            this.drawTexturedModalRect(j + 17, k + 25 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }
        final int i1 = this.furnace.getCookScaled(24);
        this.drawTexturedModalRect(j + 107, k + 34, 176, 14, i1 + 1, 16);
    }
    
    static {
        res = new ResourceLocation("rpbase", "textures/gui/afurnacegui.png");
    }
}
