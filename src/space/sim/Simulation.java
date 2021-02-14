package space.sim;

import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;
import space.sim.config.Setup;

import java.time.Instant;

//TODO: Long term list of features to add:
//  - Basic simulation stats readout
//  - Scale bodies chromatically to differentiate them
//  - Properties setup for simulation settings
//  - Trails with segments added based on change in velocity
//  - Properties setup for initial body layout
//  - Body randomizer implementation
//  - Body selection and info readout
//  - Manipulation of view center point (maybe can follow bodies?)

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  public static long duration = 0;

  /**
   * Main method. Manages lower level classes and their processes, and contains the main loop for
   * the simulation.
   *
   * @param args command line inputs
   */
  public static void main(String[] args) {
    //Sets up graphical window and timing variables.
    DrawSpace drawSpace = new DrawSpace();
    Physics.createBodies();
    Instant now;
    int frameCap = 1000 / Setup.FRAME_LIMIT;
    int difference = frameCap;
    now = Instant.now();
    long start = now.toEpochMilli();
    //Main while loop is infinite until window is closed or program interrupted.
    while (true) {
      long millis = now.toEpochMilli();
      duration = (millis - start);
      Physics.updateBodies(difference);
      drawSpace.paint(drawSpace.getGraphics());
      now = Instant.now();
      difference = (int) (now.toEpochMilli() - millis);
      //Delays until the amount of time allotted for each frame is reached.
      if (difference < frameCap) {
        while (difference < frameCap) {
          now = Instant.now();
          difference = (int) (now.toEpochMilli() - millis);
        }
      }
    }
  }

}
