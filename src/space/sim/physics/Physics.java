package space.sim.physics;

import space.sim.config.Setup;

import java.util.ArrayList;

/**
 * Static class to handle all body interactions.
 */
public class Physics {


  /**
   * The array of all bodies.
   */
  private static ArrayList<Body> bodyArray = new ArrayList<>();

  /**
   * Populates the array of bodies based on the generation data.
   */
  public static void createBodies() {
    for (String[] line : Setup.GEN_DATA) {
      bodyArray.add(new Body(new Vector3D(Double.parseDouble(line[0]), Double.parseDouble(line[1]),
          Double.parseDouble(line[2])), new Vector3D(Double.parseDouble(line[3]),
          Double.parseDouble(line[4]), Double.parseDouble(line[5])),
          Double.parseDouble(line[6]), line[7]));
    }
  }

  /**
   * Updates every body in the body array. Runs the update function for each body to update the
   * motion, then updates the gravitational forces between each body and every other body.
   * Finally, it checks for collisions between every body. If two bodies have collided it runs
   * the collision function on the larger one and removes the smaller one.
   *
   * @param millis time passed in milliseconds
   */
  public static void updateBodies(int millis) {
    while (millis > 0) {
      for (Body body : bodyArray) {
        body.gravityForces.clear();
        for (Body other : bodyArray) {
          body.gravityForces.add(findGravityForce(body, other));
        }
      }
      for (Body body : bodyArray) {
        body.update();
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
      millis--;
    }
  }

  /**
   * Returns information about every body in a <code>String</code>. Runs the verbose toString method
   * for each body in the bodies array and appends them into one <code>String</code>.
   */
  public static void printAll() {
    StringBuilder string = new StringBuilder();
    for (Body body : bodyArray) {
      string.append(body.toString(true)).append("\n\n");
    }
    System.out.println(string);
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
   * Calculates the gravitational pull between this body and another body. Uses their relative
   * positions and masses as well as the gravitational constant to calculate this. Does not
   * calculate for a comparison between a body and itself.
   *
   * @param body main body
   * @param other other body
   * @return Returns the gravitational force between the two bodies
   */
  private static Vector3D findGravityForce(Body body, Body other) {
    if (body.getId() != other.getId()) {
      double r = body.getPosition().distanceTo(other.getPosition());
      double f = Setup.G * ((body.getMass() * other.getMass()) / Math.pow(r, 2));
      Vector3D angle = body.getPosition().angleTo(other.getPosition());
      return angle.scaleVector(f);
    }
    return new Vector3D();
  }

}
