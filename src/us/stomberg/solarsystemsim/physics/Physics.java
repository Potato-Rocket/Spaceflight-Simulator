package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;
import us.stomberg.solarsystemsim.graphics.Draw;

import java.util.ArrayList;

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

    /**
     * Populates the array of bodies based on the generation data. After generating the bodies, gets the max initial
     * distance of any one body from the origin.
     */
    public static void createBodies() {
        synchronized (lock) {

            bodyArray = Setup.getGenerationData();
            initialKineticEnergy = getKineticEnergy();

            double[] distances = new double[bodyArray.size()];
            for (int i = 0; i < distances.length; i++) {
                distances[i] = bodyArray.get(i).getPosition().magnitude();
            }
            for (double dist : distances) {
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

            // Calculate the force between each body and every other body
            for (int i = 0; i < bodyArray.size(); i++) {
                Body body = bodyArray.get(i);
                for (int j = i + 1; j < bodyArray.size(); j++) {
                    Body other = bodyArray.get(j);
                    body.addGravityForce(other);
                }
            }
            // Update each body's position and velocity
            for (Body body : bodyArray) {
                body.update(Setup.getTimeStep());
            }
            // Handle collisions
            for (int i = 0; i < bodyArray.size(); i++) {
                Body body = bodyArray.get(i);
                for (int j = 0; j < bodyArray.size(); j++) {
                    Body other = bodyArray.get(j);
                    if (body.getId() != other.getId()) {
                        double distance = body.getPosition().distanceTo(other.getPosition());
                        if (body.getMass() >= other.getMass() && distance < body.getRadius() + other.getRadius()) {
                            body.collision(other);
                            bodyArray.remove(other);
                            j--;
                            if (Draw.getFocus() > body.getId()) {
                                Draw.modifyFocus(-1);
                            }
                        }
                    }
                }
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
