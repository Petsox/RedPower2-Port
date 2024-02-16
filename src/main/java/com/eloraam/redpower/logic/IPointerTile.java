
package com.eloraam.redpower.logic;

import com.eloraam.redpower.core.*;

public interface IPointerTile
{
    float getPointerDirection(final float p0);
    
    Quat getOrientationBasis();
}
