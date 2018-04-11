/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Bounce;
import com.bruynhuis.galago.games.basic.BasicGameListener;
import com.bruynhuis.galago.screen.AbstractScreen;
import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.TextAlign;
import com.bruynhuis.galago.ui.button.ControlButton;
import com.bruynhuis.galago.ui.listener.TouchButtonAdapter;
import com.bruynhuis.galago.ui.tween.WidgetAccessor;
import com.bruynhuis.galago.util.Timer;
import com.jme3.math.Vector3f;
import za.co.bruynhuis.cuberush.MainApplication;
import za.co.bruynhuis.cuberush.game.Game;
import za.co.bruynhuis.cuberush.game.Player;
import za.co.bruynhuis.cuberush.ui.GameoverPanel;
import za.co.bruynhuis.cuberush.ui.MenuPanel;

/**
 *
 * @author NideBruyn
 */
public class PlayScreen extends AbstractScreen implements BasicGameListener {

    private MainApplication mainApplication;
    private Label countDownLabel;
    private ControlButton controlButton;
    private Label scoreLabel;
    private MenuPanel menuPanel;
    private GameoverPanel gameoverPanel;

    private Game game;
    private Player player;
    private Timer startTimer = new Timer(100);
    private int startCounter = 0;
    private float cameraDistance = 24;

    @Override
    protected void init() {
        mainApplication = (MainApplication) baseApplication;

        countDownLabel = new Label(hudPanel, "Ready?", 36);
        countDownLabel.center();
        countDownLabel.setAlignment(TextAlign.CENTER);
        
        scoreLabel = new Label(hudPanel, "0", 36);
        scoreLabel.centerTop(0, 10);
        scoreLabel.setAlignment(TextAlign.CENTER);              
        
        controlButton = new ControlButton(hudPanel, "controller", window.getWidth(), window.getHeight());
        controlButton.center();
        controlButton.addTouchButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive() && game.isStarted() && !game.isPaused()) {
                    player.turn();
                }
            }

        });
        
        menuPanel = new MenuPanel(hudPanel);
        menuPanel.addPlayButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    showCountdownState();
                    
                }
            }
            
        });       
        
        gameoverPanel = new GameoverPanel(hudPanel);
        gameoverPanel.addRetryButtonListener(new TouchButtonAdapter() {
            @Override
            public void doTouchUp(float touchX, float touchY, float tpf, String uid) {
                if (isActive()) {
                    mainApplication.addRetryCount();
                    showScreen("play");
                    
                }
            }
            
        });       
    }
    
    private void showMenuState() {

        menuPanel.show();
        gameoverPanel.hide();
        scoreLabel.hide();
        countDownLabel.hide();
        controlButton.hide();
    }
    
    private void showCountdownState() {
        startCounter = 3;        
        menuPanel.hide();
        gameoverPanel.hide();
        countDownLabel.show();
        controlButton.hide();
        scoreLabel.hide();
        startTimer.start();
        updateCounterDown();
        
    }
    
    private void showGameplayState() {
        menuPanel.hide();
        gameoverPanel.hide();
        countDownLabel.hide();
        scoreLabel.setText("0");
        scoreLabel.show();
        controlButton.show();
        
    }
    
    private void showGameoverState() {
        menuPanel.hide();
        gameoverPanel.show();
        scoreLabel.hide();
        countDownLabel.hide();
        controlButton.hide();
    }
    
    private void showPauseState() {
        
    }

    @Override
    protected void load() {
        game = new Game(mainApplication, rootNode);
        game.load();

        player = new Player(game);
        player.load();

        game.addGameListener(this);

        camera.setLocation(new Vector3f(cameraDistance, cameraDistance, -cameraDistance));
        camera.lookAt(player.getPosition().clone(), Vector3f.UNIT_Y);


    }

    @Override
    protected void show() {
        setPreviousScreen(null);
        
        if (mainApplication.getRetryCount() == 0) {
            showMenuState();
        } else {
            showCountdownState();
        }

    }

    @Override
    protected void exit() {
        startTimer.stop();
        game.close();
    }

    @Override
    protected void pause() {
    }

    private void updateCounterDown() {
        countDownLabel.setText("Ready?\n" + startCounter);
        countDownLabel.setScale(0f);
        Tween.to(countDownLabel, WidgetAccessor.SCALE_XY, 0.5f)
                .target(1, 1)
                .ease(Bounce.INOUT)
                .start(mainApplication.getTweenManager());
    }

    @Override
    public void update(float tpf) {
        if (isActive()) {

            //First we do the count down start timer
            if (!game.isStarted()) {
                startTimer.update(tpf);

                if (startTimer.finished()) {
                    startCounter--;
                    if (startCounter == 0) {
                        startTimer.stop();
                        showGameplayState();
                        game.start(player);
                    } else {
                        startTimer.reset();
                        updateCounterDown();
                    }

                }

            }

            //Do some logic when game is active
            if (game.isStarted() && !game.isPaused()) {
                camera.setLocation(
                        camera.getLocation().interpolateLocal(
                                new Vector3f(player.getPosition().x + cameraDistance,
                                        cameraDistance,
                                        player.getPosition().z - cameraDistance), 0.2f));
                camera.lookAt(player.getPosition().clone(), Vector3f.UNIT_Y);
            }

        }

    }

    @Override
    public void doGameOver() {
        showGameoverState();
    }

    @Override
    public void doGameCompleted() {
    }

    @Override
    public void doScoreChanged(int score) {
        scoreLabel.setText("" + score);
    }

}
