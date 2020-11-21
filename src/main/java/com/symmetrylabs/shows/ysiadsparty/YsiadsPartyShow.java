package com.symmetrylabs.shows.ysiadsparty;

import java.util.*;

import com.symmetrylabs.shows.cubes.CubesModel;
import com.symmetrylabs.shows.cubes.CubesShow;
import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.slstudio.model.SLModel;


import heronarts.lx.transform.LXTransform;

import com.symmetrylabs.shows.Show;

/**
 * This file implements the mapping functions needed to lay out the cubes.
 */
public class YsiadsPartyShow extends CubesShow implements Show {
    public static final String SHOW_NAME = "ysiadsparty";

    static final float globalOffsetX = 0;
    static final float globalOffsetY = 0;
    static final float globalOffsetZ = 0;

    static final float globalRotationX = 0;
    static final float globalRotationY = 0;
    static final float globalRotationZ = 0;
    static final float CSP = 32;
    static final float SP = 26;


    static final TowerConfig[] TOWER_CONFIG = {
        //Left Cubes bottom to top
        new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] {"412"}),
        new TowerConfig(CSP*.25f, 0, CSP*.75f, 0, 45, 0, new String[] { "62"}),

        new TowerConfig(CSP*-.25f, SP * 1, CSP*-.25f, 0, 45, 0, new String[] { "188"}),
        new TowerConfig(CSP*-.4f, SP*1, CSP*.75f, 0, 45, 0, new String[] {"76"}),
        new TowerConfig(CSP*.5f, SP*1, SP*1.1f, 0, 45, 0, new String[] {"5410ecf67aeb"}),

        new TowerConfig(CSP*(.5f+.25f), SP*2, SP*(1.1f+.25f), 0, 45, 0, new String[] {"1119"}),

        // //Center Cubes
        new TowerConfig(CSP*(.5f+.25f+.75f), SP*2.8f, SP*(1.1f+.25f+.33f), 0, 45, 0, new String[] {"1"}),
        new TowerConfig(CSP*(.5f+.25f+.75f-.75f), SP*(3.8f-1f), SP*(1.1f+.25f+.33f), 90, 90, 90, new String[] {"128"}),

        // //Right Cubes top to bottom
        new TowerConfig(CSP*(.5f+.25f+.75f+.75f), SP*2, SP*(1.1f+.25f), 0, 45, 0, new String[] {"205"}),

        new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f), SP*1, SP*1.1f, 0, 45, 0, new String[] {"69"}),
        new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f), SP*1, CSP*(.75f), 0, 45, 0, new String[] {"61"}),
        new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f-.15f), SP*1, CSP*-.25f, 0, 45, 0, new String[] {"38"}),

        new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f-.15f-.5f), SP*0, CSP*.75f, 0, 45, 0, new String[] {"31"}),
        new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f-.15f-.5f+.25f), SP*0, 0, 0, 0, 0, new String[] {"123"}),


        // new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f+.15f-1f), SP*0, CSP*.75f, 0, 45, 0, new String[] {"0"}),
        // new TowerConfig(CSP*(.5f+.25f+.75f+.75f+.25f+.9f+.15f-1f-.25f), SP*0, 0, 0, 45, 0, new String[] {"0"}),

        // new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] { "521"}),
        // new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] { "521"}),
        // new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] { "521"}),
        // new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] { "521"}),
        // new TowerConfig(CSP*0, 0, 0, 0, 45, 0, new String[] { "521"}),

        // //back towers going to the right
        // new TowerConfig(CSP*.5f, 0, CSP*-.5f, 0, 45, 0, new String[] { "29", "141", "32", "34"}),
        // new TowerConfig(CSP*1, 0, CSP*-1, 0, 45, 0, new String[] { "86", "68", "174",}),
        // new TowerConfig(CSP*1.5f, 0, CSP*-1.5f, 0, 45, 0, new String[] { "43", "25"}),
        // //back towers going to the left
        // new TowerConfig(CSP*-.5f, 0, CSP*-.5f, 0, 45, 0, new String[] { "128", "113", "63", "51"}),
        // new TowerConfig(CSP*-1, 0, CSP*-1, 0, 45, 0, new String[] { "132", "22", "1151",}),
        // //two towers in the middle going to the right
        // new TowerConfig(CSP*0, 0, CSP*-1, 0, 45, 0, new String[] { "1117", "172", "211",}),
        // new TowerConfig(CSP*.5f, 0, CSP*-1.5f, 0, 45, 0, new String[] { "314", "408"}),
    };

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

        CubesModel m = new CubesModel(towers, allCubesArr);
        m.setTopologyTolerances(2, 6, 8);
        return m;
    }

    public void setupUi(final SLStudioLX lx, SLStudioLX.UI ui) {
        super.setupUi(lx, ui);

    
    }
}
