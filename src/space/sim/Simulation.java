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
      Thread.sleep((int) 1000 / 10);
      Physics.updateBodies();
      gui.update(gui.getGraphics());
    }
  }

}
