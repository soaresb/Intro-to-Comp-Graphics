
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class FishFood {
	//position of the food
	public float x, y, z;
	//variable keeping track of if the food has been eaten.  
	//If it has been it will be triggered to be removed from the foodlist
	public boolean isEaten;
	//list display name.  Needed when NewList is called
	public int food;
	//speed at which the food will fall when inserted at the top of the Vivarium
	private float fallSpeed;
	
	
	public FishFood() {
		Random rand = new Random();
		//picks random coordinates for the drop of the food
		//y value is constant since it drops in from the top of the tank
		x = rand.nextFloat()*2-1;
		y = 1.9f;
		z = rand.nextFloat()*2-1;
		
		fallSpeed = 0.02f;
		isEaten = false;
	}
	public void draw(GL2 gl) {
		gl.glPushMatrix();
	    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
	    gl.glTranslatef(x, y, z);
	    gl.glColor3f( 0.8f, 0.0f, 0.0f);
	    gl.glCallList( food );
	    gl.glPopAttrib();
	    gl.glPopMatrix();
	}
	
	public void init(GL2 gl) {
		GLUT glut = new GLUT();
		food = gl.glGenLists(1);
		gl.glNewList(food, GL2.GL_COMPILE);
		glut.glutSolidSphere(.11, 36, 24);
		gl.glEndList();
	}
	
	public void update(GL2 gl) {
		//if the food is not at the bottom of the tank
		//keep the food falling until it reaches the bottom of the tank
		if (y >= -1.9f) {
			y -= fallSpeed;
		}
	}
	

}
