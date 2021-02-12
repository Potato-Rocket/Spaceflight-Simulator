package space.sim.graphics;

import space.sim.Vector3D;

import java.awt.*;

public class Graphics3D {

  private Graphics2D g;
  private static double rot = 1;
  private static double angle = 0;

  public Graphics3D(Graphics2D g) {
    this.g = g;
  }

  public void point(Vector3D position, int rad) {
    Vector3D coords = convertPoint(position);
    g.fillOval((int) (coords.getX() - rad), (int) (coords.getY() - rad), rad * 2, rad * 2);
  }

  public void line(Vector3D point1, Vector3D point2) {
    Vector3D coords1 = convertPoint(point1);
    Vector3D coords2 = convertPoint(point2);
    g.drawLine((int) coords1.getX(), (int) coords1.getY(), (int) coords2.getX(),
        (int) coords2.getY());
  }

  private Vector3D convertPoint(Vector3D point) {
    double x = point.getX() * Math.cos(rot) + point.getZ() * Math.sin(rot);
    double y = point.getY() * Math.cos(angle) + point.getZ() * Math.sin(angle);
    double z = point.getZ() * Math.cos(rot) + point.getZ() * Math.sin(angle);
    return new Vector3D(x, y, z);
  }

}
