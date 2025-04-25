package us.stomberg.solarsystemsim;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Logger;

import us.stomberg.solarsystemsim.physics.Body;
import us.stomberg.solarsystemsim.physics.BodyBuilder;
import us.stomberg.solarsystemsim.physics.BodyFactory;
import us.stomberg.solarsystemsim.physics.Vector3D;

import javax.naming.ConfigurationException;

/**
 * Class to load and store all settings relating to the simulation.
 */
public class Setup {

    /**
     * Stores parameters for the graphics configuration.
     *
     * @param frameLimit      the artificial frame rate limit
     * @param shouldDrawTrail whether to draw the trail
     * @param trailHasAlpha   whether to render the trails with transparency
     * @param trailLength     the degrees of revolution the trail should cover
     * @param trailResolution the degrees of revolution between trail points
     * @param rotatePrecision the view's sensitivity to mouse movements
     * @param scalePrecision  the factor to scale by when zooming
     */
    private record GraphicsConfig(
            double frameLimit,
            boolean shouldDrawTrail,
            boolean trailHasAlpha,
            double trailLength,
            double trailResolution,
            double rotatePrecision,
            double scalePrecision
    ) {}
    /**
     * Stores parameters for the simulation mechanics.
     *
     * @param gravity  the universal gravitational constant
     * @param timeStep the simulation time step
     */
    private record SimulationConfig(double gravity, double timeStep) {}

    /**
     * The logger for this class.
     */
    private static final Logger logger = Logger.getLogger(Setup.class.getName());
    /**
     * The file path to the local .config directory.
     */
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.config/solarsystemsim/";
    /**
     * Default graphics configuration for the simulation.
     */
    private static final GraphicsConfig defaultGraphicsConfig = new GraphicsConfig(
            60,
            true,
            false,
            180,
            1.0,
            1.0,
            1.1
    );
    /**
     * Default simulation physics configuration.
     */
    private static final SimulationConfig defaultSimulationConfig = new SimulationConfig(1.0, 0.004);
    /**
     * The 2D array to store information about how to generate bodies.
     */
    private static final ArrayList<Body> generationData = new ArrayList<>();
    /**
     * The active graphics configuration.
     */
    private static GraphicsConfig graphicsConfig;
    /**
     * The active graphics configuration.
     */
    private static SimulationConfig simulationConfig;

    /**
     * Represents an exception that occurs during the processing of properties.
     * This exception serves as a general exception for property-related issues and
     * provides a base class for more specific property-related exceptions.
     */
    public static class PropertiesException extends ConfigurationException {
        public PropertiesException(String msg) {
            super(msg);
        }
    }

    /**
     * Represents an exception that occurs when a property is not found in a properties file.
     */
    public static class PropertyNotFoundException extends PropertiesException {
        public PropertyNotFoundException(String msg) {
            super(msg);
        }
    }

    /**
     * Represents an exception that occurs when a property is not formatted correctly.
     */
    public static class PropertyFormatException extends PropertiesException {
        public PropertyFormatException(String msg) {
            super(msg);
        }
    }

    /**
     * Represents an exception that occurs when a property file is not found.
     */
    public static class PropertyFileNotFoundException extends PropertiesException {
        public PropertyFileNotFoundException(String msg) {
            super(msg);
        }
    }

    /**
     * Reads the setup files and sets the fields accordingly.
     * If the main configuration file cannot be found or is invalid, default values will be used.
     * This method catches all exceptions and falls back to default settings when necessary,
     * ensuring the program can always start with at least basic configuration.
     */
    public static void read() {
        // Sets up a .properties file using InputStream.
        try (InputStream input = new FileInputStream(CONFIG_DIR + "setup.properties")) {
            Properties setup = new Properties();
            setup.load(input);
            //Gets the values for how to display the simulation.
            graphicsConfig = new GraphicsConfig(
                    validateProperty(setup, "frameLimit", Double::parseDouble, defaultGraphicsConfig.frameLimit()),
                    validateProperty(setup, "drawTrail", Boolean::parseBoolean, defaultGraphicsConfig.shouldDrawTrail()),
                    validateProperty(setup, "trailAlpha", Boolean::parseBoolean, defaultGraphicsConfig.trailHasAlpha()),
                    validateProperty(setup, "trailLength", Double::parseDouble, defaultGraphicsConfig.trailLength()),
                    validateProperty(setup, "trailResolution", Double::parseDouble, defaultGraphicsConfig.trailResolution()),
                    validateProperty(setup, "rotatePrecision", Double::parseDouble, defaultGraphicsConfig.rotatePrecision()),
                    validateProperty(setup, "scalePrecision", Double::parseDouble, defaultGraphicsConfig.scalePrecision()));
            logger.info("Setup file read successfully.");
            //Reads the system setup file.
            readGeneration(setup.getProperty("generationFile"));
        } catch (IOException e) {
            Exception e2 = new PropertyFileNotFoundException("Invalid main setup file: " + e.getMessage());
            logger.warning(e2.toString());
            graphicsConfig = defaultGraphicsConfig;
            setDefaultSystem();
        } catch (PropertiesException e) {
            logger.warning(e.toString());
            setDefaultSystem();
        }
    }

    /**
     * Getter method for the frames per second limit.
     *
     * @return the frame limit
     */
    public static double getFrameLimit() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).frameLimit();
    }

    /**
     * Getter method for the draw trail <code>boolean</code> value.
     *
     * @return whether to draw the trail
     */
    public static boolean isDrawingTrail() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).shouldDrawTrail();
    }

    /**
     * Getter method for the trail alpha <code>boolean</code> value.
     *
     * @return whether the trail has transparency
     */
    public static boolean trailHasAlpha() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailHasAlpha();
    }

    /**
     * Getter method for the trail length.
     *
     * @return the trail length
     */
    public static double getTrailLength() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailLength();
    }

    /**
     * Getter method for the trail resolution.
     *
     * @return the trail resolution
     */
    public static double getTrailResolution() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).trailResolution();
    }

    /**
     * Getter method for the rotation precision.
     *
     * @return the rotational precision
     */
    public static double getRotatePrecision() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).rotatePrecision();
    }

    /**
     * Getter method for the scaling precision.
     *
     * @return the scale precision
     */
    public static double getScalePrecision() {
        return Objects.requireNonNullElse(graphicsConfig, defaultGraphicsConfig).scalePrecision();
    }

    /**
     * Getter method for the gravitational constant.
     *
     * @return the gravity value
     */
    public static double getGravity() {
        return Objects.requireNonNullElse(simulationConfig, defaultSimulationConfig).gravity();
    }

    /**
     * Getter method for the simulation time step.
     *
     * @return the time step
     */
    public static double getTimeStep() {
        return Objects.requireNonNullElse(simulationConfig, defaultSimulationConfig).timeStep();
    }

    /**
     * Getter method for the generation data.
     *
     * @return the generation data
     */
    public static ArrayList<Body> getGenerationData() {
        return generationData;
    }

    /**
     * Reads a system setup file and writes info about the bodies to the <code>generationData</code> field.
     * This method enforces strict validation of the system setup properties.
     *
     * @param fileName name of a system setup file
     * @throws PropertyFormatException if any property has an invalid format
     * @throws PropertyNotFoundException if a required property is missing
     * @throws PropertyFileNotFoundException if the system setup file is missing or cannot be read
     */
    private static void readGeneration(String fileName)
            throws PropertyFormatException, PropertyNotFoundException, PropertyFileNotFoundException {
        if (fileName == null) {
            throw new PropertyFileNotFoundException("No system setup file specified");
        }
        try (InputStream input = new FileInputStream(CONFIG_DIR + fileName)) {
            // Sets up the .properties file using InputStream.
            Properties generation = new Properties();
            generation.load(input);
            // Sets the gravitational constant and gets the keys for the different bodies.
            simulationConfig = new SimulationConfig(
                    parseProperty(generation, "gravity", Double::parseDouble),
                    parseProperty(generation, "timeStep", Double::parseDouble)
            );
            String bodiesStr = generation.getProperty("bodies");
            if (bodiesStr == null) {
                throw new PropertyNotFoundException("No bodies specified in system setup file");
            }
            String[] keys = bodiesStr.split(" ");
            // Reads the body data for each body key.
            generationData.clear();
            for (String key : keys) {
                Body newBody = new BodyBuilder().position(parseVector(generation, key + ".position"))
                                                .velocity(parseVector(generation, key + ".velocity"))
                                                .mass(parseProperty(generation, key + ".mass", Double::parseDouble))
                                                .density(parseProperty(generation, key + ".density", Double::parseDouble))
                                                .name(generation.getProperty(key + ".name"))
                                                .color(parseColor(generation, key + ".color")).build();
                generationData.add(newBody);
            }
            logger.info("System setup file read successfully.");
        } catch (IOException e) {
            throw new PropertyFileNotFoundException("Invalid system setup file: " + e.getMessage());
        }
    }

    /**
     * Sets up a default solar system configuration when the configuration files
     * cannot be read or contain invalid data. This ensures the simulation can
     * always start with a basic system of a star and two planets.
     */
    private static void setDefaultSystem() {
        logger.info("Using default system.");
        generationData.clear();
        generationData.add(BodyFactory.createDefaultStar());
        generationData.add(BodyFactory.createDefaultInnerPlanet());
        generationData.add(BodyFactory.createDefaultOuterPlanet());
        simulationConfig = defaultSimulationConfig;
    }

    /**
     * Parses a string representation of a 3D vector and returns a corresponding Vector3D object. The input string
     * should be in the format "x,y,z", where x, y, and z are numeric values.
     *
     * @param setup the properties file containing the vector data
     * @param key   the key to read from the properties file
     * @return a Vector3D object containing the parsed vector values
     * @throws PropertyNotFoundException if the specified key is not found in the properties file
     * @throws PropertyFormatException if the vector format is invalid (wrong number of elements or non-numeric values)
     */
    private static Vector3D parseVector(Properties setup, String key)
        throws PropertyNotFoundException, PropertyFormatException {
        String in = setup.getProperty(key);
        if (in == null) {
            throw new PropertyNotFoundException("Key " + key + " not found in properties file");
        }

        String[] split = in.split(",");
        if (split.length != 3) {
            throw new PropertyFormatException("Invalid vector length for key " + key +
                                                      ": Found " + split.length + ", expected 3");
        }

        try {
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            return new Vector3D(x, y, z);
        } catch (NumberFormatException e) {
            throw new PropertyFormatException("Invalid vector values for key " + key + ": " + e.getMessage());
        }
    }

    /**
     * Parses a string representation of RGB color values and returns a corresponding Color object. The input string
     * should be in the format "r,g,b", where r, g, and b are integer values. Values are clamped between 0 and 255.
     *
     * @param setup the properties file containing the color data
     * @param key   the key to read from the properties file
     * @return a Color object containing the parsed RGB values (clamped to valid range)
     * @throws PropertyNotFoundException if the specified key is not found in the properties file
     * @throws PropertyFormatException if the color format is invalid (wrong number of elements or non-numeric values)
     */
    private static Color parseColor(Properties setup, String key)
        throws PropertyFormatException, PropertyNotFoundException {
        String in = setup.getProperty(key);
        if (in == null) {
            throw new PropertyNotFoundException("Key " + key + " not found in properties file");
        }

        String[] split = in.split(",");
        if (split.length != 3) {
            throw new PropertyFormatException("Invalid number of elements for key " + key +
                                                      ": Found " + split.length + ", expected 3");
        }

        try {
            int r = Math.clamp(Integer.parseInt(split[0]), 0, 255);
            int g = Math.clamp(Integer.parseInt(split[1]), 0, 255);
            int b = Math.clamp(Integer.parseInt(split[2]), 0, 255);
            return new Color(r, g, b);
        } catch (NumberFormatException e) {
            throw new PropertyFormatException("Invalid color values for key " + key + ": " + e.getMessage());
        }
    }

    /**
     * Gets a value from a properties file and attempts to convert it using the provided function.
     * If there is no property matching the key, it returns a default value.
     *
     * @param setup        the properties file
     * @param key          the key to read
     * @param converter    the conversion function
     * @param defaultValue the default value
     * @return the determined value or the default value if parsing fails
     */
    private static <T> T validateProperty(Properties setup, String key, Function<String, T> converter, T defaultValue) {
        String in = setup.getProperty(key);
        if (in == null) {
            logger.warning("No value found for key " + key + ", using default value " + defaultValue + ".");
            return defaultValue;
        }
        try {
            return converter.apply(in);
        } catch (NumberFormatException e) {
            logger.warning("Invalid value for key " + key + ": " + e.getMessage() +
                                   ", using default value " + defaultValue + ".");
            return defaultValue;
        }
    }

    /**
     * Gets a value from a properties file and attempts to convert it using the provided function.
     * This method enforces strict validation of property existence and format.
     *
     * @param setup     the properties file
     * @param key       the key to read
     * @param converter the conversion function to apply to the property value
     * @return the converted property value
     * @throws PropertyNotFoundException if the specified key is not found in the properties file
     * @throws PropertyFormatException if the property value cannot be converted using the provided function
     */
    private static <T> T parseProperty(Properties setup, String key, Function<String, T> converter)
            throws PropertyFormatException, PropertyNotFoundException {
        String in = setup.getProperty(key);
        if (in == null) {
            throw new PropertyNotFoundException("Key " + key + " not found in properties file");
        }
        try {
            return converter.apply(in);
        } catch (NumberFormatException e) {
            throw new PropertyFormatException("Invalid value for key " + key + ": " + e.getMessage());
        }
    }

}
