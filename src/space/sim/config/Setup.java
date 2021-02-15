package space.sim.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to load and store all settings relating to the simulation.
 */
public class Setup {

  private static final String CONFIG_DIR = "/home/oscar/.config/spaceflight-simulator/";

  /**
   * Stores the artificial frame rate limit.
   */
  private static int frameLimit = 60;
  /**
   * Whether or not to draw the trail
   */
  private static boolean drawTrail = true;
  /**
   * Stores whether or not to render the trails with transparency.
   */
  private static boolean trailAlpha = true;
  /**
   * The number of degrees of rotation the trail should cover.
   */
  private static double trailLength = 180;
  /**
   * The number of degrees of rotation between trail points.
   */
  private static double trailResolution = 1;

  /**
   * Stores the view's sensitivity to mouse movements.
   */
  private static double rotatePrecision = 100;
  /**
   * Factor to scale by when zooming.
   */
  private static double scalePrecision = 1.1;

  /**
   * Universal gravitational constant.
   */
  private static double gravity = 1;

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

  public static void read() throws IOException {
    try {
      InputStream input = new FileInputStream(CONFIG_DIR + "setup.properties");
      Properties setup = new Properties();
      setup.load(input);

      frameLimit = validateInt(setup, "graphics.frameLimit", 60);
      drawTrail = validateBoolean(setup, "graphics.drawTrail", true);
      trailAlpha = validateBoolean(setup, "graphics.trailAlpha", true);
      trailLength = validateInt(setup, "graphics.trailLength", 180);
      trailResolution = validateDouble(setup, "graphics.trailResolution", 1);

      rotatePrecision = validateDouble(setup, "control.rotatePrecision", 10);;
      scalePrecision = validateDouble(setup, "control.scalePrecision", 1.1);

      gravity = validateDouble(setup, "physics.gravity", 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static int validateInt(Properties setup, String key, int defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    try {
      return Integer.parseInt(in);
    } catch (NumberFormatException e) {
      System.out.println(e.toString());
      return defaultValue;
    }
  }

  private static double validateDouble(Properties setup, String key, double defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    try {
      return Double.parseDouble(in);
    } catch (NumberFormatException e) {
      System.out.println(e.toString());
      return defaultValue;
    }
  }

  private static boolean validateBoolean(Properties setup, String key, boolean defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    return Boolean.parseBoolean(in);
  }

  public static int getFrameLimit() {
    return frameLimit;
  }

  public static boolean isDrawTrail() {
    return drawTrail;
  }

  public static boolean isTrailAlpha() {
    return trailAlpha;
  }

  public static double getTrailLength() {
    return trailLength;
  }

  public static double getTrailResolution() {
    return trailResolution;
  }

  public static double getRotatePrecision() {
    return rotatePrecision;
  }

  public static double getScalePrecision() {
    return scalePrecision;
  }

  public static double getGravity() {
    return gravity;
  }

}
