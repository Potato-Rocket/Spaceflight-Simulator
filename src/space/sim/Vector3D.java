package space.sim;

/**
 * Stores a 3D vector, containing an <b>x</b> component,  a <b>y</b> component, and a <b>z</b>
 * component. Each component can be accessed individually through their getter methods.
 * However, the power of this class lies in its methods for comparing one vector to another.
 */
public class Vector3D {

  /**
   * <b>x</b> component of the vector.
   */
  private double x;
  /**
   * <b>y</b> component of the vector .
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
   * Sums two vectors. Adds the corresponding components separately.
   *
   * @param vector second vector
   */
  public void addVector(Vector3D vector) {
    x = x + vector.x;
    y = y + vector.y;
    z = z + vector.z;
  }

  /**
   * Multiplies a vector with another number. Each component is multiplied by this value. This
   * function is used to scale a vector.
   *
   * @param factor factor to multiply with the vector
   * @return Returns a vector containing the scaled vector.
   */
  public Vector3D scaleVector(double factor) {
    return new Vector3D(x * factor, y * factor, z * factor);
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
  public double[] compareTo(Vector3D vector) {
    return new double[]{vector.x - x, vector.y - y, vector.z - z};
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
  public double distanceTo(Vector3D vector) {
    double[] compare = compareTo(vector);
    double horizontal = Math.hypot(compare[0], compare[1]);
    return Math.hypot(horizontal, compare[2]);
  }

  /**
   * Gets a vector representing the angle to another vector in 3D space. First it calls
   * <code>compareTo</code> to find the distances on each axis. Then it calls
   * <code>distanceTo</code> to find the hypotenuse of those angles. Finally, it creates a new
   * vector using the results from <code>compareTo</code> and puts it on a unit scale using the
   * hypotenuse.
   *
   * @param vector other vector
   * @return Returns a vector on a unit scale to represent angle.
   */
  public Vector3D angleTo(Vector3D vector) {
    double hypotenuse = distanceTo(vector);
    double[] compare = compareTo(vector);
    Vector3D nonScaled = new Vector3D(compare[0], compare[1], compare[2]);
    return nonScaled.scaleVector(1/ hypotenuse);
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
   * eg. <code>"Vector(x=0.00, y=0.00, z=0.00)"</code>
   *
   * @return Returns a string expressing the vector.
   */
  public String toString() {
    return "Vector(x=" + x + ", y=" + y + ", z=" + z + ")";
  }

}
