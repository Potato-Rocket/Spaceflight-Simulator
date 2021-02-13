package space.sim.graphics.elements;

import space.sim.physics.Vector3D;

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
    g.drawLine((int) point1.getY(), (int) point1.getZ(), (int) point2.getY(), (int) point2.getZ());
  }

  public double getDepth() {
    if (point1.getX() < point2.getX()) {
      return point1.getX();
    }
    return point2.getX();
  }

}
