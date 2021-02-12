package space.sim.graphics;

import space.sim.Vector3D;

import java.awt.*;

public class Graphics3D {

  private Graphics2D g;
  private static double rot = 0.1;
  private static double angle = 0.1;

  public Graphics3D(Graphics2D g) {
    this.g = g;
  }

  public void point(Vector3D location, int rad) {
    double[] coords = convertPoint(location);
    g.fillOval((int) (coords[0] - rad), (int) (coords[1] - rad), rad * 2, rad * 2);
  }

  public void line(Vector3D point1, Vector3D point2) {
    double[] coords1 = convertPoint(point1);
    double[] coords2 = convertPoint(point2);
    g.drawLine((int) coords1[0], (int) coords1[1], (int) coords2[0], (int) coords2[1]);
  }

  //TODO: Return the relative z depth
  private double[] convertPoint(Vector3D point) {
    double x = point.getX() * Math.cos(rot) + point.getZ() * Math.sin(rot);
    double y = point.getY() * Math.cos(angle) + point.getZ() * Math.sin(angle);
    return new double[] {x, y};
  }

}
