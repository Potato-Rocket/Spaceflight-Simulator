package us.stomberg.solarsystemsim.physics;

public class BodyHistory {

    public record State(Vector3D position, Vector3D velocity, Vector3D acceleration) {}

    public static final int historyLength = 2;

    private final Vector3D position;
    private final Vector3D velocity;

    private final State[] history = new State[historyLength];
    private int headIndex = 0;
    private int size = 0;

    public BodyHistory(Vector3D position, Vector3D velocity) {
        this.position = position;
        this.velocity = velocity;

        // Pre-allocate all State objects
        for (int i = 0; i < historyLength; i++) {
            history[i] = new State(new Vector3D(), new Vector3D(), new Vector3D());
        }
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public Vector3D findLinearVelocity(TimeStep step) {
        if (size == 0) {
            return velocity.copy();
        }

        State prev = history[headIndex];
        return prev.position().add(position).scaleInPlace(1.0 / step.getRemaining());
    }

    public State getHistory() {
        return size > 0 ? history[headIndex] : null;
    }

    public State getHistory(int steps) {
        if (steps >= historyLength || steps < 0) {
            return null;
        }
        int index = (headIndex - steps + historyLength) % historyLength;
        return history[index];
    }

    public void updateHistory(Vector3D acceleration) {
        if (size < historyLength) {
            size++;
        }

        // Move head to the next position (circular)
        headIndex = (headIndex + 1) % historyLength;

        // Reuse the existing State object instead of creating a new one
        State state = history[headIndex];
        state.position().copyFrom(position);
        state.velocity().copyFrom(velocity);
        state.acceleration().copyFrom(acceleration);
    }

}
