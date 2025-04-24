package us.stomberg.solarsystemsim;

import us.stomberg.solarsystemsim.graphics.DrawSpace;
import us.stomberg.solarsystemsim.physics.Physics;
import us.stomberg.solarsystemsim.physics.TimeManager;

import java.util.concurrent.locks.LockSupport;
import java.util.logging.Logger;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics renderings
 */
public class Main {

    public static final Object lock = new Object();
    private static volatile boolean running = true;

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

        // The while loop is infinite until the window is closed or the program is interrupted.
        while (running) {
            if (TimeManager.shouldRenderFrame()) {
                drawSpace.repaint();
            }
        }
    }

    private static class Simulation implements Runnable {

        @Override
        public void run() {
            while (running) {

                if (TimeManager.shouldUpdatePhysics()) {
                    synchronized (lock) {
                        Physics.updateBodies();
                    }
                }

            }
        }

    }

}
