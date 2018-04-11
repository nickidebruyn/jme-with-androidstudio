/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.game;

import com.bruynhuis.galago.app.Base3DApplication;
import com.bruynhuis.galago.control.RaySpatialCollisionControl;
import com.bruynhuis.galago.games.basic.BasicGame;
import com.bruynhuis.galago.util.ColorUtils;
import com.bruynhuis.galago.util.SpatialUtils;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author NideBruyn
 */
public class Game extends BasicGame {
    
    public static final String TYPE_STATIC = "static";

    private static final int CUBE_X = 0;
    private static final int CUBE_Z = 1;

    private Vector3f sun = new Vector3f(-0.5f, -0.85f, 0.25f);
    private Vector3f lastPos;
    private float cubeSize = 2f;
    private int cubeType = 0;
    private int cubeRepeat = 0;
    
//    private FilterPostProcessor fpp;

    public Game(Base3DApplication baseApplication, Node rootNode) {
        super(baseApplication, rootNode);
    }

    public DirectionalLight getSunLight() {
        return sunLight;
    }

    @Override
    public void init() {
        sunLight = SpatialUtils.addSunLight(levelNode, ColorRGBA.White);
        sunLight.setDirection(sun.normalizeLocal());
        
//        Spatial floor = SpatialUtils.addBox(levelNode, 10, 0.01f, 10);
//        SpatialUtils.addColor(floor, ColorRGBA.Gray, false);
        
        addCube(new Vector3f(0, 0, 0));
        addCube(new Vector3f(cubeSize*2f, 0, 0));

        for (int i = 0; i < 33; i++) {
            addCube(null);
        }

//        fpp = new FilterPostProcessor(baseApplication.getAssetManager());
//        baseApplication.getViewPort().addProcessor(fpp);
//        
//        fpp.addFilter(new FXAAFilter());
//        fpp.addFilter(new VignetteFilter());
    }

    private void addCube(Vector3f wantedPos) {
        Vector3f pos = null;
        float height = -cubeSize * 5f;

        if (wantedPos == null) {
            if (lastPos == null) {
                pos = new Vector3f(0, height, 0);

            } else {
                int i = FastMath.nextRandomInt(0, 1);

                //Eliminate too many repeats
                if (cubeType == i) {
                    cubeRepeat++;
                } else {
                    cubeRepeat = 0;
                }

                if (cubeRepeat >= 3) {
                    if (i == 0) {
                        i = 1;
                    } else if (i == 1) {
                        i = 0;
                    }
                }

                cubeType = i;

                if (cubeType == 0) {
                    pos = new Vector3f(lastPos.x + (cubeSize * 2f), height, lastPos.z);
                } else if (cubeType == 1) {
                    pos = new Vector3f(lastPos.x, height, lastPos.z - (cubeSize * 2f));
                }

            }
        } else {
            pos = new Vector3f(wantedPos.x, height, wantedPos.z);
        }

        Spatial lastCube = SpatialUtils.addBox(levelNode, cubeSize, cubeSize * 5f, cubeSize);
        lastCube.setName(TYPE_STATIC);
        lastCube.setUserData(RaySpatialCollisionControl.TYPE, TYPE_STATIC);
        SpatialUtils.addColor(lastCube, ColorUtils.rgb(66, 130, 195), false);
        lastCube.setLocalTranslation(pos);
        lastCube.addControl(new CubeControl(this));

        lastPos = pos;

    }

    public void disposeCube(Spatial spatial) {
        spatial.removeFromParent();
        addCube(null);

//        System.out.println("Dispose spatial, count=" + levelNode.getQuantity());
    }

    @Override
    public void close() {
        super.close(); //To change body of generated methods, choose Tools | Templates.
//        baseApplication.getViewPort().removeProcessor(fpp);
    }

}
