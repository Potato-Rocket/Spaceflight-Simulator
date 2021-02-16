package space.sim.graphics.elements;

import space.sim.graphics.Draw;
import space.sim.graphics.Graphics3D;
import space.sim.physics.Vector3D;

import java.awt.*;

/**
 * Stores information about a 3D point to be drawn.
 */
public class Point {

  /**
   * Stores the point's position transformed to the 3D view.
   */
  private Vector3D converted;

  /**
   * Stores the color of this body.
   */
  private Color color;

  /**
   * Stores the point's size (the radius).
   */
  private int size;


  //TODO: Have size scale to bounds and largest body.
  /**
   * Class constructor. Takes the 3D position and size (radius) as inputs, and uses
   * <code>Graphics3D</code> to transform them to the viewing angle.
   *
   * @param position 3D position of the point
   * @param focus center point of the view
   * @param size size of the point
   */
  public Point(Vector3D position, Vector3D focus, Color c, double size) {
    this.converted = Graphics3D.convertPoint(position, focus).scaleVector(Draw.getScale());
    this.color = c;
    this.size = (int) (size * Draw.getScale());
    if (this.size < 2) {
      this.size = 2;
    }
  }
  /**
   * Draws the point on the screen. Takes the current <code>Graphics2D</code> object as an input.
   *
   * @param g current <code>Graphics2D</code> object
   */
  public void draw(Graphics2D g) {
    g.setColor(color);
    g.fillOval((int) (converted.getY() - size), (int) (converted.getZ() - size), size * 2, size * 2);
  }

  /**
   * Gets the point's depth relative to the view.
   *
   * @return Returns the point's distance along the axis perpendicular to the viewing angle.
   */
  public double getDepth() {
    return converted.getX();
  }

}
