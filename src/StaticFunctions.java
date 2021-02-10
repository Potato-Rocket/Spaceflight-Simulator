import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class StaticFunctions {

  public static BigDecimal hypotenuse(BigDecimal a, BigDecimal b) {
    return a.pow(2).add(b.pow(2)).sqrt(new MathContext(32, RoundingMode.DOWN));
  }

}
