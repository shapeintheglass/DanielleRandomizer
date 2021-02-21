package gui2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
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
import randomizers.gameplay.SelfDestructTimerHelper;
import randomizers.gameplay.filters.EnemyFilter;
import randomizers.gameplay.filters.FlowgraphFilter;
import randomizers.gameplay.filters.GravityDisablerFilter;
import randomizers.gameplay.filters.ItemSpawnFilter;
import randomizers.gameplay.filters.MorgansApartmentFilter;
import randomizers.gameplay.filters.OpenStationFilter;
import randomizers.gameplay.filters.StationConnectivityFilter;
import utils.StationGenerator;
import utils.Utils;
import utils.ZipHelper;

/**
 * Uses given settings to execute randomization in the configured way, then creates and 
 * calls the installer to copy the files over.
 */
public class RandomizerService extends Service<Void> {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private static final ImmutableMap<String, String> MORE_GUNS_DEPENDENCIES = ImmutableMap.of(ZipHelper.ARK_PICKUPS_XML,
      "libs/entityarchetypes/arkpickups.xml", ZipHelper.ARK_PROJECTILES_XML, "libs/entityarchetypes/arkprojectiles.xml",
      ZipHelper.ARK_ITEMS_XML, "ark/items/arkitems.xml");

  private static final ImmutableMap<String, String> WANDERING_HUMANS_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.AI_TREE_ARMED_HUMANS, "ark/ai/aitrees/ArmedHumanAiTree.xml", ZipHelper.AI_TREE_HUMANS,
      "ark/ai/aitrees/HumanAiTree.xml", ZipHelper.AI_TREE_UNARMED_HUMANS, "ark/ai/aitrees/UnarmedHumanAiTree.xml");

  public static final ImmutableMap<String, String> SURVIVE_APEX_KILL_WALL_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.APEX_VOLUME_CONFIG, "ark/apexvolumeconfig.xml");

  public static final ImmutableMap<String, String> NPC_GAME_EFFECTS_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.NPC_GAME_EFFECTS, "ark/npc/npcgameeffects.xml");

  private TextArea outputWindow;
  private SettingsJson finalSettings;

  private Path tempDir;
  private ZipHelper zipHelper;
  private Logger logger;

  public RandomizerService(TextArea outputWindow, SettingsJson finalSettings) {
    this.outputWindow = outputWindow;
    this.finalSettings = finalSettings;
    logger = Logger.getLogger("RandomizerService");
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

  public boolean doInstall() {
    return doInstall(true);
  }

  public boolean doInstall(boolean deleteFilesAfterwards) {
    File dataPak = new File(ZipHelper.DATA_PAK);
    if (!dataPak.exists()) {
      writeLine("Unable to find required resource " + ZipHelper.DATA_PAK);
      return false;
    }

    Date startTime = new Date();
    writeLine(Gui2Consts.INSTALL_STATUS_TEXT);

    Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
    tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
    Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
    Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);
    long secondsElapsed = 0L;
    try {
      try {
        this.zipHelper = new ZipHelper(tempLevelDir, tempPatchDir);
      } catch (IOException e) {
        e.printStackTrace();
        writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
        return false;
      }

      tempDir.toFile().mkdir();
      tempLevelDir.toFile().mkdir();
      tempPatchDir.toFile().mkdir();
      Optional<Installer> installer = initInstaller(finalSettings, tempDir, tempLevelDir, tempPatchDir, zipHelper);
      if (!installer.isPresent()) {
        writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
        return false;
      }

      // Copy over dependencies files for certain settings
      copyDependencies(finalSettings, tempPatchDir);

      executeRandomization(finalSettings, tempDir, tempLevelDir, tempPatchDir);
      zipHelper.closeOutputZips();

      try {
        writeLine(Gui2Consts.INSTALL_PROGRESS_WRITING);
        installer.get().install();
      } catch (IOException | InterruptedException e) {
        writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
      writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
    } finally {
      Date endTime = new Date();
      secondsElapsed = endTime.toInstant().getEpochSecond() - startTime.toInstant().getEpochSecond();
      writeLine(String.format(Gui2Consts.INSTALL_STATUS_COMPLETE_TEXT, secondsElapsed));
      zipHelper.close();
      if (deleteFilesAfterwards) {
        if (tempDir.toFile().exists()) {
          Utils.deleteDirectory(tempDir.toFile());
        }
      }
    }
    return true;
  }

  public Path getTempDir() {
    return tempDir;
  }

  private Optional<Installer> initInstaller(SettingsJson currentSettings, Path tempDir, Path tempLevelDir,
      Path tempPatchDir, ZipHelper zipHelper) {
    Path installDir = Paths.get(currentSettings.getInstallDir());

    // Initialize install
    Installer installer = new Installer(installDir, tempLevelDir, tempPatchDir, currentSettings);
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
    TaggedDatabase database = EntityDatabase.getInstance(zipHelper);
    if (database == null) {
      return;
    }

    writeLine(currentSettings.toString());

    /* COSMETIC */
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_BODIES);
      new BodyRandomizer(currentSettings, zipHelper).randomize();
    }
    Map<String, String> swappedLinesMap = null;
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_VOICELINES);
      VoiceRandomizer vr = new VoiceRandomizer(currentSettings, tempPatchDir, zipHelper);
      vr.randomize();
      swappedLinesMap = vr.getSwappedLinesMap();

    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_MUSIC)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_MUSIC);
      new MusicRandomizer(currentSettings, zipHelper).randomize();
    }
    if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_PLAYER_MODEL)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_PLAYER_MODEL);
      new PlayerModelRandomizer(currentSettings, zipHelper).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_NEUROMOD);
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir, zipHelper).randomize();
    } else if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
      new NeuromodTreeRandomizer(currentSettings, tempPatchDir, zipHelper).unlockAllScans();
    }

    try {
      writeLine(Gui2Consts.INSTALL_PROGRESS_LOOT);
      new LootTableRandomizer(database, currentSettings, tempPatchDir, zipHelper).randomize();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (currentSettings.getGameplaySettings().getRandomizeNightmare()) {
      writeLine("Randomizing nightmare...");
      NightmareHelper.install(database, currentSettings, zipHelper);
    }

    if (currentSettings.getGameplaySettings().getStartSelfDestruct()) {
      writeLine("Updating self-destruct timer...");
      new SelfDestructTimerHelper(currentSettings, zipHelper).randomize();
    }

    /* GAMEPLAY, LEVEL */
    LevelRandomizer levelRandomizer = new LevelRandomizer(currentSettings, zipHelper, swappedLinesMap).addFilter(
        new ItemSpawnFilter(database, currentSettings))
        .addFilter(new FlowgraphFilter(database, currentSettings))
        .addFilter(new EnemyFilter(database, currentSettings));

    if (currentSettings.getGameplaySettings().getOpenStation()) {
      levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
    }

    if (currentSettings.getGameplaySettings().getAddLootToApartment()) {
      levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
    }

    if (currentSettings.getGameplaySettings().getRandomizeStation()) {
      StationGenerator stationGenerator = new StationGenerator(currentSettings.getSeed());
      StationConnectivityFilter connectivity = new StationConnectivityFilter(stationGenerator.getDoorConnectivity(),
          stationGenerator.getSpawnConnectivity());
      String connectivityInfo = stationGenerator.toString();
      Book b = new Book("Bk_SL_Apt_Electronics", "Station Connectivity Debug Info", connectivityInfo);
      Map<String, Book> toOverwrite = Maps.newHashMap();
      toOverwrite.put("Bk_SL_Apt_Electronics", b);
      toOverwrite.put("Bk_TooFarTooFast1", b);
      BookInfoHelper bih = new BookInfoHelper(zipHelper);
      bih.installNewBooks(toOverwrite);
      levelRandomizer = levelRandomizer.addFilter(connectivity);
    }
    
    if (currentSettings.getGameplaySettings().getEnableGravity()) {
      levelRandomizer = levelRandomizer.addFilter(new GravityDisablerFilter());
    }

    writeLine(Gui2Consts.INSTALL_PROGRESS_LEVELS);
    levelRandomizer.randomize();
    writeLine("Done processing level files.");
  }

  private void copyDependencies(SettingsJson settings, Path tempPatchDir) throws IOException {
    copyFiles(NPC_GAME_EFFECTS_DEPENDENCIES, tempPatchDir);

    if (settings.getGameplaySettings().getMoreGuns()) {
      copyFiles(MORE_GUNS_DEPENDENCIES, tempPatchDir);
    }

    if (settings.getGameplaySettings().getWanderingHumans()) {
      copyFiles(WANDERING_HUMANS_DEPENDENCIES, tempPatchDir);
    }

    if (settings.getGameplaySettings().getRandomizeStation() || settings.getGameplaySettings().getStartSelfDestruct()) {
      copyFiles(SURVIVE_APEX_KILL_WALL_DEPENDENCIES, tempPatchDir);
    }
  }

  private void copyFiles(ImmutableMap<String, String> dependencies, Path tempPatchDir) throws IOException {
    for (String key : dependencies.keySet()) {
      try {
        zipHelper.copyToPatch(key, dependencies.get(key));
      } catch (IOException e) {
        logger.warning(String.format("Unable to copy dependency file %s", key));
        e.printStackTrace();
      }
    }
  }

  private void writeLine(String text) {
    if (outputWindow != null) {
      outputWindow.appendText(text + "\n");
    }
  }

}
