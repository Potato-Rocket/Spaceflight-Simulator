package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.Setup;

import java.util.logging.Logger;

public class TimeManager {

    private static final Object lock = new Object();

    /**
     * The length of the simulation in seconds.
     */
    private static double duration = 0;
    /**
     * Current time scale index.
     */
    private static double timescaleCap = 1.0;
    /**
     * An epsilon value for comparing floats.
     */
    private static final double EPSILON = 0.01;


    private static long prevFrame = -1;
    private static double currentFPS = 0;

    private static double timescaleDuration = 0;
    private static long timescaleStart = -1;
    private static double currentTimeScale = 1.0;

    /**
     * Represents the types of possible changes to the timescale cap in the simulation.
     */
    public enum TimeScaleChangeType {
        INCREMENT,
        DECREMENT
    }

    /**
     * Increments the simulation time by one time step, as specified in the setup file.
     */
    public static void incrementDuration() {
        synchronized (lock) {
            duration += Setup.getTimeStep();
            timescaleDuration += Setup.getTimeStep();
            currentTimeScale = timescaleDuration / getTimescaleSeconds();
        }
    }

    /**
     * Retrieves the total duration of the simulation in seconds.
     *
     * @return the duration of the simulation in seconds
     */
    public static double getDuration() {
        synchronized (lock) {
            return duration;
        }
    }

    /**
     * Determines whether the physics system should update based on the elapsed simulation time this frame,
     * and the target duration of each frame.
     *
     * @return true if the physics system should update, false otherwise
     */
    public static boolean shouldUpdatePhysics() {
        synchronized (lock) {
            return timescaleDuration < getTimescaleSeconds() * timescaleCap;
        }
    }

    private static double getTimescaleSeconds() {
        synchronized (lock) {
            if (timescaleStart == -1) {
                timescaleStart = System.nanoTime();
            }
            return (double)(System.nanoTime() - timescaleStart) / 1.0E9;
        }
    }

    public static boolean shouldRenderFrame() {
        long time = System.nanoTime();
        if (prevFrame == -1) {
            prevFrame = time;
        }
        boolean retVal = false;
        if (time - prevFrame >= (long) (1.0E9 / Setup.getFrameLimit())) {
            currentFPS = 1.0E9 / (time - prevFrame);
            prevFrame = time;
            retVal = true;
        }
        return retVal;
    }

    /**
     * Increases or decreases the timescale based on the input. Is bound to a keymap in the
     * <code>Keymaps</code> class.
     */
    public static void modifyTimescaleCap(TimeScaleChangeType change) {
        synchronized (lock) {
            int decade = (int) Math.floor(Math.log10(timescaleCap));
            double decadeFactor = Math.pow(10, decade);

            double factor = timescaleCap / decadeFactor;
            if (Math.abs(factor - 1) < EPSILON) {
                switch (change) {
                    case INCREMENT:
                        timescaleCap = decadeFactor * 2;
                        break;
                    case DECREMENT:
                        timescaleCap = decadeFactor * 0.5;
                        break;
                }
            } else if (Math.abs(factor - 2) < EPSILON) {
                switch (change) {
                    case INCREMENT:
                        timescaleCap = decadeFactor * 5;
                        break;
                    case DECREMENT:
                        timescaleCap = decadeFactor;
                        break;
                }
            } else if (Math.abs(factor - 5) < EPSILON) {
                switch (change) {
                    case INCREMENT:
                        timescaleCap = decadeFactor * 10;
                        break;
                    case DECREMENT:
                        timescaleCap = decadeFactor * 2;
                        break;
                }
            } else {
                timescaleCap = decadeFactor;
            }

            timescaleStart = System.nanoTime();
            timescaleDuration = 0;

        }
    }

    /**
     * Getter method for the current timescale in simulation seconds per real second.
     *
     * @return the timescale
     */
    public static double getTimescaleCap() {
        synchronized (lock) {
            return timescaleCap;
        }
    }

    /**
     * Getter method for the current timescale in simulation seconds per real second.
     *
     * @return the timescale
     */
    public static double getCurrentTimeScale() {
        synchronized (lock) {
            return currentTimeScale;
        }
    }

    /**
     * Getter method for the current fps. Averaged across the frame durations for 1 second.
     *
     * @return Returns the current fps.
     */
    public static double getCurrentFPS() {
        return currentFPS;
    }

}
