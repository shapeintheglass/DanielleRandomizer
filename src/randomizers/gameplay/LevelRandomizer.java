package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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

  private static final int READ_AHEAD_LIMIT = 10000;
  private static final String ENTITY_NAME_START = "<Entity Name=";

  private List<BaseFilter> filterList;

  private static final String MISSION_FILE_NAME = "mission_mission0.xml";

  private static final String[] LONG_ENTITY_CLASSES = { "TagPoint",
      "ArkMarker", "FlowgraphEntity", "GeomEntity", "ArkInteractiveObject" };

  public LevelRandomizer(Settings s) {
    super("LevelRandomizer", s);
    filterList = new LinkedList<>();
    Arrays.sort(LONG_ENTITY_CLASSES);
  }

  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      File in = FileConsts.DATA_LEVELS.resolve(levelDir)
          .resolve(MISSION_FILE_NAME).toFile();
      File outDir = settings.getTempLevelDir().resolve(LevelConsts.PREFIX)
          .resolve(levelDir).toFile();
      outDir.mkdirs();
      File out = outDir.toPath().resolve(MISSION_FILE_NAME).toFile();

      try {
        logger.info(String.format("filtering: %s --> %s", in, out));
        filterMissionFile(in, out);
      } catch (IOException e1) {
        e1.printStackTrace();
        return;
      }
    }
  }

  /**
   * Copies level def into temp directory, while filtering.
   * 
   * @param inputDir
   *          Location of input files
   * @param missionFile
   *          Path for mission file
   * @param outputDir
   *          Location for output files
   * @throws IOException
   */
  private void filterMissionFile(File in, File out) throws IOException {
    // Copy individual lines of mission file into new zip entry
    try {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(
          in));
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(in);
      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();

        if (event.getEventType() == XMLStreamConstants.START_DOCUMENT) {
          StartElement startElement = event.asStartElement();
          String qName = startElement.getName().getLocalPart();
          if (qName.equals("Entity")) {
            filterEntityXml(startElement);
          }
          break;
        }
      }
      XMLOutputter xmlOutput = new XMLOutputter();

      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (XMLStreamException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(StartElement x) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(x);
    }
  }
}
