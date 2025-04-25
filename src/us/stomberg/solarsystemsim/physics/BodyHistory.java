package us.stomberg.solarsystemsim.physics;

import java.util.LinkedList;

public class BodyHistory {

    public record State(Vector3D position, Vector3D velocity, Vector3D acceleration, double timestamp) {

        public boolean isSynchronous(State other) {
            return Math.abs(timestamp - other.timestamp) < 1E-10;
        }

    }

    public static final int historyLength = 2;

    private final Vector3D position;
    private final Vector3D velocity;
    private final Vector3D acceleration;

    private final LinkedList<State> history = new LinkedList<>();

    public BodyHistory(Vector3D position, Vector3D velocity) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new Vector3D();
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public Vector3D getAcceleration() {
        return acceleration;
    }

    public State getHistory(int steps) {
        if (steps >= historyLength || steps < 0) {
            return null;
        }
        return history.get(steps);
    }

    public void updateHistory() {
        State state = new State(position.copy(), velocity.copy(), acceleration.copy(), TimeManager.getDuration());
        history.addFirst(state);
        if (history.size() >= historyLength) {
            history.removeLast();
        }
    }

}
