package com.symmetrylabs.slstudio.model;

import java.util.List;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.model.LXFixture;

import static com.symmetrylabs.util.MathUtils.floor;


public class Strip extends LXModel {

    public static final float INCHES_PER_METER = 39.3701f;

    public final String id;
    public final Metrics metrics;

    public Strip(String id, Metrics metrics, List<LXPoint> points) {
        super(points);

        this.id = id;
        this.metrics = metrics;
    }

    public Strip(String id, Metrics metrics, LXFixture fixture) {
        super(fixture);

        this.id = id;
        this.metrics = metrics;
    }

    public static class Metrics {

        public final float length;
        public final int numPoints;
        public final int ledsPerMeter;

        public final float POINT_SPACING;

        public Metrics(float length, int numPoints, int ledsPerMeter) {
            this.length = length;
            this.numPoints = numPoints;
            this.ledsPerMeter = ledsPerMeter;
            this.POINT_SPACING = INCHES_PER_METER / ledsPerMeter;
        }

        public Metrics(int numPoints, float spacing) {
            this.length = numPoints * spacing;
            this.numPoints = numPoints;
            this.ledsPerMeter = floor((INCHES_PER_METER / this.length) * numPoints);
            this.POINT_SPACING = spacing;
        }
    }
}
