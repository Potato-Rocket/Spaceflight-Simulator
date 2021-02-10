package space.sim.physics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Stores and updates physical information about a body. Updates information such as position
 * and velocity as time passes and as forces are applied to the body.
 */
public class Body {

  /** Universal gravitational constant. */
  public static final BigDecimal G = new BigDecimal("0.00000000006674");
  /** The array of all bodies. */
  public static ArrayList<Body> bodies = new ArrayList<>();

  /** Vector to store the body's position. */
  private Vector3D position;
  /** Vector to store the body's velocity. */
  private Vector3D velocity;
  /** Vector to store the current forces acting on the body. */
  private ArrayList<Vector3D> gravityForces;
  /** Vector to store the body's total acceleration. */
  private Vector3D acceleration;
  /** The body's mass. */
  private BigDecimal mass;
  /** The body's name. */
  private String name;
  /** The body's identification number. */
  private int id;

  /**
   * Class constructor with all values specified by user. Assigns specified values to their
   * corresponding fields and generates an id and gravity forces array for the body. Adds another
   * vector to each body's gravity force array.
   *
   * @param position initial position of the body
   * @param velocity initial velocity of the body
   * @param mass initial mass of the body
   * @param name name of the body
   */
  public Body(Vector3D position, Vector3D velocity, BigDecimal mass, String name) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.name = name;
    id = bodies.size();
    bodies.add(this);
    gravityForces = new ArrayList<>();
    for (int i = 0; i < id; i++) {
      gravityForces.add(new Vector3D());
    }
    for (Body body : bodies) {
      body.gravityForces.add(new Vector3D());
    }
  }

  /**
   * Class constructor with mass and name specified by user. Sets position and velocity to
   * <code>(0.00, 0.00, 0.00)</code>  as a default.
   *
   * @param mass initial mass of the body
   * @param name name of the body
   */
  public Body(BigDecimal mass, String name) {
    new Body(new Vector3D(), new Vector3D(), mass, name);
  }

  /**
   * Class constructor without user specifications. Mass is set to 1.0 and name is set to Body
   * as a default.
   */
  public Body() {
    new Body(BigDecimal.ONE, "space.sim.physics.Body");
  }

  /**
   * Updates the body's physical motion vectors. Resets the acceleration factor and sets it
   * based on the mass and the gravity forces currently acting on the body. The velocity is
   * updated based on the acceleration and the position is updated based on the velocity.
   */
  private void update() {
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
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this. Does not
   * calculate for a comparison between a body and itself.
   *
   * @param body second body
   */
  private void setGravityForce(Body body) {
    if (id != body.id) {
      BigDecimal r = position.distanceTo(body.position);
      BigDecimal f = G.multiply(mass.multiply(body.mass).setScale(32, RoundingMode.DOWN).
          divide(r.pow(2), RoundingMode.DOWN));
      Vector3D angle = position.angleTo(body.position);
      gravityForces.set(body.id, angle.scaleVector(f));
    } else {
      gravityForces.set(id, new Vector3D());
    }
  }

  /**
   * Updates every body. Runs the update function for each body to update the motion. Then
   * updates the gravitational forces between each body and every other body.
   */
  public static void updateAll() {
    for (Body body : bodies) {
      body.update();
    }
    for (Body body : bodies) {
      for (Body other : bodies) {
        body.setGravityForce(other);
      }
    }
  }

  /**
   * Returns information about every body in a <code>String</code>. Runs the verbose toString method
   * for each body in the bodies array and appends them into one <code>String</code>.
   *
   * @return Returns the concatenated <code>String</code> containing info about every body.
   */
  public static String allToString() {
    StringBuilder string = new StringBuilder();
    for (Body body : bodies) {
      string.append(body.toString(true)).append("\n\n");
    }
    return String.valueOf(string);
  }

  /**
   * Formats the body's attributes into a <code>String</code>.
   *
   * @return Returns a string representing the body's attributes.
   */
  public String toString(boolean verbose) {
    String string = "space.sim.physics.Body " + (id + 1) + ": " + name;
    if (verbose) {
      string += "\nMass = " + mass +
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
