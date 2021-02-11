package space.sim.physics;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private BigDecimal mass;
  /**
   * The body's radius.
   */
  private BigDecimal radius;
  /**
   * The body's name.
   */
  private String name;
  /**
   * The body's identification number.
   */
  private int id;
  /**
   * The number of bodies that have been generated/
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
  public Body(Vector3D position, Vector3D velocity, BigDecimal mass,
              BigDecimal radius, String name) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.radius = radius;
    this.name = name;
    id = count;
    count++;
  }

  /**
   * Updates the body's physical motion vectors. Resets the acceleration factor and sets it
   * based on the mass and the gravity forces currently acting on the body. The velocity is
   * updated based on the acceleration and the position is updated based on the velocity.
   */
  public void update() {
    acceleration = new Vector3D();
    for (Vector3D f : gravityForces) {
      acceleration.addVector(f.scaleVector(BigDecimal.ONE.setScale(32, RoundingMode.DOWN).
          divide(mass, RoundingMode.DOWN)));
      f.fixScale(32);
    }
    acceleration.fixScale(32);
    velocity.addVector(acceleration);
    position.addVector(velocity);
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
   * Getter method for the body's acceleration.
   *
   * @return Returns the acceleration.
   */
  public Vector3D getAcceleration() {
    return acceleration;
  }

  /**
   * Getter method for the body's mass.
   *
   * @return Returns the body's mass.
   */
  public BigDecimal getMass() {
    return mass;
  }

  /**
   * Getter method for the body's radius.
   *
   * @return Returns the body's radius.
   */
  public BigDecimal getRadius() {
    return radius;
  }

  /**
   * Getter method for the body's name.
   *
   * @return Returns the body's name.
   */
  public String getName() {
    return name;
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
