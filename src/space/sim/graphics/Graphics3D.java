package space.sim.graphics;

import space.sim.Vector3D;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;

public abstract class Graphics3D {

  private static double yaw = 0;
  private static double tilt = 0;
  private static final double MIN_PITCH = Math.toRadians(-90);
  private static final double MAX_PITCH = Math.toRadians(90);

  public Graphics3D() {
  }

  public static Point point(Vector3D position, int size) {
    return new Point(convertPoint(position), size);
  }

  public static Line line(Vector3D point1, Vector3D point2, Color c) {
    return new Line(convertPoint(point1), convertPoint(point2), c);
  }

  public static void changeYaw(double angle) {
    yaw += angle;
  }

  public static void changeTilt(double angle) {
    tilt += angle;
    if (tilt > MAX_PITCH) {
      tilt = MAX_PITCH;
    }
    if (tilt < MIN_PITCH) {
      tilt = MIN_PITCH;
    }
  }

  private static Vector3D convertPoint(Vector3D point) {
    double pitch = tilt * cos(yaw);
    double roll = tilt * sin(yaw);
    double x =
        point.getX() * (cos(yaw) * cos(pitch)) +
        point.getY() * (cos(yaw) * sin(pitch) * sin(roll) - sin(yaw) * cos(roll)) +
        point.getZ() * (cos(yaw) * sin(pitch) * cos(roll) + sin(yaw) * sin(roll));
    double y =
        point.getX() * (sin(yaw) * cos(pitch)) +
        point.getY() * (sin(yaw) * sin(pitch) * sin(roll) + cos(yaw) * cos(roll)) +
        point.getZ() * (sin(yaw) * sin(pitch) * cos(roll) - cos(yaw) * sin(roll));
    double z =
        point.getX() * (-sin(pitch)) +
        point.getY() * (cos(pitch) * sin(roll)) +
        point.getZ() * (cos(pitch) * cos(roll));

    return new Vector3D(x, y, z);
  }

  private static double sin(double theta) {
    return Math.sin(theta);
  }

  private static double cos(double theta) {
    return Math.cos(theta);
  }

}
