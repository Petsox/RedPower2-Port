//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import com.eloraam.redpower.wiring.*;
import net.minecraft.block.*;

public class TileRibbon extends TileWiring
{
    @Override
    public int getExtendedID() {
        return 12;
    }
    
    @Override
    public int getConnectClass(final int side) {
        return 66;
    }
    
    @Override
    public void onBlockNeighborChange(final Block block) {
        super.onBlockNeighborChange(block);
        this.getConnectionMask();
        this.getExtConnectionMask();
    }
}
