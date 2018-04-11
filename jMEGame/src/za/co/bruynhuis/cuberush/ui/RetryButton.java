/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.ui;

import com.bruynhuis.galago.ui.button.TouchButton;
import com.bruynhuis.galago.ui.effect.TouchEffect;
import com.bruynhuis.galago.ui.panel.Panel;

/**
 *
 * @author NideBruyn
 */
public class RetryButton extends TouchButton {
    
    public RetryButton(Panel panel) {
        super(panel, "retry-button", "Interface/button-play.png", 300, 60, true);
        setText("RETRY");
        setFontSize(32);
        setTransparency(0.8f);
        addEffect(new TouchEffect(this));
    }
    
}
