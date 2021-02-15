package space.sim.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
  private static String[][] generationData = {};

  public static void read() throws IOException {
    try {
      InputStream input = new FileInputStream(CONFIG_DIR + "setup.properties");
      Properties setup = new Properties();
      setup.load(input);

      frameLimit = validateInt(setup, "frameLimit", 60);
      drawTrail = validateBoolean(setup, "drawTrail", true);
      trailAlpha = validateBoolean(setup, "trailAlpha", false);
      trailLength = validateInt(setup, "trailLength", 180);
      trailResolution = validateDouble(setup, "trailResolution", 1);
      rotatePrecision = validateDouble(setup, "rotatePrecision", 10);
      scalePrecision = validateDouble(setup, "scalePrecision", 1.1);

      readGeneration(setup.getProperty("generationFile"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void readGeneration(String fileName) {
    try {
      InputStream input = new FileInputStream(CONFIG_DIR + fileName);
      Properties generation = new Properties();
      generation.load(input);

      gravity = validateDouble(generation, "gravity", 1);
      String[] keys = generation.getProperty("bodies", "").split(" ");
      generationData = new String[keys.length][9];
      for (int i = 0; i < keys.length; i++) {
        String[] position = generation.getProperty(keys[i] + ".position","0,0,0").split(",");
        generationData[i][0] = position[0];
        generationData[i][1] = position[1];
        generationData[i][2] = position[2];
        String[] velocity = generation.getProperty(keys[i] + ".velocity","0,0,0").split(",");
        generationData[i][3] = velocity[0];
        generationData[i][4] = velocity[1];
        generationData[i][5] = velocity[2];
        generationData[i][6] = generation.getProperty(keys[i] + ".mass", "1");
        generationData[i][7] = generation.getProperty(keys[i] + ".density", "1");
        generationData[i][8] = generation.getProperty(keys[i] + ".name", "Body " + i);
      }
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

  public static String[][] getGenerationData() {
    return generationData;
  }
}
