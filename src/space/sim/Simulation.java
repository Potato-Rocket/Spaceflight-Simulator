package space.sim;

import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;
import space.sim.config.Setup;

import java.io.IOException;
import java.time.Instant;

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
  public static void main(String[] args) throws IOException {
    //Sets up graphical window and timing variables.
    Setup.read();
    Physics.createBodies();
    DrawSpace drawSpace = new DrawSpace();
    Instant now;
    int frameCap = 1000 / Setup.getFrameLimit();
    int difference = frameCap;
    now = Instant.now();
    long start = now.toEpochMilli();
    //Main while loop is infinite until window is closed or program interrupted.
    while (true) {
      long millis = now.toEpochMilli();
      duration = (millis - start);
      Physics.updateBodies((int) (difference * Physics.getTimeScale()));
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
