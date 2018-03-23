package com.symmetrylabs.slstudio.pattern;

import com.symmetrylabs.slstudio.pattern.base.SLModelPattern;
import heronarts.lx.LX;
import heronarts.lx.audio.GraphicMeter;
import heronarts.lx.audio.LXAudioInput;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;

import static processing.core.PApplet.*;


public class CubeEQ extends SLModelPattern {

    private LXAudioInput audioInput = lx.engine.audio.getInput();
    private GraphicMeter eq = new GraphicMeter(audioInput);

    private final CompoundParameter edge = new CompoundParameter("EDGE", 0.5);
    private final CompoundParameter clr = new CompoundParameter("CLR", 0.1, 0, .5);
    private final CompoundParameter blockiness = new CompoundParameter("BLK", 0.5);

    private final CompoundParameter gain = new CompoundParameter("GAIN", 0.5);
    private final CompoundParameter range = new CompoundParameter("RANG", 0.2);
    private final CompoundParameter attack = new CompoundParameter("ATTK", 0.4);
    private final CompoundParameter release = new CompoundParameter("RLS", 0.4);
    private final CompoundParameter slope = new CompoundParameter("SLOP", 0.5);

    public CubeEQ(LX lx) {
        super(lx);
        eq.start();
        addParameter(gain);
        addParameter(range);
        addParameter(attack);
        addParameter(release);
        addParameter(slope);

        addParameter(edge);
        addParameter(clr);
        addParameter(blockiness);
        addModulator(eq).start();
    }

    public void onActive() {
        // eq.range.setValue(48);
        // eq.release.setValue(300);
    }

    public void run(double deltaMs) {
        eq.gain.setNormalized(gain.getValuef());
        eq.range.setNormalized(range.getValuef());
        eq.attack.setNormalized(attack.getValuef());
        eq.release.setNormalized(release.getValuef());
        eq.slope.setNormalized(release.getValuef());

        final float edgeConst = 2 + 30 * edge.getValuef();
        final float clrConst = 1.1f + clr.getValuef();

        model.forEachPoint((start, end) -> {
            for (int i = start; i < end; i++) {
                LXPoint p = model.points[i];

                float avgIndex = constrain(2 + p.x / model.xMax * (eq.numBands - 4), 0, eq.numBands - 4);
                int avgFloor = (int) avgIndex;

                float leftVal = eq.getBandf(avgFloor);
                float rightVal = eq.getBandf(avgFloor + 1);
                float smoothValue = lerp(leftVal, rightVal, avgIndex - avgFloor);

                float chunkyValue = (
                    eq.getBandf(avgFloor / 4 * 4) +
                        eq.getBandf(avgFloor / 4 * 4 + 1) +
                        eq.getBandf(avgFloor / 4 * 4 + 2) +
                        eq.getBandf(avgFloor / 4 * 4 + 3)
                ) / 4f;

                float value = lerp(smoothValue, chunkyValue, blockiness.getValuef());

                float b = constrain(edgeConst * (value * model.yMax - p.y), 0, 100);
                colors[p.index] = lx.hsb(
                    480 + palette.getHuef() - min(clrConst * p.y, 120),
                    100,
                    b
                );
            }
        });
    }
}
