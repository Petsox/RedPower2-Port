package com.eloraam.redpower.core;

public class MathLib
{
    private static Matrix3[] orientMatrixList;
    private static Quat[] orientQuatList;
    
    public static void orientMatrix(final Matrix3 m, final int down, final int rot) {
        m.set(MathLib.orientMatrixList[down * 4 + rot]);
    }
    
    public static Quat orientQuat(final int down, final int rot) {
        return new Quat(MathLib.orientQuatList[down * 4 + rot]);
    }
    
    static {
        MathLib.orientMatrixList = new Matrix3[24];
        MathLib.orientQuatList = new Quat[24];
        Quat q2 = Quat.aroundAxis(1.0, 0.0, 0.0, 3.141592653589793);
        for (int j = 0; j < 4; ++j) {
            Quat q3 = Quat.aroundAxis(0.0, 1.0, 0.0, -1.5707963267948966 * j);
            MathLib.orientQuatList[j] = q3;
            q3 = new Quat(q3);
            q3.multiply(q2);
            MathLib.orientQuatList[j + 4] = q3;
        }
        for (int i = 0; i < 4; ++i) {
            final int k = (i >> 1 | i << 1) & 0x3;
            q2 = Quat.aroundAxis(0.0, 0.0, 1.0, 1.5707963267948966);
            q2.multiply(Quat.aroundAxis(0.0, 1.0, 0.0, 1.5707963267948966 * (k + 1)));
            for (int j = 0; j < 4; ++j) {
                final Quat q3 = new Quat(MathLib.orientQuatList[j]);
                q3.multiply(q2);
                MathLib.orientQuatList[8 + 4 * i + j] = q3;
            }
        }
        for (int i = 0; i < 24; ++i) {
            MathLib.orientMatrixList[i] = new Matrix3(MathLib.orientQuatList[i]);
        }
    }
}
