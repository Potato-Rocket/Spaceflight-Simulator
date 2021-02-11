package space.sim.graphics;

import space.sim.physics.Body;
import space.sim.physics.Physics;
import space.sim.physics.Vector3D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Draw {

  private static Graphics2D g;
  private static int w;
  private static int h;

  public static void drawAll(Graphics graphics, int width, int height) {
    g = (Graphics2D) graphics;
    w = width;
    h = height;
    g.fillRect(0, 0, width, height);
    drawBodies(1000, 100);
  }

  private static void drawBodies(double minBounds, int tickDistance) {
    g.translate(w / 2, h / 2);
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
    double factor = min / (minBounds * 2);
    g.scale(factor, factor);
    g.setColor(Color.WHITE);
    for (int x = 0; x < w; x += tickDistance) {
      g.drawLine(x, tickDistance / 10, x, tickDistance / -10);
    }
    for (int x = 0; x > -w; x -= tickDistance) {
      g.drawLine(x, tickDistance / 10, x, tickDistance / -10);
    }
    for (int y = 0; y < h; y += tickDistance) {
      g.drawLine(tickDistance / 10, y, tickDistance / -10, y);
    }
    for (int y = 0; y > -h; y -= tickDistance) {
      g.drawLine(tickDistance / 10, y, tickDistance / -10, y);
    }
  }

}
