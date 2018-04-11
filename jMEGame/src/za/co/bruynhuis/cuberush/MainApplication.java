package za.co.bruynhuis.cuberush;

import com.bruynhuis.galago.app.Base3DApplication;
import com.bruynhuis.galago.resource.EffectManager;
import com.bruynhuis.galago.resource.FontManager;
import com.bruynhuis.galago.resource.ModelManager;
import com.bruynhuis.galago.resource.ScreenManager;
import com.bruynhuis.galago.resource.SoundManager;
import com.bruynhuis.galago.resource.TextureManager;
import com.bruynhuis.galago.util.ColorUtils;
import za.co.bruynhuis.cuberush.screens.PlayScreen;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * 
 * @author nidebruyn
 */
public class MainApplication extends Base3DApplication {
    
    private int retryCount = 0;

    public static void main(String[] args) {
        new MainApplication();
    }

    public MainApplication() {
        super("Cube Rush", 480, 800, "examplegame.save", null, null, false);
    }

    @Override
    protected void preInitApp() {
        BACKGROUND_COLOR = ColorUtils.rgb(239,239,239);
    }

    @Override
    protected void postInitApp() {
        showScreen("play");
//        cam.setParallelProjection(true);
//        float aspect = (float) cam.getWidth() / cam.getHeight();
//        float frustumSize = 18;
//        cam.setFrustum(-500, 500, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
    }

    @Override
    protected boolean isPhysicsEnabled() {
        return true;
    }

    @Override
    protected void initScreens(ScreenManager screenManager) {
        screenManager.loadScreen("play", new PlayScreen());
    }

    @Override
    public void initModelManager(ModelManager modelManager) {
    }

    @Override
    protected void initSound(SoundManager soundManager) {
    }

    @Override
    protected void initEffect(EffectManager effectManager) {
    }

    @Override
    protected void initTextures(TextureManager textureManager) {
    }

    @Override
    protected void initFonts(FontManager fontManager) {
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void addRetryCount() {
        retryCount ++;
    }
}
