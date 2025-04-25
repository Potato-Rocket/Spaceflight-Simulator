package us.stomberg.solarsystemsim.physics;

public interface ForceCalculator {

    class ForceSynchronousException extends Exception {
        public ForceSynchronousException(String message) {
            super(message);
        }
    }

    Vector3D calculateForce(Body first, Body second) throws ForceSynchronousException;

}
