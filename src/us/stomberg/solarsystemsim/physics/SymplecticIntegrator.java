package us.stomberg.solarsystemsim.physics;

public class SymplecticIntegrator implements Integrator {

    @Override
    public void setPrediction(Body body, double dt) {
        BodyHistory state = body.getState();
        // First set v_{n+1} = v_n + a_n * dt
        state.getVelocity().addInPlace(state.getAcceleration().scale(dt));
        // Then set x_{n+1} = x_n + v_{n+1} * dt
        state.getPosition().addInPlace(state.getVelocity().scale(dt));
    }

    @Override
    public void update(Body body, Vector3D f, double dt) {
        BodyHistory state = body.getState();
        // Finally, set a_{n+1} = f(x_{n+1}) / m
        state.getAcceleration().copyFrom(f.scaleInPlace(1.0 / body.getMass()));
        // Now that all x, v, a have been updated, we can move the state to history
        state.updateHistory();
    }

}
