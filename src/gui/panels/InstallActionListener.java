package gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.SettingsJson;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.NeuromodTreeRandomizer;
import randomizers.gameplay.filters.EnemyFilter;
import randomizers.gameplay.filters.FlowgraphFilter;
import randomizers.gameplay.filters.ItemSpawnFilter;
import randomizers.gameplay.filters.MorgansApartmentFilter;
import randomizers.gameplay.filters.OpenStationFilter;
import randomizers.gameplay.filters.StationConnectivityFilter;
import utils.Utils;

public class InstallActionListener implements ActionListener {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private JButton uninstallButton;
  private JLabel statusLabel;
  private Logger logger;

  // Used only for getting the current state of the GUI
  private TopPanel topPanel;
  private OptionsPanel optionsPanel;

  TaggedDatabase database;

  public InstallActionListener(JButton uninstallButton, JLabel statusLabel, TopPanel topPanel,
      OptionsPanel optionsPanel) {
    this.uninstallButton = uninstallButton;
    this.statusLabel = statusLabel;
    this.topPanel = topPanel;
    this.optionsPanel = optionsPanel;

    logger = Logger.getLogger("installer");
    database = EntityDatabase.getInstance();
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    uninstallButton.setEnabled(false);
    statusLabel.setText("Installing...");

    // Finalize settings
    SettingsJson currentSettings = null;
    try {
      currentSettings = MainPanel.getSettingsFromGui(topPanel, optionsPanel);
    } catch (Exception e) {
      statusLabel.setText("Failed to process input.");
      uninstallButton.setEnabled(true);
      e.printStackTrace();
      return;
    }

    // Log settings info
    try {
      logger.info("Current settings:");
      logger.info(new ObjectMapper().writeValueAsString(currentSettings));
    } catch (JsonProcessingException e1) {
      logger.warning("Failed to parse json settings!");
      e1.printStackTrace();
      uninstallButton.setEnabled(true);
    }

    Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
    Path tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
    Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
    Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);
    try {
      tempDir.toFile().mkdir();
      tempLevelDir.toFile().mkdir();
      tempPatchDir.toFile().mkdir();
      Optional<Installer> installer = initInstaller(currentSettings, tempDir, tempLevelDir, tempPatchDir);
      if (!installer.isPresent()) {
        return;
      }

      executeRandomization(currentSettings, tempDir, tempLevelDir, tempPatchDir);

      installer.get().install();
      statusLabel.setText("Done installing.");
    } catch (Exception e) {
      logger.severe(e.getMessage());
      statusLabel.setText("Install failed. See log for more info.");
      uninstallButton.setEnabled(true);
    } finally {
      if (tempDir.toFile().exists()) {
        Utils.deleteDirectory(tempDir.toFile());
      }
      uninstallButton.setEnabled(true);
    }
  }

  private Optional<Installer> initInstaller(SettingsJson currentSettings, Path tempDir, Path tempLevelDir,
      Path tempPatchDir) {
    Path installDir = Paths.get(currentSettings.getInstallDir());

    // Initialize install
    Installer installer = new Installer(installDir, tempDir, tempLevelDir, tempPatchDir, currentSettings);
    if (!installer.verifyDataExists()) {
      statusLabel.setText("Could not find data/ folder");
      return Optional.absent();
    }
    if (!installer.verifyInstallDir()) {
      statusLabel.setText("Prey install directory not valid.");
      return Optional.absent();
    }
    if (!installer.testInstall()) {
      statusLabel.setText("Unable to write to file. Is Prey running?");
      return Optional.absent();
    }
    return Optional.of(installer);
  }

  private void executeRandomization(SettingsJson currentSettings, Path tempDir, Path tempLevelDir, Path tempPatchDir) {
    /* COSMETIC */
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
      new BodyRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
      new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
    } else if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
    }

    try {
      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_LOOT)) {
        new LootTableRandomizer(database, currentSettings, tempPatchDir).randomize();
      } else {
        new LootTableRandomizer(database, currentSettings, tempPatchDir).copyFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    /* GAMEPLAY, LEVEL */
    LevelRandomizer levelRandomizer = new LevelRandomizer(currentSettings, tempLevelDir).addFilter(new ItemSpawnFilter(
        database, currentSettings))
        .addFilter(new FlowgraphFilter(database, currentSettings))
        .addFilter(new EnemyFilter(database, currentSettings));

    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.OPEN_STATION)) {
      levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
    }

    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.ADD_LOOT_TO_APARTMENT)) {
      levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
    }

    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_STATION)) {
      StationConnectivityFilter connectivity = new StationConnectivityFilter(currentSettings);
      try {
        connectivity.visualize();
      } catch (IOException e) {
        e.printStackTrace();
      }
      levelRandomizer = levelRandomizer.addFilter(connectivity);
    }

    levelRandomizer.randomize();
  }
}