package us.stomberg.solarsystemsim.physics;

public class VerletIntegrator implements Integrator {

    @Override
    public void setPrediction(Body body, double dt) {
        BodyHistory state = body.getState();
        // Predict x_{n+1} = x_n + v_n * dt + 1/2 * a_n * dt^2
        state.getPosition()
             .addAndScaleInPlace(state.getVelocity(), dt)
             .addAndScaleInPlace(state.getHistory().acceleration(), (dt * dt) * 0.5);
        // Predict v_{n+1} = v_n + a_n * dt
        state.getVelocity()
             .addAndScaleInPlace(state.getHistory().acceleration(), dt);
    }

    @Override
    public void rollBack(Body body, double dt) {
        BodyHistory state = body.getState();
        // Predict v_n = v_{n+1} - a_n * dt
        state.getVelocity()
             .addAndScaleInPlace(state.getHistory().acceleration(), -dt);
        // Predict x_n = x_{n+1} - v_n * dt - 1/2 * a_n * dt^2
        state.getPosition()
             .addAndScaleInPlace(state.getVelocity(), -dt)
             .addAndScaleInPlace(state.getHistory().acceleration(), (dt * dt) * -0.5);
    }

    @Override
    public void update(Body body, Vector3D f, double dt) {
        BodyHistory state = body.getState();
        // Find a_{n+1} = f(x_{n+1}) / m
        f.scaleInPlace(1.0 / body.getMass());
        // Add acceleration to force vector
        f.addInPlace(state.getHistory().acceleration());
        // Set v_{n+1} = v_n + (a_{n+1} + a_n) / 2 * dt
        state.getVelocity().copyFrom(state.getHistory().velocity())
             .interpolateInPlace(f, 0.5 * dt);
        // Now that all x, v, a have been updated, we can move the state to history
        state.updateHistory(f);
    }

}
