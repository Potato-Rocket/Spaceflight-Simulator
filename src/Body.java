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
  private ArrayList<Vector3D> gravityForces;
  /** Vector to store the body's total acceleration. */
  private Vector3D acceleration;
  /** The body's mass. */
  private double mass;
  /** The body's name. */
  private String name;
  /** The body's identification number. */
  private int id;
  /** The list of bodies */
  private static ArrayList<Body> bodies = new ArrayList<>(0);

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
    id = bodies.size();
    bodies.add(this);
    gravityForces = new ArrayList<>(bodies.size() - 1);
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

  /**
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this.
   *
   * @param body second body
   */
  private void setGravityForce(Body body) {
    if (id != body.id) {
      double r = position.distanceTo(body.position);
      double f = G * (mass * body.mass / Math.pow(r, 2));
      Vector3D angle = position.angleTo(body.position);
      gravityForces.set(body.id, angle.scaleVector(f));
    } else {
      gravityForces.set(id, new Vector3D());
    }
  }

  private void update() {
    acceleration = new Vector3D();
    for (Vector3D f : gravityForces) {
      acceleration.addVector(f.scaleVector(1 / mass));
    }
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
