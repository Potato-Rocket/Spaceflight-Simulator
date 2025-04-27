package us.stomberg.solarsystemsim.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CollisionDetector {

    private static final Logger logger = Logger.getLogger(CollisionDetector.class.getName());

    private static final ArrayList<CollisionEvent> collisions = new ArrayList<>();

    public List<CollisionEvent> detectCollisions(List<Body> bodies, TimeStep timeStep, Integrator integrator) {
        // Clear the collision list of any previous collisions
        collisions.clear();
        // For each body, determine whether there has been a collision with another body
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                // Find the collision time
                double t = findEarliestLinearApproach(body, other, timeStep);
                // NaN if the bodies do not intersect
                if (Double.isNaN(t) || t < -timeStep.getElapsed() || t >= timeStep.getRemaining()) {
                    continue;
                }
                logger.info("Collision detected between " + body + " and " + other);
                collisions.add(new CollisionEvent(body, other, t));
            }
        }
        return collisions;
    }

    private double findEarliestLinearApproach(Body a, Body b, TimeStep timeStep) {
        BodyHistory stateA = a.getState();
        BodyHistory stateB = b.getState();

        Vector3D relPos = stateB.getHistory().position()
                                .subtract(stateA.getHistory().position());

        Vector3D relVel = stateB.findLinearVelocity(timeStep)
                                .subtract(stateA.findLinearVelocity(timeStep));

        // Suppose for a body p(t) = r_p_0 + v_p_0 * t.
        // Then if r = r_p2_0 - r_p1_0, v = v_p2_0 - v_p1_0,
        // the distance between the bodies will be r(t) = || r + v * t || at time t.
        // To find the instant of collision, we must solve for r(t) = r_1 + r_2, the radii of the bodies.
        // Squaring both sides, the equation becomes
        // r^2 + 2 * r * v * t + v^2 * t^2 = (r_1 + r_2)^2

        // Now we can solve this as a standard quadratic with
        // a = v^2
        // b = 2 * r * v
        // c = r^2 - r_1 - r_2
        double threshold = a.getRadius() + b.getRadius();
        double qa = relVel.dotProduct(relVel);
        double qb = 2 * relPos.dotProduct(relVel);
        double qc = relPos.dotProduct(relPos) - threshold * threshold;

        // Find the discriminant and check for real roots
        double discriminant = qb * qb - 4 * qa * qc;
        if (discriminant < 0) {
            return Double.NaN;
        } else if (discriminant == 0) {
            // Don't bother computing a square root if not necessary
            return (-qb) / (2 * qa);
        } else {
            // We want the earliest intersection
            return (-qb - Math.sqrt(discriminant)) / (2 * qa);
        }

    }

}
