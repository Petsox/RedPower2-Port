//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.nei;

import codechicken.nei.recipe.*;
import net.minecraft.client.gui.inventory.*;
import java.util.*;
import codechicken.nei.*;
import net.minecraft.inventory.*;

public class AlloyFurnaceOverlayHandler extends DefaultOverlayHandler
{
    public Slot[][] mapIngredSlots(final GuiContainer gui, final List<PositionedStack> ingredients) {
        final Slot[][] map = super.mapIngredSlots(gui, (List)ingredients);
        final Slot[] ingredSlots = new Slot[9];
        for (int i = 0; i < 9; ++i) {
            ingredSlots[i] = (Slot) gui.inventorySlots.inventorySlots.get(i);
        }
        for (int i = 0; i < map.length; ++i) {
            map[i] = ingredSlots;
        }
        return map;
    }
}
