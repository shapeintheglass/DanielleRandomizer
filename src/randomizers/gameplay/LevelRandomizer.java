package randomizers.gameplay;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import randomizers.gameplay.filters.BaseFilter;
import randomizers.gameplay.filters.rules.GameTokenRule;
import randomizers.generators.AddEntityHelper;
import utils.LevelConsts;
import utils.Utils;
import utils.ZipHelper;

/**
 * A special kind of randomizer that requires a series of filters in order to process level files.
 * 
 * @author Kida
 *
 */
public class LevelRandomizer extends BaseRandomizer {

  private static final double MORGAN_SPAWN_Z = 6.7267303;
  private static final double MORGAN_SPAWN_Y = 1586.2552;
  private static final double MORGAN_SPAWN_X = 862.20569;
  private static final double MORGAN_SPAWN_DISTANCE_THRESHOLD = 50.0;
  private static final ImmutableSet<String> PROP_MIMICABLE_TAGS =
      ImmutableSet.of("ArkPhysicsProps", "_MIMICABLE", "_CARRYABLE");
  private static final ImmutableSet<String> PROP_MIMICABLE_TAGS_BLOCKLIST =
      ImmutableSet.of("_LEVERAGE_I", "_LEVERAGE_II", "_LEVERAGE_III", "Tech", "Space", "_IMPORTANT",
          "_MISSION_ITEMS", "_PLOT_CRITICAL", "_PROGRESSION", "Data", "ArkLight", "ArkFruitTree",
          "TurretFabPlan", "NB_OxygenFuse", "Wrench");
  private static final ImmutableSet<String> PICKUP_MIMICABLE_TAGS = ImmutableSet.of("ArkPickups",
      "_MIMICABLE", "_CARRYABLE", "RigidBodyEx", "_CAN_TRIGGER_AREAS");
  private static final String MISSION_FILE_NAME = "mission_mission0.xml";
  private static final String TOKENS_FOLDER_NAME = "gametokens/";

  // Maximum number of mimics to spawn into a level
  private static final int MAX_MIMICS = 400;

  private List<BaseFilter> filterList;

  private Map<String, String> gameTokenValues;

  private Map<String, String> swappedLinesMap;

  private GameTokenRule gameTokenRule;

  TaggedDatabase database;

  private float mimicSlider;

  public LevelRandomizer(Settings s, ZipHelper zipHelper, Map<String, String> swappedLinesMap,
      TaggedDatabase database) {
    super(s, zipHelper);
    filterList = new LinkedList<>();
    this.gameTokenValues = new HashMap<>();
    this.swappedLinesMap = swappedLinesMap;
    this.database = database;
    setupGameTokens();

    mimicSlider = 1.0f * s.getGameplaySettings()
        .getRandomizeMimics()
        .getSliderValue() / 100.0f;
  }

  private void setupGameTokens() {
    // Use the settings to determine required level game tokens
    if (settings.getCheatSettings()
        .getUnlockAllScans()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getCheatSettings()
        .getOpenStation()) {
      gameTokenValues.putAll(LevelConsts.UNLOCK_QUESTS_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.UNLOCK_EXTERIOR_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getGameplaySettings()
        .getRandomizeStation()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getExpSettings()
        .getStartSelfDestruct()) {
      gameTokenValues.putAll(LevelConsts.START_SELF_DESTRUCT_TOKENS);
    }
    if (settings.getGameStartSettings()
        .getSkipJovanCutscene()) {
      gameTokenValues.putAll(LevelConsts.SKIP_JOVAN_TOKENS);
    }
    if (settings.getCheatSettings()
        .getUseCustomSpawn()) {
      gameTokenValues.putAll(LevelConsts.ALLOW_GAME_SAVE_TOKENS);
    }
    if (settings.getGameStartSettings()
        .getStartOutsideApartment()) {
      gameTokenValues.putAll(LevelConsts.START_OUTSIDE_APARTMENT_TOKENS);
      gameTokenValues.putAll(LevelConsts.ALLOW_GAME_SAVE_TOKENS);
    }
    if (!settings.getCheatSettings()
        .getGameTokenOverrides()
        .isEmpty()) {
      String[] tokens = settings.getCheatSettings()
          .getGameTokenOverrides()
          .split(",");
      for (String token : tokens) {
        if (!token.contains("=")) {
          continue;
        }
        String[] keyVals = token.split("=");
        gameTokenValues.put(keyVals[0], keyVals[1]);
      }
    }

    gameTokenRule = new GameTokenRule(gameTokenValues);
  }

  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      String levelDirInputPath = ZipHelper.DATA_LEVELS + "/" + levelDir;

      Stack<String> toExamine = new Stack<>();
      toExamine.push(levelDirInputPath);
      while (!toExamine.isEmpty()) {
        String f = toExamine.pop();
        Path inputPath = Paths.get(f);
        String pathInOutputZip = Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir)
            .relativize(inputPath)
            .toString();

        if (zipHelper.isDirectory(f)) {
          if (f.endsWith(TOKENS_FOLDER_NAME)) {
            // Filter tokens folder
            try {
              filterTokensFolder(f, levelDir);
            } catch (JDOMException | IOException e) {
              logger.warning("Unable to filter gametokens for level " + levelDir);
              e.printStackTrace();
            }
          } else {
            for (String file : zipHelper.listFiles(f)) {
              toExamine.push(file);
            }
          }
        } else {
          if (f.endsWith(MISSION_FILE_NAME)) {
            // Filter mission file
            try {
              filterMissionFile(f, pathInOutputZip, levelDir);
            } catch (IOException | JDOMException e) {
              logger.warning("Unable to filter mission file for level " + levelDir);
              e.printStackTrace();
            }
          } else if (swappedLinesMap != null && f.endsWith("moviedata.xml")) {
            // Filter movie data if applicable
            try {
              filterMovieDataFile(f, pathInOutputZip, levelDir);
            } catch (IOException | JDOMException e) {
              logger.warning("Unable to filter movie data for level " + levelDir);
              e.printStackTrace();
            }
          } else {
            // Copy without filtering
            try {
              zipHelper.copyToLevelPak(f, pathInOutputZip, levelDir);
            } catch (IOException e) {
              logger.warning("Unable to copy level dependency " + f + " for level " + levelDir);
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  private boolean tooCloseToMorgan(String levelDir, Element levelEntity) {
    // Get the distance between this object and Morgan's starting spawn in ND
    if (!levelDir.equals("research/simulationlabs")) {
      return false;
    }
    String pos = levelEntity.getAttributeValue("Pos");
    if (pos == null) {
      return false;
    }
    String[] tokens = pos.split(",");
    double x = Double.parseDouble(tokens[0]);
    double y = Double.parseDouble(tokens[1]);
    double z = Double.parseDouble(tokens[2]);

    double distFromMorgan = Math.sqrt(Math.pow(x - MORGAN_SPAWN_X, 2)
        + Math.pow(y - MORGAN_SPAWN_Y, 2) + Math.pow(z - MORGAN_SPAWN_Z, 2));

    return distFromMorgan < MORGAN_SPAWN_DISTANCE_THRESHOLD;
  }

  private boolean isUsedAsDynamicObstacle(Element levelEntity) {
    Element properties2 = levelEntity.getChild("Properties2");
    if (properties2 == null) {
      return false;
    }
    String isDynamicObstacle = properties2.getAttributeValue("bUsedAsDynamicObstacle");
    return isDynamicObstacle != null && isDynamicObstacle.equals("1");
  }

  /**
   * Copies level def into temp directory, while filtering.
   */
  private void filterMissionFile(String missionFilePath, String pathInZip, String levelDir)
      throws IOException, JDOMException {
    Document document = zipHelper.getDocument(missionFilePath);
    Element root = document.getRootElement();
    Element objects = root.getChild("Objects");

    // List of pickup items that take priority for mimicking.
    List<Element> priorityMimickableEntities = Lists.newArrayList();
    // List of props that take backup priority for mimicking.
    // If there are too many mimics, some of these can be dropped.
    List<Element> backupMimickableEntities = Lists.newArrayList();

    for (Element e : objects.getChildren()) {
      // Perform regular filtering
      filterEntityXml(e, levelDir);

      // Observe whether this is an entity we should mimic
      String archetype = e.getAttributeValue("Archetype");
      if (settings.getGameplaySettings()
          .getRandomizeMimics()
          .getIsEnabled() && archetype != null && isUsedAsDynamicObstacle(e)
          && !tooCloseToMorgan(levelDir, e)) {

        String shortArchetypeName = Utils.stripLibraryPrefixForEntity(archetype);

        Set<String> tags = Utils.getTags(database.getEntityByName(shortArchetypeName));

        boolean isPickupAndMimicable = tags.containsAll(PICKUP_MIMICABLE_TAGS);
        boolean isPropAndMimicable = tags.containsAll(PROP_MIMICABLE_TAGS)
            && Sets.intersection(tags, PROP_MIMICABLE_TAGS_BLOCKLIST)
                .isEmpty();

        // Store mimic-able entities in either the priority or the backup list.
        if (isPickupAndMimicable) {
          boolean mimicThisItem = r.nextFloat() < mimicSlider;
          if (mimicThisItem) {
            priorityMimickableEntities.add(e);
          }
        } else if (isPropAndMimicable) {
          boolean mimicThisItem = r.nextFloat() < mimicSlider;
          if (mimicThisItem) {
            backupMimickableEntities.add(e);
          }
        }
      }
    }
    Collections.shuffle(priorityMimickableEntities, r);
    Collections.shuffle(backupMimickableEntities, r);

    int priorityListSize = priorityMimickableEntities.size();
    if (priorityListSize > MAX_MIMICS) {
      // If the priority list is larger than the max allowable size, trim it down.
      priorityMimickableEntities = priorityMimickableEntities.subList(0, MAX_MIMICS);
    } else if (priorityListSize < MAX_MIMICS) {
      // If the priority list is smaller than the max allowable size, fill with backup entities.
      int backfillNeeded = MAX_MIMICS - priorityListSize;
      if (backupMimickableEntities.size() < backfillNeeded) {
        priorityMimickableEntities.addAll(backupMimickableEntities);
      } else {
        priorityMimickableEntities.addAll(backupMimickableEntities.subList(0, backfillNeeded));
      }

    }

    logger.info(String.format("Identified %d mimickable objects in %s",
        priorityMimickableEntities.size(), levelDir, MAX_MIMICS));

    AddEntityHelper.addEntities(objects, levelDir, settings, zipHelper, priorityMimickableEntities,
        r);

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(Element e, String filename) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(e, r, filename);
    }
  }

  private void filterTokensFolder(String gameTokensFolder, String levelDir)
      throws JDOMException, IOException {
    for (String f : zipHelper.listFiles(gameTokensFolder)) {
      String pathInOutputZip = Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir)
          .relativize(Paths.get(f))
          .toString();
      filterTokensFile(f, pathInOutputZip, levelDir);
    }
  }

  private void filterTokensFile(String gameTokensFile, String pathInZip, String levelDir)
      throws JDOMException, IOException {
    Document document = zipHelper.getDocument(gameTokensFile);
    Element root = document.getRootElement();
    List<Element> entities = root.getChildren();

    for (Element e : entities) {
      if (gameTokenRule.trigger(e, r, null)) {
        gameTokenRule.apply(e, r, null);
      }
    }

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }

  private void filterMovieDataFile(String movieDataFile, String pathInZip, String levelDir)
      throws IOException, JDOMException {
    Document document = zipHelper.getDocument(movieDataFile);
    Element root = document.getRootElement();
    List<Element> sequences = root.getChild("Mission")
        .getChild("SequenceData")
        .getChildren();

    for (Element s : sequences) {
      List<Element> nodes = s.getChild("Nodes")
          .getChildren();
      for (Element n : nodes) {
        List<Element> tracks = n.getChildren();
        for (Element t : tracks) {
          String paramType = t.getAttributeValue("paramType");
          if (paramType == null) {
            continue;
          }
          if (!paramType.equals("Dialog")) {
            continue;
          }
          List<Element> keys = t.getChildren();
          for (Element k : keys) {
            String dialogIdHex = k.getAttributeValue("dialogId");
            String dialogIdDec = new BigInteger(dialogIdHex, 16).toString();
            if (swappedLinesMap.containsKey(dialogIdDec)) {
              String newDialogIdHex = new BigInteger(swappedLinesMap.get(dialogIdDec)).toString(16)
                  .toUpperCase();
              k.setAttribute("dialogId", newDialogIdHex);
            }
          }
        }

      }
    }

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }

}
