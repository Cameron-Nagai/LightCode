package com.symmetrylabs.slstudio.effect;

import com.symmetrylabs.slstudio.effect.SLEffect;
import com.symmetrylabs.slstudio.kernel.SLKernel;
import heronarts.lx.LX;
import heronarts.lx.parameter.CompoundParameter;


public class ColorShiftEffect extends SLEffect {
    public CompoundParameter shift = new CompoundParameter("Shift", 0, 0, 360);

    private final ColorShiftKernel kernel = new ColorShiftKernel();

    public ColorShiftEffect(LX lx) {
        super(lx);

        addParameter(shift);
    }

    public String getLabel() {
        return "ColorShift";
    }

    public void run(double deltaMs, double enabledAmount) {
//        kernel.colors = colors;
//        kernel.shiftValue = shift.getValuef();
//        kernel.executeForSize(model.points.length);
        float shiftValue = shift.getValuef();
        for (int i = 0; i < colors.length; i++) {
            colors[i] = kernel.shiftHue(colors[i], shiftValue);
        }
    }

    final static class ColorShiftKernel extends SLKernel {

        int[] colors;

        float shiftValue = 0;

        @Override public void run() {
            int i = getGlobalId();
            if (i >= colors.length) return;
            colors[i] = shiftHue(colors[i], shiftValue);
        }
    };
}
