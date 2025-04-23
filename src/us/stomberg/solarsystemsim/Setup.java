package us.stomberg.solarsystemsim;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import us.stomberg.solarsystemsim.physics.Body;
import us.stomberg.solarsystemsim.physics.Vector3D;

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
    private static ArrayList<Body> generationData;

    /**
     * Reads the setup files and sets the fields accordingly.
     * TODO: Improve error handling and include a default setup file.
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
     * TODO: Integrate system generation data with the Physics.createBodies() to be more flexible.
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
            gravity = validateDouble(generation, "gravity", gravity);
            String[] keys = generation.getProperty("bodies", "").split(" ");
            //Reads the body data for each body key.
            generationData = new ArrayList<>();

            for (String key : keys) {
                Body newBody = new Body.Builder()
                        .position(parseVector(generation, key + ".position"))
                        .velocity(parseVector(generation, key + ".velocity"))
                        .mass(parseDouble(generation, key + ".mass"))
                        .density(parseDouble(generation, key + ".density"))
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
            gravity = 1;
        }
    }

    private static void setDefaultSystem() {
        generationData = new ArrayList<>();
        
        // Create a central star (Sun-like)
        Body star = new Body.Builder()
                .position(new Vector3D(0, 0, 0))
                .velocity(new Vector3D(0, 0, 0))
                .mass(1000000.)
                .density(1.4)
                .name("Sun")
                .color(new Color(255, 220, 0))
                .build();
        generationData.add(star);
        
        // Create an inner planet (Mercury-like)
        Body innerPlanet = new Body.Builder()
                .position(new Vector3D(800, 0, 50))
                .velocity(new Vector3D(0, 35, 0))
                .mass(1000.)
                .density(5.4)
                .name("Inner Planet")
                .color(new Color(180, 180, 180))
                .build();
        generationData.add(innerPlanet);
        
        // Create an outer planet (Earth-like)
        Body outerPlanet = new Body.Builder()
                .position(new Vector3D(1500, 0, 0))
                .velocity(new Vector3D(-5, 25, 0))
                .mass(5000.)
                .density(5.5)
                .name("Outer Planet")
                .color(new Color(0, 100, 255))
                .build();
        generationData.add(outerPlanet);
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
     * Gets and int value from a properties file. If the property cannot be converted to an
     * <code>integer</code>, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param defaultValue the default value
     * @return Returns the determined value.
     */
    private static int validateInt(Properties setup, String key, int defaultValue) {
        String in = setup.getProperty(key);
        if (in == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return defaultValue;
        }
    }

    /**
     * Gets a Double value from a properties file. If the property cannot be converted to a Double, it returns null.
     *
     * @param setup the properties file
     * @param key   the key to read
     * @return Returns the parsed Double value or null if parsing fails.
     */
    private static Double parseDouble(Properties setup, String key) {
        String in = setup.getProperty(key);
        if (in == null) {
            return null;
        }
        try {
            return Double.parseDouble(in);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
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
        String in = setup.getProperty(key);
        if (in == null) {
            return defaultValue;
        }
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
        String in = setup.getProperty(key);
        if (in == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(in);
        } catch (Exception e) {
            System.out.println(e);
            return defaultValue;
        }
    }

}
