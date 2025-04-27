package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;
import us.stomberg.solarsystemsim.TimeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to handle all body interactions.
 */
public class Physics {

    public final Object lock = new Object();

    /**
     * The initial max distance of any body from the origin.
     */
    private double initBounds = 0;

    /**
     * The array of all bodies.
     */
    private final List<Body> bodyArray;

    private final double initialKineticEnergy;

    private final Integrator integrator;

    private final TimeStep timeStep;

    private final GravityCalculator gravityCalculator;

    private final CollisionDetector collisionDetector = new CollisionDetector();

    /**
     * Constructor for the Physics class.
     * Populates the array of bodies based on the generation data and initializes the physics system.
     */
    public Physics(Integrator integrator) {
        timeStep = new TimeStep(Setup.getTimeStep());
        this.integrator = integrator;

        synchronized (lock) {
            // Import the bodies from the generation data
            bodyArray = Setup.getGenerationData();
            gravityCalculator = new GravityCalculator(bodyArray);

            // Calculate the kinetic energy of the system at initialization
            initialKineticEnergy = getKineticEnergy();
            // Make sure each body starts with the proper history and initial state
            ArrayList<Vector3D> forces = gravityCalculator.updateForces(bodyArray);
            for (int i = 0; i < bodyArray.size(); i++) {
                Body body = bodyArray.get(i);
                body.getState().updateHistory(forces.get(i).scaleInPlace(1.0 / body.getMass()));
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
    public void updateBodies() {
        timeStep.reset();
        synchronized (lock) {
            // Recursively update the bodies until there are no collisions
            while (!timeStep.isFinished()) {
                // Make the initial position prediction for each body
                for (Body body : bodyArray) {
                    integrator.setPrediction(body, timeStep.getRemaining());
                }
                List<CollisionEvent> collisions = collisionDetector.detectCollisions(bodyArray, timeStep);
                double t = timeStep.getRemaining();
                if (!collisions.isEmpty()) {
                    CollisionEvent collision = Collections.min(collisions);
                    collision.processCollision(bodyArray, timeStep, integrator);
                    t = collision.getT();
                }
                timeStep.update(t);
                TimeManager.incrementDuration(t);
                // Calculate the force between each body and every other body
                ArrayList<Vector3D> forces = gravityCalculator.updateForces(bodyArray);
                // Update each body's final state based on the acceleration
                for (int i = 0; i < bodyArray.size(); i++) {
                    integrator.update(bodyArray.get(i), forces.get(i), t);
                }
            }
            // Update each body's trail
            for (Body body : bodyArray) {
                body.updateTrail();
            }
        }
    }

    /**
     * Getter method for the <code>ArrayList</code> of bodies.
     *
     * @return Returns the <code>ArrayList</code> of bodies.
     */
    public List<Body> getBodyArray() {
        return bodyArray;
    }

    /**
     * Gets the initial boundaries for the simulation view. Equal to the furthest distance from the origin any one
     * body starts at.
     *
     * @return Returns the initial boundaries for the view.
     */
    public double getInitBounds() {
        return initBounds;
    }

    /**
     * Retrieves the total kinetic energy of the system.
     *
     * @return The total kinetic energy value as a double.
     */
    public double getKineticEnergy() {
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
    public double getInitialKineticEnergy() {
        synchronized (lock) {
            return initialKineticEnergy;
        }
    }
}
