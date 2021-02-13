package space.sim.graphics;

import space.sim.physics.Vector3D;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;

public class Graphics3D {

  private static double yaw;
  private static double tilt;

  public static Point point(Vector3D position, int size) {
    return new Point(convertPoint(position), size);
  }

  public static Line line(Vector3D point1, Vector3D point2, Color c) {
    return new Line(convertPoint(point1), convertPoint(point2), c);
  }

  public static void changeYaw(double angle) {
    yaw += angle;
    yaw = yaw % (Math.PI * 2);
    if (yaw < 0) {
      yaw += Math.PI * 2;
    }
  }

  public static void changeTilt(double angle) {
    tilt += angle;
    if (tilt > Math.PI / 2) {
      tilt = Math.PI / 2;
    }
    if (tilt < Math.PI / -2) {
      tilt = Math.PI / -2;
    }
  }

  private static Vector3D convertPoint(Vector3D point) {

    double yawX = point.getX() * Math.cos(yaw) - point.getY() * Math.sin(yaw);
    double yawY = point.getX() * Math.sin(yaw) + point.getY() * Math.cos(yaw);
    double yawZ = point.getZ();

    double finalX = yawX * Math.cos(tilt) + yawZ * Math.sin(tilt);
    double finalY = yawY;
    double finalZ = yawZ * Math.cos(tilt) - yawX * Math.sin(tilt);

    return new Vector3D(finalX, finalY, finalZ);
  }

}
