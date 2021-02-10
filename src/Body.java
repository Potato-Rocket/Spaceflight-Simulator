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
  /** The list of bodies */
  private static ArrayList<Body> bodies = new ArrayList<>();

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
   * corresponding fields and generates an id for the body.
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
   * (0.0, 0.0, 0.0)  as a default.
   *
   * @param mass initial mass of the body
   * @param name name of the body
   */
  public Body(BigDecimal mass, String name) {
    new Body(new Vector3D(), new Vector3D(), mass, name);
  }

  /**
   * Class constructor without user specifications. Mass is set to 1.0 and name is set to "Body"
   * as a default.
   */
  public Body() {
    new Body(BigDecimal.ONE, "Body");
  }

  /**
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this.
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

  public static void updateAll() {
    for (Body body : bodies) {
      for (Body other : bodies) {
        body.setGravityForce(other);
      }
    }
    for (Body body : bodies) {
      body.update();
    }
  }

  public static String allToString() {
    StringBuilder string = new StringBuilder();
    for (Body body : bodies) {
      string.append(body.toString(true)).append("\n\n");
    }
    return String.valueOf(string);
  }

  /**
   * Formats the body's attributes into a <code>String</code>.
   * <p>
   * eg. <code>Body(pos=Vector(0.0, 0.0, 0.0), vel=Vector(0.0, 0.0, 0.0), mass=1.0,
   * name="Body", id=1)</code>
   *
   * @return Returns a string representing the body's attributes.
   */
  public String toString(boolean verbose) {
    String string = "Body " + (id + 1) + ": " + name;
    if (verbose) {
      string += "\nMass = " + mass +
          "\nPosition = " + position +
          "\nVelocity = " + velocity +
          "\nAcceleration = " + acceleration +
          "\nForces = " + gravityForces;
    }
    return string;
  }

  public String toString() {
    return toString(false);
  }

}
