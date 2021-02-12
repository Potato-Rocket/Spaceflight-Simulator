package space.sim.graphics;

import space.sim.physics.Body;
import space.sim.physics.Physics;

import java.awt.*;

public class Draw {

  private Graphics2D g2d;
  private Graphics3D g3d;
  private Physics p;
  private int w;
  private int h;
  private double scale;

  Draw(Graphics graphics, Physics physics, int width, int height) {
    g2d = (Graphics2D) graphics;
    g3d = new Graphics3D(g2d);
    p = physics;
    w = width;
    h = height;
  }

  public void drawAll(double minBounds) {
    g2d.fillRect(0, 0, w, h);
    g2d.setColor(Color.WHITE);
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

    //TODO: Use a draw queue to sort lines and points by depth
    //  - Sort items by z depth
    //  - Add method to actually render everything
    for (Body body : p.bodyArray) {
      drawBody(body);
    }
  }

  private void drawBody(Body body) {
    int size = (int) body.getRadius();
    if (size / scale < 1) {
      size = (int) (1 / scale);
    }
    drawTrail(body);
    g2d.setColor(Color.WHITE);
    g3d.point(body.getPosition(), size);
  }

  private void drawTrail(Body body) {
    double increment = 256.0 / body.trail.size();
    for (int i = body.trail.size() - 1; i > 0; i--) {
      //FIXME: Use alpha rather than darkness
      g2d.setColor(new Color(0, 255 - (int) (increment * i), 0));
      g3d.line(body.trail.get(i), body.trail.get(i - 1));
    }
  }

}
