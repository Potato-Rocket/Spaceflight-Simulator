/**
 * Stores and updates physical information about a body. Updates information such as position
 * and velocity as time passes and as forces are applied to the body.
 */
public class Body {

  /** Vector to store the body's position. */
  private Vector position;
  /** Vector to store the body's velocity. */
  private Vector velocity;
  /** Vector to store the current forces acting on the body. */
  private Vector force;
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
  public Body(Vector position, Vector velocity, double mass, String name) {
    this.position = position;
    this.velocity = velocity;
    this.mass = mass;
    this.name = name;
    force = new Vector();
    count++;
    id = count;
  }

  /**
   * Modifies the position and velocity based on current forces and simulation time passed since
   * previous update.
   *
   * @param time simulation time passed since previous simulation update
   */
  public void update(double time) {
    Vector scaled = velocity.copy();
    scaled.multiplyVector(time);
    this.position.addVector(scaled);
  }
}
