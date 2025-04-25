package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

public class GravityCalculator implements ForceCalculator {

    @Override
    public Vector3D calculateForce(Body first, Body second) throws ForceSynchronousException {
        // Get both bodies' current state and ensure they are synchronous.
        BodyState stateFirst = first.getState();
        BodyState stateSecond = second.getState();
        if (!stateFirst.isSynchronous(stateSecond)) {
            throw new ForceSynchronousException("Cannot calculate force between two states with different timestamps.");
        }
        // Calculate gravity using newton's law of universal gravitation.
        double r = stateFirst.position().distanceTo(stateSecond.position());
        double f = Setup.getGravity() * ((first.getMass() * second.getMass()) / Math.pow(r, 2));
        Vector3D angle = stateFirst.position().angleTo(stateFirst.position());
        // Add the force to each body's gravitational force
        return angle.scaleVector(f);
    }

}
