package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;
import us.stomberg.solarsystemsim.graphics.FormatText;

import java.awt.*;
import java.util.ArrayList;

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
    public final ArrayList<Vector3D> gravityForces = new ArrayList<>();

    /**
     * Stores the position vectors to render the trail.
     */
    private final ArrayList<Vector3D> trail = new ArrayList<>();

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
     * Vector to store the body's total acceleration.
     */
    private final Vector3D acceleration = new Vector3D();

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
     * A builder class for creating instances of the Body class. This class uses
     * the builder design pattern to allow for a step-by-step configuration of
     * the Body's attributes before creating the object.
     */
    public static class Builder {
        private Vector3D pos = new Vector3D();
        private Vector3D vel = new Vector3D();
        private double mass = 1.0;
        private double density = 1.0;
        private String name;
        private Color c = Color.WHITE;

        private boolean isDefault = true;
        private boolean isBuilt = false;

        public Builder() {}

        public Builder position(Vector3D pos) {
            if (pos != null) {
                isDefault = false;
                this.pos = pos;
            }
            return this;
        }

        public Builder velocity(Vector3D vel) {
            if (vel != null) {
                isDefault = false;
                this.vel = vel;
            }
            return this;
        }

        public Builder mass(Double mass) {
            if (mass != null && mass > 0) {
                isDefault = false;
                this.mass = mass;
            }
            return this;
        }

        public Builder density(Double density) {
            if (density != null && density > 0) {
                isDefault = false;
                this.density = density;
            }
            return this;
        }

        public Builder name(String name) {
            if (name != null) {
                isDefault = false;
                this.name = name;
            }
            return this;
        }

        public Builder color(Color c) {
            if (c != null) {
                isDefault = false;
                this.c = c;
            }
            return this;
        }

        public Body build() {
            if (name == null) {
                name = "Body " + (count + 1);
            }
            if (isDefault || isBuilt) {
                return null;
            }
            isBuilt = true;
            return new Body(pos, vel, mass, density, name, c);
        }

    }

    /**
     * Class constructor with all values specified by the user. Assigns specified values to their corresponding fields
     * and generates an id and gravity forces array for the body. Adds another vector to each body's gravity force
     * array.
     *
     * @param pos  initial position of the body
     * @param vel  initial velocity of the body
     * @param mass initial mass of the body
     * @param name name of the body
     */
    public Body(Vector3D pos, Vector3D vel, double mass, double density, String name, Color c) {
        this.position = pos;
        this.velocity = vel;
        this.mass = mass;
        this.radius = Math.cbrt((3 * (mass / density)) / (4 * Math.PI));
        this.name = name;
        this.color = c;
        prevTrail = vel.angleTo();
        id = count;
        count++;
        trail.add(pos.copy());
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
    public void update(double millis) {
        acceleration.setToZero();
        for (Vector3D f : gravityForces) {
            acceleration.addVector(f.scaleVector(1 / mass));
        }
        velocity.addVector(acceleration.scaleVector(millis / 1000));
        position.addVector(velocity.scaleVector(millis / 1000));
        Vector3D direction = velocity.angleTo();
        if (direction.distanceTo(prevTrail) > ONE_DEGREE * Setup.getTrailResolution() || position.distanceTo(
                trail.getFirst()) > Physics.getInitBounds() / 10) {
            if (trail.size() < Setup.getTrailLength() / Setup.getTrailResolution()) {
                trail.add(new Vector3D());
            }
            for (int i = trail.size() - 1; i > 0; i--) {
                trail.set(i, trail.get(i - 1));
            }
            trail.set(0, position.copy());
            prevTrail = direction;
        }
    }

    /**
     * Modifies this body's attributes based on collision information. Adjusts the position and velocity based on the
     * other body's position and velocity scaled to the bodies' relative masses. The masses are then merged.
     * <p>
     * TODO: Modify collision to take another body as a input
     * <p>
     * TODO: Take weighted average of the densities to find the new radius
     *
     * @param pos  The other body's position.
     * @param vel  The other body's velocity.
     * @param mass The other body's mass.     */
    public void collision(Vector3D pos, Vector3D vel, double mass) {
        double scale = mass / this.mass;
        velocity.addVector(vel.scaleVector(scale));
        position.addVector(position.compareTo(pos).scaleVector(scale * 0.5));
        this.mass += mass;
    }

    /**
     * Getter method for the body's trail data.
     *
     * @return Returns the <code>ArrayList</code> of trail points.
     */
    public ArrayList<Vector3D> getTrail() {
        return trail;
    }

    /**
     * Getter method for the body's position.
     *
     * @return Returns the body's position.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Getter method for the body's velocity.
     *
     * @return Returns the body's velocity.
     */
    public Vector3D getVelocity() {
        return velocity;
    }

    /**
     * Getter method for the body's mass.
     *
     * @return Returns the body's mass.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Getter method for the body's radius.
     *
     * @return Returns the body's radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Getter method for the body's identification number.
     *
     * @return Returns the body's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter method for the body's name.
     *
     * @return Returns the body's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the body's drawing color.
     *
     * @return Returns the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Formats the body's attributes into a <code>String[]</code>. Each array item is a line.
     *
     * @return Returns a string array representing the body's attributes.
     */
    public ArrayList<String> toStringArray() {
        ArrayList<String> string = new ArrayList<>();
        string.add("Body " + (id + 1) + ": " + name);
        string.add("Mass = " + FormatText.formatNum(mass, "kg", "t"));
        string.add("Radius = " + FormatText.formatNum(radius, "m", "km"));
        string.add("Position = " + position.toString("m"));
        string.add("Velocity = " + velocity.toString("m/s"));
        string.add("Acceleration = " + acceleration.toString("m/s²"));
        return string;
    }

    /**
     * Runs the <code>toString</code> method with the <code>verbose</code> option set to
     * <code>false</code>.
     *
     * @return Returns a string representing the body.
     */
    public String toString() {
        return toStringArray().getFirst();
    }

}
