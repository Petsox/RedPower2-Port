
package com.eloraam.redpower.nei;

import java.awt.*;
import codechicken.nei.recipe.*;
import com.eloraam.redpower.base.*;
import net.minecraft.item.*;
import net.minecraft.client.resources.*;
import codechicken.nei.*;
import com.eloraam.redpower.core.*;
import cpw.mods.fml.common.*;
import net.minecraftforge.oredict.*;
import codechicken.lib.inventory.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class AlloyFurnaceRecipeHandler extends FurnaceRecipeHandler implements ICraftingHandler, IUsageHandler
{
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(11, 12, 18, 18), "fuel", new Object[0]));
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(102, 23, 24, 18), "alloy", new Object[0]));
    }
    
    public Class<GuiAlloyFurnace> getGuiClass() {
        return GuiAlloyFurnace.class;
    }
    
    public void loadCraftingRecipes(final String outputId, final Object... results) {
        if (outputId.equals("alloy") && this.getClass() == AlloyFurnaceRecipeHandler.class) {
            final List<List<Object>> allrecipes = (List<List<Object>>)CraftLib.alloyRecipes;
            for (final List<Object> lrecipe : allrecipes) {
                this.arecipes.add(new CachedAlloyRecipe(lrecipe));
            }
        }
        else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    public void loadCraftingRecipes(final ItemStack result) {
        final List<List<Object>> allrecipes = (List<List<Object>>)CraftLib.alloyRecipes;
        for (final List<Object> lrecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack)lrecipe.get(1), result)) {
                this.arecipes.add(new CachedAlloyRecipe(lrecipe));
            }
        }
    }
    
    public void loadUsageRecipes(final String inputId, final Object... ingredients) {
        if (inputId.equals("fuel") && this.getClass() == AlloyFurnaceRecipeHandler.class) {
            this.loadCraftingRecipes("alloy", new Object[0]);
        }
        else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }
    
    public void loadUsageRecipes(final ItemStack ingredient) {
        final List<List<Object>> allrecipes = (List<List<Object>>)CraftLib.alloyRecipes;
        for (final List<Object> lrecipe : allrecipes) {
            final CachedAlloyRecipe recipe = new CachedAlloyRecipe(lrecipe);
            if (recipe.contains((Collection)recipe.ingredients, ingredient)) {
                this.arecipes.add(recipe);
            }
        }
    }
    
    public String getRecipeName() {
        return I18n.format("tile.rpafurnace.name", new Object[0]);
    }
    
    public String getGuiTexture() {
        return "rpbase:textures/gui/afurnacegui.png";
    }
    
    public void drawExtras(final int recipe) {
        this.drawProgressBar(12, 14, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(102, 23, 176, 14, 24, 16, 48, 0);
    }
    
    public String getOverlayIdentifier() {
        return "alloy";
    }
    
    public class CachedAlloyRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        List<PositionedStack> ingredients;
        ItemStack result;
        
        public CachedAlloyRecipe(final Object[] ingreds, final ItemStack result) {
            super();
            this.ingredients = new ArrayList<PositionedStack>();
            for (int i = 0; i < ingreds.length; ++i) {
                Object ingred = null;
                if (!(ingreds[i] instanceof OreStack)) {
                    if (ingreds[i] instanceof ItemStack) {
                        ingred = ingreds[i];
                    }
                    else {
                        FMLCommonHandler.instance().raiseException((Throwable)new ClassCastException("not an ItemStack or OreStack"), "NEI", false);
                    }
                }
                else {
                    final OreStack ore = (OreStack)ingreds[i];
                    final List<ItemStack> list = new ArrayList<ItemStack>(OreDictionary.getOres(ore.material));
                    for (int j = 0; j < list.size(); ++j) {
                        list.set(j, InventoryUtils.copyStack((ItemStack)list.get(j), ore.quantity));
                    }
                    ingred = list;
                }
                this.ingredients.add(new PositionedStack(ingred, 43 + i * 18, 6 + i / 3 * 18));
            }
            this.result = result;
        }
        
        public CachedAlloyRecipe( final List<Object> lrecipe) {
            this((Object[]) lrecipe.get(0), (ItemStack) lrecipe.get(1));
        }

        public PositionedStack getResult() {
            return new PositionedStack((Object)this.result, 136, 24);
        }
        
        public List<PositionedStack> getIngredients() {
            return this.ingredients;
        }
        
        public List<PositionedStack> getOtherStacks() {
            final List<PositionedStack> slots = new ArrayList<PositionedStack>();
            slots.add(new PositionedStack((Object)AlloyFurnaceRecipeHandler.afuels.get(AlloyFurnaceRecipeHandler.this.cycleticks / 48 % AlloyFurnaceRecipeHandler.afuels.size()).stack.item, 12, 31));
            return slots;
        }
    }
    
    public class AlloyDupeComparator implements Comparator<List<Object>>
    {
        @Override
        public int compare(final List<Object> o1, final List<Object> o2) {
            final ItemStack result1 = (ItemStack) o1.get(1);
            final ItemStack result2 = (ItemStack) o2.get(1);
            final int resultcompare = NEIServerUtils.compareStacks(result1, result2);
            if (resultcompare != 0) {
                return resultcompare;
            }
            final ItemStack[] ingreds1 = (ItemStack[]) o1.get(0);
            final ItemStack[] ingreds2 = (ItemStack[]) o2.get(0);
            final int lengthcompare = Integer.valueOf(ingreds1.length).compareTo(ingreds2.length);
            if (lengthcompare != 0) {
                return lengthcompare;
            }
            for (int i = 0; i < ingreds1.length; ++i) {
                final int ingredcompare = NEIServerUtils.compareStacks(ingreds1[i], ingreds2[i]);
                if (ingredcompare != 0) {
                    return ingredcompare;
                }
            }
            return 0;
        }
    }
}
