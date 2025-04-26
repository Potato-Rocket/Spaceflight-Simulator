package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.LinkedList;
import java.util.List;

public class CollisionDetector {

    private static final LinkedList<CollisionEvent> collisions = new LinkedList<>();

    public List<CollisionEvent> detectCollisions(List<Body> bodies) {
        // Clear the collision list of any previous collisions
        collisions.clear();
        // For each body, determine whether there has been a collision with another body
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                // Check whether the distance between the bodies reaches a minimum during the time step
                double t = findClosestLinearApproach(body, other);
                if (t < 0 || t >= -Setup.getTimeStep()) {
                    continue;
                }
                // Refine the collision of time
                t = findEarliestLinearApproach(body, other);
                // NaN if the bodies do not intersect
                if (Double.isNaN(t) || t < 0 || t >= -Setup.getTimeStep()) {
                    continue;
                }
                collisions.add(new CollisionEvent(body, other, t));
            }
        }
        return collisions;
    }

    /**
     * Calculates the time of the closest linear approach between this body and another body.
     * This method considers the relative position and velocity of the two bodies
     * to determine when their separation distance will be minimized assuming linear motion.
     *
     * @param a the first body
     * @param b the second body
     * @return the time of closest approach as a double, or 0 if the relative velocity is negligible
     */
    private double findClosestLinearApproach(Body a, Body b) {
        BodyHistory stateA = a.getState();
        BodyHistory stateB = b.getState();

        // Suppose for a body p(t) = r_p_0 + v_p_0 * t.
        // Then if r = r_p2_0 - r_p1_0, v = v_p2_0 - v_p1_0,
        // the distance between the bodies will be r(t) = || r + v * t || at time t.
        // To find the minimum of this, we can solve the derivative of this,
        // giving t = -(r * v) / (v * v).
        Vector3D relPos = stateB.getPosition().subtract(stateA.getPosition());
        Vector3D relVel = stateB.getVelocity().subtract(stateA.getVelocity());

        // If the denominator is zero, that means that the relative velocity is zero
        double den = relVel.dotProduct(relVel);
        if (Math.abs(den) < 1.0e-12) {
            return 0;
        }
        // calculate the time of the closest approach
        return -relPos.dotProduct(relVel) / den;
    }

    private double findEarliestLinearApproach(Body a, Body b) {
        BodyHistory stateA = a.getState();
        BodyHistory stateB = b.getState();

        Vector3D posA = stateA.getHistory(0).position();
        Vector3D posB = stateB.getHistory(0).position();
        // FIXME: This will not hold up if adaptive or recursive timesteps are used
        Vector3D velA = posA.subtract(stateA.getPosition()).scaleInPlace(1.0 / Setup.getTimeStep());
        Vector3D velB = posB.subtract(stateB.getPosition()).scaleInPlace(1.0 / Setup.getTimeStep());

        Vector3D relPos = posB.subtract(posA);
        Vector3D relVel = velB.subtract(velA);

        // Find coefficients of the quadratic equation a*x^2 + b*x + c = 0
        double qc = relPos.dotProduct(relPos) - a.getRadius() - b.getRadius();
        double qb = 2 * relPos.dotProduct(relVel);
        double qa = relVel.dotProduct(relVel);

        double discriminant = qb * qb - 4 * qa * qc;
        if (discriminant < 0) {
            return Double.NaN;
        } else if (discriminant == 0) {
            return (-qb) / (2 * qa);
        } else {
            return (-qb - Math.sqrt(discriminant)) / (2 * qa);
        }

    }

}
