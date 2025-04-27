package us.stomberg.solarsystemsim.physics;

public class TimeStep {
    private final double dt;
    private double elapsed;
    private double remaining;

    public TimeStep(double dt) {
        this.dt = dt;
        this.elapsed = 0;
        this.remaining = dt;
    }

    public void update(double dt) {
        this.elapsed += dt;
        this.remaining -= dt;
    }

    public void reset() {
        this.elapsed = 0;
        this.remaining = dt;
    }

    public double getDt() {
        return dt;
    }

    public double getElapsed() {
        return elapsed;
    }

    public double getRemaining() {
        return remaining;
    }

    public boolean isFinished() {
        return remaining <= 1.0e-12;
    }

}
