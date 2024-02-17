package com.eloraam.redpower.core;

public class OreStack
{
    public String material;
    public int quantity;
    
    public OreStack(final String mat, final int qty) {
        this.material = mat;
        this.quantity = qty;
    }
    
    public OreStack(final String mat) {
        this.material = mat;
        this.quantity = 1;
    }
}
