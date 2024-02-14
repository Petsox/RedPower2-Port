//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.base;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.resources.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraft.inventory.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;

public class GuiAdvBench extends GuiContainer
{
    private static final ResourceLocation res;
    private TileAdvBench bench;
    
    public GuiAdvBench(final InventoryPlayer pli, final TileAdvBench td) {
        super(new ContainerAdvBench(pli, td));
        this.bench = td;
        super.ySize = 222;
    }
    
    public GuiAdvBench(final Container cn) {
        super(cn);
        super.ySize = 222;
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
        super.fontRendererObj.drawString(I18n.format("tile.rpabench.name"), 60, 6, 4210752);
        super.fontRendererObj.drawString(I18n.format("container.inventory"), 8, super.ySize - 96 + 2, 4210752);
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(GuiAdvBench.res);
        final int j = (super.width - super.xSize) / 2;
        final int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        final ItemStack plan = super.inventorySlots.getSlot(9).getStack();
        final ItemStack craft = super.inventorySlots.getSlot(10).getStack();
        if (plan != null && craft != null && plan.getItem() == RedPowerBase.itemPlanBlank) {
            this.drawTexturedModalRect(j + 18, k + 55, 176, 0, 14, 14);
        }
        if (plan != null && plan.getItem() == RedPowerBase.itemPlanFull) {
            final ContainerAdvBench cont = (ContainerAdvBench)super.inventorySlots;
            final ItemStack[] ist = ContainerAdvBench.getShadowItems(plan);
            RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(32826);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            for (int n = 0; n < 9; ++n) {
                if (ist[n] != null) {
                    final Slot sl = super.inventorySlots.getSlot(n);
                    if (sl.getStack() == null) {
                        final int slx = sl.xDisplayPosition + j;
                        final int sly = sl.yDisplayPosition + k;
                        GuiContainer.itemRender.renderItemIntoGUI(super.fontRendererObj, super.mc.renderEngine, ist[n], slx, sly);
                        GuiContainer.itemRender.renderItemOverlayIntoGUI(super.fontRendererObj, super.mc.renderEngine, ist[n], slx, sly);
                    }
                }
            }
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            super.mc.renderEngine.bindTexture(GuiAdvBench.res);
            for (int n = 0; n < 9; ++n) {
                if (ist[n] != null) {
                    final Slot sl = super.inventorySlots.getSlot(n);
                    if (sl.getStack() == null) {
                        final int slx = sl.xDisplayPosition;
                        final int sly = sl.yDisplayPosition;
                        if ((cont.satisfyMask & 1 << n) > 0) {
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
                        }
                        else {
                            GL11.glColor4f(1.0f, 0.1f, 0.1f, 0.6f);
                        }
                        this.drawTexturedModalRect(j + slx, k + sly, slx, sly, 16, 16);
                    }
                }
            }
            GL11.glDisable(3042);
        }
    }
    
    protected void mouseClicked(final int i, final int j, final int k) {
        final int x = i - (super.width - super.xSize) / 2;
        final int y = j - (super.height - super.ySize) / 2;
        if (x >= 18 && y >= 55 && x <= 32 && y <= 69) {
            final ItemStack plan = super.inventorySlots.getSlot(9).getStack();
            final ItemStack craft = super.inventorySlots.getSlot(10).getStack();
            if (plan == null || craft == null || plan.getItem() != RedPowerBase.itemPlanBlank) {
                return;
            }
            RedPowerCore.sendPacketToServer(new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, new byte[0]));
        }
        super.mouseClicked(i, j, k);
    }
    
    static {
        res = new ResourceLocation("rpbase", "textures/gui/advbench.png");
    }
}
