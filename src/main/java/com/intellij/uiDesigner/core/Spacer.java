package com.intellij.uiDesigner.core;

import java.awt.*;
import javax.swing.*;

public class Spacer extends JComponent {
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public final Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
