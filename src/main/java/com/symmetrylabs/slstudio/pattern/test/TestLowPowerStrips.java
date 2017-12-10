package com.symmetrylabs.slstudio.pattern.test;

import com.symmetrylabs.slstudio.model.Strip;
import com.symmetrylabs.slstudio.model.Sun;
import com.symmetrylabs.slstudio.pattern.SLPattern;
import com.symmetrylabs.slstudio.util.FastHSB;
import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;

/**
* @author Yona Appletree (yona@concentricsky.com)
*/
public class TestLowPowerStrips extends SLPattern {

    public TestLowPowerStrips(LX lx) {
        super(lx);
    }

    public void run(double deltaMs) {
        setColors(0);

        for (Sun sun : model.suns) {
            for (Strip strip : sun.strips) {
                int si = 0;
                for (LXPoint p : strip.points) {
                    if (si < 3) {
                        colors[p.index] =FastHSB.hsb(LXColor.RED, 100, 100);
                    }
                    if (si > strip.points.length - 3) {
                        colors[p.index] = FastHSB.hsb(LXColor.BLUE, 100, 100);
                    }
                    si++;
                }
            }
        }
    }
}
