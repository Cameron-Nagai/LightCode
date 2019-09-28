package com.symmetrylabs.shows.pilots_v2;

import com.google.gson.Gson;
import com.symmetrylabs.shows.HasWorkspace;
import com.symmetrylabs.shows.cubes.*;
import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.workspaces.Workspace;
import com.symmetrylabs.util.EdgeSwitch.EdgeSwitch;
import com.symmetrylabs.util.SLPathsHelper;
import heronarts.lx.transform.LXTransform;
import heronarts.p3lx.ui.UI2dScrollContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * This file implements the mapping functions needed to lay out the cubes.
 */
public class Pilots_v2_Show extends CubesShow implements HasWorkspace {

    public static final String SHOW_NAME = "pilots_v2";

    private Workspace workspace;

    static final float globalOffsetX = 0;
    static final float globalOffsetY = 0;
    static final float globalOffsetZ = 0;

    static final float globalRotationX = 0;
    static final float globalRotationY = 0;
    static final float globalRotationZ = 0;

    static final float CUBE_WIDTH = 24;
    static final float CUBE_HEIGHT = 24;
    static final float TOWER_WIDTH = 24;
    static final float TOWER_HEIGHT = 24;
    static final float CUBE_SPACING = 1.5f;

    static final float TOWER_VERTICAL_SPACING = 0;
    static final float TOWER_RISER = 14;
    static final float SP = 25.5f;
    static final float JUMP = TOWER_HEIGHT+TOWER_VERTICAL_SPACING;

    static final float CART_WIDE = 4;
    static final float CART_FORWARD_OFFSET = 3;
//    static final float FLANK_SPACING = -2.5f;
    static final float FLANK_SPACING = -.5f;
//
    static final float INCHES_PER_METER = 39.3701f;

    static EdgeSwitch[] brains;


    // add hoc read file
    String readFile(String path, Charset encoding)
        throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public class Response {
        ArrayList < String > controllers = new ArrayList < String > ();
    }


//    private String[][] populateFromJSONarray() {
//        return new String[][]{
//            new String[]{response.controllers.get(controllerIndex++),response.controllers.get(controllerIndex++)},
//            new String[]{response.controllers.get(controllerIndex++),response.controllers.get(controllerIndex++)},
//            new String[]{response.controllers.get(controllerIndex++),response.controllers.get(controllerIndex++)},
//            new String[]{response.controllers.get(controllerIndex++),response.controllers.get(controllerIndex++)},
//        };
//    }

//    String[][] towerArray = populateFromJSONarray();

    static final ClusterConfig[] clusters = new ClusterConfig[] {


        /**--------------------------------------------------------------------------------------------------------------------------
         * LEFT FACE
        */

        new ClusterConfig("cart7",SP*6*CART_WIDE + SP*FLANK_SPACING, SP*0, -SP*CART_FORWARD_OFFSET, new TowerConfig[]{

            // col 1

            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"607","606"},
                new String[]{"673","1071"},
                new String[]{"445","444"},
                new String[]{"703","963"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"457","456"},
                new String[]{"525","524"},
                new String[]{"721","720"},
                new String[]{"801","800"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"1003","989"},
                new String[]{"729","728"},
                new String[]{"483","482"},
                new String[]{"651","650"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"523","522"},
                new String[]{"749","748"},
                new String[]{"981","982"},
                new String[]{"689","688"},
            }),
        }),

        new ClusterConfig("cart6",SP*5*CART_WIDE, SP*0, SP*0, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"992","819"},
//                new String[]{"857","856"},
                new String[]{"857","460"},
                new String[]{"465","458"},
                new String[]{"811","810"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"675","674"},
                new String[]{"655","463"},
                new String[]{"892","899"},
                new String[]{"696","5410ecf5d969"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"561","560"},
                new String[]{"5410ecf48db7","842"},
                new String[]{"851","5410ecf48cc9"},
//                new String[]{"5410ecf5efb5","5410ecf4fd2d"},
                new String[]{"697","628"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"779","796"},
                new String[]{"839","838"},
                new String[]{"389","202"},
                new String[]{"397","5410ecf4c80a"},
            }),
        }),

        new ClusterConfig("cart5",SP*4*CART_WIDE, SP*0, SP*0, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"1082","784"},
                new String[]{"563","562"},
                new String[]{"777","776"},
                new String[]{"565","672"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"555","419"},
                new String[]{"625","372"},
                new String[]{"1010","576"},
                new String[]{"681","680"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"5410ecf63691","664"},
                new String[]{"5410ecfd7b9c","1005"},
                new String[]{"627","626"},
                new String[]{"623","622"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"579","578"},
                new String[]{"677","676"},
                new String[]{"815","814"},
                new String[]{"639","638"},
            }),
        }),




        new ClusterConfig("cart4",SP*3*CART_WIDE, SP*0, SP*0, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"708","709"},
                new String[]{"503","502"},
                new String[]{"511","510"},
                new String[]{"665","836"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"878","984"},
                new String[]{"704","705"},
                new String[]{"988","768"},
                new String[]{"619","618"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"731","730"},
//                new String[]{"461","460"},
                new String[]{"461","493"},
                new String[]{"479","478"},
                new String[]{"1080","1061"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"735","734"},
                new String[]{"755","754"},
                new String[]{"715","714"},
                new String[]{"884","880"},
            }),
        }),


        new ClusterConfig("cart3",SP*2*CART_WIDE, SP*0, SP*0, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"513","512"},
                new String[]{"861","860"},
                new String[]{"599","598"},
                new String[]{"717","716"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
//                new String[]{"841","840"},
                new String[]{"837","840"},
                new String[]{"863","862"},
                new String[]{"805","556"},
                new String[]{"993","973"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"621","952"},
                new String[]{"789","788"},
                new String[]{"635","634"},
                new String[]{"203","156"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"753","752"},
                new String[]{"541","540"},
                new String[]{"585","584"},
                new String[]{"671","670"},
            }),
        }),


        new ClusterConfig("cart2",SP*1*CART_WIDE, SP*0, SP*0, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"661","660"},
                new String[]{"1043","758"},
                new String[]{"925","934"},
                new String[]{"985","979"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"543","542"},
                new String[]{"659","658"},
                new String[]{"980","1006"},
                new String[]{"581","580"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"663","662"},
                new String[]{"617","616"},
                new String[]{"?13","?14"},
                new String[]{"909","530"},
//                new String[]{"943","530"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"1092","951"},
                new String[]{"611","610"},
                new String[]{"653","652"},
                new String[]{"592","1000"},
            }),
        }),

//        new String[]{"453","664"},


        new ClusterConfig("cart1",SP*0*CART_WIDE - SP*FLANK_SPACING, SP*0, -SP*CART_FORWARD_OFFSET, new TowerConfig[]{
            // col 1
            new TowerConfig(SP * 0, SP * 0, SP * 0, new String[][]{
                new String[]{"545","439"},
                new String[]{"871","870"},
                new String[]{"799","798"},
                new String[]{"1013","1015"},
            }),
            new TowerConfig(SP * 1, SP * 0, SP * 0, new String[][]{
                new String[]{"469","468"},
                new String[]{"835","834"},
                new String[]{"687","686"},
                new String[]{"922","1059"},
            }),
            new TowerConfig(SP * 2, SP * 0, SP * 0, new String[][]{
                new String[]{"869","868"},
                new String[]{"855","854"},
                new String[]{"569","568"},
                new String[]{"890","879"},
            }),
            new TowerConfig(SP * 3, SP * 0, SP * 0, new String[][]{
                new String[]{"1035","1036"},
                new String[]{"1033","1034"},
                new String[]{"829","828"},
                new String[]{"887","882"},
            }),
        }),
};

    static class ClusterConfig {
        final String id;
        final float x;
        final float y;
        final float z;
        final TowerConfig[] configs;

        ClusterConfig(String id, float x, float y, float z, TowerConfig[] configs) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.configs = configs;
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
        final String[][] ids;
        final float[] yValues;

        TowerConfig(float x, float y, float z, String[][] ids) {
            this(CubesModel.Cube.Type.LARGE, x, y, z, ids);
        }

        TowerConfig(float x, float y, float z, float yRot, String[][] ids) {
            this(x, y, z, 0, yRot, 0, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, String[][] ids) {
            this(type, x, y, z, 0, 0, 0, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, float yRot, String[][] ids) {
            this(type, x, y, z, 0, yRot, 0, ids);
        }

        TowerConfig(float x, float y, float z, float xRot, float yRot, float zRot, String[][] ids) {
            this(CubesModel.Cube.Type.LARGE, x, y, z, xRot, yRot, zRot, ids);
        }

        TowerConfig(CubesModel.Cube.Type type, float x, float y, float z, float xRot, float yRot, float zRot, String[][] ids) {
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

    private void copyMappingsFile(){
        File dir = new File(SLPathsHelper.getMappingsDataDir());

        // create multiple directories at one time
        boolean successful = dir.mkdirs();
        if (successful) {
            // created the directories successfully
            System.out.println("directories were created successfully");
        }
        else {
            // something failed trying to create the directories
            System.out.println("failed trying to create the directories");
        }
        Path originalPath = Paths.get("data/mapping/pilots_v2.json");
        Path copied = Paths.get(SLPathsHelper.getMappingsDataPath());
        try {
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public SLModel buildModel() {
        // Any global transforms
        LXTransform globalTransform = new LXTransform();
        globalTransform.translate(globalOffsetX, globalOffsetY, globalOffsetZ);
        globalTransform.rotateX(globalRotationX * Math.PI / 180.);
        globalTransform.rotateY(globalRotationY * Math.PI / 180.);
        globalTransform.rotateZ(globalRotationZ * Math.PI / 180.);

        List<CubesModel.Tower> towers = new ArrayList<>();
        List<CubesModel.Cube> allCubes = new ArrayList<>();




        // begin read in controller IDs from JSON
        int controllerIndex = 0;
        String jsonStr = null;
        try {
            File checkDir = new File(SLPathsHelper.getMappingsDataDir());
            if (checkDir.isDirectory()){
                System.out.println( checkDir.getAbsolutePath() + " is a directory");
                // good goahead and source it.
                jsonStr = readFile(SLPathsHelper.getMappingsDataPath(), StandardCharsets.UTF_8);
            }
            else {
                System.out.println( checkDir.getAbsolutePath() + " not found.  Creating dir and copying local *.json");
                copyMappingsFile();
                jsonStr = readFile(SLPathsHelper.getMappingsDataPath(), StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Response response = gson.fromJson(jsonStr, Response.class);

        

//        try {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
//                Utils.createInput("data/SummerStageCoordinates.txt")));
//
//            List<CubesModel.Cube> cubes = new ArrayList<>();
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                line = line.replaceAll(" ","").replaceAll("\"","");
//                String[] vals = line.split(",");
//
//                float x = metersToInches(Float.parseFloat(vals[0]));
//                float y = metersToInches(Float.parseFloat(vals[2]));
//                float z = metersToInches(Float.parseFloat(vals[1]));
//
//                CubesModel.DoubleControllerCube cube = new CubesModel.DoubleControllerCube("0", "0", x, y, z, 0, 0, 0, globalTransform);
//                cubes.add(cube);
//                allCubes.add(cube);
//            }
//            towers.add(new CubesModel.Tower("", cubes));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        for (ClusterConfig cluster : clusters) {
            List<CubesModel.Cube> cubes = new ArrayList<>();

            globalTransform.push();
            globalTransform.translate(cluster.x, cluster.y, cluster.z);

//            System.out.print('\n' + cluster.id + '\n');
            System.out.println();

            for (TowerConfig config : cluster.configs) {
                float x = config.x;
                float z = config.z;

                float rX = config.xRot;
                float rY = config.yRot;
                float rZ = config.zRot;

                for (int i = 0; i < config.ids.length; i++) {
//                    String idA = config.ids[i][0];
//                    String idB = config.ids[i][1];
                    // grab the ids from the JSON interchange
                    String idA = response.controllers.get(controllerIndex++);
                    String idB = response.controllers.get(controllerIndex++);
                    float y = config.yValues[i];
                    CubesModel.DoubleControllerCube cube = new CubesModel.DoubleControllerCube(idA, x, y, z, rX, rY, rZ, globalTransform);
                    System.out.print('"' + idA + '"' + ',' + '"' + idB + '"' + ',' + '\t' );
                    cubes.add(cube);
                    allCubes.add(cube);
                }
                System.out.println();
            }
            globalTransform.pop();
            towers.add(new CubesModel.Tower(cluster.id, cubes));
        }

        CubesModel.Cube[] allCubesArr = new CubesModel.Cube[allCubes.size()];
        for (int i = 0; i < allCubesArr.length; i++) {
            allCubesArr[i] = allCubes.get(i);
        }

        CubesModel model = new CubesModel(getShowName(), towers, allCubesArr, cubeInventory, mapping);
        model.setTopologyTolerances(6, 6, 8);
        return model;
    }

    public void setupUi(SLStudioLX lx, SLStudioLX.UI ui) {
//        workspace = new Workspace(lx, ui, "shows/pilots");
        workspace = new Workspace(lx, ui, System.getProperty("user.home") + "/symmetrylabs/software/SLStudio/shows/pilots");
        workspace.setRequestsBeforeSwitch(2);

//        brains = new EdgeSwitch[14];
//
//        try {
//            brains[0] = new EdgeSwitch("10.200.1.242");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            brains[0].retrieve_port_power_output();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        UI2dScrollContext utility = ui.rightPane.utility;
        new UINateMapper(lx, ui, 0, 0, utility.getContentWidth()).addToContainer(utility);
        new UICubesOutputs(lx, ui, this, 0, 0, utility.getContentWidth()).addToContainer(utility);
        new UIBlackList(lx, ui, this, 0, 0, utility.getContentWidth()).addToContainer(utility);
        new UICubesMappingPanel(lx, ui, 0, 0, utility.getContentWidth()).addToContainer(utility);

    }

    @Override
    public String getShowName() {
        return "pilots_v2";
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }
}