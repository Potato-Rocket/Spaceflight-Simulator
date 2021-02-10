package space.sim;

import space.sim.physics.Physics;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  public static void main(String[] args) {
    Physics.createBodies();
  }

}
