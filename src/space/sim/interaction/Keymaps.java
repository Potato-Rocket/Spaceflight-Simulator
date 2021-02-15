package space.sim.interaction;

import space.sim.physics.Physics;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Keymaps {

  public Keymaps(JComponent component) {
    InputMap inputMap = component.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap actionMap = component.getActionMap();
    inputMap.put(KeyStroke.getKeyStroke("released W"), "SPEED_UP");
    actionMap.put("SPEED_UP", speedUp);
    inputMap.put(KeyStroke.getKeyStroke("released S"), "SLOW_DOWN");
    actionMap.put("SLOW_DOWN", slowDown);
  }

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

}
