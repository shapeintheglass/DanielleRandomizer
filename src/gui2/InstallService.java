package gui2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

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
import randomizers.cosmetic.MusicRandomizer;
import randomizers.cosmetic.PlayerModelRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.BookInfoHelper;
import randomizers.gameplay.BookInfoHelper.Book;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.NeuromodTreeRandomizer;
import randomizers.gameplay.NightmareHelper;
import randomizers.gameplay.filters.EnemyFilter;
import randomizers.gameplay.filters.FlowgraphFilter;
import randomizers.gameplay.filters.ItemSpawnFilter;
import randomizers.gameplay.filters.MorgansApartmentFilter;
import randomizers.gameplay.filters.OpenStationFilter;
import randomizers.gameplay.filters.StationConnectivityFilter;
import utils.Utils;

public class InstallService extends Service<Void> {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private TextArea outputWindow;
  private SettingsJson finalSettings;

  private Path tempDir;

  public Path getTempDir() {
    return tempDir;
  }

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
    doInstall(true);
  }

  public void doInstall(boolean cleanUp) {
    Date startTime = new Date();
    writeLine(Gui2Consts.INSTALL_STATUS_TEXT);

    Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
    tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
    Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
    Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);
    long secondsElapsed = 0L;
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
        writeLine(Gui2Consts.INSTALL_PROGRESS_WRITING);
        installer.get().install();
      } catch (IOException | InterruptedException e) {
        writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
        e.printStackTrace();
      }

      Date endTime = new Date();
      secondsElapsed = endTime.toInstant().getEpochSecond() - startTime.toInstant().getEpochSecond();
    } catch (Exception e) {
      e.printStackTrace();
      writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
    } finally {
      if (cleanUp) {
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
    writeLine(String.format(Gui2Consts.INSTALL_STATUS_COMPLETE_TEXT, secondsElapsed));
  }

  private Optional<Installer> initInstaller(SettingsJson currentSettings, Path tempDir, Path tempLevelDir,
      Path tempPatchDir) {
    Path installDir = Paths.get(currentSettings.getInstallDir());

    // Initialize install
    Installer installer = new Installer(installDir, tempDir, tempLevelDir, tempPatchDir, currentSettings);
    if (!installer.verifyDataExists()) {
      writeLine(Gui2Consts.INSTALL_ERROR_DATA_NOT_FOUND);
      return Optional.absent();
    }
    if (!installer.verifyInstallDir()) {
      writeLine(Gui2Consts.INSTALL_ERROR_INVALID_INSTALL_FOLDER);
      return Optional.absent();
    }
    if (!installer.testInstall()) {
      writeLine(Gui2Consts.INSTALL_ERROR_CANNOT_WRITE);
      return Optional.absent();
    }
    return Optional.of(installer);
  }

  private void executeRandomization(SettingsJson currentSettings, Path tempDir, Path tempLevelDir, Path tempPatchDir) {
    TaggedDatabase database = EntityDatabase.getInstance();

    /* COSMETIC */
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_BODIES);
      new BodyRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_VOICELINES);
      new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_MUSIC)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_MUSIC);
      new MusicRandomizer(currentSettings, tempPatchDir).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_PLAYER_MODEL)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_PLAYER_MODEL);
      new PlayerModelRandomizer(currentSettings, tempPatchDir).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_NEUROMOD);
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
    } else if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
    }

    try {
      writeLine(Gui2Consts.INSTALL_PROGRESS_LOOT);
      new LootTableRandomizer(database, currentSettings, tempPatchDir).randomize();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (currentSettings.getGameplaySettings().getRandomizeNightmare()) {
      writeLine("Randomizing nightmare...");
      NightmareHelper.install(database, currentSettings, tempPatchDir);
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
      String connectivityInfo = connectivity.toString();
      Book b = new Book("Bk_SL_Apt_Electronics", "Station Connectivity Debug Info", connectivityInfo);
      Map<String, Book> toOverwrite = Maps.newHashMap();
      toOverwrite.put("Bk_SL_Apt_Electronics", b);
      toOverwrite.put("Bk_TooFarTooFast1", b);
      BookInfoHelper bih = new BookInfoHelper(tempPatchDir);
      bih.installNewBooks(toOverwrite);

      try {
        connectivity.visualize();
      } catch (IOException e) {
        e.printStackTrace();
      }
      levelRandomizer = levelRandomizer.addFilter(connectivity);
    }

    writeLine(Gui2Consts.INSTALL_PROGRESS_LEVELS);
    levelRandomizer.randomize();
  }

  private void writeLine(String text) {
    if (outputWindow != null) {
      outputWindow.appendText(text + "\n");
    }
  }
}
