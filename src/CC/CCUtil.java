package CC;





import com.sun.opengl.util.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.texture.*;





/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Utility functions in CC
 * @author Ping Wang
 */
public class CCUtil {
   
    /**
     * Load a texture from a file
     * @param fileName the file containing the texture
     * @return the Texture object
     */
    /*
    protected static Texture load(String fileName) {
        Texture text = null;
        java.io.InputStream fullpath = null;
        try {
            fullpath = CCUtil.class.getResourceAsStream(fileName);
            text = TextureIO.newTexture(fullpath, false, ".png");
            text.setTexParameterf(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
            text.setTexParameterf(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error loading texture " + fullpath);
        }
        return text;
    }
     */
     
      
     

    /**
     * Draw the image of a plait
     * x, y are the inner coordinate of the center of the plait
     * xP, yP are half of the length of the plait in the x and y dimensions
     * image_theta is the angle of the image in radian
     * color is a string containing the color of the image using RGB mode, e.g: "0,0,0"
     * txt is the texture of the image
     */
    protected static void DrawPlait(GL gl, GLU glu, float x, float y, float xP, float yP,
            float image_theta, String color, Texture txt, String img_source, boolean bSaying, String words2Say, boolean isDefault) {
        double costheta = Math.cos(image_theta);
        double sintheta = Math.sin(image_theta);
        GLUT glut = new GLUT();
         

        TextureCoords tc = null;
        if (txt != null) {
            tc = txt.getImageTexCoords();
        } else {
            System.err.println("In CCUtil.DrawPlait, error in getImageTexCoords");
        }
        //get the values of the red, green, blue colors from the string
        java.util.StringTokenizer st = new java.util.StringTokenizer(color, ",");
        int r_color = (int)Float.parseFloat(st.nextToken());
        int g_color = (int)Float.parseFloat(st.nextToken());
        int b_color = (int)Float.parseFloat(st.nextToken());

        gl.glPushMatrix();
        gl.glMatrixMode(gl.GL_MODELVIEW);
        //gl.glScalef(0.5f, 0.5f, 1f);
        gl.glTranslatef(x, y, 0.0f);

        gl.glEnable(gl.GL_TEXTURE_2D);
        gl.glAlphaFunc(gl.GL_GREATER, 0.3f);
        //no need to enable depth test
        //gl.glEnable(gl.GL_DEPTH_TEST);
        //gl.glDepthMask(true);
        gl.glEnable(gl.GL_ALPHA_TEST);
        gl.glEnable(gl.GL_BLEND);
        //using gl.GL_MODULATE does the trick for adding the input color to the texutre
        gl.glEnable(gl.GL_MODULATE);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glTexEnvf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
        gl.glDepthMask(false);

        txt.enable();
        txt.bind();
        gl.glBegin(gl.GL_QUADS);
        
        //gl.glColor3f(255, 255, 255);

        //convert RGB color (from 0 to 255) to OpenGL color (from 0 to 1)
        if(!isDefault){
           //gl.glColor3f(255, 255, 255);
           gl.glColor3f(r_color, g_color, b_color);
        }else{
            gl.glColor3f(r_color / 255.0f, g_color / 255.0f, b_color / 255.0f);
        }
        
        //rotate 180 degree so the image is not upside down
        //gl.glRotatef((float) 180, 1.0f, 0.0f, 0.0f);

        gl.glTexCoord2d(tc.left(), tc.top());
        //rotate the iamge by image_theta
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

        txt.disable();
        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
        gl.glDisable(gl.GL_MODULATE);
        gl.glDisable(GL.GL_ALPHA);
        gl.glDisable(gl.GL_BLEND);
        gl.glDisable(gl.GL_TEXTURE_2D);
        //gl.glDisable(gl.GL_DEPTH_TEST);
        //gl.glDepthMask(true);
       // gl.glColor3f(255,255,255);
        txt.disable();

            gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
            ////gl.glDisable(GL.GL_ALPHA);
            ////gl.glDisable(gl.GL_BLEND);
            gl.glDisable(gl.GL_TEXTURE_2D);
            
        
        if (bSaying && words2Say != null) {
                gl.glColor3f(0f, 0f, 0f);
                gl.glRasterPos2f(0.4f, 0.4f);
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, words2Say);
                
            }

        gl.glPopMatrix();
    }

    /**
     * Draw the vector of a plait
     * x1, y1 are the starting coordinate of vector
     * x2, y2 are the ending coordinate of vector
     * xP is half of the length of the plait in the x dimension
     * ori_cx is half of the original length of the plait in the x dimension
     * theta is the angle of the vector in radian
     * translate is the percentage by which the plait moves
     */
    protected static void DrawVector(GL gl, float x1, float y1, float x2, float y2,
            float xP, float ori_cx, float theta, float translate) {
        //for drawing the arrow of the vector
        double cosDelta = Math.cos(theta + (float) Math.toRadians(25));
        double sinDelta = Math.sin(theta + (float) Math.toRadians(25));
        double cosMinusDelta = Math.cos(theta - (float) Math.toRadians(25));
        double sinMinusDelta = Math.sin(theta - (float) Math.toRadians(25));

        try {
            //setting line width according to translate and dilate properties
            gl.glLineWidth((float) (6.0 * translate * xP / ori_cx));
            gl.glPushMatrix();
            gl.glDepthFunc(GL.GL_LESS);//Should use Less, but why?
            gl.glEnable(gl.GL_DEPTH_TEST);
            gl.glDepthMask(true);

            //draw the line of the vector
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
            ////rotate +delta
            gl.glVertex2f((float) (cosDelta * (0) - sinDelta * (0)),
                    (float) (sinDelta * (0) + cosDelta * (0)));
            gl.glVertex2f((float) (cosDelta * (-xP * translate * 0.2) - sinDelta * (0)),
                    (float) (sinDelta * (-xP * translate * 0.2) + cosDelta * (0)));
            //rotate -delta
            gl.glVertex2f((float) (cosMinusDelta * (0) - sinMinusDelta * (0)),
                    (float) (sinMinusDelta * (0) + cosMinusDelta * (0)));
            gl.glVertex2f((float) (cosMinusDelta * (-xP * translate * 0.2) - sinMinusDelta * (0)),
                    (float) (sinMinusDelta * (-xP * translate * 0.2) + cosMinusDelta * (0)));

            gl.glEnd();

            //do not disable depth, orelse the vector can't be seen
            //gl.glDisable(gl.GL_DEPTH_TEST);
            //gl.glDepthMask(false);
            gl.glPopMatrix();
        } catch (Exception e) {
            System.err.println("In CCUtil.DrawVector, error: " + e);
        }
    }


}
