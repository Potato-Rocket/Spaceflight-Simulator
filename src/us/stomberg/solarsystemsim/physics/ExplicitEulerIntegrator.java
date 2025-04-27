package us.stomberg.solarsystemsim.physics;

public class ExplicitEulerIntegrator implements Integrator {

    @Override
    public void setPrediction(Body body, double dt) {
        BodyHistory state = body.getState();
        // Predict x_{n+1} = x_n + v_n * dt
        state.getPosition()
             .addAndScaleInPlace(state.getVelocity(), dt);
        // Predict v_{n+1} = v_n + a_n * dt
        state.getVelocity()
             .addAndScaleInPlace(state.getHistory().acceleration(), dt);
    }

    @Override
    public void rollBack(Body body, double dt) {
        BodyHistory state = body.getState();
        // Predict v_n = v_{n+1} - a_n * dt
        state.getVelocity()
             .addAndScaleInPlace(state.getHistory().acceleration(), dt);
        // Predict x_n = x_{n+1} - v_n * dt
        state.getPosition()
             .addAndScaleInPlace(state.getVelocity(), dt);
    }

    @Override
    public void update(Body body, Vector3D f, double dt) {
        BodyHistory state = body.getState();
        // Set a_{n+1} = f(x_{n+1}) / m
        // Now that all x, v, a have been updated, we can move the state to history
        state.updateHistory(f.scaleInPlace(1.0 / body.getMass()));
    }

}
