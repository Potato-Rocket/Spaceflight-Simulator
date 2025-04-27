package us.stomberg.solarsystemsim;

import us.stomberg.solarsystemsim.graphics.DrawSpace;
import us.stomberg.solarsystemsim.physics.ExplicitEulerIntegrator;
import us.stomberg.solarsystemsim.physics.Physics;
import us.stomberg.solarsystemsim.physics.SymplecticIntegrator;
import us.stomberg.solarsystemsim.physics.VerletIntegrator;

/**
 * Top level runner class. Dictates initial body creation as well as physics updates and graphics renderings
 */
public class Main {

    private static volatile boolean running = true;
    private static Physics physics;

    /**
     * Main method. Manages lower level classes and their processes and contains the main loop for the simulation.
     * Manages and calculates the frame rate.
     *
     * @param args command line inputs
     */
    public static void main(String[] args) throws InterruptedException {
        //Sets up graphical window and timing variables.
        Setup.read();
        physics = new Physics(new ExplicitEulerIntegrator());
        DrawSpace drawSpace = new DrawSpace();

        Thread simulation = new Thread(new Simulation());
        simulation.start();

        // The while loop is infinite until the window is closed or the program is interrupted.
        while (running) {
            if (TimeManager.shouldRenderFrame()) {
                drawSpace.repaint();
                TimeManager.updateFPS();
            }
        }

        simulation.join();
        System.exit(0);
    }

    private static class Simulation implements Runnable {

        @Override
        public void run() {
            while (running) {

                if (TimeManager.shouldUpdatePhysics()) {
                    physics.updateBodies();
                }

            }
        }

    }

    /**
     * Gets the physics instance.
     *
     * @return The physics instance.
     */
    public static Physics getPhysics() {
        return physics;
    }

    /**
     * Stops the simulation from running.
     */
    public static void stopRunning() {
        running = false;
    }

}
