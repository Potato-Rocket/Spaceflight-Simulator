package space.sim.graphics;

import javax.swing.JFrame;
import java.awt.*;

//TODO: Add mouse interaction
//  - Add MouseListener and MouseMovementListener to JFrame
//  - Create class to manipulate the view based on mouse actions
public class DrawSpace extends JFrame {

  public DrawSpace() {
    setTitle("Spaceflight Simulator");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public void paint(Graphics g) {
    Draw draw = new Draw(g, getWidth(), getHeight());
    draw.drawAll(1000);
    revalidate();
  }

}
