package space.sim;

import space.sim.graphics.GUI;
import space.sim.physics.Physics;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  public static void main(String[] args) throws InterruptedException {
    GUI gui = new GUI();
    Physics.createBodies();
    while (true) {
      Thread.sleep(1000);
      gui.update(gui.getGraphics());
    }
  }

}
