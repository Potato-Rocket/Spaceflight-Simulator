package us.stomberg.solarsystemsim.physics;

public interface Integrator {

    void setPrediction(Body body, double dt);

    void rollBack(Body body, double dt);

    void update(Body body, Vector3D f, double dt);

}
