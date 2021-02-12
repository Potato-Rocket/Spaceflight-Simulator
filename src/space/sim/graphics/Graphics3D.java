package space.sim.graphics;

import space.sim.Vector3D;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;

public abstract class Graphics3D {

  private static double yaw = 0;
  private static double pitch = 0;
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

  public static void changePitch(double angle) {
    pitch += angle;
    if (pitch > MAX_PITCH) {
      pitch = MAX_PITCH;
    }
    if (pitch < MIN_PITCH) {
      pitch = MIN_PITCH;
    }
  }

  //FIXME: Reevaluate and fix the Y and Z conversion
  private static Vector3D convertPoint(Vector3D point) {
//    //Yaw
    double x =
        point.getX() * (Math.cos(yaw)) + point.getY() * (-Math.sin(yaw)) + point.getZ() * (0);
    double y =
        point.getX() * (Math.sin(yaw)) + point.getY() * (Math.cos(yaw)) + point.getZ() * (0);
    double z =
        point.getX() * (0) + point.getY() * (0) + point.getZ() * (1);

//    //Pitch
//    double x =
//        point.getX() * (Math.cos(pitch)) + point.getY() * (0) + point.getZ() * (Math.sin(pitch));
//    double y =
//        point.getX() * (0) + point.getY() * (1) + point.getZ() * (0);
//    double z =
//        point.getX() * (-Math.sin(pitch)) + point.getY() * (0) + point.getZ() * (Math.cos(pitch));

    return new Vector3D(x, y, z);
  }

}
