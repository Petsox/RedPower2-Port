
package com.eloraam.redpower.core;

import net.minecraft.block.*;
import java.lang.reflect.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import cpw.mods.fml.common.registry.*;

public class BlockReplaceHelper
{
    public static void replaceBlock(final Block toReplace, final Class<? extends Block> blockClass, final Class<? extends ItemBlock> itemBlockClass) {
        final Class<?>[] classTest = (Class<?>[])new Class[4];
        Exception exception = null;
        try {
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            for (final Field blockField : Blocks.class.getDeclaredFields()) {
                if (Block.class.isAssignableFrom(blockField.getType())) {
                    final Block block = (Block)blockField.get(null);
                    if (block == toReplace) {
                        final String registryName = Block.blockRegistry.getNameForObject((Object)block);
                        final int id = Block.getIdFromBlock(block);
                        final Block newBlock = (Block)blockClass.newInstance();
                        final FMLControlledNamespacedRegistry<Block> registryBlocks = (FMLControlledNamespacedRegistry<Block>)GameData.getBlockRegistry();
                        final Field map1 = RegistrySimple.class.getDeclaredFields()[1];
                        map1.setAccessible(true);
                        ((Map)map1.get(registryBlocks)).put(registryName, newBlock);
                        final Field map2 = RegistryNamespaced.class.getDeclaredFields()[0];
                        map2.setAccessible(true);
                        ((ObjectIntIdentityMap)map2.get(registryBlocks)).func_148746_a((Object)newBlock, id);
                        blockField.setAccessible(true);
                        modifiersField.setInt(blockField, blockField.getModifiers() & 0xFFFFFFEF);
                        blockField.set(null, newBlock);
                        final ItemBlock itemBlock = (ItemBlock)itemBlockClass.getConstructor(Block.class).newInstance(newBlock);
                        final FMLControlledNamespacedRegistry<Item> registryItems = (FMLControlledNamespacedRegistry<Item>)GameData.getItemRegistry();
                        ((Map)map1.get(registryItems)).put(registryName, itemBlock);
                        ((ObjectIntIdentityMap)map2.get(registryItems)).func_148746_a((Object)itemBlock, id);
                        classTest[0] = blockField.get(null).getClass();
                        classTest[1] = Block.blockRegistry.getObjectById(id).getClass();
                        classTest[2] = ((ItemBlock)Item.getItemFromBlock(newBlock)).field_150939_a.getClass();
                        classTest[3] = Item.getItemFromBlock(newBlock).getClass();
                    }
                }
            }
        }
        catch (Exception e) {
            exception = e;
        }
        if (classTest[0] != classTest[1] || classTest[0] != classTest[2] || classTest[0] == null || classTest[3] != itemBlockClass) {
            throw new RuntimeException("RedPower was unable to replace block " + toReplace.getUnlocalizedName() + "! Debug info to report: " + classTest[0] + "," + classTest[1] + "," + classTest[2] + "," + classTest[3], exception);
        }
    }
}
