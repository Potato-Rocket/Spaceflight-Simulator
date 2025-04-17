package space.sim.interaction;

import space.sim.graphics.Draw;
import space.sim.graphics.Graphics3D;
import space.sim.physics.Physics;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class to create and manage keymaps and their corresponding actions.
 */
public class Keymaps {

  /**
   * Class constructor. Creates an <code>inputMap</code> and an <code>actionMap</code> for each
   * <code>Action</code>.
   *
   * @param component <code>JComponent</code> in which to create the input and action maps
   */
  public Keymaps(JComponent component) {
    //Creates the inputMap and actionMap.
    InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = component.getActionMap();
    //Function key maps.
    inputMap.put(KeyStroke.getKeyStroke("released F1"), "RESET_VIEW");
    actionMap.put("RESET_VIEW", resetView);
    inputMap.put(KeyStroke.getKeyStroke("released F2"), "TOGGLE_LOG");
    actionMap.put("TOGGLE_LOG", toggleLog);
    inputMap.put(KeyStroke.getKeyStroke("released F3"), "TOGGLE_VERBOSE");
    actionMap.put("TOGGLE_VERBOSE", toggleVerbose);
    //Q&E keys to change the time scale.
    inputMap.put(KeyStroke.getKeyStroke("released Q"), "SLOW_DOWN");
    actionMap.put("SLOW_DOWN", slowDown);
    inputMap.put(KeyStroke.getKeyStroke("released E"), "SPEED_UP");
    actionMap.put("SPEED_UP", speedUp);
    //Vim keys to control zooming and toggling the focused body.
    inputMap.put(KeyStroke.getKeyStroke("J"), "ZOOM_IN");
    actionMap.put("ZOOM_IN", zoomIn);
    inputMap.put(KeyStroke.getKeyStroke("K"), "ZOOM_OUT");
    actionMap.put("ZOOM_OUT", zoomOut);
    inputMap.put(KeyStroke.getKeyStroke("released H"), "PREV_FOCUS");
    actionMap.put("PREV_FOCUS", prevFocus);
    inputMap.put(KeyStroke.getKeyStroke("released L"), "NEXT_FOCUS");
    actionMap.put("NEXT_FOCUS", nextFocus);
    //WASD keys to rotate the view.
    inputMap.put(KeyStroke.getKeyStroke("W"), "TILT_UP");
    actionMap.put("TILT_UP", tiltUp);
    inputMap.put(KeyStroke.getKeyStroke("S"), "TILT_DOWN");
    actionMap.put("TILT_DOWN", tiltDown);
    inputMap.put(KeyStroke.getKeyStroke("A"), "YAW_LEFT");
    actionMap.put("YAW_LEFT", yawLeft);
    inputMap.put(KeyStroke.getKeyStroke("D"), "YAW_RIGHT");
    actionMap.put("YAW_RIGHT", yawRight);
  }

  /**
   * Tilts the view up.
   */
  private Action tiltUp = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeTilt(Math.PI / -36);
    }
  };

  /**
   * Tilts the view down.
   */
  private Action tiltDown = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeTilt(Math.PI / 36);
    }
  };

  /**
   * Spins the view left.
   */
  private Action yawLeft = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeYaw(Math.PI / -36);
    }
  };

  /**
   * Spins the view right.
   */
  private Action yawRight = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Graphics3D.changeYaw(Math.PI / 36);
    }
  };

  /**
   * Speeds up the time scale.
   */
  private Action speedUp = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Physics.modifyTimeScale(true);
    }
  };

  /**
   * Slows down the time scale.
   */
  private Action slowDown = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Physics.modifyTimeScale(false);
    }
  };

  /**
   * Zooms in the view.
   */
  private Action zoomIn = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < 10; i++) {
        Draw.modifyBounds(-1);
      }
    }
  };

  /**
   * Zooms out the view.
   */
  private Action zoomOut = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = 0; i < 10; i++) {
        Draw.modifyBounds(1);
      }
    }
  };

  /**
   * Toggles the focus to the previous body.
   */
  private Action prevFocus = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.modifyFocus(-1);
    }
  };

  /**
   * Toggles the focus to the next body.
   */
  private Action nextFocus = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.modifyFocus(1);
    }
  };

  /**
   * Resets the view scaling and rotation.
   */
  private Action resetView = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.resetBounds();
      Graphics3D.resetView();
    }
  };

  /**
   * Toggles whether to draw planet size to scale or in a lgo scale for visibility.
   */
  private Action toggleLog = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.toggleLogScale();
    }
  };

  /**
   * Toggles whether to draw planet size to scale or in a lgo scale for visibility.
   */
  private Action toggleVerbose = new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent e) {
      Draw.toggleVerboseOut();
    }
  };

}
