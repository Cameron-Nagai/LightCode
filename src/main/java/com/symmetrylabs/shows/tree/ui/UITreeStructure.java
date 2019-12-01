package com.symmetrylabs.shows.tree.ui;

import com.symmetrylabs.shows.tree.TreeModel;
import static com.symmetrylabs.util.DistanceConstants.*;

import com.symmetrylabs.slstudio.ui.UICylinder;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.UI3dComponent;
import static processing.core.PConstants.*;
import processing.core.PGraphics;

public class UITreeStructure extends UI3dComponent {

    private final TreeModel tree;
    private static final int WOOD_FILL = 0xFF281403;

    public UITreeStructure(TreeModel tree) {
        this.tree = tree;
        addChild(new UICylinder(
            4f*FEET, 1.5f*FEET,
            0, 26*FEET, 15, WOOD_FILL));
    }
}
