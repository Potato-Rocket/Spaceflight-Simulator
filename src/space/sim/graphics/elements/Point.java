package space.sim.graphics.elements;

import space.sim.Vector3D;

import java.awt.*;

public class Point {

  private Vector3D position;
  private int size;

  public Point(Vector3D position, int size) {
    this.position = position;
    this.size = size;
  }

  public void draw(Graphics2D g) {
    g.setColor(Color.WHITE);
    g.fillOval((int) (position.getY() - size), (int) (position.getZ() - size), size * 2, size * 2);
  }

  public double getDepth() {
    return position.getX();
  }

}
