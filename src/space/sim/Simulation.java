package space.sim;

import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;

import java.time.Instant;

//TODO: Long term list of features to add:
//  - Scale indicators with unit scale readout
//  - Scale bodies chromatically to differentiate them
//  - Properties setup for simulation settings
//  - Trails with segments added based on change in velocity
//  - Properties setup for initial body layout
//  - Body randomizer implementation
//  - Body selection and info readout
//  - Basic simulation stats readout
//  - Manipulation of view center point (maybe can follow bodies?)

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  /**
   * Stores the artificial frame rate limit.
   */
  private static final int FRAME_LIMIT = 60;

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
    Instant time;
    int frameCap = 1000 / FRAME_LIMIT;
    int difference = frameCap;
    time = Instant.now();
    //Main while loop is infinite until window is closed or program interrupted.
    while (true) {
      long millis = time.toEpochMilli();
      Physics.updateBodies(difference);
      drawSpace.paint(drawSpace.getGraphics());
      time = Instant.now();
      difference = (int) (time.toEpochMilli() - millis);
      //Delays until the amount of time allotted for each frame is reached.
      if (difference < frameCap) {
        while (difference < frameCap) {
          time = Instant.now();
          difference = (int) (time.toEpochMilli() - millis);
        }
      }
    }
  }

}
