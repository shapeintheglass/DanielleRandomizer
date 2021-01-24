package gui.panels;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.RandomizerGui;
import json.AllPresetsJson;
import json.SettingsJson;

public class MainPanel extends JPanel {

  private static final int BORDER_SIZE = 10;

  private static final long serialVersionUID = 7726564274787491220L;

  private TopPanel topPanel;
  private OptionsPanel optionsPanel;
  private ButtonsPanel buttonsPanel;

  public MainPanel(SettingsJson initialSettings, AllPresetsJson allPresets) {
    this.topPanel = new TopPanel(initialSettings.getInstallDir(), initialSettings.getSeed());
    this.optionsPanel = new OptionsPanel(allPresets, initialSettings.getGameplaySettings(), initialSettings
        .getCosmeticSettings());
    this.buttonsPanel = new ButtonsPanel(topPanel, optionsPanel);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    this.add(topPanel);
    this.add(Box.createRigidArea(new Dimension(0, BORDER_SIZE)));
    this.add(optionsPanel);
    this.add(Box.createRigidArea(new Dimension(0, BORDER_SIZE)));
    this.add(buttonsPanel);
  }

  // Generate a new settings based off of existing UI
  public static SettingsJson getSettingsFromGui(TopPanel topPanel, OptionsPanel optionsPanel) {
    return new SettingsJson(RandomizerGui.RELEASE_VER, topPanel.installDirPanel.getInstallDir(), topPanel.seedPanel
        .getSeed(), optionsPanel.getCosmeticSettings(), optionsPanel.getGameplaySettings());
  }
}
