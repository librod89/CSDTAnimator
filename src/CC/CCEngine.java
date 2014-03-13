/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CC;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import pCSDT.Presentation.GUI.*;
import pCSDT.Presentation.OpenGL.PEngineOgl;
import pCSDT.Scripting.PObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.jdom.Element;
import pCSDT.Presentation.JPnlObjMgr;
import pCSDT.Scripting.PEngine;
import pCSDT.Scripting.PProperty;




/**
 *
 * @author Tanushree
 */
public class CCEngine extends PEngineOgl {

    String codename1 = "CC";
    String version1 = "0.41";
   

    String[][] appJars1 = {
        {"v0.41/CC.jar", "true"}
    };

    String[][] appExts1 = {
        {"pCSDT-Core", "../Core/Animator/Core.jnlp"},
    };

    String appletName1 = "pCSDT CC Applet";
    String appletMainClass1 = "CC.CCGui";
    String appletWidth1 = "1000";
    String appletHeight1 = "600";

    static Class[] s_objs = {Object.class};
    public static Vector<Object> objects = new Vector<Object>(0);
    public static Vector<ObjectImage> objectImages = new Vector<ObjectImage>(0);
    public int step_num = 0;
    
    //Control which plaitImages belong to which plaits
    public static HashMap<Integer, ArrayList<ObjectImage>> object_map = new HashMap<Integer, ArrayList<ObjectImage>>();
    
    //Control which image belongs to which Plait Object
//    public static HashMap<Integer, String> image_map = new HashMap<>();
    
    /*
     * Store image saved by user in xml file
     */
//    protected String objImg_base64 = "";


    float r = 0;
    float g = 0;
    float b = 0;
   

     /**
     * Default class constructor
     */
    public CCEngine()
    {
        super(s_objs);
       // System.out.println("CCEngine.DefaultConstructor\n");

        codename = codename1;
        version = version1;

        appJars = appJars1;
        appExts = appExts1;

        appletName = appletName1;
        appletMainClass = appletMainClass1;
        appletWidth = appletWidth1;
        appletHeight = appletHeight1;
        
        translate_x = translate_y = 0;
        scale = 50;
        
        range_x = 12;
        range_y = 12;

        bDrawGridBeyondRange = true;
        bDrawAnimate = false;
        
        
        
        interval_x = 1; interval_y = 1;
        textStartx = -6;
        textStarty = -6;
        textHeight = 12;
        textLength = 12;
        
        defaultBgFile = "bgImg/white.png";
    }

     @Override
    public void OnAddObject(PObject obj)
    {
       // System.out.println("CCEngine.OnAddObject "+ obj + "\n");

        if (obj instanceof Object) {
            objects.add((Object)obj);
            object_map.put(objects.indexOf(obj), new ArrayList<ObjectImage>());
            if(default_map.get(obj) == null){
                default_map.put(obj, true);
            }
        }
        else{
            System.err.println("CCEngine.OnAddObject, unknown object");
        }

    }

  /**
     * Delete a PObject from the BoarderEngine
     * @param obj the PObject to be deleted
     */
    @Override
    public void OnDelObject(PObject obj)
    {
       // System.out.println("CCEngine.OnDelObject\n");
        if (obj instanceof Object) {
            objects.remove((Object)obj);
            object_map.remove(objects.indexOf(obj));
            default_map.remove(obj);
        }
        else{
            System.err.println("CCEngine.OnDelObject, unknown object");
        }
    }
    /**
     * Calculate the new position of non-static object after a time elapse
     * of dt unit
     * @param dt the time elapse
     */
    @Override
    public void Step(double dt)
    {       
        //System.out.println("Step #" + step_num + "\n");
        step_num++;
        for(int g = 0; g < objects.size(); g++){
           int n = 0; //temporary integer
           ArrayList<ObjectImage> temp = new ArrayList<ObjectImage>();
           temp = object_map.get(g);
           
           for(int i = 0; i<temp.size(); i++){
                /*Check for Wait Codelet*/
               if((int)temp.get(i).pm_at_seconds == step_num){
                   temp.get(i).pm_visible = true;
                   n = i;
               
                   if(objects.get(g).animateFirst){
                       for(int j = 0; j< n; j++){
                           temp.get(j).pm_visible = false;
                       }
                   }
                   /*Check for Clear Codelet*/
                   for(int j = 0; j<temp.size(); j++){
                        if((int)temp.get(j).after_seconds < step_num){
                            if(temp.get(j).clear) temp.get(j).pm_visible = false;
                        }
                   }
               }
           }
        }
        
    }

    /**
     * Set Selected objects
     * @param objs an array of PObjects
     */
    @Override
    public void SetSelectedObjects(PObject[] objs)
    {
       // System.out.println("CCEngine.SetSelectedObjects\n");

        }
    
      /**
     * This method includes things to be done when Engine's simulation starts
     */
    @Override
    public void OnBegin()
    {
      // System.out.println("CCEngine.OnBegin\n");
        step_num = 0;
    }
    
//    @Override
//    public void OnEnd(){
//        bReady = false;
//        world = null;
//    }

    @Override
    public void ClearDrawing() {
        //curves.removeAllElements();
        //System.out.println("CCEngine.ClearDrawing\n");

        for(Object p: objects) {
            p.Reset();
            //p.Clear();
        }

    }
    @Override
    public PObject GetDefaultObject()
    {
         //   System.out.println("CCEngine.GetDefaultObject\n");
            if(objects.size() != 0){
                return objects.get(0);
            }
                   
            return super.GetDefaultObject();
    }
    
    public boolean IsInside(float x, float y) {
        if (x > range_x || y > range_y || x < 0 || y < 0) return false;
        return true;
    }
    
}