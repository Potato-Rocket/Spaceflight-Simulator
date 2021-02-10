import java.util.ArrayList;

/**
 * Stores and updates physical information about a body. Updates information such as position
 * and velocity as time passes and as forces are applied to the body.
 */
public class Body {

  /** Universal gravitational constant. */
  private final double G = 6.674 * Math.pow(10, -11);
  /** Vector to store the body's position. */
  private Vector3D position;
  /** Vector to store the body's velocity. */
  private Vector3D velocity;
  /** Vector to store the current forces acting on the body. */
  private ArrayList<Vector3D> forces;
  /** Vector to store the body's total acceleration. */
  private Vector3D acceleration;
  /** The body's mass. */
  private double mass;
  /** The body's name. */
  private String name;
  /** The body's identification number. */
  private int id;
  /** The number of bodies */
  private static int count = 0;

  /**
   * Class constructor with all values specified by user. Assigns specified values to their
   * corresponding fields and generates an id for the body.
   *
   * @param position initial position of the body
   * @param velocity initial velocity of the body
   * @param mass initial mass of the body
   * @param name name of the body
   */
  public Body(Vector3D position, Vector3D velocity, double mass, String name) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.name = name;
    id = count;
    count++;
  }

  /**
   * Class constructor with mass and name specified by user. Sets position and velocity to
   * (0.0, 0.0, 0.0)  as a default.
   *
   * @param mass initial mass of the body
   * @param name name of the body
   */
  public Body(double mass, String name) {
    new Body(new Vector3D(), new Vector3D(), mass, name);
  }

  /**
   * Class constructor without user specifications. Mass is set to 1.0 and name is set to "Body"
   * as a default.
   */
  public Body() {
    new Body(1, "Body");
  }

  /** Create a force for every created body. */
  public void createForces() {
    forces = new ArrayList<>(count);
  }

  /**
   * Gets the body's position as a 3D vector.
   *
   * @return Returns the position.
   */
  public Vector3D getPosition() {
    return position;
  }

  /**
   * Gets the body's mass as a double.
   *
   * @return Returns the mass.
   */
  public double getMass() {
    return mass;
  }

  /**
   * Gets the body's identification number as an integer.
   *
   * @return Returns the id.
   */
  public int getId() {
    return id;
  }

  /**
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this.
   *
   * @param body second body
   */
  public void setGravityForce(Body body) {
    double r = this.position.distanceTo(body.getPosition());
    double f = G * (this.mass * body.getMass() / Math.pow(r, 2));
    forces.set(body.getId(), new Vector3D(0, 0, -1 * f));
  }

  /**
   * Formats the body's attributes into a <code>String</code>.
   * <p>
   * eg. <code>Body(pos=Vector(0.0, 0.0, 0.0), vel=Vector(0.0, 0.0, 0.0), mass=1.0,
   * name="Body", id=1)</code>
   *
   * @return Returns a string representing the body's attributes.
   */
  public String toString() {
    return "Body(pos=" + position + ", vel=" + velocity + ", mass=" + mass + ", name=" + name +
        ", id=" + id + ")";
  }
}
