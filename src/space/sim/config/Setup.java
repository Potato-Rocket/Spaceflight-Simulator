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
   * Stores whether or not to render the trails with transparency.
   */
  public static final boolean TRANSPARENCY = true;
  /**
   * Stores the view's sensitivity to mouse movements.
   */
  public static final double SENSITIVITY = 100;
  /**
   * Factor to scale by when zooming.
   */
  public static final double SCALE_FACTOR = 1.1;
  /**
   * The number of seconds the trail remains on screen.
   * */
  public static final double TRAIL_LENGTH = 100;
  /**
   * The ratio between real time and simulation time.
   */
  public static final double TIME_SCALE = 20;
  /**
   * Universal gravitational constant.
   */
  public static final double G = 1;
  /**
   * The 2D array to store information about how to generate bodies.
   */
  public static final String[][] GEN_DATA = {
      {"500", "0", "0", "0", "20", "0", "1000000", "Star 1"},
      {"-500", "0", "0", "0", "-20", "0", "1000000", "Star 2"},
      {"0", "0", "5000", "0", "20", "0", "1000", "Satellite 1"}
  };

}
