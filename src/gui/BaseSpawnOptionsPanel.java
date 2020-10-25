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

import json.NameAndDescription;

public class BaseSpawnOptionsPanel<T extends NameAndDescription> extends JPanel {

  public static final String DELIMITER = ";";

  private static final long serialVersionUID = -1427304877583197708L;

  private JLabel headerLabel;
  private List<JRadioButton> radioButtons;
  private List<T> settings;
  private ButtonGroup buttonGroup;

  String header;
  String prefix;
  private ActionListener listener;
  
  private int currentIndex;

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
    this.add(headerLabel);
    currentIndex = 0;
  }
  
  public int getCurrentIndex() {
    return currentIndex;
  }
  
  public void setCurrentIndex(int newIndex) {
    radioButtons.get(currentIndex).setSelected(false);
    radioButtons.get(newIndex).setSelected(true);
    this.currentIndex = newIndex;
    
  }

  public void setHeaderLabel(String header) {
    this.headerLabel.setText(header);
  }

  public void setRadioLabels(List<T> newSettings, String selected) {
    settings = new ArrayList<>();
    // Remove old radio buttons, if they exist
    for (int i = 0; i < radioButtons.size(); i++) {
      this.remove(radioButtons.get(i));
      buttonGroup.remove(radioButtons.get(i));
    }
    radioButtons.clear();

    for (int i = 0; i < newSettings.size(); i++) {
      String id = String.format("%s%s%s", prefix, DELIMITER, newSettings.get(i).getName());
      JRadioButton btn = new JRadioButton(newSettings.get(i).getName());
      btn.addActionListener(listener);
      btn.setActionCommand(id);
      btn.setToolTipText(newSettings.get(i).getDesc());
      if (selected != null && newSettings.get(i).getName()
                                            .equals(selected)) {
        btn.setSelected(true);
      }
      buttonGroup.add(btn);
      radioButtons.add(btn);
      this.add(btn);
      settings.add(newSettings.get(i));
    }
  }

  public T getSettingsByName(String name) {
    for (T t : settings) {
      if (t.getName()
           .equals(name)) {
        return t;
      }
    }
    return null;
  }
}
