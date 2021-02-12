package space.sim.graphics.elements;

import space.sim.Vector3D;

import java.awt.*;

public class Line {

  private Vector3D point1;
  private Vector3D point2;
  private Color color;

  public Line(Vector3D point1, Vector3D point2, Color c) {
    this.point1 = point1;
    this.point2 = point2;
    color = c;
  }

  public void draw(Graphics2D g) {
    g.setColor(color);
    g.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());
  }

  public double getDepth() {
    if (point1.getZ() < point2.getZ()) {
      return point1.getZ();
    }
    return point2.getZ();
  }

}
