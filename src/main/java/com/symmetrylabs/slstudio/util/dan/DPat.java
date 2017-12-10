package com.symmetrylabs.slstudio.util.dan;

import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.model.Sun;
import com.symmetrylabs.slstudio.util.FastHSB;
import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.color.LXColor;
import heronarts.lx.midi.LXMidiOutput;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.CompoundParameter;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.function.Consumer;

import static com.symmetrylabs.slstudio.util.Utils.noise;
import static processing.core.PApplet.*;
import static processing.core.PVector.angleBetween;

 //----------------------------------------------------------------------------------------------------------------------------------
public abstract class DPat extends LXPattern {
    //ArrayList<Pick>   picks  = new ArrayList<Pick>  ();
    public ArrayList<DBool> bools = new ArrayList<DBool>();
    public PVector pTrans = new PVector();
    public PVector mMax, mCtr, mHalf;
    public SplittableRandom splittableRandom = new SplittableRandom();

    public LXMidiOutput APCOut;
    public LXMidiOutput MidiFighterTwisterOut;
    public int nMaxRow = 53;
    public float LastJog = -1;
    public float[] xWaveNz, yWaveNz;
    public PVector xyzJog = new PVector(), modmin;

    public float NoiseMove = random(10000);
    public CompoundParameter pSpark;
    public CompoundParameter pWave;
    public CompoundParameter pRotX;
    public CompoundParameter pRotY;
    public CompoundParameter pRotZ;
    public CompoundParameter pSpin;
    public CompoundParameter pTransX;
    public CompoundParameter pTransY;
    public BooleanParameter pXsym, pYsym, pRsym, pXdup, pXtrip, pJog, pGrey;
    public BooleanParameter perSun;

    public float lxh() {
        return palette.getHuef();
    }

    public int c1c(float a) {
        return round(100 * constrain(a, 0, 1));
    }

    float interpWv(float i, float[] vals) {
        return interp(i - floor(i), vals[floor(i)], vals[ceil(i)]);
    }

    public void setNorm(PVector vec) {
        vec.set(vec.x / mMax.x, vec.y / mMax.y, vec.z / mMax.z);
    }

    void setRand(PVector vec) {
        vec.set(random(mMax.x), random(mMax.y), random(mMax.z));
    }

    void setVec(PVector vec, LXPoint p) {
        vec.set(p.x, p.y, p.z);
    }

    void interpolate(float i, PVector a, PVector b) {
        a.set(interp(i, a.x, b.x), interp(i, a.y, b.y), interp(i, a.z, b.z));
    }

    public float val(CompoundParameter p) {
        return p.getValuef();
    }

    protected abstract void StartRun(double deltaMs);
    protected abstract int CalcPoint(PVector p);

    int blend3(int c1, int c2, int c3) {
        return PImage.blendColor(c1, PImage.blendColor(c2, c3, ADD), ADD);
    }

    public void rotateZ(PVector p, PVector o, float nSin, float nCos) {
        p.set(nCos * (p.x - o.x) - nSin * (p.y - o.y) + o.x, nSin * (p.x - o.x) + nCos * (p.y - o.y) + o.y, p.z);
    }

    void rotateX(PVector p, PVector o, float nSin, float nCos) {
        p.set(p.x, nCos * (p.y - o.y) - nSin * (p.z - o.z) + o.y, nSin * (p.y - o.y) + nCos * (p.z - o.z) + o.z);
    }

    void rotateY(PVector p, PVector o, float nSin, float nCos) {
        p.set(nSin * (p.z - o.z) + nCos * (p.x - o.x) + o.x, p.y, nCos * (p.z - o.z) - nSin * (p.x - o.x) + o.z);
    }

    public CompoundParameter addParam(String label, double value) {
        CompoundParameter p = new CompoundParameter(label, value);
        addParameter(p);
        return p;
    }

    CompoundParameter addParam(String label, double value, double min, double max) {
        CompoundParameter p2 = new CompoundParameter(label, value, min, max);
        addParameter(p2);
        return p2;
    }

    PVector vT1 = new PVector(), vT2 = new PVector();

    public float calcCone(PVector v1, PVector v2, PVector c) {
        vT1.set(v1);
        vT2.set(v2);
        vT1.sub(c);
        vT2.sub(c);
        return degrees(angleBetween(vT1, vT2));
    }

    // Pick        addPick(String name, int def, int _max, String[] desc) {
    //     Pick P      = new Pick(name, def, _max+1, nMaxRow, desc);
    //     nMaxRow     = P.EndRow + 1;
    //     picks.add(P);
    //     return P;
    // }

    // float interp(float v1, float v2, float amt) {
    //     return (float)LXUtils.lerp(v1, v2, amt);
    // }

    public float random(float max) {
        return (float) splittableRandom.nextDouble((double) max);
    }

    float random(float min, float max) {
        return (float) splittableRandom.nextDouble((double) min, (double) max) + min;
    }

    boolean btwn(int a, int b, int c) {
        return a >= b && a <= c;
    }

    boolean btwn(double a, double b, double c) {
        return a >= b && a <= c;
    }

    float interp(float a, float b, float c) {
        return (1 - a) * b + a * c;
    }

    float randctr(float a) {
        return (float) (splittableRandom.nextDouble((double) a) - a * 0.5f);
    }

    float min4(float a, float b, float c, float d) {
        return min(min(a, b), min(c, d));
    }

    float pointDist(LXPoint p1, LXPoint p2) {
        return dist(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }

    float xyDist(LXPoint p1, LXPoint p2) {
        return dist(p1.x, p1.y, p2.x, p2.y);
    }

    float distToSeg(float x, float y, float x1, float y1, float x2, float y2) {
        float A = x - x1, B = y - y1, C = x2 - x1, D = y2 - y1;
        float dot = A * C + B * D, len_sq = C * C + D * D;
        float xx, yy, param = dot / len_sq;

        if (param < 0 || (x1 == x2 && y1 == y2)) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }
        float dx = x - xx, dy = y - yy;
        return sqrt(dx * dx + dy * dy);
    }

/* Pre PatternControls UI
    boolean noteOn(LXMidiNote note) {
        if (handleNote(note)) {
                updateLights();
                return true;
        } else {
                return false;
        }
        }

        boolean handleNote(LXMidiNote note) {
                int row = note.getPitch(), col = note.getChannel();
                for (int i=0; i<picks.size(); i++) if (picks.get(i).set(row, col))          { presetManager.dirty(this); return true; }
                for (int i=0; i<bools.size(); i++) if (bools.get(i).toggle(row, col))   { presetManager.dirty(this); return true; }
                println("row: " + row + "  col:   " + col);
                return false;
        }

        void        onInactive()            { uiDebugText.setText(""); }
*/


    // boolean noteOn(LXMidiNote note) {return false;}

    // boolean handleNote(LXMidiNote note) {return false;}

    public void onInactive() {
    }

    void onReset() {
        // for (int i=0; i<bools .size(); i++) bools.get(i).reset();
        // for (int i=0; i<picks .size(); i++) picks.get(i).reset();
        //presetManager.dirty(this);
        //  updateLights(); now handled by patternControl UI
    }

    public DPat(LX lx) {
        super(lx);

        pSpark = addParam("Sprk", 0);
        pWave = addParam("Wave", 0);
        pTransX = addParam("TrnX", .5);
        pTransY = addParam("TrnY", .5);
        pRotX = addParam("RotX", .5);
        pRotY = addParam("RotY", .5);
        pRotZ = addParam("RotZ", .5);
        pSpin = addParam("Spin", .5);

        perSun = new BooleanParameter("perSun");
        addParameter(perSun);

        pXsym = new BooleanParameter("X-SYM");
        pYsym = new BooleanParameter("Y-SYM");
        pRsym = new BooleanParameter("R-SYM");
        pXdup = new BooleanParameter("X-DUP");
        pJog = new BooleanParameter("JOG");
        pGrey = new BooleanParameter("GREY");


        // addNonKnobParameter(pXsym);
        // addNonKnobParameter(pYsym);
        // addNonKnobParameter(pRsym);
        // addNonKnobParameter(pXdup);
        // addNonKnobParameter(pJog);
        // addNonKnobParameter(pGrey);

        //addMultipleParameterUIRow("Bools",pXsym,pYsym,pRsym,pXdup,pJog,pGrey);

        modmin = new PVector(model.xMin, model.yMin, model.zMin);
        mMax = new PVector(model.xMax, model.yMax, model.zMax);
        mMax.sub(modmin);
        mCtr = new PVector();
        mCtr.set(mMax);
        mCtr.mult(.5f);
        mHalf = new PVector(.5f, .5f, .5f);
        xWaveNz = new float[ceil(mMax.y) + 1];
        yWaveNz = new float[ceil(mMax.x) + 1];
        //println (model.xMin + " " + model.yMin + " " +  model.zMin);
        //println (model.xMax + " " + model.yMax + " " +  model.zMax);
        //for (MidiOutputDevice o: RWMidi.getOutputDevices()) { if (o.toString().contains("APC")) { APCOut = o.createOutput(); break;}}
    }

    public float spin() {
        float raw = val(pSpin);
        if (raw <= 0.45f) {
            return raw + 0.05f;
        } else if (raw >= 0.55f) {
            return raw - 0.05f;
        }
        return 0.5f;
    }

    // void setAPCOutput(LXMidiOutput output) {
    //   APCOut = output;
    // }

    // void setMidiFighterTwisterOutput(LXMidiOutput output) {
    //     MidiFighterTwisterOut = output;
    // }

    //Pre patternControls UI
    // void updateLights() {     if (APCOut == null ) return;
    //     for (int i = 0; i < NumApcRows; ++i)
    //         for (int j = 0; j < 8; ++j)         APCOut.sendNoteOn(j, 53+i,  0);
    //     for (int i=0; i<picks .size(); i++)     APCOut.sendNoteOn(picks.get(i).CurCol, picks.get(i).CurRow, 3);
    //     for (int i=0; i<bools .size(); i++)     if (bools.get(i).b)     APCOut.sendNoteOn   (bools.get(i).col, bools.get(i).row, 1);
    //                                             else                    APCOut.sendNoteOff  (bools.get(i).col, bools.get(i).row, 0);
    // }

    void updateLights() {
    }

    public void run(double deltaMs) {
          /* pre patternControls UI
                    if (this == midiEngine.getFocusedPattern()) {
                        String Text1="", Text2="";
                        for (int i=0; i<bools.size(); i++) if (bools.get(i).b) Text1 += " " + bools.get(i).tag       + "   ";
                        for (int i=0; i<picks.size(); i++) Text1 += picks.get(i).tag + ": " + picks.get(i).CurDesc() + "   ";
                        //uiDebugText.setText(Text1, Text2);
                }*/

        NoiseMove += deltaMs;
        NoiseMove = (float) (NoiseMove % 1e7);
        StartRun(deltaMs);

        pTrans.set(val(pTransX) * 200 - 100, val(pTransY) * 100 - 50, 0);

        if (pJog.getValueb()) {
            float tRamp = (lx.tempo.rampf() % .25f);
            if (tRamp < LastJog) xyzJog.set(randctr(mMax.x * .2f), randctr(mMax.y * .2f), randctr(mMax.z * .2f));
            LastJog = tRamp;
        }

        // precalculate this stuff
        final float wvAmp = val(pWave), sprk = val(pSpark);
        if (wvAmp > 0) {
            for (int i = 0; i < ceil(mMax.x) + 1; i++)
                yWaveNz[i] = wvAmp * (noise((float) (i / (mMax.x * .3f) - (2e3 + NoiseMove) / 1500f)) - .5f) * (mMax.y / 2f);

            for (int i = 0; i < ceil(mMax.y) + 1; i++)
                xWaveNz[i] = wvAmp * (noise((float) (i / (mMax.y * .3f) - (1e3 + NoiseMove) / 1500f)) - .5f) * (mMax.x / 2f);
        }

        // TODO Threadding: For some reason, using parallelStream here messes up the animations.

        if (perSun.isOn()) {
            SLModel slModel = (SLModel) model;
            Arrays.asList(slModel.masterSun.points).parallelStream().forEach(new Consumer<LXPoint>() {
                @Override
                public void accept(final LXPoint p) {
                    PVector P = new PVector(), tP = new PVector();

                    setVec(P, p);
                    P.sub(modmin);
                    P.sub(pTrans);
                    if (sprk > 0) {
                        P.y += sprk * randctr(50);
                        P.x += sprk * randctr(50);
                        P.z += sprk * randctr(50);
                    }
                    if (wvAmp > 0) P.y += interpWv(p.x - modmin.x, yWaveNz);
                    if (wvAmp > 0) P.x += interpWv(p.y - modmin.y, xWaveNz);
                    if (pJog.getValueb()) P.add(xyzJog);


                    int cNew, cOld = colors[p.index];
                    {
                        tP.set(P);
                        cNew = CalcPoint(tP);
                    }
                    if (pXsym.getValueb()) {
                        tP.set(mMax.x - P.x, P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pYsym.getValueb()) {
                        tP.set(P.x, mMax.y - P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pRsym.getValueb()) {
                        tP.set(mMax.x - P.x, mMax.y - P.y, mMax.z - P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pXdup.getValueb()) {
                        tP.set((P.x + mMax.x * .5f) % mMax.x, P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pGrey.getValueb()) {
                        cNew = FastHSB.hsb(0, 0, LXColor.b(cNew));
                    }
                    colors[p.index] = cNew;
                }
            });

            for (Sun sun : slModel.suns) {
                sun.copyFromMasterSun(colors);
            }
        } else {
            ((SLModel) model).forEachPoint((start, end) -> {
                for (int i = start; i < end; i++) {
                    LXPoint p = model.points[i];
                    PVector P = new PVector(), tP = new PVector();

                    setVec(P, p);
                    P.sub(modmin);
                    P.sub(pTrans);
                    if (sprk > 0) {
                        P.y += sprk * randctr(50);
                        P.x += sprk * randctr(50);
                        P.z += sprk * randctr(50);
                    }
                    if (wvAmp > 0) P.y += interpWv(p.x - modmin.x, yWaveNz);
                    if (wvAmp > 0) P.x += interpWv(p.y - modmin.y, xWaveNz);
                    if (pJog.getValueb()) P.add(xyzJog);


                    int cNew, cOld = colors[p.index];
                    {
                        tP.set(P);
                        cNew = CalcPoint(tP);
                    }
                    if (pXsym.getValueb()) {
                        tP.set(mMax.x - P.x, P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pYsym.getValueb()) {
                        tP.set(P.x, mMax.y - P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pRsym.getValueb()) {
                        tP.set(mMax.x - P.x, mMax.y - P.y, mMax.z - P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pXdup.getValueb()) {
                        tP.set((P.x + mMax.x * .5f) % mMax.x, P.y, P.z);
                        cNew = PImage.blendColor(cNew, CalcPoint(tP), ADD);
                    }
                    if (pGrey.getValueb()) {
                        cNew = FastHSB.hsb(0, 0, LXColor.b(cNew));
                    }
                    colors[p.index] = cNew;
                }
            });
        }

    }
}
