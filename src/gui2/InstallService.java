package gui2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.google.common.base.Optional;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
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

public class InstallService extends Service<Void> {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private TextArea outputWindow;
  private SettingsJson finalSettings;

  public InstallService(TextArea outputWindow, SettingsJson finalSettings) {
    this.outputWindow = outputWindow;
    this.finalSettings = finalSettings;
  }
  
  @Override
  protected Task<Void> createTask() {
    return new Task<Void>() {

      @Override
      protected Void call() throws Exception {
        doInstall();
        return null;
      }
      
    };
  }

  public void doInstall() {
    Date startTime = new Date();
    outputWindow.appendText(Gui2Consts.INSTALL_STATUS_TEXT + "\n");

    Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
    Path tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
    Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
    Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);
    try {
      tempDir.toFile().mkdir();
      tempLevelDir.toFile().mkdir();
      tempPatchDir.toFile().mkdir();
      Optional<Installer> installer = initInstaller(finalSettings, tempDir, tempLevelDir, tempPatchDir);
      if (!installer.isPresent()) {
        return;
      }

      executeRandomization(finalSettings, tempDir, tempLevelDir, tempPatchDir);

      try {
        outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_WRITING + "\n");
        installer.get().install();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      Date endTime = new Date();
      long secondsElapsed = endTime.toInstant().getEpochSecond() - startTime.toInstant().getEpochSecond();
      outputWindow.appendText(String.format(Gui2Consts.INSTALL_STATUS_COMPLETE_TEXT + "\n", secondsElapsed));
    } catch (Exception e) {
      e.printStackTrace();
      outputWindow.appendText(Gui2Consts.INSTALL_STATUS_FAILED_TEXT + "\n");
    } finally {
      if (tempLevelDir.toFile().exists()) {
        Utils.deleteDirectory(tempLevelDir.toFile());
      }
      if (tempPatchDir.toFile().exists()) {
        Utils.deleteDirectory(tempPatchDir.toFile());
      }
      if (tempDir.toFile().exists()) {
        Utils.deleteDirectory(tempDir.toFile());
      }
    }
  }

  private Optional<Installer> initInstaller(SettingsJson currentSettings, Path tempDir, Path tempLevelDir,
      Path tempPatchDir) {
    Path installDir = Paths.get(currentSettings.getInstallDir());

    // Initialize install
    Installer installer = new Installer(installDir, tempDir, tempLevelDir, tempPatchDir, currentSettings);
    if (!installer.verifyDataExists()) {
      outputWindow.appendText(Gui2Consts.INSTALL_ERROR_DATA_NOT_FOUND + "\n");
      return Optional.absent();
    }
    if (!installer.verifyInstallDir()) {
      outputWindow.appendText(Gui2Consts.INSTALL_ERROR_INVALID_INSTALL_FOLDER + "\n");
      return Optional.absent();
    }
    if (!installer.testInstall()) {
      outputWindow.appendText(Gui2Consts.INSTALL_ERROR_CANNOT_WRITE + "\n");
      return Optional.absent();
    }
    return Optional.of(installer);
  }

  private void executeRandomization(SettingsJson currentSettings, Path tempDir, Path tempLevelDir, Path tempPatchDir) {
    TaggedDatabase database = EntityDatabase.getInstance();

    /* COSMETIC */
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
      outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_BODIES + "\n");
      new BodyRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
      outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_VOICELINES + "\n");
      new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
      outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_NEUROMOD + "\n");
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
    } else if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
    }

    try {
      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_LOOT)) {
        outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_LOOT + "\n");
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

    outputWindow.appendText(Gui2Consts.INSTALL_PROGRESS_LEVELS + "\n");
    levelRandomizer.randomize();
  }
}
