package space.sim;

import space.sim.graphics.GUI;
import space.sim.physics.Physics;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  public static final double FRAME_RATE = 30;

  public static void main(String[] args) throws InterruptedException {
    GUI gui = new GUI();
    Physics.createBodies();
    int delay = (int) (1000 / FRAME_RATE);
    while (true) {
      Thread.sleep(delay);
      Physics.updateBodies(delay);
      gui.update(gui.getGraphics());
    }
  }

}
