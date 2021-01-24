package gui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import com.google.common.collect.Maps;

import json.NameAndDescription;

public class BaseRadioOptionsPanel<T extends NameAndDescription> extends JPanel {
  private static final int MAX_NUM_OPTIONS = 100;

  public static final String DELIMITER = ";";

  private static final long serialVersionUID = -1427304877583197708L;

  private List<JRadioButton> radioButtons;
  private ButtonGroup buttonGroup;
  private String selected;
  private Map<String, T> nameToValue;
  private T defaultValue;

  public BaseRadioOptionsPanel(String header, List<T> values, String selected) {
    defaultValue = values.get(0);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setAlignmentY(Component.TOP_ALIGNMENT);
    JLabel headerLabel = new JLabel(header);
    radioButtons = new ArrayList<>();
    buttonGroup = new ButtonGroup();
    nameToValue = Maps.newHashMap();

    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.add(headerLabel);
    this.add(Box.createRigidArea(new Dimension(0, 10)));
    setRadioLabels(values, selected);
  }

  public T getValue() {
    return nameToValue.containsKey(selected) ? nameToValue.get(selected) : defaultValue;
  }

  private void setRadioLabels(List<T> values, String selected) {
    ActionListener radioListener = new OnRadioClick();
    for (int i = 0; i < values.size(); i++) {
      if (i >= MAX_NUM_OPTIONS) {
        break;
      }
      String name = values.get(i).getName();
      String desc = values.get(i).getDesc();

      if (nameToValue.containsKey(name)) {
        continue;
      }
      nameToValue.put(name, values.get(i));

      JRadioButton btn = new JRadioButton(name);
      btn.setActionCommand(name);
      btn.setToolTipText(desc);
      btn.addActionListener(radioListener);
      if (selected != null && name.equals(selected)) {
        btn.setSelected(true);
      }
      buttonGroup.add(btn);
      radioButtons.add(btn);
      this.add(btn);
    }
  }

  private class OnRadioClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      selected = e.getActionCommand();
    }
  }
}
