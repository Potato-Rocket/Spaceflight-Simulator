package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.HashMap;
import java.util.List;

public class GravityCalculator {

    private final HashMap<Body, Vector3D> gravitationalForces = new HashMap<>();

    public HashMap<Body, Vector3D> updateForces(List<Body> bodies) {
        // For each body, calculate the force between it and every other body
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            for (int j = i + 1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                // Find the force between the two bodies
                Vector3D force = calculateForce(body, other);
                // Add the force to the first body
                Vector3D total = gravitationalForces.getOrDefault(body, new Vector3D()).addInPlace(force);
                gravitationalForces.putIfAbsent(body, total);
                // Subtract the force from the second body
                total = gravitationalForces.getOrDefault(other, new Vector3D()).subtractInPlace(force);
                gravitationalForces.putIfAbsent(other, total);
            }
        }
        // Return the calculated forces
        return gravitationalForces;
    }

    private Vector3D calculateForce(Body body, Body other) {
        // Get both bodies' current state
        BodyHistory stateFirst = body.getState();
        BodyHistory stateSecond = other.getState();
        // Calculate gravity using newton's law of universal gravitation
        double r = stateFirst.getPosition().distance(stateSecond.getPosition());
        double f = Setup.getGravity() * ((body.getMass() * other.getMass()) / (r * r));
        // Combine the direction vector with the magnitude of the force
        return stateFirst.getPosition().angleTo(stateSecond.getPosition()).scaleInPlace(f);
    }

}
