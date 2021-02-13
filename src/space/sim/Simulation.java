package space.sim;

import space.sim.graphics.DrawSpace;
import space.sim.physics.Physics;

import java.time.Instant;
import java.util.Properties;
import java.util.Scanner;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics
 * renderings
 */
public class Simulation {

  //TODO: Add user inputs before creating the window and bodies.
  public static void main(String[] args) throws InterruptedException {
    Scanner in = new Scanner(System.in);
    Properties properties = new Properties();

    DrawSpace drawSpace = new DrawSpace();
    Physics physics = new Physics();

    Instant time;
    int frameCap = 60;
    int difference = 1000 / frameCap;
    time = Instant.now();
    while (true) {
      long millis = time.toEpochMilli();
      physics.updateBodies(difference);
      drawSpace.paint(drawSpace.getGraphics(), physics);
      if (difference < 1000 / frameCap) {
        Thread.sleep(1000 / frameCap - difference);
      }
      time = Instant.now();
      difference = (int) (time.toEpochMilli() - millis);
    }
  }

}
