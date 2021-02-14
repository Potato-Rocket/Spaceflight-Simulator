package space.sim.graphics.elements;

import space.sim.physics.Vector3D;

import java.awt.*;

/**
 * Stores information about a 3D point to be drawn.
 */
public class Point {

  /**
   * Stores the point's position.
   */
  private Vector3D position;
  /**
   * Stores the point's size (the radius).
   */
  private int size;

  /**
   * Class constructor. Takes the 3D position and size (radius) as inputs to assign to private
   * variables.
   *
   * @param position 3D position of the point
   * @param size size of the point
   */
  public Point(Vector3D position, int size) {
    this.position = position;
    this.size = size;
  }

  /**
   * Draws the point on the screen. Takes the current <code>Graphics2D</code> object as an input.
   *
   * @param g current <code>Graphics2D</code> object
   */
  public void draw(Graphics2D g) {
    g.setColor(Color.WHITE);
    g.fillOval((int) (position.getY() - size), (int) (position.getZ() - size), size * 2, size * 2);
  }

  /**
   * Gets the point's depth relative to the view.
   *
   * @return Returns the point's distance along the axis perpendicular to the viewing angle.
   */
  public double getDepth() {
    return position.getX();
  }

}
