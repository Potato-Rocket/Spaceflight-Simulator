package space.sim.graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Rotation implements MouseMotionListener {

  public static int[] mousePos = new int[2];

  @Override
  public void mouseDragged(MouseEvent e) {
    if (mousePos[0] != 0 && mousePos[1] != 0) {
      double[] difference = {e.getX() - mousePos[0], e.getY() - mousePos[1]};
      Graphics3D.changeRot(difference[0] / 100);
      Graphics3D.changeAngle(difference[1] / 100);
    }
    mousePos = new int[] {e.getX(), e.getY()};
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

}