package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static class to handle all body interactions.
 */
public class Physics {

    public static final Object lock = new Object();

    /**
     * The initial max distance of any body from the origin.
     */
    private static double initBounds = 0;

    /**
     * The array of all bodies.
     */
    private static ArrayList<Body> bodyArray;

    private static double initialKineticEnergy = 0;

    private static final Integrator integrator = new VerletIntegrator();

    private static final GravityCalculator gravityCalculator = new GravityCalculator();

    // private static final CollisionDetector collisionDetector = new CollisionDetector();

    /**
     * Populates the array of bodies based on the generation data. After generating the bodies, gets the max initial
     * distance of any one body from the origin.
     */
    public static void createBodies() {
        synchronized (lock) {

            // Import the bodies from the generation data
            bodyArray = Setup.getGenerationData();
            // Calculate the kinetic energy of the system at initialization
            initialKineticEnergy = getKineticEnergy();
            // Make sure each body starts with the proper history and initial state
            HashMap<Body, Vector3D> forces = gravityCalculator.updateForces(bodyArray);
            BodyHistory state;
            for (Body body : bodyArray) {
                state = body.getState();
                state.getAcceleration().addInPlace(forces.get(body).scaleInPlace(1.0 / body.getMass()));
                state.updateHistory();
            }
            // Calculate the max initial distance of any one body from the origin
            for (Body body : bodyArray) {
                double dist = body.getState().getPosition().magnitude();
                if (dist > initBounds) {
                    initBounds = dist;
                }
            }

        }
    }

    /**
     * Updates every body in the body array. Runs the update function for each body to update the motion, then updates
     * the gravitational forces between each body and every other body. Finally, it checks for collisions between every
     * body. If two bodies have collided, it runs the collision function on the larger one and removes the smaller one.
     * <p>
     * The code to update the physics for every body is run once for every millisecond in the simulation that has passed
     * since the previous frame. That is the real time milliseconds passed times the timescale.
     */
    public static void updateBodies() {
        synchronized (lock) {
            // Make the initial position prediction for each body
            for (Body body : bodyArray) {
                integrator.setPrediction(body, Setup.getTimeStep());
            }
            // Calculate the force between each body and every other body
            HashMap<Body, Vector3D> forces = gravityCalculator.updateForces(bodyArray);
            // Update each body's final state based on the acceleration
            for (Body body : bodyArray) {
                integrator.update(body, forces.get(body), Setup.getTimeStep());
            }
            // Update each body's trail
            for (Body body : bodyArray) {
                body.updateTrail(Setup.getTimeStep());
            }
        }

        TimeManager.incrementDuration();
    }

    /**
     * Getter method for the <code>ArrayList</code> of bodies.
     *
     * @return Returns the <code>ArrayList</code> of bodies.
     */
    public static ArrayList<Body> getBodyArray() {
        return bodyArray;
    }

    /**
     * Gets the initial boundaries for the simulation view. Equal th=o the furthest distance from the origin any one
     * body starts at.
     *
     * @return Returns the initial boundaries for the view.
     */
    public static double getInitBounds() {
        return initBounds;
    }

    /**
     * Retrieves the total kinetic energy of the system.
     *
     * @return The total kinetic energy value as a double.
     */
    public static double getKineticEnergy() {
        double sum = 0;
        synchronized (lock) {
            for (Body body : bodyArray) {
                sum += body.getKineticEnergy();
            }

        }
        return sum;
    }

    /**
     * Returns the total kinetic energy of the system at initialization.
     *
     * @return The initial kinetic energy value as a double.
     */
    public static double getInitialKineticEnergy() {
        synchronized (lock) {
            return initialKineticEnergy;
        }
    }

}
