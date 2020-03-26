package com.symmetrylabs.shows.area15;

import com.symmetrylabs.controllers.symmeTreeController.infrastructure.AllPortsPowerEnableMask;
import com.symmetrylabs.shows.HasWorkspace;
import com.symmetrylabs.shows.base.SLShow;
import com.symmetrylabs.shows.treeV2.BranchConfig;
import com.symmetrylabs.shows.treeV2.LimbConfig;
import com.symmetrylabs.shows.treeV2.TreeConfig;
import com.symmetrylabs.shows.treeV2.TreeModel_v2;
import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.util.NetworkChannelDebugMonitor.DebugPortMonitor;
import com.symmetrylabs.util.NetworkChannelDebugMonitor.MachinePortMonitor;
import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.transform.LXMatrix;


public class Area15Show extends SLShow {
    public static final String SHOW_NAME = "area15";

    final LimbConfig[] LIMB_CONFIGS = new LimbConfig[] {
        new LimbConfig(
            new BranchConfig[] {
                new BranchConfig("L0-B1", "C444", LXMatrix.createFromColumnMajor(new double[] {0.922528556949325,3.02752656551013E-02,-0.38473948316037,0,0.276798061139382,0.642787609686538,0.714287702668152,0,0.268931022685139,-0.765445946571657,0.584609791155216,0,0.15223893362666,2.18752578394953,0.392858236467483,1})),
                new BranchConfig("L0-B2", "C453", LXMatrix.createFromColumnMajor(new double[] {-0.835128738876087,0.057408946882132,0.547050456832934,0,-0.352807441999435,0.707106781186551,-0.612802503968293,0,-0.422003434077322,-0.704772454638998,-0.570270890725687,0,-0.229324837299633,1.93882045962119,-0.398321627579391,1})),
            }
        ),
        new LimbConfig(   
            new BranchConfig[] {
                new BranchConfig("L1-0-B1", "", LXMatrix.createFromColumnMajor(new double[] {-0.52518278333969,-0.391047709579566,-0.755820569259763,0,1.54570566182895E-02,0.883636791950601,-0.467917833932358,0,0.850849060341654,-0.257425151731191,-0.458026383270547,0,1.80273608633567,3.07502969929011,-0.525067881613801,1})),
                new BranchConfig("L1-0-B2", "C505", LXMatrix.createFromColumnMajor(new double[] {-0.207282287116043,-9.69478776174533E-02,-0.973465542519823,0,0.976622228938158,0.037418870811032,-0.211681010133385,0,5.69480060388908E-02,-0.994585811849686,8.69251832065706E-02,0,2.48798337712165,2.80026958975743,-0.60014599399408,1})),
                new BranchConfig("L1-0-B3", "C522", LXMatrix.createFromColumnMajor(new double[] {-0.879725323312641,-7.32629503588613E-02,-0.469804103459287,0,0.465960092468686,6.39142152172386E-02,-0.882494286281639,0,9.46812956662691E-02,-0.99526253481073,-2.20893425225152E-02,0,2.51767184815679,3.07771327108692,-1.21188444171154,1})),
                new BranchConfig("L1-0-B4", "C477", LXMatrix.createFromColumnMajor(new double[] {-0.330262083950523,2.75745453851888E-02,-0.943486407083567,0,0.766619108525076,0.590977715082152,-0.251078638518884,0,0.550656061756226,-0.806216462696637,-0.216316705152528,0,2.87115449743373,3.54433183625622,-1.11262473155846,1})),
                new BranchConfig("L1-0-B5", "C431", LXMatrix.createFromColumnMajor(new double[] {-0.631085127421415,-0.320825831379706,-0.70625940550694,0,-0.014047351134854,0.915041450051984,-0.403115140639567,0,0.775586380665359,-0.244478896034573,-0.581975803208455,0,2.52157567713316,3.86703952114533,-1.25466977591313,1})),
            },
            new LimbConfig[] {
                new LimbConfig(
                    new BranchConfig[] {   
                        new BranchConfig("L1-1-B1", "C437", LXMatrix.createFromColumnMajor(new double[] {0.763393010627055,-0.111070537152004,-0.636313167475361,0,0.644925751496875,7.60353693229754E-02,0.760453415843541,0,-3.60816626827567E-02,-0.990899570283833,0.129677119143524,0,2.29587980766597,3.10099464983712,0.597977216791313,1})),
                        new BranchConfig("L1-1-B2", "C507", LXMatrix.createFromColumnMajor(new double[] {0.391336173046384,-0.190071759563836,-0.900404756696519,0,0.899813792727426,0.284077815866576,0.331111662358729,0,0.192850040401592,-0.939772589911402,0.282199116172112,0,2.62334388750787,3.54408352747429,0.475164296629273,1})),
                        new BranchConfig("L1-1-B3", "C474", LXMatrix.createFromColumnMajor(new double[] {0.529448742527759,-0.094716044857397,-0.843037899434151,0,0.214085080272854,0.976501671356216,0.024739932155977,0,0.820884649290228,-0.193580362343622,0.537284874042227,0,2.42662719081679,4.17803085439409,0.429183982803889,1})),
                        new BranchConfig("L1-1-B4", "C455", LXMatrix.createFromColumnMajor(new double[] {0.706965280784555,0.117674626795017,-0.697389972665128,0,0.427434190265284,0.714514674149653,0.55386712613864,0,0.563471476436453,-0.689653146613186,0.454838908415997,0,2.6049752011687,4.07300303972433,0.759087919989664,1})),
                    }
                )
            }
        ),
        new LimbConfig( 
            new BranchConfig[] { 
                new BranchConfig("L2-0-B1", "C436", LXMatrix.createFromColumnMajor(new double[] {0.552603208134421,0.335375458642499,0.762989512444227,0,-0.80814851608582,-8.21594787503982E-03,0.588921449897014,0,0.20377848342706,-0.94204872482311,0.266493020079925,0,-1.88831462293666,3.15379929959741,0.373160468725274,1})),
                new BranchConfig("L2-0-B2", "C426", LXMatrix.createFromColumnMajor(new double[] {-0.242325684618102,0.126935153545163,0.961855357820933,0,-0.969548395850708,4.50498040493782E-03,-0.244858353451278,0,-3.54142722315216E-02,-0.991900787350236,0.121978102035958,0,-2.39388280894539,3.4177392885757,2.11472328126613E-02,1})),
                new BranchConfig("L2-0-B3", "C421", LXMatrix.createFromColumnMajor(new double[] {-0.188771872226604,-6.98172343100241E-02,0.979535978945828,0,-0.104099683845926,0.993271992713272,5.07346460969582E-02,0,-0.976487806416056,-9.23921115934934E-02,-0.194769765713404,0,-2.07698636364518,4.5685764901734,0.213235471442662,1})),
                new BranchConfig("L2-0-B4", "C429", LXMatrix.createFromColumnMajor(new double[] {4.37032892680475E-02,-0.250768601962842,0.967060045073084,0,-0.542686131953844,0.806761233169116,0.233726495803156,0,-0.838797821105973,-0.535024691883515,-0.100830523071251,0,-2.45249850701777,4.22041772424774,0.291562117182284,1})),
                new BranchConfig("L2-0-B5", "C492", LXMatrix.createFromColumnMajor(new double[] {-0.316240260124243,-0.118337350208479,0.941269551946831,0,-0.915595483377226,0.297810727943662,-0.270173427895657,0,-0.248348562902577,-0.947261865519404,-0.202528885438406,0,-2.79377171144063,3.99603672443299,0.121333064591142,1})),
            },
            new LimbConfig[] {
                new LimbConfig(
                    new BranchConfig[] { 
                        new BranchConfig("L2-1-B1", "C475", LXMatrix.createFromColumnMajor(new double[] {0.909693149251636,-0.304028134225146,0.282887376537409,0,0.260768658897897,0.948347807124547,0.180654762623784,0,-0.323199753621864,-9.05722381397319E-02,0.941986512078133,0,-0.854269563348222,3.49046671662208,0.708309914960226,1})),
                        new BranchConfig("L2-1-B2", "C460", LXMatrix.createFromColumnMajor(new double[] {0.91010516939746,0.147069030808917,0.38740067218959,0,-0.26948545520494,0.920256738480228,0.28373425016152,0,-0.314779557895691,-0.362626854298823,0.877163379577792,0,-1.21936455194575,3.67664873942784,1.09649208110921,1})),
                        new BranchConfig("L2-1-B3", "C479", LXMatrix.createFromColumnMajor(new double[] {0.974973965146754,-0.211895973083779,6.72745410753122E-02,0,0.081251544754407,0.621290596600548,0.779356260673357,0,-0.206939492995025,-0.754385903345638,0.622959031616324,0,-1.08239343469479,3.78718608906152,1.6707100626752,1})),
                        new BranchConfig("L2-1-B4", "C425", LXMatrix.createFromColumnMajor(new double[] {0.956427178782059,4.94869933379478E-03,0.291929036003435,0,-0.289606436100489,0.143071118652472,0.946392501648604,0,-3.70832018057324E-02,-0.989700038083476,0.13827028155537,0,-1.27760709777577,3.45392539588657,1.72075402887018,1})),
                    }
                ),
                new LimbConfig(
                    new BranchConfig[] { 
                        new BranchConfig("L2-2-B1", "C469", LXMatrix.createFromColumnMajor(new double[] {-0.841987248555583,-0.135075351485628,0.522314199204687,0,-0.27924259010561,0.937487169279313,-0.207705039196862,0,-0.461607028911941,-0.32073736429935,-0.827071033225923,0,-1.36617371905326,3.45420520523193,-0.71599755576219,1})),
                        new BranchConfig("L2-2-B2", "C434", LXMatrix.createFromColumnMajor(new double[] {-0.903006675657317,0.279831396752683,0.325995909651891,0,1.09077392714163E-02,0.773477242339136,-0.633730208217529,0,-0.429488026541263,-0.568706730200099,-0.701507441216987,0,-1.38910790425917,3.48417752220993,-1.27921969115382,1})),
                        new BranchConfig("L2-2-B3", "C381", LXMatrix.createFromColumnMajor(new double[] {-0.727977454673807,4.86613397722432E-03,0.685583799565568,0,-0.599374921450568,0.480977710212434,-0.639851659226519,0,-0.332864129973132,-0.876719318264928,-0.347224290564917,0,-1.89533941444489,3.52287615404409,-1.60005914784202,1})),
                        new BranchConfig("L2-2-B4", "C452", LXMatrix.createFromColumnMajor(new double[] {-0.870565947346618,0.170270215099418,0.461652450627616,0,-0.480254781886361,-8.98175708756201E-02,-0.872518279715266,0,-0.107099373461143,-0.981295499741032,0.159965203692021,0,-1.80791966002935,3.15069013867728,-1.6742940380539,1})),
                    }
                )
            }
        ),
        new LimbConfig( 
            new BranchConfig[] { 
                new BranchConfig("L3-B1", "C402", LXMatrix.createFromColumnMajor(new double[] {-0.936519779404926,-7.67552851858847E-02,-0.3421101123606,0,0.350210808348787,-0.157959115722707,-0.923255819085799,0,1.68253528651819E-02,-0.984457995018647,0.174812366681617,0,0.241906172762233,2.86254224655027,-1.36186919746786,1})),
                new BranchConfig("L3-B2", "C473", LXMatrix.createFromColumnMajor(new double[] {-0.991621555636374,-0.054519882504219,0.117107953653984,0,-0.122022793127595,9.78279353989122E-02,-0.987694352020364,0,4.23925506974702E-02,-0.99370884944601,-0.103660957829117,0,2.58930681114268E-02,3.27674821320217,-1.6787528452635,1})),
                new BranchConfig("L3-B3", "C438", LXMatrix.createFromColumnMajor(new double[] {-0.935612876045637,0.164789285555694,-0.312206722451097,0,0.350905825232964,0.530924802726394,-0.771352030960905,0,3.86477424332217E-02,-0.831242049715956,-0.554565601880275,0,0.34060140206212,3.73667606482729,-1.90265219501741,1})),
                new BranchConfig("L3-B4", "C449", LXMatrix.createFromColumnMajor(new double[] {-0.998795130083472,9.49797865910519E-03,-4.81464071653598E-02,0,0.027850261459639,0.917502308212243,-0.396754177497649,0,4.04060769955605E-02,-0.397617030352823,-0.916661358471727,0,0.154729653554086,3.92668001521752,-1.61800213904668,1})),
            }
        ),
        new LimbConfig( 
            new BranchConfig[] { 
                new BranchConfig("L4-B1", "C458", LXMatrix.createFromColumnMajor(new double[] {0.978664217219926,-0.192007359128719,-7.31404400706204E-02,0,6.93093836058017E-02,-2.66030980157949E-02,0.997240434659639,0,-0.193423264571491,-0.981032848184187,-1.27275883862635E-02,0,0.421192110444882,3.47565972318314,1.25884545878931,1})),
                new BranchConfig("L4-B2", "C443", LXMatrix.createFromColumnMajor(new double[] {0.831665087756584,-0.215907376353843,-0.511583020283935,0,0.546772721620112,0.157790405605595,0.822278407104891,0,-9.68130812388964E-02,-0.963579883940518,0.249281035311973,0,0.794947412762503,3.87601977927349,1.39545545076643,1})),
                new BranchConfig("L4-B3", "C509", LXMatrix.createFromColumnMajor(new double[] {0.916260867990518,-0.140545549876331,-0.375117275260981,0,0.242443577500101,0.94001196598087,0.23999711569461,0,0.31888420079743,-0.310844739737927,0.895370545784842,0,0.721111358719983,4.53092858450738,1.30411238712264,1})),
                new BranchConfig("L4-B4", "C461", LXMatrix.createFromColumnMajor(new double[] {0.990120951298729,4.35577999888571E-02,-0.1332787299588,0,0.074857264393673,0.639523621631216,0.76511824402715,0,0.118561763510821,-0.767536484758753,0.629945118877335,0,0.657975929878766,4.39557744936272,1.66379203661864,1})),
            }
        ),
        new LimbConfig( 
            new BranchConfig[] { 
                new BranchConfig("L5-0-B1", "", LXMatrix.createFromColumnMajor(new double[] {0.762139927609156,-8.56876834707189E-02,0.641716722273413,0,-0.440023403339065,0.658510240430242,0.610527368561321,0,-0.474891708979866,-0.747677660593071,0.464172360866366,0,-0.536478466500362,5.07089558922976,0.595221815757456,1})),
                new BranchConfig("L5-0-B2", "", LXMatrix.createFromColumnMajor(new double[] {-0.418803634945737,0.356367655716278,-0.83522787867475,0,0.325811495887345,0.917505207190358,0.228103187014628,0,0.847614525892453,-0.1765964006934,-0.50036319484775,0,-8.73948707238788E-02,5.5385746916711,0.539809513119723,1})),
                new BranchConfig("L5-0-B3", "", LXMatrix.createFromColumnMajor(new double[] {0.529012418929928,-0.300175500143707,0.793750924239703,0,-0.213645879220466,0.858111923047781,0.466904022059517,0,-0.821280280361108,-0.416579640209066,0.389820605475473,0,-0.433054925575709,5.76259763564075,0.760297685560432,1})),
            },
            new LimbConfig[] {
                new LimbConfig(
                    new BranchConfig[] {   
                        new BranchConfig("L5-1-B1", "", LXMatrix.createFromColumnMajor(new double[] {-0.018984794530949,-8.21580845499794E-02,-0.996438471115852,0,0.980466214366806,0.193612459164931,-3.46441646072088E-02,0,0.195769201004247,-0.977631967971172,7.68775333824684E-02,0,0.957455737229752,4.49238984225491,-0.278169553532471,1})),
                        new BranchConfig("L5-1-B2", "C420", LXMatrix.createFromColumnMajor(new double[] {-0.443758466090596,-0.216028248121801,-0.869718471567867,0,0.775532246167428,0.393714961727523,-0.493495961550243,0,0.449030242775946,-0.89348773070777,-7.17747499160873E-03,0,1.00930369687253,4.95516100130785,-0.597227751767327,1})),
                        new BranchConfig("L5-1-B3", "C385", LXMatrix.createFromColumnMajor(new double[] {-0.325669207163392,-0.103752206225579,-0.939773934097394,0,3.55582134707351E-02,0.991913735347873,-0.121830846188476,0,0.944814892431495,-7.30932372491136E-02,-0.319346516668191,0,0.778768838211902,5.56189681825391,-0.450850879610501,1})),
                        new BranchConfig("L5-1-B4", "C540", LXMatrix.createFromColumnMajor(new double[] {-0.128114810750237,0.13495052158291,-0.982534962223193,0,0.610945861514371,0.791141039477963,2.90001715889773E-02,0,0.781237319618625,-0.596560317468593,-0.18380434721975,0,1.15835802804016,5.49901859216737,-0.390569555212263,1})),
                    }
                ),
                new LimbConfig(
                    new BranchConfig[] {
                        new BranchConfig("L5-2-B1", "C533", LXMatrix.createFromColumnMajor(new double[] {-0.629744791240301,-0.011358572109692,0.776719048784912,0,5.64290198294007E-02,0.996582485785577,6.03250756032216E-02,0,-0.774749807116338,8.18188967480632E-02,-0.626951676373966,0,-0.404138760176883,5.17316808637914,-0.336751693837918,1})),
                        new BranchConfig("L5-2-B2", "C375", LXMatrix.createFromColumnMajor(new double[] {-0.581537941263835,0.439818832468418,0.684377832397235,0,0.118227320559748,0.878015980088329,-0.463799783724394,0,-0.804882552639752,-0.188805014010883,-0.562598207551772,0,-0.581595580496308,5.3654983194073,-0.836913316532668,1})),
                        new BranchConfig("L5-2-B3", "C469", LXMatrix.createFromColumnMajor(new double[] {-0.554872300406912,0.292156902913169,0.778948698131865,0,-0.715412409900135,0.310325614940848,-0.626005668083337,0,-0.424619610948461,-0.904622770436257,3.68215861446524E-02,0,-1.1942962822805,5.30138627651001,-1.08828626120646,1})),
                        new BranchConfig("L5-2-B4", "C459", LXMatrix.createFromColumnMajor(new double[] {-0.427232805696423,3.51724411732025E-02,0.90345726468864,0,-0.528881407458609,0.800730500481259,-0.281274105533435,0,-0.733318884647984,-0.597991275004034,-0.323496288135871,0,-1.10442252932715,5.64588079219933,-0.930420179949186,1})),
                    }
                )
            }
        )
    };

    public TreeModel_v2 buildModel() {
        return new TreeModel_v2(SHOW_NAME, new TreeConfig(LIMB_CONFIGS));
    }

    public void setupLx(final LX lx) {
        super.setupLx(lx);
        TreeModel_v2 tree = (TreeModel_v2) (lx.model);

        // lx.engine.addLoopTask(new LXLoopTask() {
        //     @Override
        //     public void loop(double v) {
        //         if (lx.engine.framesPerSecond.getValuef() != 35) {
        //             lx.engine.framesPerSecond.setValue(35);
        //         }
        //     }
        // });

        //lx.engine.output.enabled.setValue(false);
    }

    public void setupUi(SLStudioLX lx, SLStudioLX.UI ui) {
        super.setupUi(lx, ui);
    }

    public void setupUi(LX lx){
        super.setupUi(lx);
    }

    @Override
    public String getShowName() {
        return SHOW_NAME;
    }
}