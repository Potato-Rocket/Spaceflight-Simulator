package space.sim;

import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  public static final double FRAME_RATE = 10;

  //TODO: Add user inputs before creating the window and bodies
  public static void main(String[] args) throws InterruptedException {
    DrawSpace drawSpace = new DrawSpace();
    Physics.createBodies();
    int delay = (int) (1000 / FRAME_RATE);
    while (true) {
      Thread.sleep(delay);
      Physics.updateBodies(delay);
      drawSpace.update(drawSpace.getGraphics());
    }
  }

}
