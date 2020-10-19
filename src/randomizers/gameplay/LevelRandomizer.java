package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import randomizers.BaseRandomizer;
import randomizers.gameplay.level.filters.BaseFilter;
import settings.Settings;
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

  private List<BaseFilter> filterList;

  private static final String MISSION_FILE_NAME = "mission_mission0.xml";

  public LevelRandomizer(Settings s) {
    super(s);
    filterList = new LinkedList<>();
  }

  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      File in = FileConsts.DATA_LEVELS.resolve(levelDir).resolve(MISSION_FILE_NAME).toFile();
      File outDir = settings.getTempLevelDir().resolve(LevelConsts.PREFIX).resolve(levelDir).toFile();
      outDir.mkdirs();
      File out = outDir.toPath().resolve(MISSION_FILE_NAME).toFile();

      try {
        logger.info(String.format("filtering level file: %s --> %s", in, out));
        filterMissionFile(in, out);
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
  private void filterMissionFile(File in, File out) throws IOException, JDOMException {
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(in);
    Element root = document.getRootElement();
    List<Element> entities = root.getChild("Objects").getChildren();

    for (Element e : entities) {
      filterEntityXml(e);
    }
    
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(document, new FileOutputStream(out));

  }

  // Filters the xml representation of an entity
  private void filterEntityXml(Element e) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(e);
    }
  }
}
