package us.stomberg.solarsystemsim.physics;

public abstract class BaseIntegrator implements Integrator {

    protected int requiredHistory;

    public int getRequiredHistory() {
        return requiredHistory;
    }

}
