/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CC;

import com.sun.opengl.util.texture.Texture;
import java.io.File;
import javax.media.opengl.GL;

import pCSDT.Scripting.*;
import pCSDT.Presentation.OpenGL.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.texture.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 *
 * @author Ping Wang
 */
public class ObjectImage {
    int pm_id;
    boolean pm_visible = false;  // tell whether it should be drawn or not
    public String pm_color = "0,0,0";
    public float pm_xO = 0;//real x coordinate
    public float pm_yO = 0;
    public float pm_image_theta = 0;
    public float pm_vector_theta = 0;
    public float pm_cx = 200f;   //size
    public float pm_cy = 200f;
    public String pm_image_source;
    public boolean pm_vector;
    public float pm_nextXO = 0;
    public float pm_nextYO = 0;
    public float pm_ori_cx=0;
    public float pm_translate = 1.0f;
    public float pm_at_seconds;
    public float pm_delayed;
    public Texture pm_txt;
    public boolean pm_updated;
    public boolean pm_bSaying;
    public String pm_words2say;
    public boolean clear = false;
    public float after_seconds;
    public boolean pm_isDefault;
     


    /**
     * Create a plait image according to the input arguments
     */
    public ObjectImage(int id, float xO, float yO, float cx, float cy,
            float image_theta, float vector_theta, String color, String image_source, boolean vector, boolean visible, float at_seconds, Texture txt1, boolean updated, boolean bSaying, String word2say, boolean isDefault)
    {
        pm_id = id;
        pm_xO = xO;
        pm_yO = yO;
        pm_cx = cx;
        pm_cy = cy;
        pm_image_theta = image_theta;
        pm_vector_theta = vector_theta;
        pm_color = color;
        pm_image_source = image_source;
        pm_vector = vector;
        pm_visible = visible;
        pm_at_seconds = at_seconds;
        pm_txt = txt1;
        pm_updated = updated;
        pm_bSaying = bSaying;
        pm_words2say = word2say;
        pm_isDefault = isDefault;

    }

    /**
     * Set the vector of the plait image according to the input arguments
     */
   public void SetVector(float nextXO, float nextYO, float ori_cx,
           float translate, float vector_theta, float at_seconds)
   {
        pm_nextXO = nextXO;
        pm_nextYO = nextYO;
        pm_ori_cx=ori_cx;
        pm_translate = translate;
        pm_vector_theta = vector_theta;
        pm_at_seconds = at_seconds;
   }

   /**
     * Draw the plait image, called by Plait.Draw
     * txts is the hashmap stored in plait, for getting the texture of the image
     * using the image source's name
     */
  
    public void Draw(GL gl, GLU glu, int frame, double dt, Texture txt) {


        if (!this.pm_updated)
            OnLoadTextures(gl, glu);

        if (pm_visible) {
            CCUtil.DrawPlait(gl, glu, pm_xO, pm_yO, pm_cx/2, pm_cy/2, pm_image_theta,
                    pm_color, pm_txt, pm_image_source, pm_bSaying, pm_words2say, pm_isDefault);

            if(pm_vector)
                CCUtil.DrawVector(gl, pm_xO, pm_yO, pm_nextXO, pm_nextYO,
                            pm_cx/2, pm_ori_cx/2, pm_vector_theta, pm_translate);
        }
    }

    public void OnLoadTextures(GL gl, GLU glu) {

        pm_txt = null;
        String fileName = pm_image_source;
        //System.out.append("Texture Load");
        java.io.InputStream InputStream;
        
        try {   
                //System.out.println("Adding new texture\n");
                if (fileName != null && !fileName.equals("") && !fileName.equals("img/plaitColor.png")) {
                    pm_txt = TextureIO.newTexture(new File(fileName), false);
                    pm_txt.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    pm_txt.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    this.pm_updated = true;
                }
            }catch (Exception e) {
                fileName = "img/plaitColor.png";
                System.out.println(e.getMessage());
                System.out.println("Error loading texture " + pm_image_source);
            }
        
        try {
                if(fileName.equals("img/plaitColor.png")){
                    InputStream = CCEngine.class.getResourceAsStream(fileName);
                    pm_txt = TextureIO.newTexture(InputStream, false, ".png");
                    pm_txt.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    pm_txt.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
                    this.pm_updated = true;
                }
                
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error loading texture " + pm_image_source);
        }
    }


    /*
    private void DrawPlait(GL gl, float x, float y, float xP, float yP,
            float image_theta, String color) {
        double costheta = Math.cos(image_theta);
        double sintheta = Math.sin(image_theta);
        TextureCoords tc = null;
        if (pm_txt != null) {
            tc = pm_txt.getImageTexCoords();
        } else {
            System.err.println("In PlaitImage.DrawPlait, error in getImageTexCoords");
        }
        java.util.StringTokenizer st = new java.util.StringTokenizer(color, ",");
        int r_color = Integer.parseInt(st.nextToken());
        int g_color = Integer.parseInt(st.nextToken());
        int b_color = Integer.parseInt(st.nextToken());

        gl.glPushMatrix();
        gl.glMatrixMode(gl.GL_MODELVIEW);
        //gl.glScalef(0.5f, 0.5f, 1f);
        gl.glTranslatef(x, y, 0.0f);

        gl.glEnable(gl.GL_TEXTURE_2D);
        gl.glAlphaFunc(gl.GL_GREATER, 0.3f);
        gl.glEnable(gl.GL_ALPHA_TEST);
        gl.glEnable(gl.GL_MODULATE);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glTexEnvf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        gl.glDepthMask(false);

        pm_txt.enable();
        pm_txt.bind();
        gl.glBegin(gl.GL_QUADS);
        gl.glColor3f(r_color/255.0f, g_color/255.0f, b_color/255.0f);
        gl.glRotatef((float) 180, 1.0f, 0.0f, 0.0f);

        gl.glTexCoord2d(tc.left(), tc.top());
        gl.glVertex2f((float) (costheta * (-xP) - sintheta * (yP)),
                (float) (sintheta * (-xP) + costheta * (yP)));
        gl.glTexCoord2d(tc.right(), tc.top());
        gl.glVertex2f((float) (costheta * (xP) - sintheta * (yP)),
                (float) (sintheta * (xP) + costheta * (yP)));
        gl.glTexCoord2d(tc.right(), tc.bottom());
        gl.glVertex2f((float) (costheta * (xP) - sintheta * (-yP)),
                (float) (sintheta * (xP) + costheta * (-yP)));
        gl.glTexCoord2d(tc.left(), tc.bottom());
        gl.glVertex2f((float) (costheta * (-xP) - sintheta * (-yP)),
                (float) (sintheta * (-xP) + costheta * (-yP)));
        gl.glEnd();

        pm_txt.disable();
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glDisable(GL.GL_ALPHA);
        gl.glDisable(gl.GL_BLEND);
        gl.glDisable(gl.GL_TEXTURE_2D);
        gl.glDisable(gl.GL_DEPTH_TEST);
        gl.glDepthMask(true);

        gl.glPopMatrix();
    }

    private void DrawVector(GL gl, float x1, float y1, float x2, float y2,
            float xP, float ori_cx, float theta) {
        double cosDelta = Math.cos(theta + (float) Math.toRadians(25));
        double sinDelta = Math.sin(theta + (float) Math.toRadians(25));
        double cosMinusDelta = Math.cos(theta - (float) Math.toRadians(25));
        double sinMinusDelta = Math.sin(theta - (float) Math.toRadians(25));

        try {
            gl.glLineWidth((float) (6.0 * pm_translate * xP / ori_cx));
            gl.glPushMatrix();
            gl.glDepthFunc(GL.GL_LESS);//Should use Less, but why?
            gl.glEnable(gl.GL_DEPTH_TEST);
            gl.glDepthMask(true);

            gl.glBegin(gl.GL_LINES);
            gl.glColor3f(0.1f, 0.1f, 1.0f);
            gl.glVertex2f(x1, y1);
            gl.glVertex2f(x2, y2);
            gl.glEnd();

            //draw the arrow
            gl.glMatrixMode(gl.GL_MODELVIEW);
            gl.glTranslatef(x2, y2, 0.0f);

            gl.glBegin(gl.GL_LINES);
            gl.glColor3f(0.1f, 0.1f, 1.0f);
            gl.glVertex2f((float) (cosDelta * (0) - sinDelta * (0)),
                    (float) (sinDelta * (0) + cosDelta * (0)));
            gl.glVertex2f((float) (cosDelta * (-xP * pm_translate * 0.2) - sinDelta * (0)),
                    (float) (sinDelta * (-xP * pm_translate * 0.2) + cosDelta * (0)));
            gl.glVertex2f((float) (cosMinusDelta * (0) - sinMinusDelta * (0)),
                    (float) (sinMinusDelta * (0) + cosMinusDelta * (0)));
            gl.glVertex2f((float) (cosMinusDelta * (-xP * pm_translate * 0.2) - sinMinusDelta * (0)),
                    (float) (sinMinusDelta * (-xP * pm_translate * 0.2) + cosMinusDelta * (0)));

            gl.glEnd();

            //gl.glDisable(gl.GL_DEPTH_TEST);
            //gl.glDepthMask(false);
            gl.glPopMatrix();
        } catch (Exception e) {
            System.err.println("In PlaitImage.DrawVector, error: " + e);
        }
    }
*/

}