package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.LinkedList;
import java.util.List;

public class CollisionDetector {

    public List<CollisionEvent> detectCollisions(List<Body> bodies) {
        LinkedList<CollisionEvent> collisions = new LinkedList<>();
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                double bound = body.getRadius() + other.getRadius();
                bound += Setup.getTimeStep() * body.getVelocity().magnitude();
                bound += Setup.getTimeStep() * other.getVelocity().magnitude();
                if (bound < body.getPosition().compareTo(other.getPosition()).magnitude()) {
                    continue;  // Skip if bodies are too far apart
                }
                double t = body.findClosestLinearApproach(other);
                if (t > 0 || t < -Setup.getTimeStep()) {
                    continue;
                }
                Vector3D bodyPos = body.getPosition().sumVector(body.getVelocity().scaleVector(t));
                Vector3D otherPos = other.getPosition().sumVector(other.getVelocity().scaleVector(t));
                if (bodyPos.subtract(otherPos).magnitude() < body.getRadius() + other.getRadius()) {
                    collisions.add(new CollisionEvent(body, other));
                }
            }
        }
        while (!collisions.isEmpty()) {
            collisions.remove().processCollision(bodyArray);
        }
    }

    /**
     * Calculates the time of the closest linear approach between this body and another body.
     * This method considers the relative position and velocity of the two bodies
     * to determine when their separation distance will be minimized assuming linear motion.
     *
     * @param other the other body to calculate the closest linear approach with
     * @return the time of closest approach as a double, or 0 if the relative velocity is negligible
     */
    public double findClosestLinearApproach(Body other) {
        Vector3D relPos = position.compareTo(other.position);
        Vector3D relVel = velocity.compareTo(other.velocity);

        // Square the dot product of the relative velocity
        double den = relVel.dotProduct(relVel);
        // If it is zero, that means that the relative velocity is zero
        if (den < 1.0e-12) {
            return 0;
        }
        // calculate the time of the closest approach
        return -relPos.dotProduct(relVel) / den;
    }

}
