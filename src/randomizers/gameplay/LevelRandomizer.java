package randomizers.gameplay;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import randomizers.gameplay.filters.AddEntityHelper;
import randomizers.gameplay.filters.BaseFilter;
import randomizers.gameplay.filters.rules.GameTokenRule;
import utils.LevelConsts;
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

  private Map<String, String> swappedLinesMap;

  private GameTokenRule gameTokenRule;

  public LevelRandomizer(Settings s, ZipHelper zipHelper, Map<String, String> swappedLinesMap) {
    super(s, zipHelper);
    filterList = new LinkedList<>();
    this.gameTokenValues = new HashMap<>();
    this.swappedLinesMap = swappedLinesMap;

    // Use the settings to determine required level game tokens
    if (s.getCheatSettings().getUnlockAllScans()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getCheatSettings().getOpenStation()) {
      gameTokenValues.putAll(LevelConsts.UNLOCK_QUESTS_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.UNLOCK_EXTERIOR_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getStoryProgressionSettings().getRandomizeStation()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getCheatSettings().getStartSelfDestruct()) {
      gameTokenValues.putAll(LevelConsts.START_SELF_DESTRUCT_TOKENS);
    }
    if (s.getGameStartSettings().getSkipJovanCutscene()) {
      gameTokenValues.putAll(LevelConsts.SKIP_JOVAN_TOKENS);
    }
    if (s.getNpcSettings().getRandomizeNightmare()) {
      gameTokenValues.putAll(LevelConsts.ENABLE_NIGHTMARE_TOKENS);
    }

    gameTokenRule = new GameTokenRule(gameTokenValues);
  }

  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  public LevelRandomizer addGameTokenValues(Map<String, String> values) {
    this.gameTokenValues.putAll(values);
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
        String pathInOutputZip = Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir).relativize(inputPath).toString();

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
  private void filterMissionFile(String missionFilePath, String pathInZip, String levelDir) throws IOException,
      JDOMException {
    Document document = zipHelper.getDocument(missionFilePath);
    Element root = document.getRootElement();
    Element objects = root.getChild("Objects");

    for (Element e : objects.getChildren()) {
      filterEntityXml(e, levelDir);
    }
    
    AddEntityHelper.addEntities(objects, levelDir, settings, zipHelper);

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(Element e, String filename) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(e, r, filename);
    }
  }

  private void filterTokensFolder(String gameTokensFolder, String levelDir) throws JDOMException, IOException {
    for (String f : zipHelper.listFiles(gameTokensFolder)) {
      String pathInOutputZip = Paths.get(ZipHelper.DATA_LEVELS + "/" + levelDir).relativize(Paths.get(f)).toString();
      filterTokensFile(f, pathInOutputZip, levelDir);
    }
  }

  private void filterTokensFile(String gameTokensFile, String pathInZip, String levelDir) throws JDOMException,
      IOException {
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

  private void filterMovieDataFile(String movieDataFile, String pathInZip, String levelDir) throws IOException,
      JDOMException {
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
              String newDialogIdHex = new BigInteger(swappedLinesMap.get(dialogIdDec)).toString(16).toUpperCase();
              k.setAttribute("dialogId", newDialogIdHex);
            }
          }
        }

      }
    }

    zipHelper.copyToLevelPak(document, pathInZip, levelDir);
  }
}
