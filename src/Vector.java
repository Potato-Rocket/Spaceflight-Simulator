/**
 * Stores a 3D vector, containing an <b>x</b> component,  a <b>y</b> component, and a <b>z</b>
 * component. Each component can be accessed individually through their getter methods.
 * However, the power of this class lies in its methods for comparing one vector to another.
 */
public class Vector {

  /** <b>x</b> component of the vector. */
  private double x;
  /** <b>y</b> component of the vector .*/
  private double y;
  /** <b>z</b> component of the vector. */
  private double z;

  /**
   * Class constructor specifying initial values.
   *
   * @param x initial <b>x</b> value
   * @param y initial <b>y</b> value
   * @param z initial <b>z</b> value
   */
  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /** Class constructor. Sets values to (0.0, 0.0, 0.0) as a default. */
  public Vector() {
    new Vector(0, 0, 0);
  }

  /**
   * Sums two vectors. Adds the corresponding components separately.
   *
   * @param vector second vector
   */
  public void addVector(Vector vector) {
    x += vector.getX();
    y += vector.getY();
    z += vector.getZ();
  }

  /**
   * Multiplies a vector with another number. Each component is multiplied by this value. This
   * function is used to scale a vector.
   *
   * @param factor factor to multiply with the vector
   */
  public void multiplyVector(double factor) {
    x *= factor;
    y *= factor;
    z *= factor;
  }

  /**
   * Gets the <b>x</b> component.
   *
   * @return Returns the <b>x</b> component of the vector.
   */
  public double getX() {
    return x;
  }

  /**
   * Gets the <b>y</b> component.
   *
   * @return Returns the <b>y</b> component of the vector.
   */
  public double getY() {
    return y;
  }

  /**
   * Gets the <b>z</b> component.
   *
   * @return Returns the <b>z</b> component of the vector.
   */
  public double getZ() {
    return z;
  }

  /**
   * Gets the differences between the corresponding components of two vectors.
   * Subtracts the second vector from the first vector. Returned as an array of values.
   * <p>
   * eg. <code>array([0.0, 0.0, 0.0])</code>
   *
   * @param vector second vector
   * @return Returns the <b>x</b>, <b>y</b>, and <b>z</b> difference.
   */
  public double[] compareTo(Vector vector) {
    return new double[]{x - vector.getX(), y - vector.getY(), z - vector.getZ()};
  }

  /**
   * Calculates the 3D distance between two vectors. First calls the <code>compareTo</code>
   * method to find the separate linear distances between the two vectors. It then uses the
   * <b>x</b> and <b>y</b> differences to calculate the composite horizontal distance between the
   * vectors. Finally it uses the horizontal distance and the <b>z</b> difference to calculate the
   * final 3D distance.
   *
   * @param vector second vector
   * @return Returns the 3D distance between the two vectors.
   */
  public double distanceTo(Vector vector) {
    double[] compare = this.compareTo(vector);
    double horizontal = Math.hypot(compare[0], compare[1]);
    return Math.hypot(horizontal, compare[2]);
  }

  /**
   * Create a new <code>Vector</code> with identical values.
   *
   * @return Returns the copied vector.
   */
  public Vector copy() {
    return new Vector(x, y, z);
  }

  /**
   * Verifies whether two vectors are equal. Compares each pair of corresponding components. Each
   * pair must contain equal values for the method to return the vectors as equal.
   *
   * @param vector second vector
   * @return <code>true</code> if the vectors contain equal values;
   * <code>false</code> otherwise.
   */
  public boolean equals(Vector vector) {
    return vector.getX() == x && vector.getY() == y && vector.getZ() == z;
  }

  /**
   * Formats the vector into a <code>String</code>.
   * <p>
   * eg. <code>"Vector(x=0.0, y=0.0, z=0.0)"</code>
   *
   * @return Returns a string expressing the vector.
   */
  public String toString() {
    return "Vector(x=" + x + ", y=" + y + ", z=" + z + ")";
  }
}
