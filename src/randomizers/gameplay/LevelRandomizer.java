package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
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
import utils.FileConsts;
import utils.LevelConsts;

/**
 * A special kind of randomizer that requires a series of filters in order to
 * process level files.
 * 
 * @author Kida
 *
 */
public class LevelRandomizer extends BaseRandomizer {

  private static final String MISSION_FILE_NAME = "mission_mission0.xml";
  
  private List<BaseFilter> filterList;
  private Random r;
  
  private Path tempLevelDir;

  public LevelRandomizer(SettingsJson s, Path tempLevelDir) {
    super(s);
    filterList = new LinkedList<>();
    r = new Random(s.getSeed());
    this.tempLevelDir = tempLevelDir;
  }

  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      File in = FileConsts.DATA_LEVELS.resolve(levelDir).resolve(MISSION_FILE_NAME).toFile();
      File outDir = tempLevelDir.resolve(LevelConsts.PREFIX).resolve(levelDir).toFile();
      outDir.mkdirs();
      File out = outDir.toPath().resolve(MISSION_FILE_NAME).toFile();

      try {
        logger.info(String.format("filtering level file: %s --> %s", in, out));
        filterMissionFile(in, out, levelDir);
      } catch (IOException | JDOMException e1) {
        e1.printStackTrace();
        return;
      }
    }
  }

  /**
   * Copies level def into temp directory, while filtering.
   * 
   * @param inputDir    Location of input files
   * @param missionFile Path for mission file
   * @param outputDir   Location for output files
   * @throws IOException
   * @throws JDOMException
   */
  private void filterMissionFile(File in, File out, String levelDir) throws IOException, JDOMException {
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
}
