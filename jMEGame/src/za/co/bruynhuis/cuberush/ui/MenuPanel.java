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
public class MenuPanel extends Panel {
    
    private Label heading;
    private PlayButton playButton;
        
    public MenuPanel(Panel parent) {
        super(parent, null, parent.getWindow().getWidth(), parent.getWindow().getHeight(), false);
        
        heading = new Label(this, "Zig Zag Dash", 42);
        heading.centerTop(0, 0);
        
        playButton = new PlayButton(this);
        playButton.centerAt(0, -100);
        
        parent.add(this);
    }
    
    public void addPlayButtonListener(TouchButtonListener buttonListener) {
        playButton.addTouchButtonListener(buttonListener);
    }
    
}
