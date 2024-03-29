package randomizers.gameplay;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import com.google.common.collect.Lists;
import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import randomizers.gameplay.filters.BaseFilter;
import randomizers.gameplay.filters.rules.GameTokenRule;
import randomizers.generators.AddEntityHelper;
import utils.LevelConsts;
import utils.MimicSliderUtils;
import utils.StationConnectivityConsts.Door;
import utils.Utils;
import utils.ZipHelper;

/**
 * A special kind of randomizer that requires a series of filters in order to process level files.
 * 
 * @author Kida
 *
 */
public class LevelRandomizer extends BaseRandomizer {
  private static final String MISSION_FILE_NAME = "mission_mission0.xml";
  private static final String TOKENS_FOLDER_NAME = "gametokens/";

  private List<BaseFilter> filterList;

  private Map<String, String> gameTokenValues;

  // Mapping for randomizing cutscene dialogue
  private Map<String, String> swappedLinesMap;

  private GameTokenRule gameTokenRule;
  
  private Door spawnLocation;

  TaggedDatabase database;

  private float ratioOfObjectsThatAreHiddenMimics;

  public LevelRandomizer(Settings s, ZipHelper zipHelper, Map<String, String> swappedLinesMap, Door spawnLocation,
      TaggedDatabase database) {
    super(s, zipHelper);
    filterList = new LinkedList<>();
    this.gameTokenValues = new HashMap<>();
    this.swappedLinesMap = swappedLinesMap;
    this.spawnLocation = spawnLocation;
    this.database = database;
    setupGameTokens();

    ratioOfObjectsThatAreHiddenMimics = 1.0f * s.getGameplaySettings().getRandomizeMimics().getSliderValue() / 100.0f;
  }

  private void setupGameTokens() {
    // Use the settings to determine required level game tokens
    if (settings.getCheatSettings().getUnlockAllScans()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getCheatSettings().getOpenStation()) {
      gameTokenValues.putAll(LevelConsts.UNLOCK_QUESTS_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.UNLOCK_EXTERIOR_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getGameplaySettings().getRandomizeStation()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (settings.getExpSettings().getStartSelfDestruct()) {
      gameTokenValues.putAll(LevelConsts.START_SELF_DESTRUCT_TOKENS);
    }
    if (settings.getGameStartSettings().getStartOutsideApartment() || settings.getGameStartSettings().getRandomStart()) {
      // Overwrite the "FromNowhere" token from the get-go so that we skip the intro
      gameTokenValues.putAll(LevelConsts.START_OUTSIDE_APARTMENT_TOKENS);  
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
        String pathInOutputZip =
            Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir).relativize(inputPath).toString();

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

  /**
   * Copies level def into temp directory, while filtering.
   */
  private void filterMissionFile(String missionFilePath, String pathInZip, String levelDir)
      throws IOException, JDOMException {
    Document document = zipHelper.getDocument(missionFilePath);
    Element root = document.getRootElement();
    Element objects = root.getChild("Objects");

    // Mimic slider: Keep a list of items to become mimics.
    List<Element> entitiesToMimic = Lists.newArrayList();
    
    for (Element e : objects.getChildren()) {
      // Perform regular filtering
      filterEntityXml(e, levelDir);

      // Mimic Slider: Determine whether this is an entity we should mimic
      String archetype = e.getAttributeValue("Archetype");
      if (settings.getGameplaySettings().getRandomizeMimics().getIsEnabled() 
          && archetype != null
          && e.getAttribute("Pos") != null
          && e.getAttribute("Name") != null
          && !MimicSliderUtils.isInAntiMimicZone(e, levelDir)) {

        String shortArchetypeName = Utils.stripLibraryPrefixForEntity(archetype);
        Set<String> tags = Utils.getTags(database.getEntityByName(shortArchetypeName));
        if (entitiesToMimic.size() < MimicSliderUtils.MAX_MIMICS
            && MimicSliderUtils.canBeHiddenMimic(tags)
            && r.nextFloat() < ratioOfObjectsThatAreHiddenMimics) {
          entitiesToMimic.add(e);
        }
      }
    }

    if (settings.getGameplaySettings().getRandomizeMimics().getIsEnabled()) {
      logger.info(String.format("Identified %d mimickable objects in %s",
          entitiesToMimic.size(), levelDir, MimicSliderUtils.MAX_MIMICS));
    }

    AddEntityHelper.addEntities(objects, levelDir, settings, zipHelper, entitiesToMimic, spawnLocation, r);

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
      String pathInOutputZip =
          Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir).relativize(Paths.get(f)).toString();
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
    List<Element> sequences = root.getChild("Mission").getChild("SequenceData").getChildren();

    for (Element s : sequences) {
      List<Element> nodes = s.getChild("Nodes").getChildren();
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
              String newDialogIdHex =
                  new BigInteger(swappedLinesMap.get(dialogIdDec)).toString(16).toUpperCase();
              k.setAttribute("dialogId", newDialogIdHex);
            }
          }
        }

      }
    }

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }

}
