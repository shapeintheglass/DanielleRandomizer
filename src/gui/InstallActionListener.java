package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import gui.panels.OptionsPanel;
import gui.panels.TopPanel;

public class InstallActionListener implements ActionListener {

  private List<JButton> toDisable;
  private JLabel statusLabel;
  private TopPanel topPanel;
  private OptionsPanel optionsPanel;

  public InstallActionListener(List<JButton> toDisable, JLabel statusLabel, TopPanel topPanel,
      OptionsPanel optionsPanel) {
    this.toDisable = toDisable;
    this.statusLabel = statusLabel;
    this.topPanel = topPanel;
    this.optionsPanel = optionsPanel;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    new InstallWorker(toDisable, statusLabel, topPanel, optionsPanel).execute();
  }

}