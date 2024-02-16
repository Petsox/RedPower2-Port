
package com.eloraam.redpower.core;

import com.mojang.authlib.*;
import net.minecraftforge.common.util.*;
import net.minecraft.server.*;
import net.minecraftforge.common.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.item.*;
import net.minecraftforge.oredict.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import java.lang.reflect.*;
import net.minecraft.block.material.*;
import java.util.*;

public class CoreLib
{
    private static TreeMap<ItemStack, String> oreMap;
    public static String[] rawColorNames;
    public static String[] enColorNames;
    public static int[] paintColors;
    public static final Material materialRedpower;
    public static final GameProfile REDPOWER_PROFILE;
    
    public static FakePlayer getRedpowerPlayer(final World world, final int x, final int y, final int z, final int rotation, final GameProfile profile) {
        final MinecraftServer server = ((WorldServer)world).func_73046_m();
        final FakePlayer player = FakePlayerFactory.get((WorldServer)world, profile);
        double dx = x + 0.5;
        double dy = y - 1.1;
        double dz = z + 0.5;
        float pitch = 0.0f;
        float yaw = 0.0f;
        switch (rotation ^ 0x1) {
            case 0: {
                pitch = 90.0f;
                yaw = 0.0f;
                dy -= 0.51;
                break;
            }
            case 1: {
                pitch = -90.0f;
                yaw = 0.0f;
                dy += 0.51;
                break;
            }
            case 2: {
                pitch = 0.0f;
                yaw = 180.0f;
                dz -= 0.51;
                break;
            }
            case 3: {
                pitch = 0.0f;
                yaw = 0.0f;
                dz += 0.51;
                break;
            }
            case 4: {
                pitch = 0.0f;
                yaw = 90.0f;
                dx -= 0.51;
                break;
            }
            default: {
                pitch = 0.0f;
                yaw = 270.0f;
                dx += 0.51;
                break;
            }
        }
        player.setLocationAndAngles(dx, dy, dz, yaw, pitch);
        return player;
    }
    
    public static boolean hasBreakPermission(final EntityPlayerMP player, final int x, final int y, final int z) {
        return hasEditPermission(player, x, y, z) && !ForgeHooks.onBlockBreakEvent(player.worldObj, player.theItemInWorldManager.getGameType(), player, x, y, z).isCanceled();
    }
    
    public static boolean hasEditPermission(final EntityPlayerMP player, final int x, final int y, final int z) {
        return player.canPlayerEdit(x, y, z, player.worldObj.getBlockMetadata(x, y, z), (ItemStack)null) && !MinecraftServer.getServer().isBlockProtected(player.worldObj, x, y, z, (EntityPlayer)player);
    }
    
    public static void updateAllLightTypes(final World world, final int x, final int y, final int z) {
        world.updateLightByType(EnumSkyBlock.Block, x, y, z);
        world.updateLightByType(EnumSkyBlock.Sky, x, y, z);
    }
    
    @Deprecated
    void initModule(final String name) {
        Class<?> cl;
        try {
            cl = Class.forName(name);
        }
        catch (ClassNotFoundException exc) {
            return;
        }
        Method mth;
        try {
            mth = cl.getDeclaredMethod("initialize", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException exc2) {
            return;
        }
        try {
            mth.invoke(null, new Object[0]);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
    }
    
    public static <T> T getTileEntity(final IBlockAccess iba, final int x, final int y, final int z, final Class<T> type) {
        final TileEntity tile = iba.getTileEntity(x, y, z);
        return (T)((tile == null || !type.isAssignableFrom(tile.getClass())) ? null : tile);
    }
    
    public static <T> T getTileEntity(final IBlockAccess iba, final WorldCoord wc, final Class<T> type) {
        final TileEntity tile = iba.getTileEntity(wc.x, wc.y, wc.z);
        return (T)((tile == null || !type.isAssignableFrom(tile.getClass())) ? null : tile);
    }
    
    public static <T extends TileEntity> T getGuiTileEntity(final World world, final int x, final int y, final int z, final Class<T> cl) {
        if (!world.isRemote) {
            final TileEntity tr = world.getTileEntity(x, y, z);
            return (T)(cl.isInstance(tr) ? tr : null);
        }
        try {
            final T t = cl.newInstance();
            t.setWorldObj(world);
            return t;
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            return null;
        }
    }
    
    public static void markBlockDirty(final World world, final int i, final int j, final int k) {
        if (world.blockExists(i, j, k)) {
            world.getChunkFromBlockCoords(i, k).setChunkModified();
        }
    }
    
    public static int compareItemStack(final ItemStack a, final ItemStack b) {
        return (Item.getIdFromItem(a.getItem()) != Item.getIdFromItem(b.getItem())) ? (Item.getIdFromItem(a.getItem()) - Item.getIdFromItem(b.getItem())) : ((a.getItemDamage() == b.getItemDamage()) ? 0 : (a.getItem().getHasSubtypes() ? (a.getItemDamage() - b.getItemDamage()) : 0));
    }
    
    static void registerOre(final String name, final ItemStack ore) {
        CoreLib.oreMap.put(ore, name);
    }
    
    public static void readOres() {
        for (final String st : OreDictionary.getOreNames()) {
            for (final ItemStack ist : OreDictionary.getOres(st)) {
                registerOre(st, ist);
            }
        }
    }
    
    public static String getOreClass(ItemStack ist) {
        final String st = CoreLib.oreMap.get(ist);
        if (st != null) {
            return st;
        }
        ist = new ItemStack(ist.getItem(), 1, -1);
        return CoreLib.oreMap.get(ist);
    }
    
    public static boolean matchItemStackOre(final ItemStack a, final ItemStack b) {
        final String s1 = getOreClass(a);
        final String s2 = getOreClass(b);
        return (ItemStack.areItemStacksEqual(a, b) || (s1 != null && s2 != null && s1.equals(s2))) && ItemStack.areItemStackTagsEqual(a, b);
    }
    
    public static void dropItem(final World world, final int i, final int j, final int k, final ItemStack ist) {
        if (!world.isRemote) {
            final double d = 0.7;
            final double x = world.rand.nextFloat() * d + (1.0 - d) * 0.5;
            final double y = world.rand.nextFloat() * d + (1.0 - d) * 0.5;
            final double z = world.rand.nextFloat() * d + (1.0 - d) * 0.5;
            final EntityItem item = new EntityItem(world, i + x, j + y, k + z, ist);
            item.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld((Entity)item);
        }
    }
    
    public static ItemStack copyStack(final ItemStack ist, final int n) {
        return new ItemStack(ist.getItem(), n, ist.getItemDamage());
    }
    
    public static int rotToSide(final int r) {
        switch (r) {
            case 0: {
                return 5;
            }
            case 1: {
                return 3;
            }
            case 2: {
                return 4;
            }
            default: {
                return 2;
            }
        }
    }
    
    public static int getFacing(final int side) {
        switch (side) {
            case 0: {
                return 2;
            }
            case 1: {
                return 5;
            }
            case 2: {
                return 3;
            }
            case 3: {
                return 4;
            }
            case 4: {
                return 1;
            }
            case 5: {
                return 0;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static MovingObjectPosition retraceBlock(final World world, final EntityLivingBase ent, final int x, final int y, final int z) {
        final Vec3 org = Vec3.createVectorHelper(ent.posX, ent.posY + 1.62 - ent.yOffset, ent.posZ);
        final Vec3 vec = ent.getLook(1.0f);
        final Vec3 end = org.addVector(vec.xCoord * 5.0, vec.yCoord * 5.0, vec.zCoord * 5.0);
        final Block bl = world.getBlock(x, y, z);
        return (bl == null) ? null : bl.collisionRayTrace(world, x, y, z, org, end);
    }
    
    public static MovingObjectPosition traceBlock(final EntityPlayer player) {
        final Vec3 org = Vec3.createVectorHelper(player.posX, player.posY + 1.62 - player.yOffset, player.posZ);
        final Vec3 vec = player.getLook(1.0f);
        final Vec3 end = org.addVector(vec.xCoord * 5.0, vec.yCoord * 5.0, vec.zCoord * 5.0);
        return player.worldObj.rayTraceBlocks(org, end);
    }
    
    public static void placeNoise(final World world, final int i, final int j, final int k, final Block block) {
        world.playSoundEffect((double)(i + 0.5f), (double)(j + 0.5f), (double)(k + 0.5f), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
    }
    
    public static int getBurnTime(final ItemStack ist) {
        return TileEntityFurnace.getItemBurnTime(ist);
    }
    
    public static double getAverageEdgeLength(final AxisAlignedBB aabb) {
        final double d = aabb.maxX - aabb.minX;
        final double d2 = aabb.maxY - aabb.minY;
        final double d3 = aabb.maxZ - aabb.minZ;
        return (d + d2 + d3) / 3.0;
    }
    
    public static void writeChat(final EntityPlayer pl, final String str) {
        if (pl instanceof EntityPlayerMP) {
            final EntityPlayerMP emp = (EntityPlayerMP)pl;
            emp.addChatComponentMessage((IChatComponent)new ChatComponentText(str));
        }
    }
    
    public static void updateBlock(final World world, final int x, final int y, final int z) {
        if (!(world.getTileEntity(x, y, z) instanceof TileExtended)) {
            world.func_147479_m(x, y, z);
        }
    }
    
    public static int[] toIntArray(final List<Integer> integerList) {
        final int[] intArray = new int[integerList.size()];
        for (int i = 0; i < integerList.size(); ++i) {
            intArray[i] = integerList.get(i);
        }
        return intArray;
    }
    
    public static <T, E> void setFinalValue(final Class<? super T> classToAccess, final T instance, final E value, final String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        }
        catch (Exception e) {
            throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
        }
    }
    
    public static Field findField(final Class<?> clazz, final String... fieldNames) {
        Exception failed = null;
        final int length = fieldNames.length;
        int i = 0;
        while (i < length) {
            final String fieldName = fieldNames[i];
            try {
                final Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
                return field;
            }
            catch (Exception e) {
                failed = e;
                ++i;
                continue;
            }
        }
        throw new ReflectionHelper.UnableToFindFieldException(fieldNames, failed);
    }
    
    static {
        CoreLib.oreMap = new TreeMap<ItemStack, String>(CoreLib::compareItemStack);
        CoreLib.rawColorNames = new String[] { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
        CoreLib.enColorNames = new String[] { "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };
        CoreLib.paintColors = new int[] { 16777215, 16744448, 16711935, 7110911, 16776960, 65280, 16737408, 5460819, 9671571, 65535, 8388863, 255, 5187328, 32768, 16711680, 2039583 };
        materialRedpower = new Material(MapColor.woodColor);
        REDPOWER_PROFILE = new GameProfile(UUID.fromString("d90e51a0-41af-4a37-9fd0-f2fdc15a181b"), "[RedPower]");
    }
}
