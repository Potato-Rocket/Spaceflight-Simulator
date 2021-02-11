package space.sim.physics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Physics {

  /**
   * Universal gravitational constant.
   */
  public static final BigDecimal G = new BigDecimal("1");

  /**
   * The 2D array to store information about how to generate bodies.
   */
  private static final String[][] genData = {
      {"500", "1000", "0", "0", "0", "0", "10000", "10", "Body One"},
      {"-500", "0", "0", "0", "0", "0", "10000", "10", "Body Two"},
      {"0", "-1000", "0", "0", "0", "0", "10000", "10", "Body Three"}};

  /**
   * The array of all bodies.
   */
  public static ArrayList<Body> bodyArray = new ArrayList<>();

  public static void createBodies() {
    for (String[] line : genData) {
      bodyArray.add(new Body(
          new Vector3D(new BigDecimal(line[0]), new BigDecimal(line[1]), new BigDecimal(line[2])),
          new Vector3D(new BigDecimal(line[3]), new BigDecimal(line[4]), new BigDecimal(line[5])),
          new BigDecimal(line[6]), new BigDecimal(line[7]), line[8]));
    }
  }

  /**
   * Updates every body. Runs the update function for each body to update the motion. Then
   * updates the gravitational forces between each body and every other body.
   */
  public static void updateBodies() {
    for (Body body : bodyArray) {
      body.gravityForces.clear();
      for (Body other : bodyArray) {
        body.gravityForces.add(findGravityForce(body, other));
      }
    }
    for (Body body : bodyArray) {
      body.update();
    }
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
  public static Vector3D findGravityForce(Body body, Body other) {
    if (body.getId() != other.getId()) {
      BigDecimal r = body.getPosition().distanceTo(other.getPosition());
      BigDecimal f = G.multiply(body.getMass().multiply(other.getMass()).setScale(32,
          RoundingMode.DOWN).
          divide(r.pow(2), RoundingMode.DOWN));
      Vector3D angle = body.getPosition().angleTo(other.getPosition());
      return angle.scaleVector(f);
    }
    return new Vector3D();
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

}
