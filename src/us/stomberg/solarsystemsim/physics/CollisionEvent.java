package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.ArrayList;

public class CollisionEvent implements Comparable<CollisionEvent> {

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
        // Find the old positions of each body
        Vector3D posKeep = keepState.getHistory(0).position().copy();
        Vector3D posDestroy = destroyState.getHistory(0).position().copy();
        // Find the velocities of each body
        Vector3D velKeep = posKeep.subtract(keepState.getPosition()).scale(1.0 / Setup.getTimeStep());
        Vector3D velDestroy = posDestroy.subtract(destroyState.getPosition()).scale(1.0 / Setup.getTimeStep());
        // Find the bodies' position at the collision time
        posKeep.addInPlace(velKeep.scale(t));
        posDestroy.addInPlace(velDestroy.scale(t));
        // Find the ratio of masses
        double scale = destroy.getMass() / keep.getMass();
        // Average the position of the bodies, weighted by the relative masses
        keepState.getPosition().copyFrom(posKeep.interpolateInPlace(posDestroy, scale));
        // Find the resultant velocity by conservation of momentum
        keepState.getVelocity().copyFrom(velKeep.interpolateInPlace(velDestroy, scale));

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

    @Override
    public int compareTo(CollisionEvent other) {
        return Double.compare(t, other.t);
    }

}
