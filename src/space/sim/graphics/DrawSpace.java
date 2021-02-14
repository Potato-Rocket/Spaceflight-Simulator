package space.sim.graphics;

import space.sim.interaction.EnterRotation;
import space.sim.interaction.Rotation;
import space.sim.interaction.Zooming;

import javax.swing.JFrame;
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
    setTitle("Space Simulator");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    addMouseListener(new EnterRotation());
    addMouseMotionListener(new Rotation());
    addMouseWheelListener(new Zooming());
  }

  /**
   * Updates the graphics in the frame. Creates a new <code>Draw</code> object and runs the
   * <code>drawAll</code> method. Finally, it revalidates to ensure smoother animations.
   *
   * @param g current <code>Graphics</code> object
   */
  public void paint(Graphics g) {
    Draw draw = new Draw(g, getWidth(), getHeight());
    draw.drawAll();
    revalidate();
  }

}
