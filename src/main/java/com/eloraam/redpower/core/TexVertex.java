package com.eloraam.redpower.core;

public class TexVertex
{
    public int vtx;
    public double u;
    public double v;
    public float r;
    public float g;
    public float b;
    public int brtex;
    
    public TexVertex() {
    }
    
    public TexVertex(final int vti, final int tn, final double ui, final double vi) {
        this.vtx = vti;
        this.u = (tn & 0xF) * 0.0625 + ui * 0.0625;
        this.v = (tn >> 4) * 0.0625 + vi * 0.0625;
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
    }
    
    public TexVertex(final int vti, final double ui, final double vi) {
        this.vtx = vti;
        this.u = ui;
        this.v = vi;
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
    }
    
    public void setUV(final double ui, final double vi) {
        this.u = ui;
        this.v = vi;
    }
    
    public TexVertex copy() {
        final TexVertex tr = new TexVertex(this.vtx, this.u, this.v);
        tr.r = this.r;
        tr.g = this.g;
        tr.b = this.b;
        tr.brtex = this.brtex;
        return tr;
    }
}
