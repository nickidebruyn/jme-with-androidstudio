/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.game;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author NideBruyn
 */
public class CubeControl extends AbstractControl {

    private Game game;
    private float disposeDistance = 80;

    public CubeControl(Game game) {
        this.game = game;
    }

    @Override
    protected void controlUpdate(float tpf) {

        if (game.isStarted() && !game.isPaused()) {

            if (spatial.getWorldTranslation().x < game.getPlayer().getPosition().x) {
                if (spatial.getWorldTranslation().distance(game.getPlayer().getPosition()) > disposeDistance) {
                    game.disposeCube(spatial);
                }
            }

        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

}
