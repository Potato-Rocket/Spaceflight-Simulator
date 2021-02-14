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
  public static final boolean DRAW_TRAIL = true;
  /**
   * Stores whether or not to render the trails with transparency.
   */
  public static final boolean TRANSPARENCY = true;
  /**
   * The number of degrees of rotation the trail should cover.
   */
  public static final double TRAIL_LENGTH = 365;
  /**
   * The number of degrees of rotation between trail points.
   */
  public static final double TRAIL_RESOLUTION = 5;
  /**
   * Stores the view's sensitivity to mouse movements.
   */
  public static final double SENSITIVITY = 100;
  /**
   * Factor to scale by when zooming.
   */
  public static final double SCALE_FACTOR = 1.1;
  /**
   * The ratio between real time and simulation time.
   */
  public static final double TIME_SCALE = 300;
  /**
   * Universal gravitational constant.
   */
  public static final double G = 1;
  /**
   * The 2D array to store information about how to generate bodies.
   */
  public static final String[][] GEN_DATA = {
      {"1000", "0", "0", "0", "30", "0", "10000000", "Star 1"},
      {"-1000", "0", "0", "0", "-30", "0", "10000000", "Star 2"},
      {"0", "0", "10000", "0", "30", "0", "10000", "Satellite 1"}
  };

}
