package com.symmetrylabs.shows.mikey;

import com.google.common.collect.Lists;
import com.symmetrylabs.shows.Show;
import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.slstudio.model.CandyBar;
import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.model.Strip;
import com.symmetrylabs.slstudio.model.StripsModel;
import com.symmetrylabs.slstudio.output.SimplePixlite;
import com.symmetrylabs.slstudio.output.PointsGrouping;
import com.symmetrylabs.slstudio.model.DoubleStrip;
import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXMatrix;
import heronarts.lx.transform.LXTransform;
import java.util.ArrayList;
import java.util.List;

public class MikeyShow implements Show {
    public static final String SHOW_NAME = "mikey";

    @Override
    public SLModel buildModel() {
        return MikeyModel.create();
    }

    @Override
    public void setupLx(LX lx) {
        MikeyPixlite pixlite = new MikeyPixlite(lx, "192.168.1.50", (MikeyModel) lx.model);
        lx.addOutput(pixlite);
    }

    static class MikeyModel extends StripsModel<Strip> {
        public MikeyModel(List<Strip> strips) {
            super(SHOW_NAME, strips);
        }

        public static MikeyModel create() {
            int ledStrip = 96;
            int dmx3 = 3;
            int dmx6 = 6;
            int dmx9 = 9;
            int dmx12 = 12;
            int dmxLightsOffset = -20;
            double zRotation = 1.57;
            int stripWidth = 1;
            List<Strip> strips = new ArrayList<Strip>();
            LXTransform t = new LXTransform();
            // Strip.Metrics metricsL4W = new Strip.Metrics(2, 2.15f); //strip config

            Strip.Metrics metricsStrip = new Strip.Metrics(ledStrip, stripWidth); //strip config
            Strip.Metrics metricsDMX3 = new Strip.Metrics(dmx3, 5); //strip config
            Strip.Metrics metricsDMX6 = new Strip.Metrics(dmx6, 5); //strip config
            Strip.Metrics metricsDMX9 = new Strip.Metrics(dmx9, 5); //strip config
            Strip.Metrics metricsDMX12 = new Strip.Metrics(dmx12, 5); //strip config




            //FARTHEST TWO LED STRIPS
            //----------------BOX 1 (PIXLITE CHANNELS 1-4)--------------
            //Strip-1
            t.translate(0, 0, 0);
            t.push();
            t.rotateZ(zRotation);
            Strip strip1 = new Strip("1", metricsStrip, t);         //create the first strip
            strips.add(strip1);  
            t.pop();

            //Strip-2
            t.push();
            t.translate(10, 0, 0);
            t.rotateZ(zRotation);
            Strip strip2 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip2);
            t.pop();

            //Strip-3
            t.push();
            t.translate(50, 0, 0);
            t.rotateZ(zRotation);
            Strip strip3 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip3);
            t.pop();

            //FARTHEST DMX LIGHTS
            t.push();
            t.translate(0, 0, dmxLightsOffset);
            t.rotateZ(0);
            Strip strip4 = new Strip("1", metricsDMX9, t);         //create the second strip
            strips.add(strip4);                                                  //add the first strip to strip array
            t.pop();
            //----------------BOX 1 --------------


            //----------------BOX 2-1 (PIXLITE CHANNELS 5-7) ---------------
            //Strip-4
            t.push();
            t.translate(60, 0, 0);
            t.rotateZ(zRotation);
            t.translate(0,0,0);
            Strip strip5 = new Strip("1", metricsStrip, t);         //create the second strip
            strips.add(strip5);                                                  //add the first strip to strip array
            t.pop();

            //Strip-5
            t.push();
            t.translate(100, 0, 0);
            t.rotateZ(zRotation);
            Strip strip6 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip6);
            t.pop();

            //Strip-6
            t.push();
            t.translate(110, 0, 0);
            t.rotateZ(zRotation);
            Strip strip7 = new Strip("1", metricsStrip, t);         //create the second strip
            strips.add(strip7);                                                  //add the first strip to strip array
            t.pop();

            t.push();
            t.translate(150, 0, 0);
            t.rotateZ(0);
            Strip strip8 = new Strip("1", metricsStrip, t);         //create the second strip
            strips.add(strip8);                                                  //add the first strip to strip array
            t.pop();
            //----------------BOX 2 --------------


            //----------------BOX 2-2 (PIXLITE CHANNELS 9-12)--------------
            //SOLO DMX LIGHT 
            t.push();
            t.translate(60, 0, dmxLightsOffset);
            t.rotateZ(zRotation);
            Strip strip9 = new Strip("1", metricsDMX9, t);         //create the second strip
            strips.add(strip9);                                                  //add the first strip to strip array
            t.pop();

            //3 LED STRIPS
            //Strip-8
            t.push();
            t.translate(160, 0, 0);
            t.rotateZ(zRotation);
            Strip strip10 = new Strip("1", metricsStrip, t);         //create the second strip
            strips.add(strip10);                                                  //add the first strip to strip array
            t.pop();

            //Strip-9
            t.push();
            t.translate(180, 0, 0);
            t.rotateZ(zRotation);
            Strip strip11 = new Strip("1", metricsStrip, t);         //create the second strip
            strips.add(strip11);                                                  //add the first strip to strip array
            t.pop();

            t.push();
            t.translate(190, 0, 0);
            t.rotateZ(0);
            Strip strip12 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip12);
            t.pop();

            //----------------BOX 4 (PIXLITE CHANNELS 13-16)--------------
            
            //Strip-10
            t.push();
            t.translate(240, 0, dmxLightsOffset);
            t.rotateZ(zRotation);
            Strip strip13 = new Strip("1", metricsDMX9, t);         //create the turn in the first strip
            strips.add(strip13);
            t.pop();


            t.push();
            t.translate(250, 0, 0);
            t.rotateZ(zRotation);
            Strip strip14 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip14);
            t.pop();

            t.push();
            t.translate(270, 0, 0);
            t.rotateZ(zRotation);
            Strip strip15 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip15);
            t.pop();

            t.push();
            t.translate(280, 0, 0);
            t.rotateZ(zRotation);
            Strip strip16 = new Strip("1", metricsStrip, t);         //create the turn in the first strip
            strips.add(strip16);
            t.pop();
            //----------------BOX 4--------------


            //----------------BOX 5 (PIXLITE CHANNELS 17-20)---------------
            //FAR RIGHT DMX LIGHTS
            t.push();
            t.translate(240, 0, dmxLightsOffset);
            t.rotateZ(0);
            Strip strip17 = new Strip("1", metricsDMX9, t);         //create the turn in the first strip
            strips.add(strip17);
            t.pop();

            //TOP DMX LIGHTS
            t.push();
            t.translate(190, 110, dmxLightsOffset);
            t.rotateZ(0);
            Strip strip18 = new Strip("1", metricsDMX9, t);         //create the turn in the first strip
            strips.add(strip18);
            t.pop();
            //----------------BOX 5--------------s
            return new MikeyModel(strips);
        }
    }
    static class MikeyPixlite extends SimplePixlite {
        public MikeyPixlite(LX lx, String ip, MikeyModel model) {
            super(lx, ip);
            //BOX 1
            //LED LIGHTS
            addPixliteOutput(
                new PointsGrouping("6").addPoints(model.getStripByIndex(0).getPoints()));
            addPixliteOutput(
                new PointsGrouping("5").addPoints(model.getStripByIndex(1).getPoints()));
            addPixliteOutput(
                new PointsGrouping("7").addPoints(model.getStripByIndex(2).getPoints()));
            //DMX LIGHTS
            addPixliteOutput(
                new PointsGrouping("8").addPoints(model.getStripByIndex(3).getPoints()));

            //BOX 2-1
            //LED LIGHTS
            addPixliteOutput(
                new PointsGrouping("9").addPoints(model.getStripByIndex(4).getPoints()));
            addPixliteOutput(
                new PointsGrouping("10").addPoints(model.getStripByIndex(5).getPoints()));
            addPixliteOutput(
                new PointsGrouping("11").addPoints(model.getStripByIndex(6).getPoints()));
            addPixliteOutput(
                new PointsGrouping("12").addPoints(model.getStripByIndex(7).getPoints()));
            //BOX 2-2
            //DMX LIGHT
            addPixliteOutput(
                new PointsGrouping("21").addPoints(model.getStripByIndex(8).getPoints()));

            //BOX 3
            //LED STRIPS
            addPixliteOutput(
                new PointsGrouping("18").addPoints(model.getStripByIndex(9).getPoints()));
            addPixliteOutput(
                new PointsGrouping("17").addPoints(model.getStripByIndex(10).getPoints()));
            addPixliteOutput(
                new PointsGrouping("19").addPoints(model.getStripByIndex(11).getPoints()));
            //DMX LIGHTS
            addPixliteOutput(
                new PointsGrouping("20").addPoints(model.getStripByIndex(12).getPoints()));

            //BOX 4
            //LED LIGHTS
            addPixliteOutput(
                new PointsGrouping("15").addPoints(model.getStripByIndex(13).getPoints()));
            addPixliteOutput(
                new PointsGrouping("14").addPoints(model.getStripByIndex(14).getPoints()));
            //DMX LIGHTS
            addPixliteOutput(
                new PointsGrouping("17").addPoints(model.getStripByIndex(15).getPoints()));

            //BOX 5
            //DMX ROOF
            addPixliteOutput(
                new PointsGrouping("25").addPoints(model.getStripByIndex(17).getPoints()));
        //     addPixliteOutput(
        //         new PointsGrouping("11").addPoints(model.getStripByIndex(15).getPoints()));
        //     addPixliteOutput(
        //         new PointsGrouping("12").addPoints(model.getStripByIndex(16).getPoints()));
        //     addPixliteOutput(
        //         new PointsGrouping("13").addPoints(model.getStripByIndex(17).getPoints()));

        }

        @Override
        public SimplePixlite addPixliteOutput(PointsGrouping pointsGrouping) {
            try {
                SimplePixliteOutput spo = new SimplePixliteOutput(pointsGrouping);
                spo.setLogConnections(false);
                addChild(spo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }
    }
}
