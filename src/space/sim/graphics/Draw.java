package space.sim.graphics;

import space.sim.Simulation;
import space.sim.physics.Vector3D;
import space.sim.physics.Body;
import space.sim.physics.Physics;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class to create all the drawings and graphics. Runs the 3D transformation code and
 * organises the 3D elements before drawing them.
 */
public class Draw {

  /**
   * Stores whether or not to render the trails with transparency.
   */
  private static final boolean TRANSPARENCY = true;
  /**
   * Factor to scale by when zooming.
   */
  private static final double SCALE_FACTOR = 1.1;
  /**
   * Stores the scale factor between the virtual units and the pixels on screen.
   */
  private static double scale = 1;

  /**
   * Stores element objects to draw.
   */
  private static ArrayList<Object> elements = new ArrayList<>();
  /**
   * Simulated units to fit on the screen
   */
  private static double minBounds = 1000;

  /**
   * Stores the <code>Graphics2D</code> object used to create all graphics.
   */
  private Graphics2D g2d;
  /**
   * Stores the frame width.
   */
  private int w;
  /**
   * Stores the frame height.
   */
  private int h;

  /**
   * Class constructor. Takes the <code>Graphics</code> object provided by the <code>paint</code>
   * method in the <code>drawSpace</code> class as well as the frame's width and height.
   *
   * @param graphics current <code>Graphics</code> object
   * @param width width of the frame
   * @param height height of the frame
   */
  public Draw(Graphics graphics, int width, int height) {
    g2d = (Graphics2D) graphics;
    w = width;
    h = height;
  }

  /**
   * Draws everything. Operates in the following order:
   * <ol>
   *   <li>Scales and transforms the <code>Graphics</code> object's coordinate system as required
   *   by the defined scale of the view and the 3D origin point.</li>
   *   <li>3D axes to aid with orientation.</li>
   *   <li>Draws the physical bodies and their trails. Uses the <code>Graphics3D</code> class
   *   to get the <code>Line</code> and <code>Point</code> objects describing the elements to
   *   be rendered. These objects are added to an array which sorts the elements by their
   *   apparent depth before finally rendering them on the screen.</li>
   *   <li>Adds a 2D crosshair at the center of the screen, as well as tick marks to indicate
   *   scale.</li>
   * </ol>
   */
  public void drawAll() {
    //Transforms the coordinate grid
    g2d.translate(w / 2, h / 2);
    int min = h;
    if (h > w) {
      min = w;
    }
    w /= 2;
    h /=2;
    scale = min / (minBounds * 2);
    elements.clear();
    drawAxes();
    for (Body body : Physics.getBodyArray()) {
      drawBody(body);
    }
    render();
    drawGuides();
    drawText(new String[] {"Duration: " + Simulation.duration + "ms"}, -w + 10, -h + 10, 1.2);
  }

  /**
   * Modifies the scale of the vie by the scale factor.
   *
   * @param direction Which direction the zooming is going in.
   */
  public static void modifyBounds(int direction) {
    if (direction == 1) {
      minBounds *= SCALE_FACTOR;
    } else {
      minBounds /= SCALE_FACTOR;
    }
  }

  /**
   * Getter method for the current drawing scale factor.
   *
   * @return Returns the drawing scale.
   */
  public static double getScale() {
    return scale;
  }

  /**
   * Draws the 3D axes. The X-axis is red, the Y-axis is green, and the
   * Z-axis is blue. The axes' positive sections appear brighter than their negative
   * sections.
   */
  private void drawAxes() {
    elements.add(new Line(new Vector3D(minBounds / 2, 0, 0),
        new Vector3D(0, 0, 0), new Color(255, 0, 0)));
    elements.add(new Line(new Vector3D(minBounds / -2, 0, 0),
        new Vector3D(0, 0, 0), new Color(63, 0, 0)));
    elements.add(new Line(new Vector3D(0, minBounds / 2, 0),
        new Vector3D(0, 0, 0), new Color(0, 255, 0)));
    elements.add(new Line(new Vector3D(0, minBounds / -2, 0),
        new Vector3D(0, 0, 0), new Color(0, 63, 0)));
    elements.add(new Line(new Vector3D(0, 0, minBounds / 2),
        new Vector3D(0, 0, 0), new Color(0, 0, 255)));
    elements.add(new Line(new Vector3D(0, 0, minBounds / -2),
        new Vector3D(0, 0, 0), new Color(0, 0, 63)));
  }

  /**
   * Draws a crosshair and tick marks for scale. Maintains the distance between the tick marks at
   * a factor of 10 and makes every 10th tick mark longer.
   */
  private void drawGuides() {
    g2d.setColor(Color.WHITE);
    g2d.drawLine(10, 0, -10, 0);
    g2d.drawLine(0, 10, 0, -10);

    int tickExp = 1;
    int tickDist = 10;
    while (h / (tickDist * scale) > 2) {
      tickExp++;
      tickDist = (int) Math.pow(10, tickExp);
    }
    tickExp--;
    tickDist = (int) Math.pow(10, tickExp);
    for (int x = 0; x < w; x += tickDist * scale) {
      int len = 10;
      if (Math.round(x / (tickDist * scale)) % 10 == 0) {
        len = 20;
      }
      g2d.drawLine(x, h, x, h - len);
    }
    for (int x = 0; x > -w; x -= tickDist * scale) {
      int len = 10;
      if (Math.round(x / (tickDist * scale)) % 10 == 0) {
        len = 20;
      }
      g2d.drawLine(x, h, x, h - len);
    }
    for (int y = 0; y < h; y += tickDist * scale) {
      int len = 10;
      if (Math.round(y / (tickDist * scale)) % 10 == 0) {
        len = 20;
      }
      g2d.drawLine(w, y, w - len, y);
    }
    for (int y = 0; y > -h; y -= tickDist * scale) {
      int len = 10;
      if (Math.round(y / (tickDist * scale)) % 10 == 0) {
        len = 20;
      }
      g2d.drawLine(w, y, w - len, y);
    }
  }

  private void drawText(String[] text, int x, int y, double lineSpacing) {
    for (int l = 0; l < text.length; l++) {
      g2d.drawString(text[l], x, y + 9 + (int) (lineSpacing * 10 * l));
    }
  }

  /**
   * Sorts the elements array by depth before running the <code>draw</code> function on each
   * element.
   */
  private void render() {
    Object[] elementArray = elements.toArray();
    quickSort(elementArray, 0, elementArray.length - 1);
    for (Object e : elementArray) {
      if (e instanceof Point) {
        ((Point) e).draw(g2d);
      } else if (e instanceof Line) {
        ((Line) e).draw(g2d);
      }
    }
  }

  /**
   * Adds a <code>Point</code> object to the elements array. Uses the given body's position and
   * size to generate the new object using <code>Graphics3D</code>.
   *
   * @param body body to draw
   */
  private void drawBody(Body body) {
    int size = (int) body.getRadius();
    if (size * scale < 2) {
      size = (int) (2 / scale);
    }
    drawTrail(body);
    elements.add(new Point(body.getPosition(), size));
  }

  /**
   * Adds <code>Line</code> objects to the elements array. Uses the given body's trail data to
   * generate a line for each trail segment using <code>Graphics3D</code>.
   *
   * @param body body to draw the trail for
   */
  private void drawTrail(Body body) {
    for (int i = body.trail.size() - 1; i > 0; i--) {
      Color c;
      double fade = 1.0 - (1.0 / body.trail.size() * i);
      if (TRANSPARENCY) {
        c = new Color(255, 255, 0, (int) (255 * fade));
      } else {
        c = new Color( (int) (255 * fade),  (int) (255 * fade), 0);
      }
      elements.add(new Line(body.trail.get(i), body.trail.get(i - 1), c));
    }
  }

  /**
   * Runs the quicksort sorting algorithm on an array of elements. This sorts by depth and is a
   * recursive algorithm.
   * <p>
   * Information on the quicksort algorithm can be found here on Wikipedia:
   * <a href="https://en.wikipedia.org/wiki/Quicksort">Quicksort</a>
   *
   * @param arr array to sort
   * @param begin beginning index of the range to compare
   * @param end ending index of the range to compare
   */
  private void quickSort(Object[] arr, int begin, int end) {
    if (begin < end) {
      double pivot = getDepth(arr[end]);
      int i = (begin-1);
      for (int j = begin; j < end; j++) {
        if (getDepth(arr[j]) <= pivot) {
          i++;
          Object swapTemp = arr[i];
          arr[i] = arr[j];
          arr[j] = swapTemp;
        }
      }
      Object swapTemp = arr[i+1];
      arr[i+1] = arr[end];
      arr[end] = swapTemp;
      int partitionIndex = i+1;
      quickSort(arr, begin, partitionIndex-1);
      quickSort(arr, partitionIndex+1, end);
    }
  }

  /**
   * Gets the relative depth of any 3D element. Functions by determining  the element type, then
   * calling its <code>getDepth</code> method.
   * <p>
   * Functions for:
   * <ul>
   *   <li>Any <code>Point</code> object.</li>
   *   <li>Any <code>Line</code> object.</li>
   * </ul>
   *
   * @param e object to get the depth of
   * @return Returns the relative depth of the object.
   */
  private double getDepth(Object e) {
    if (e instanceof Point) {
      return ((Point) e).getDepth();
    } else if (e instanceof Line) {
      return ((Line) e).getDepth();
    }
    return 0;
  }

}
