package space.sim.interaction;

import space.sim.graphics.Draw;
import space.sim.graphics.Graphics3D;
import space.sim.physics.Physics;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Keymaps {

  public Keymaps(JComponent component) {
    InputMap inputMap = component.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap actionMap = component.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke("released F1"), "RESET_VIEW");
    actionMap.put("RESET_VIEW", resetView);

    inputMap.put(KeyStroke.getKeyStroke("J"), "ZOOM_IN");
    actionMap.put("ZOOM_IN", zoomIn);
    inputMap.put(KeyStroke.getKeyStroke("K"), "ZOOM_OUT");
    actionMap.put("ZOOM_OUT", zoomOut);
    inputMap.put(KeyStroke.getKeyStroke("released H"), "PREV_FOCUS");
    actionMap.put("PREV_FOCUS", prevFocus);
    inputMap.put(KeyStroke.getKeyStroke("released L"), "NEXT_FOCUS");
    actionMap.put("NEXT_FOCUS", nextFocus);

    inputMap.put(KeyStroke.getKeyStroke("W"), "TILT_UP");
    actionMap.put("TILT_UP", tiltUp);
    inputMap.put(KeyStroke.getKeyStroke("S"), "TILT_DOWN");
    actionMap.put("TILT_DOWN", tiltDown);
    inputMap.put(KeyStroke.getKeyStroke("A"), "YAW_LEFT");
    actionMap.put("YAW_LEFT", yawLeft);
    inputMap.put(KeyStroke.getKeyStroke("D"), "YAW_RIGHT");
    actionMap.put("YAW_RIGHT", yawRight);
    inputMap.put(KeyStroke.getKeyStroke("released Q"), "SLOW_DOWN");
    actionMap.put("SLOW_DOWN", slowDown);
    inputMap.put(KeyStroke.getKeyStroke("released E"), "SPEED_UP");
    actionMap.put("SPEED_UP", speedUp);
  }

  Action tiltUp = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeTilt(Math.PI / -20);
    }
  };

  Action tiltDown = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeTilt(Math.PI / 20);
    }
  };

  Action yawLeft = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeYaw(Math.PI / -20);
    }
  };

  Action yawRight = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeYaw(Math.PI / 20);
    }
  };

  Action speedUp = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Physics.modifyTimeScale(true);
    }
  };

  Action slowDown = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Physics.modifyTimeScale(false);
    }
  };

  Action zoomIn = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < 10; i++) {
        Draw.modifyBounds(-1);
      }
    }
  };

  Action zoomOut = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < 10; i++) {
        Draw.modifyBounds(1);
      }
    }
  };

  Action prevFocus = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.modifyFocus(-1);
    }
  };

  Action nextFocus = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.modifyFocus(1);
    }
  };

  Action resetView = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.resetBounds();
      Graphics3D.resetView();
    }
  };

}
