package us.stomberg.solarsystemsim.physics;

import java.util.ArrayList;

public class CollisionEvent {

    private final Body a;
    private final Body b;

    public CollisionEvent(Body a, Body b) {
        this.a = a;
        this.b = b;
    }

    public void processCollision(ArrayList<Body> bodies) {
        Body destroy;
        Body keep;
        if (a.getMass() < b.getMass()) {
            destroy = a;
            keep = b;
        } else {
            destroy = b;
            keep = a;
        }
        bodies.remove(destroy);
        keep.collision(destroy);
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
