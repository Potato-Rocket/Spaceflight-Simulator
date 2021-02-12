package space.sim.graphics;

import space.sim.physics.Physics;

import javax.swing.JFrame;
import java.awt.*;

public class DrawSpace extends JFrame {

  public DrawSpace() {
    setTitle("Spaceflight Simulator");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    addMouseListener(new Interaction());
    addMouseMotionListener(new Rotation());
  }

  public void paint(Graphics g, Physics p) {
    Draw draw = new Draw(g, p, getWidth(), getHeight());
    draw.drawAll(1000);
    revalidate();
  }

}
