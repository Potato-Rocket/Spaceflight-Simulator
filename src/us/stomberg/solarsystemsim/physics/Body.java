package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;
import us.stomberg.solarsystemsim.graphics.FormatText;

import java.awt.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;

/**
 * Stores and updates physical information about a body. Updates information such as position and velocity as time
 * passes and as forces are applied to the body.
 */
public class Body {

    /**
     * Stores the distance between points one degree apart on a unit circle.
     */
    private static final double ONE_DEGREE = Math.PI * 2 / 360;

    /**
     * The number of bodies that have been generated.
     */
    private static int count = 0;

    /**
     * Vector to store the current gravitational forces acting on the body.
     */
    private final Vector3D gravityForce = new Vector3D();

    private final BitSet updatedBodies = new BitSet();

    /**
     * Stores the position vectors to render the trail.
     */
    private final LinkedList<Vector3D> trail = new LinkedList<>();

    /**
     * Stores the direction at the previous trail point.
     */
    private Vector3D prevTrail;

    /**
     * Vector to store the body's position.
     */
    private final Vector3D position;

    /**
     * Vector to store the body's velocity.
     */
    private final Vector3D velocity;

    /**
     * Stores the color to use when drawing this body.
     */
    private final Color color;

    /**
     * The body's mass.
     */
    private double mass;

    /**
     * The body's radius.
     */
    private double density;

    /**
     * The body's radius.
     */
    private double radius;

    /**
     * The body's name.
     */
    private final String name;

    /**
     * The body's identification number.
     */
    private final int id;

    /**
     * Class constructor with all values specified by the user. Assigns specified values to their corresponding fields
     * and generates an id and initializes the trail.
     *
     * @param pos     initial position of the body
     * @param vel     initial velocity of the body
     * @param mass    initial mass of the body
     * @param density density of the body used to calculate radius
     * @param name    name of the body
     * @param c       color used for rendering the body
     */
    public Body(Vector3D pos, Vector3D vel, double mass, double density, String name, Color c) {
        this.position = pos;
        this.velocity = vel;
        this.mass = mass;
        this.density = density;
        this.name = name;
        this.color = c;

        id = count;
        count++;
        updateRadius();
        resetGravityForce();

        prevTrail = vel.normalize();
        trail.add(pos.copy());
    }

    /**
     * Updates the radius of the body based on its mass and density.
     * <p>
     * This method calculates the radius using the formula derived from the
     * volume of a sphere, where the volume is determined by the mass and
     * density of the body.
     */
    private void updateRadius() {
        radius = Math.cbrt((3 * (mass / density)) / (4 * Math.PI));
    }

    /**
     * Updates the body's physical motion vectors. Resets the acceleration factor and sets it based on the mass and the
     * gravity forces currently acting on the body. The velocity is updated based on the acceleration and the position
     * is updated based on the velocity.
     * <p>
     * The trail is updated based on both direction change and distance traveled. It will add a point to the trail if
     * either:
     * <ul>
     *   <li>The direction of the body's velocity has changed more than [trail resolution] degrees
     *   from the direction of velocity at the previous trail point.</li>
     *   <li>THe body has traveled more than a predetermined distance since the previous trail
     *   point. Scaled to the size of the system.</li>
     * </ul>
     */
    public void update(double seconds) {
        // Calculate the acceleration of the body
        Vector3D acceleration = gravityForce.scaleVector(1 / mass);
        // Update the velocity based on the acceleration
        velocity.addVector(acceleration.scaleVector(seconds));
        // Update the position based on the velocity
        position.addVector(velocity.scaleVector(seconds));
        // Find the change in direction
        Vector3D direction = velocity.normalize();
        // If the direction has changed more than the resolution
        // Or the body has traveled more than a tenth of the initial bounds
        if (direction.distanceTo(prevTrail) > ONE_DEGREE * Setup.getTrailResolution() ||
                position.distanceTo(trail.getFirst()) > Physics.getInitBounds() / 10) {
            // Insert a new point into the trail
            trail.addFirst(position.copy());
            // If the trail is too long, remove the last point
            if (trail.size() > Setup.getTrailLength() / Setup.getTrailResolution()) {
                trail.removeLast();
            }
            prevTrail = direction;
        }
        resetGravityForce();
    }

    /**
     * Modifies this body's attributes based on the body colliding with it.
     * Assumes that the other body is smaller and will be removed from the simulation.
     * Combines the masses, positions, and densities of the two bodies based on their relative masses.
     * The resultant velocity is based on conservation of momentum, and the radius is updated based on the new mass and
     * density.
     *
     * @param other the other body
     */
    public void collision(Body other) {
        // Find the ratio of masses
        double scale = other.mass / mass;
        // Find the resultant velocity by conservation of momentum
        velocity.addVector(other.velocity.scaleVector(scale));
        // Average the position of the bodies, weighted by the relative masses
        position.addVector(position.compareTo(other.position).scaleVector(scale * 0.5));
        // Average the densities of the bodies, weighted by the relative masses
        density = ((mass * density) + (other.mass * other.density)) / (mass + other.mass);
        // Add the masses together
        mass += other.mass;
        updateRadius();
    }

    /**
     * Resets the gravity force vector to zero and clears the set of updated bodies. Also marks this body as updated in
     * the updatedBodies BitSet. This is called after each physics update to prepare for the next frame's calculations.
     */
    private void resetGravityForce() {
        gravityForce.setToZero();
        updatedBodies.clear();
        updatedBodies.set(id);
    }

    /**
     * Calculates and adds the gravitational force between this body and another body. Uses Newton's law of universal
     * gravitation: F = G * (m1 * m2) / r^2 The force is only calculated once per body pair per frame using a BitSet to
     * track which interactions have already been computed. When calculated, the force is added to both bodies in
     * opposite directions.
     *
     * @param other the other body to calculate gravitational force with
     */
    public void addGravityForce(Body other) {
        if (updatedBodies.get(other.id)) {
            return;
        }
        // Calculate the magnitude of force using Newton's law of universal gravitation
        double r = position.distanceTo(other.position);
        double f = Setup.getGravity() * ((mass * other.mass) / Math.pow(r, 2));
        Vector3D angle = position.angleTo(other.position);
        // Add the force to each body's gravitational force
        gravityForce.addVector(angle.scaleVector(f));
        other.gravityForce.addVector(angle.scaleVector(-f));
        // Mark the bodies as updated
        updatedBodies.set(other.id);
        other.updatedBodies.set(id);
    }

    /**
     * Getter method for the body's trail data.
     *
     * @return the <code>ArrayList</code> of trail points
     */
    public LinkedList<Vector3D> getTrail() {
        return trail;
    }

    /**
     * Getter method for the body's position.
     *
     * @return the body's position
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Getter method for the body's velocity.
     *
     * @return the body's velocity
     */
    public Vector3D getVelocity() {
        return velocity;
    }

    /**
     * Getter method for the body's mass.
     *
     * @return the body's mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Getter method for the body's radius.
     *
     * @return the body's radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Getter method for the body's identification number.
     *
     * @return the body's id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter method for the body's name.
     *
     * @return the body's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the body's drawing color.
     *
     * @return the body's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Formats the body's attributes into a <code>String[]</code>. Each array item is a line.
     *
     * @return a string array representing the body's attributes
     */
    public ArrayList<String> toStringArray() {
        ArrayList<String> string = new ArrayList<>();
        string.add("Body " + (id + 1) + ": " + name);
        string.add("Mass = " + FormatText.formatNum(mass, "kg", "t"));
        string.add("Radius = " + FormatText.formatNum(radius, "m", "km"));
        string.add("Position = " + position.toString("m"));
        string.add("Velocity = " + velocity.toString("m/s"));
        return string;
    }

    /**
     * Runs the <code>toString</code> method with the <code>verbose</code> option set to
     * <code>false</code>.
     *
     * @return a string representing the body
     */
    public String toString() {
        return toStringArray().getFirst();
    }

}
