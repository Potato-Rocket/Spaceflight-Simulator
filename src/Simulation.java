import java.util.ArrayList;

public class Simulation {

  public static void main(String[] args) {
    ArrayList<Body> bodies = new ArrayList<>();
    bodies.add(new Body(1000000, "Main"));
    bodies.add(new Body(new Vector3D(0, 0, 10),
        new Vector3D(0, 0, 0), 1, "Satellite"));

    for (Body body : bodies) {
      body.createForces();
    }
  }

}
