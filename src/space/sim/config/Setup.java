package space.sim.config;

/**
 * Class to load and store all settings relating to the simulation.
 */
public class Setup {

  /**
   * Stores the artificial frame rate limit.
   */
  public static final int FRAME_LIMIT = 60;
  /**
   *
   */
  public static final boolean DRAW_TRAIL = false;
  /**
   * Stores whether or not to render the trails with transparency.
   */
  public static final boolean TRANSPARENCY = true;
  /**
   * The number of degrees of rotation the trail should cover.
   */
  public static final double TRAIL_LENGTH = 90;
  /**
   * The number of degrees of rotation between trail points.
   */
  public static final double TRAIL_RESOLUTION = 1;
  /**
   * Stores the view's sensitivity to mouse movements.
   */
  public static final double SENSITIVITY = 10;
  /**
   * Factor to scale by when zooming.
   */
  public static final double SCALE_FACTOR = 1.01;
  /**
   * The ratio between real time and simulation time.
   */
  public static final double TIME_SCALE = 10;
  /**
   * Universal gravitational constant.
   */
  public static final double G = 1;
  /**
   * The 2D array to store information about how to generate bodies.
   */
  public static final String[][] GEN_DATA = {
      {"1000", "0", "0", "0", "40", "0", "10000000", "Star 1"},
      {"-1000", "0", "0", "0", "-40", "0", "10000000", "Star 2"},
      {"0", "0", "10000", "0", "40", "0", "10000", "Satellite"},
      {"0", "100", "10000", "10", "40", "0", "10", "Moon"},
      {"0", "1000", "0", "0", "1000000", "0", "10", "Missile"}
  };

}
