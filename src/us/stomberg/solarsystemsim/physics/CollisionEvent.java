package us.stomberg.solarsystemsim.physics;

import java.util.List;
import java.util.logging.Logger;

public class CollisionEvent implements Comparable<CollisionEvent> {

    private static final Logger logger = Logger.getLogger(CollisionEvent.class.getName());

    private final Body a;
    private final Body b;
    private final double t;

    public CollisionEvent(Body a, Body b, double t) {
        this.a = a;
        this.b = b;
        this.t = t;
    }

    public void processCollision(List<Body> bodies, TimeStep timeStep, Integrator integrator) {
        Body destroy;
        Body keep;
        if (a.getMass() < b.getMass()) {
            destroy = a;
            keep = b;
        } else {
            destroy = b;
            keep = a;
        }
        BodyHistory keepState = keep.getState();
        BodyHistory destroyState = destroy.getState();

        logger.info("Collision processed between " + a + " and " + b);
        logger.info(destroy + " will be consumed by " + keep + " at time " + (timeStep.getElapsed() + t));

        double rollBack = timeStep.getRemaining() - t;
        for (Body body : bodies) {
            integrator.rollBack(body, rollBack);
        }

        double totalMass = keep.getMass() + destroy.getMass();
        // Average the position of the bodies, weighted by the relative masses
        keepState.getPosition()
                 .interpolateInPlace(destroyState.getPosition(), destroy.getMass() / totalMass);

        // Find the resultant velocity by conservation of momentum
        keepState.getVelocity()
                 .scaleInPlace(keep.getMass() / totalMass)
                 .addInPlace(destroyState.getVelocity()
                                         .scale(destroy.getMass() / totalMass));

        // Update the history of the body now to avoid overwriting the velocity
        BodyHistory.State prev = keepState.getHistory();
        keep.getState().updateHistory((prev == null) ? new Vector3D() : prev.acceleration());

        // Combine mass, density, and radius
        keep.merge(destroy);
        bodies.remove(destroy);
    }

    public double getT() {
        return t;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollisionEvent that = (CollisionEvent) o;
        return (that.a.equals(a) && that.b.equals(b)) || (that.a.equals(b) && that.b.equals(a));
    }

    @Override
    public int compareTo(CollisionEvent other) {
        return Double.compare(t, other.t);
    }

}
