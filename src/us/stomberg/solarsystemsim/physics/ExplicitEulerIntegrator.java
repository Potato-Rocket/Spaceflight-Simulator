package us.stomberg.solarsystemsim.physics;

public class ExplicitEulerIntegrator extends BaseIntegrator {

    protected int requiredHistory = 2;

    @Override
    public void update(Body body, double timeStep) {
        BodyState current = body.getState();
        if (!current.isValid()) {
            BodyState newState = new BodyState(current.position(), current.velocity(), TimeManager.getDuration());
        }

    }

}
