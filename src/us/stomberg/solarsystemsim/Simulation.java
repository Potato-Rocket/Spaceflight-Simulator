package us.stomberg.solarsystemsim;

import us.stomberg.solarsystemsim.config.Setup;
import us.stomberg.solarsystemsim.graphics.DrawSpace;
import us.stomberg.solarsystemsim.physics.Physics;

import java.io.IOException;
import java.time.Instant;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics renderings
 */
public class Simulation {

    /**
     * Stores the current average fps.
     */
    private static double fps = Setup.getFrameLimit();

    /**
     * Stores the duration of each frame in the last second.
     */
    private static final int[] prevFrames = new int[Setup.getFrameLimit()];

    /**
     * Main method. Manages lower level classes and their processes, and contains the main loop for the simulation.
     * Manages and calculates the frame rate.
     *
     * @param args command line inputs
     */
    public static void main(String[] args) throws IOException {
        //Sets up graphical window and timing variables.
        Setup.read();
        Physics.createBodies();
        DrawSpace drawSpace = new DrawSpace();
        int frameCap = 1000 / Setup.getFrameLimit();
        int difference = frameCap;
        Instant now = Instant.now();
        //Main while loop is infinite until window is closed or program interrupted.
        while (true) {
            long millis = now.toEpochMilli();
            Physics.updateBodies(fps);
            drawSpace.repaint();
            now = Instant.now();
            difference = (int) (now.toEpochMilli() - millis);
            //Delays until the amount of time allotted for each frame is reached.
            if (difference < frameCap) {
                while (difference < frameCap) {
                    now = Instant.now();
                    difference = (int) (now.toEpochMilli() - millis);
                }
            }
            //Updates fps counter.
            for (int i = prevFrames.length - 1; i > 0; i--) {
                prevFrames[i] = prevFrames[i - 1];
            }
            prevFrames[0] = difference;
            int sum = 0;
            for (int i : prevFrames) {
                sum += i;
            }
            fps = 1000 / ((double) sum / prevFrames.length);
        }
    }

    /**
     * Getter method for the current fps. Averaged across the frame durations for 1 second.
     *
     * @return Returns the current fps.
     */
    public static double getFps() {
        return fps;
    }

}
