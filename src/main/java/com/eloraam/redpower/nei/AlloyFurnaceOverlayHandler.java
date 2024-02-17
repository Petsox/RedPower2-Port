package com.eloraam.redpower.nei;

import codechicken.nei.recipe.*;
import net.minecraft.client.gui.inventory.*;
import java.util.*;
import codechicken.nei.*;
import net.minecraft.inventory.*;

public class AlloyFurnaceOverlayHandler extends DefaultOverlayHandler
{
    public Slot[][] mapIngredSlots(final GuiContainer gui, final List<PositionedStack> ingredients) {
        final Slot[][] map = super.mapIngredSlots(gui, ingredients);
        final Slot[] ingredSlots = new Slot[9];
        for (int i = 0; i < 9; ++i) {
            ingredSlots[i] = (Slot) gui.inventorySlots.inventorySlots.get(i);
        }
        Arrays.fill(map, ingredSlots);
        return map;
    }
}
