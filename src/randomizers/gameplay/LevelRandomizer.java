package randomizers.gameplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import filters.BaseFilter;
import randomizers.BaseRandomizer;
import settings.Settings;
import utils.FileConsts;
import utils.LevelConsts;
import utils.XmlEntity;

/**
 * A special kind of randomizer that requires a series of filters in order to process level files.
 * @author Kida
 *
 */
public class LevelRandomizer extends BaseRandomizer {

  private static final int READ_AHEAD_LIMIT = 10000;
  private static final String ENTITY_NAME_START = "<Entity Name=";

  private List<BaseFilter> filterList;

  private static final String MISSION_FILE_NAME = "mission_mission0.xml";

  private static final String[] LONG_ENTITY_CLASSES = { "TagPoint", "ArkMarker",
      "FlowgraphEntity", "GeomEntity", "ArkInteractiveObject" };

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
      File in = FileConsts.DATA_LEVELS
          .resolve(levelDir)
          .resolve(MISSION_FILE_NAME).toFile();
      File outDir = settings.getTempLevelDir()
          .resolve(LevelConsts.PREFIX)
          .resolve(levelDir).toFile();
      outDir.mkdirs();
      File out = outDir.toPath()
          .resolve(MISSION_FILE_NAME).toFile();

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
   * @param inputDir Location of input files
   * @param missionFile Path for mission file
   * @param outputDir Location for output files
   * @throws IOException
   */
  private void filterMissionFile(File in, File out) throws IOException {
    // Copy individual lines of mission file into new zip entry
    try (BufferedReader br = new BufferedReader(new FileReader(in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      String line = "";
      br.mark(READ_AHEAD_LIMIT);
      while ((line = br.readLine()) != null) {
        // Scan until we see the start of an entity definition
        String toWrite;
        if (line.contains(ENTITY_NAME_START)) {
          // Rewind to the mark
          br.reset();
          // Parse into an xml entity
          XmlEntity x = new XmlEntity(br);
          // Perform filtering
          filterEntityXml(x);
          toWrite = x.toString();
        } else {
          // Pass through all non-entity lines
          toWrite = line;
        }
        bw.write(toWrite);
        bw.write('\n');
        br.mark(READ_AHEAD_LIMIT);
      }
    }
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(XmlEntity x) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(x);
    }
  }
}
