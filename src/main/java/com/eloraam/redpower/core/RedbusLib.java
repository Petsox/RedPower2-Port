package com.eloraam.redpower.core;

import net.minecraft.world.*;

public class RedbusLib
{
    public static IRedbusConnectable getAddr(final IBlockAccess iba, final WorldCoord pos, final int addr) {
        final RedbusPathfinder pf = new RedbusPathfinder(iba, addr);
        pf.addSearchBlocks(pos, 16777215, 0);
        while (pf.iterate()) {}
        return pf.result;
    }
    
    private static class RedbusPathfinder extends WirePathfinder
    {
        public IRedbusConnectable result;
        IBlockAccess iba;
        int addr;
        
        public RedbusPathfinder(final IBlockAccess ib, final int ad) {
            this.result = null;
            this.iba = ib;
            this.addr = ad;
            this.init();
        }
        
        @Override
        public boolean step(final WorldCoord wc) {
            final IRedbusConnectable irb = (IRedbusConnectable)CoreLib.getTileEntity(this.iba, wc, (Class)IRedbusConnectable.class);
            if (irb != null && irb.rbGetAddr() == this.addr) {
                this.result = irb;
                return false;
            }
            final IWiring iw = (IWiring)CoreLib.getTileEntity(this.iba, wc, (Class)IWiring.class);
            if (iw == null) {
                return true;
            }
            this.addSearchBlocks(wc, iw.getConnectionMask(), iw.getExtConnectionMask());
            return true;
        }
    }
}
