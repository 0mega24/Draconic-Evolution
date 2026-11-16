package com.brandon3055.draconicevolution.client.gui.modwiki.swing;

import java.awt.Window;
import org.lwjgl.opengl.Display;

/** Created by brandon3055 on 8/09/2016. */
public final class SwingHelper {

    private SwingHelper() {}

    public static void centerOnMinecraftWindow(Window window) {
        int centerX = Display.getX() + (Display.getWidth() / 2);
        int centerY = Display.getY() + (Display.getHeight() / 2);
        window.setLocation(centerX - (window.getWidth() / 2), centerY - (window.getHeight() / 2));
    }
}
