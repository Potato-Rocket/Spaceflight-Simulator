package space.sim.physics;

import space.sim.config.Setup;

import java.util.ArrayList;

/**
 * Static class to handle all body interactions.
 */
public class Physics {

  /**
   * Array of different time scales to toggle through.
   */
  private static final int[] SPEEDS = {1, 2, 4, 8, 16, 32, 64,
      100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 1000000};

  private static int scaleLimit = 7/*SPEEDS.length*/;

  /**
   * Current time scale index.
   */
  private static int timeScale = 0;

  /**
   * Simulation duration in milliseconds.
   */
  private static long duration = 0;

  /**
   * The initial max distance of any body from the origin.
   */
  private static double initBounds = 0;

  /**
   * The array of all bodies.
   */
  private static ArrayList<Body> bodyArray = new ArrayList<>();

  //TODO: Calculate sphere of influence for each body. Run only once per frame.
  /**
   * Populates the array of bodies based on the generation data. After generating the bodies,
   * gets the max initial distance of any one body from the origin.
   */
  public static void createBodies() {
    for (String[] line : Setup.getGenerationData()) {
      bodyArray.add(new Body(new Vector3D(Double.parseDouble(line[0]), Double.parseDouble(line[1]),
          Double.parseDouble(line[2])), new Vector3D(Double.parseDouble(line[3]),
          Double.parseDouble(line[4]), Double.parseDouble(line[5])),
          Double.parseDouble(line[6]), Double.parseDouble(line[7]), line[8]));
    }
    double[] distances = new double[bodyArray.size()];
    for (int i = 0; i < distances.length; i++) {
      distances[i] = bodyArray.get(i).getPosition().distanceTo();
    }
    for (double dist : distances) {
      if (dist > initBounds) {
        initBounds = dist;
      }
    }
  }

  //TODO: Implement automatic time scale limit detection.
  //TODO: Add basic multithreading.
  //TODO: On collision, update focused body if focused is >= to this body.

  /**
   * Updates every body in the body array. Runs the update function for each body to update the
   * motion, then updates the gravitational forces between each body and every other body.
   * Finally, it checks for collisions between every body. If two bodies have collided it runs
   * the collision function on the larger one and removes the smaller one.
   * <p>
   * The code to update the physics for every body is run once for every millisecond in the
   * simulation that has passed since the previous frame. That is the real time milliseconds
   * passed times the time scale.
   *
   * @param realMillis time passed in milliseconds
   */
  public static void updateBodies(int realMillis, double fps) {
    int reps = realMillis * SPEEDS[timeScale];
    double repMillis = 1;
    if (timeScale > scaleLimit) {
      reps = realMillis * SPEEDS[scaleLimit];
      repMillis = (double) SPEEDS[timeScale] / SPEEDS[scaleLimit];
    }
    while (reps > 0) {
      for (Body body : bodyArray) {
        body.gravityForces.clear();
        for (Body other : bodyArray) {
          body.gravityForces.add(findGravityForce(body, other));
        }
      }
      for (Body body : bodyArray) {
        body.update(repMillis);
      }
      for (int i = 0; i < bodyArray.size(); i++) {
        Body body = bodyArray.get(i);
        for (int j = 0; j < bodyArray.size(); j++) {
          Body other = bodyArray.get(j);
          if (body.getId() != other.getId()) {
            double distance = body.getPosition().distanceTo(other.getPosition());
            if (body.getMass() >= other.getMass() && distance < body.getRadius()) {
              body.collision(other.getPosition(), other.getVelocity(), other.getMass());
              bodyArray.remove(other);
              j--;
            }
          }
        }
      }
      duration += repMillis;
      reps--;
    }
  }

  /**
   * Getter method for the <code>ArrayList</code> of bodies.
   *
   * @return Returns the <code>ArrayList</code> of bodies.
   */
  public static ArrayList<Body> getBodyArray() {
    return bodyArray;
  }

  /**
   * Gets the initial boundaries for the simulation view. Equal th=o the furthest distance from
   * the origin any one body starts at.
   *
   * @return Returns the initial boundaries for the view.
   */
  public static double getInitBounds() {
    return initBounds;
  }

  /**
   * Increases or decreases the time scale based on the input. Is bound to a keymap in the
   * <code>Keymaps</code> class.
   *
   * @param increase whether to increase or decrease the time scale
   */
  public static void modifyTimeScale(boolean increase) {
    if (increase && timeScale < SPEEDS.length - 1) {
      timeScale++;
    } else if (!increase && timeScale > 0) {
      timeScale--;
    }
  }

  /**
   * Getter method for the current time scale in milliseconds.
   *
   * @return Returns the time scale.
   */
  public static int getTimeScale() {
    return SPEEDS[timeScale];
  }

  /**
   * Gets the duration of the simulation. This variable is increased each time the physics are
   * updated.
   *
   * @return Returns the simulation duration.
   */
  public static long getDuration() {
    return duration;
  }

  /**
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this. Does not
   * calculate for a comparison between a body and itself.
   *
   * @param body  main body
   * @param other other body
   * @return Returns the gravitational force between the two bodies
   */
  private static Vector3D findGravityForce(Body body, Body other) {
    if (body.getId() != other.getId()) {
      double r = body.getPosition().distanceTo(other.getPosition());
      double f = Setup.getGravity() * ((body.getMass() * other.getMass()) / Math.pow(r, 2));
      Vector3D angle = body.getPosition().angleTo(other.getPosition());
      return angle.scaleVector(f);
    }
    return new Vector3D();
  }
}
