package com.symmetrylabs.slstudio.pattern;

import com.symmetrylabs.slstudio.pattern.base.SLPattern;
import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.LXUtils;
import heronarts.lx.transform.LXVector;

import java.util.List;
import java.util.LinkedList;
import processing.core.PImage;
import java.util.Iterator;

import java.lang.Math;
import static processing.core.PApplet.*;
import static com.symmetrylabs.util.MathUtils.random;

public class Raindrops extends SLPattern {

    public final CompoundParameter numRainDrops = new CompoundParameter("NUM", -40, -500, -20);
    public final CompoundParameter size = new CompoundParameter("SIZE", 0.35, 0.1, 1.0);
    public final CompoundParameter speedP = new CompoundParameter("SPD", -1000, -7000, -300);
    public final CompoundParameter hueV = new CompoundParameter("HUE", 0.5);

    private float leftoverMs = 0;
    private float msPerRaindrop = 40;
    private List<Raindrop> raindrops;

    class Raindrop {
        LXVector p;
        LXVector v;
        float radius;
        float hue;
        float speed;

        Raindrop() {
            this.radius = (float)((model.yRange*0.4f)*size.getValuef());

            this.p = new LXVector(
                random(model.xMax - model.xMin) + model.xMin,
                model.yMax + this.radius,
                random(model.zMax - model.zMin) + model.zMin
            );

            float velMagnitude = 120;
            this.v = new LXVector(0, -3 * model.yMax, 0);
            this.hue = (random(0, 50) * hueV.getValuef()) + palette.getHuef();
            this.speed = abs(speedP.getValuef());
        }

        // returns TRUE when this should die
        boolean age(double ms) {
            p.add(new LXVector(v).mult((float) (ms / this.speed)));
            return this.p.y < (0 - this.radius);
        }
    }

    public Raindrops(LX lx) {
        super(lx);
        addParameter(numRainDrops);
        addParameter(size);
        addParameter(speedP);
        addParameter(hueV);
        raindrops = new LinkedList<Raindrop>();
    }

    private LXVector randomVector() {
        return new LXVector(
            random(model.xMax - model.xMin) + model.xMin,
            random(model.yMax - model.yMin) + model.yMin,
            random(model.zMax - model.zMin) + model.zMin
        );
    }

    public void run(double deltaMs) {
        leftoverMs += deltaMs;
        float msPerRaindrop = Math.abs(numRainDrops.getValuef());
        while (leftoverMs > msPerRaindrop) {
            leftoverMs -= msPerRaindrop;
            raindrops.add(new Raindrop());
        }

        for (LXPoint p : model.points) {
            int c = 0;
            for (Raindrop raindrop : raindrops) {
                if (p.x >= (raindrop.p.x - raindrop.radius) && p.x <= (raindrop.p.x + raindrop.radius) &&
                        p.y >= (raindrop.p.y - raindrop.radius) && p.y <= (raindrop.p.y + raindrop.radius)) {

                    float d = ((float)LXUtils.distance(raindrop.p.x, raindrop.p.y, p.x, p.y)) / raindrop.radius;
                    if (d < 1) {
                        c = PImage.blendColor(c, lx.hsb(raindrop.hue, 80, (float)Math.pow(1 - d, 0.01) * 100), ADD);
                    }
                }
            }
            colors[p.index] = c;
        }

        Iterator<Raindrop> i = raindrops.iterator();
        while (i.hasNext()) {
            Raindrop raindrop = i.next();
            boolean dead = raindrop.age(deltaMs);
            if (dead) {
                i.remove();
            }
        }
    }
}
