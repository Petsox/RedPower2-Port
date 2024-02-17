package com.eloraam.redpower.core;

import net.minecraft.tileentity.*;
import java.util.*;

public class WorldCoord implements Comparable<WorldCoord>
{
    public int x;
    public int y;
    public int z;
    
    public WorldCoord(final int xi, final int yi, final int zi) {
        this.x = xi;
        this.y = yi;
        this.z = zi;
    }
    
    public WorldCoord(final TileEntity te) {
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
    }
    
    public WorldCoord copy() {
        return new WorldCoord(this.x, this.y, this.z);
    }
    
    public WorldCoord coordStep(final int dir) {
        switch (dir) {
            case 0: {
                return new WorldCoord(this.x, this.y - 1, this.z);
            }
            case 1: {
                return new WorldCoord(this.x, this.y + 1, this.z);
            }
            case 2: {
                return new WorldCoord(this.x, this.y, this.z - 1);
            }
            case 3: {
                return new WorldCoord(this.x, this.y, this.z + 1);
            }
            case 4: {
                return new WorldCoord(this.x - 1, this.y, this.z);
            }
            default: {
                return new WorldCoord(this.x + 1, this.y, this.z);
            }
        }
    }
    
    public void set(final WorldCoord wc) {
        this.x = wc.x;
        this.y = wc.y;
        this.z = wc.z;
    }
    
    public int squareDist(final int xi, final int yi, final int zi) {
        return (xi - this.x) * (xi - this.x) + (yi - this.y) * (yi - this.y) + (zi - this.z) * (zi - this.z);
    }
    
    public void step(final int dir) {
        switch (dir) {
            case 0: {
                --this.y;
                break;
            }
            case 1: {
                ++this.y;
                break;
            }
            case 2: {
                --this.z;
                break;
            }
            case 3: {
                ++this.z;
                break;
            }
            case 4: {
                --this.x;
                break;
            }
            default: {
                ++this.x;
                break;
            }
        }
    }
    
    public void step(final int dir, final int dist) {
        switch (dir) {
            case 0: {
                this.y -= dist;
                break;
            }
            case 1: {
                this.y += dist;
                break;
            }
            case 2: {
                this.z -= dist;
                break;
            }
            case 3: {
                this.z += dist;
                break;
            }
            case 4: {
                this.x -= dist;
                break;
            }
            default: {
                this.x += dist;
                break;
            }
        }
    }
    
    public static int getRightDir(final int dir) {
        if (dir < 2) {
            return dir;
        }
        switch (dir) {
            case 0: {
                return 0;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 4;
            }
            case 3: {
                return 5;
            }
            case 4: {
                return 3;
            }
            default: {
                return 2;
            }
        }
    }
    
    public static int getIndStepDir(final int d1, final int d2) {
        switch (d1) {
            case 0: {
                return d2 + 2;
            }
            case 1: {
                return d2 + 2;
            }
            case 2: {
                return d2 + (d2 & 0x2);
            }
            case 3: {
                return d2 + (d2 & 0x2);
            }
            case 4: {
                return d2;
            }
            default: {
                return d2;
            }
        }
    }
    
    public void indStep(final int d1, final int d2) {
        this.step(d1);
        this.step(getIndStepDir(d1, d2));
    }
    
    @Override
    public int hashCode() {
        return this.x + 31 * (this.y + 31 * this.z);
    }
    
    @Override
    public int compareTo(final WorldCoord wc) {
        return (this.x == wc.x) ? ((this.y == wc.y) ? (this.z - wc.z) : (this.y - wc.y)) : (this.x - wc.x);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof WorldCoord)) {
            return false;
        }
        final WorldCoord wc = (WorldCoord)obj;
        return this.x == wc.x && this.y == wc.y && this.z == wc.z;
    }
    
    public static Comparator<WorldCoord> getCompareDir(final int dir) {
        return new WCComparator(dir);
    }
    
    public static class WCComparator implements Comparator<WorldCoord>
    {
        int dir;
        
        private WCComparator(final int d) {
            this.dir = d;
        }
        
        @Override
        public int compare(final WorldCoord wa, final WorldCoord wb) {
            switch (this.dir) {
                case 0: {
                    return wa.y - wb.y;
                }
                case 1: {
                    return wb.y - wa.y;
                }
                case 2: {
                    return wa.z - wb.z;
                }
                case 3: {
                    return wb.z - wa.z;
                }
                case 4: {
                    return wa.x - wb.x;
                }
                default: {
                    return wb.x - wa.x;
                }
            }
        }
    }
}
