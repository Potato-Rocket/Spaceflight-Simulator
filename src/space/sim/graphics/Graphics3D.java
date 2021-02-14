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
   * Returns a new transformed <code>Point</code> object. Runs the <code>convertPoint</code>
   * method to transform the point based on the current view.
   *
   * @param position 3D position of the point
   * @param size size of the drawn point
   * @return Returns the transformed <code>Line</code> object.
   */
  public static Point point(Vector3D position, int size) {
    return new Point(convertPoint(position), size);
  }

  /**
   * Returns a new transformed <code>Line</code> object. Runs the <code>convertPoint</code>
   * method to transform the line's endpoints based on the current view.
   *
   * @param point1 first endpoint of the line
   * @param point2 second endpoint of the line
   * @param c color of the drawn line
   * @return Returns the transformed <code>Line</code> object.
   */
  public static Line line(Vector3D point1, Vector3D point2, Color c) {
    return new Line(convertPoint(point1), convertPoint(point2), c);
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
  private static Vector3D convertPoint(Vector3D point) {
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
