/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.game;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Circ;
import com.bruynhuis.galago.control.RaySpatialCollisionControl;
import com.bruynhuis.galago.control.RaySpatialListener;
import com.bruynhuis.galago.control.tween.SpatialAccessor;
import com.bruynhuis.galago.games.basic.BasicGame;
import com.bruynhuis.galago.games.basic.BasicPlayer;
import com.bruynhuis.galago.util.SharedSystem;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author NideBruyn
 */
public class Player extends BasicPlayer implements RaySpatialListener {

    private Spatial model;
    private Spatial trail;
    private Tween tween;
    private float size = 0.5f;
    private boolean forward = true;
    private float speed = 0f;
    private float startSpeed = 10f;
    private Quaternion targetAngle = new Quaternion();
    private float gravity = -10f;
    private float velocity = 0f;
    private float acceleration = 0.1f;
    private boolean onground = true;
    private RaySpatialCollisionControl raySpatialCollisionControl;
    private ShadowControl shadowControl;
    private boolean gameover = false;

    public Player(BasicGame basicGame) {
        super(basicGame);
    }
    
    public void increaseSpeed(float amount) {
        startSpeed = speed + amount;
    }

    @Override
    protected void init() {
        model = game.getBaseApplication().getAssetManager().loadModel("Models/box.j3o");
        playerNode.attachChild(model);
//        model = SpatialUtils.addBox(playerNode, size, size, size);
//        model.move(0, size, 0);
//        SpatialUtils.addColor(model, ColorRGBA.Green, false);

        trail = game.getBaseApplication().getAssetManager().loadModel("Models/effects/player-trail.j3o");
        trail.move(-size, 0, 0);
        
        playerNode.addControl(new AbstractControl() {
            @Override
            protected void controlUpdate(float tpf) {
                if (game.isStarted() && !game.isLoading() && !game.isPaused()) {
                    
                    if (speed < startSpeed) {
                        speed += (tpf*20);
                    }                    
                    
                    if (!onground) {
                        velocity += acceleration;
                    }
                    
                    playerNode.getLocalRotation().slerp(targetAngle, 0.3f);

                    Vector3f direction = playerNode.getLocalRotation().clone().getRotationColumn(0);
                    playerNode.move(direction.x * tpf * speed, velocity*gravity*tpf, direction.z * tpf * speed);
                    
                    onground = false;
                    
                    if (getPosition().y < -1) {
                        doDamage(10);
                        
                    }
                } else if (!game.isStarted() && game.isPaused() && gameover) {
                    //If game over do this
                    if (!onground) {
                        velocity += acceleration;
                    }
                    playerNode.move(0, velocity*gravity*tpf, 0);
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });

        targetAngle = new Quaternion();
        playerNode.setLocalRotation(targetAngle.clone());
        
        String types[] = {Game.TYPE_STATIC};
        
        raySpatialCollisionControl = new RaySpatialCollisionControl(
                game.getLevelNode(), playerNode, new Vector3f(0, -1f, 0), 
                size*2f, types);
        playerNode.addControl(raySpatialCollisionControl);
        raySpatialCollisionControl.addRaySpatialListener(this);
        
        //Add the shadow
        shadowControl = new ShadowControl(((Game)game).getSunLight(), 
                game.getBaseApplication().getAssetManager().loadTexture("Textures/shadow.png"), 0.35f);
        playerNode.addControl(shadowControl);
        
        tween = Tween.to(playerNode, SpatialAccessor.POS_XYZ, 0.2f)
                    .target(0, size*2, 0)
                    .delay(0.2f)
                    .ease(Circ.OUT)
                    .repeatYoyo(9, 0.05f)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> bt) {
                            System.out.println("Tween done");
                            tween.kill();
                            
                        }
                    })
                    .start(SharedSystem.getInstance().getBaseApplication().getTweenManager());
    }
    
    public void jump() {
        velocity = -1f;
    }

    public void turn() {

        forward = !forward;
        if (forward) {
            targetAngle = new Quaternion();
            targetAngle.fromAngleAxis(FastMath.DEG_TO_RAD*0, new Vector3f(0, 1, 0));
        } else {
            targetAngle = new Quaternion();
            targetAngle.fromAngleAxis(FastMath.DEG_TO_RAD*90, new Vector3f(0, 1, 0));
        }

        addScore(1);
        
        if (getScore() % 10 == 0) {
            increaseSpeed(1);
        }
    }

    @Override
    public Vector3f getPosition() {
        return playerNode.getLocalTranslation();
    }

    @Override
    public void doDie() {
        gameover = true;
        shadowControl.dispose();
    }

    @Override
    public void doAction(Vector3f contactPoint, Geometry contactObject, boolean hasCollision) {
//        System.out.println("Collision with: " + contactObject.getName() + "; collision=" + hasCollision);
        onground = true;
    }

    @Override
    public void start() {
//        tween.kill();
        
        playerNode.attachChild(trail);
    }

}
