package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.HashMap;
import java.util.List;

public class GravityCalculator {

    private final HashMap<Body, Vector3D> gravitationalForces = new HashMap<>();

    // Reusable vector to reduce object creation
    private final Vector3D force = new Vector3D();

    public GravityCalculator(List<Body> bodies) {
        for (Body body : bodies) {
            gravitationalForces.put(body, new Vector3D());
        }
    }

    public HashMap<Body, Vector3D> updateForces(List<Body> bodies) {
        // For each body, calculate the force between it and every other body
        for (Body value : bodies) {
            gravitationalForces.get(value).setToZero();
        }
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                // Find the force between the two bodies
                calculateForce(body, other);
                // Add the force to the first body
                gravitationalForces.get(body).addInPlace(force);
                // Subtract the force from the second body
                gravitationalForces.get(other).subtractInPlace(force);
            }
        }
        // Return the calculated forces
        return gravitationalForces;
    }

    private void calculateForce(Body body, Body other) {
        // Get both bodies' current state
        BodyHistory stateFirst = body.getState();
        BodyHistory stateSecond = other.getState();
        // Calculate gravity using newton's law of universal gravitation
        // Reuse tempVector to avoid creating new objects
        force.copyFrom(stateSecond.getPosition())
             .subtractInPlace(stateFirst.getPosition());
        double dist = force.magnitude();
        // Combine the direction vector with the magnitude of the force
        force.scaleInPlace(Setup.getGravity() *
                           ((body.getMass() * other.getMass()) /
                           (dist * dist * dist)));
    }

}
