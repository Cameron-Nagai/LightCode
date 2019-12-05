package com.symmetrylabs.slstudio.cue;

import com.google.gson.JsonObject;
import heronarts.lx.LX;
import heronarts.lx.LXComponent;
import heronarts.lx.parameter.*;
import org.joda.time.DateTime;


public class Cue implements CueTypeAdapter {
    public final static int MAX_DURATION = 10;

    private static int uid_counter = 0;
    public int uid;
    public final StringParameter startAtStr;
    public final CompoundParameter durationMs;
    public final CompoundParameter durationSec;
    public final CompoundParameter fadeTo;
    public final BoundedParameter cuedParameter;
    public boolean isHourly = false;
    private DateTime startAt;

    public Cue(BoundedParameter cuedParameter) {
        this.uid = Cue.uid_counter++;

        this.cuedParameter = cuedParameter;

        startAtStr = new StringParameter("startAt", "00:00");
        startAt = DateTime.now().withTime(0, 0, 0, 0);
        durationMs = (CompoundParameter) new CompoundParameter("duration", 1000, 50, MAX_DURATION * 1000)
            .setExponent(4)
            .setUnits(LXParameter.Units.MILLISECONDS);

        // proxy just for nice display
        durationSec = (CompoundParameter) new CompoundParameter("duration (sec)", 1, 0.05, MAX_DURATION)
            .setExponent(4)
            .setUnits(LXParameter.Units.SECONDS).addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                durationMs.setValue(durationSec.getValue() * 1000);
            }
        });
        fadeTo = new CompoundParameter("fadeTo", cuedParameter.getValue(), cuedParameter.range.v0, cuedParameter.range.v1);

        startAtStr.addListener(p -> {
                String[] timebits = startAtStr.getString().split(":");
                if (timebits.length != 2 && timebits.length != 3) {
                    return;
                }
                int h, m, s;
                try {
                    h = Integer.parseInt(timebits[0]);
                    m = Integer.parseInt(timebits[1]);
                    s = timebits.length > 2 ? Integer.parseInt(timebits[2]) : 0;
                } catch (NumberFormatException e) {
                    return;
                }
                startAt = DateTime.now().withTime(h, m, s, 0);
            });

    }

    @Override
    public String toString() {
        return String.format("%.3f second transition on %s to %.3f starting at %02d:%02d",
                             durationMs.getValue() / 1000.0,
                             cuedParameter.getLabel(),
                             fadeTo.getValue(),
                             startAt.getHourOfDay(), startAt.getMinuteOfHour());
    }

    public void save(JsonObject obj) {
        obj.addProperty("startAt", startAtStr.getString());
        obj.addProperty("duration", durationMs.getValue());
        obj.addProperty("fadeTo", fadeTo.getValue());
        obj.addProperty("type", getCueType());
        /* TODO: store cued parameter path */
    }

    public void load(JsonObject obj) {
        startAtStr.setValue(obj.get("startAt").getAsString());
        durationMs.setValue(obj.get("duration").getAsDouble());
        fadeTo.setValue(obj.get("fadeTo").getAsDouble());
    }

    public DateTime getStartTime() {
        return startAt;
    }

    @Override
    public String getCueType() {
        return "normal";
    }
}
