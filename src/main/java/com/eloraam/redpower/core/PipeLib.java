package com.eloraam.redpower.core;

import net.minecraftforge.common.util.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraftforge.fluids.*;

public class PipeLib
{
    private static boolean isConSide(final IBlockAccess iba, final int x, final int y, final int z, final int side) {
        final TileEntity te = iba.getTileEntity(x, y, z);
        if (te instanceof IPipeConnectable) {
            final IPipeConnectable itc1 = (IPipeConnectable)te;
            final int ilt1 = itc1.getPipeConnectableSides();
            return (ilt1 & 1 << side) > 0;
        }
        if (te instanceof IFluidHandler) {
            final IFluidHandler itc2 = (IFluidHandler)te;
            final FluidTankInfo[] info = itc2.getTankInfo(ForgeDirection.getOrientation(side));
            if (info != null) {
                for (final FluidTankInfo i : info) {
                    if (i != null && i.capacity > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static int getConnections(final IBlockAccess iba, final int x, final int y, final int z) {
        final IPipeConnectable itc = (IPipeConnectable)CoreLib.getTileEntity(iba, x, y, z, (Class)IPipeConnectable.class);
        if (itc == null) {
            return 0;
        }
        int trs = 0;
        final int sides = itc.getPipeConnectableSides();
        if ((sides & 0x1) > 0 && isConSide(iba, x, y - 1, z, 1)) {
            trs |= 0x1;
        }
        if ((sides & 0x2) > 0 && isConSide(iba, x, y + 1, z, 0)) {
            trs |= 0x2;
        }
        if ((sides & 0x4) > 0 && isConSide(iba, x, y, z - 1, 3)) {
            trs |= 0x4;
        }
        if ((sides & 0x8) > 0 && isConSide(iba, x, y, z + 1, 2)) {
            trs |= 0x8;
        }
        if ((sides & 0x10) > 0 && isConSide(iba, x - 1, y, z, 5)) {
            trs |= 0x10;
        }
        if ((sides & 0x20) > 0 && isConSide(iba, x + 1, y, z, 4)) {
            trs |= 0x20;
        }
        return trs;
    }
    
    public static int getFlanges(final IBlockAccess iba, final WorldCoord wci, final int sides) {
        int tr = 0;
        for (int i = 0; i < 6; ++i) {
            if ((sides & 1 << i) != 0x0) {
                final WorldCoord wc = wci.copy();
                wc.step(i);
                final TileEntity te = iba.getTileEntity(wc.x, wc.y, wc.z);
                if (te != null) {
                    if (te instanceof IPipeConnectable) {
                        final IPipeConnectable itc = (IPipeConnectable)te;
                        if ((itc.getPipeFlangeSides() & 1 << (i ^ 0x1)) > 0) {
                            tr |= 1 << i;
                        }
                    }
                    else if (te instanceof IFluidHandler) {
                        final IFluidHandler itc2 = (IFluidHandler)te;
                        final FluidTankInfo[] info = itc2.getTankInfo(ForgeDirection.getOrientation(i ^ 0x1));
                        if (info != null) {
                            for (final FluidTankInfo inf : info) {
                                if (inf != null && inf.capacity > 0) {
                                    tr |= 1 << i;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return tr;
    }
    
    public static Integer getPressure(final World world, final WorldCoord wc, final int side) {
        final TileEntity te = world.getTileEntity(wc.x, wc.y, wc.z);
        if (te != null) {
            if (te instanceof IPipeConnectable) {
                final IPipeConnectable itc = (IPipeConnectable)te;
                return itc.getPipePressure(side);
            }
            if (te instanceof IFluidHandler) {
                final IFluidHandler ifh = (IFluidHandler)te;
                final FluidTankInfo[] info = ifh.getTankInfo(ForgeDirection.getOrientation(side));
                if (info != null) {
                    for (final FluidTankInfo i : info) {
                        if (i.fluid != null) {
                            return (int)(i.fluid.amount / (double)i.capacity * 100.0);
                        }
                    }
                    for (final FluidTankInfo i : info) {
                        if (i.capacity > 0) {
                            return -100;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static Fluid getFluid(final World world, final WorldCoord wc) {
        final Block bl = world.getBlock(wc.x, wc.y, wc.z);
        if (bl instanceof IFluidBlock) {
            final IFluidBlock fcl = (IFluidBlock)bl;
            return fcl.getFluid();
        }
        if (bl instanceof BlockLiquid) {
            final BlockLiquid blq = (BlockLiquid)bl;
            if (blq.getMaterial() == Material.water) {
                return FluidRegistry.WATER;
            }
            if (blq.getMaterial() == Material.lava) {
                return FluidRegistry.LAVA;
            }
        }
        return null;
    }
    
    public static int getFluidAmount(final World world, final WorldCoord wc) {
        final Block bl = world.getBlock(wc.x, wc.y, wc.z);
        if (bl instanceof IFluidBlock) {
            final IFluidBlock fcl = (IFluidBlock)bl;
            final float fp = fcl.getFilledPercentage(world, wc.x, wc.y, wc.z);
            return (int)(fcl.getFluid().getDensity() * fp);
        }
        if (bl instanceof BlockLiquid) {
            final BlockLiquid blq = (BlockLiquid)bl;
            if (blq.getMaterial() == Material.water || blq.getMaterial() == Material.lava) {
                return 1000;
            }
        }
        return 0;
    }
    
    public static void movePipeLiquid(final World world, final IPipeConnectable src, final WorldCoord wsrc, final int sides) {
        for (int side = 0; side < 6; ++side) {
            if ((sides & 1 << side) != 0x0) {
                final WorldCoord wc = wsrc.coordStep(side);
                final TileEntity te = world.getTileEntity(wc.x, wc.y, wc.z);
                if (te != null) {
                    if (te instanceof IPipeConnectable) {
                        final IPipeConnectable itc = (IPipeConnectable)te;
                        final int srcPressure = src.getPipePressure(side);
                        final int dstPressure = itc.getPipePressure(side ^ 0x1);
                        if (srcPressure >= dstPressure) {
                            final FluidBuffer srcBuffer = src.getPipeBuffer(side);
                            if (srcBuffer != null) {
                                final Fluid srcType = srcBuffer.Type;
                                final int srcLevel = srcBuffer.getLevel() + srcBuffer.Delta;
                                if (srcType != null) {
                                    if (srcLevel > 0) {
                                        final FluidBuffer dstBuffer = itc.getPipeBuffer(side ^ 0x1);
                                        if (dstBuffer != null) {
                                            final Fluid dstType = dstBuffer.Type;
                                            final int dstLevel = dstBuffer.getLevel();
                                            if (dstType == null || dstType == srcType) {
                                                int ls = Math.max((srcPressure > dstPressure) ? 25 : 0, (srcLevel - dstLevel) / 2);
                                                ls = Math.min(Math.min(ls, dstBuffer.getMaxLevel() - dstLevel), srcLevel);
                                                if (ls > 0) {
                                                    srcBuffer.addLevel(srcType, -ls);
                                                    dstBuffer.addLevel(srcType, ls);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (te instanceof IFluidHandler) {
                        final IFluidHandler ifh = (IFluidHandler)te;
                        final FluidBuffer srcBuffer2 = src.getPipeBuffer(side);
                        if (srcBuffer2 != null) {
                            final FluidTankInfo[] info = ifh.getTankInfo(ForgeDirection.getOrientation(side ^ 0x1));
                            if (info != null) {
                                for (final FluidTankInfo i : info) {
                                    final Fluid bType = srcBuffer2.Type;
                                    final int srcLevel2 = srcBuffer2.getLevel() + srcBuffer2.Delta;
                                    final int srcPressure2 = src.getPipePressure(side);
                                    Label_0652: {
                                        if (i.capacity > 0) {
                                            if (i.fluid != null) {
                                                if (i.fluid.getFluid() != bType) {
                                                    if (bType != null) {
                                                        break Label_0652;
                                                    }
                                                }
                                            }
                                            else if (bType == null) {
                                                break Label_0652;
                                            }
                                            final int dstLevel2 = (i.fluid == null) ? 0 : i.fluid.amount;
                                            final int dstPressure2 = (dstLevel2 <= 0) ? -100 : ((int)(dstLevel2 / (double)i.capacity * 100.0));
                                            if (srcPressure2 < dstPressure2 && dstLevel2 > 0) {
                                                final int qty = Math.min(Math.min(Math.max(25, (dstLevel2 - srcLevel2) / 2), srcBuffer2.getMaxLevel() - srcLevel2), dstLevel2);
                                                if (qty > 0) {
                                                    final FluidStack drStack = ifh.drain(ForgeDirection.getOrientation(side ^ 0x1), qty, true);
                                                    if (drStack != null) {
                                                        srcBuffer2.addLevel(drStack.getFluid(), drStack.amount);
                                                    }
                                                }
                                            }
                                            else if (srcPressure2 > dstPressure2 && srcLevel2 > 0) {
                                                int qty = Math.min(Math.min(Math.max(25, (srcLevel2 - dstLevel2) / 2), i.capacity - dstLevel2), srcLevel2);
                                                if (qty > 0) {
                                                    qty = ifh.fill(ForgeDirection.getOrientation(side ^ 0x1), new FluidStack(bType, qty), true);
                                                    srcBuffer2.addLevel(bType, -qty);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
