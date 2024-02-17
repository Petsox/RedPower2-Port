package com.eloraam.redpower.machine;

import net.minecraft.util.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;

public class BlockFrame extends BlockCoverable
{
    public BlockFrame() {
        super(CoreLib.materialRedpower);
        this.setHardness(0.5f);
        this.setCreativeTab(CreativeExtraTabs.tabMachine);
    }
    
    public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB box, final List list, final Entity ent) {
        TileFrameMoving tl = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)world, x, y, z, (Class)TileFrameMoving.class);
        if (tl == null) {
            super.addCollisionBoxesToList(world, x, y, z, box, list, ent);
        }
        else {
            this.computeCollidingBoxes(world, x, y, z, box, list, (TileMultipart)tl);
            final TileMotor tm = (TileMotor)CoreLib.getTileEntity((IBlockAccess)world, tl.motorX, tl.motorY, tl.motorZ, (Class)TileMotor.class);
            if (tm != null) {
                final WorldCoord wc = new WorldCoord(x, y, z);
                wc.step(tm.MoveDir ^ 0x1);
                tl = (TileFrameMoving)CoreLib.getTileEntity((IBlockAccess)world, wc, (Class)TileFrameMoving.class);
                if (tl != null) {
                    this.computeCollidingBoxes(world, wc.x, wc.y, wc.z, box, list, (TileMultipart)tl);
                }
            }
        }
    }
}
