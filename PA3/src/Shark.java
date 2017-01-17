
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Shark {
	private GLUT glut;
	//position of the shark currently
	public float x, y, z; 
	//previous position of the shark
	public float prevx, prevy, prevz;
	//radius of bounding sphere used for collision purposes
	public float boundingR;
	//identifiers 
	private int shark;
	private int tail;
	private int body;
	private int fin;
	

	public float scale;
	
	//variable containing the direction of the shark's movement
	private float dirx;
	private float diry;
	private float dirz;
	
	private Vivarium v;
	
	//holds the fish the shark is attacking
	//will be set when the vivarium is created
	private Fish prey = null;
	//speed of the shark in the 3 different directions
	private float speedx;
	private float speedy;
	private float speedz;
	
	//holds the scalar potential values of of the shark
	private float fishPotential;
	private float wallPotential;
	
	//angles, speeds, and directions needed to the animation of the shark's movement
	private float tailAngle;
	private float tailSpeed;
	private float tailDirection;
	private float bodySpeed;
	private float bodyAngle;

	private Random rand;
	

	public Shark(float _x, float _y, float _z, float _scale, float _tailSpeed) {
		glut = new GLUT();
		rand = new Random();
		x = prevx = _x;
		y = prevy = _y;
		z = prevz = _z;
		scale = _scale;
		tailSpeed = _tailSpeed;
		
		
		fishPotential = -.5f;
		wallPotential = 0.08f;
		tailAngle = bodyAngle = 0;
		tailDirection = 1;
		bodySpeed = tailSpeed / 4;
		
		shark = tail = body = 0;
		dirx = rand.nextFloat();
		diry = rand.nextFloat();
		dirz = rand.nextFloat();
		speedx = 0.02f;
		speedy = 0.02f;
		speedz = 0.02f;

		boundingR = 0.35f * scale;

	}
	public void init(GL2 gl){
		createBody(gl);
		createTail(gl);
		createFin(gl);
		shark = gl.glGenLists(1);
		gl.glNewList(shark, GL2.GL_COMPILE);
		displayList(gl);
		gl.glEndList();
	}
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glPushAttrib(gl.GL_CURRENT_BIT);
		gl.glColor3f(.223f,.223f,.81f); 
		gl.glCallList(shark);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	public void update(GL2 gl) {
		potential();
		distance(gl);
		translate();
		animate();
		gl.glNewList(shark, GL2.GL_COMPILE);
		displayList(gl);
		gl.glEndList();
	}
	//helper function for the crossproduct of 2 vectors
	private void crossProduct(float vector1[],float vector2[],float[] vector3){
		float [] cross = {vector2[1]*vector3[2] - vector3[1]*vector2[2],
				vector2[0]*vector3[2] - vector3[0]*vector2[2],
				vector2[0]*vector3[1] - vector3[0]*vector2[1]
		};
		vector1 = cross;
	}
	//helper function to normalize a vector
	private void normalizeVector(float vec1[]){
		float magnitude = (float)Math.sqrt(vec1[0]*vec1[0] + vec1[1]*vec1[1] + vec1[2]*vec1[2]);
		vec1[0] = vec1[0]/magnitude;
		vec1[1] = vec1[1]/magnitude;
		vec1[2] = vec1[2]/magnitude;
	}
	
	private void createFin(GL2 gl) {
		fin = gl.glGenLists(1);
		gl.glNewList(fin, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glScalef(0.6f, 1.2f, 1);
		gl.glTranslatef(0, 0, -0.14f);
		gl.glRotatef(-75, 1, 0, 0);
		glut.glutSolidCone(0.1, 0.3, 20, 20);
		gl.glPopMatrix();
		gl.glEndList();
	}
	private void createBody(GL2 gl) {
		body = gl.glGenLists(1);
		gl.glNewList(body, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glScalef(0.6f, 0.6f, 1.2f);
		gl.glTranslatef(0, 0, -.1f);
		glut.glutSolidSphere(0.2, 36, 24);
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createTail(GL2 gl) {
		tail = gl.glGenLists(1);
		gl.glNewList(tail, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glScalef(0.6f, 1f, 1f);
		gl.glTranslatef(0, 0, 0.35f);
		gl.glRotatef(-180, 0, 1, 0);
		glut.glutSolidCone(0.07, 0.4, 20, 20);

		gl.glEnd();
		gl.glPopMatrix();
		gl.glEndList();
	}
	//displaylist for the parts of the shark
	private void displayList(GL2 gl) {
		gl.glPushMatrix();
		gl.glScalef(scale, scale, scale);

		float dx = prevx - x;
		
		float dy = 0.0f;
		float dz = prevz - z;

		float magnitude = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		float[] v = new float[3];
		v[0] = dx / magnitude;
		v[1] = dy / magnitude;
		v[2] = dz / magnitude;

		// unit vector y axis
		float[] yHat = { 0.0f, 1.0f, 0.0f };
		float[] u = { v[1] * yHat[2] - yHat[1] * v[2],
				v[0] * yHat[2] - yHat[0] * v[2], v[0] * yHat[1] - yHat[0] * v[1] };
		normalizeVector(u);
		//v cross u
		float[] n = { v[1] * u[2] - u[1] * v[2],
				v[0] * u[2] - u[0] * v[2],
				v[0] * u[1] - u[0] * v[1] };
		
		normalizeVector(n);
		float[] rotationMatrix = { u[0], u[1], u[2], 0.0f, n[0],
				n[1], n[2], 0.0f, v[0], v[1], v[2], 0.0f, x, y, z,
				1.0f };
		gl.glMultMatrixf(rotationMatrix, 0);

		// Rotates the tail
		gl.glPushMatrix();
		gl.glRotatef(tailAngle, 0, 1, 0);
		gl.glCallList(tail);
		gl.glPopMatrix();
		
		// Rotates the body
		gl.glPushMatrix();
		gl.glRotatef(bodyAngle, 0, 1, 0);
		gl.glCallList(body);
		gl.glPopMatrix();

		
		gl.glPushMatrix();
		gl.glCallList(fin);
		gl.glPopMatrix();

		gl.glPopMatrix();
	}
	//animates the movement of the shark
	private void animate() {
		tailAngle += tailSpeed * tailDirection;
		bodyAngle += bodySpeed * tailDirection * -1;

		if (tailAngle > 10 || tailAngle < -10) {
			tailDirection *= -1;
		}
	}
	

	//potential functoin from class
	private Coord potentialFunction(Coord p, Coord q1, float scale) {
		float x = (float) (scale*(p.x - q1.x)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		float y = (float) (scale*(p.y - q1.y)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		float z = (float) (scale*(p.z - q1.z)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		Coord potential = new Coord(x, y, z);
		return potential;
	}
	private void distance(GL2 gl) {
		Coord a = new Coord(x*scale,y*scale,z*scale);
	}
	
	//sums up all the potentials on the fish to decide the next direction
	private void potential() {
		Coord p = new Coord(x,y,z);
		Coord q1;
		if (prey != null) q1 = new Coord(prey.x, prey.y, prey.z);
		else q1 = new Coord(100000,100000,100000);
		Coord q2 = new Coord(1.8, y, z);
		Coord q3 = new Coord(-1.8, y, z);
		Coord q4 = new Coord(x, 1.8, z);
		Coord q5 = new Coord(x, -1.8, z);
		Coord q6 = new Coord(x, y, 1.8);
		Coord q7 = new Coord(x, y, -1.8);
		Coord[] coords = {potentialFunction(p,q1,fishPotential), potentialFunction(p,q2, wallPotential), 
				potentialFunction(p,q3,wallPotential), potentialFunction(p,q4,wallPotential),
				potentialFunction(p,q5,wallPotential), potentialFunction(p,q6,wallPotential), 
				potentialFunction(p,q7,wallPotential)};
		Coord sum = add(coords);
		dirx += sum.x;
		diry += sum.y;
		dirz += sum.z;
		
	}
	//
	private void translate() {
		prevx = x;
		prevy = y;
		prevz = z;
		x += speedx * dirx;
		y += speedy * diry;
		z += speedz * dirz;

		float n = rand.nextFloat();
		while (n < 0.25f) {
			//make a large enough offset to compensate for the shark being bigger
			n = rand.nextFloat();
		}
		if (x > 1.5  || x < -1.5 / scale) {
			x = 1.5f / scale;
			if (dirx > 0) {
				dirx = -1 * n;
			} else {
				x *= -1;
				dirx = n;
			}
		}
		if (y > 1.5 / scale || y < -1.5 / scale) {
			y = 1.7f / scale;
			if (diry > 0) {
				diry = -1 * n;
			} else {
				y *= -1;
				diry = n;
			}
		}
		if (z > 1.5 / scale || z < -1.5 / scale) {
			z = 1.5f / scale;
			if (dirz > 0) {
				dirz = -1 * n;
			} else {
				z *= -1;
				dirz = n;
			}
		}

	}
	public void addPrey(Fish f) {
		prey = f;
	}
	
	//removes fish, continues to move as if it is alone in tank
	public void removePrey() {
		prey = null;
	}
	
	private Coord add(Coord[] b) {
		Coord ret = new Coord();
		for (Coord a : b) {
			ret.x += a.x;
			ret.y += a.y;
			ret.z += a.z;
		}
		return ret;
	}
}