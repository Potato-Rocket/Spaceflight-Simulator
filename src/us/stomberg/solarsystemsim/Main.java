package us.stomberg.solarsystemsim;

import us.stomberg.solarsystemsim.graphics.DrawSpace;
import us.stomberg.solarsystemsim.physics.Physics;

import java.util.logging.Logger;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics renderings
 */
public class Main {

    private static double currentFPS = 0;
    private static double currentTimeScale;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final Object lock = new Object();
    private static volatile boolean running = true;
    private static volatile int stepCount = 0;

    /**
     * Main method. Manages lower level classes and their processes and contains the main loop for the simulation.
     * Manages and calculates the frame rate.
     *
     * @param args command line inputs
     */
    public static void main(String[] args) {
        //Sets up graphical window and timing variables.
        Setup.read();
        Physics.createBodies();
        DrawSpace drawSpace = new DrawSpace();

        Thread simulation = new Thread(new Simulation());
        simulation.start();

        long prevFrame = System.nanoTime();

        // Main while loop is infinite until the window is closed or the program is interrupted.
        while (running) {
            if (System.nanoTime() - prevFrame > 1.0E9 / Setup.getFrameLimit()) {
                synchronized (lock) {
                    long timeDelta = System.nanoTime() - prevFrame;
                    currentFPS = 1.0E9 / timeDelta;
                    currentTimeScale = currentFPS * stepCount;
                    stepCount = 0;
                    prevFrame = System.nanoTime();
                    drawSpace.repaint();
                }
            }
        }
    }

    private static class Simulation implements Runnable {

        @Override
        public void run() {
            while (running) {
                if (Physics.getTimeStep() != 0) {
                    synchronized (lock) {
                        Physics.updateBodies();
                        stepCount += Physics.getTimeStep();
                    }
                }
            }
        }

    }

    /**
     * Getter method for the current fps. Averaged across the frame durations for 1 second.
     *
     * @return Returns the current fps.
     */
    public static double getFPS() {
        return currentFPS;
    }

    /**
     * Getter method for the current fps. Averaged across the frame durations for 1 second.
     *
     * @return Returns the current fps.
     */
    public static double getTimeScale() {
        return currentTimeScale;
    }

}
