package com.symmetrylabs.layouts.cubes;

//package com.symmetrylabs.layouts.cubes;

import java.util.*;
import java.lang.ref.WeakReference;

import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.output.SLController;
import com.symmetrylabs.util.CubePhysicalIdMap;
import com.symmetrylabs.util.listenable.SetListener;
import heronarts.lx.LX;
import heronarts.lx.output.FadecandyOutput;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.transform.LXTransform;

import com.symmetrylabs.layouts.Layout;
import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.slstudio.network.NetworkMonitor;
import com.symmetrylabs.slstudio.network.NetworkDevice;
import com.symmetrylabs.util.dispatch.Dispatcher;
import com.symmetrylabs.util.listenable.ListenableSet;
import heronarts.p3lx.ui.UI2dScrollContext;

/**
 * This file implements the mapping functions needed to lay out the cubes.
 */
public class OfficeLayout implements Layout {
    ListenableSet<CubesController> controllers = new ListenableSet<>();
    CubePhysicalIdMap cubePhysicalIdMap = new CubePhysicalIdMap();

    static final float globalOffsetX = 0;
    static final float globalOffsetY = 0;
    static final float globalOffsetZ = 0;

    static final float globalRotationX = 0;
    static final float globalRotationY = 180;
    static final float globalRotationZ = 0;

    static final float CUBE_WIDTH = 24;
    static final float CUBE_HEIGHT = 24;
    static final float TOWER_WIDTH = 24;
    static final float TOWER_HEIGHT = 24;
    static final float CUBE_SPACING = 2.5f;

    static final float TOWER_VERTICAL_SPACING = 2.5f;
    static final float TOWER_RISER = 14;
    static final float SP = 24+2;
    static final float JUMP = TOWER_HEIGHT+TOWER_VERTICAL_SPACING;

    static final float INCHES_PER_METER = 39.3701f;

    static final TowerConfig[] TOWER_CONFIG = {
//        new TowerConfig(SP*0.f, 0, -SP*0.f, new String[] {"0", "0", "0", "0", "0"}),
//        new TowerConfig(SP*1.f, 0, -SP*1.f, new String[] {"0", "0", "0", "0", "0"}),
//        new TowerConfig(SP*2.f, 0, -SP*2.f, new String[] {"0", "0", "0", "0", "0"}),
//        new TowerConfig(SP*3.f, 0, -SP*3.f, new String[] {"0", "0", "0", "0", "0"}),
//        new TowerConfig(SP*4.f, 0, -SP*4.f, new String[] {"0", "0", "0", "0", "0"}),
//        new TowerConfig(SP*5.f, 0, -SP*5.f, new String[] {"0", "0", "0", "0", "0"}),

// back row right to left

        new TowerConfig(0, 0, -264+168, 0, 45, 0, new String[] { "d880399b2000", "d880396305af", "d8803962b00e", "398", "44"}), //397, 16, 50
        new TowerConfig(36, 0, -264+168, 0, 45, 0, new String[] { "d8803962f603", "87", "d880399b2b08", "001ec0f543f4"}),  //76, 420,  186
        new TowerConfig(54, SP*4, -264+168, 0, 45, 0, new String[] {"367"}),
        new TowerConfig(72, 0, -264+168, 0, 45, 0, new String[] { "d8803963520d", "336", "001ec0f56380", "82"}),//68, 199
        new TowerConfig(108, 0, -264+168, 0, 45, 0, new String[] { "d8803962cc02", "419", "1", "57", "415hp"}), //17

// 2nd to back row right to left

//        new TowerConfig(144, 0, -264+168, 0, 45, 0, new String[] { "0"}),
        new TowerConfig(24, 0, -228+168, 0, 45, 0, new String[] { "372", "6", "001ec0f43d1d", "4"}), // 211
        new TowerConfig(90 , 0, -228+168, 0, 45, 0, new String[] { "001ec0f4ea81", "d8803963052f", "001ec0f4abc2", "21"}), // 138, 27, 156

// 3rd from back r to l
        new TowerConfig(40, 0, -192+164, 0, 45, 0, new String[] { "001ec0f5270f", "418", "65"}),        //348
        new TowerConfig(74, 0, -192+164, 0, 45, 0, new String[] { "351", "79", "001ec0f54cd7"}),   //184

// front

        new TowerConfig(54, 0, -168+164, 0, 45, 0, new String[] { "d880399b2b0a", "001ec0f56df8", "353"}),     // 312, 189
    };

    static class TowerConfig {

        final CubesModel.Cube.Type type;
        final float x;
        final float y;
        final float z;
        final float xRot;
        final float yRot;
        final float zRot;
        final String[] ids;
        final float[] yValues;

        TowerConfig(float x, float y, float z, String[] ids) {
            this(CubesModel.Cube.Type.LARGE, x, y, z, ids);
        }

        TowerConfig(float x, float y, float z, float yRot, String[] ids) {
            this(x, y, z, 0, yRot, 0, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, String[] ids) {
            this(type, x, y, z, 0, 0, 0, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, float yRot, String[] ids) {
            this(type, x, y, z, 0, yRot, 0, ids);
        }

        TowerConfig(float x, float y, float z, float xRot, float yRot, float zRot, String[] ids) {
            this(CubesModel.Cube.Type.LARGE, x, y, z, xRot, yRot, zRot, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, float xRot, float yRot, float zRot, String[] ids) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
            this.xRot = xRot;
            this.yRot = yRot;
            this.zRot = zRot;
            this.ids = ids;

            this.yValues = new float[ids.length];
            for (int i = 0; i < ids.length; i++) {
                yValues[i] = y + i * (CUBE_HEIGHT + CUBE_SPACING);
            }
        }
    }

    public SLModel buildModel() {
        // Any global transforms
        LXTransform globalTransform = new LXTransform();
        globalTransform.translate(globalOffsetX, globalOffsetY, globalOffsetZ);
        globalTransform.rotateX(globalRotationX * Math.PI / 180.);
        globalTransform.rotateY(globalRotationY * Math.PI / 180.);
        globalTransform.rotateZ(globalRotationZ * Math.PI / 180.);

        /* Cubes ----------------------------------------------------------*/
        List<CubesModel.Tower> towers = new ArrayList<>();
        List<CubesModel.Cube> allCubes = new ArrayList<>();

        int stripId = 0;
        for (TowerConfig config : TOWER_CONFIG) {
            List<CubesModel.Cube> cubes = new ArrayList<>();
            float x = config.x;
            float z = config.z;
            float xRot = config.xRot;
            float yRot = config.yRot;
            float zRot = config.zRot;
            CubesModel.Cube.Type type = config.type;

            for (int i = 0; i < config.ids.length; i++) {
                float y = config.yValues[i];
                CubesModel.Cube cube = new CubesModel.Cube(config.ids[i], x, y, z, xRot, yRot, zRot, globalTransform, type);
                cubes.add(cube);
                allCubes.add(cube);
            }
            towers.add(new CubesModel.Tower("", cubes));
        }
        /*-----------------------------------------------------------------*/

        CubesModel.Cube[] allCubesArr = new CubesModel.Cube[allCubes.size()];
        for (int i = 0; i < allCubesArr.length; i++) {
            allCubesArr[i] = allCubes.get(i);
        }

        return new CubesModel(towers, allCubesArr);
    }

    /*
    public static LXModel importObjModel() {
        return new LXModel(new ObjImporter("data", globalTransform).getModels().toArray(new LXModel[0]));
    }
    */

    private static Map<LX, WeakReference<OfficeLayout>> instanceByLX = new WeakHashMap<>();

    public static OfficeLayout getInstance(LX lx) {
        WeakReference<OfficeLayout> weakRef = instanceByLX.get(lx);
        return weakRef == null ? null : weakRef.get();
    }

    public static void addFadecandyOutput(LX lx) throws Exception {
        lx.engine.addOutput(new FadecandyOutput(lx, "localhost", 7890, lx.model));
//        lx.engine.addOutput(new FadecandyOutput(lx, "192.168.0.113", 1234, lx.model));
    }
    public void setupLx(SLStudioLX lx) {
//        instanceByLX.put(lx, new WeakReference<>(this));

        final NetworkMonitor networkMonitor = NetworkMonitor.getInstance(lx).start();
        final Dispatcher dispatcher = Dispatcher.getInstance(lx);

        /*
        CubesController local_debug = new CubesController(lx, "localhost", "localdebug");
        controllers.add(local_debug);
        lx.addOutput(local_debug);
        */

        networkMonitor.deviceList.addListener(new SetListener<NetworkDevice>() {
            public void onItemAdded(NetworkDevice device) {
                String physicalId = cubePhysicalIdMap.getPhysicalId(device.deviceId);
                final CubesController controller = new CubesController(lx, device, physicalId);
                controller.set16BitColorEnabled(device.featureIds.contains("rgb16"));
                controllers.add(controller);
                dispatcher.dispatchNetwork(() -> lx.addOutput(controller));
                //controller.enabled.setValue(false);
            }

            public void onItemRemoved(NetworkDevice device) {
                final CubesController controller = getControllerByDevice(device);
                controllers.remove(controller);
                dispatcher.dispatchNetwork(() -> {
                    //lx.removeOutput(controller);
                });
            }
        });

        //lx.addOutput(new CubesController(lx, "10.200.1.255"));

        lx.engine.output.enabled.addListener(param -> {
            boolean isEnabled = ((BooleanParameter) param).isOn();
            for (CubesController controller : controllers) {
                controller.enabled.setValue(isEnabled);
            }
        });
    }

    public CubesController getControllerByDevice(NetworkDevice device) {
        for (CubesController controller : controllers) {
            if (controller.networkDevice == device) {
                return controller;
            }
        }
        return null;
    }

    public Collection<CubesController> getSortedControllers() {
        return new TreeSet<CubesController>(controllers);
    }

    public void addControllerSetListener(SetListener<CubesController> listener) {
        controllers.addListener(listener);
    }

    public void setupUi(SLStudioLX lx, SLStudioLX.UI ui) {
        UI2dScrollContext utility = ui.rightPane.utility;
//        new UIOutputs(lx, ui, this, 0, 0, utility.getContentWidth()).addToContainer(utility);
//        new UIMappingPanel(lx, ui, 0, 0, utility.getContentWidth()).addToContainer(utility);
    }
}