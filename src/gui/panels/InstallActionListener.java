package gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import gui.InstallWorker;

public class InstallActionListener implements ActionListener {

  private JButton installButton;
  private JButton uninstallButton;
  private JLabel statusLabel;
  private TopPanel topPanel;
  private OptionsPanel optionsPanel;

  public InstallActionListener(JButton installButton, JButton uninstallButton, JLabel statusLabel, TopPanel topPanel,
      OptionsPanel optionsPanel) {
    this.installButton = installButton;
    this.uninstallButton = uninstallButton;
    this.statusLabel = statusLabel;
    this.topPanel = topPanel;
    this.optionsPanel = optionsPanel;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    new InstallWorker(installButton, uninstallButton, statusLabel, topPanel, optionsPanel).execute();
  }

}