//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.control;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.*;
import com.eloraam.redpower.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import net.minecraft.tileentity.*;
import com.eloraam.redpower.core.*;
import net.minecraft.world.*;
import net.minecraft.nbt.*;

public class TileDiskDrive extends TileExtended implements IRedbusConnectable, IInventory, IFrameSupport
{
    public int Rotation;
    public boolean hasDisk;
    public boolean Active;
    private ItemStack[] contents;
    private int accessTime;
    private byte[] databuf;
    private int sector;
    private int cmdreg;
    private int rbaddr;
    
    public TileDiskDrive() {
        this.Rotation = 0;
        this.hasDisk = false;
        this.Active = false;
        this.contents = new ItemStack[1];
        this.accessTime = 0;
        this.databuf = new byte[128];
        this.sector = 0;
        this.cmdreg = 0;
        this.rbaddr = 2;
    }
    
    @Override
    public int rbGetAddr() {
        return this.rbaddr;
    }
    
    @Override
    public void rbSetAddr(final int addr) {
        this.rbaddr = addr;
    }
    
    @Override
    public int rbRead(final int reg) {
        if (reg < 128) {
            return this.databuf[reg] & 0xFF;
        }
        switch (reg) {
            case 128: {
                return this.sector & 0xFF;
            }
            case 129: {
                return this.sector >> 8;
            }
            case 130: {
                return this.cmdreg & 0xFF;
            }
            default: {
                return 0;
            }
        }
    }
    
    @Override
    public void rbWrite(final int reg, final int dat) {
        this.markDirty();
        if (reg < 128) {
            this.databuf[reg] = (byte)dat;
        }
        else {
            switch (reg) {
                case 128: {
                    this.sector = ((this.sector & 0xFF00) | dat);
                    break;
                }
                case 129: {
                    this.sector = ((this.sector & 0xFF) | dat << 8);
                    break;
                }
                case 130: {
                    this.cmdreg = dat;
                    break;
                }
            }
        }
    }
    
    public int getConnectableMask() {
        return 16777215;
    }
    
    public int getConnectClass(final int side) {
        return 66;
    }
    
    public int getCornerPowerMode() {
        return 0;
    }
    
    @Override
    public void onBlockPlaced(final ItemStack ist, final int side, final EntityLivingBase ent) {
        this.Rotation = ((int)Math.floor(ent.rotationYaw * 4.0f / 360.0f + 0.5) + 1 & 0x3);
        if (ent instanceof EntityPlayer) {
            this.Owner = ((EntityPlayer)ent).getGameProfile();
        }
    }
    
    @Override
    public boolean onBlockActivated(final EntityPlayer player) {
        if (this.worldObj.isRemote) {
            return true;
        }
        if (!this.hasDisk || this.contents[0] == null || this.Active) {
            return false;
        }
        this.ejectDisk();
        return true;
    }
    
    public Block getBlockType() {
        return RedPowerControl.blockPeripheral;
    }
    
    @Override
    public int getExtendedID() {
        return 2;
    }
    
    @Override
    public void onBlockRemoval() {
        for (int i = 0; i < 1; ++i) {
            final ItemStack ist = this.contents[i];
            if (ist != null && ist.stackSize > 0) {
                CoreLib.dropItem(super.worldObj, super.xCoord, super.yCoord, super.zCoord, ist);
            }
        }
    }
    
    boolean setDisk(final ItemStack ist) {
        if (this.contents[0] != null) {
            return false;
        }
        this.setInventorySlotContents(0, ist);
        return true;
    }
    
    private NBTTagCompound getDiskTags() {
        NBTTagCompound tags = this.contents[0].stackTagCompound;
        if (tags == null) {
            this.contents[0].setTagCompound(new NBTTagCompound());
            tags = this.contents[0].stackTagCompound;
        }
        return tags;
    }
    
    private File startDisk() {
        if (this.contents[0].getItemDamage() > 0) {
            return null;
        }
        final NBTTagCompound tags = this.getDiskTags();
        final File savedir = DiskLib.getSaveDir(super.worldObj);
        if (tags.hasKey("serno")) {
            return DiskLib.getDiskFile(savedir, tags.getString("serno"));
        }
        String serno = null;
        while (true) {
            serno = DiskLib.generateSerialNumber(super.worldObj);
            final File diskFile = DiskLib.getDiskFile(savedir, serno);
            try {
                if (diskFile.createNewFile()) {
                    tags.setString("serno", serno);
                    return diskFile;
                }
                continue;
            }
            catch (IOException exc) {
                exc.printStackTrace();
                return null;
            }
        }
    }
    
    private void runCmd1() {
        Arrays.fill(this.databuf, (byte)0);
        String nm = "";
        if (this.contents[0].getItemDamage() > 0) {
            nm = "System Disk";
        }
        else {
            final NBTTagCompound e = this.contents[0].stackTagCompound;
            if (e == null) {
                return;
            }
            nm = e.getString("label");
        }
        final byte[] e2 = nm.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(e2, 0, this.databuf, 0, Math.min(e2.length, 128));
    }
    
    private void runCmd2() {
        if (this.contents[0].getItemDamage() > 0) {
            this.cmdreg = -1;
        }
        else {
            final NBTTagCompound tags = this.getDiskTags();
            int len;
            for (len = 0; this.databuf[len] != 0 && len < 64; ++len) {}
            this.cmdreg = 0;
            final String e = new String(this.databuf, 0, len, StandardCharsets.US_ASCII);
            tags.setString("label", e);
        }
    }
    
    private void runCmd3() {
        Arrays.fill(this.databuf, (byte)0);
        String nm = "";
        if (this.contents[0].getItemDamage() > 0) {
            nm = String.format("%016d", this.contents[0].getItemDamage());
        }
        else {
            final NBTTagCompound e = this.getDiskTags();
            this.startDisk();
            if (e == null) {
                return;
            }
            nm = e.getString("serno");
        }
        final byte[] e2 = nm.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(e2, 0, this.databuf, 0, Math.min(e2.length, 128));
    }
    
    private void runCmd4() {
        if (this.sector > 2048) {
            this.cmdreg = -1;
        }
        else {
            final long l = this.sector * 128L;
            if (this.contents[0].getItemDamage() > 0) {
                InputStream file = null;
                switch (this.contents[0].getItemDamage()) {
                    case 1: {
                        file = RedPowerControl.class.getResourceAsStream("/assets/rpcontrol/forth/redforth.img");
                        break;
                    }
                    case 2: {
                        file = RedPowerControl.class.getResourceAsStream("/assets/rpcontrol/forth/redforthxp.img");
                        break;
                    }
                }
                try {
                    if (file.skip(l) != l) {
                        this.cmdreg = -1;
                        return;
                    }
                    if (file.read(this.databuf) == 128) {
                        this.cmdreg = 0;
                        return;
                    }
                    this.cmdreg = -1;
                }
                catch (IOException exc) {
                    exc.printStackTrace();
                    this.cmdreg = -1;
                }
                finally {
                    try {
                        if (file != null) {
                            file.close();
                        }
                    }
                    catch (IOException ex) {}
                }
            }
            else {
                final File file2 = this.startDisk();
                if (file2 == null) {
                    this.cmdreg = -1;
                }
                else {
                    RandomAccessFile raf = null;
                    try {
                        raf = new RandomAccessFile(file2, "r");
                        raf.seek(l);
                        if (raf.read(this.databuf) == 128) {
                            this.cmdreg = 0;
                            return;
                        }
                        this.cmdreg = -1;
                    }
                    catch (IOException var35) {
                        var35.printStackTrace();
                        this.cmdreg = -1;
                    }
                    finally {
                        try {
                            if (raf != null) {
                                raf.close();
                            }
                        }
                        catch (IOException ex2) {}
                    }
                }
            }
        }
    }
    
    private void runCmd5() {
        if (this.contents[0].getItemDamage() > 0) {
            this.cmdreg = -1;
        }
        else if (this.sector > 2048) {
            this.cmdreg = -1;
        }
        else {
            final long l = this.sector * 128L;
            final File file = this.startDisk();
            if (file == null) {
                this.cmdreg = -1;
            }
            else {
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(file, "rw");
                    raf.seek(l);
                    raf.write(this.databuf);
                    raf.close();
                    raf = null;
                    this.cmdreg = 0;
                }
                catch (IOException var14) {
                    this.cmdreg = -1;
                }
                finally {
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                    }
                    catch (IOException ex) {}
                }
            }
        }
    }
    
    private void runDiskCmd() {
        this.markDirty();
        if (this.contents[0] == null) {
            this.cmdreg = -1;
        }
        else if (!(this.contents[0].getItem() instanceof ItemDisk)) {
            this.cmdreg = -1;
        }
        else {
            switch (this.cmdreg) {
                case 1: {
                    this.runCmd1();
                    this.cmdreg = 0;
                    break;
                }
                case 2: {
                    this.runCmd2();
                    break;
                }
                case 3: {
                    this.runCmd3();
                    this.cmdreg = 0;
                    break;
                }
                case 4: {
                    this.runCmd4();
                    break;
                }
                case 5: {
                    this.runCmd5();
                    break;
                }
                default: {
                    this.cmdreg = -1;
                    break;
                }
            }
            this.accessTime = 5;
            if (!this.Active) {
                this.Active = true;
                this.updateBlock();
            }
        }
    }
    
    private void ejectDisk() {
        if (this.contents[0] != null) {
            MachineLib.ejectItem(super.worldObj, new WorldCoord(this), this.contents[0], CoreLib.rotToSide(this.Rotation) ^ 0x1);
            this.contents[0] = null;
            this.hasDisk = false;
            this.updateBlock();
        }
    }
    
    public void markDirty() {
        super.markDirty();
        if (this.contents[0] != null && !(this.contents[0].getItem() instanceof ItemDisk)) {
            this.ejectDisk();
        }
    }
    
    @Override
    public void updateEntity() {
        if (this.cmdreg != 0 && this.cmdreg != -1) {
            this.runDiskCmd();
        }
        if (this.accessTime > 0 && --this.accessTime == 0) {
            this.Active = false;
            this.updateBlock();
        }
    }
    
    public int getSizeInventory() {
        return 1;
    }
    
    public ItemStack getStackInSlot(final int i) {
        return this.contents[i];
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        if (this.contents[i] == null) {
            return null;
        }
        if (this.contents[i].stackSize <= j) {
            final ItemStack tr = this.contents[i];
            this.contents[i] = null;
            this.markDirty();
            return tr;
        }
        final ItemStack tr = this.contents[i].splitStack(j);
        if (this.contents[i].stackSize == 0) {
            this.contents[i] = null;
        }
        this.markDirty();
        return tr;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        if (this.contents[i] == null) {
            return null;
        }
        final ItemStack ist = this.contents[i];
        this.contents[i] = null;
        return ist;
    }
    
    public void setInventorySlotContents(final int i, final ItemStack ist) {
        this.contents[i] = ist;
        if (ist != null && ist.stackSize > this.getInventoryStackLimit()) {
            ist.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
        this.hasDisk = (this.contents[i] != null);
        this.updateBlock();
    }
    
    public String getInventoryName() {
        return "tile.rpdiskdrive.name";
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return !this.isInvalid() && super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) == this && player.getDistanceSq(super.xCoord + 0.5, super.yCoord + 0.5, super.zCoord + 0.5) <= 64.0;
    }
    
    public void writeFramePacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("fl", (byte)((this.hasDisk ? 1 : 0) | (this.Active ? 2 : 0)));
    }
    
    public void readFramePacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        final int fl = tag.getByte("fl");
        this.hasDisk = ((fl & 0x1) > 0);
        this.Active = ((fl & 0x2) > 0);
    }
    
    public void onFrameRefresh(final IBlockAccess iba) {
    }
    
    public void onFramePickup(final IBlockAccess iba) {
    }
    
    public void onFrameDrop() {
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.Rotation = data.getByte("rot");
        this.accessTime = data.getByte("actime");
        this.sector = data.getShort("sect");
        this.cmdreg = (data.getByte("cmd") & 0xFF);
        this.rbaddr = (data.getByte("rbaddr") & 0xFF);
        final byte fl = data.getByte("fl");
        this.hasDisk = ((fl & 0x1) > 0);
        this.Active = ((fl & 0x2) > 0);
        this.databuf = data.getByteArray("dbuf");
        if (this.databuf.length != 128) {
            this.databuf = new byte[128];
        }
        final NBTTagList items = data.getTagList("Items", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < items.tagCount(); ++i) {
            final NBTTagCompound item = items.getCompoundTagAt(i);
            final int j = item.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.contents.length) {
                this.contents[j] = ItemStack.loadItemStackFromNBT(item);
            }
        }
        this.hasDisk = (this.contents[0] != null);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        data.setByte("rot", (byte)this.Rotation);
        final int fl = (this.hasDisk ? 1 : 0) | (this.Active ? 2 : 0);
        data.setByte("fl", (byte)fl);
        data.setByte("actime", (byte)this.accessTime);
        data.setByteArray("dbuf", this.databuf);
        data.setShort("sect", (short)this.sector);
        data.setByte("cmd", (byte)this.cmdreg);
        data.setByte("rbaddr", (byte)this.rbaddr);
        final NBTTagList items = new NBTTagList();
        for (int i = 0; i < this.contents.length; ++i) {
            if (this.contents[i] != null) {
                final NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)i);
                this.contents[i].writeToNBT(item);
                items.appendTag(item);
            }
        }
        data.setTag("Items", items);
    }
    
    @Override
    protected void readFromPacket(final NBTTagCompound tag) {
        this.Rotation = tag.getByte("rot");
        final int fl = tag.getByte("fl");
        this.hasDisk = ((fl & 0x1) > 0);
        this.Active = ((fl & 0x2) > 0);
    }
    
    @Override
    protected void writeToPacket(final NBTTagCompound tag) {
        tag.setByte("rot", (byte)this.Rotation);
        tag.setByte("fl", (byte)((this.hasDisk ? 1 : 0) | (this.Active ? 2 : 0)));
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public boolean isItemValidForSlot(final int slotID, final ItemStack stack) {
        return slotID == 0 && stack.getItem() instanceof ItemDisk;
    }
}
