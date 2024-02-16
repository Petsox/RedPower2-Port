
package com.eloraam.redpower.core;

import java.util.*;

public class Vector3
{
    public double x;
    public double y;
    public double z;
    
    public Vector3() {
    }
    
    public Vector3(final double xi, final double yi, final double zi) {
        this.x = xi;
        this.y = yi;
        this.z = zi;
    }
    
    public Vector3(final Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public Object clone() {
        return new Vector3(this);
    }
    
    public void set(final double xi, final double yi, final double zi) {
        this.x = xi;
        this.y = yi;
        this.z = zi;
    }
    
    public void set(final Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public double dotProduct(final Vector3 v) {
        return v.x * this.x + v.y * this.y + v.z * this.z;
    }
    
    public double dotProduct(final double xi, final double yi, final double zi) {
        return xi * this.x + yi * this.y + zi * this.z;
    }
    
    public void crossProduct(final Vector3 v) {
        final double tx = this.y * v.z - this.z * v.y;
        final double ty = this.z * v.x - this.x * v.z;
        final double tz = this.x * v.y - this.y * v.x;
        this.x = tx;
        this.y = ty;
        this.z = tz;
    }
    
    public void add(final double xi, final double yi, final double zi) {
        this.x += xi;
        this.y += yi;
        this.z += zi;
    }
    
    public void add(final Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    
    public void subtract(final Vector3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }
    
    public void multiply(final double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }
    
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public double magSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public void normalize() {
        final double d = this.mag();
        if (d != 0.0) {
            this.multiply(1.0 / d);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Formatter fmt = new Formatter(sb, Locale.US);
        fmt.format("Vector:\n", new Object[0]);
        fmt.format("  < %f %f %f >\n", this.x, this.y, this.z);
        return sb.toString();
    }
}
