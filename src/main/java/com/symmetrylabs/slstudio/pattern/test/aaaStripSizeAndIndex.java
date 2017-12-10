package com.symmetrylabs.slstudio.pattern.test;

import com.symmetrylabs.slstudio.model.Slice;
import com.symmetrylabs.slstudio.model.Strip;
import com.symmetrylabs.slstudio.model.Sun;
import com.symmetrylabs.slstudio.pattern.SLPattern;
import com.symmetrylabs.slstudio.util.FastHSB;
import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.DiscreteParameter;

/**
* @author Yona Appletree (yona@concentricsky.com)
*/
public class aaaStripSizeAndIndex extends SLPattern {
    final DiscreteParameter indexp = new DiscreteParameter("index", 100);
    final DiscreteParameter sizep = new DiscreteParameter("length", 35);

    public aaaStripSizeAndIndex (LX lx) {
        super(lx);
        addParameter(indexp);
        addParameter(sizep);
    }

    public void run(double deltaMs) {
        // convert parameters to int values
        int index = indexp.getValuei();
        int  size = sizep.getValuei();
        setColors(0);
        for (Sun sun : model.suns) {
            float hue = 0;
            for (Slice slice : sun.slices){
                int compare_index = 0;
                for (Strip strip : slice.strips) {
                    for (LXPoint p : strip.points) {
                        if ( (compare_index++ > index) && (compare_index < index + size) ){
                            colors[p.index] = FastHSB.hsb(120, 100, 100);
                        }
                    }
                }
            }
        }
    }
}
