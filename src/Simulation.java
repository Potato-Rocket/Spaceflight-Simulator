import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Simulation {

  public static final BigDecimal EARTH_MASS = new BigDecimal("5972200000000000000000000");
  public static final BigDecimal LUNAR_MASS = new BigDecimal("73420000000000000000000");

  public static void main(String[] args) throws InterruptedException {
    new Body(LUNAR_MASS, "Moon");
    new Body(new Vector3D(BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("100000")),
        new Vector3D(), new BigDecimal("100"), "Object");

    while (true) {
      Body.updateAll();
      System.out.println(Body.allToString());
      TimeUnit.SECONDS.sleep(1);
    }
  }

}
