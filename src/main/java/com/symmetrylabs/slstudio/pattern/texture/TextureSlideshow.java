package com.symmetrylabs.slstudio.pattern.texture;

import org.apache.commons.math3.util.FastMath;
import processing.core.PImage;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;

import com.symmetrylabs.slstudio.SLStudio;
import com.symmetrylabs.slstudio.model.Sun;
import com.symmetrylabs.slstudio.pattern.SunsPattern;

public abstract class TextureSlideshow extends SunsPattern {
    public final CompoundParameter rate = new CompoundParameter("rate", 3000, 10000, 250);
    public final BooleanParameter perSun = new BooleanParameter("perSun", false);
    public final CompoundParameter offsetX = new CompoundParameter("offsetX", 0, -1, 1);
    public final CompoundParameter offsetY = new CompoundParameter("offsetY", 0, -1, 1);
    public final CompoundParameter zoomX = new CompoundParameter("zoomX", 0, 0, 5);
    public final CompoundParameter zoomY = new CompoundParameter("zoomY", 0, 0, 5);
    public final BooleanParameter enableInterp = new BooleanParameter("interp", true);

    private final SawLFO lerp = (SawLFO) startModulator(new SawLFO(0, 1, rate));

    private int imageIndex = 0;
    private final PImage[] images;
    private final int[][] imageLayers;

    public TextureSlideshow(LX lx) {
        super(lx);

        String[] paths = getPaths();
        images = new PImage[paths.length];
        for (int i = 0; i < images.length; ++i) {
            images[i] = SLStudio.applet.loadImage(paths[i]);
            images[i].loadPixels();
        }

        imageLayers = new int[images.length][colors.length];

        addParameter(rate);
        addParameter(perSun);
        addParameter(offsetX);
        addParameter(offsetY);
        addParameter(zoomX);
        addParameter(zoomY);
        addParameter(enableInterp);

        LXParameterListener updateRastersListener = new LXParameterListener() {
            private boolean inProgress = false;

            public void onParameterChanged(LXParameter ignore) {
                synchronized (this) {
                    if (inProgress)
                        return;

                    inProgress = true;
                }

                updateRasters();

                synchronized (this) {
                    inProgress = false;
                }
            }
        };

        perSun.addListener(updateRastersListener);
        zoomX.addListener(updateRastersListener);
        zoomY.addListener(updateRastersListener);
        offsetX.addListener(updateRastersListener);
        offsetY.addListener(updateRastersListener);
        enableInterp.addListener(updateRastersListener);

        updateRasters();
    }

    abstract String[] getPaths();

    private int bilinearInterp(PImage image, double px, double py) {
        int imgOffsX = (int)(offsetX.getValue() * (image.width - 1) + image.width);
        int imgOffsY = (int)(offsetY.getValue() * (image.height - 1) + image.height);

        double zoomXValue = zoomX.getValue() + 1;
        double zoomYValue = zoomY.getValue() + 1;

        double imgX = px * (image.width - 1) / zoomXValue + imgOffsX;
        double imgY = py * (image.height - 1) / zoomYValue + imgOffsY;

        int imgXFloor = (int)FastMath.floor(imgX);
        int imgYFloor = (int)FastMath.floor(imgY);

        double xRem = imgX - imgXFloor;
        double yRem = imgY - imgYFloor;

        imgXFloor %= image.width;
        imgYFloor %= image.height;

        if (!enableInterp.isOn())
            return image.get(imgXFloor, imgYFloor);

        int imgXCeil = (int)FastMath.ceil(imgX) % image.width;
        int imgYCeil = (int)FastMath.ceil(imgY) % image.height;

        int q11 = image.get(imgXFloor, imgYFloor);
        int q12 = image.get(imgXFloor, imgYCeil);
        int q21 = image.get(imgXCeil, imgYFloor);
        int q22 = image.get(imgXCeil, imgYCeil);

        int q1 = LXColor.lerp(q11, q21, xRem);
        int q2 = LXColor.lerp(q12, q22, xRem);

        return LXColor.lerp(q1, q2, yRem);
    }

    private void updateRasters() {
        for (int i = 0; i < images.length; ++i) {
            PImage image = images[i];
            int[] layer = imageLayers[i];

            if (perSun.getValueb()) {
                for (Sun sun : model.getSuns()) {
                    for (LXPoint p : sun.points) {
                        double px = (p.x - sun.xMin) / sun.xRange;
                        double py = (p.y - sun.yMin) / sun.yRange;

                        layer[p.index] = bilinearInterp(image, px, py);
                    }
                }
            } else {
                for (LXPoint p : model.points) {
                    double px = (p.x - model.xMin) / model.xRange;
                    double py = (p.y - model.yMin) / model.yRange;

                    layer[p.index] = bilinearInterp(image, px, py);
                }
            }
        }
    }

    public void run(double deltaMs) {
        if (images.length == 0)
            return;

        double lerpValue = lerp.getValue();
        if (lerp.loop()) {
            imageIndex = (imageIndex + 1) % images.length;
        }

        int image1Index = imageIndex;
        int image2Index = (imageIndex + 1) % images.length;

        for (LXPoint p : lx.model.points) {
            int c1 = imageLayers[image1Index][p.index];
            int c2 = imageLayers[image2Index][p.index];

            colors[p.index] = LXColor.lerp(c1, c2, lerpValue);
        }
    }
}
