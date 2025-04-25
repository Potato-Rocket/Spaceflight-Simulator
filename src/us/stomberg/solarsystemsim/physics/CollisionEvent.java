package us.stomberg.solarsystemsim.physics;

import java.util.ArrayList;

public class CollisionEvent {

    private final Body a;
    private final Body b;
    private final double t;

    public CollisionEvent(Body a, Body b, double t) {
        this.a = a;
        this.b = b;
        this.t = t;
    }

    public void processCollision(ArrayList<Body> bodies, Integrator integrator) {
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

        // Find the ratio of masses
        double scale = destroy.getMass() / keep.getMass();
        // Find the resultant velocity by conservation of momentum
        keepState.getVelocity().addInPlace(destroyState.getVelocity().scale(scale));
        // Average the position of the bodies, weighted by the relative masses
        keepState.getPosition().addInPlace(destroyState.getPosition().subtract(keepState.getPosition()).scale(scale));

        keep.merge(destroy);
        bodies.remove(destroy);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollisionEvent that = (CollisionEvent) o;
        return (that.a.equals(a) && that.b.equals(b)) || (that.a.equals(b) && that.b.equals(a));
    }

}
