package us.stomberg.solarsystemsim.physics;

public interface IntegrationMethod {

    void update(Body body, double timeStep);

}
