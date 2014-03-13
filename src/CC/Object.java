/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CC;

import java.util.logging.Logger;
import pCSDT.Scripting.*;
import pCSDT.Presentation.OpenGL.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.texture.*;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import pCSDT.Presentation.GUI;
import pCSDT.Presentation.GUI.eRenderState;
import pCSDT.Presentation.JPnlObjMgr;

/**
 *
 * @author Bill
 */
@AutomatableClass(name="Object", desc="The Object")
public class Object extends PObjectOgl {
    PProperty[] m_props;
    PMethod[] m_methods;

    Texture bgTexture = null;  // reference to texture used in this Plait
    boolean bTextureUpdated = false;  // if the texture in bgTexture matches that in m_image_source?
    int timesDrawn = 0;
    //private String m_def_image_source = "img/plaitColor.png";
    
    /*
     * Bytes for default image "img/plaitColor.png"
     */
    @AutomatableProperty(name="Object image bytes", desc="bytes in base 64", DesignTimeBehavior="H", RunTimeBehavior="H")
    public String ImageBytes = "";
     /*
     * Bytes for Costume images
     * Begins with default image
     */
    @AutomatableProperty(name="Costumes", desc="bytes in base 64", DesignTimeBehavior="H", RunTimeBehavior="H")
    public String Costumes = "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\n";
    
    @AutomatableProperty(name="isDefault", desc="true/false whether the image is a plait or not", DesignTimeBehavior="H", RunTimeBehavior="H")
    public boolean isDefault;
    
    @AutomatableProperty(name="icon", desc="icon of current object", DesignTimeBehavior="H", RunTimeBehavior="H")
    public String initIcon = "img/plaitColor.png";
    
    //------------------- Properties Visible in Sensing Panel
    @AutomatableProperty(name="X", desc="x coordinate of current Object position", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compX;

    @AutomatableProperty(name="Y", desc="y coordinate of current Object position", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compY;
    @AutomatableProperty(name="Angle (Degrees)", desc="angle for Plait", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compAngle;
    @AutomatableProperty(name="Red", desc="red color value", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compR;
    @AutomatableProperty(name="Green", desc="green color value", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compG;
    @AutomatableProperty(name="Blue", desc="blue color value", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compB;
    @AutomatableProperty(name="Translation", desc="translation for Object", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compTranslation;
    @AutomatableProperty(name="Dilation", desc="dilation for Object", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compDilation;
    @AutomatableProperty(name="Size", desc="Size of Object", DesignTimeBehavior="H", RunTimeBehavior="R")
    public float compSize;
//    @AutomatableProperty(name="Rotation", desc="rotation for Plait", DesignTimeBehavior="H", RunTimeBehavior="R")
//    public float compRotation;
    
    //------------------- Properties Visible in Starting Values panel
    @AutomatableProperty(name = "Color", desc = "Color of object in R,G,B format", RunTimeBehavior = "H")
    public String orig_color = "1.0f, 1.0f, 1.0f";
    
    @AutomatableProperty(name = "Vector", desc = "Flag for showing/hiding the Vector", RunTimeBehavior = "H")
    public boolean m_vector;
    @AutomatableProperty(name = "Initial X, Y", desc = "Initial X, Y - coordinate", RunTimeBehavior = "H")
    public String orig_x_y;
    @AutomatableProperty(name = "Starting Angle", desc = "Initial Angle for Object", RunTimeBehavior = "H")
    public float orig_angle;
    @AutomatableProperty(name = "Starting Size", desc = "Initial Size for Object", RunTimeBehavior = "H")
    public float orig_size = 100f;
    
    @AutomatableProperty(name="saying?", desc="saying?", DesignTimeBehavior="H", RunTimeBehavior="H")
    public boolean bSaying = false;
    @AutomatableProperty(name="init say?", desc="init say?", DesignTimeBehavior="A", RunTimeBehavior="H")
    public boolean bInitSay = false;
    @AutomatableProperty(name="init words to say", desc="init words to say", DesignTimeBehavior="A", RunTimeBehavior="H")
    public String initWord2say = "Hello";
    

    @AutomatableProperty(name="words to say", desc="words to say", DesignTimeBehavior="H", RunTimeBehavior="H")
    public String word2say = "Hello";
    
    
    public Vector<ObjectImage> firstPlaits = new Vector<ObjectImage>(0);
     
    
    public float orig_x;        //x,y in grid
    public float orig_y;
    private float orig_cx;      //size
    private float orig_cy;
   
    //------------------- Values for 'First' Plait
    
    private float first_m_xO;
    private float first_m_yO;
    private float first_m_cx;
    private float first_m_cy;
    private String first_image_source;
    private String first_color;
    private float first_image_theta;
    
    //------------------- Current System Values for Plait
    
    
    private float m_xO;
    private float m_yO;
    private float m_r;
    private float m_g;
    private float m_b;
    private float m_translate;
    private float m_translateX;
    private float m_translateY;
    private float m_dilate;
    public String m_color;
    private String b_color;
    
    private float m_x;
    private float m_y;
    private float m_nextXO;
    private float m_nextYO;
    private float m_image_theta;
    private float m_vector_theta;
   
    private float m_cx;         //size
    private float m_cy;
    private float m_slope;
   // private float m_rotate_theta;
    //private float m1_rotate;
    private boolean m_x_reflection;
    private boolean m_y_reflection;
    int m_mct = 1;
    int enabledPlaitImageCnt = 0;
    private ObjectImage m_prePlaitImage=null;
    public float m_seconds_delay = 0;
    public float m_at_seconds = 1;
    private String m_image_source;
    private int plait_image_count = 1;
    public boolean m_visible = true;
    public boolean animateFirst = true;
    public int plaitCount = 0;
    public int clearUntil = 0;
    public boolean clear = false;
    public int initial_m_seconds = 0;
    public String imgFileName;

    /**
     * default class constructor
     * @param name name of the Line Object
     * @param desc description of the Line Object
     */
    public Object(String name, String desc) {
        super(name, desc);
        ////System.out.println("Plait default constructor\n");
        
        this.orig_x_y = "-2, 2";
        this.orig_angle = 10f;
        this.orig_cx = 1f;   //size
        this.orig_cy = 1f;
        StringTokenizer tokens = new StringTokenizer(this.orig_x_y, ",");
        try{
        if (tokens.hasMoreTokens()) {
            this.orig_x = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        if (tokens.hasMoreTokens()) {
            this.orig_y = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        }
        catch (Exception e) {
        }
        this.m_nextXO = 0;
        this.m_nextYO = 0;
        this.m_vector_theta = 10;
        this.m_cx = orig_cx;
        this.m_cy = orig_cy;
        this.m_slope = 0;
        this.m_translate = 1; //internally stored is Not in percentage
        this.m_translateX = 1;
        this.m_translateY = 1;
        this.m_x_reflection = false;
        this.m_y_reflection = false;
        this.m_vector = false;
        this.m_image_source = initIcon;
        this.m_x = orig_x;
        this.m_y = orig_y;
        this.m_image_theta = (float) Math.toRadians(orig_angle);
        
        this.first_m_cx = orig_cx;
        this.first_m_cy = orig_cy;
        this.first_image_theta = m_image_theta;
        this.m_color = orig_color;
        this.first_image_source = initIcon;
        int nextId = CCEngine.objectImages.size() + 1;
        this.compR = 0;
        this.compG = 0;
        this.compB = 0;
        //this.compRotation = (float)Math.toDegrees(first_image_theta*100);
        this.compX = orig_x;
        this.compY = orig_y;
        this.compSize = 1;
        this.compDilation = 1;
        this.compTranslation = 1;
        this.compAngle = orig_angle;
        this.animateFirst = true;
       
        
        //this.bSaying = this.bInitSay;
        //this.word2say = this.initWord2say;
         
        Init();
        
  }

   /*Inititate positon, next position and texture*/
    private void Init(){
       //System.out.println("Plait.Init\n");

       ComputeInnerPosition();
       ComputeNextPosition();
        
    }

    /*
     * m_x, m_y are the coordinates of the center of the plait seen by users
     * m_yO, m_yO are the coordinates of the center of the plait used inside
     * This function converts m_x, m_y to m_xO, m_yO
     */
    private void ComputeInnerPosition(){
           //System.out.println("Plait.ComputeInnerPosition\n");


           m_xO = m_x;
           m_yO = m_y;
            
    }

    /*
     * Computer the next inner coordinates of the center of the plait
     */
    private void ComputeNextPosition(){
               //System.out.println("Plait.ComputeNextPosition\n");

        m_nextXO = m_xO + (float) (m_cx * m_translate * Math.cos(m_vector_theta));
        m_nextYO = m_yO + (float) (m_cy * m_translate * Math.sin(m_vector_theta));
    }
    
    private void ComputeNextXPosition(){
               //System.out.println("Plait.ComputeNextPosition\n");

        m_nextXO = m_xO + (float) (m_cx * m_translateX * Math.cos(m_vector_theta));
    }
    
    private void ComputeNextYPosition(){
               //System.out.println("Plait.ComputeNextPosition\n");

        m_nextYO = m_yO + (float) (m_cy * m_translateY * Math.sin(m_vector_theta));
    }

  
    /**
     * Rotate the plait by the input degree
     */
    @AutomatableMethod(displayPosString = "[]", name = "Rotate", argNames = {"Degree"}, argVals = {"80"})
    public void Rotate(float rotate) {
        //System.out.println("Plait.AutomatableMethod.Rotate");


        float rotate_thePlait = (float) Math.toRadians(rotate);
        if((m_x_reflection && !m_y_reflection) || (!m_x_reflection && m_y_reflection))
            rotate_thePlait *= -1;
        
        compAngle += rotate;
        m_vector_theta += rotate_thePlait;
        m_image_theta += rotate_thePlait;

        ComputeNextPosition();
        //Rotation changes the vector of the previous plait image
        if(m_prePlaitImage!=null)
            m_prePlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate,
                    m_vector_theta, m_at_seconds);
        if(plait_image_count == 1){
            first_image_theta = rotate_thePlait;
            //compRotation = (float)Math.toDegrees(first_image_theta);
            
        }else{
        //m_rotate_theta = rotate_thePlait;        
        CCEngine.objectImages.lastElement().pm_image_theta = m_image_theta;
        CCEngine.objectImages.lastElement().pm_vector_theta = m_vector_theta;
        //compRotation = (float)Math.toDegrees(m_image_theta);
    
        }
        
       

    }
    /*
    @AutomatableMethod(name="Set say message", argNames={""}, argDesc={"Message to say"}, argVals = {""})
    public void ChangeWordsToSay(String str)
    {
        word2say = str;
    }

   
    
    @AutomatableMethod(name="Start Saying", argNames={}, argDesc={}, argVals = {})
    public void StartSaying() {
        bSaying = true;
    }
    @AutomatableMethod(name="Stop Saying", argNames={}, argDesc={}, argVals = {})
    public void StopSaying() {
        bSaying = false;
    }
     * 
     */
     
    @AutomatableMethod(displayPosString = "[]", name = "Dilate by percent", argNames = {"Percent"}, argVals = {"80"})
    public void DilateByPercent( float dilate) {
        //System.out.println("Plait.AutomatableMethod.DilatebyPercent\n");

        //assert dilate >= 0 : "Dilate percent should not be less than 0";
        m_cx *= dilate/100;
        m_cy *= dilate/100;
        m_dilate = dilate;
        if(plait_image_count == 1){
            first_m_cx *= dilate/100;
            first_m_cy *= dilate/100;
            compSize = (first_m_cx*100);

            
        }else{
       
        CCEngine.objectImages.lastElement().pm_cx = m_cx;
        CCEngine.objectImages.lastElement().pm_cy = m_cy;
        compSize = (m_cx*100);

        }
        compDilation = dilate;
    }
   
    
    @AutomatableMethod(displayPosString = "[]", name = "Translate by percent", argNames = {"Percent"}, argVals = {"80"})
    public void TranslateByPercent(float translate) {
        //System.out.println("Plait.AutomatableMethod.TranslatebyPercent\n");

        assert translate >= 0 : "translate percent should not be less than 0";
        m_translate = translate/100;
        ComputeNextPosition();
        m_xO = m_nextXO;
        m_yO = m_nextYO;
        if(plait_image_count == 1){
             if(m_prePlaitImage!=null){
                    m_prePlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate,
                        m_vector_theta, m_at_seconds);}
                compX = first_m_xO;
                compY = first_m_xO;
                first_m_xO = m_nextXO;
                first_m_yO = m_nextYO;
               
       

        }else{
        
        //Translation changes the vector of the previous plait image
        if(m_prePlaitImage!=null)
            m_prePlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate,
                    m_vector_theta, m_at_seconds);

        
        CCEngine.objectImages.lastElement().pm_xO = m_xO;
        CCEngine.objectImages.lastElement().pm_yO = m_yO;
        compX = m_xO;
        compY = m_yO;
        m_x = m_xO;
        m_y = m_yO;

        
        
        }
        compTranslation = translate;
    }
    
    @AutomatableMethod(displayPosString = "[]", name = "Translate X", argNames = {""}, argVals = {"1"})
    public void TranslateX(float translateX) {
        m_translateX = translateX;
        ComputeNextXPosition();
        m_xO = m_xO + translateX;
        if(plait_image_count == 1){
            first_m_xO = first_m_xO + translateX;
        }else{
           // if(BLEngine.beadImages.size()>0){
           // BLEngine.beadImages.lastElement().pm_xO = m_xO;
            
           // }

        }
        compX = m_xO;
        //compXTranslation = (int)translateX;
    }
    
    @AutomatableMethod(displayPosString = "[]", name = "Translate Y", argNames = {""}, argVals = {"1"})
    public void TranslateY(float translateY) {
        m_translateY = translateY;
        ComputeNextYPosition();
        m_yO = m_yO + translateY;
        if(plait_image_count == 1){
            first_m_yO = first_m_yO + translateY;
        }else{
           // if(BLEngine.beadImages.size()>0){
           // BLEngine.beadImages.lastElement().pm_yO = m_yO;
            
           // }

        }
        compY = m_yO;
        //compYTranslation = (int)translateY;

    }
    
    @AutomatableMethod(displayPosString = "[]", name = "Duplicate")
    public void Duplicate() {
        //System.out.println("Plait.AutomatableMethod.Duplicate\n");

        plait_image_count++;
        animateFirst = false;
       
        //int nextId = CCEngine.plaitImages.size() + 1;
        m_at_seconds = m_seconds_delay * (float)plait_image_count * 10;
        if(m_at_seconds == 0){m_at_seconds = 1;}
        m_image_source = initIcon;
        bTextureUpdated = false;
       
        getColor(this.m_color);
        ObjectImage newPlaitImage = new ObjectImage(plait_image_count, m_xO, m_yO, m_cx, m_cy,
                m_image_theta, m_vector_theta, m_color, m_image_source, m_vector, m_visible, m_at_seconds, null, false, bSaying, word2say, isDefault);
        
        newPlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate, m_vector_theta, m_at_seconds);
        CCEngine.objectImages.add(newPlaitImage);
        CCEngine.object_map.get(CCEngine.objects.indexOf(this)).add(newPlaitImage);
        m_prePlaitImage = newPlaitImage;
    }

   
@AutomatableMethod(displayPosString = "[]", name = "Set Color", argNames = {"Red (0-255)",
                                                                "Green (0-255)",
                                                                "Blue (0-255)"},argVals={"255","0","0"})
    public void setColor(float r, float g, float b) {
        //System.out.println("Plait.AutomatableMethod.SetPlaitColor\n");
        this.m_r = r;
        this.m_g = g;
        this.m_b = b;
        this.m_color = m_r + "," + m_g + "," + m_b;
        
        if(animateFirst){
            first_color = m_color;
        }else{
            if(CCEngine.objectImages.size()>0)
            CCEngine.objectImages.lastElement().pm_color = m_color;
        }
        compR = m_r;
        compG = m_g;
        compB = m_b;
     }
//@AutomatableMethod(displayPos = 5, name = "Go To", argNames = {"X", "Y"}, argVals = {"0","0"})
//    public void GoTo(float x, float y) {
//        //System.out.println("Plait.AutomatableMethod.GoTo\n");
//
//        m_xO = x;
//        m_yO = y;
//        compX = x;
//        compY = y;
//        //ComputeInnerPosition();//this must be before ComputeNextPosition
//        ComputeNextPosition();
//        if(plait_image_count == 1){
//            first_m_xO = m_xO;
//            first_m_yO = m_yO;
//        }else{
//            CCEngine.plaitImages.lastElement().pm_xO = m_xO;
//            CCEngine.plaitImages.lastElement().pm_yO = m_yO;         
//        }
//        
//    }

@AutomatableMethod(displayPosString = "[]", name = "Set X", argNames = {"X"}, argVals = {"0"})
    public void GoToX(float x) {
        //System.out.println("Plait.AutomatableMethod.GoTo\n");

        m_xO = x;
        compX = x;
        //ComputeInnerPosition();//this must be before ComputeNextPosition
        ComputeNextPosition();
        if(plait_image_count == 1){
            first_m_xO = m_xO;
        }else{
            CCEngine.objectImages.lastElement().pm_xO = m_xO;       
        }
        
    }

@AutomatableMethod(displayPosString = "[]", name = "Set Y", argNames = {"Y"}, argVals = {"0"})
    public void GoToY(float y) {
        //System.out.println("Plait.AutomatableMethod.GoTo\n");

        m_yO = y;
        compY = y;
        //ComputeInnerPosition();//this must be before ComputeNextPosition
        ComputeNextPosition();
        if(plait_image_count == 1){
            first_m_yO = m_yO;
        }else{
            CCEngine.objectImages.lastElement().pm_yO = m_yO;         
        }
        
    }

@AutomatableMethod(displayPosString = "[]", name = "Set Size By Percent", argNames = {"Percent"}, argVals = {"100"})
    public void SetSize( float percent) {
        //System.out.println("Plait.AutomatableMethod.SetSize\n");

        assert percent >= 0 : "Set Size, percent should not be less than 0";
        m_cx = orig_cx*percent/100;
        m_cy = orig_cy*percent/100;
        
        if(plait_image_count == 1){
            first_m_cx = m_cx;
            first_m_cy = m_cy;
            compSize = m_cx;
        }else{
            CCEngine.objectImages.lastElement().pm_cx = m_cx;
            CCEngine.objectImages.lastElement().pm_cy = m_cy;  
            compSize = m_cx;

        }
    }

   @AutomatableMethod(displayPosString = "[]", name = "Reflect", argNames = {"X Reflection", "Y Reflection"}, argVals = {"false","false"})
    public void Reflect(boolean x_reflection, boolean y_reflection) {
        //System.out.println("Plait.AutomatableMethod.Reflect\n");

        assert x_reflection == true || x_reflection == false : "reflect about x-axis should be 0 or 1";
        assert y_reflection == true || y_reflection == false : "reflect about y-axis should be 0 or 1";
        m_x_reflection = (x_reflection == true);
        m_y_reflection = (y_reflection == true);
        DoReflection();//this action can only be performance once!!
        
        if(plait_image_count == 1){
            first_m_xO = m_x;
            first_m_yO = m_y;
            first_image_theta = m_image_theta;
            //first_vector_theta = m_vector_theta;
            
        }else{
            CCEngine.objectImages.lastElement().pm_xO = m_x;
            CCEngine.objectImages.lastElement().pm_yO = m_y;
            CCEngine.objectImages.lastElement().pm_image_theta = m_image_theta;
            CCEngine.objectImages.lastElement().pm_vector_theta = m_vector_theta;

            
        } 
        
    }
 private void DoReflection() {

        if (m_x_reflection && !m_y_reflection) {
            //m_x=m_x;
            m_y = -m_y;
            m_image_theta = (float) Math.toRadians(90) - m_image_theta;
            m_vector_theta = -m_vector_theta;
        } else if (!m_x_reflection && m_y_reflection) {
            m_x = -m_x;
            //m_y=m_y;
            m_image_theta = -(float) Math.toRadians(90) - m_image_theta;
            m_vector_theta = (float) Math.toRadians(180) - m_vector_theta;
        } else if (m_x_reflection && m_y_reflection) {
            m_x = -m_x;
            m_y = -m_y;
            m_image_theta = -(float) Math.toRadians(180) + m_image_theta;
            m_vector_theta = (float) Math.toRadians(180) + m_vector_theta;
        }
        //ComputeInnerPosition must before ComputeNextPosition, which uses its result
        ComputeInnerPosition();
        ComputeNextPosition();
    }
 
    public void Show(){
        plait_image_count++;
       
        //int nextId = CCEngine.plaitImages.size() + 1;
        m_at_seconds = m_seconds_delay * (float)plait_image_count * 10;
        if(m_at_seconds == 0){m_at_seconds = 1;}
        m_image_source = initIcon;
        bTextureUpdated = false;
       
        getColor(this.m_color);
        ObjectImage newPlaitImage = new ObjectImage(plait_image_count, m_xO, m_yO, m_cx, m_cy,
                m_image_theta, m_vector_theta, m_color, m_image_source, m_vector, m_visible, m_at_seconds, null, false, bSaying, word2say, isDefault);
        
        newPlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate, m_vector_theta, m_at_seconds);
        CCEngine.objectImages.add(newPlaitImage);
        CCEngine.object_map.get(CCEngine.objects.indexOf(this)).add(newPlaitImage);
        m_prePlaitImage = newPlaitImage;
        
    }
    
@AutomatableMethod(displayPosString = "[]", name = "Wait", argNames = {"Seconds"}, argVals = {"0"})
    public void Wait(float seconds){
        //System.out.println("Wait\n");
        this.m_seconds_delay = seconds;
        this.m_visible = false;
        if(animateFirst){
            Show();        
        }else{
            if(plaitCount == 0){
                plaitCount = plait_image_count;
            }
            for(int i = plaitCount-1; i < CCEngine.object_map.get(CCEngine.objects.indexOf(this)).size(); i++){
                CCEngine.object_map.get(CCEngine.objects.indexOf(this)).get(i).pm_visible = m_visible;
                CCEngine.object_map.get(CCEngine.objects.indexOf(this)).get(i).pm_at_seconds = m_seconds_delay * (float)plaitCount * 10;
                //System.out.println("Plait " + CCEngine.plaits.indexOf(this) + " plaitCount = " + plaitCount);
                //System.out.println("Plait " + CCEngine.plaits.indexOf(this) + " PM_At_Seconds " + CCEngine.plaitImages.get(i).pm_at_seconds);
            }
            plaitCount = plait_image_count;
            //CCEngine.plaits.get(CCEngine.plaits.indexOf(this)).m_visible = true;            
        } 
    }
/*
@AutomatableMethod(displayPos = 1, name = "Set Plait Image", argNames = {"Image source file "},argVals={"img/plaitColor.png"})
    public void SetImage(String image_source) {
        m_image_source = image_source;
        bTextureUpdated = false;
         if(plait_image_count == 1){
            first_image_source = image_source;
            
        }else{
            CCEngine.plaitImages.lastElement().pm_image_source = m_image_source;
        }       
    }
  * 
  */
public void getColor(String colorString) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(colorString, ",");
        //System.out.println("Plait.AutomatableMethod.getColor\n");

        this.m_r = Float.parseFloat(st.nextToken());
        this.m_g = Float.parseFloat(st.nextToken());
        this.m_b = Float.parseFloat(st.nextToken());
        }

@AutomatableMethod(displayPosString = "[]", name = "Reset")
    public void ResetObject() throws IOException
    {
        //System.out.println("Plait.AutomatableMethod.ResetPlait\n");

        animateFirst = true;
        m_color = orig_color;
        getColor(m_color);
        setColor(m_r, m_g, m_b);
        m_translate = 1.0f;
        m_x_reflection = false;
        m_y_reflection = false;
        m_prePlaitImage = null;
        plait_image_count = 1;
        m_image_source = initIcon;
        m_visible = true;
        m_cx = orig_cx*orig_size/100;
        m_cy = orig_cy*orig_size/100;
       
        StringTokenizer tokens = new StringTokenizer(this.orig_x_y, ",");
        try{
        if (tokens.hasMoreTokens()) {
            this.orig_x = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        if (tokens.hasMoreTokens()) {
            this.orig_y = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        }
            catch (Exception e) {
        }


        m_x = orig_x;
        m_y = orig_y;
        first_m_cx = m_cx;
        first_m_cy = m_cy;
        
        first_m_xO = orig_x;
        first_m_yO = orig_y;
        first_color = orig_color;
        first_image_theta = (float) Math.toRadians(orig_angle);
        m_vector_theta=first_image_theta;
        
        //compRotation = first_image_theta;
        compX = orig_x;
        compY = orig_y;
        compSize = 1;
        compDilation = 1;
        compTranslation = 1;
        compAngle = orig_angle;
        
        
        Init();
        
       
    }
 @AutomatableMethod(displayPosString = "[]", name = "Angle", argNames = {"Degree"}, argVals = {"80"})
    public void abs_rotate(float rotate) {
        //System.out.println("Plait.AutomatableMethod.Angle\n");

        float rotate_thetab = (float) Math.toRadians(rotate);
        //if reflection is on, change the direction of the rotate accordingly
        m_vector_theta = rotate_thetab;
        m_image_theta = rotate_thetab;
        ComputeNextPosition();
        if(m_prePlaitImage!=null)
            m_prePlaitImage.SetVector(m_nextXO, m_nextYO, orig_cx, m_translate,
                    m_vector_theta, m_at_seconds);
        if(plait_image_count == 1){
            first_image_theta = m_image_theta;
            
        } else{
            CCEngine.objectImages.lastElement().pm_image_theta = m_image_theta;
            CCEngine.objectImages.lastElement().pm_vector_theta = m_vector_theta;
        }

        
        
        compAngle = rotate;

    }
 

 int temp=8;
    /**
     *
     */
    @AutomatableMethod(displayPosString = "[]", name = "Change Costume", argNames={""}, argDesc={"File Name"}, argVals={"[PlaitCostumeChange]"})
    public void ChangeCostume(String s) {
        //Find index of string in codelet
        char i = s.charAt(s.length()-1);
        int j = i - '0';
        ArrayList<String> temp = JPnlObjMgr.ObjectImageMap.get(this);
        if(temp != null) {
            //color correcting purposes if the image isn't the default
            if(j > 1) CCEngine.default_map.put(this, false);
            isDefault = CCEngine.default_map.get(this);
            initIcon = temp.get(j-1);
        }
    }
 

  
     /**
     * Get the x-coordinate of the object world
     * @return 0f
     */
    @Override
    public double GetX() {
        return 0f;
    }

    /**
     * Get the y-coorindate of the object world
     * @return 0f
     */
    @Override
    public double GetY() {
        return 0f;
    }

    /**
     * Get the rotation matrix of the line
     * @return 0f
     */
    @Override
    public Rotation3 GetRotation() {
        return new Rotation3(0f);
    }

    /**
     * This method actually draws the Line object
     * @param gl GL object
     * @param glu GLU object
     * @param frame frame rate
     * @param dt time elapse
    */
    @Override
    public void Draw(GL gl, GLU glu, int frame, double dt) {
        //loading texture must happen in a GL context
//        System.out.println("Plait.AutomatableMethod.Draw\n");
        
        if (!this.bTextureUpdated)
            OnLoadTextures(gl, glu);

        getColor(m_color);
        isDefault = CCEngine.default_map.get(this);
        if(m_visible){
            CCUtil.DrawPlait(gl, glu, first_m_xO, first_m_yO, first_m_cx/2, first_m_cy/2, first_image_theta, first_color,
                  bgTexture, first_image_source, bSaying, word2say, isDefault);
        }
     
        for (ObjectImage pm : firstPlaits)
            pm.Draw(gl, glu, frame, dt, bgTexture);
        //draw the list of plait images
        for (ObjectImage pm : CCEngine.objectImages)
            pm.Draw(gl, glu, frame, dt, bgTexture);
        float m_image_theta = (float) Math.toRadians(orig_angle);
        timesDrawn++;
    
       //DrawPlait must be after drawing the plait images, orelse the plait
       //will be covered by the last plait image

      
       if(this.m_vector)
            CCUtil.DrawVector(gl, m_xO, m_yO, m_nextXO, m_nextYO,
			m_cx/2, orig_cx/2, m_vector_theta, m_translate);
    }

    
     /**
     * Clear the plait image, leave the plait
     * @return 0f
     */
    @AutomatableMethod(displayPosString = "[]", name = "Clear")
    public void Clear() {
        //System.out.println("Plait.AutomatableMethod.Clear\n");
        ArrayList<ObjectImage> temp = new ArrayList<ObjectImage>();
        for(int i=clearUntil; i<CCEngine.object_map.get(CCEngine.objects.indexOf(this)).size(); i++){
            CCEngine.object_map.get(CCEngine.objects.indexOf(this)).get(i).clear = true;
            temp = CCEngine.object_map.get(CCEngine.objects.indexOf(this));
            CCEngine.object_map.get(CCEngine.objects.indexOf(this)).get(i).after_seconds = temp.get(temp.size()-1).pm_at_seconds;
        }
        clearUntil = CCEngine.object_map.get(CCEngine.objects.indexOf(this)).size()-1;
    }

    /**
     * This method checks if the mouse location c is within the Line
     * with a view vector specified by l
     * @param c the mouse location
     * @param l the view vector
     * @return HTResult.Hit if the mouse location is within the object area
     *         HTResult.Miss otherwise
     */


    @Override
    public HTResult HitTest(Vector3 c, Vector3 l) {

        ////System.out.println("Plait.AutomatableMethod.HitTest\n");

        //////System.out.println("Enter Hit Test");
        //(x, y) is the vector from the center of the plait to the mouse location
        double x = c.x() - first_m_xO;
        double y = c.y() - first_m_yO;
        /*
        double x = c.x() - m_xO;
        double y = c.y() - m_yO;
         * 
         */
        //rotate (x, y) by -m_image_theta, since the plait has been rotated m_image_theta
        //so that we can use m_cx/2 and m_cy/2 as the checking distances
        double costheta = Math.cos(-m_image_theta);
        double sintheta = Math.sin(-m_image_theta);
        double xrot = x*costheta - y*sintheta;
        double yrot = x*sintheta + y*costheta;
        if(
            -m_cx/2 < xrot && xrot < m_cx/2 &&
            -m_cy/2 < yrot && yrot < m_cy/2
        ) {
            return HTResult.Hit;
        }
        return HTResult.Miss;
    }



    /**
     * This is called when a pCSDT is started by the user
     */
    @Override
    public void OnBegin()
    {
        ////System.out.println("Plait.AutomatableMethod.OnBegin\n");
        super.OnBegin();
        this.m_at_seconds = 0;
        Reset(); 
    }

    /**
     * Encloses all stuff to be done when the simulation is stopped
     */
    @Override
    public void OnEnd() {
                ////System.out.println("Plait.AutomatableMethod.OnEnd\n");
        super.OnEnd();
        //Reset();
        
    }

    /**
     * Reset the plait to its initial state
     */
    @Override
    public void Reset()
    {
        ////System.out.println("Plait.AutomatableMethod.Reset\n");
        initial_m_seconds = 0;
        clear = false;
        clearUntil = 0;
        CCEngine.objectImages.removeAllElements();
        CCEngine.object_map.put(CCEngine.objects.indexOf(this), new ArrayList<ObjectImage>());
        firstPlaits.removeAllElements();
        animateFirst = true;
        m_mct = 1;
        m_seconds_delay = 0;
        bTextureUpdated = false;
        m_color = orig_color;
        getColor(m_color);
        setColor(m_r, m_g, m_b);
        m_translate = 1.0f;
        m_x_reflection = false;
        m_y_reflection = false;
        m_prePlaitImage = null;
        plait_image_count = 1;
        m_image_source = initIcon;
        m_visible = true;
        m_cx = orig_cx*orig_size/100;
        m_cy = orig_cy*orig_size/100;
       
        StringTokenizer tokens = new StringTokenizer(this.orig_x_y, ",");
        try{
        if (tokens.hasMoreTokens()) {
            this.orig_x = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        if (tokens.hasMoreTokens()) {
            this.orig_y = Float.parseFloat(tokens.nextToken());
        }
        else {
            throw new Exception();
        }
        }
            catch (Exception e) {
        }


        m_x = orig_x;
        m_y = orig_y;
        first_m_cx = m_cx;
        first_m_cy = m_cy;
        first_m_xO = orig_x;
        first_m_yO = orig_y;
        first_color = orig_color;
        first_image_theta = (float) Math.toRadians(orig_angle);
        m_image_theta = first_image_theta;
        m_vector_theta=first_image_theta;
        
        //compRotation = first_image_theta;
        compX = orig_x;
        compY = orig_y;
        compSize = 1;
        compDilation = 1;
        compTranslation = 1;
        compAngle = orig_angle;
        
        bSaying = bInitSay;
        word2say = initWord2say;
        ComputeInnerPosition();
        ComputeNextPosition();
        //    Init();
        
    }
    
    public Texture load(String fileName)
    {
            //load(initIcon);
            //System.out.append("Texture Load\n");
            Texture text = null;
            java.io.InputStream InputStream;
        
        try {   
                if (fileName != null && !fileName.equals("") && !fileName.equals("img/plaitColor.png")) {
                    bgTexture = TextureIO.newTexture(new File(fileName), false);
                    bgTexture.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    bgTexture.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    m_cx = bgTexture.getWidth();
                    m_cy = bgTexture.getHeight();
                    this.bTextureUpdated = true;
                    CCEngine.image_map.put(this, fileName);
                    setColor(255,255,255);
                    //System.out.println("Loading texture " + this);
                    //CCEngine.objectImgSource = fileName;
                    //System.out.println("Adding new texture\n");
            }
            }catch (Exception e) {
                fileName = "img/plaitColor.png";
                System.out.println(e.getMessage());
                System.out.println("1 Error loading texture " + initIcon);
            }
        try {
                if(fileName.equals("img/plaitColor.png")){
                    InputStream = CCEngine.class.getResourceAsStream(fileName);
                    bgTexture = TextureIO.newTexture(InputStream, false, ".png");
                    bgTexture.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    bgTexture.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    this.bTextureUpdated = true;
                    CCEngine.image_map.put(this, fileName);
                    //System.out.println("Adding default texture\n");
                }
                
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("2 Error loading texture " + initIcon);
        }
        return text;
    }     

    /**
     * This function is called by the core for loading textures
     */
    @Override
    public void OnLoadTextures(GL gl, GLU glu) {
            ////System.out.println("Plait.AutomatableMethod.OnLoadTextures\n");
         load(initIcon);
        
//            bgTexture = null;
//            java.io.InputStream fullpath = null;
//
//            try {
//                fullpath = CCEngine.class.getResourceAsStream(m_image_source);
//
//                bgTexture = TextureIO.newTexture(fullpath, false, ".png");
//                bgTexture.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
//                bgTexture.setTexParameterf(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
//                this.bTextureUpdated = true;
//               }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
    }
@Override
    public String IsInputValid(String str, String propName) {
        ////System.out.println("Plait.AutomatableMethod.IsInputValid\n");

        String superReturn = super.IsInputValid(str, propName);
        if (superReturn != null) {
            return superReturn;
        }
        if (propName.equals("Initial X, Y")) {
            if (!Pattern.matches("^-?\\d+\\.?\\d*\\s*,\\s*-?\\d+\\.?\\d*$", str.trim())) {
                return "Please use comma to separate x, y coordinates.";
            }
        }
        else if (propName.equals("Starting Angle")) {
            float f = Float.parseFloat(str);
            if (f<-360 || f>360) {
                return "The value should be between -360 and 360 inclusive.";
            }
        }

        return null;
    }
@Override
   public Vector<Vector3> GetPolyBound() {
       // compute the width and height
       
       double costheta = Math.cos(first_image_theta);
       double sintheta = Math.sin(first_image_theta);

       

      

       // obtain how long one pixel is representing logically
       java.awt.Dimension canvasDim = this.GetPEngine().GetGui().getCanvasSize();

       PEngineOgl engineOgl = (PEngineOgl)m_pEngine;
       double allowx = (float)1/(engineOgl.scale/200)/canvasDim.width*m_pEngine.pixelAllowance_x;
       double allowy = (float)1/(engineOgl.scale/200)/canvasDim.height*m_pEngine.pixelAllowance_y;
       
       // the allowance is adjusted according to how far they are away from x
       // and y axes
       
       Vector<Vector3> v = new Vector<Vector3>(0);
       if(GUI.renderState==GUI.eRenderState.Stopped){
           v.add(new Vector3(first_m_xO-.75, first_m_yO+.75, 0));
           v.add(new Vector3(first_m_xO+.75, first_m_yO+.75, 0));
           v.add(new Vector3(first_m_xO+.75, first_m_yO-.75, 0));
           v.add(new Vector3(first_m_xO-.75, first_m_yO-.75, 0));
           GUI.animation = true;
           GUI.needsToDraw = true;
       }
       return v;
       
   }
   
}