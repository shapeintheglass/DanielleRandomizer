package gui2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import proto.RandomizerSettings.Settings;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.MusicRandomizer;
import randomizers.cosmetic.PlanetRandomizer;
import randomizers.cosmetic.PlayerModelRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.EntityArchetypesRandomizer;
import randomizers.gameplay.FabPlanCostRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.NeuromodTreeRandomizer;
import randomizers.gameplay.NightmareRandomizer;
import randomizers.gameplay.NpcAbilitiesRandomizer;
import randomizers.gameplay.PreorderLockerRandomizer;
import randomizers.gameplay.filters.BrokenObjectFilter;
import randomizers.gameplay.filters.EnemyFilter;
import randomizers.gameplay.filters.FlowgraphFilter;
import randomizers.gameplay.filters.FruitTreeFilter;
import randomizers.gameplay.filters.GravityDisablerFilter;
import randomizers.gameplay.filters.ItemSpawnFilter;
import randomizers.gameplay.filters.LivingCorpseFilter;
import randomizers.gameplay.filters.LockedObjectFilter;
import randomizers.gameplay.filters.MorgansApartmentFilter;
import randomizers.gameplay.filters.OpenStationFilter;
import randomizers.gameplay.filters.OperatorDispenserFilter;
import randomizers.gameplay.filters.RecyclerFilter;
import randomizers.gameplay.filters.StationConnectivityFilter;
import randomizers.generators.BookInfoHelper;
import randomizers.generators.BookInfoHelper.Book;
import randomizers.generators.CustomSpawnGenerator;
import randomizers.generators.OptionsMenuGenerator;
import randomizers.generators.SelfDestructTimerHelper;
import randomizers.generators.StationGenerator;
import utils.Utils;
import utils.ZipHelper;

/**
 * Uses given settings to execute randomization in the configured way, then creates and calls the
 * installer to copy the files over.
 */
public class RandomizerService extends Service<Void> {

  private static final String TEMP_FOLDER_SUFFIX = "deleteme";
  private static final String TEMP_PATCH_DIR_NAME = "patch";
  private static final String TEMP_LEVEL_DIR_NAME = "level";
  private static final String DEFAULT_WORKING_DIR = ".";

  private static final String LOGO = "libs/ui/textures/danielle_shared_textures/danielle_title.dds";
  private static final ImmutableMap<String, String> MORE_GUNS_DEPENDENCIES = ImmutableMap.of(ZipHelper.ARK_PICKUPS_XML,
      "libs/entityarchetypes/arkpickups.xml", ZipHelper.ARK_PROJECTILES_XML, "libs/entityarchetypes/arkprojectiles.xml",
      ZipHelper.ARK_ITEMS_XML, "ark/items/arkitems.xml");
  private static final ImmutableList<String> MORE_GUNS_MATERIALS = ImmutableList.of(
      "objects/weapons/shotgun/1p/shotgun1p_phantom01.mtl", "objects/weapons/shotgun/3p/shotgun3p_phantom01.mtl",
      "objects/weapons/shotgun/1p/shotgun1p_telepath01.mtl", "objects/weapons/shotgun/3p/shotgun3p_telepath01.mtl",
      "objects/weapons/shotgun/1p/shotgun1p_nightmare01.mtl", "objects/weapons/shotgun/3p/shotgun3p_nightmare01.mtl",
      "objects/weapons/shotgun/1p/shotgun1p_voltaic01.mtl", "objects/weapons/shotgun/3p/shotgun3p_voltaic01.mtl",
      "objects/weapons/googun/1p/googun1p_phantom01.mtl", "objects/weapons/googun/3p/googun3p_phantom01.mtl",
      "objects/weapons/googun/1p/googun1p_telepath01.mtl", "objects/weapons/googun/3p/googun3p_telepath01.mtl",
      "objects/weapons/googun/1p/googun1p_nightmare01.mtl", "objects/weapons/googun/3p/googun3p_nightmare01.mtl",
      "objects/weapons/googun/1p/googun1p_voltaic01.mtl", "objects/weapons/googun/3p/googun3p_voltaic01.mtl",
      "objects/weapons/pistol/1p/pistol1p_phantom01.mtl", "objects/weapons/pistol/3p/pistol3p_phantom01.mtl",
      "objects/weapons/pistol/1p/pistol1p_telepath01.mtl", "objects/weapons/pistol/3p/pistol3p_telepath01.mtl",
      "objects/weapons/pistol/1p/pistol1p_nightmare01.mtl", "objects/weapons/pistol/3p/pistol3p_nightmare01.mtl",
      "objects/weapons/pistol/1p/pistol1p_voltaic01.mtl", "objects/weapons/pistol/3p/pistol3p_voltaic01.mtl",
      "objects/weapons/toygun/1p/toygun1p_phantom01.mtl", "objects/weapons/toygun/3p/toygun3p_phantom01.mtl",
      "objects/weapons/toygun/1p/toygun1p_telepath01.mtl", "objects/weapons/toygun/3p/toygun3p_telepath01.mtl",
      "objects/weapons/toygun/1p/toygun1p_nightmare01.mtl", "objects/weapons/toygun/3p/toygun3p_nightmare01.mtl",
      "objects/weapons/toygun/1p/toygun1p_voltaic01.mtl", "objects/weapons/toygun/3p/toygun3p_voltaic01.mtl");
  private static final String MORE_GUNS_FILE_LIST = "libs/ui/textures/icons_inventory/filelist.txt";
  private static final String MORE_GUNS_ICONS_DIR = "libs/ui/textures/icons_inventory/";

  private static final ImmutableMap<String, String> WANDERING_HUMANS_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.AI_TREE_ARMED_HUMANS, "ark/ai/aitrees/ArmedHumanAiTree.xml", ZipHelper.AI_TREE_HUMANS,
      "ark/ai/aitrees/HumanAiTree.xml", ZipHelper.AI_TREE_UNARMED_HUMANS, "ark/ai/aitrees/UnarmedHumanAiTree.xml");

  public static final ImmutableMap<String, String> SURVIVE_APEX_KILL_WALL_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.APEX_VOLUME_CONFIG, "ark/apexvolumeconfig.xml");

  public static final ImmutableMap<String, String> NPC_GAME_EFFECTS_DEPENDENCIES = ImmutableMap.of(
      ZipHelper.NPC_GAME_EFFECTS, "ark/npc/npcgameeffects.xml");

  private TextArea outputWindow;
  private Settings finalSettings;

  private Path tempDir;
  private ZipHelper zipHelper;
  private Logger logger;

  public RandomizerService(TextArea outputWindow, Settings finalSettings) {
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
      if (!sanityChecks(finalSettings, tempPatchDir)) {
        writeLine(Gui2Consts.INSTALL_STATUS_FAILED_TEXT);
        return false;
      }

      String spawnLocation = executeRandomization(finalSettings, tempDir, tempLevelDir, tempPatchDir);

      // Copy over dependencies files for certain settings
      copyDependencies(finalSettings, tempPatchDir);

      zipHelper.closeOutputZips();

      try {
        writeLine(Gui2Consts.INSTALL_PROGRESS_WRITING);
        Path installDir = Paths.get(finalSettings.getInstallDir());
        Installer installer = new Installer(installDir, tempLevelDir, tempPatchDir, spawnLocation);
        installer.install();
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

  private boolean sanityChecks(Settings currentSettings, Path tempPatchDir) {
    Path installDir = Paths.get(currentSettings.getInstallDir());
    if (!Installer.verifyDataExists(logger)) {
      writeLine(Gui2Consts.INSTALL_ERROR_DATA_NOT_FOUND);
      return false;
    }
    if (!Installer.verifyInstallDir(logger, installDir)) {
      writeLine(Gui2Consts.INSTALL_ERROR_INVALID_INSTALL_FOLDER);
      return false;
    }
    if (!Installer.testInstall(logger, tempPatchDir.resolve(Installer.PATCH_NAME).toFile())) {
      writeLine(Gui2Consts.INSTALL_ERROR_CANNOT_WRITE);
      return false;
    }
    return true;
  }

  private String executeRandomization(Settings currentSettings, Path tempDir, Path tempLevelDir, Path tempPatchDir) {
    TaggedDatabase database = EntityDatabase.getInstance(zipHelper);
    if (database == null) {
      return null;
    }

    writeLine(SettingsHelper.settingsToString(currentSettings));

    /* COSMETIC */
    //try {
    //  OptionsMenuGenerator.addOptionsMenu(currentSettings, zipHelper);
    //} catch (Exception e) {
    //  e.printStackTrace();
    //}
    
    if (currentSettings.getCosmeticSettings().getRandomizeBodies()) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_BODIES);
      try {
        new BodyRandomizer(currentSettings, zipHelper).randomize();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Map<String, String> swappedLinesMap = null;
    if (currentSettings.getCosmeticSettings().getRandomizeVoicelines() || currentSettings.getCosmeticSettings()
        .getRandomizeEmotions()) {
      writeLine("Randomizing dialogue...");
      VoiceRandomizer vr = new VoiceRandomizer(currentSettings, tempPatchDir, zipHelper);
      vr.randomize();
      swappedLinesMap = vr.getSwappedLinesMap();

    }
    if (currentSettings.getCosmeticSettings().getRandomizeMusic()) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_MUSIC);
      new MusicRandomizer(currentSettings, zipHelper).randomize();
    }
    if (currentSettings.getCosmeticSettings().getRandomizePlayerModel()) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_PLAYER_MODEL);
      new PlayerModelRandomizer(currentSettings, zipHelper).randomize();
    }
    if (currentSettings.getCosmeticSettings().getRandomizePlanetSize()) {
      writeLine("Randomizing planet size");
      new PlanetRandomizer(currentSettings, zipHelper).randomize();
    }

    /* GAMEPLAY, NON-LEVEL */
    if (currentSettings.getGameplaySettings().getRandomizeNeuromods()) {
      writeLine(Gui2Consts.INSTALL_PROGRESS_NEUROMOD);
      new NeuromodTreeRandomizer(currentSettings, zipHelper).randomize();
    } else if (currentSettings.getCheatSettings().getUnlockAllScans()) {
      new NeuromodTreeRandomizer(currentSettings, zipHelper).unlockAllScans();
    }

    try {
      writeLine(Gui2Consts.INSTALL_PROGRESS_LOOT);
      new LootTableRandomizer(database, currentSettings, tempPatchDir, zipHelper).randomize();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (currentSettings.getGameplaySettings().getRandomizeFabPlanCosts()) {
      writeLine("Randomizing fab plan costs...");
      new FabPlanCostRandomizer(currentSettings, zipHelper).randomize();
    }

    if (currentSettings.getExpSettings().getStartSelfDestruct()) {
      writeLine("Updating self-destruct timer...");
      new SelfDestructTimerHelper(currentSettings, zipHelper).randomize();
    }

    EntityArchetypesRandomizer entityRandomizer = new EntityArchetypesRandomizer(currentSettings, database, zipHelper)
        .addFilter(new FruitTreeFilter(currentSettings, database));
    entityRandomizer.randomize();

    new PreorderLockerRandomizer(currentSettings, zipHelper, database).randomize();

    new NightmareRandomizer(currentSettings, zipHelper, database).randomize();
    new NpcAbilitiesRandomizer(currentSettings, zipHelper, database).randomize();

    /* GAMEPLAY, LEVEL */
    LevelRandomizer levelRandomizer = new LevelRandomizer(currentSettings, zipHelper, swappedLinesMap).addFilter(
        new ItemSpawnFilter(database, currentSettings))
        .addFilter(new FlowgraphFilter(database, currentSettings))
        .addFilter(new EnemyFilter(database, currentSettings));

    if (currentSettings.getCheatSettings().getOpenStation()) {
      levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
    }

    if (currentSettings.getGameStartSettings().getAddLootToApartment()) {
      levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
    }
    
    if (currentSettings.getExpSettings().getLivingCorpses()) {
      levelRandomizer = levelRandomizer.addFilter(new LivingCorpseFilter());
    }
    
    if (currentSettings.getGameplaySettings().getRandomizeBreakables().getIsEnabled()) {
      levelRandomizer = levelRandomizer.addFilter(new BrokenObjectFilter());
    }
    
    if (currentSettings.getGameplaySettings().getRandomizeDispensers().getIsEnabled()) {
      levelRandomizer = levelRandomizer.addFilter(new OperatorDispenserFilter());
    }
    
    if (currentSettings.getGameplaySettings().getRandomizeHackables().getIsEnabled()) {
      levelRandomizer = levelRandomizer.addFilter(new LockedObjectFilter());
    }
    
    if (currentSettings.getGameplaySettings().getRandomizeRecyclers().getIsEnabled()) {
      levelRandomizer = levelRandomizer.addFilter(new RecyclerFilter());
    }

    CustomSpawnGenerator customSpawnGenerator = new CustomSpawnGenerator();

    if (currentSettings.getCheatSettings().getUseCustomSpawn()) {
      logger.info(String.format("Setting custom spawn to %s", customSpawnGenerator.getLocation()));
      customSpawnGenerator.setSpawn(currentSettings.getCheatSettings().getCustomSpawnLocation(), zipHelper, Utils
          .stringToLong(currentSettings.getSeed()));
      customSpawnGenerator.swapLocationId();
    }

    if (currentSettings.getGameplaySettings().getRandomizeStation()) {
      StationGenerator stationGenerator = new StationGenerator(Utils.stringToLong(currentSettings.getSeed()),
          customSpawnGenerator.getLevelsToIds());
      StationConnectivityFilter connectivity = new StationConnectivityFilter(stationGenerator.getDoorConnectivity(),
          stationGenerator.getSpawnConnectivity(), customSpawnGenerator.getLevelsToIds());
      String connectivityInfo = stationGenerator.toString();
      Book b = new Book("Bk_SL_Apt_Electronics", "Station Connectivity Debug Info", connectivityInfo);
      Map<String, Book> toOverwrite = Maps.newHashMap();
      toOverwrite.put("Bk_SL_Apt_Electronics", b);
      toOverwrite.put("Bk_TooFarTooFast1", b);
      BookInfoHelper bih = new BookInfoHelper(zipHelper);
      bih.installNewBooks(toOverwrite);
      levelRandomizer = levelRandomizer.addFilter(connectivity);
    }

    if (currentSettings.getExpSettings().getEnableGravityInExtAndGuts()) {
      levelRandomizer = levelRandomizer.addFilter(new GravityDisablerFilter());
    }

    writeLine(Gui2Consts.INSTALL_PROGRESS_LEVELS);
    levelRandomizer.randomize();
    writeLine("Done processing level files.");

    if (currentSettings.getCheatSettings().getUseCustomSpawn()) {
      return customSpawnGenerator.getNewSpawnLocation();
    } else {
      return null;
    }
  }

  private void copyDependencies(Settings settings, Path tempPatchDir) throws IOException {
    copyFiles(NPC_GAME_EFFECTS_DEPENDENCIES, tempPatchDir);

    //zipHelper.copyToPatch(LOGO, LOGO);

    if (settings.getMoreSettings().getMoreGuns()) {
      copyFiles(MORE_GUNS_DEPENDENCIES, tempPatchDir);
      copyFiles(MORE_GUNS_MATERIALS);

      try (BufferedReader br = new BufferedReader(new InputStreamReader(zipHelper.getInputStream(
          MORE_GUNS_FILE_LIST)))) {
        String line = "";
        while (line != null) {
          line = br.readLine();
          if (line == null) {
            break;
          }
          String path = MORE_GUNS_ICONS_DIR + line;
          zipHelper.copyToPatch(path, path);
        }
      }
    }
    
    if (settings.getMoreSettings().getPreySoulsGuns()) {
      copyFile(ZipHelper.SIGNAL_SYSTEM_PACKAGES);
      copyFile(ZipHelper.ARK_PROJECTILES_XML);
      copyFile(ZipHelper.ARK_PICKUPS_XML);
      copyFile(ZipHelper.ARK_ITEMS_XML);
      copyFile(ZipHelper.PARTICLES_CHARACTERS);
      copyFile(ZipHelper.ANIMATIONS_ARK_PLAYER_DATABASE_3P);
      copyFile(ZipHelper.ANIMATIONS_DUAL_WRENCH_PLAYER_1P);
    }

    if (settings.getExpSettings().getWanderingHumans()) {
      copyFiles(WANDERING_HUMANS_DEPENDENCIES, tempPatchDir);
    }

    if (settings.getGameplaySettings().getRandomizeStation() || settings.getExpSettings().getStartSelfDestruct()) {
      copyFiles(SURVIVE_APEX_KILL_WALL_DEPENDENCIES, tempPatchDir);
    }
  }

  private void copyFiles(ImmutableList<String> dependencies) {
    for (String s : dependencies) {
      copyFile(s);
    }
  }

  private void copyFiles(ImmutableMap<String, String> dependencies, Path tempPatchDir) {
    for (String key : dependencies.keySet()) {
      copyFile(key);
    }
  }
  
  private void copyFile(String file) {
    try {
      zipHelper.copyToPatch(file, file);
    } catch (IOException e) {
      logger.warning(String.format("Unable to copy dependency file %s, it may already have been added.", file));
      e.printStackTrace();
    }
  }

  private void writeLine(String text) {
    if (outputWindow != null) {
      outputWindow.appendText(text + "\n");
    }
  }

}
