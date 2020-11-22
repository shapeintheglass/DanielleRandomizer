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
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import randomizers.gameplay.level.filters.BaseFilter;
import randomizers.gameplay.level.filters.rules.GameTokenRule;
import utils.FileConsts;
import utils.LevelConsts;

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

  public LevelRandomizer(SettingsJson s, Path tempLevelDir) {
    super(s);
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
      gameTokenValues.putAll(LevelConsts.UNLOCK_PSYCHOTRONICS_GAME_TOKENS);
      gameTokenValues.putAll(LevelConsts.UNLOCK_LIFT_GAME_TOKENS);
    }
    if (s.getGameplaySettings().getRandomizeStation()) {
      gameTokenValues.putAll(LevelConsts.UNLOCK_PSYCHOTRONICS_GAME_TOKENS);
    }
    if (s.getGameplaySettings().getStartOn2ndDay()) {
      gameTokenValues.putAll(LevelConsts.START_2ND_DAY_GAME_TOKENS);
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
      Path levelDirIn = FileConsts.DATA_LEVELS.resolve(levelDir);
      Path levelDirOut = tempLevelDir.resolve(LevelConsts.PREFIX).resolve(levelDir);
      levelDirOut.toFile().mkdirs();

      File missionIn = levelDirIn.resolve(MISSION_FILE_NAME).toFile();
      File missionOut = levelDirOut.resolve(MISSION_FILE_NAME).toFile();
      File gameTokensIn = levelDirIn.resolve(TOKENS_FOLDER_NAME).toFile();
      Path gameTokensOut = levelDirOut.resolve(TOKENS_FOLDER_NAME);
      gameTokensOut.toFile().mkdirs();

      try {
        logger.info(String.format("filtering level file: %s --> %s", missionIn, missionOut));
        filterMissionFile(missionIn, missionOut, levelDir);

        for (File gameTokenFile : gameTokensIn.listFiles()) {
          logger.info(String.format("filtering tokens file: %s", gameTokenFile));
          filterTokensFile(gameTokenFile, gameTokensOut.resolve(gameTokenFile.getName()).toFile(),
              levelDir);
        }

      } catch (IOException | JDOMException e1) {
        e1.printStackTrace();
        return;
      }
    }
  }

  /**
   * Copies level def into temp directory, while filtering.
   * 
   * @param inputDir Location of input files
   * @param missionFile Path for mission file
   * @param outputDir Location for output files
   * @throws IOException
   * @throws JDOMException
   */
  private void filterMissionFile(File in, File out, String levelDir)
      throws IOException, JDOMException {
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(in);
    Element root = document.getRootElement();
    List<Element> entities = root.getChild("Objects").getChildren();

    for (Element e : entities) {
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

  private void filterTokensFile(File in, File out, String levelDir)
      throws JDOMException, IOException {
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(in);
    Element root = document.getRootElement();
    List<Element> entities = root.getChildren();

    for (Element e : entities) {
      if (gameTokenRule.trigger(e, r, in.getName())) {
        gameTokenRule.apply(e, r, in.getName());
      }
    }

    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(document, new FileOutputStream(out));
  }
}
