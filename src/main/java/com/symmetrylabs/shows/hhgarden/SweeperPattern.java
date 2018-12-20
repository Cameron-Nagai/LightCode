package com.symmetrylabs.shows.hhgarden;

import com.symmetrylabs.color.Ops16;
import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.pattern.base.SLPattern;
import com.symmetrylabs.slstudio.pattern.instruments.MarkUtils;
import com.symmetrylabs.slstudio.pattern.instruments.PointPartition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import heronarts.lx.LX;
import heronarts.lx.PolyBuffer;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.transform.LXVector;

import static heronarts.lx.PolyBuffer.Space.RGB16;

public class SweeperPattern extends SLPattern<SLModel> {
    private final CompoundParameter hueParam = new CompoundParameter("Hue", 0, -1, 1);
    private final CompoundParameter hueVarParam = new CompoundParameter("HueVar", 0, 0, 1);
    private final CompoundParameter satParam = new CompoundParameter("Sat", 0, 0, 1);

    private final CompoundParameter attackParam = new CompoundParameter("Attack", 0.5, 0, 1);
    private final CompoundParameter decayParam = new CompoundParameter("Decay", 0.5, 0, 4);

    protected Sweeper[] sweepers;
    protected PointPartition partition;

    protected float knobMin = 0.15f; // Novation LaunchKey knobs are hard to turn to extremes
    protected float knobMax = 0.85f;

    enum Axis {X, Y, Z};

    public SweeperPattern(LX lx) {
        super(lx);

        partition = new PointPartition(model.getPoints(), 30);

        sweepers = new Sweeper[] {
            new Sweeper("A", 1068, 713, true, 450, 0f, 0.55f),
            new Sweeper("B", 416, 653, true, 450, -0.04f, 0.7f),
            new Sweeper("C", 674, 616, true, 400, 0.4f, 0.67f),
          new Sweeper("D", 1062, 330, false, 418, 0.51f, -0.51f),
            new Sweeper("X", Axis.X, model.xMax, -model.xRange, null),
            new Sweeper("Z", Axis.Z, model.zMin, model.zRange, null),
            new Sweeper("XU", Axis.X, model.xMax, -model.xRange, new LXVector(model.cx, model.yMax, 0)),
            new Sweeper("XL", Axis.X, model.xMax, -model.xRange, new LXVector(model.cx, model.yMin, 0)),
        };

        addParameter(hueParam);
        addParameter(hueVarParam);
        addParameter(satParam);

        addParameter(attackParam);
        addParameter(decayParam);
    };

    protected void run(double deltaMs, PolyBuffer.Space preferredSpace) {
        long[] colors = (long[]) getPolyBuffer().getArray(RGB16);
        Arrays.fill(colors, Ops16.BLACK);
        getPolyBuffer().markModified(RGB16);
        for (Sweeper sweeper : sweepers) {
            sweeper.run(deltaMs, getPolyBuffer());
        }
    }

    class Sweeper {
        private CompoundParameter param;
        private float lastParamValue;
        private int[] indexes;
        private boolean[] states;
        private float[] positions;
        private float[] amplitudes;
        private boolean initialized;

        public Sweeper(String name, float x, float y, boolean useArcCluster, float radius, float startAngle, float sweepAngle) {
            initParam(name);

            LXVector center = new LXVector(x, y, model.cz);
            startAngle = floorMod(startAngle, 1);

            LXVector clusterCenter = new LXVector(center);
            if (useArcCluster) {
                double radians = (startAngle + sweepAngle/2) * (2 * Math.PI);
                clusterCenter.x += Math.cos(radians) * radius;
                clusterCenter.y += Math.sin(radians) * radius;
            }
            int cluster = getNearestCluster(clusterCenter);
            List<LXPoint> points = new ArrayList<>();
            for (LXPoint point : MarkUtils.getAllPointsWithin(model, center, radius)) {
              if (partition.getClusterNumber(point) == cluster) {
                  points.add(point);
                }
            }
            initArrays(points);
            int i = 0;
            for (LXPoint point : points) {
                float bearing = floorMod((float) (Math.atan2(point.y - y, point.x - x) / (2 * Math.PI)), 1);
                float pos = sweepAngle > 0 ?
                    floorMod(bearing - startAngle, 1) / sweepAngle :
                    floorMod(startAngle - bearing, 1) / -sweepAngle;
                positions[i++] = knobMin + pos * (knobMax - knobMin);
            }
        }

        public Sweeper(String name, Axis axis, float startValue, float sweepValue, LXVector clusterCenter) {
            initParam(name);

            List<LXPoint> points;
            if (clusterCenter != null) {
                int cluster = getNearestCluster(clusterCenter);
                points = partition.getCluster(cluster);
            } else {
                points = model.getPoints();
            }
            initArrays(points);
            int i = 0;
            for (LXPoint point : points) {
                positions[i++] =
                    axis == Axis.X ? (point.x - startValue) / sweepValue :
                        axis == Axis.Y ? (point.y - startValue) / sweepValue :
                            (point.z - startValue) / sweepValue;
            }
        }

        protected void initParam(String name) {
            param = new CompoundParameter(name);
            SweeperPattern.this.addParameter(param);
        }

        protected void initArrays(List<LXPoint> points) {
            indexes = new int[points.size()];
            states = new boolean[points.size()];
            positions = new float[points.size()];
            amplitudes = new float[points.size()];
            int i = 0;
            for (LXPoint point : points) {
                indexes[i++] = point.index;
            }
        }

        protected int getNearestCluster(LXVector center) {
            return partition.getClusterNumber(MarkUtils.getNearestPoint(model.getPoints(), center));
        }

        protected float floorMod(float num, float den) {
            float quo = (float) Math.floor(num / den);
            return num - (quo * den);
        }

        public void run(double deltaMs, PolyBuffer buffer) {
            float deltaSec = (float) deltaMs / 1000;
            float attackSec = attackParam.getValuef();
            float decaySec = decayParam.getValuef();

            float nextParamValue = param.getValuef();
            if (!initialized) {
                // Prevent a flash on startup when parameters change from zero.
                lastParamValue = nextParamValue;
                initialized = true;
            }
            float min = Math.min(lastParamValue, nextParamValue);
            float max = Math.max(lastParamValue, nextParamValue);

            for (int i = 0; i < indexes.length; i++) {
                float position = positions[i];
                if (min <= position && position < max) {
                    states[i] = true;
                    float progress = (nextParamValue - position) / (nextParamValue - lastParamValue);
                    amplitudes[i] += deltaSec * progress / attackSec;
                } else if (states[i]) {
                    amplitudes[i] += deltaSec / attackSec;
                } else {
                    amplitudes[i] -= deltaSec / decaySec;
                }
                if (amplitudes[i] >= 1) {
                    states[i] = false;
                }
                amplitudes[i] = Math.max(0, Math.min(1, amplitudes[i]));
            }

            double hue = hueParam.getValue() + hueVarParam.getValue() * param.getValuef();
            double sat = satParam.getValue();
            long[] colors = (long[]) buffer.getArray(RGB16);
            for (int i = 0; i < indexes.length; i++) {
                if (amplitudes[i] > 0) {
                    MarkUtils.addColor(colors, indexes[i], Ops16.hsb(hue, sat, amplitudes[i]));
                }
            }
            buffer.markModified(RGB16);

            lastParamValue = nextParamValue;
        }
    }
}