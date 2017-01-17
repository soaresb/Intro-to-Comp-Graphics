/**
 * PA2.java - driver for the hand model simulation
 * 
 * History:
 * 
 * 19 February 2011
 * 
 * - added documentation
 * 
 * (Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>)
 * 
 * 16 January 2008
 * 
 * - translated from C code by Stan Sclaroff
 * 
 * (Tai-Peng Tian <tiantp@gmail.com>)
 * 
 * 
 * 18 October 2016
 * 
 * - Project modified using old resources to depict a spider and its movements
 * the spider has 8 legs an palm like body and a circular head.
 * Pressing T gives 5 examples of test positions that the spider can be in
 * - Files modified from original - PA2, TestCases, Palm
 * - Files added - Head.java
 * 
 * (Brian Griffin Soares <soaresb@bu.edu>)
 * 
 */


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

/**
 * The main class which drives the hand model simulation.
 * 
 * @author Tai-Peng Tian <tiantp@gmail.com>
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2008
 */
public class PA2 extends JFrame implements GLEventListener, KeyListener,
    MouseListener, MouseMotionListener {

  /**
   * A Leg which has a palm joint, a middle joint, a distal joint, and a foot joint.
   * 
   * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
   * @since Spring 2011
   * MODIFIED BY Brian Soares <soaresb@bu.edu> OCTOBER 2016
   */
  private class Leg {
    /** The distal joint of this spider's leg. */
    private final Component distalJoint;
    /** The list of all the joints in this spider. */
    private final List<Component> joints;
    /** The middle joint of this spider's leg. */
    private final Component middleJoint;
    /** The "palm" (joint closest to the body on the leg) joint of spider's leg. */
    private final Component palmJoint;
    /** The Foot joint for this spider */
    private final Component footJoint;

    //Modified from original code
    //Instantiates a Leg class that has parameters palmJoint, distalJoint, middleJoint, footJoint
    public Leg(final Component palmJoint, final Component middleJoint,
        final Component distalJoint, final Component footJoint) {
      this.palmJoint = palmJoint;
      this.middleJoint = middleJoint;
      this.distalJoint = distalJoint;
      this.footJoint = footJoint;
      //adds these joints to the list of all joints
      this.joints = Collections.unmodifiableList(Arrays.asList(this.palmJoint,
          this.middleJoint, this.distalJoint, this.footJoint));
    }

    /**
     * Gets the distal joint of this Leg.
     * 
     * @return The distal joint of this Leg.
     */
    Component distalJoint() {
      return this.distalJoint;
    }

    /**
     * Gets an unmodifiable view of the list of the joints of this Leg.
     * 
     * @return An unmodifiable view of the list of the joints of this Leg.
     */
    List<Component> joints() {
      return this.joints;
    }

    /**
     * Gets the middle joint of this Leg.
     * 
     * @return The middle joint of this Leg.
     */
    Component middleJoint() {
      return this.middleJoint;
    }

    /**
     * Gets the palm joint of this Leg.
     * 
     * @return The palm joint of this Leg.
     */
    Component palmJoint() {
      return this.palmJoint;
    }
    Component footJoint() {
        return this.footJoint;
      }
  }

  /** The color for components which are selected for rotation. */
  public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  /** The radius of the components which comprise the arm. */
  public static final double ARM_RADIUS = 0.25;
  /** The default width of the created window. */
  public static final int DEFAULT_WINDOW_HEIGHT = 512;
  /** The default height of the created window. */
  public static final int DEFAULT_WINDOW_WIDTH = 512;
  /** The height of the distal joint on each of the Legs. */
  public static final double DISTAL_JOINT_HEIGHT = 0.45;
  /** The radius of each joint which comprises the Leg. */
  public static final double Leg_RADIUS = 0.04;
  //the radius of the distal and foot part of the leg.  Smaller because spider's legs get thinner as they go from 
  //"body to foot" 
  public static final double DISTAL_RADIUS = 0.029;
  /** The height of the forearm. */
  public static final double FOREARM_HEIGHT = 1.5;
  //radius of the body of the spider
  public static final double BODY_RADIUS = 0.48;
  /** The color for components which are not selected for rotation. */
  public static final FloatColor INACTIVE_COLOR = FloatColor.ORANGE;
  /** The initial position of the top level component in the scene. */
  public static final Point3D INITIAL_POSITION = new Point3D(1, 1, 0);
  /** The height of the middle joint on each of the Legs. */
  public static final double MIDDLE_JOINT_HEIGHT = 0.4;
  /** The height of the palm joint on each of the Legs. */
  public static final double PALM_JOINT_HEIGHT = 0.25;
  /** The angle by which to rotate the joint on user request to rotate. */
  public static final double ROTATION_ANGLE = 2.0;
  /** Randomly generated serial version UID. */
  private static final long serialVersionUID = -7060944143920496524L;

  /**
   * Runs the hand simulation in a single JFrame.
   * 
   * @param args
   *          This parameter is ignored.
   */
  public static void main(final String[] args) {
    new PA2().animator.start();
  }

  /**
   * The animator which controls the framerate at which the canvas is animated.
   */
  final FPSAnimator animator;
  /** The canvas on which we draw the scene. */
  private final GLCanvas canvas;
  /** The capabilities of the canvas. */
  private final GLCapabilities capabilities = new GLCapabilities(null);
  /** The Legs on the spider to be modeled. */
  private final Leg[] Legs;
 
  /** The OpenGL utility object. */
  private final GLU glu = new GLU();
  /** The OpenGL utility toolkit object. */
  private final GLUT glut = new GLUT();
  /** The hand to be modeled. */
  private final Component hand;
  //the head to be modeled
  private final Component head;
  /** The last x and y coordinates of the mouse press. */
  private int last_x = 0, last_y = 0;
  /** Whether the world is being rotated. */
  private boolean rotate_world = false;
  /** The axis around which to rotate the selected joints. */
  private Axis selectedAxis = Axis.X;
  /** The set of components which are currently selected for rotation. */
  private final Set<Component> selectedComponents = new HashSet<Component>(18);
  //the set of legs that can be selected for rotation
  //
  private final Set<Leg> selectedLegs = new HashSet<Leg>(8);
  /** Whether the state of the model has been changed. */
  private boolean stateChanged = true;
  /**
   * The top level component in the scene which controls the positioning and
   * rotation of everything in the scene.
   */
  private final Component topLevelComponent;
  /** The upper arm to be modeled. */
  //private final Component upperArm;
  /** The quaternion which controls the rotation of the world. */
  private Quaternion viewing_quaternion = new Quaternion();
  /** The set of all components. */
  private final List<Component> components;
  //left front leg of spider names
  public static String INDEX_PALM_NAME = "index palm";
  public static String INDEX_MIDDLE_NAME = "index middle";
  public static String INDEX_DISTAL_NAME = "index distal";
  //right front leg of spider names
  public static String LEG_PALM_NAME = "leg palm";
  public static String LEG_MIDDLE_NAME = "leg middle";
  public static String LEG_DISTAL_NAME = "leg distal";
  //right second to front leg of spider names
  public static String LEGBOT2_PALM_NAME = "LEGBOT2 palm";
  public static String LEGBOT2_MIDDLE_NAME = "LEGBOT2 middle";
  public static String LEGBOT2_DISTAL_NAME = "LEGBOT2 distal";
  //right second to back leg of spider names
  public static String LEGTOP2_PALM_NAME = "LEGTOP2 palm";
  public static String LEGTOP2_MIDDLE_NAME = "LEGTOP2 middle";
  public static String LEGTOP2_DISTAL_NAME = "LEGTOP2 distal";
  //right back leg of spider names
  public static String LEGTOP_PALM_NAME = "LEGTOP palm";
  public static String LEGTOP_MIDDLE_NAME = "LEGTOP middle";
  public static String LEGTOP_DISTAL_NAME = "LEGTOP distal";
  //second to front left leg of spider names
  public static String RING_PALM_NAME = "ring palm";
  public static String RING_MIDDLE_NAME = "ring middle";
  public static String RING_DISTAL_NAME = "ring distal";
  //second to back leg of spider names
  public static String MIDDLE_PALM_NAME = "middle palm";
  public static String MIDDLE_MIDDLE_NAME = "middle middle";
  public static String MIDDLE_DISTAL_NAME = "middle distal";
  //back left legs of spider names
  public static String PINKY_PALM_NAME = "pinky palm";
  public static String PINKY_MIDDLE_NAME = "pinky middle";
  public static String PINKY_DISTAL_NAME = "pinky distal";
  //leftover names from previous version of this code
  public static String HAND_NAME = "hand";
  public static String FOREARM_NAME = "forearm";
  public static String UPPER_ARM_NAME = "upper arm";
  public static String TOP_LEVEL_NAME = "top level";
  public static String HEAD_NAME = "head name";
  public static String FOOT1_NAME = "foot1 name";
  public static String FOOT2_NAME = "foot2 name";
  public static String FOOT3_NAME = "foot3 name";
  public static String FOOT4_NAME = "foot4 name";
  public static String FOOT5_NAME = "foot5 name";
  public static String FOOT6_NAME = "foot6 name";
  public static String FOOT7_NAME = "foot7 name";
  public static String FOOT8_NAME = "foot8 name";

  /**
   * Initializes the necessary OpenGL objects and adds a canvas to this JFrame.
   */
  public PA2() {
    this.capabilities.setDoubleBuffered(true);

    this.canvas = new GLCanvas(this.capabilities);
    this.canvas.addGLEventListener(this);
    this.canvas.addMouseListener(this);
    this.canvas.addMouseMotionListener(this);
    this.canvas.addKeyListener(this);
    // this is true by default, but we just add this line to be explicit
    this.canvas.setAutoSwapBufferMode(true);
    this.getContentPane().add(this.canvas);

    // refresh the scene at 60 frames per second
    this.animator = new FPSAnimator(this.canvas, 60);

    this.setTitle("CS480/CS680 : Hand Simulator");
    this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    // all the distal joints
    //setting the distal joints of the spider.  Made from a roundedcyliner
    //the distal parts of the leg are slightly skinnier than the other parts of the leg and therefore
    //the radius of their cylinder is smaller
    final Component distal1 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), PINKY_DISTAL_NAME);
    final Component distal2 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), RING_DISTAL_NAME);
    final Component distal3 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), MIDDLE_DISTAL_NAME);
    final Component distal4 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), INDEX_DISTAL_NAME);
    final Component distal5 = new Component(new Point3D(0, 0,
    	MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEG_DISTAL_NAME);
    final Component distal6 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEGBOT2_DISTAL_NAME);
    final Component distal7 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEGTOP2_DISTAL_NAME);
    final Component distal8 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT, this.glut), LEGTOP_DISTAL_NAME);
    

    // all the middle joints
    //initiating the middle joints made from rounded cylinders
    final Component middle1 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), PINKY_MIDDLE_NAME);
    final Component middle2 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), RING_MIDDLE_NAME);
    final Component middle3 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), MIDDLE_MIDDLE_NAME);
    final Component middle4 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), INDEX_MIDDLE_NAME);
    final Component middle5 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEG_MIDDLE_NAME);
    final Component middle6 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEGBOT2_MIDDLE_NAME);
    final Component middle7 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEGTOP2_MIDDLE_NAME);
    final Component middle8 = new Component(new Point3D(0, 0,
        PALM_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), LEGTOP_MIDDLE_NAME);
    

    // all the palm joints, displaced by various amounts from the body
    //the 4 right palm parts needed to be flipped by 180 degrees in order to compensate for the cylinder shape
    //the x position for each joint is also slightly different since the body is round and some parts stick out more than others
    final Component palm1 = new Component(new Point3D(-0.3, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        PINKY_PALM_NAME);
    final Component palm2 = new Component(new Point3D(-.1, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        RING_PALM_NAME);
    final Component palm3 = new Component(new Point3D(0.1, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        MIDDLE_PALM_NAME);
    final Component palm4 = new Component(new Point3D(0.3, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        INDEX_PALM_NAME);
    final Component palm5 = new Component(new Point3D(-.3, 0, .2),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        LEG_PALM_NAME);
    final Component palm6 = new Component(new Point3D(-0.1, 0, .2),
        new RoundedCylinder(Leg_RADIUS, PALM_JOINT_HEIGHT, this.glut),
        LEGBOT2_PALM_NAME);
    final Component palm7 = new Component(new Point3D(0.1, 0,.2),
 		new RoundedCylinder(Leg_RADIUS,
        PALM_JOINT_HEIGHT, this.glut), LEGTOP2_PALM_NAME);
    final Component palm8 = new Component(new Point3D(0.3,0,.2),
    	new RoundedCylinder(Leg_RADIUS,
        PALM_JOINT_HEIGHT, this.glut), LEGTOP_PALM_NAME);
    //initiating the feet of the spider
    //they are small and therefore have a very small height.  They are as skinny as the distal part of the leg
    final Component feet1 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT1_NAME);
    final Component feet2 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT2_NAME);
    final Component feet3 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT3_NAME);
    final Component feet4 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT4_NAME);
    final Component feet5 = new Component(new Point3D(0, 0,
      	DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT5_NAME);
    final Component feet6 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT6_NAME);
    final Component feet7 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT7_NAME);
    final Component feet8 = new Component(new Point3D(0, 0,
        DISTAL_JOINT_HEIGHT), new RoundedCylinder(DISTAL_RADIUS,
        DISTAL_JOINT_HEIGHT/4.5, this.glut), FOOT8_NAME);

    // put together the Legs for easier selection by keyboard input later on
    this.Legs = new Leg[] { new Leg(palm1, middle1, distal1, feet1),
        new Leg(palm2, middle2, distal2, feet2),
        new Leg(palm3, middle3, distal3, feet3),
        new Leg(palm4, middle4, distal4, feet4),
        new Leg(palm5, middle5, distal5, feet5),
        new Leg(palm6, middle6, distal6, feet6),
        new Leg(palm7, middle7, distal7, feet7),
        new Leg(palm8, middle8, distal8, feet8)
         };

    // the hand, which models the wrist joint
    //in my example hand=body
    this.hand = new Component(new Point3D(0, 0, FOREARM_HEIGHT), new Palm(
        BODY_RADIUS, this.glut), HAND_NAME);
    //the head of the spider.  Displaced in order to bring it to the front of the body
    //created using a Head() type with is essentially a slightly flattened sphere
    this.head = new Component(new Point3D(-.4, -.1, .3), new Head(
            BODY_RADIUS/3, this.glut), HEAD_NAME);

    // the top level component which provides an initial position and rotation
    // to the scene (but does not cause anything to be drawn)
    this.topLevelComponent = new Component(INITIAL_POSITION, TOP_LEVEL_NAME);
    
    //makes the initial child to the toplevelcomponent the body of the spider
    //allows everything to branch off from the body
    this.topLevelComponent.addChild(this.hand);
    //adds the 8 closest joints to the body and the head as children to the body
    //provides a proper hierarchy to the structure of the spider's legs
    this.hand.addChildren(palm1, palm2, palm3, palm4, palm5, palm6, palm7, palm8, this.head);
    //adds the middle part of the leg as children to the joints closed to the body
    palm1.addChild(middle1);
    palm2.addChild(middle2);
    palm3.addChild(middle3);
    palm4.addChild(middle4);
    palm5.addChild(middle5);
    palm6.addChild(middle6);
    palm7.addChild(middle7);
    palm8.addChild(middle8);
    //adds the distal part of the leg as a child to the middle part of the leg
    middle1.addChild(distal1);
    middle2.addChild(distal2);
    middle3.addChild(distal3);
    middle4.addChild(distal4);
    middle5.addChild(distal5);
    middle6.addChild(distal6);
    middle7.addChild(distal7);
    middle8.addChild(distal8);
    //adds the foot as a child to the distal part of the leg
    distal1.addChild(feet1);
    distal2.addChild(feet2);
    distal3.addChild(feet3);
    distal4.addChild(feet4);
    distal5.addChild(feet5);
    distal6.addChild(feet6);
    distal7.addChild(feet7);
    distal8.addChild(feet8);

    //sets up the spider in a good demonstration position
    this.topLevelComponent.rotate(Axis.Y, 240);
    this.topLevelComponent.rotate(Axis.X, -90);
    this.topLevelComponent.rotate(Axis.Z, 55);
   

    //rotate the legs so that they are facing the correct way on the right side of the body
    //rotate the legs in order to give the creature an insect look at the start of the program
    palm5.rotate(Axis.Y, 180);
    palm6.rotate(Axis.Y, 180);
    palm7.rotate(Axis.Y, 180);
    palm8.rotate(Axis.Y, 180);
    middle5.rotate(Axis.X, 45);
    distal5.rotate(Axis.X, -90);
    middle6.rotate(Axis.X, 45);
    distal6.rotate(Axis.X, -90);
    middle7.rotate(Axis.X, 45);
    distal7.rotate(Axis.X, -90);
    middle8.rotate(Axis.X, 45);
    distal8.rotate(Axis.X, -90);
    middle1.rotate(Axis.X,45);
    distal1.rotate(Axis.X, -90);
    middle2.rotate(Axis.X,45);
    distal2.rotate(Axis.X, -90);
    middle3.rotate(Axis.X,45);
    distal3.rotate(Axis.X, -90);
    middle4.rotate(Axis.X,45);
    distal4.rotate(Axis.X, -90);
    //rotate feet in order to give them a good starting angle
    feet1.rotate(Axis.X, 45);
    feet2.rotate(Axis.X, 45);
    feet3.rotate(Axis.X, 45);
    feet4.rotate(Axis.X, 45);
    feet5.rotate(Axis.X, 45);
    feet6.rotate(Axis.X, 45);
    feet7.rotate(Axis.X, 45);
    feet8.rotate(Axis.X, 45);
    
    
    

    

    // set rotation limits for the palm joints of the Legs
    for (final Component palmJoint : Arrays.asList(palm1, palm2, palm3, palm4)){
    		
      palmJoint.setXPositiveExtent(15);
      palmJoint.setXNegativeExtent(-10);
      palmJoint.setYPositiveExtent(5);
      palmJoint.setYNegativeExtent(-5);
      palmJoint.setZPositiveExtent(0);
      palmJoint.setZNegativeExtent(0);
    }
    //set rotation limits for the right palm joints of the spider
    for (final Component palmJoint : Arrays.asList(palm5, palm6, palm7, palm8)){
    	 palmJoint.setXPositiveExtent(15);
         palmJoint.setXNegativeExtent(-10);
         palmJoint.setYPositiveExtent(185);
         palmJoint.setYNegativeExtent(175);
         palmJoint.setZPositiveExtent(0);
         palmJoint.setZNegativeExtent(0);
    }
    // set rotation limits for the middle joints of the Leg
    for (final Component middleJoint : Arrays.asList(middle1, middle2,
        middle3, middle4, middle5, middle6, middle7, middle8)) {
      middleJoint.setXPositiveExtent(45);
      middleJoint.setXNegativeExtent(15);
      middleJoint.setYPositiveExtent(0);
      middleJoint.setYNegativeExtent(0);
      middleJoint.setZPositiveExtent(0);
      middleJoint.setZNegativeExtent(0);
    }

    // set rotation limits for the distal joints of the Leg
    for (final Component distalJoint : Arrays.asList(distal1, distal2,
        distal3, distal4,distal5,distal6,distal7,distal8)) {
      distalJoint.setXPositiveExtent(-45);
      distalJoint.setXNegativeExtent(-150);
      distalJoint.setYPositiveExtent(0);
      distalJoint.setYNegativeExtent(0);
      distalJoint.setZPositiveExtent(0);
      distalJoint.setZNegativeExtent(0);
    }
    //set rotation limits for the "feet" of the creature
    for (final Component footJoint : Arrays.asList(feet1,feet2,feet3,
    		feet4,feet5,feet6,feet7,feet8)){
    	footJoint.setXPositiveExtent(65);
        footJoint.setXNegativeExtent(25);
        footJoint.setYPositiveExtent(0);
        footJoint.setYNegativeExtent(0);
        footJoint.setZPositiveExtent(0);
        footJoint.setZNegativeExtent(0);
        
    }

    // create the list of all the components for debugging purposes
    this.components = Arrays.asList(palm1, middle1, distal1,feet1, palm2, middle2,
        distal2,feet2, palm3, middle3, distal3, feet3, palm4, middle4, distal4, feet4,
        palm5, middle5, distal5, feet5, palm6, middle6, distal6, feet6, 
        palm7, middle7, distal7, feet7, palm8, middle8, distal8, feet8, this.hand);
  }

  /**
   * Redisplays the scene containing the hand model.
   * 
   * @param drawable
   *          The OpenGL drawable object with which to create OpenGL models.
   */
  public void display(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // clear the display
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // from here on affect the model view
    gl.glMatrixMode(GL2.GL_MODELVIEW);

    // start with the identity matrix initially
    gl.glLoadIdentity();

    // rotate the world by the appropriate rotation quaternion
    gl.glMultMatrixf(this.viewing_quaternion.toMatrix(), 0);

    // update the position of the components which need to be updated
    // TODO only need to update the selected and JUST deselected components
    if (this.stateChanged) {
      this.topLevelComponent.update(gl);
      this.stateChanged = false;
    }

    // redraw the components
    this.topLevelComponent.draw(gl);
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param drawable
   *          This parameter is ignored.
   * @param modeChanged
   *          This parameter is ignored.
   * @param deviceChanged
   *          This parameter is ignored.
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged) {
    // intentionally unimplemented
  }

  /**
   * Initializes the scene and model.
   * 
   * @param drawable
   *          {@inheritDoc}
   */
  public void init(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // perform any initialization needed by the hand model
    this.topLevelComponent.initialize(gl);

    // initially draw the scene
    this.topLevelComponent.update(gl);

    // set up for shaded display of the hand
    final float light0_position[] = { 1, 1, 1, 0 };
    final float light0_ambient_color[] = { 0.25f, 0.25f, 0.25f, 1 };
    final float light0_diffuse_color[] = { 1, 1, 1, 1 };

    gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
    gl.glEnable(GL2.GL_COLOR_MATERIAL);
    gl.glColorMaterial(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL2.GL_SMOOTH);

    // set up the light source
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient_color, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse_color, 0);

    // turn lighting and depth buffering on
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glEnable(GL2.GL_NORMALIZE);
  }

  /**
   * Interprets key presses according to the following scheme:
   * 
   * up-arrow, down-arrow: increase/decrease rotation angle
   * 
   * @param key
   *          The key press event object.
   */
  public void keyPressed(final KeyEvent key) {
    switch (key.getKeyCode()) {
    case KeyEvent.VK_KP_UP:
    case KeyEvent.VK_UP:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    case KeyEvent.VK_KP_DOWN:
    case KeyEvent.VK_DOWN:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, -ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    default:
      break;
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param key
   *          This parameter is ignored.
   */
  public void keyReleased(final KeyEvent key) {
    // intentionally unimplemented
  }

  private final TestCases testCases = new TestCases();

  private void setModelState(final Map<String, Angled> state) {
    
    this.hand.setAngles(state.get(HAND_NAME));
    this.Legs[0].palmJoint().setAngles(state.get(PINKY_PALM_NAME));
    this.Legs[0].middleJoint().setAngles(state.get(PINKY_MIDDLE_NAME));
    this.Legs[0].distalJoint().setAngles(state.get(PINKY_DISTAL_NAME));
    this.Legs[0].footJoint().setAngles(state.get(FOOT1_NAME));
    this.Legs[1].palmJoint().setAngles(state.get(RING_PALM_NAME));
    this.Legs[1].middleJoint().setAngles(state.get(RING_MIDDLE_NAME));
    this.Legs[1].distalJoint().setAngles(state.get(RING_DISTAL_NAME));
    this.Legs[1].footJoint().setAngles(state.get(FOOT2_NAME));
    this.Legs[2].palmJoint().setAngles(state.get(MIDDLE_PALM_NAME));
    this.Legs[2].middleJoint().setAngles(state.get(MIDDLE_MIDDLE_NAME));
    this.Legs[2].distalJoint().setAngles(state.get(MIDDLE_DISTAL_NAME));
    this.Legs[2].footJoint().setAngles(state.get(FOOT3_NAME));
    this.Legs[3].palmJoint().setAngles(state.get(INDEX_PALM_NAME));
    this.Legs[3].middleJoint().setAngles(state.get(INDEX_MIDDLE_NAME));
    this.Legs[3].distalJoint().setAngles(state.get(INDEX_DISTAL_NAME));
    this.Legs[3].footJoint().setAngles(state.get(FOOT4_NAME));
    this.Legs[4].palmJoint().setAngles(state.get(LEGTOP_PALM_NAME));
    this.Legs[4].middleJoint().setAngles(state.get(LEGTOP_MIDDLE_NAME));
    this.Legs[4].distalJoint().setAngles(state.get(LEGTOP_DISTAL_NAME));
    this.Legs[4].footJoint().setAngles(state.get(FOOT5_NAME));
    this.Legs[5].palmJoint().setAngles(state.get(LEGTOP2_PALM_NAME));
    this.Legs[5].middleJoint().setAngles(state.get(LEGTOP2_MIDDLE_NAME));
    this.Legs[5].distalJoint().setAngles(state.get(LEGTOP2_DISTAL_NAME));
    this.Legs[5].footJoint().setAngles(state.get(FOOT6_NAME));
    this.Legs[6].palmJoint().setAngles(state.get(LEGBOT2_PALM_NAME));
    this.Legs[6].middleJoint().setAngles(state.get(LEGBOT2_MIDDLE_NAME));
    this.Legs[6].distalJoint().setAngles(state.get(LEGBOT2_DISTAL_NAME));
    this.Legs[6].footJoint().setAngles(state.get(FOOT7_NAME));
    this.Legs[7].palmJoint().setAngles(state.get(LEG_PALM_NAME));
    this.Legs[7].middleJoint().setAngles(state.get(LEG_MIDDLE_NAME));
    this.Legs[7].distalJoint().setAngles(state.get(LEG_DISTAL_NAME));
    this.Legs[7].footJoint().setAngles(state.get(FOOT8_NAME));
    this.stateChanged = true;
  }
  //keyboard layout changed by Brian Soares
  //keyboard interface description changed to fit my layout of the spider
  /**
   * Interprets typed keys according to the following scheme:
   * 
   * 1 : toggle the front left leg active in rotation
   * 
   * 2 : toggle the second to front left leg active in rotation
   * 
   * 3 : toggle the second to back left leg active in rotation
   * 
   * 4 : toggle the back left leg active in rotation
   * 
   * 5 : toggle the front right leg active in rotation
   * 
   * 6 : toggle second to front right leg for rotation
   * 
   * 7 : toggle second to back right leg for rotation
   * 
   * 8 : toggle back right leg for rotation
   * 
   * X : use the X axis rotation at the active joint(s)
   * 
   * Y : use the Y axis rotation at the active joint(s)
   * 
   * Z : use the Z axis rotation at the active joint(s)
   * 
   * T:  runs the test poses!
   * 
   * P : select joint that connects leg to body
   * 
   * M : select middle joint
   * 
   * D : select last (distal) joint
   * 
   * F : select the "feet" of the spider
   * 
   * R : resets the view to the initial rotation
   * 
   * K : prints the angles of the five Legs for debugging purposes
   * 
   * Q, Esc : exits the program
   * 
   */
  public void keyTyped(final KeyEvent key) {
    switch (key.getKeyChar()) {
    case 'Q':
    case 'q':
    case KeyEvent.VK_ESCAPE:
      new Thread() {
        @Override
        public void run() {
          PA2.this.animator.stop();
        }
      }.start();
      System.exit(0);
      break;

    // print the angles of the components
    case 'K':
    case 'k':
      printJoints();
      break;

    // resets to the stop sign
    case 'C':
    case 'c':
      this.setModelState(this.testCases.stop());
      break;

    // set the state of the hand to the next test case
    case 'T':
    case 't':
      this.setModelState(this.testCases.next());
      break;

    // set the viewing quaternion to 0 rotation
    case 'R':
    case 'r':
      this.viewing_quaternion.reset();
      //set the correct angles for the left 4 legs
      for(int i=0;i<4;i++){
    	  Legs[i].footJoint.setAngles(45.0, 0, 0);
    	  Legs[i].palmJoint.setAngles(0, 0, 0);
    	  Legs[i].distalJoint.setAngles(-90, 0, 0);
    	  Legs[i].middleJoint.setAngles(45, 0, 0);
    	  
      }
      //set the correct angles for the right 4 legs
      for(int i=4;i<8;i++){
    	  Legs[i].footJoint.setAngles(45.0,0,0);
    	  Legs[i].palmJoint.setAngles(0,180,0);
    	  Legs[i].middleJoint.setAngles(45,0,0);
    	  Legs[i].distalJoint.setAngles(-90, 0, 0);
      }
      //mark stateChanged to true to allow a redraw of creature
      this.stateChanged=true;
      break; 

    // Toggle which legs are affected by the current rotation
    case '1':
      toggleSelection(this.Legs[0]);
      	break;
    case '2':
      toggleSelection(this.Legs[1]);
      	break;
    case '3':
      toggleSelection(this.Legs[2]);
      	break;
    case '4':
      toggleSelection(this.Legs[3]);
      	break;
    case '5':
      toggleSelection(this.Legs[4]);
      	break;
    case '6':
        toggleSelection(this.Legs[5]);
        break;
    case '7':
    	toggleSelection(this.Legs[6]);
    	break;
    case '8':
    	toggleSelection(this.Legs[7]);
    	break;
    // toggle which joints are affected by the current rotation
    case 'D':
    case 'd':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.distalJoint());
      }
      break;
    case 'M':
    case 'm':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.middleJoint());
      }
      break;
    case 'P':
    case 'p':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.palmJoint());
      }
      break;
    case 'F':
    case 'f':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.footJoint());
      }
      break;
    
    

    // change the axis of rotation at current active joint
    case 'X':
    case 'x':
      this.selectedAxis = Axis.X;
      break;
    case 'Y':
    case 'y':
      this.selectedAxis = Axis.Y;
      break;
    case 'Z':
    case 'z':
      this.selectedAxis = Axis.Z;
      break;
    default:
      break;
    }
  }

  /**
   * Prints the joints on the System.out print stream.
   */
  private void printJoints() {
    this.printJoints(System.out);
  }

  /**
   * Prints the joints on the specified PrintStream.
   * 
   * @param printStream
   *          The stream on which to print each of the components.
   */
  private void printJoints(final PrintStream printStream) {
    for (final Component component : this.components) {
      printStream.println(component);
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseClicked(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Updates the rotation quaternion as the mouse is dragged.
   * 
   * @param mouse
   *          The mouse drag event object.
   */
  public void mouseDragged(final MouseEvent mouse) {
	if (this.rotate_world) {
		// get the current position of the mouse
		final int x = mouse.getX();
		final int y = mouse.getY();
	
		// get the change in position from the previous one
		final int dx = x - this.last_x;
		final int dy = y - this.last_y;
	
		// create a unit vector in the direction of the vector (dy, dx, 0)
		final double magnitude = Math.sqrt(dx * dx + dy * dy);
		final float[] axis = magnitude == 0 ? new float[]{1,0,0}: // avoid dividing by 0
			new float[] { (float) (dy / magnitude),(float) (dx / magnitude), 0 };
	
		// calculate appropriate quaternion
		final float viewing_delta = 3.1415927f / 180.0f;
		final float s = (float) Math.sin(0.5f * viewing_delta);
		final float c = (float) Math.cos(0.5f * viewing_delta);
		final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
				* axis[2]);
		this.viewing_quaternion = Q.multiply(this.viewing_quaternion);
	
		// normalize to counteract acccumulating round-off error
		this.viewing_quaternion.normalize();
	
		// save x, y as last x, y
		this.last_x = x;
		this.last_y = y;
	}
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseEntered(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseExited(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseMoved(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Starts rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse press event object.
   */
  public void mousePressed(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.last_x = mouse.getX();
      this.last_y = mouse.getY();
      this.rotate_world = true;
    }
  }

  /**
   * Stops rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse release event object.
   */
  public void mouseReleased(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.rotate_world = false;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @param drawable
   *          {@inheritDoc}
   * @param x
   *          {@inheritDoc}
   * @param y
   *          {@inheritDoc}
   * @param width
   *          {@inheritDoc}
   * @param height
   *          {@inheritDoc}
   */
  public void reshape(final GLAutoDrawable drawable, final int x, final int y,
      final int width, final int height) {
    final GL2 gl = (GL2)drawable.getGL();

    // prevent division by zero by ensuring window has height 1 at least
    final int newHeight = Math.max(1, height);

    // compute the aspect ratio
    final double ratio = (double) width / newHeight;

    // reset the projection coordinate system before modifying it
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    // set the viewport to be the entire window
    gl.glViewport(0, 0, width, newHeight);

    // set the clipping volume
    this.glu.gluPerspective(25, ratio, 0.1, 100);

    // camera positioned at (0,0,6), look at point (0,0,0), up vector (0,1,0)
    this.glu.gluLookAt(0, 0, 12, 0, 0, 0, 0, 1, 0);

    // switch back to model coordinate system
    gl.glMatrixMode(GL2.GL_MODELVIEW);
  }

  private void toggleSelection(final Component component) {
    if (this.selectedComponents.contains(component)) {
      this.selectedComponents.remove(component);
      component.setColor(INACTIVE_COLOR);
    } else {
      this.selectedComponents.add(component);
      component.setColor(ACTIVE_COLOR);
    }
    this.stateChanged = true;
  }

  private void toggleSelection(final Leg Leg) {
    if (this.selectedLegs.contains(Leg)) {
      this.selectedLegs.remove(Leg);
      this.selectedComponents.removeAll(Leg.joints());
      for (final Component joint : Leg.joints()) {
        joint.setColor(INACTIVE_COLOR);
      }
    } else {
      this.selectedLegs.add(Leg);
    }
    this.stateChanged = true;
  }

@Override
public void dispose(GLAutoDrawable drawable) {
	// TODO Auto-generated method stub
	
}
}
