import java.util.ArrayList;

public class Simulation {

  public static void main(String[] args) {
    ArrayList<Body> bodies = new ArrayList<>();
    new Body(1000000, "Main");
    new Body(new Vector3D(0, 0, 10), new Vector3D(0, 0, 0), 1,
        "Satellite");

    for (int x = 0; x < 100; x++) {
      Body.updateAll();
    }
  }

}
