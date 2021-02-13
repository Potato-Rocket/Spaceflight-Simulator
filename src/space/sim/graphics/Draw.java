package space.sim.graphics;

import space.sim.physics.Vector3D;
import space.sim.physics.Body;
import space.sim.physics.Physics;
import space.sim.graphics.elements.Point;
import space.sim.graphics.elements.Line;

import java.awt.*;
import java.util.ArrayList;

public class Draw {

  private static final boolean TRANSPARENCY = false;

  private Graphics2D g2d;
  private Physics p;
  private int w;
  private int h;
  private double scale;

  private static ArrayList<Object> elements = new ArrayList<>();

  Draw(Graphics graphics, Physics physics, int width, int height) {
    g2d = (Graphics2D) graphics;
    p = physics;
    w = width;
    h = height;
  }

  public void drawAll(double minBounds) {
    g2d.fillRect(0, 0, w, h);
    g2d.translate(w / 2, h / 2);
    g2d.drawLine(-10, 0, 10, 0);
    g2d.drawLine(0, -10, 0, 10);

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

    elements.clear();
    elements.add(Graphics3D.line(new Vector3D(minBounds / 2, 0, 0), new Vector3D(0, 0, 0),
        new Color(255, 0, 0)));
    elements.add(Graphics3D.line(new Vector3D(minBounds / -2, 0, 0), new Vector3D(0, 0, 0),
        new Color(63, 0, 0)));
    elements.add(Graphics3D.line(new Vector3D(0, minBounds / 2, 0), new Vector3D(0, 0, 0),
        new Color(0, 255, 0)));
    elements.add(Graphics3D.line(new Vector3D(0, minBounds / -2, 0), new Vector3D(0, 0, 0),
        new Color(0, 63, 0)));
    elements.add(Graphics3D.line(new Vector3D(0, 0, minBounds / 2), new Vector3D(0, 0, 0),
        new Color(0, 0, 255)));
    elements.add(Graphics3D.line(new Vector3D(0, 0, minBounds / -2), new Vector3D(0, 0, 0),
        new Color(0, 0, 63)));
    render();

    elements.clear();
    for (Body body : p.bodyArray) {
      drawBody(body);
    }
    render();

    g2d.setColor(Color.WHITE);
    g2d.drawLine((int) (10 / scale), 0, (int) (-10 / scale), 0);
    g2d.drawLine(0, (int) (10 / scale), 0, (int) (-10 / scale));
  }

  private void drawBody(Body body) {
    int size = (int) body.getRadius();
    if (size / scale < 2) {
      size = (int) (2 / scale);
    }
    drawTrail(body);
    elements.add(Graphics3D.point(body.getPosition(), size));
  }

  private void drawTrail(Body body) {
    for (int i = body.trail.size() - 1; i > 0; i--) {
      Color c;
      if (TRANSPARENCY) {
        c = new Color(0, 255, 0, 255 - (int) (255.0 / body.trail.size() * i));
      } else {
        c = new Color(0, 255 - (int) (255.0 / body.trail.size() * i), 0);
      }
      elements.add(Graphics3D.line(body.trail.get(i), body.trail.get(i - 1), c));
    }
  }

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

  public void quickSort(Object[] arr, int begin, int end) {
    if (begin < end) {
      int partitionIndex = partition(arr, begin, end);
      quickSort(arr, begin, partitionIndex-1);
      quickSort(arr, partitionIndex+1, end);
    }
  }

  private int partition(Object[] arr, int begin, int end) {
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
    return i+1;
  }

  public double getDepth(Object e) {
    if (e instanceof Point) {
      return ((Point) e).getDepth();
    } else if (e instanceof Line) {
      return ((Line) e).getDepth();
    }
    return 0;
  }

}
