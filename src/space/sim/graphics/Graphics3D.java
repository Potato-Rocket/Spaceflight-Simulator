package space.sim.graphics;

import space.sim.physics.Vector3D;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;

/**
 * Static class to handle conversion between 3D and 2D graphics. Stores the current yaw and tilt
 * of the view, which is later used to transform the coordinates of a point so it can be
 * rendered correctly.
 */
public class Graphics3D {

  /**
   * Stores the yaw, or rotation, of the current view.
   */
  private static double yaw;
  /**
   * Stores the tilt, or inclination, of the current view.
   */
  private static double tilt;

  /**
   * Alters the current yaw, or rotation of the view.
   *
   * @param angle change in radians
   */
  public static void changeYaw(double angle) {
    yaw += angle;
    yaw = yaw % (Math.PI * 2);
    if (yaw < 0) {
      yaw += Math.PI * 2;
    }
  }

  /**
   * Alters the current tilt, or inclination of the view.
   *
   * @param angle change in radians
   */
  public static void changeTilt(double angle) {
    tilt += angle;
    if (tilt > Math.PI / 2) {
      tilt = Math.PI / 2;
    }
    if (tilt < Math.PI / -2) {
      tilt = Math.PI / -2;
    }
  }

  /**
   * Transforms a point in 3D space to be displayed in 2D space. Uses the yaw and tilt of the
   * view to effectively rotate the point about the origin. This allows the 3D <b>y</b>
   * and <b>z</b> axes to be projected to the 2D <b>x</b> and <b>y</b> axes, giving the illusion
   * of 3D rotation.
   *
   * @param point point to be transformed
   * @return Returns the transformed point.
   */
  public static Vector3D convertPoint(Vector3D point) {
    //Transforms the point based on the yaw angle.
    double x = point.getX() * Math.cos(yaw) - point.getY() * Math.sin(yaw);
    double y = point.getX() * Math.sin(yaw) + point.getY() * Math.cos(yaw);
    //Transforms the new point based on the tilt angle.
    double finalX = x * Math.cos(tilt) + point.getZ() * Math.sin(tilt);
    double finalZ = point.getZ() * Math.cos(tilt) - x * Math.sin(tilt);
    //Returns the composite transformation
    return new Vector3D(finalX, y, finalZ);
  }

}
