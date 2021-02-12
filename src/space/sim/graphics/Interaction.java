package space.sim.graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Interaction implements MouseListener {

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    Rotation.mousePos = new int[2];
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

}
