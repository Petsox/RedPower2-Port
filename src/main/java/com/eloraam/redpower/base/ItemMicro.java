
package com.eloraam.redpower.base;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import com.eloraam.redpower.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.event.world.*;
import net.minecraft.tileentity.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.item.*;
import net.minecraft.creativetab.*;
import java.util.*;
import com.eloraam.redpower.core.*;

public class ItemMicro extends ItemBlock
{
    private final IMicroPlacement[] placers;
    
    public ItemMicro(final Block block) {
        super(block);
        this.placers = new IMicroPlacement[256];
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    private boolean useCover(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        MovingObjectPosition pos = CoreLib.retraceBlock(world, player, x, y, z);
        if (pos == null) {
            return false;
        }
        if (pos.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return false;
        }
        pos = CoverLib.getPlacement(world, pos, ist.getItemDamage());
        if (pos == null) {
            return false;
        }
        final Block oldBlock = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        if (world.canPlaceEntityOnSide(oldBlock, pos.blockX, pos.blockY, pos.blockZ, false, side, player, ist)) {
            world.setBlock(pos.blockX, pos.blockY, pos.blockZ, RedPowerBase.blockMicro, 0, 3);
        }
        final TileEntity te = world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
        final Block newBlock = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
        final int newMeta = world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
        if (!(te instanceof ICoverable)) {
            return false;
        }
        final ICoverable icv = (ICoverable)te;
        final PlayerInteractEvent event = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, pos.blockX, pos.blockY, pos.blockZ, side, world);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            final NBTTagCompound nbt = new NBTTagCompound();
            te.writeToNBT(nbt);
            final BlockSnapshot snapshot = new BlockSnapshot(world, pos.blockX, pos.blockY, pos.blockZ, newBlock, newMeta, nbt);
            final BlockEvent.PlaceEvent plvt = new BlockEvent.PlaceEvent(snapshot, oldBlock, player);
            if (!MinecraftForge.EVENT_BUS.post(plvt)) {
                if (icv.tryAddCover(pos.subHit, CoverLib.damageToCoverValue(ist.getItemDamage()))) {
                    if (!player.capabilities.isCreativeMode) {
                        --ist.stackSize;
                    }
                    CoreLib.placeNoise(world, pos.blockX, pos.blockY, pos.blockZ, CoverLib.getBlock(ist.getItemDamage() & 0xFF));
                    RedPowerLib.updateIndirectNeighbors(world, pos.blockX, pos.blockY, pos.blockZ, RedPowerBase.blockMicro);
                    world.markBlockForUpdate(pos.blockX, pos.blockY, pos.blockZ);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean func_150936_a(final World world, final int x, final int y, final int z, final int side, final EntityPlayer player, final ItemStack ist) {
        return true;
    }
    
    public boolean onItemUse(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return player != null && !player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    public boolean onItemUseFirst(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float xp, final float yp, final float zp) {
        return !world.isRemote && player.isSneaking() && this.itemUseShared(ist, player, world, x, y, z, side);
    }
    
    private boolean itemUseShared(final ItemStack ist, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side) {
        int hb = ist.getItemDamage();
        hb >>= 8;
        return (hb != 0 && (hb < 16 || hb > 45)) ? (this.placers[hb] != null && this.placers[hb].onPlaceMicro(ist, player, world, new WorldCoord(x, y, z), side)) : this.useCover(ist, player, world, x, y, z, side);
    }
    
    private String getMicroName(final int hb) {
        switch (hb) {
            case 0: {
                return "rpcover";
            }
            case 16: {
                return "rppanel";
            }
            case 17: {
                return "rpslab";
            }
            case 18: {
                return "rpcovc";
            }
            case 19: {
                return "rppanc";
            }
            case 20: {
                return "rpslabc";
            }
            case 21: {
                return "rpcovs";
            }
            case 22: {
                return "rppans";
            }
            case 23: {
                return "rpslabs";
            }
            case 24: {
                return "rphcover";
            }
            case 25: {
                return "rphpanel";
            }
            case 26: {
                return "rphslab";
            }
            case 27: {
                return "rpcov3";
            }
            case 28: {
                return "rpcov5";
            }
            case 29: {
                return "rpcov6";
            }
            case 30: {
                return "rpcov7";
            }
            case 31: {
                return "rphcov3";
            }
            case 32: {
                return "rphcov5";
            }
            case 33: {
                return "rphcov6";
            }
            case 34: {
                return "rphcov7";
            }
            case 35: {
                return "rpcov3c";
            }
            case 36: {
                return "rpcov5c";
            }
            case 37: {
                return "rpcov6c";
            }
            case 38: {
                return "rpcov7c";
            }
            case 39: {
                return "rpcov3s";
            }
            case 40: {
                return "rpcov5s";
            }
            case 41: {
                return "rpcov6s";
            }
            case 42: {
                return "rpcov7s";
            }
            case 43: {
                return "rppole1";
            }
            case 44: {
                return "rppole2";
            }
            case 45: {
                return "rppole3";
            }
            default: {
                return null;
            }
        }
    }
    
    public String getUnlocalizedName(final ItemStack ist) {
        int hb = ist.getItemDamage();
        final int lb = hb & 0xFF;
        hb >>= 8;
        final String stub = this.getMicroName(hb);
        if (stub != null) {
            final String name = CoverLib.getName(lb);
            if (name == null) {
                throw new IndexOutOfBoundsException();
            }
            return "tile." + stub + "." + name;
        }
        else {
            if (this.placers[hb] == null) {
                throw new IndexOutOfBoundsException();
            }
            final String name = this.placers[hb].getMicroName(hb, lb);
            if (name == null) {
                throw new IndexOutOfBoundsException();
            }
            return name;
        }
    }
    
    public void registerPlacement(final int md, final IMicroPlacement imp) {
        this.placers[md] = imp;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item id, final CreativeTabs tab, final List list) {
        if (tab != CreativeExtraTabs.tabWires && tab != CreativeExtraTabs.tabMachine) {
            if (tab == CreativeExtraTabs.tabMicros) {
                for (int i = 0; i < 255; ++i) {
                    final String stub = CoverLib.getName(i);
                    if (stub != null) {
                        list.add(new ItemStack(RedPowerBase.blockMicro, 1, i));
                    }
                }
                for (int i = 1; i < 255; ++i) {
                    final String stub = this.getMicroName(i);
                    if (stub != null) {
                        list.add(new ItemStack(RedPowerBase.blockMicro, 1, i << 8));
                    }
                }
                for (int i = 1; i < 255; ++i) {
                    final String stub = this.getMicroName(i);
                    if (stub != null) {
                        list.add(new ItemStack(RedPowerBase.blockMicro, 1, i << 8 | 0x2));
                    }
                }
                for (int i = 1; i < 255; ++i) {
                    final String stub = this.getMicroName(i);
                    if (stub != null) {
                        list.add(new ItemStack(RedPowerBase.blockMicro, 1, i << 8 | 0x17));
                    }
                }
                for (int i = 1; i < 255; ++i) {
                    final String stub = this.getMicroName(i);
                    if (stub != null) {
                        list.add(new ItemStack(RedPowerBase.blockMicro, 1, i << 8 | 0x1A));
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 255; ++i) {
                if (this.placers[i] != null) {
                    this.placers[i].addCreativeItems(i, tab, list);
                }
            }
        }
    }
    
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[] { CreativeExtraTabs.tabWires, CreativeExtraTabs.tabMicros, CreativeExtraTabs.tabMachine };
    }
}
