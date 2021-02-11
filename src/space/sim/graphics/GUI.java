package space.sim.graphics;

import javax.swing.JFrame;
import java.awt.*;

public class GUI extends JFrame {

  public GUI() {
    setTitle("Spaceflight Simulator");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public void paint(Graphics g) {
    Draw.drawAll(g, getWidth(), getHeight());
  }

}
