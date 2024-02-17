package com.eloraam.redpower.core;

import java.lang.reflect.*;

public class ReflectLib
{
    public static void callClassMethod(final String className, final String method, final Class[] def, final Object... params) {
        Class cl;
        try {
            cl = Class.forName(className);
        }
        catch (ClassNotFoundException var10) {
            return;
        }
        Method mth;
        try {
            mth = cl.getDeclaredMethod(method, (Class[])def);
        }
        catch (NoSuchMethodException var11) {
            return;
        }
        try {
            mth.invoke(null, params);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
    }
    
    public static <T> T getStaticField(final String classname, final String var, final Class<? extends T> varcl) {
        Class cl;
        try {
            cl = Class.forName(classname);
        }
        catch (ClassNotFoundException var2) {
            return null;
        }
        Field fld;
        try {
            fld = cl.getDeclaredField(var);
        }
        catch (NoSuchFieldException var3) {
            return null;
        }
        Object ob;
        try {
            ob = fld.get(null);
        }
        catch (IllegalAccessException | NullPointerException ex2) {
            final Exception ex;
            return null;
        }
        return (T)(varcl.isInstance(ob) ? ob : null);
    }
    
    public static <T> T getField(final Object ob, final String var, final Class<? extends T> varcl) {
        final Class cl = ob.getClass();
        Field fld;
        try {
            fld = cl.getDeclaredField(var);
        }
        catch (NoSuchFieldException var2) {
            return null;
        }
        Object ob2;
        try {
            ob2 = fld.get(ob);
        }
        catch (IllegalAccessException | NullPointerException ex2) {
            final Exception ex;
            return null;
        }
        return (T)(varcl.isInstance(ob2) ? ob2 : null);
    }
}
