//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

public class FractalLib
{
    public static long hash64shift(long key) {
        key = ~key + (key << 21);
        key ^= key >>> 24;
        key = key + (key << 3) + (key << 8);
        key ^= key >>> 14;
        key = key + (key << 2) + (key << 4);
        key ^= key >>> 28;
        key += key << 31;
        return key;
    }
    
    public static double hashFloat(final long key) {
        final long f = hash64shift(key);
        return Double.longBitsToDouble(0x3FF0000000000000L | (f & 0xFFFFFFFFFFFFFL)) - 1.0;
    }
    
    public static double noise1D(final long seed, final double pos, final float lac, final int octave) {
        double tr = 0.5;
        double scale = 1 << octave;
        for (int i = 0; i < octave; ++i) {
            double p = pos * scale;
            final long pint = (long)Math.floor(p);
            final double m1 = hashFloat(seed + pint);
            final double m2 = hashFloat(seed + pint + 1L);
            p -= Math.floor(p);
            double v = 0.5 + 0.5 * Math.cos(3.141592653589793 * p);
            v = v * m1 + (1.0 - v) * m2;
            tr = (1.0f - lac) * tr + lac * v;
            scale *= 0.5;
        }
        return tr;
    }
    
    public static double perturbOld(final long seed, final float pos, final float lac, final int octave) {
        double tr = 0.0;
        double mscale = 1.0;
        double scale = 1.0;
        for (int i = 0; i < octave; ++i) {
            final long v = (long)Math.floor(pos * scale);
            final long p = hash64shift(seed + v);
            final double mag = Double.longBitsToDouble(0x3FF0000000000000L | (p & 0xFFFFFFFFFFFFFL)) - 1.0;
            tr += mscale * mag * Math.sin(6.283185307179586 * pos * scale);
            scale *= 2.0;
            mscale *= lac;
        }
        return tr;
    }
    
    public static void fillVector(final Vector3 v, final Vector3 org, final Vector3 dest, final float pos, final long seed) {
        final double window = 4.0 * Math.sin(3.141592653589793 * pos);
        v.x = org.x + pos * pos * dest.x + window * perturbOld(seed, pos, 0.7f, 5);
        v.y = org.y + pos * dest.y + window * perturbOld(seed + 1L, pos, 0.7f, 5);
        v.z = org.z + pos * pos * dest.z + window * perturbOld(seed + 2L, pos, 0.7f, 5);
    }
    
    public static int mdist(final Vector3 a, final Vector3 b) {
        return (int)(Math.abs(Math.floor(a.x) - Math.floor(b.x)) + Math.abs(Math.floor(a.y) - Math.floor(b.y)) + Math.abs(Math.floor(a.z) - Math.floor(b.z)));
    }
    
    public static class BlockRay
    {
        private Vector3 p1;
        private Vector3 p2;
        private Vector3 dv;
        public Vector3 enter;
        public Vector3 exit;
        public int xp;
        public int yp;
        public int zp;
        public int dir;
        public int face;
        
        public BlockRay(final Vector3 s, final Vector3 d) {
            this.p1 = new Vector3(s);
            this.p2 = new Vector3(d);
            (this.dv = new Vector3(d)).subtract(s);
            this.exit = new Vector3(s);
            this.enter = new Vector3();
            this.xp = (int)Math.floor(s.x);
            this.yp = (int)Math.floor(s.y);
            this.zp = (int)Math.floor(s.z);
            this.dir = 0;
            this.dir |= ((d.x > s.x) ? 4 : 0);
            this.dir |= ((d.y > s.y) ? 1 : 0);
            this.dir |= ((d.z > s.z) ? 2 : 0);
        }
        
        public void set(final Vector3 s, final Vector3 d) {
            this.p1.set(s);
            this.p2.set(d);
            this.dv.set(d);
            this.dv.subtract(s);
            this.exit.set(s);
            this.xp = (int)Math.floor(s.x);
            this.yp = (int)Math.floor(s.y);
            this.zp = (int)Math.floor(s.z);
            this.dir = 0;
            this.dir |= ((d.x > s.x) ? 4 : 0);
            this.dir |= ((d.y > s.y) ? 1 : 0);
            this.dir |= ((d.z > s.z) ? 2 : 0);
        }
        
        boolean step() {
            double bp = 1.0;
            int sd = -1;
            if (this.dv.x != 0.0) {
                int x = this.xp;
                if ((this.dir & 0x4) > 0) {
                    ++x;
                }
                final double d = (x - this.p1.x) / this.dv.x;
                if (d >= 0.0 && d <= bp) {
                    bp = d;
                    sd = (((this.dir & 0x4) > 0) ? 4 : 5);
                }
            }
            if (this.dv.y != 0.0) {
                int y = this.yp;
                if ((this.dir & 0x1) > 0) {
                    ++y;
                }
                final double d = (y - this.p1.y) / this.dv.y;
                if (d >= 0.0 && d <= bp) {
                    bp = d;
                    sd = (((this.dir & 0x1) <= 0) ? 1 : 0);
                }
            }
            if (this.dv.z != 0.0) {
                int z = this.zp;
                if ((this.dir & 0x2) > 0) {
                    ++z;
                }
                final double d = (z - this.p1.z) / this.dv.z;
                if (d >= 0.0 && d <= bp) {
                    bp = d;
                    sd = (((this.dir & 0x2) > 0) ? 2 : 3);
                }
            }
            switch (this.face = sd) {
                case 0: {
                    ++this.yp;
                    break;
                }
                case 1: {
                    --this.yp;
                    break;
                }
                case 2: {
                    ++this.zp;
                    break;
                }
                case 3: {
                    --this.zp;
                    break;
                }
                case 4: {
                    ++this.xp;
                    break;
                }
                case 5: {
                    --this.xp;
                    break;
                }
            }
            this.enter.set(this.exit);
            this.exit.set(this.dv);
            this.exit.multiply(bp);
            this.exit.add(this.p1);
            return bp >= 1.0;
        }
    }
    
    public static class BlockSnake
    {
        int fep;
        BlockRay ray;
        Vector3 org;
        Vector3 dest;
        Vector3 fracs;
        Vector3 frace;
        long seed;
        
        public BlockSnake(final Vector3 o, final Vector3 d, final long s) {
            this.fep = -1;
            this.org = new Vector3(o);
            this.dest = new Vector3(d);
            this.fracs = new Vector3(o);
            this.frace = new Vector3();
            this.seed = s;
            FractalLib.fillVector(this.frace, this.org, this.dest, 0.125f, this.seed);
            this.ray = new BlockRay(this.fracs, this.frace);
        }
        
        public boolean iterate() {
            if (this.fep == -1) {
                ++this.fep;
                return true;
            }
            if (!this.ray.step()) {
                return true;
            }
            if (this.fep == 8) {
                return false;
            }
            this.fracs.set(this.frace);
            FractalLib.fillVector(this.frace, this.org, this.dest, this.fep / 8.0f, this.seed);
            this.ray.set(this.fracs, this.frace);
            ++this.fep;
            return true;
        }
        
        public Vector3 get() {
            return new Vector3(this.ray.xp, this.ray.yp, this.ray.zp);
        }
    }
}
