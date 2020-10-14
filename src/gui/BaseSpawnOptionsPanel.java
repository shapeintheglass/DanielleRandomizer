package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import json.GenericSpawnSettingsJson;

public class BaseSpawnOptionsPanel extends JPanel {

  private static final long serialVersionUID = -1427304877583197708L;

  private JLabel headerLabel;
  private List<JRadioButton> radioButtons;
  private List<GenericSpawnSettingsJson> settings;
  private ButtonGroup buttonGroup;

  String header;
  String prefix;
  private ActionListener listener;

  public BaseSpawnOptionsPanel(String header, String prefix, ActionListener listener) {
    this.setLayout(new GridLayout(0, 1));
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    headerLabel = new JLabel(header);
    radioButtons = new ArrayList<>();
    settings = new ArrayList<>();
    buttonGroup = new ButtonGroup();

    this.header = header;
    this.prefix = prefix;
    this.listener = listener;
  }

  public void setHeaderLabel(String header) {
    this.headerLabel.setText(header);
  }

  public void setRadioLabels(GenericSpawnSettingsJson[] newSettings) {
    // Remove old radio buttons, if they exist
    for (int i = 0; i < radioButtons.size(); i++) {
      this.remove(radioButtons.get(i));
      buttonGroup.remove(radioButtons.get(i));
    }
    radioButtons.clear();

    for (int i = 0; i < newSettings.length; i++) {
      String id = String.format("%s_%s", prefix, i);
      JRadioButton btn = new JRadioButton(newSettings[i].getName());
      btn.addActionListener(listener);
      btn.setActionCommand(id);
      if (i == 0) {
        btn.setSelected(true);
      }
      buttonGroup.add(btn);
      radioButtons.add(btn);
      this.add(btn);
      settings.add(newSettings[i]);
    }
  }

  public GenericSpawnSettingsJson getSettingsForId(int index) {
    return settings.get(index);
  }
}
