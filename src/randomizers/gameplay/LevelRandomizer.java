package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import json.SettingsJson;
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
  private static final String TOKENS_FOLDER_NAME = "gametokens";

  private List<BaseFilter> filterList;
  private Random r;

  private Path tempLevelDir;

  private Map<String, String> gameTokenValues;

  private GameTokenRule gameTokenRule;

  public LevelRandomizer(SettingsJson s, Path tempLevelDir, ZipHelper zipHelper) {
    super(s, zipHelper);
    filterList = new LinkedList<>();
    r = new Random(s.getSeed());
    this.tempLevelDir = tempLevelDir;
    this.gameTokenValues = new HashMap<>();

    // Use the settings to determine required level game tokens
    if (s.getGameplaySettings().getUnlockAllScans()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getGameplaySettings().getOpenStation()) {
      gameTokenValues.putAll(LevelConsts.UNLOCK_QUESTS_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.UNLOCK_EXTERIOR_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getGameplaySettings().getRandomizeStation()) {
      gameTokenValues.putAll(LevelConsts.PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS);
    }
    if (s.getGameplaySettings().getStartSelfDestruct()) {
      gameTokenValues.putAll(LevelConsts.START_SELF_DESTRUCT_TOKENS);
      gameTokenValues.put(LevelConsts.SELF_DESTRUCT_TIMER_TOKEN_NAME, s.getGameplaySettings().getSelfDestructTimer());
    }
    if (s.getGameplaySettings().getSkipJovanCutscene()) {
      gameTokenValues.putAll(LevelConsts.SKIP_JOVAN_TOKENS);
    }
    if (s.getGameplaySettings().getRandomizeNightmare()) {
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
      String levelDirIn = ZipHelper.DATA_LEVELS + "/" + levelDir;
      Path levelDirOut = tempLevelDir.resolve(LevelConsts.PREFIX).resolve(levelDir);
      levelDirOut.toFile().mkdirs();

      String missionIn = levelDirIn + "/" + MISSION_FILE_NAME;
      File missionOut = levelDirOut.resolve(MISSION_FILE_NAME).toFile();
      String gameTokensIn = levelDirIn + "/" + TOKENS_FOLDER_NAME;
      Path gameTokensOut = levelDirOut.resolve(TOKENS_FOLDER_NAME);
      gameTokensOut.toFile().mkdirs();

      try {
        logger.info(String.format("filtering level file: %s --> %s", missionIn, missionOut));
        filterMissionFile(missionIn, missionOut, levelDir);

        for (String gameTokenFile : zipHelper.listFiles(gameTokensIn)) {
          String filename = ZipHelper.getFileName(gameTokenFile);
          logger.info(String.format("filtering tokens file: %s", filename));
          filterTokensFile(gameTokenFile, gameTokensOut.resolve(filename).toFile(), levelDir);
        }

      } catch (IOException | JDOMException e1) {
        e1.printStackTrace();
        return;
      }
    }
  }

  /**
   * Copies level def into temp directory, while filtering.
   */
  private void filterMissionFile(String in, File out, String levelDir) throws IOException, JDOMException {
    Document document = zipHelper.getDocument(in);
    Element root = document.getRootElement();
    Element objects = root.getChild("Objects");

    AddEntityHelper.addEntities(objects, levelDir, settings, zipHelper);

    for (Element e : objects.getChildren()) {
      filterEntityXml(e, levelDir);
    }

    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(document, new FileOutputStream(out));
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(Element e, String filename) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(e, r, filename);
    }
  }

  private void filterTokensFile(String in, File out, String levelDir) throws JDOMException, IOException {
    Document document = zipHelper.getDocument(in);
    Element root = document.getRootElement();
    List<Element> entities = root.getChildren();

    for (Element e : entities) {
      if (gameTokenRule.trigger(e, r, null)) {
        gameTokenRule.apply(e, r, null);
      }
    }

    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(document, new FileOutputStream(out));
  }
}
