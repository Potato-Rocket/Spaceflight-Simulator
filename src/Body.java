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
  /** Vector to store the current force acting on the body. */
  private ArrayList<Vector3D> forces;
  /** The body's mass. */
  private double mass;
  /** The body's name. */
  private String name;
  /** The body's identification number. */
  private int id;
  /** The number of bodies */
  private static int count = 0;

  /**
   * Class constructor. Assigns specified values to their corresponding fields and generates an
   * id for the body.
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
    count++;
    id = count;
  }

  public Body(double mass, String name) {
    new Body(new Vector3D(), new Vector3D(), mass, name);
  }

  public Body() {
    new Body(0, "");
  }

  public void createForces() {
    forces = new ArrayList<>(count);
  }

  public Vector3D getPosition() {
    return position;
  }

  public double getMass() {
    return mass;
  }

  public int getId() {
    return id;
  }

  public void setGravityForce(Body body) {
    double r = this.position.distanceTo(body.getPosition());
    double f = G * (this.mass * body.getMass() / Math.pow(r, 2));
  }

}
