//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.block.*;
import com.eloraam.redpower.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import com.eloraam.redpower.core.*;
import net.minecraft.nbt.*;

public class TileRAM extends TileBackplane
{
    public byte[] memory;
    
    public TileRAM() {
        this.memory = new byte[8192];
    }
    
    public int readBackplane(final int addr) {
        return this.memory[addr] & 0xFF;
    }
    
    public void writeBackplane(final int addr, final int val) {
        this.memory[addr] = (byte)val;
    }
    
    public Block getBlockType() {
        return RedPowerControl.blockBackplane;
    }
    
    public int getExtendedID() {
        return 1;
    }
    
    public void addHarvestContents(final List<ItemStack> ist) {
        ist.add(new ItemStack(RedPowerControl.blockBackplane, 1, 0));
        ist.add(new ItemStack(RedPowerControl.blockBackplane, 1, 1));
    }
    
    public void onHarvestPart(final EntityPlayer player, final int part, final boolean willHarvest) {
        if (willHarvest) {
            CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, new ItemStack(RedPowerControl.blockBackplane, 1, 1));
        }
        super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, RedPowerControl.blockBackplane);
        final TileBackplane tb = CoreLib.getTileEntity(super.worldObj, super.xCoord, super.yCoord, super.zCoord, TileBackplane.class);
        if (tb != null) {
            tb.Rotation = this.Rotation;
        }
        this.updateBlockChange();
    }
    
    public void setPartBounds(final BlockMultipart block, final int part) {
        if (part == 0) {
            super.setPartBounds(block, part);
        }
        else {
            block.setBlockBounds(0.0f, 0.125f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    public int getSolidPartsMask() {
        return 3;
    }
    
    public int getPartsMask() {
        return 3;
    }
    
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.memory = data.getByteArray("ram");
        if (this.memory.length != 8192) {
            this.memory = new byte[8192];
        }
    }
    
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByteArray("ram", this.memory);
    }
}
