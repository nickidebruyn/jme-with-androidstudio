/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.cuberush.game;
import com.bruynhuis.galago.app.BaseApplication;
import com.bruynhuis.galago.util.Debug;
import com.bruynhuis.galago.util.SharedSystem;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.io.IOException;

/**
 * This is a simple shadow control class which will create a quad surface with a texture
 * below the player or object.
 * It will make use of rays to determine the scale of the shadow to the surface.
 * 
 * 
 * @author NideBruyn
 */
public class ShadowControl extends AbstractControl {
    
    protected BaseApplication baseApplication;
    private Light sourceLight;
    private Texture shadowTexture;
    private Material material;
    private float shadowIntesity;
    private Geometry shadowGeometry;
    private float xSize;
    private float zSize;
    private float offsetY = 0.01f;
    private float scale = 1.5f;

    public ShadowControl(Light sourceLight, Texture shadowTexture, float shadowIntesity) {
        this.sourceLight = sourceLight;
        this.shadowTexture = shadowTexture;
        this.shadowIntesity = shadowIntesity;
        this.baseApplication = SharedSystem.getInstance().getBaseApplication();
    }
    

    @Override
    protected void controlUpdate(float tpf) {
        
        //First we initialize the quad
        if (shadowGeometry == null) {
            BoundingVolume boundingVolume = spatial.getWorldBound();
            if (boundingVolume instanceof BoundingSphere) {
                BoundingSphere bs = (BoundingSphere) boundingVolume;
                xSize = bs.getRadius()*2;
                zSize = bs.getRadius()*2;
            } else {
                BoundingBox bb = (BoundingBox) boundingVolume;
                xSize = bb.getXExtent()*2;
                zSize = bb.getZExtent()*2;
            }
            
            Debug.log("xSize: " + xSize + "; zSize: " + zSize);
            
            Quad q = new Quad(xSize*scale, zSize*scale);
            
            shadowGeometry = new Geometry("shadow-" + spatial.getName(), q);
            shadowGeometry.move(xSize, offsetY, zSize);
            shadowGeometry.rotate(-FastMath.HALF_PI, 0, 0);
            
            shadowGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
            
            material = new Material(baseApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("ColorMap", shadowTexture);
            material.setColor("Color", new ColorRGBA(1, 1, 1, shadowIntesity));
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.PremultAlpha); 
            material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Back);
            shadowGeometry.setMaterial(material);

            spatial.getParent().attachChild(shadowGeometry);

        } else if (spatial != null) {
            shadowGeometry.setLocalTranslation(spatial.getWorldTranslation().x-(xSize*scale*0.5f), spatial.getWorldTranslation().y + offsetY, spatial.getWorldTranslation().z + (zSize*scale*0.5f));
                    
        }
        
    }
    
    public void dispose() {
        spatial.removeControl(this);
        shadowGeometry.removeFromParent();
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        ShadowControl control = new ShadowControl(sourceLight, shadowTexture, shadowIntesity);
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
    
}
