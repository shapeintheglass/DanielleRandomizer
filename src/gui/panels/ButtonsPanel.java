package gui.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import gui.Consts;
import gui.RandomizerGui;
import installers.Installer;
import json.SettingsJson;

public class ButtonsPanel extends JPanel {

  private static final long serialVersionUID = 8216223745201426275L;

  private JLabel statusLabel;
  private InstallButton installButton;

  public ButtonsPanel(TopPanel topPanel, OptionsPanel optionsPanel) {
    JButton uninstallButton = new UninstallButton(topPanel);
    statusLabel = new JLabel("", JLabel.LEFT);
    installButton = new InstallButton(topPanel, optionsPanel, uninstallButton);
    

    this.setLayout(new FlowLayout(FlowLayout.RIGHT));
    this.add(statusLabel);
    this.add(new SaveButton(topPanel, optionsPanel));
    this.add(installButton);
    this.add(uninstallButton);
    this.add(new CloseButton());
  }

  public static class SaveButton extends JButton {
    private static final long serialVersionUID = -4162434755236353522L;

    // Used for getting the current state of the GUI
    private TopPanel topPanelInner;
    private OptionsPanel optionsPanelInner;

    public SaveButton(TopPanel topPanel, OptionsPanel optionsPanel) {
      super(Consts.SAVE_BUTTON_LABEL);

      this.topPanelInner = topPanel;
      this.optionsPanelInner = optionsPanel;

      this.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          try {
            SettingsJson toSave = MainPanel.getSettingsFromGui(topPanelInner, optionsPanelInner);
            writeLastUsedSettingsToFile(RandomizerGui.SAVED_SETTINGS_FILE, toSave);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      this.setToolTipText(Consts.SAVE_BUTTON_TTT);
    }

    private void writeLastUsedSettingsToFile(String savedSettingsFilePath, SettingsJson toSave)
        throws JsonGenerationException, JsonMappingException, IOException {
      File savedSettingsFile = new File(savedSettingsFilePath);
      savedSettingsFile.createNewFile();
      ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writerFor(SettingsJson.class).writeValue(savedSettingsFile, toSave);
    }
  }

  public class InstallButton extends JButton {
    private static final long serialVersionUID = 4475030086297300751L;

    public InstallButton(TopPanel topPanel, OptionsPanel optionsPanel, JButton uninstallButton) {
      super("Install");

      this.setActionCommand("install");
      this.addActionListener(new InstallActionListener(uninstallButton, statusLabel, topPanel, optionsPanel));
      this.setToolTipText("Randomizes according to above settings and installs in game directory");
    }

  }

  public class UninstallButton extends JButton {
    private static final long serialVersionUID = 4288469916977513838L;

    public UninstallButton(TopPanel topPanel) {
      super("Uninstall");
      this.setActionCommand("uninstall");
      this.addActionListener(new OnUninstallClick(topPanel));
      this.setToolTipText("Removes any mods added by this randomizer, restoring game files to previous state");
    }
  }

  private class OnUninstallClick implements ActionListener {
    TopPanel topPanel;

    public OnUninstallClick(TopPanel topPanel) {
      this.topPanel = topPanel;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      statusLabel.setText("Uninstalling...");
      installButton.setEnabled(false);

      Installer.uninstall(Paths.get(topPanel.installDirPanel.getInstallDir()), Logger.getGlobal());
      statusLabel.setText("Done uninstalling.");
      installButton.setEnabled(true);
    }
  }

  public class CloseButton extends JButton {
    private static final long serialVersionUID = -1455745998460122616L;

    public CloseButton() {
      super("Close");
      this.setActionCommand("close");
      this.addActionListener(new OnCloseClick());
      this.setToolTipText("Closes this GUI");
    }
  }

  private class OnCloseClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  }
}
