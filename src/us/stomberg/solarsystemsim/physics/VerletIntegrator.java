package us.stomberg.solarsystemsim.physics;

public class VerletIntegrator implements Integrator {

    @Override
    public void setPrediction(Body body, double dt) {
        BodyHistory state = body.getState();
        // Set x_{n+1} = x_n + v_n * dt + 1/2 * a_n * dt^2
        state.getPosition()
             .addInPlace(state.getVelocity().scale(dt))
             .addInPlace(state.getAcceleration().scale((dt * dt) * 0.5));
    }

    @Override
    public void update(Body body, Vector3D f, double dt) {
        BodyHistory state = body.getState();
        // Find a_{n+1} = f(x_{n+1}) / m
        f.scaleInPlace(1.0 / body.getMass());
        // Set v_{n+1} = v_n + (a_{n+1} + a_n) / 2 * dt
        state.getVelocity().addInPlace(state.getAcceleration()
                                            .interpolateInPlace(f, 0.5)
                                            .scale(dt));

        // Set a_{n+1}
        state.getAcceleration().copyFrom(f);
        // Now that all x, v, a have been updated, we can move the state to history
        state.updateHistory();
    }

}
