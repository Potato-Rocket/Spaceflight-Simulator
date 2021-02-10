import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stores a 3D vector, containing an <b>x</b> component,  a <b>y</b> component, and a <b>z</b>
 * component. Each component can be accessed individually through their getter methods.
 * However, the power of this class lies in its methods for comparing one vector to another.
 */
public class Vector3D {

  /**
   * <b>x</b> component of the vector.
   */
  private BigDecimal x;
  /**
   * <b>y</b> component of the vector .
   */
  private BigDecimal y;
  /**
   * <b>z</b> component of the vector.
   */
  private BigDecimal z;

  /**
   * Class constructor specifying initial values.
   *
   * @param x initial <b>x</b> value
   * @param y initial <b>y</b> value
   * @param z initial <b>z</b> value
   */
  public Vector3D(BigDecimal x, BigDecimal y, BigDecimal z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Class constructor. Sets values to (0.0, 0.0, 0.0) as a default.
   */
  public Vector3D() {
    this.x = BigDecimal.ZERO;
    this.y = BigDecimal.ZERO;
    this.z = BigDecimal.ZERO;
  }

  /**
   * Sums two vectors. Adds the corresponding components separately.
   *
   * @param vector second vector
   */
  public void addVector(Vector3D vector) {
    x = x.add(vector.x);
    y = y.add(vector.y);
    z = z.add(vector.z);
  }

  /**
   * Multiplies a vector with another number. Each component is multiplied by this value. This
   * function is used to scale a vector.
   *
   * @param factor factor to multiply with the vector
   * @return Returns a vector containing the scaled vector.
   */
  public Vector3D scaleVector(BigDecimal factor) {
    return new Vector3D(x.multiply(factor), y.multiply(factor), z.multiply(factor));
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
  public BigDecimal[] compareTo(Vector3D vector) {
    return new BigDecimal[]{vector.x.subtract(x), vector.y.subtract(y), vector.z.subtract(z)};
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
  public BigDecimal distanceTo(Vector3D vector) {
    BigDecimal[] compare = compareTo(vector);
    BigDecimal horizontal = StaticFunctions.hypotenuse(compare[0], compare[1]);
    return StaticFunctions.hypotenuse(horizontal, compare[2]);
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
    BigDecimal hypotenuse = distanceTo(vector);
    BigDecimal[] compare = compareTo(vector);
    Vector3D nonScaled = new Vector3D(compare[0], compare[1], compare[2]);
    return nonScaled.scaleVector(BigDecimal.ONE.setScale(32, RoundingMode.DOWN).
        divide(hypotenuse, RoundingMode.DOWN));
  }

  public void fixScale(int scale) {
    x = x.setScale(scale, RoundingMode.DOWN);
    y = y.setScale(scale, RoundingMode.DOWN);
    z = z.setScale(scale, RoundingMode.DOWN);
  }

  /**
   * Create a new <code>Vector</code> with identical values.
   *
   * @return Returns the copied vector.
   */
  public Vector3D copy() {
    return new Vector3D(x, y, z);
  }

  /**
   * Formats the vector into a <code>String</code>.
   * <p>
   * eg. <code>"Vector(x=0.0, y=0.0, z=0.0)"</code>
   *
   * @return Returns a string expressing the vector.
   */
  public String toString() {
    return "Vector(x=" + x.setScale(2, RoundingMode.DOWN) +
        ", y=" + y.setScale(2, RoundingMode.DOWN) +
        ", z=" + z.setScale(2, RoundingMode.DOWN) + ")";
  }

}
