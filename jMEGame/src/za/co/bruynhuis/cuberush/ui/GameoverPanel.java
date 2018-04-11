/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.ui;

import com.bruynhuis.galago.ui.Label;
import com.bruynhuis.galago.ui.listener.TouchButtonListener;
import com.bruynhuis.galago.ui.panel.Panel;

/**
 *
 * @author NideBruyn
 */
public class GameoverPanel extends Panel {
    
    private Label heading;
    private RetryButton retryButton;
        
    public GameoverPanel(Panel parent) {
        super(parent, null, parent.getWindow().getWidth(), parent.getWindow().getHeight(), false);
        
        heading = new Label(this, "Game Over", 42);
        heading.centerTop(0, 0);
        
        retryButton = new RetryButton(this);
        retryButton.center();
        
        parent.add(this);
    }
    
    public void addRetryButtonListener(TouchButtonListener buttonListener) {
        retryButton.addTouchButtonListener(buttonListener);
    }
    
}
