
package com.eloraam.redpower.core;

import net.minecraft.client.gui.inventory.*;
import java.util.*;
import net.minecraft.inventory.*;

public abstract class GuiExtended extends GuiContainer
{
    ArrayList widgetList;
    
    public GuiExtended(final Container cn) {
        super(cn);
        this.widgetList = new ArrayList();
    }
}
