/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CC;
import pCSDT.Presentation.OpenGL.*;
import java.text.*;
import java.awt.*;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Tanushree
 */
public class CCGui extends GUIOgl {

     CCEngine pEngine;
     boolean bFirstInit = true;
     
    /**
     * Initialize the applet
     */
    @Override
    public void init()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        System.err.println("Screen width: "+dim.width+", Screen height: "+dim.height);
        setSize(1200, 800);
        BGImgPropertiesStr = "DefaultBackgrounds.properties";
        goalImgPropertiesStr = "GoalImages.properties";
        demoPropertiesStr = "Demos.properties";
        //demoImgPropertiesStr = "DemoImages.properties";
        bEraseDrawingButtonEnabled = true;
        bGoalImagesButtonEnabled = true;
        //animation = false;
        useCodeletDisplayOrder = true;

        super.init();
    }
    
    @Override
    public void DeferredInitialize() {

        super.DeferredInitialize();
        
    }
       
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        super.reshape(drawable, x, y, width, height);
        for (ObjectImage pi: pEngine.objectImages) {
            pi.OnLoadTextures(drawable.getGL(), glu);
        }
    }
    

    /**
     * Return the MP simulation engine.
     * @return a MP simulation engine
     */
    @Override
    public PEngineOgl GetEngineOgl()
    {
        if (pEngine == null) {
                pEngine = new CCEngine();
            }

    
        return pEngine;
    }
    @Override
    public String DisplayMouseLocation(double x, double y, double z) {
        NumberFormat format = new DecimalFormat("0.00");
        return "[" + format.format(x) + ", " + format.format(y) + "]";
    }
    


}