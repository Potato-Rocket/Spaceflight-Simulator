package space.sim.graphics;

import space.sim.physics.Body;
import space.sim.physics.Physics;

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
    drawBodies(1000, 250);
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

    for (Body body : Physics.bodyArray) {
      drawTrail(body);
    }

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
    for (Body body : Physics.bodyArray) {
      drawBody(body, factor);
    }
  }

  private static void drawBody(Body body, double factor) {
    double[] pos = body.getPosition().getValues();
    int size = (int) body.getRadius();
    if (size / factor < 1) {
      size = (int) (1 / factor);
    }
    g.fillOval((int) pos[0] - size, (int) pos[1] - size, size * 2, size * 2);
  }

  private static void drawTrail(Body body) {
    double increment = 256.0 / body.trail.size();
    for (int i = body.trail.size() - 1; i > 0; i--) {
      double[] pos1 = body.trail.get(i).getValues();
      double[] pos2 = body.trail.get(i - 1).getValues();
      g.setColor(new Color(0, 255 - (int) (increment * i), 0));
      g.drawLine((int) pos1[0], (int) pos1[1], (int) pos2[0], (int) pos2[1]);
    }
  }

}
