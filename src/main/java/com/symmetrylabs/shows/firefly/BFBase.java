package com.symmetrylabs.shows.firefly;

import art.lookingup.KaledoscopeModel;
import art.lookingup.LUButterfly;
import art.lookingup.LUFlower;
import com.symmetrylabs.slstudio.pattern.base.SLPattern;
import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;

import java.util.*;

abstract public class BFBase extends SLPattern {

    protected static final Random random = new Random();
    protected Map<LUButterfly, Integer> randomInts;
    protected Map<LUFlower, Integer> randomFlowerInts;

    public BFBase(LX lx) {
        super(lx);

    }

    public void onActive() {
        randomInts = new HashMap<LUButterfly, Integer>();
        randomFlowerInts = new HashMap<LUFlower, Integer>();
        for (LUButterfly butterfly : KaledoscopeModel.allButterflies) {
            randomInts.put(butterfly, random.nextInt(1000));
        }
        for (LUFlower flower: KaledoscopeModel.allFlowers) {
            randomFlowerInts.put(flower, random.nextInt(1000));
        }
    }

    protected int getRandom(LUButterfly butterfly) {
        return randomInts.get(butterfly);
    }
    protected int getRandom(LUFlower flower) { return randomFlowerInts.get(flower); }

    @Override
    protected void run(double deltaMs) {
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.rgb(0, 0, 0);
        }
        preDraw(deltaMs);
        for (LUButterfly butterfly : KaledoscopeModel.allButterflies) {
            renderButterfly(deltaMs, butterfly, getRandom(butterfly));
        }
        for (LUFlower flower : KaledoscopeModel.allFlowers) {
            renderFlower(deltaMs, flower, getRandom(flower));
        }
        postDraw(deltaMs);
    }

    void preDraw(double deltaMs) {

    }

    void postDraw(double deltaMs) {

    }
    abstract void renderButterfly(double deltaMs, LUButterfly butterfly, int randomInt);

    protected void renderFlower(double deltaMs, LUFlower flower, int randomInt) {
        // Child class should override.
    }
}
