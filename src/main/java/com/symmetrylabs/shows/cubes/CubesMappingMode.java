package com.symmetrylabs.shows.cubes;

import java.lang.ref.WeakReference;
import java.util.*;

import com.symmetrylabs.slstudio.pattern.cubes.CubesMappingPattern;
import com.symmetrylabs.util.listenable.SetListener;
import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.LXChannel;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.EnumParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.StringParameter;

/**
 * Mapping Mode
 * (TODO)
 *  1) iterate through mapped cubes in order (tower by tower, cube by cube)
 *  2) get cubes not mapped but on network to pulse
 *  3) get a "display orientation" mode
 */

public class CubesMappingMode {

    public static enum MappingModeType {MAPPED, UNMAPPED};
    public static enum MappingDisplayModeType {ALL, ITERATE};

    private LXChannel mappingChannel = null;
    private LXPattern mappingPattern = null;

    public final BooleanParameter enabled;
    public final EnumParameter<MappingModeType> mode;
    public final EnumParameter<MappingDisplayModeType> displayMode;
    //public final BooleanParameter displayOrientation;

    public final DiscreteParameter selectedMappedFixture;
    public final DiscreteParameter selectedUnMappedFixture;

    public final StringParameter selectedControllerA;
    public final StringParameter selectedControllerB;

    public final SortedSet<String> fixturesMappedAndOnTheNetwork = new TreeSet<String>();
    public final SortedSet<String> fixturesMappedButNotOnNetwork = new TreeSet<String>();
    public final SortedSet<String> fixturesOnNetworkButNotMapped = new TreeSet<String>();

    private LX lx;
    private CubesModel cubesModel;

    private static Map<LX, WeakReference<CubesMappingMode>> instanceByLX = new WeakHashMap<>();

    public static CubesMappingMode getInstance(LX lx) {
        WeakReference<CubesMappingMode> weakRef = instanceByLX.get(lx);
        CubesMappingMode ref = weakRef == null ? null : weakRef.get();
        if (ref == null) {
            instanceByLX.put(lx, new WeakReference<>(ref = new CubesMappingMode(lx)));
        }
        return ref;
    }

    public void finalize(){
        System.out.println();
    }

    private CubesMappingMode(LX lx) {
        this.lx = lx;

        this.enabled = new BooleanParameter("enabled", false)
         .setDescription("Mapping Mode: toggle on/off");

        this.mode = new EnumParameter<MappingModeType>("mode", MappingModeType.MAPPED)
         .setDescription("Mapping Mode: toggle between mapped/unmapped fixtures");

        this.displayMode = new EnumParameter<MappingDisplayModeType>("displayMode", MappingDisplayModeType.ALL)
         .setDescription("Mapping Mode: display all mapped/unmapped fixtures");

        //this.displayOrientation = new BooleanParameter("displayOrientation", false)
        //        .setDescription("Mapping Mode: display colors on strips to indicate it's orientation");

        cubesModel = lx.model instanceof CubesModel ? (CubesModel)lx.model : new CubesModel();
        for (CubesModel.Cube cube : cubesModel.getCubes()) {
            // initially just add all fixtures to the fixtures pool
            fixturesMappedButNotOnNetwork.add(cube.id);
        }

        String[] emptyOptions = new String[] {"-"};

        String[] initialMappedFixtures = fixturesMappedButNotOnNetwork.isEmpty()
                ? emptyOptions : fixturesMappedButNotOnNetwork.toArray(new String[0]);

        selectedMappedFixture = new DiscreteParameter("selectedModelFixture", initialMappedFixtures);
        selectedUnMappedFixture = new DiscreteParameter("selectedUnMappedFixture", emptyOptions);

        selectedControllerA = new StringParameter("uninitialized");
        selectedControllerB = new StringParameter("uninitialized");

        CubesShow show = CubesShow.getInstance(lx);

        if (show != null) {
            show.addControllerSetListener(new SetListener<CubesController>() {
                public void onItemAdded(final CubesController c) {
                    if (isFixtureMapped(c.id)) {
                        fixturesMappedButNotOnNetwork.remove(c.id);
                        fixturesMappedAndOnTheNetwork.add(c.id);
                    } else {
                        fixturesOnNetworkButNotMapped.add(c.id);
                    }

                    selectedMappedFixture.setOptions(fixturesMappedAndOnTheNetwork.isEmpty() ? emptyOptions
                            : fixturesMappedAndOnTheNetwork.toArray(new String[0]));
                    selectedUnMappedFixture.setOptions(fixturesOnNetworkButNotMapped.isEmpty() ? emptyOptions
                            : fixturesOnNetworkButNotMapped.toArray(new String[0]));
                }
                public void onItemRemoved(final CubesController c) {}
            });
        }
        else {
            System.err.println("**WARNING** CubesMappingMode used before CubesShow has been initialized.");
        }

        enabled.addListener(p -> {
            if (((BooleanParameter)p).isOn()) {
                addChannel();
            }
            else {
                removeChannel();
            }
        });
    }

    public boolean isFixtureMapped(String id) {
        for (CubesModel.Cube fixture : cubesModel.getCubes()) {
            if (fixture.id.equals(id))
                return true;
        }
        return false;
    }

    public boolean inMappedMode() {
        return mode.getObject() == MappingModeType.MAPPED;
    }

    public boolean inUnMappedMode() {
        return mode.getObject() == MappingModeType.UNMAPPED;
    }

    public boolean inDisplayAllMode() {
        return displayMode.getObject() == MappingDisplayModeType.ALL;
    }

    public boolean inIterateFixturesMode() {
        return displayMode.getObject() == MappingDisplayModeType.ITERATE;
    }

    public String getSelectedMappedFixtureId() {
        String returnval = (String)selectedMappedFixture.getOption();
        selectedControllerA.setValue(returnval);
        return returnval;
    }

    public String getSelectedUnMappedFixtureId() {
        String returnval = (String)selectedUnMappedFixture.getOption();
        selectedControllerB.setValue(returnval);
        return returnval;
    }

    public boolean isSelectedUnMappedFixture(String id) {
        return id.equals(selectedUnMappedFixture.getOption());
    }

    public int getUnMappedColor() {
        //if (mappingPattern != null)
        //    return mappingPattern.getUnMappedButOnNetworkColor;
        //return 0;
        return LXColor.RED; // temp
    }

    private void addChannel() {
        mappingPattern = new CubesMappingPattern(lx);
        mappingChannel = lx.engine.addChannel(new LXPattern[] {mappingPattern});

        for (LXChannel channel : lx.engine.channels)
            channel.cueActive.setValue(false);

        mappingChannel.fader.setValue(1);
        mappingChannel.label.setValue("Mapping");
        mappingChannel.cueActive.setValue(true);
    }

    private void removeChannel() {
        lx.engine.removeChannel(mappingChannel);
        mappingChannel = null;
        mappingPattern = null;
    }
}
