package gui;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import gui.panels.MainPanel;
import gui.panels.OptionsPanel;
import gui.panels.TopPanel;
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
import randomizers.gameplay.filters.StartOutsideApartmentFilter;
import randomizers.gameplay.filters.StationConnectivityFilter;
import utils.Utils;

public class InstallWorker extends SwingWorker<Void, Integer> {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private Path tempDir;

  private List<JButton> toDisable;
  private JLabel statusLabel;

  private Logger logger;

  // Used only for getting the current state of the GUI
  private TopPanel topPanel;
  private OptionsPanel optionsPanel;

  public InstallWorker(List<JButton> toDisable, JLabel statusLabel, TopPanel topPanel, OptionsPanel optionsPanel) {
    this.toDisable = toDisable;
    this.statusLabel = statusLabel;
    this.topPanel = topPanel;
    this.optionsPanel = optionsPanel;

    this.logger = Logger.getLogger("installworker");
  }

  @Override
  protected Void doInBackground() throws Exception {
    Date startTime = new Date();
    statusLabel.setText(Consts.INSTALL_STATUS_TEXT);
    enableButtons(toDisable, false);

    // Finalize settings
    SettingsJson currentSettings = null;
    try {
      currentSettings = MainPanel.getSettingsFromGui(topPanel, optionsPanel);
    } catch (Exception e) {
      statusLabel.setText(Consts.ERROR_COULD_NOT_PARSE_GUI);
      enableButtons(toDisable, true);
      e.printStackTrace();
      return null;
    }

    // Log settings info
    try {
      logger.info("Current settings:");
      logger.info(new ObjectMapper().writeValueAsString(currentSettings));
    } catch (JsonProcessingException e1) {
      logger.warning(Consts.ERROR_COULD_NOT_PARSE_JSON);
      e1.printStackTrace();
      enableButtons(toDisable, true);
    }

    Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
    tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
    Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
    Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);
    try {
      tempDir.toFile().mkdir();
      tempLevelDir.toFile().mkdir();
      tempPatchDir.toFile().mkdir();
      Optional<Installer> installer = initInstaller(currentSettings, tempDir, tempLevelDir, tempPatchDir);
      if (!installer.isPresent()) {
        return null;
      }

      executeRandomization(currentSettings, tempDir, tempLevelDir, tempPatchDir);

      try {
        statusLabel.setText(Consts.INSTALL_PROGRESS_WRITING);
        installer.get().install();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      Date endTime = new Date();
      long secondsElapsed = endTime.toInstant().getEpochSecond() - startTime.toInstant().getEpochSecond();
      statusLabel.setText(String.format(Consts.INSTALL_STATUS_COMPLETE_TEXT, secondsElapsed));

    } catch (Exception e) {
      e.printStackTrace();
      statusLabel.setText(Consts.INSTALL_STATUS_FAILED_TEXT);
    } finally {
      enableButtons(toDisable, true);
      if (tempDir.toFile().exists()) {
        Utils.deleteDirectory(tempDir.toFile());
      }
    }
    return null;
  }

  private static void enableButtons(List<JButton> buttons, boolean enable) {
    for (JButton b : buttons) {
      b.setEnabled(enable);
    }
  }

  private Optional<Installer> initInstaller(SettingsJson currentSettings, Path tempDir, Path tempLevelDir,
      Path tempPatchDir) {
    Path installDir = Paths.get(currentSettings.getInstallDir());

    // Initialize install
    Installer installer = new Installer(installDir, tempDir, tempLevelDir, tempPatchDir, currentSettings);
    if (!installer.verifyDataExists()) {
      statusLabel.setText(Consts.INSTALL_ERROR_DATA_NOT_FOUND);
      return Optional.absent();
    }
    if (!installer.verifyInstallDir()) {
      statusLabel.setText(Consts.INSTALL_ERROR_INVALID_INSTALL_FOLDER);
      return Optional.absent();
    }
    if (!installer.testInstall()) {
      statusLabel.setText(Consts.INSTALL_ERROR_CANNOT_WRITE);
      return Optional.absent();
    }
    return Optional.of(installer);
  }

  private void executeRandomization(SettingsJson currentSettings, Path tempDir, Path tempLevelDir, Path tempPatchDir) {
    TaggedDatabase database = EntityDatabase.getInstance();

    /* COSMETIC */
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
      statusLabel.setText(Consts.INSTALL_PROGRESS_BODIES);
      new BodyRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
      statusLabel.setText(Consts.INSTALL_PROGRESS_VOICELINES);
      new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
      statusLabel.setText(Consts.INSTALL_PROGRESS_NEUROMOD);
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
    } else if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
    }

    try {
      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_LOOT)) {
        statusLabel.setText(Consts.INSTALL_PROGRESS_LOOT);
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

    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.START_OUTSIDE_LOBBY)) {
      levelRandomizer = levelRandomizer.addFilter(new StartOutsideApartmentFilter());
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

    statusLabel.setText(Consts.INSTALL_PROGRESS_LEVELS);
    levelRandomizer.randomize();
  }
}
