package com.symmetrylabs.slstudio.mappings;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import heronarts.lx.LX;
import heronarts.lx.model.LXModel;
import heronarts.lx.transform.LXTransform;

import com.symmetrylabs.slstudio.SLStudio;
import com.symmetrylabs.slstudio.model.CubesModel;
import com.symmetrylabs.slstudio.output.SLController;
import com.symmetrylabs.slstudio.network.NetworkMonitor;
import com.symmetrylabs.slstudio.network.NetworkDevice;
import com.symmetrylabs.slstudio.util.NetworkUtils;
import com.symmetrylabs.slstudio.util.dispatch.Dispatcher;
import com.symmetrylabs.slstudio.util.listenable.ListenableList;
import com.symmetrylabs.slstudio.util.listenable.ListListener;
import com.symmetrylabs.slstudio.objimporter.ObjImporter;

/**
 * This file implements the mapping functions needed to lay out the cubes.
 */
public class CubesLayout {

    static final float globalOffsetX = 0;
    static final float globalOffsetY = 0;
    static final float globalOffsetZ = 0;

    static final float globalRotationX = 0;
    static final float globalRotationY = 0;
    static final float globalRotationZ = 0;

    static final float objOffsetX = 0;
    static final float objOffsetY = 0;
    static final float objOffsetZ = 0;

    static final float objRotationX = 0;
    static final float objRotationY = 0;
    static final float objRotationZ = 0;

    static final float CUBE_WIDTH = 24;
    static final float CUBE_HEIGHT = 24;
    static final float TOWER_WIDTH = 24;
    static final float TOWER_HEIGHT = 24;
    static final float CUBE_SPACING = 3.5f;

    static final float TOWER_VERTICAL_SPACING = 2.5f;
    static final float TOWER_RISER = 14;
    static final float SP = 24;
    static final float JUMP = TOWER_HEIGHT+TOWER_VERTICAL_SPACING;

    static final float INCHES_PER_METER = 39.3701f;

    static final float smallCluster1_OffsetX = -6;
static final float smallCluster1_OffsetY = 0;
static final float smallCluster1_OffsetZ = -175;

static final float smallCluster1_RotationX = 0;
static final float smallCluster1_RotationY = 0;
static final float smallCluster1_RotationZ = 0;

// STAGE RIGHT / HOUSE LEFT
static final TowerConfig[] SMALL_CLUSTER_1 = {
    
        // 500
    new TowerConfig(    0-15,     0,     0,    45, new String[] {"406hp"}),
    new TowerConfig(41-24-15,    25,    -7,     0, new String[] {"341"}), // square
    new TowerConfig(    0-15,    50,     0,    45, new String[] {"354"}),

        // 400
    new TowerConfig(21+9,     0,    27,    45, new String[] {"181"}),
    new TowerConfig(21+17+9, 25,  27-7,     0, new String[] {"18"}), // square
    new TowerConfig(21+9,    50,    27,    45, new String[] {"91"}),

    // 510
    new TowerConfig(43+18,     0,     0,    45, new String[] {"308"}),
    new TowerConfig(43+17+18, 25,    -7,     0, new String[] {"51"}), // square   WRONG
    new TowerConfig(43+18,    50,     0,    45, new String[] {"70"}),



};

static final float smallCluster2_OffsetX = 246;
static final float smallCluster2_OffsetY = 0;
static final float smallCluster2_OffsetZ = -175;

static final float smallCluster2_RotationX = 0;
static final float smallCluster2_RotationY = 0;
static final float smallCluster2_RotationZ = 0;


// STAGE LEFT / HOUSE RIGHT
static final TowerConfig[] SMALL_CLUSTER_2 = {

    // 520
    new TowerConfig(0-18,     0,     0,  45, new String[] {"120"}),
    new TowerConfig(0-7-18,  25,    17,  90, new String[] {"197"}), // square
    new TowerConfig(0-18,    50,     0,  45, new String[] {"69"}),

 //410
    new TowerConfig(21-9,    0,    27,  45, new String[] {"55"}),
    new TowerConfig(21-7-9, 25, 27+17,  90, new String[] {"39"}), // square
    new TowerConfig(21-9,   50,    27,  45, new String[] {"111"}),

    // 530
    new TowerConfig(43,    0,     0,  45, new String[] {"14"}),
    new TowerConfig(43-7, 25,    17,  90, new String[] {"35"}), // square
    new TowerConfig(43,   50,     0,  45, new String[] {"326"}),




 
//389
};

static final TowerConfig[] TOWER_CONFIG = {
//d8:80:39:9b:8:33
//335? 
    // back row (HOUSE LEFT TO HOUSE RIGHT)
 
     new TowerConfig(40+24, 0, 0-24, 45, new String[] {"85", "362", "128", "46"}),  // 100   1 46
    new TowerConfig(80, 0, 0, 45, new String[] {"d8:80:39:9b:23:ad", "314", "198", "132", "56"}),  // 110 2
    new TowerConfig(120, 0, 0, 45, new String[] {"25", "205", "11", "66", "356", "43" }),  // 120 3 
    new TowerConfig(160, 0, 0, 45, new String[] {"105", "113", "123", "337", "384", "389"}),  // 130 4
    new TowerConfig(200, 0, 0, 45, new String[] {"15", "330", "34", "135", "329"  }),  // 140 5
    new TowerConfig(240-24, 0, 0-24, 45, new String[] {"94", "157", "391", "202"}),  // 150 6

    // front row (HOUSE LEFT TO HOUSE RIGHT)
    new TowerConfig(100, 0, -25, 45, new String[] {"174", "9", "d8:80:39:9b:8:33", "40", "31"}),  // 200
    new TowerConfig(140, 0, -25, 45, new String[] {"358", "408", "77", "86", "126"}),  // 210
    new TowerConfig(180, 0, -25, 45, new String[] {"23", "191", "22", "182", "320"}),  // 220

    // front single tower (behind drummer)
    // new TowerConfig(140, 0, -67, 45, new String[] {"321", "323", "185", "70"}),  // 300

    // HOUSE LEFT truss
    new TowerConfig(0, JUMP*3, -144, 0, new String[] {"163"}),  // 601
    new TowerConfig(-26, JUMP*2, -160, 0, new String[] {"412"}),  // 602

    // HOUSE RIGHT truss
    new TowerConfig(296-16-20, JUMP*3, -124, 90, new String[] {"122"}),  // 603
    new TowerConfig(340-12-48, JUMP*2, -140, 90, new String[] {"71"}),  // 604

    // HOUSE LEFT floor
    new TowerConfig(-58-24, 0, -270, 45, new String[] {"318"}),  // 605

    // HOUSE RIGHT floor
    new TowerConfig(319+48, 0, -270, 45, new String[] {"52"}),  // 606

};

    static final StripConfig[] STRIP_CONFIG = {
        // controller-id x y z x-rot y-rot z-rot num-leds pitch-in-inches
        //new StripConfig("206", 0, 0, 0, 0, 0, 0, 10, 0.25),
    };

    static class StripConfig {
        String id;
        int numPoints;
        float spacing;
        float x;
        float y;
        float z;
        float xRot;
        float yRot;
        float zRot;

        StripConfig(String id, float x, float y, float z, float xRot, float yRot, float zRot, int numPoints, float spacing) {
            this.id = id;
            this.numPoints = numPoints;
            this.spacing = spacing;
            this.x = x;
            this.y = y;
            this.z = z;
            this.xRot = xRot;
            this.yRot = yRot;
            this.zRot = zRot;
        }
    }

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
            this.x = x-33.f;
            this.y = y;
            this.z = z;
            this.xRot = xRot;
            this.yRot = yRot-180.f;
            this.zRot = zRot-180.f;
            this.ids = ids;

            this.yValues = new float[ids.length];
            for (int i = 0; i < ids.length; i++) {
                yValues[i] = y + i * (CUBE_HEIGHT + CUBE_SPACING);
            }
        }

    }

    static Map<String, String> macToPhysid = new HashMap<>();
    static Map<String, String> physidToMac = new HashMap<>();

    public static CubesModel buildModel() {

        byte[] bytes = SLStudio.applet.loadBytes("physid_to_mac.json");
        if (bytes != null) {
            try {
                JsonObject json = new Gson().fromJson(new String(bytes), JsonObject.class);
                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    macToPhysid.put(entry.getValue().getAsString(), entry.getKey());
                    physidToMac.put(entry.getKey(), entry.getValue().getAsString());
                }
            }  catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        int stripId = 0;

        // Any global transforms
        LXTransform globalTransform = new LXTransform();
        globalTransform.translate(globalOffsetX, globalOffsetY, globalOffsetZ);
        globalTransform.rotateX(globalRotationX * Math.PI / 180.);
        globalTransform.rotateY(globalRotationY * Math.PI / 180.);
        globalTransform.rotateZ(globalRotationZ * Math.PI / 180.);

        /* Cubes ----------------------------------------------------------*/
        List<CubesModel.Tower> towers = new ArrayList<>();
        List<CubesModel.Cube> allCubes = new ArrayList<>();

        for (TowerConfig config : TOWER_CONFIG) {
          List<CubesModel.Cube> cubes = new ArrayList<CubesModel.Cube>();
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

        globalTransform.push();
        globalTransform.translate(smallCluster1_OffsetX, smallCluster1_OffsetY, smallCluster1_OffsetZ);
        globalTransform.rotateX(smallCluster1_RotationX);
        globalTransform.rotateY(smallCluster1_RotationY);
        globalTransform.rotateZ(smallCluster1_RotationZ);
        for (TowerConfig config : SMALL_CLUSTER_1) {
          List<CubesModel.Cube> cubes = new ArrayList<CubesModel.Cube>();
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
        globalTransform.pop();

        globalTransform.push();
        globalTransform.translate(smallCluster2_OffsetX, smallCluster2_OffsetY, smallCluster2_OffsetZ);
        globalTransform.rotateX(smallCluster2_RotationX);
        globalTransform.rotateY(smallCluster2_RotationY);
        globalTransform.rotateZ(smallCluster2_RotationZ);
        for (TowerConfig config : SMALL_CLUSTER_2) {
          List<CubesModel.Cube> cubes = new ArrayList<CubesModel.Cube>();
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
        globalTransform.pop();
        /*-----------------------------------------------------------------*/

        /* Strips ----------------------------------------------------------*/
        List<CubesModel.CubesStrip> strips = new ArrayList<>();

        for (StripConfig stripConfig : STRIP_CONFIG) {

        }
        /*-----------------------------------------------------------------*/

        CubesModel.Cube[] allCubesArr = new CubesModel.Cube[allCubes.size()];
        for (int i = 0; i < allCubesArr.length; i++) {
            allCubesArr[i] = allCubes.get(i);
        }

        return new CubesModel(towers, allCubesArr, strips);
    }

    /*
    public static LXModel importObjModel() {
        return new LXModel(new ObjImporter("data", globalTransform).getModels().toArray(new LXModel[0]));
    }
    */

    public static ListenableList<SLController> setupCubesOutputs(LX lx) {

        ListenableList<SLController> controllers = new ListenableList<>();

        if (!(lx.model instanceof CubesModel))
            return controllers;

        final NetworkMonitor networkMonitor = NetworkMonitor.getInstance(lx);
        final Dispatcher dispatcher = Dispatcher.getInstance(lx);

        networkMonitor.networkDevices.addListener(new ListListener<NetworkDevice>() {
            public void itemAdded(int index, NetworkDevice device) {
                String macAddr = NetworkUtils.macAddrToString(device.macAddress);
                String physid = macToPhysid.get(macAddr);
                if (physid == null) {
                    physid = macAddr;
                    System.err.println("WARNING: MAC address not in physid_to_mac.json: " + macAddr);
                }
                final SLController controller = new SLController(lx, device, physid);
                controllers.add(index, controller);
                dispatcher.dispatchEngine(new Runnable() {
                    public void run() {
                        lx.addOutput(controller);
                    }
                });
                //controller.enabled.setValue(false);
            }
            public void itemRemoved(int index, NetworkDevice device) {
                final SLController controller = controllers.remove(index);
                dispatcher.dispatchEngine(new Runnable() {
                    public void run() {
                        //lx.removeOutput(controller);
                    }
                });
            }
        });

        //lx.addOutput(new SLController(lx, "10.200.1.255"));
        //lx.addOutput(new LIFXOutput());

        return controllers;
    }
}