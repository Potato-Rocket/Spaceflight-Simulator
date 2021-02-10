import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Simulation {

  public static final BigDecimal EARTH_MASS = new BigDecimal("5972200000000000000000000");

  public static void main(String[] args) throws InterruptedException {
    Body earth = new Body(EARTH_MASS, "Earth");
    Body object = new Body(new Vector3D(new BigDecimal("100000"), new BigDecimal("100000"),
        new BigDecimal("100000")), new Vector3D(new BigDecimal("10000"),
        new BigDecimal("10000"), new BigDecimal("-10000")),
        new BigDecimal("1000"), "Object");

    while (true) {
      Body.updateAll();
      System.out.println(object.toString(true));
      TimeUnit.SECONDS.sleep(1);
    }
  }

}
