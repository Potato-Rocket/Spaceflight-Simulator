package us.stomberg.solarsystemsim;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

import us.stomberg.solarsystemsim.physics.Body;
import us.stomberg.solarsystemsim.physics.Vector3D;

/**
 * Class to load and store all settings relating to the simulation.
 */
public class Setup {

    /**
     * The file path to the local .config directory.
     */
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.config/solarsystm/";

    /**
     * Universal gravitational constant.
     */
    private static double gravity;

    private static final double DEFAULT_GRAVITY = 1.0;

    /**
     * Stores parameters for the graphics configuration.
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
    private static final ArrayList<Body> generationData = new ArrayList<>();

    /**
     * Reads the setup files and sets the fields accordingly.
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
                validateProperty(setup, "frameLimit", Integer::parseInt, defaultGraphicsConfig.frameLimit()),
                validateProperty(setup, "drawTrail", Boolean::parseBoolean, defaultGraphicsConfig.shouldDrawTrail()),
                validateProperty(setup, "trailAlpha", Boolean::parseBoolean, defaultGraphicsConfig.trailHasAlpha()),
                validateProperty(setup, "trailLength", Double::parseDouble, defaultGraphicsConfig.trailLength()),
                validateProperty(setup, "trailResolution", Double::parseDouble, defaultGraphicsConfig.trailResolution()),
                validateProperty(setup, "rotatePrecision", Double::parseDouble, defaultGraphicsConfig.rotatePrecision()),
                validateProperty(setup, "scalePrecision", Double::parseDouble, defaultGraphicsConfig.scalePrecision())
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
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).frameLimit();
    }

    /**
     * Getter method for the draw trail <code>boolean</code> value.
     *
     * @return Returns whether to draw the trail.
     */
    public static boolean isDrawingTrail() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).shouldDrawTrail();
    }

    /**
     * Getter method for the trail alpha <code>boolean</code> value.
     *
     * @return Returns whether the trail has transparency.
     */
    public static boolean trailHasAlpha() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailHasAlpha();
    }

    /**
     * Getter method for the trail length.
     *
     * @return Returns the trail length.
     */
    public static double getTrailLength() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailLength();
    }

    /**
     * Getter method for the trail resolution.
     *
     * @return Returns the trail resolution.
     */
    public static double getTrailResolution() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailResolution();
    }

    /**
     * Getter method for the rotation precision.
     *
     * @return Returns the rotational precision.
     */
    public static double getRotatePrecision() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).rotatePrecision();
    }

    /**
     * Getter method for the scaling precision.
     *
     * @return Returns the scale precision.
     */
    public static double getScalePrecision() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).scalePrecision();
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
    public static ArrayList<Body> getGenerationData() {
        return generationData;
    }

    /**
     * Reads a system setup file and writes info about the bodies to the <code>genData</code> field.
     *
     * @param fileName name of a system setup file
     */
    private static void readGeneration(String fileName) {
        try {
            //Sets up the .properties file using InputStream.
            InputStream input = new FileInputStream(CONFIG_DIR + fileName);
            Properties generation = new Properties();
            generation.load(input);
            //Sets the gravitational constant and gets the keys for the different bodies.
            gravity = validateProperty(generation, "gravity", Double::parseDouble, DEFAULT_GRAVITY);
            String[] keys = generation.getProperty("bodies", "").split(" ");
            //Reads the body data for each body key.
            generationData.clear();
            for (String key : keys) {
                Body newBody = new Body.Builder()
                        .position(parseVector(generation, key + ".position"))
                        .velocity(parseVector(generation, key + ".velocity"))
                        .mass(parseProperty(generation, key + ".mass", Double::parseDouble))
                        .density(parseProperty(generation, key + ".density", Double::parseDouble))
                        .name(generation.getProperty(key + ".name"))
                        .color(parseColor(generation, key + ".color"))
                        .build();
                generationData.add(newBody);
            }
            
            if (generationData.isEmpty()) {
                throw new IOException("System setup was empty; Cannot have system with 0 bodies.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            setDefaultSystem();
        }
    }

    private static void setDefaultSystem() {
        generationData.clear();
        generationData.add(Body.Factory.createDefaultStar());
        generationData.add(Body.Factory.createDefaultInnerPlanet());
        generationData.add(Body.Factory.createDefaultOuterPlanet());
        gravity = DEFAULT_GRAVITY;
    }

    /**
     * Parses a string representation of a 3D vector and returns a corresponding Vector3D object. The input string
     * should be in the format "x,y,z", where x, y, and z are numeric values.
     *
     * @param setup the properties file containing the vector data
     * @param key the key to read from the properties file
     * @return a Vector3D object if the input string is valid, or null if the input is null, does not have exactly three
     *         components or contains non-numeric values
     */
    private static Vector3D parseVector(Properties setup, String key) {
        String in = setup.getProperty(key);
        if (in == null) {
            return null;
        }

        String[] split = in.split(",");
        if (split.length != 3) {
            return null;
        }

        try {
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            return new Vector3D(x, y, z);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Parses a string representation of RGB color values and returns a corresponding Color object. The input string
     * should be in the format "r,g,b", where r, g, and b are integer values. Values are clamped between 0 and 255.
     *
     * @param setup the properties file containing the color data
     * @param key the key to read from the properties file
     * @return a Color object if the input string is valid, or null if the input is null, does not have exactly three
     *         components or contains non-numeric values
     */
    private static Color parseColor(Properties setup, String key) {
        String in = setup.getProperty(key);
        if (in == null) {
            return null;
        }

        String[] split = in.split(",");
        if (split.length != 3) {
            return null;
        }

        try {
            int r = Math.clamp(Integer.parseInt(split[0]), 0, 255);
            int g = Math.clamp(Integer.parseInt(split[1]), 0, 255);
            int b = Math.clamp(Integer.parseInt(split[2]), 0, 255);
            return new Color(r, g, b);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Gets a value from a properties file, and attempts to convert it using the provided function. If there is no
     * property matching the key, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param converter    the conversion function
     * @param defaultValue the default value
     * @return Returns the determined value.
     */
    private static <T> T validateProperty(Properties setup, String key, Function<String, T> converter, T defaultValue) {
        String in = setup.getProperty(key);
        if (in == null) {
            return defaultValue;
        }
        try {
            return converter.apply(in);
        } catch (Exception e) {
            System.out.println(e);
            return defaultValue;
        }
    }

    /**
     * Gets a value from a properties file, and attempts to convert it using the provided function. If there is no
     * property matching the key, it returns null.
     *
     * @param setup     the properties file
     * @param key       the key to read
     * @param converter the conversion function
     * @return Returns the determined value or null if parsing fails.
     */
    private static <T> T parseProperty(Properties setup, String key, Function<String, T> converter) {
        String in = setup.getProperty(key);
        if (in == null) {
            return null;
        }
        try {
            return converter.apply(in);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
