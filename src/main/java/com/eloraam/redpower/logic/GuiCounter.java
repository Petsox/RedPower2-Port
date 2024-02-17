package com.eloraam.redpower.logic;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import com.google.common.primitives.*;
import com.eloraam.redpower.core.*;
import com.eloraam.redpower.*;
import cpw.mods.fml.common.network.simpleimpl.*;

public class GuiCounter extends GuiContainer
{
    private TileLogicStorage tileLogic;
    private GuiButton[] buttons;
    private ResourceLocation guiRes;
    
    public GuiCounter(final InventoryPlayer pli, final TileLogicStorage te) {
        super((Container)new ContainerCounter((IInventory)pli, te));
        this.buttons = new GuiButton[18];
        this.guiRes = new ResourceLocation("rplogic", "textures/gui/countergui.png");
        super.xSize = 228;
        super.ySize = 117;
        this.tileLogic = te;
    }
    
    public GuiCounter(final Container cn) {
        super(cn);
        this.buttons = new GuiButton[18];
        this.guiRes = new ResourceLocation("rplogic", "textures/gui/countergui.png");
        super.xSize = 228;
        super.ySize = 117;
    }
    
    public void initGui() {
        super.initGui();
        final int bw = super.xSize - 20;
        final int l = (super.width - super.xSize) / 2;
        final int m = (super.height - super.ySize) / 2;
        super.buttonList.add(this.buttons[0] = new GuiButton(1, l + 10, m + 20, bw / 6, 20, "-25"));
        super.buttonList.add(this.buttons[1] = new GuiButton(2, l + 10 + bw / 6, m + 20, bw / 6, 20, "-5"));
        super.buttonList.add(this.buttons[2] = new GuiButton(3, l + 10 + bw * 2 / 6, m + 20, bw / 6, 20, "-1"));
        super.buttonList.add(this.buttons[3] = new GuiButton(4, l + 10 + bw * 3 / 6, m + 20, bw / 6, 20, "+1"));
        super.buttonList.add(this.buttons[4] = new GuiButton(5, l + 10 + bw * 4 / 6, m + 20, bw / 6, 20, "+5"));
        super.buttonList.add(this.buttons[5] = new GuiButton(6, l + 10 + bw * 5 / 6, m + 20, bw / 6, 20, "+25"));
        super.buttonList.add(this.buttons[6] = new GuiButton(7, l + 10, m + 55, bw / 6, 20, "-25"));
        super.buttonList.add(this.buttons[7] = new GuiButton(8, l + 10 + bw / 6, m + 55, bw / 6, 20, "-5"));
        super.buttonList.add(this.buttons[8] = new GuiButton(9, l + 10 + bw * 2 / 6, m + 55, bw / 6, 20, "-1"));
        super.buttonList.add(this.buttons[9] = new GuiButton(10, l + 10 + bw * 3 / 6, m + 55, bw / 6, 20, "+1"));
        super.buttonList.add(this.buttons[10] = new GuiButton(11, l + 10 + bw * 4 / 6, m + 55, bw / 6, 20, "+5"));
        super.buttonList.add(this.buttons[11] = new GuiButton(12, l + 10 + bw * 5 / 6, m + 55, bw / 6, 20, "+25"));
        super.buttonList.add(this.buttons[12] = new GuiButton(13, l + 10, m + 90, bw / 6, 20, "-25"));
        super.buttonList.add(this.buttons[13] = new GuiButton(14, l + 10 + bw / 6, m + 90, bw / 6, 20, "-5"));
        super.buttonList.add(this.buttons[14] = new GuiButton(15, l + 10 + bw * 2 / 6, m + 90, bw / 6, 20, "-1"));
        super.buttonList.add(this.buttons[15] = new GuiButton(16, l + 10 + bw * 3 / 6, m + 90, bw / 6, 20, "+1"));
        super.buttonList.add(this.buttons[16] = new GuiButton(17, l + 10 + bw * 4 / 6, m + 90, bw / 6, 20, "+5"));
        super.buttonList.add(this.buttons[17] = new GuiButton(18, l + 10 + bw * 5 / 6, m + 90, bw / 6, 20, "+25"));
    }
    
    protected void drawGuiContainerForegroundLayer(final int p1, final int p2) {
    }
    
    protected void drawGuiContainerBackgroundLayer(final float f, final int p1, final int p2) {
        final FontRenderer fontrenderer = super.mc.fontRenderer;
        final TileLogicStorage.LogicStorageCounter lsc = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        super.mc.renderEngine.bindTexture(this.guiRes);
        final int l = (super.width - super.xSize) / 2;
        final int m = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(l, m, 0, 0, super.xSize, super.ySize);
        String str = String.format("Maximum Count: %d", lsc.CountMax);
        this.drawCenteredString(fontrenderer, str, super.width / 2, m + 10, -1);
        str = String.format("Increment: %d", lsc.Inc);
        this.drawCenteredString(fontrenderer, str, super.width / 2, m + 45, -1);
        str = String.format("Decrement: %d", lsc.Dec);
        this.drawCenteredString(fontrenderer, str, super.width / 2, m + 80, -1);
    }
    
    public void changeCountMax(final int cc) {
        final TileLogicStorage.LogicStorageCounter logicStorageCounter;
        final TileLogicStorage.LogicStorageCounter lsc = logicStorageCounter = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        logicStorageCounter.CountMax += cc;
        if (lsc.CountMax < 1) {
            lsc.CountMax = 1;
        }
        if (!super.mc.theWorld.isRemote) {
            this.tileLogic.updateBlock();
        }
        else {
            final byte[] i = Ints.toByteArray(lsc.CountMax);
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(1, super.inventorySlots.windowId, i));
        }
    }
    
    public void changeInc(final int cc) {
        final TileLogicStorage.LogicStorageCounter logicStorageCounter;
        final TileLogicStorage.LogicStorageCounter lsc = logicStorageCounter = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        logicStorageCounter.Inc += cc;
        if (lsc.Inc < 1) {
            lsc.Inc = 1;
        }
        if (!super.mc.theWorld.isRemote) {
            this.tileLogic.updateBlock();
        }
        else {
            final byte[] i = Ints.toByteArray(lsc.Inc);
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(2, super.inventorySlots.windowId, i));
        }
    }
    
    public void changeDec(final int cc) {
        final TileLogicStorage.LogicStorageCounter logicStorageCounter;
        final TileLogicStorage.LogicStorageCounter lsc = logicStorageCounter = (TileLogicStorage.LogicStorageCounter)this.tileLogic.getLogicStorage(TileLogicStorage.LogicStorageCounter.class);
        logicStorageCounter.Dec += cc;
        if (lsc.Dec < 1) {
            lsc.Dec = 1;
        }
        if (!super.mc.theWorld.isRemote) {
            this.tileLogic.updateBlock();
        }
        else {
            final byte[] i = Ints.toByteArray(lsc.Dec);
            RedPowerCore.sendPacketToServer((IMessage)new PacketGuiEvent.GuiMessageEvent(3, super.inventorySlots.windowId, i));
        }
    }
    
    protected void actionPerformed(final GuiButton guibutton) {
        if (guibutton.enabled) {
            switch (guibutton.id) {
                case 1: {
                    this.changeCountMax(-25);
                    break;
                }
                case 2: {
                    this.changeCountMax(-5);
                    break;
                }
                case 3: {
                    this.changeCountMax(-1);
                    break;
                }
                case 4: {
                    this.changeCountMax(1);
                    break;
                }
                case 5: {
                    this.changeCountMax(5);
                    break;
                }
                case 6: {
                    this.changeCountMax(25);
                    break;
                }
                case 7: {
                    this.changeInc(-25);
                    break;
                }
                case 8: {
                    this.changeInc(-5);
                    break;
                }
                case 9: {
                    this.changeInc(-1);
                    break;
                }
                case 10: {
                    this.changeInc(1);
                    break;
                }
                case 11: {
                    this.changeInc(5);
                    break;
                }
                case 12: {
                    this.changeInc(25);
                    break;
                }
                case 13: {
                    this.changeDec(-25);
                    break;
                }
                case 14: {
                    this.changeDec(-5);
                    break;
                }
                case 15: {
                    this.changeDec(-1);
                    break;
                }
                case 16: {
                    this.changeDec(1);
                    break;
                }
                case 17: {
                    this.changeDec(5);
                    break;
                }
                case 18: {
                    this.changeDec(25);
                    break;
                }
            }
        }
    }
}
