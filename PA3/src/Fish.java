
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Fish {
	private GLUT glut;
	//position of the fish currently
	public float x, y, z;
	//prev position of the fish
	public float prevx, prevy, prevz;
	//radius of bounding sphere used for collision purposes
	public float boundingR;
	//identifiers
	private int fish;
	private int tail;
	private int body;
	//variable tracking if the fish is alive or dead
	public boolean dead;
	//variable containing the direction of the fish's movement
	private float dirx;
	private float diry;
	private float dirz;
	
	private Vivarium v;
	//holds the shark that is attacking.  Will be set when the vivarium is created
	private Shark predator = null;
	//speed of the fish in the 3 different directions
	private float speedx;
	private float speedy;
	private float speedz;
	//holds the scalar potential values of of the shark
	private float sharkPotential;
	private float wallPotential;
	private float foodPotential;
	
	//angles, speeds, and directions needed to the animation of the fish's movement
	private float tailAngle;
	private float tailSpeed;
	private float tailDirection;
	private float bodySpeed;
	private float bodyAngle;

	private Random rand;
	
	
	public Fish(float _x, float _y, float _z, float _tailSpeed, Vivarium _v) {
		glut = new GLUT();
		rand = new Random();
		x = prevx = _x;
		y = prevy = _y;
		z = prevz = _z;
		tailSpeed = _tailSpeed;
		v = _v;
		
		sharkPotential = 0.26f;
		wallPotential = 0.08f;
		foodPotential = -0.15f;
		tailAngle = bodyAngle = 0;
		tailDirection = 1;
		bodySpeed = tailSpeed / 4;
		
		fish = tail = body = 0;
		dirx = rand.nextFloat();
		diry = rand.nextFloat();
		dirz = rand.nextFloat();
		speedx = 0.02f;
		speedy = 0.02f;
		speedz = 0.02f;
		
		dead = false;
	}

	public void init(GL2 gl) {
		createBody(gl);
		createTail(gl);
		fish = gl.glGenLists(1);
		gl.glNewList(fish, GL2.GL_COMPILE);
		displayList(gl);
		gl.glEndList();
	}

	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glPushAttrib(gl.GL_CURRENT_BIT);
		//nemo orange
		gl.glColor3f(1.0f,0.5317f, 0f ); 
		gl.glCallList(fish);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}

	public void update(GL2 gl) {
		distance(gl);
		potential();
		translate();
		if (!dead) animate();
		gl.glNewList(fish, GL2.GL_COMPILE);
		displayList(gl);
		gl.glEndList();
	}
	//helper function to calc the cross product of 2 vectors
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
	private void createBody(GL2 gl) {
		body = gl.glGenLists(1);
		gl.glNewList(body, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glScalef(0.4f, 0.6f, 1);
		gl.glTranslatef(0, 0, -0.09f);
		glut.glutSolidSphere(0.2, 36, 24);
		gl.glPopMatrix();
		gl.glEndList();
	}

	private void createTail(GL2 gl) {
		tail = gl.glGenLists(1);
		gl.glNewList(tail, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glScalef(0.5f, 1, 1);
		gl.glTranslatef(0, 0, 0.35f);
		gl.glRotatef(-180, 0, 1, 0);
		glut.glutSolidCone(0.1, 0.35, 20, 20);
		gl.glEnd();
		gl.glPopMatrix();
		gl.glEndList();
	}
	private void displayList(GL2 gl) {
		float dx = prevx - x;
		float dy = 0.0f;
		float dz = prevz - z;

		float mag = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		float[] v = new float[3];
		v[0] = dx / mag;
		v[1] = dy / mag;
		v[2] = dz / mag;

		// unit vector in the y direction
		float[] yHat = { 0.0f, 1.0f, 0.0f };
		float[] u = { v[1] * yHat[2] - yHat[1] * v[2],
				v[0] * yHat[2] - yHat[0] * v[2], v[0] * yHat[1] - yHat[0] * v[1] };
		
		normalizeVector(u);

		//v cross u
		float[] n = { u[1] * v[2] - v[1] * u[2],
				u[0] * v[2] - v[0] * u[2],
				u[0] * v[1] - v[0] * u[1] };
		normalizeVector(n);
		float[] rotationMatrix = { u[0], u[1], u[2], 0.0f, n[0],
				n[1], n[2], 0.0f, v[0], v[1], v[2], 0.0f, x, y, z,
				1.0f };
		gl.glMultMatrixf(rotationMatrix, 0);
		//puts the fish on its side showing that it is dead
		if (dead) {
			gl.glRotatef(-90, 0, 0, 1);
		}
		
		// rotates the tail
		gl.glPushMatrix();
		gl.glRotatef(tailAngle, 0, 1, 0);
		gl.glCallList(tail);
		gl.glPopMatrix();
		
		// Rotates the body
		gl.glPushMatrix();
		gl.glRotatef(bodyAngle, 0, 1, 0);
		gl.glCallList(body);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
	}
	//animates the movement of the fish
	private void animate() {
		tailAngle += tailSpeed * tailDirection;
		bodyAngle += bodySpeed * tailDirection * -1;

		if (tailAngle > 10 || tailAngle < -10) {
			tailDirection *= -1;
		}
	}
	
	//sums the potential of all things causing it to react.  Then sets the proper values for the new direction of the fish
	private void potential() {
		Coord p = new Coord(x,y,z);
		
		Coord q2 = new Coord(1.9, y, z);
		Coord q3 = new Coord(-1.9, y, z);
		Coord q4 = new Coord(x, 1.9, z);
		Coord q5 = new Coord(x, -1.9, z);
		Coord q6 = new Coord(x, y, 1.9);
		Coord q7 = new Coord(x, y, -1.9);
		Coord q1 = new Coord(predator.x, predator.y, predator.z);
		Coord[] coords = {potentialFunction(p,q1,sharkPotential), potentialFunction(p,q2, wallPotential), 
				potentialFunction(p,q3,wallPotential), potentialFunction(p,q4,wallPotential),
				potentialFunction(p,q5,wallPotential), potentialFunction(p,q6,wallPotential), 
				potentialFunction(p,q7,wallPotential)};
		Coord sum = add(coords);
		for (FishFood f : v.food) {
			Coord q = new Coord(f.x, f.y, f.z);
			q = potentialFunction(p, q, foodPotential);
			Coord[] m = {sum, q};
			sum = add(m);
		}
		dirx += sum.x;
		diry += sum.y;
		dirz += sum.z;
		
	}	
	
	//potential function from class
	private Coord potentialFunction(Coord p, Coord q1, float scale) {
		float x = (float) (scale*(p.x - q1.x)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		float y = (float) (scale*(p.y - q1.y)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		float z = (float) (scale*(p.z - q1.z)*Math.pow(Math.E,-1*(Math.pow((p.x-q1.x), 2) + Math.pow((p.y-q1.y), 2) + Math.pow((p.z-q1.z), 2)) ));
		Coord potential = new Coord(x, y, z);
		return potential;
	}
	//functoin to calculate if the fish is close enough to the food to eat it
	//also to see if the shark is close enough to eat the fish
	private void distance(GL2 gl){
		Coord a = new Coord(x,y,z);
		for (FishFood f : v.food) {
			Coord b = new Coord(f.x,f.y,f.z);
			if (distance(a, b) < 0.5) {
				gl.glDeleteLists(f.food, 1);
				f.isEaten = true;
			}
		}
		
		Coord shark = new Coord(predator.x*predator.scale, predator.y*predator.scale, predator.z*predator.scale);
		if (distance(a, shark) < predator.boundingR) {
			
			predator.removePrey();
			dead = true;
		}
	}
	
	//flip direction of the fish when it is about to be out of the tank
	private void translate() {

		prevx = x;
		prevy = y;
		prevz = z;
		x += speedx * dirx;
		y += speedy * diry;
		z += speedz * dirz;
		
		float n = rand.nextFloat();
		while (n < 0.2f) {
			n = rand.nextFloat();
		}
		if (x > 1.8 || x < -1.8) {
			// if n is not large enough to pull it below the 
			// constraints of the tank, it will get stuck, so
			// I am setting x at the constraint so it won't get
			// stuck.
			x = 1.8f;
			if (dirx > 0){
				dirx = -1 * n;
			} else {
				x *= -1;
				dirx = n;
			}
		}
		if (y > 1.9 || y < -1.9) {
			y = 1.9f;
			if (diry > 0){
				diry = -1 * n;
			} else {
				y *= -1;
				diry = n;
			}
		}
		if (z > 1.9 || z < -1.9) {
			z = 1.9f;
			if (dirz > 0){
				dirz = -1 * n;
			} else {
				z*= -1;
				dirz = n;
			}
		}
		}

	
	public void addPredator(Shark s) {
		predator = s;
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
	
	private float distance(Coord a, Coord b) {
		return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
	}
	
	
}


