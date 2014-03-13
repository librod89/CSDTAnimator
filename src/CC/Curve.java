/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CC;

import pCSDT.Scripting.*;
import pCSDT.Presentation.OpenGL.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

/**
 *
 * @author Tanushree
 */
public class Curve extends PObjectOgl{
	PProperty[] m_props;
	PMethod[] m_methods;
	@AutomatableProperty(name = "Length", desc = "length of the rectangular obstacle")
	public float m_length = 4f;
	@AutomatableProperty(name = "Iteration", desc = "number of links")
	public int Iteration = 3;
	@AutomatableProperty(name = "Width", desc = "width of the rectangular obstacle")
	public float m_width = .1f;
	@AutomatableProperty(name = "x_Start", desc = "Start x axis")
	public int x_Start = 0;
	@AutomatableProperty(name = "y_Start", desc = "Start y axis")
	public int y_Start = 0;
	@AutomatableProperty(name = "Slope", desc = "Staring angle in degree")
	public float m_slope = 0;
	@AutomatableProperty(name = "Starting_dilation", desc = "Starting dilation in percentage")
	public float m_starting_dilation = 1; //internally stored is Not in percentage
	@AutomatableProperty(name = "X Reflection", desc = "Reflection about X-axis")
	public int m_x_reflection = 0;
	@AutomatableProperty(name = "Y Reflection", desc = "Reflection about Y-axis")
	public int m_y_reflection = 0;
        @AutomatableProperty(name = "Rotate", desc = "Rotate angle in degree per Iteration")
	public float m_rotate = 0;
	@AutomatableProperty(name = "Vector", desc = "Flag for showing/hiding the Vector")
	public boolean m_vector = true;
	@AutomatableProperty(name = "Translate", desc = "Translate in percentage per Iteration")
	public float m_translate = 1; //internally stored is Not in percentage
	@AutomatableProperty(name = "Dilate", desc = "Dilate in percentage per Iteration")
	public float m_dilate = 1; //internally stored is Not in percentage
    	@AutomatableProperty(name = "RedColor", desc = "How red the braid is")
	public float m_r = 0f;
    	@AutomatableProperty(name = "GreenColor", desc = "How green the braid is")
	public float m_g = 0f;
        @AutomatableProperty(name = "BlueColor", desc = "How blue the braid is")
	public float m_b = 0f;

	public Texture m_txt = null;
	public float m_cx = 200f;
	public float m_cy = 200f;
	//float r, g, b;
	float size = 1f;

	/**
	 * default class constructor
	 * @param name name of the Line Object
	 * @param desc description of the Line Object
	 */
	public Curve(String name, String desc) {
		super(name, desc);
		//    x1 = 2; y1 = 7;
		//    x2 = 5; y2 = 7;
		//m_r = m_g = m_b = 0;
	}

        public Curve(String name, String desc, int it, int x, int y, float slope, Texture texture){
            	super(name, desc);
		Iteration = it;
		x_Start = x;
		y_Start = y;
		m_slope = slope;
                m_txt=texture;
        }

	/*public Curve(String name, String desc, int it, int x, int y, int slope,
			Texture texture) {

		super(name, desc);
		Iteration = it;
                //start parameters
		x_Start = x;
		y_Start = y;
		m_slope = slope;
		//m_starting_dilation = starting_dilation;
                m_txt=texture;
		//per iteration parameters
		//m_rotate = rotate;
		//m_translate = translate / 100;
		//m_dilate = dilate / 100;
	}
        */

        public Curve Duplicate(Curve orig_curve, int next_id){
                Curve new_curve = new Curve(Integer.toString(next_id),"Braid No"+ next_id,
                        orig_curve.Iteration, orig_curve.x_Start, orig_curve.y_Start, orig_curve.m_slope,
                        orig_curve.m_txt);
                new_curve.SetStartingDilate(orig_curve.m_starting_dilation);
                new_curve.Rotate(orig_curve.m_rotate);
                new_curve.TranslateByPercent(orig_curve.m_translate);
                new_curve.Dilate(orig_curve.m_dilate);
                return new_curve;
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
		//DrawOval(gl, 0,0, 1,0, 0, 0);

		DrawBraid(gl, x_Start * 100, y_Start * 100, 1, 0, 0, 0);


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

		return HTResult.Miss;

	}

        public void SetStartingDilate(float starting_dilate){
            m_starting_dilation = starting_dilate;
        }

        public void TranslateByPercent(float translate){
            m_translate= translate;
        }

        public void Dilate(float dilate){
            m_dilate= dilate;
        }

        public void Rotate(float rotate){
            m_rotate = rotate;
        }

         public void Reflect(int x_reflection, int y_reflection){
            m_x_reflection=x_reflection;
            m_y_reflection=y_reflection;
        }

        public void MoveTo(int x, int y){
            x_Start=x;
            y_Start=y;
        }

        public void SetColor(float r, float g, float b){
            m_r=r;
            m_g=g;
            m_b=b;
        }

	private void DrawPlait(GL gl, float x, float y, float xP, float yP,
			double costheta, double sintheta, TextureCoords tc) {
		//float xP = m_cx / 4;//m_cx / 2;
		//float yP = m_cy / 4;//m_cy / 2;

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

		m_txt.enable();
		m_txt.bind();
		gl.glBegin(gl.GL_QUADS);
		gl.glColor3f(m_r, m_g, m_b);
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

		m_txt.disable();
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		gl.glDisable(GL.GL_ALPHA);
		gl.glDisable(gl.GL_BLEND);
		gl.glDisable(gl.GL_TEXTURE_2D);
		gl.glDisable(gl.GL_DEPTH_TEST);
		gl.glDepthMask(true);

		gl.glPopMatrix();
	}

	private void DrawVector(GL gl, float x1, float y1, float x2, float y2,
			float xP, float theta) {

		double cosDelta = Math.cos(theta + (float) Math.toRadians(25));
		double sinDelta = Math.sin(theta + (float) Math.toRadians(25));
		double cosMinusDelta = Math.cos(theta - (float) Math.toRadians(25));
		double sinMinusDelta = Math.sin(theta - (float) Math.toRadians(25));

		gl.glLineWidth((float) (6.0 * m_translate * xP / m_cx));
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
		gl.glVertex2f((float) (cosDelta * (-xP * m_translate * 0.2) - sinDelta * (0)),
				(float) (sinDelta * (-xP * m_translate * 0.2) + cosDelta * (0)));
		gl.glVertex2f((float) (cosMinusDelta * (0) - sinMinusDelta * (0)),
				(float) (sinMinusDelta * (0) + cosMinusDelta * (0)));
		gl.glVertex2f((float) (cosMinusDelta * (-xP * m_translate * 0.2) - sinMinusDelta * (0)),
				(float) (sinMinusDelta * (-xP * m_translate * 0.2) + cosMinusDelta * (0)));

		gl.glEnd();

		//gl.glDisable(gl.GL_DEPTH_TEST);
		//gl.glDepthMask(false);
		gl.glPopMatrix();
	}

	/**
	 * This method helps to draw an oval.
	 * @param gl the GL object
	 * @param x x-coordinate of the center
	 * @param y y-coordinate of the center
	 * @param size the size of the oval
	 * @param r red component
	 * @param g green component
	 * @param b blue component
	 */
	private void DrawBraid(GL gl, float x, float y, float size,
			float r, float g, float b) {

            float reflected_x=0, reflected_y=0;
            float reflected_vector_angle=0, reflected_image_angle=0, reflected_rotate=0;

            // Degeneracy = do not want
		if (m_length == 0.0f || m_width == 0.0f) {
			return;
		}

		////int mct = (int) (1 / this.GetPEngine().m_uniscale);
		////mct = (mct / 2) / 100 * 100;

                //Do the reflection
                if(m_x_reflection==0 && m_y_reflection==0)
                {
                    reflected_x=x;
                    reflected_y=y;
                    reflected_image_angle=m_slope;
                    reflected_vector_angle=m_slope;
                    reflected_rotate= m_rotate;
                }
                else if(m_x_reflection==1 && m_y_reflection==0)
                {
                    reflected_x=x;
                    reflected_y=-y;
                    reflected_image_angle=90-m_slope;
                    reflected_vector_angle=-m_slope;
                    reflected_rotate= -m_rotate;
                }
                else if(m_x_reflection==0 && m_y_reflection==1)
                {
                    reflected_x=-x;
                    reflected_y=y;
                    reflected_image_angle=-90-m_slope;
                    reflected_vector_angle=180-m_slope;
                    reflected_rotate= -m_rotate;
                }
                else if(m_x_reflection==1 && m_y_reflection==1)
                {
                    reflected_x=-x;
                    reflected_y=-y;
                    reflected_image_angle=m_slope-180;
                    reflected_vector_angle=m_slope+180;
                    reflected_rotate= m_rotate;
                }

		float start_image_theta = (float) Math.toRadians(reflected_image_angle);
 		float start_vector_theta = (float) Math.toRadians(reflected_vector_angle);
		float rotate_theta = (float) Math.toRadians(reflected_rotate);
		float cur_image_theta = 0f, cur_vector_theta = 0f;
		double image_costheta = 0, vector_costheta = 0;
		double image_sintheta = 0,  vector_sintheta = 0;
		float xP = 0f;
		float yP = 0f;
		float xO = 0f, nextXO = 0f;
		float yO = 0f, nextYO = 0f;

		//if (txt == null) {
		//	txt = load("img/plaitWhite.png");
		//}
		TextureCoords tc = m_txt.getImageTexCoords();
		xO = nextXO = reflected_x;
		yO = nextYO = reflected_y;
                xP = m_cx * m_starting_dilation;
		yP = m_cy * m_starting_dilation;
                //vector
		cur_vector_theta = start_vector_theta;
                vector_costheta = Math.cos(cur_vector_theta);
		vector_sintheta = Math.sin(cur_vector_theta);
                //image
                cur_image_theta = start_image_theta;
		image_costheta = Math.cos(cur_image_theta);
		image_sintheta = Math.sin(cur_image_theta);
		
		for (int i = 0; i < Iteration; i++) {			
                        DrawPlait(gl, xO, yO, xP / 2, yP / 2, image_costheta, image_sintheta, tc);
			//vector
                        cur_vector_theta += rotate_theta;
			vector_costheta = Math.cos(cur_vector_theta);
			vector_sintheta = Math.sin(cur_vector_theta);
                        //image
			cur_image_theta += rotate_theta;
			image_costheta = Math.cos(cur_image_theta);
			image_sintheta = Math.sin(cur_image_theta);
                        //next centor position
			nextXO = xO + (float) (xP * m_translate * vector_costheta);
			nextYO = yO + (float) (yP * m_translate * vector_sintheta);
			if (m_vector) {
				DrawVector(gl, xO, yO, nextXO, nextYO, xP, cur_vector_theta);
			}
			xO = nextXO;
			yO = nextYO;
			xP *= m_dilate;
			yP *= m_dilate;
		}
	}




	/**
	 * This method helps to draw an oval.
	 * @param gl the GL object
	 * @param x x-coordinate of the center
	 * @param y y-coordinate of the center
	 * @param size the size of the oval
	 * @param r red component
	 * @param g green component
	 * @param b blue component
	 */
	private void DrawOval(GL gl, float x, float y, float size, float r, float g, float b) {
		// Degeneracy = do not want
		if (m_length == 0.0f || m_width == 0.0f) {
			return;
		}


		////int mct = (int) (1 / this.GetPEngine().m_uniscale);
		////mct = (mct / 2) / 100 * 100;
		gl.glPushMatrix();
		float theta = (float) Math.toRadians(m_slope);
		// Draw our line:
		gl.glLineWidth(2.0f);

		if (Iteration != 0) {
			for (int i = 0; i < Iteration; i++) {


				if ((m_slope >= 0 && m_slope <= 45) || (m_slope >= 315 && m_slope <= 360)) {
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + (100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (0 + i * 200) * ((float) Math.cos(theta)), 100 + y_Start * 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (-50 + i * 200) * ((float) Math.cos(theta)), 100 + y_Start * 100 + (-50 + i * 200) * ((float) Math.sin(theta)));

					gl.glEnd();

					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + (-100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (-100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (-50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (-50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + -20 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (40 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 20 + (40 + i * 200) * ((float) Math.sin(theta)));



					gl.glEnd();
				} else if ((m_slope >= 135 && m_slope < 225)) {
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + 100 + (50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (-100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 - 100 + (-100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + (-50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 - 100 + (-50 + i * 200) * ((float) Math.sin(theta)));


					gl.glEnd();

				} else if (m_slope > 45 && m_slope < 135) {
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + -100 + (100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + -100 + (50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 100 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 100 + (-50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-50 + i * 200) * ((float) Math.sin(theta)));

					gl.glEnd();
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 - 100 + (-100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 - 100 + (-50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 20 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + -20 + (40 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (40 + i * 200) * ((float) Math.sin(theta)));



					gl.glEnd();
				} else {
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + 100 + (-100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 100 + (-50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 - 100 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 - 100 + (50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (50 + i * 200) * ((float) Math.sin(theta)));

					gl.glEnd();
					gl.glBegin(gl.GL_QUAD_STRIP);
					// Draw x- and y-axes:
					gl.glColor3f(0.0f, 0.0f, 0.0f);
					gl.glVertex2d(x_Start * 100 + 100 + (100 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (100 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 100 + (50 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (50 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 - 20 + (0 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (0 + i * 200) * ((float) Math.sin(theta)));
					gl.glVertex2d(x_Start * 100 + 20 + (-40 + i * 200) * ((float) Math.cos(theta)), y_Start * 100 + (-40 + i * 200) * ((float) Math.sin(theta)));



					gl.glEnd();

				}

				gl.glPopMatrix();
			}
		}
	}
}
