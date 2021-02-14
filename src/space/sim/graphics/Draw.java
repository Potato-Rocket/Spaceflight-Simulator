package space.sim.graphics;

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
   * Stores the scale factor between the virtual units and the pixels on screen.
   */
  private double scale;
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
   *   <li>Fills the background with black.</li>
   *   <li>Scales and transforms the <code>Graphics</code> object's coordinate system as required
   *   by the defined scale of the view and the 3D origin point.
   *   </li>
   *   <li>3D axes to aid with orientation. The X-axis is red, the Y-axis is green, and the
   *   Z-axis is blue. The axes' positive sections appear brighter than their negative
   *   sections.</li>
   *   <li>Draws the physical bodies and their trails. Uses the <code>Graphics3D</code> class
   *   to get the <code>Line</code> and <code>Point</code> objects describing the elements to
   *   be rendered. These objects are added to an array which sorts the elements by their
   *   apparent depth before finally rendering them on the screen.</li>
   *   <li>Adds a 2D crosshair at the center of the screen.</li>
   * </ol>
   */
  public void drawAll() {
    g2d.fillRect(0, 0, w, h);
    //Transforms the coordinate grid
    g2d.translate(w / 2, h / 2);
    int min = h;
    double ratio = (double) w / h;
    if (h > w) {
      min = w;
      w = (int) minBounds;
      h = (int) (minBounds / ratio);
    } else {
      h = (int) minBounds;
      w = (int) (minBounds * ratio);
    }
    scale = min / (minBounds * 2);
    g2d.scale(scale, scale);
    //Draws the 3D axes
    elements.clear();
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
    //Draws the bodies and their respective trails
    for (Body body : Physics.getBodyArray()) {
      drawBody(body);
    }
    render();
    //Draws the 2D crosshair
    g2d.setColor(Color.WHITE);
    g2d.drawLine((int) (10 / scale), 0, (int) (-10 / scale), 0);
    g2d.drawLine(0, (int) (10 / scale), 0, (int) (-10 / scale));
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
