package space.sim.graphics;

import space.sim.physics.Body;
import space.sim.physics.Physics;
import space.sim.physics.Vector3D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class Draw {

  public static void drawAll(Graphics graphics, int width, int height) {
    Graphics2D g = (Graphics2D) graphics;

    g.fillRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    for (Body body : Physics.bodyArray) {
      g.drawString(body.toString(true), 10, 12 + 24 * body.getId());
    }

  }

}
