package us.stomberg.solarsystemsim.physics;

import us.stomberg.solarsystemsim.graphics.FormatText;

/**
 * Stores a 3D vector, containing an <b>x</b> component, a <b>y</b> component, and a <b>z</b> component. Each component
 * can be accessed individually through their getter methods. However, the power of this class lies in its methods for
 * comparing one vector to another.
 */
public class Vector3D {

    /**
     * <b>x</b> component of the vector.
     */
    private double x;

    /**
     * <b>y</b> component of the vector.
     */
    private double y;

    /**
     * <b>z</b> component of the vector.
     */
    private double z;

    /**
     * Class constructor specifying initial values.
     *
     * @param x initial <b>x</b> value
     * @param y initial <b>y</b> value
     * @param z initial <b>z</b> value
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Class constructor. Sets values to (0.0, 0.0, 0.0) as a default.
     */
    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Getter method for the <b>x</b> component.
     *
     * @return Returns the <b>x</b> component.
     */
    public double getX() {
        return x;
    }

    /**
     * Getter method for the <b>y</b> component.
     *
     * @return Returns the <b>y</b> component.
     */
    public double getY() {
        return y;
    }

    /**
     * Getter method for the <b>z</b> component.
     *
     * @return Returns the <b>z</b> component.
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets all values to those of another vector.
     */
    public Vector3D copyFrom(Vector3D vector) {
        x = vector.x;
        y = vector.y;
        z = vector.z;
        return this;
    }

    /**
     * Sums two vectors. Adds the corresponding components separately. This method returns a new vector rather than
     * setting the values of this vector.
     *
     * @param vector The second vector.
     * @return The sum of both vectors.
     */
    public Vector3D add(Vector3D vector) {
        return new Vector3D(x + vector.x, y + vector.y, z + vector.z);
    }

    /**
     * Adds another vector to this vector. This is an elementwise operation.
     *
     * @param vector The other vector.
     */
    public Vector3D addInPlace(Vector3D vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
        return this;
    }

    /**
     * Multiplies a vector with another number. This value multiplies each component. This function is used to
     * scale a vector.
     *
     * @param factor The factor by which to scale the vector.
     * @return The scaled vector.
     */
    public Vector3D scale(double factor) {
        return new Vector3D(x * factor, y * factor, z * factor);
    }

    /**
     * Multiplies a vector with another number. This value multiplies each component. This function is used to
     * scale a vector.
     *
     * @param factor The factor by which to scale the vector.
     * @return The scaled vector.
     */
    public Vector3D scaleInPlace(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
        return this;
    }

    public Vector3D negate() {
        return new Vector3D(-x, -y, -z);
    }

    public Vector3D negateInPlace() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * Gets the differences between the corresponding components of two vectors. Subtracts the second vector from the
     * first vector. Returned as an array of values.
     *
     * @param vector The second vector.
     * @return Returns the <b>x</b>, <b>y</b>, and <b>z</b> difference.
     */
    public Vector3D subtract(Vector3D vector) {
        return new Vector3D(vector.x - x, vector.y - y, vector.z - z);
    }

    /**
     * Gets the differences between the corresponding components of two vectors. Subtracts the second vector from the
     * first vector. Returned as an array of values.
     *
     * @param vector The second vector.
     * @return Returns the <b>x</b>, <b>y</b>, and <b>z</b> difference.
     */
    public Vector3D subtractInPlace(Vector3D vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
        return this;
    }

    /**
     * Calculates the 3D distance between two vectors. First calls the <code>compareTo</code> method to find the
     * separate linear distances between the two vectors. It then uses the
     * <b>x</b> and <b>y</b> differences to calculate the composite horizontal distance between the
     * vectors. Finally, it uses the horizontal distance and the <b>z</b> difference to calculate the final 3D distance.
     *
     * @param vector The second vector.
     * @return Returns the 3D distance between the two vectors.
     */
    public double distance(Vector3D vector) {
        Vector3D compare = subtract(vector);
        return Math.hypot(Math.hypot(compare.getX(), compare.getY()), compare.getZ());
    }

    /**
     * Returns the vector's 3D distance to (0, 0, 0). Is equal to the vector's magnitude.
     *
     * @return Returns the vector's magnitude.
     */
    public double magnitude() {
        return Math.hypot(Math.hypot(x, y), z);
    }

    /**
     * Gets a vector representing the angle to another vector in 3D space. First it calls
     * <code>compareTo</code> to find the distances on each axis. Then it calls
     * <code>distanceTo</code> to find the hypotenuse of those angles. Finally, it creates a new
     * vector using the results from <code>compareTo</code> and puts it on a unit scale using the hypotenuse.
     *
     * @param vector The other vector.
     * @return Returns a vector on a unit scale to representing a direction.
     */
    public Vector3D angleTo(Vector3D vector) {
        return subtract(vector).normalizeInPlace();
    }

    /**
     * Gets a vector representing only the vector's direction, its angle to (0, 0, 0).
     *
     * @return Returns a vector on a unit scale to represent angle.
     */
    public Vector3D normalize() {
        return scale(1 / magnitude());
    }

    /**
     * Gets a vector representing only the vector's direction, its angle to (0, 0, 0).
     *
     * @return Returns a vector on a unit scale to represent angle.
     */
    public Vector3D normalizeInPlace() {
        return scaleInPlace(1 / magnitude());
    }

    public Vector3D interpolate(Vector3D other, double p) {
        return new Vector3D(x + (other.x - x) * p, y + (other.y - y) * p, z + (other.z - z) * p);
    }

    public Vector3D interpolateInPlace(Vector3D other, double p) {
        x += (other.x - x) * p;
        y += (other.y - y) * p;
        z += (other.z - z) * p;
        return this;
    }

    /**
     * Calculates the dot product between this vector and another vector.
     *
     * @param vector The other vector to calculate dot product with
     * @return the scalar dot product of the two vectors
     */
    public double dotProduct(Vector3D vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    /**
     * Create a new <code>Vector3D</code> with identical values.
     *
     * @return Returns the copied vector.
     */
    public Vector3D copy() {
        return new Vector3D(x, y, z);
    }

    /**
     * Formats the vector into a <code>String</code>.
     * <p>
     * eg. <code>"Vector3D(x=0.00, y=0.00, z=0.00)"</code>
     *
     * @return Returns a string expressing the vector.
     */
    public String toString(String unit) {
        String string = "Vector3D(x=" + FormatText.formatValue(x, unit, "k" + unit);
        string += ", y=" + FormatText.formatValue(y, unit, "k" + unit);
        string += ", z=" + FormatText.formatValue(z, unit, "k" + unit);
        return string + ")";
    }

}
