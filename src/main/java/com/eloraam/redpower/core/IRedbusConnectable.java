
package com.eloraam.redpower.core;

public interface IRedbusConnectable extends IConnectable
{
    int rbGetAddr();
    
    void rbSetAddr(final int p0);
    
    int rbRead(final int p0);
    
    void rbWrite(final int p0, final int p1);
    
    public static class Dummy implements IRedbusConnectable
    {
        private int address;
        
        public int getConnectableMask() {
            return 0;
        }
        
        public int getConnectClass(final int side) {
            return 0;
        }
        
        public int getCornerPowerMode() {
            return 0;
        }
        
        @Override
        public int rbGetAddr() {
            return this.address;
        }
        
        @Override
        public void rbSetAddr(final int addr) {
            this.address = addr;
        }
        
        @Override
        public int rbRead(final int reg) {
            return 0;
        }
        
        @Override
        public void rbWrite(final int reg, final int dat) {
        }
    }
}
