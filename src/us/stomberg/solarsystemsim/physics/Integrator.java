package us.stomberg.solarsystemsim.physics;

public interface Integrator {

    int getRequiredHistory();

    void update(Body body, double timeStep);

}
