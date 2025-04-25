package us.stomberg.solarsystemsim.physics;

public record BodyState(
        Vector3D position,
        Vector3D velocity,
        Vector3D acceleration,
        double timestamp
) {

    public boolean isValid() {
        return position != null && velocity != null && acceleration != null && timestamp >= 0;
    }

    public boolean isSynchronous(BodyState other) {
        return Math.abs(timestamp - other.timestamp) < 1E-10;
    }

}
