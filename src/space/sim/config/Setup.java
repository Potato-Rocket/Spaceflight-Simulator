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

  /**
   * The file path to the local .config directory.
   */
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
  private static double rotatePrecision = 1;

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

  /**
   * Reads the setup files and sets the fields accordingly.
   *
   * @throws IOException when the setup file directories are incorrect
   */
  public static void read() throws IOException {
    try {
      //Sets up Properties file using InputStream.
      InputStream input = new FileInputStream(CONFIG_DIR + "setup.properties");
      Properties setup = new Properties();
      setup.load(input);
      //Gets the values for how to display the simulation.
      frameLimit = validateInt(setup, "frameLimit", 60);
      drawTrail = validateBoolean(setup, "drawTrail", true);
      trailAlpha = validateBoolean(setup, "trailAlpha", false);
      trailLength = validateInt(setup, "trailLength", 180);
      trailResolution = validateDouble(setup, "trailResolution", 1);
      rotatePrecision = validateDouble(setup, "rotatePrecision", 1);
      scalePrecision = validateDouble(setup, "scalePrecision", 1.1);
      //Reads the system setup file.
      readGeneration(setup.getProperty("generationFile"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter method for the frames per second limit.
   *
   * @return Returns the frame limit.
   */
  public static int getFrameLimit() {
    return frameLimit;
  }

  /**
   * Getter method for the draw trail <code>boolean</code> value.
   *
   * @return Returns whether to draw the trail.
   */
  public static boolean isDrawingTrail() {
    return drawTrail;
  }

  /**
   * Getter method for the trail alpha <code>boolean</code> value.
   *
   * @return Returns whether the trail has transparency.
   */
  public static boolean trailHasAlpha() {
    return trailAlpha;
  }

  /**
   * Getter method for the trail length.
   *
   * @return Returns the trail length.
   */
  public static double getTrailLength() {
    return trailLength;
  }

  /**
   * Getter method for the trail resolution.
   *
   * @return Returns the trail resolution.
   */
  public static double getTrailResolution() {
    return trailResolution;
  }

  /**
   * Getter method for the rotation precision.
   *
   * @return Returns the rotate precision.
   */
  public static double getRotatePrecision() {
    return rotatePrecision;
  }

  /**
   * Getter method for the scaling precision.
   *
   * @return Returns the scale precision.
   */
  public static double getScalePrecision() {
    return scalePrecision;
  }

  /**
   * Getter method for the gravitational constant.
   *
   * @return Returns the gravity.
   */
  public static double getGravity() {
    return gravity;
  }

  /**
   * Getter method for the generation data.
   *
   * @return Returns the generation data.
   */
  public static String[][] getGenerationData() {
    return generationData;
  }

  //TODO: Differentiate between different body constructors.
  /**
   * Reads a system setup file and writes info about the bodies to the <code>genData</code> field.
   *
   * @param fileName name of system setup file
   */
  private static void readGeneration(String fileName) {
    try {
      //Sets up Properties file using InputStream.
      InputStream input = new FileInputStream(CONFIG_DIR + fileName);
      Properties generation = new Properties();
      generation.load(input);
      //Sets the gravitational constant and gets the keys for the different bodies.
      gravity = validateDouble(generation, "gravity", 1);
      String[] keys = generation.getProperty("bodies", "").split(" ");
      //Reads the body data for each body key.
      generationData = new String[keys.length][12];
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
        String[] color = generation.getProperty(keys[i] + ".color","255,255,255").split(",");
        generationData[i][9] = color[0];
        generationData[i][10] = color[1];
        generationData[i][11] = color[2];
      }
      if (generationData.length < 1) {
        throw new IOException("System setup was empty; Cannot have system with 0 bodies.");
      }
    } catch (IOException e) {
      e.printStackTrace();
      gravity = 1;
      generationData = new String[][] {
          {"0", "0", "0", "0", "0", "0", "1000000", "1", "Star", "255", "255", "255"},
          {"1000", "0", "0", "0", "30", "0", "1000", "1", "Satellite", "127", "127", "127"}
      };
    }
  }

  /**
   * Gets and int value from a properties file. If the property cannot be converted to an
   * <code>integer</code>, it returns a default value.
   *
   * @param setup the properties file
   * @param key the key to read
   * @param defaultValue the default value
   * @return Returns the determined value.
   */
  private static int validateInt(Properties setup, String key, int defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    try {
      return Integer.parseInt(in);
    } catch (NumberFormatException e) {
      System.out.println(e.toString());
      return defaultValue;
    }
  }

  /**
   * Gets and double value from a properties file. If the property cannot be converted to an
   * <code>double</code>, it returns a default value.
   *
   * @param setup the properties file
   * @param key the key to read
   * @param defaultValue the default value
   * @return Returns the determined value.
   */
  private static double validateDouble(Properties setup, String key, double defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    try {
      return Double.parseDouble(in);
    } catch (NumberFormatException e) {
      System.out.println(e.toString());
      return defaultValue;
    }
  }

  /**
   * Gets and int value from a properties file. If there is no property matching the key, it
   * returns a default value.
   *
   * @param setup the properties file
   * @param key the key to read
   * @param defaultValue the default value
   * @return Returns the determined value.
   */
  private static boolean validateBoolean(Properties setup, String key, boolean defaultValue) {
    String in = setup.getProperty(key, String.valueOf(defaultValue));
    return Boolean.parseBoolean(in);
  }

}
