package space.sim.physics;

import space.sim.Vector3D;

import java.util.ArrayList;

/**
 * Stores and updates physical information about a body. Updates information such as position
 * and velocity as time passes and as forces are applied to the body.
 */
public class Body {

  /**
   * Vector to store the current gravitational forces acting on the body.
   */
  public ArrayList<Vector3D> gravityForces = new ArrayList<>();
  /**
   * Stores the position vectors to render the trail.
   */
  public ArrayList<Vector3D> trail = new ArrayList<>();

  /**
   * The number of seconds the trail remains on screen.
   * */
  private static final double TRAIL_LENGTH = 10;
  /**
   * The ratio between real time and simulation time.
   */
  private static final double TIME_SCALE = 10;

  /**
   * Vector to store the body's position.
   */
  private Vector3D position;
  /**
   * Vector to store the body's velocity.
   */
  private Vector3D velocity;
  /**
   * Vector to store the body's total acceleration.
   */
  private Vector3D acceleration;
  /**
   * The body's mass.
   */
  private double mass;
  /**
   * The body's radius.
   */
  private double radius;
  /**
   * The body's name.
   */
  private String name;
  /**
   * The body's identification number.
   */
  private int id;
  /**
   * The number of bodies that have been generated.
   */
  private static int count = 0;



  /**
   * Class constructor with all values specified by user. Assigns specified values to their
   * corresponding fields and generates an id and gravity forces array for the body. Adds another
   * vector to each body's gravity force array.
   *
   * @param position initial position of the body
   * @param velocity initial velocity of the body
   * @param mass     initial mass of the body
   * @param name     name of the body
   */
  public Body(Vector3D position, Vector3D velocity, double mass, String name) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.radius = Math.cbrt((3 * mass) / (4 * Math.PI));
    this.name = name;
    id = count;
    count++;
    trail.add(position.copy());
  }

  //TODO: Have trail only add a point when it has changed direction a certain amount
  /**
   * Updates the body's physical motion vectors. Resets the acceleration factor and sets it
   * based on the mass and the gravity forces currently acting on the body. The velocity is
   * updated based on the acceleration and the position is updated based on the velocity. Shifts
   * the trail and inserts the updated position at the start.
   */
  public void update(int millis) {
    millis *= TIME_SCALE;
    acceleration = new Vector3D();
    for (Vector3D f : gravityForces) {
      acceleration.addVector(f.scaleVector(1 / mass));
    }
    velocity.addVector(acceleration.scaleVector((double) millis / 1000));
    position.addVector(velocity.scaleVector((double) millis / 1000));
    if (trail.size() < ((TRAIL_LENGTH) / (millis / 1000.0))) {
      trail.add(new Vector3D());
    }
    for (int i = trail.size() - 1; i > 0; i--) {
      trail.set(i, trail.get(i - 1));
    }
    trail.set(0, position.copy());
  }

  /**
   * Modifies this body's attributes based on collision information. Adjusts the position and
   * velocity based on the other body's position and velocity scaled to the bodies' relative
   * masses. The masses are then merged.
   *
   * @param pos other body's position
   * @param vel other body's velocity
   * @param mass other body's mass
   */
  public void collision(Vector3D pos, Vector3D vel, double mass) {
    double scale = mass / this.mass;
    velocity.addVector(vel.scaleVector(scale));
    double[] offset = position.compareTo(pos);
    position.addVector(new Vector3D(offset[0], offset[1], offset[2]).scaleVector(scale * 0.5));
    this.mass += mass;
  }

  /**
   * Getter method for the body's position.
   *
   * @return Returns the body's position.
   */
  public Vector3D getPosition() {
    return position;
  }

  /**
   * Getter method for the body's velocity.
   *
   * @return Returns the body's velocity.
   */
  public Vector3D getVelocity() {
    return velocity;
  }

  /**
   * Getter method for the body's mass.
   *
   * @return Returns the body's mass.
   */
  public double getMass() {
    return mass;
  }

  /**
   * Getter method for the body's radius.
   *
   * @return Returns the body's radius.
   */
  public double getRadius() {
    return radius;
  }

  /**
   * Getter method for the body's identification number.
   *
   * @return Returns the body's id.
   */
  public int getId() {
    return id;
  }

  /**
   * Formats the body's attributes into a <code>String</code>.
   *
   * @return Returns a string representing the body's attributes.
   */
  public String toString(boolean verbose) {
    String string = "Body " + (id + 1) + ": " + name;
    if (verbose) {
      string += "\nMass = " + mass +
          "\nRadius = " + radius +
          "\nPosition = " + position +
          "\nVelocity = " + velocity +
          "\nAcceleration = " + acceleration +
          "\nForces = " + gravityForces;
    }
    return string;
  }

  /**
   * Runs the <code>toString</code> method with the <code>verbose</code> option set to
   * <code>false</code>.
   *
   * @return Returns a string representing the body.
   */
  public String toString() {
    return toString(false);
  }

}
