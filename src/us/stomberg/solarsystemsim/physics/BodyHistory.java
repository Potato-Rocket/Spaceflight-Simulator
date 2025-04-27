package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.TimeManager;

import java.util.LinkedList;

public class BodyHistory {

    public record State(Vector3D position, Vector3D velocity, Vector3D acceleration, double timestamp) {}

    public static final int historyLength = 2;

    private final Vector3D position;
    private final Vector3D velocity;

    private final LinkedList<State> history = new LinkedList<>();

    public BodyHistory(Vector3D position, Vector3D velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public Vector3D findLinearVelocity(TimeStep step) {
        if (history.isEmpty()) {
            return velocity.copy();
        }
        State prev = history.getFirst();
        return prev.position().addAndScaleInPlace(position, 1.0 / step.getRemaining());
    }

    public State getHistory() {
        return history.isEmpty() ? null : history.getFirst();
    }

    public State getHistory(int steps) {
        if (steps >= historyLength || steps < 0) {
            return null;
        }
        return history.get(steps);
    }

    public void updateHistory(Vector3D acceleration) {
        State state = new State(position.copy(), velocity.copy(), acceleration.copy(), TimeManager.getDuration());
        history.addFirst(state);
        if (history.size() >= historyLength) {
            history.removeLast();
        }
    }

}
