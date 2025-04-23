package us.stomberg.solarsystemsim;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to load and store all settings relating to the simulation.
 */
public class Setup {

    /**
     * The file path to the local .config directory.
     */
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.config/solarsystemsim/";

    /**
     * Universal gravitational constant.
     */
    private static double gravity = 1;

    /**
     * Stores parameters for the graphics configuration.
     * TODO: Investigate how angular trail length is derived.
     *
     * @param frameLimit Stores the artificial frame rate limit.
     * @param shouldDrawTrail Whether to draw the trail.
     * @param trailHasAlpha Stores whether to render the trails with transparency.
     * @param trailLength The degrees of revolution the trail should cover.
     * @param trailResolution The degrees of revolution between trail points.
     * @param rotatePrecision Stores the view's sensitivity to mouse movements.
     * @param scalePrecision Factor to scale by when zooming.
     */
    private record GraphicsConfig(
            int frameLimit,
            boolean shouldDrawTrail,
            boolean trailHasAlpha,
            double trailLength,
            double trailResolution,
            double rotatePrecision,
            double scalePrecision
    ) {}

    private static GraphicsConfig graphicsConfig;

    private static final GraphicsConfig defaultGraphicsConfig = new GraphicsConfig(
            60,
            true,
            true,
            360,
            1,
            1,
            1.1
    );

    /**
     * The 2D array to store information about how to generate bodies.
     */
    private static String[][] generationData = {};

    /**
     * Reads the setup files and sets the fields accordingly.
     * TODO: Improve error handling and include a default setup file.
     * TODO: Implement records to organize and contain setup data.
     * TODO: Integrate system generation data with the system class to be more flexible.
     *
     * @throws IOException when the setup file directories are incorrect
     */
    public static void read() throws IOException {
        try {
            // Sets up a .properties file using InputStream.
            InputStream input = new FileInputStream(CONFIG_DIR + "setup.properties");
            Properties setup = new Properties();
            setup.load(input);
            //Gets the values for how to display the simulation.
            graphicsConfig = new GraphicsConfig(
                validateInt(setup, "frameLimit", defaultGraphicsConfig.frameLimit()),
                validateBoolean(setup, "drawTrail", defaultGraphicsConfig.shouldDrawTrail()),
                validateBoolean(setup, "trailAlpha", defaultGraphicsConfig.trailHasAlpha()),
                validateDouble(setup, "trailLength", defaultGraphicsConfig.trailLength()),
                validateDouble(setup, "trailResolution", defaultGraphicsConfig.trailResolution()),
                validateDouble(setup, "rotatePrecision", defaultGraphicsConfig.rotatePrecision()),
                validateDouble(setup, "scalePrecision", defaultGraphicsConfig.scalePrecision())
            );
            //Reads the system setup file.
            readGeneration(setup.getProperty("generationFile"));
        } catch (IOException e) {
            e.printStackTrace();
            setDefaultSystem();
        }
    }

    /**
     * Getter method for the frames per second limit.
     *
     * @return Returns the frame limit.
     */
    public static int getFrameLimit() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.frameLimit();
        }
        return graphicsConfig.frameLimit();
    }

    /**
     * Getter method for the draw trail <code>boolean</code> value.
     *
     * @return Returns whether to draw the trail.
     */
    public static boolean isDrawingTrail() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.shouldDrawTrail();
        }
        return graphicsConfig.shouldDrawTrail();
    }

    /**
     * Getter method for the trail alpha <code>boolean</code> value.
     *
     * @return Returns whether the trail has transparency.
     */
    public static boolean trailHasAlpha() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.trailHasAlpha();
        }
        return graphicsConfig.trailHasAlpha();
    }

    /**
     * Getter method for the trail length.
     *
     * @return Returns the trail length.
     */
    public static double getTrailLength() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.trailLength();
        }
        return graphicsConfig.trailLength();
    }

    /**
     * Getter method for the trail resolution.
     *
     * @return Returns the trail resolution.
     */
    public static double getTrailResolution() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.trailResolution();
        }
        return graphicsConfig.trailResolution();
    }

    /**
     * Getter method for the rotation precision.
     *
     * @return Returns the rotational precision.
     */
    public static double getRotatePrecision() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.rotatePrecision();
        }
        return graphicsConfig.rotatePrecision();
    }

    /**
     * Getter method for the scaling precision.
     *
     * @return Returns the scale precision.
     */
    public static double getScalePrecision() {
        if (graphicsConfig == null) {
            return defaultGraphicsConfig.scalePrecision();
        }
        return graphicsConfig.scalePrecision();
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

    /**
     * Reads a system setup file and writes info about the bodies to the <code>genData</code> field.
     *
     * @param fileName name of a system setup file
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
                String[] position = generation.getProperty(keys[i] + ".position", "0,0,0").split(",");
                generationData[i][0] = position[0];
                generationData[i][1] = position[1];
                generationData[i][2] = position[2];
                String[] velocity = generation.getProperty(keys[i] + ".velocity", "0,0,0").split(",");
                generationData[i][3] = velocity[0];
                generationData[i][4] = velocity[1];
                generationData[i][5] = velocity[2];
                generationData[i][6] = generation.getProperty(keys[i] + ".mass", "1");
                generationData[i][7] = generation.getProperty(keys[i] + ".density", "1");
                generationData[i][8] = generation.getProperty(keys[i] + ".name", "Body " + i);
                String[] color = generation.getProperty(keys[i] + ".color", "255,255,255").split(",");
                generationData[i][9] = color[0];
                generationData[i][10] = color[1];
                generationData[i][11] = color[2];
            }
            if (generationData.length < 1) {
                throw new IOException("System setup was empty; Cannot have system with 0 bodies.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            setDefaultSystem();
            gravity = 1;
        }
    }

    private static void setDefaultSystem() {
        generationData = new String[][]{{"0", "0", "0", "0", "0", "0", "1000000", "1", "Star", "255", "255", "255"},
                {"1000", "0", "0", "0", "30", "0", "1000", "1", "Satellite", "127", "127", "127"}};
    }

    /**
     * Gets and int value from a properties file. If the property cannot be converted to an
     * <code>integer</code>, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param defaultValue the default value
     * @return Returns the determined value.
     */
    private static int validateInt(Properties setup, String key, int defaultValue) {
        String in = setup.getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return defaultValue;
        }
    }

    /**
     * Gets and double value from a properties file. If the property cannot be converted to an
     * <code>double</code>, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param defaultValue the default value
     * @return Returns the determined value.
     */
    private static double validateDouble(Properties setup, String key, double defaultValue) {
        String in = setup.getProperty(key, String.valueOf(defaultValue));
        try {
            return Double.parseDouble(in);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return defaultValue;
        }
    }

    /**
     * Gets and int value from a properties file. If there is no property matching the key, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param defaultValue the default value
     * @return Returns the determined value.
     */
    private static boolean validateBoolean(Properties setup, String key, boolean defaultValue) {
        String in = setup.getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(in);
    }

}
