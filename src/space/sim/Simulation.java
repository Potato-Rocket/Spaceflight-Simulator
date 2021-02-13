package space.sim;

import space.sim.config.Setup;
import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;

import java.time.Instant;
import java.util.Scanner;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  private static final int FRAME_LIMIT = 60;

  //TODO: Add user inputs before creating the window and bodies.
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    Setup setup = new Setup();
    DrawSpace drawSpace = new DrawSpace();
    Physics physics = new Physics();

    Instant time;
    int frameCap = 1000 / FRAME_LIMIT;
    int difference = frameCap;
    time = Instant.now();
    while (true) {
      long millis = time.toEpochMilli();
      physics.updateBodies(difference);
      drawSpace.paint(drawSpace.getGraphics(), physics);
      time = Instant.now();
      difference = (int) (time.toEpochMilli() - millis);
      if (difference < frameCap) {
        while (difference < frameCap) {
          time = Instant.now();
          difference = (int) (time.toEpochMilli() - millis);
        }
      }
    }
  }

}
