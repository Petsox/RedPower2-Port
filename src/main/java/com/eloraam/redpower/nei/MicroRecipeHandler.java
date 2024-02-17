package com.eloraam.redpower.nei;

import com.eloraam.redpower.base.*;
import com.eloraam.redpower.core.*;
import codechicken.nei.*;
import com.eloraam.redpower.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import codechicken.nei.recipe.*;
import java.util.*;

public class MicroRecipeHandler extends ShapedRecipeHandler
{
    public static int[] covers;
    public static int[] strips;
    public static int[] corners;
    public static int[] posts;
    public static int[] hollow;
    public static ItemHandsaw[] saws;
    public static int[] materials;
    public static Random rand;
    
    public MicroRecipeHandler() {
        load();
    }
    
    public static void load() {
        if (MicroRecipeHandler.materials == null) {
            final List<Integer> amaterial = new ArrayList<Integer>();
            for (int i = 0; i < 256; ++i) {
                if (CoverLib.getItemStack(i) != null) {
                    amaterial.add(i);
                }
            }
            MicroRecipeHandler.materials = new int[amaterial.size()];
            for (int i = 0; i < amaterial.size(); ++i) {
                MicroRecipeHandler.materials[i] = amaterial.get(i);
            }
        }
    }
    
    public static PositionedStack position(final ItemStack item, final int row, final int col) {
        return new PositionedStack((Object)item, 25 + col * 18, 6 + row * 18);
    }
    
    public String getRecipeName() {
        return "Microblocks";
    }
    
    public void loadCraftingRecipes(ItemStack ingred) {
        ingred = ingred.copy();
        ingred.stackSize = 1;
        if (CoverLib.getMaterial(ingred) != null) {
            this.arecipes.add(new GluingRecipe(MicroRecipeHandler.covers, ingred, -1));
            this.arecipes.add(new GluingRecipe(MicroRecipeHandler.hollow, ingred, -1));
        }
        else if (ingred.getItem() == ItemHandsaw.getItemFromBlock((Block)RedPowerBase.blockMicro) && isValidMicroType(ingred.getItemDamage() >> 8)) {
            final int type = ingred.getItemDamage() >> 8;
            final int material = ingred.getItemDamage() & 0xFF;
            this.addCuttingRecipe(type, material);
            this.addGluingRecipe(type, material);
            this.addPostRecipe(type, material);
        }
    }
    
    private void addPostRecipe(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if (thickness % 2 == 0 && (microclass == MicroRecipeHandler.posts || microclass == MicroRecipeHandler.strips)) {
            this.arecipes.add(new PostRecipe(getMicro(type, material, 1)));
        }
    }
    
    private void addGluingRecipe(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if ((microclass == MicroRecipeHandler.covers || microclass == MicroRecipeHandler.hollow) && thickness > 1) {
            this.arecipes.add(new GluingRecipe(getMicroClass(type), getMicro(type, material, 1), -1));
        }
        if (thicknessPow2(thickness) && (microclass == MicroRecipeHandler.covers || microclass == MicroRecipeHandler.strips)) {
            final int[] subclass = getNextMicroClass(getMicroClass(type), false);
            if (microclass == MicroRecipeHandler.covers) {
                this.arecipes.add(new GluingRecipe(getMicro(getNextMicroClass(subclass, false)[thickness - 1], material, 4), getMicro(type, material, 1)));
            }
            this.arecipes.add(new GluingRecipe(getMicro(subclass[thickness - 1], material, 2), getMicro(type, material, 1)));
        }
    }
    
    private void addCuttingRecipe(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if (microclass != MicroRecipeHandler.covers && microclass != MicroRecipeHandler.hollow) {
            if (microclass == MicroRecipeHandler.strips || microclass == MicroRecipeHandler.corners) {
                this.arecipes.add(new CuttingRecipe(getMicro(type, material, 2), getMicro(getNextMicroClass(microclass, true)[thickness - 1], material, 1), null));
            }
        }
        else if (thickness <= 3 || (microclass == MicroRecipeHandler.covers && thickness == 4)) {
            this.arecipes.add(new CuttingRecipe(getMicro(type, material, 2), getMicro(setThickness(type, thickness * 2), material, 1), null));
        }
    }
    
    public void loadUsageRecipes(ItemStack result) {
        result = result.copy();
        result.stackSize = 1;
        if (CoverLib.getMaterial(result) != null) {
            this.arecipes.add(new CuttingRecipe(getMicro(MicroRecipeHandler.covers[3], getMaterial(result), 2), result, null));
        }
        else if (result.getItem() instanceof ItemHandsaw) {
            this.addSawUsage(result);
        }
        else if (result.getItem() == Item.getItemFromBlock((Block)RedPowerBase.blockMicro) && isValidMicroType(result.getItemDamage() >> 8)) {
            final int type = result.getItemDamage() >> 8;
            final int material = result.getItemDamage() & 0xFF;
            this.addCuttingUsage(type, material);
            this.addGluingUsage(type, material);
            this.addPostUsage(type, material);
        }
    }
    
    private void addSawSplitting(final int[] microclass, final int thicknesses, final ItemStack handsaw) {
        for (int i = thicknesses; i >= 0; --i) {
            this.arecipes.add(new CuttingRecipe(setThickness(microclass[i], (i + 1) * 2), microclass[i], handsaw));
        }
    }
    
    private void addSawCutting(final int[] microclass, final ItemStack handsaw) {
        final int[] superclass = getNextMicroClass(microclass, true);
        for (int i = 6; i >= 0; --i) {
            this.arecipes.add(new CuttingRecipe(superclass[i], microclass[i], handsaw));
        }
    }
    
    private void addPostUsage(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if (thickness % 2 == 0 && (microclass == MicroRecipeHandler.posts || microclass == MicroRecipeHandler.strips)) {
            this.arecipes.add(new PostRecipe(getMicro(swapPostType(type), material, 1)));
        }
    }
    
    private void addGluingUsage(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if (thicknessPow2(thickness) && (microclass == MicroRecipeHandler.corners || microclass == MicroRecipeHandler.strips)) {
            final int[] superclass = getNextMicroClass(microclass, true);
            if (microclass == MicroRecipeHandler.corners) {
                this.arecipes.add(new GluingRecipe(getMicro(type, material, 4), getMicro(getNextMicroClass(superclass, true)[thickness - 1], material, 1)));
            }
            this.arecipes.add(new GluingRecipe(getMicro(type, material, 2), getMicro(superclass[thickness - 1], material, 1)));
        }
        if (microclass == MicroRecipeHandler.covers || microclass == MicroRecipeHandler.hollow) {
            for (int i = thickness + 1; i <= 8; ++i) {
                this.arecipes.add(new GluingRecipe(microclass, getMicro(setThickness(type, i), material, 1), thickness));
            }
        }
    }
    
    private void addCuttingUsage(final int type, final int material) {
        final int thickness = getThickness(type);
        final int[] microclass = getMicroClass(type);
        if (thickness % 2 == 0 && (microclass == MicroRecipeHandler.covers || microclass == MicroRecipeHandler.hollow)) {
            this.arecipes.add(new CuttingRecipe(getMicro(setThickness(type, getThickness(type) / 2), material, 2), getMicro(type, material, 1), null));
        }
        if (microclass == MicroRecipeHandler.covers || microclass == MicroRecipeHandler.strips) {
            this.arecipes.add(new CuttingRecipe(getMicro(getNextMicroClass(microclass, false)[getThickness(type) - 1], material, 2), getMicro(type, material, 1), null));
        }
    }
    
    private void addSawUsage(final ItemStack ingredient) {
        this.addSawSplitting(MicroRecipeHandler.covers, 3, ingredient);
        this.addSawSplitting(MicroRecipeHandler.hollow, 2, ingredient);
        this.addSawCutting(MicroRecipeHandler.strips, ingredient);
        this.addSawCutting(MicroRecipeHandler.corners, ingredient);
    }
    
    public static int swapPostType(final int type) {
        return containsInt(MicroRecipeHandler.posts, type) ? MicroRecipeHandler.strips[getThickness(type) - 1] : MicroRecipeHandler.posts[getThickness(type) - 1];
    }
    
    public static boolean isValidMicroType(final int type) {
        return type == 0 || (type >= 16 && type <= 45);
    }
    
    public static int[] getNextMicroClass(final int[] microclass, final boolean higher) {
        if (higher) {
            if (microclass == MicroRecipeHandler.corners) {
                return MicroRecipeHandler.strips;
            }
            if (microclass == MicroRecipeHandler.strips) {
                return MicroRecipeHandler.covers;
            }
        }
        else {
            if (microclass == MicroRecipeHandler.strips) {
                return MicroRecipeHandler.corners;
            }
            if (microclass == MicroRecipeHandler.covers) {
                return MicroRecipeHandler.strips;
            }
        }
        return null;
    }
    
    public static int getMaterial(final ItemStack stack) {
        return (stack.getItem() == Item.getItemFromBlock(CoverLib.blockCoverPlate)) ? (stack.getItemDamage() & 0xFF) : CoverLib.getMaterial(stack);
    }
    
    public static int getThickness(final int type) {
        return (type == -1) ? 8 : (getIndex(getMicroClass(type), type) + 1);
    }
    
    public static int[] getMicroClass(final int type) {
        return containsInt(MicroRecipeHandler.covers, type) ? MicroRecipeHandler.covers : (containsInt(MicroRecipeHandler.strips, type) ? MicroRecipeHandler.strips : (containsInt(MicroRecipeHandler.corners, type) ? MicroRecipeHandler.corners : (containsInt(MicroRecipeHandler.hollow, type) ? MicroRecipeHandler.hollow : MicroRecipeHandler.posts)));
    }
    
    public static int setThickness(final int type, final int thickness) {
        return (thickness == 8) ? -1 : getMicroClass(type)[thickness - 1];
    }
    
    public static ItemStack getMicro(final int type, final int material, final int quantity) {
        if (type == -1) {
            final ItemStack stack = CoverLib.getItemStack(material).copy();
            stack.stackSize = quantity;
            return stack;
        }
        return new ItemStack(CoverLib.blockCoverPlate, quantity, type << 8 | material);
    }
    
    public static int getType(final ItemStack stack) {
        return (stack.getItem() == Item.getItemFromBlock(CoverLib.blockCoverPlate)) ? (stack.getItemDamage() >> 8) : -1;
    }
    
    public static boolean thicknessPow2(final int thickness) {
        return thickness == 1 || thickness == 2 || thickness == 4;
    }
    
    public static boolean containsInt(final int[] array, final int i) {
        return getIndex(array, i) != -1;
    }
    
    public static int getIndex(final int[] arr, final int i) {
        for (int j = 0; j < arr.length; ++j) {
            if (arr[j] == i) {
                return j;
            }
        }
        return -1;
    }
    
    static {
        MicroRecipeHandler.covers = new int[] { 0, 16, 27, 17, 28, 29, 30 };
        MicroRecipeHandler.strips = new int[] { 21, 22, 39, 23, 40, 41, 42 };
        MicroRecipeHandler.corners = new int[] { 18, 19, 35, 20, 36, 37, 38 };
        MicroRecipeHandler.posts = new int[] { -1, 43, -1, 44, -1, 45, -1 };
        MicroRecipeHandler.hollow = new int[] { 24, 25, 31, 26, 32, 33, 34 };
        MicroRecipeHandler.rand = new Random();
    }
    
    public class CuttingRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        ItemStack saw;
        ItemStack ingred;
        ItemStack result;
        int cycletype;
        List<Integer> cyclemap;
        
        public CuttingRecipe(final ItemStack result, final ItemStack ingred, final ItemStack saw) {
            this.cyclemap = new ArrayList<Integer>();
            this.result = result;
            this.ingred = ingred;
            this.saw = saw;
            this.cycletype = 0;
            this.mapSharpSaws();
        }
        
        public CuttingRecipe(final int typeingred, final int typeresult, final ItemStack saw) {
            this.cyclemap = new ArrayList<Integer>();
            this.result = MicroRecipeHandler.getMicro(typeingred, 0, 1);
            this.ingred = MicroRecipeHandler.getMicro(typeresult, 0, 2);
            this.saw = saw;
            this.cycletype = 1;
            this.mapSoftMaterials();
        }
        
        public void mapSharpSaws() {
            for (int i = 0; i < MicroRecipeHandler.saws.length; ++i) {
                if (MicroRecipeHandler.saws[i].getSharpness() >= CoverLib.getHardness(MicroRecipeHandler.getMaterial(this.ingred))) {
                    this.cyclemap.add(i);
                }
            }
        }
        
        public void mapSoftMaterials() {
            for (int i = 0; i < MicroRecipeHandler.materials.length; ++i) {
                if (((ItemHandsaw)this.saw.getItem()).getSharpness() >= CoverLib.getHardness(MicroRecipeHandler.materials[i])) {
                    this.cyclemap.add(i);
                }
            }
        }
        
        public List<PositionedStack> getIngredients() {
            final int index = this.cyclemap.get(MicroRecipeHandler.this.cycleticks / 20 % this.cyclemap.size());
            if (this.cycletype == 0) {
                this.saw = new ItemStack((Item)MicroRecipeHandler.saws[index]);
            }
            else {
                this.ingred = MicroRecipeHandler.getMicro(MicroRecipeHandler.getType(this.ingred), MicroRecipeHandler.materials[index], 1);
            }
            final List<PositionedStack> ingreds = new ArrayList<PositionedStack>();
            final int type = this.result.getItemDamage() >> 8;
            if (!MicroRecipeHandler.containsInt(MicroRecipeHandler.covers, type) && !MicroRecipeHandler.containsInt(MicroRecipeHandler.hollow, type)) {
                ingreds.add(MicroRecipeHandler.position(this.saw, 1, 0));
            }
            else {
                ingreds.add(MicroRecipeHandler.position(this.saw, 0, 1));
            }
            ingreds.add(MicroRecipeHandler.position(this.ingred, 1, 1));
            return ingreds;
        }
        
        public PositionedStack getResult() {
            final int index = this.cyclemap.get(MicroRecipeHandler.this.cycleticks / 20 % this.cyclemap.size());
            if (this.cycletype == 1) {
                this.result = MicroRecipeHandler.getMicro(MicroRecipeHandler.getType(this.result), MicroRecipeHandler.materials[index], 2);
            }
            return new PositionedStack((Object)this.result, 119, 24);
        }
    }
    
    public class GluingRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        ItemStack result;
        int[] microclass;
        List<LinkedList<Integer>> gluingcombos;
        List<PositionedStack> ingreds;
        int cycletype;
        
        public GluingRecipe(final int[] microclass, final ItemStack result, final int usedthickness) {
            this.ingreds = new ArrayList<PositionedStack>();
            this.result = result;
            this.microclass = microclass;
            this.gluingcombos = (List<LinkedList<Integer>>)ComboGenerator.generate(MicroRecipeHandler.getThickness(MicroRecipeHandler.getType(result)));
            if (usedthickness != -1) {
                ComboGenerator.removeNotContaining((List)this.gluingcombos, usedthickness);
            }
            this.cycletype = 0;
        }
        
        public GluingRecipe(final ItemStack micro, final ItemStack result) {
            this.ingreds = new ArrayList<PositionedStack>();
            this.result = result;
            final ItemStack m = micro.copy();
            m.stackSize = 1;
            for (int i = 0; i < micro.stackSize; ++i) {
                final int pos = (i >= 2) ? (i + 1) : i;
                this.ingreds.add(MicroRecipeHandler.position(m, pos / 3, pos % 3));
            }
            this.cycletype = -1;
        }
        
        public List<PositionedStack> getIngredients() {
            if (this.cycletype == 0) {
                this.ingreds.clear();
                final int cycle = MicroRecipeHandler.this.cycleticks / 20 % this.gluingcombos.size();
                final int material = MicroRecipeHandler.getMaterial(this.result);
                final LinkedList<Integer> combo = this.gluingcombos.get(cycle);
                this.ingreds = new ArrayList<PositionedStack>(combo.size());
                for (int i = 0; i < combo.size(); ++i) {
                    this.ingreds.add(MicroRecipeHandler.position(MicroRecipeHandler.getMicro(this.microclass[combo.get(i) - 1], material, 1), i / 3, i % 3));
                }
            }
            return this.ingreds;
        }
        
        public PositionedStack getResult() {
            return new PositionedStack((Object)this.result, 119, 24);
        }
    }
    
    public class PostRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        int type;
        int material;
        
        public PostRecipe(final ItemStack result) {
            this.type = MicroRecipeHandler.getType(result);
            this.material = MicroRecipeHandler.getMaterial(result);
        }
        
        public List<PositionedStack> getIngredients() {
            return new ArrayList<PositionedStack>(Collections.singletonList(MicroRecipeHandler.position(MicroRecipeHandler.getMicro(MicroRecipeHandler.swapPostType(this.type), this.material, 1), 1, 1)));
        }
        
        public PositionedStack getResult() {
            return new PositionedStack((Object)MicroRecipeHandler.getMicro(this.type, this.material, 1), 119, 24);
        }
    }
}
