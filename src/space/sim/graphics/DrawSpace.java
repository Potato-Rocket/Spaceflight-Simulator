package space.sim.graphics;

import space.sim.interaction.EnterRotation;
import space.sim.interaction.Rotation;
import space.sim.interaction.Zooming;
import space.sim.interaction.Keymaps;

import javax.swing.*;
import java.awt.*;

/**
 * Class to handle the <code>JFrame</code> and the highest level of graphics operations.
 */
public class DrawSpace extends JFrame {

  /**
   * Sets up a new <code>JFrame</code> and sets the required settings. Adds various listeners
   * required for user interaction.
   */
  public DrawSpace() {
    setTitle("Spaceflight Simulator");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    addMouseListener(new EnterRotation());
    addMouseMotionListener(new Rotation());
    addMouseWheelListener(new Zooming());
    new Keymaps(rootPane);
  }

  /**
   * Updates the graphics in the frame. Creates a new <code>Draw</code> object and runs the
   * <code>drawAll</code> method. Finally, it revalidates to ensure smoother animations.
   *
   * @param g current <code>Graphics</code> object
   */
  public void paint(Graphics g) {
    g.setFont(new Font("Roboto Mono Thin", Font.PLAIN, 12));
    g.fillRect(0, 0, getWidth(), getHeight());
    Draw draw = new Draw(g, getWidth(), getHeight());
    draw.drawAll();
    revalidate();
  }

}
