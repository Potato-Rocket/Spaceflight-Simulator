package space.sim.graphics;

import space.sim.Vector3D;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;

public abstract class Graphics3D {

  private static double rot = 0;
  private static double angle = 0;
  private static final double MIN_ANGLE = Math.toRadians(-90);
  private static final double MAX_ANGLE = Math.toRadians(90);

  public Graphics3D() {
  }

  public static Point point(Vector3D position, int size) {
    return new Point(convertPoint(position), size);
  }

  public static Line line(Vector3D point1, Vector3D point2, Color c) {
    return new Line(convertPoint(point1), convertPoint(point2), c);
  }

  public static void changeRot(double rot) {
    Graphics3D.rot += rot;
  }

  public static void changeAngle(double a) {
    angle += a;
    if (angle > MAX_ANGLE) {
      angle = MAX_ANGLE;
    }
    if (angle < MIN_ANGLE) {
      angle = MIN_ANGLE;
    }
  }

  //FIXME: Reevaluate to ensure it is functioning as intended
  private static Vector3D convertPoint(Vector3D point) {
    double x = point.getX() * Math.cos(rot) + point.getZ() * Math.sin(rot);
    double y = point.getY() * Math.cos(angle) + point.getZ() * Math.sin(angle);
    double z = point.getZ() * Math.cos(rot) + point.getZ() * Math.sin(angle);
    return new Vector3D(x, y, z);
  }

}
