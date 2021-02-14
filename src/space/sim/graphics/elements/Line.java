package space.sim.graphics.elements;

import space.sim.physics.Vector3D;

import java.awt.*;

/**
 * Stores information about a line to draw.
 */
public class Line {

  /**
   * Stores the position of the line's first endpoint.
   */
  private Vector3D point1;
  /**
   * Stores the position of the line's second endpoint.
   */
  private Vector3D point2;
  /**
   * Stores the color of the line to draw.
   */
  private Color color;

  /**
   * Class constructor. Takes the position of both endpoints as an input as well as the color of
   * the line.
   *
   * @param point1 first endpoint
   * @param point2 second endpoint
   * @param c color of the line
   */
  public Line(Vector3D point1, Vector3D point2, Color c) {
    this.point1 = point1;
    this.point2 = point2;
    color = c;
  }

  /**
   * Draws the line on the screen. Takes the current <code>Graphics2D</code> object as an input.
   *
   * @param g current <code>Graphics2D</code> object
   */
  public void draw(Graphics2D g) {
    g.setColor(color);
    g.drawLine((int) point1.getY(), (int) point1.getZ(), (int) point2.getY(), (int) point2.getZ());
  }

  /**
   * Gets the line's average depth relative to the view.
   *
   * @return Returns the point's average distance along the axis perpendicular to the viewing angle.
   */
  public double getDepth() {
    return (point1.getX() + point2.getX()) / 2;
  }

}
