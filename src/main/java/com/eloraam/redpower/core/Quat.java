//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import java.util.*;

public class Quat
{
    public double x;
    public double y;
    public double z;
    public double s;
    public static final double SQRT2;
    
    public Quat() {
        this.s = 1.0;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public Quat(final Quat q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.s = q.s;
    }
    
    public Quat(final double si, final double xi, final double yi, final double zi) {
        this.x = xi;
        this.y = yi;
        this.z = zi;
        this.s = si;
    }
    
    public void set(final Quat q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.s = q.s;
    }
    
    public static Quat aroundAxis(final double xi, final double yi, final double zi, double a) {
        a *= 0.5;
        final double sn = Math.sin(a);
        return new Quat(Math.cos(a), xi * sn, yi * sn, zi * sn);
    }
    
    public void multiply(final Quat q) {
        final double ts = this.s * q.s - this.x * q.x - this.y * q.y - this.z * q.z;
        final double tx = this.s * q.x + this.x * q.s - this.y * q.z + this.z * q.y;
        final double ty = this.s * q.y + this.x * q.z + this.y * q.s - this.z * q.x;
        final double tz = this.s * q.z - this.x * q.y + this.y * q.x + this.z * q.s;
        this.s = ts;
        this.x = tx;
        this.y = ty;
        this.z = tz;
    }
    
    public void rightMultiply(final Quat q) {
        final double ts = this.s * q.s - this.x * q.x - this.y * q.y - this.z * q.z;
        final double tx = this.s * q.x + this.x * q.s + this.y * q.z - this.z * q.y;
        final double ty = this.s * q.y - this.x * q.z + this.y * q.s + this.z * q.x;
        final double tz = this.s * q.z + this.x * q.y - this.y * q.x + this.z * q.s;
        this.s = ts;
        this.x = tx;
        this.y = ty;
        this.z = tz;
    }
    
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.s * this.s);
    }
    
    public void normalize() {
        double d = this.mag();
        if (d != 0.0) {
            d = 1.0 / d;
            this.x *= d;
            this.y *= d;
            this.z *= d;
            this.s *= d;
        }
    }
    
    public void rotate(final Vector3 v) {
        final double ts = -this.x * v.x - this.y * v.y - this.z * v.z;
        final double tx = this.s * v.x + this.y * v.z - this.z * v.y;
        final double ty = this.s * v.y - this.x * v.z + this.z * v.x;
        final double tz = this.s * v.z + this.x * v.y - this.y * v.x;
        v.x = tx * this.s - ts * this.x - ty * this.z + tz * this.y;
        v.y = ty * this.s - ts * this.y + tx * this.z - tz * this.x;
        v.z = tz * this.s - ts * this.z - tx * this.y + ty * this.x;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Formatter fmt = new Formatter(sb, Locale.US);
        fmt.format("Quaternion:\n", new Object[0]);
        fmt.format("  < %f %f %f %f >\n", this.s, this.x, this.y, this.z);
        fmt.close();
        return sb.toString();
    }
    
    static {
        SQRT2 = Math.sqrt(2.0);
    }
}
