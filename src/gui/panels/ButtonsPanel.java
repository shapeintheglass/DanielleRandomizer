package gui.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;

import gui.Consts;
import gui.InstallActionListener;
import gui.RandomizerGui;
import installers.Installer;
import json.SettingsJson;
import utils.FileConsts;

public class ButtonsPanel extends JPanel {

  private static final long serialVersionUID = 8216223745201426275L;

  private JLabel statusLabel;
  private InstallButton installButton;

  public ButtonsPanel(TopPanel topPanel, OptionsPanel optionsPanel) {
    statusLabel = new JLabel("", JLabel.LEFT);
    SaveButton saveButton = new SaveButton(topPanel, optionsPanel, statusLabel);
    RunButton runButton = new RunButton(topPanel, statusLabel);
    UninstallButton uninstallButton = new UninstallButton(topPanel);
    CloseButton closeButton = new CloseButton();

    List<JButton> toDisable = Lists.newArrayList(saveButton, runButton, uninstallButton, closeButton);

    installButton = new InstallButton(toDisable, statusLabel, topPanel, optionsPanel);

    this.setLayout(new FlowLayout(FlowLayout.RIGHT));

    this.add(statusLabel);
    // this.add(runButton);
    this.add(saveButton);
    this.add(installButton);
    this.add(uninstallButton);
    this.add(closeButton);
  }

  public static class RunButton extends JButton {
    private static final long serialVersionUID = 5484007977067147208L;

    // Used for getting the current state of the GUI
    private TopPanel topPanelInner;

    private JLabel statusLabelInner;

    public RunButton(TopPanel topPanel, JLabel statusLabel) {
      super(Consts.RUN_BUTTON_LABEL);

      this.topPanelInner = topPanel;
      this.statusLabelInner = statusLabel;

      this.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          try {
            Path binaryPath = Paths.get(topPanelInner.installDirPanel.getInstallDir()).resolve(FileConsts.BINARY_PATH);
            Runtime.getRuntime().exec(binaryPath.toString());
            statusLabelInner.setText(binaryPath.toString());
          } catch (Exception e) {
            e.printStackTrace();
            statusLabelInner.setText(Consts.ERROR_COULD_NOT_RUN_PREY);
          }
        }
      });
      this.setToolTipText(Consts.RUN_BUTTON_TTT);
    }
  }

  public static class SaveButton extends JButton {
    private static final long serialVersionUID = -4162434755236353522L;

    // Used for getting the current state of the GUI
    private TopPanel topPanelInner;
    private OptionsPanel optionsPanelInner;
    private JLabel statusLabelInner;

    public SaveButton(TopPanel topPanel, OptionsPanel optionsPanel, JLabel statusLabel) {
      super(Consts.SAVE_BUTTON_LABEL);

      this.topPanelInner = topPanel;
      this.optionsPanelInner = optionsPanel;
      this.statusLabelInner = statusLabel;

      this.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          try {
            SettingsJson toSave = MainPanel.getSettingsFromGui(topPanelInner, optionsPanelInner);
            writeLastUsedSettingsToFile(RandomizerGui.SAVED_SETTINGS_FILE, toSave);
            statusLabelInner.setText(String.format(Consts.SAVE_STATUS_COMPLETE, RandomizerGui.SAVED_SETTINGS_FILE));
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

    public InstallButton(List<JButton> toDisable, JLabel statusLabel, TopPanel topPanel, OptionsPanel optionsPanel) {
      super(Consts.INSTALL_BUTTON_LABEL);
      toDisable.add(this);
      this.addActionListener(new InstallActionListener(toDisable, statusLabel, topPanel, optionsPanel));
      this.setToolTipText(Consts.INSTALL_BUTTON_TTT);
    }
  }

  public class UninstallButton extends JButton {
    private static final long serialVersionUID = 4288469916977513838L;

    public UninstallButton(TopPanel topPanel) {
      super(Consts.UNINSTALL_BUTTON_LABEL);
      this.addActionListener(new OnUninstallClick(topPanel));
      this.setToolTipText(Consts.UNINSTALL_BUTTON_TTT);
    }
  }

  private class OnUninstallClick implements ActionListener {
    TopPanel topPanel;

    public OnUninstallClick(TopPanel topPanel) {
      this.topPanel = topPanel;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      statusLabel.setText(Consts.UNINSTALLING_STATUS_TEXT);
      installButton.setEnabled(false);

      Installer.uninstall(Paths.get(topPanel.installDirPanel.getInstallDir()), Logger.getGlobal());
      statusLabel.setText(Consts.UNINSTALLING_STATUS_COMPLETE_TEXT);
      installButton.setEnabled(true);
    }
  }

  public class CloseButton extends JButton {
    private static final long serialVersionUID = -1455745998460122616L;

    public CloseButton() {
      super(Consts.CLOSE_BUTTON_LABEL);
      this.addActionListener(new OnCloseClick());
      this.setToolTipText(Consts.CLOSE_BUTTON_TTT);
    }
  }

  private class OnCloseClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  }
}
