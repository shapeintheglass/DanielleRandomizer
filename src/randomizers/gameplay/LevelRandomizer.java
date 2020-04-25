package randomizers.gameplay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import filters.BaseFilter;
import filters.EnemyFilter;
import filters.ItemSpawnFilter;
import randomizers.BaseRandomizer;
import utils.ItemSpawnSettings;
import utils.Utils;

public class LevelRandomizer extends BaseRandomizer {

  private static final String ENTITY_TXT = "entity.txt";
  private static final String ENTITY_SINGLELINE_END = "/>";
  private static final String ENTITY_NAME_END = "</Entity>";
  private static final String ENTITY_NAME_START = "<Entity Name=";
  ItemSpawnFilter isf;
  EnemyFilter ef;

  List<BaseFilter> filterList;

  public static final String LEVEL_PAK_NAME = "level.pak";
  public static final String TEMP_ZIP_NAME = "level.zip";
  public static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  public static final String MISSION_FILE_NAME = "mission_mission0.xml";

  public static final Path LOCAL_LEVELS = Paths.get("levels");
  public static final String PREFIX = "gamesdk/levels/campaign";
  public static final String[] LEVEL_DIRS = { "endgame", "engineering/cargobay", "engineering/lifesupport",
      "engineering/powersource", "executive/arboretum", "executive/bridge", "executive/corporateit",
      "executive/crewfacilities", "research/lobby", "research/prototype", "research/psychotronics",
      "research/shuttlebay", "research/simulationlabs", "research/zerog_utilitytunnels", "station/exterior" };

  public static final String[] LONG_ENTITY_CLASSES = { "TagPoint", "ArkMarker", "FlowgraphEntity", "GeomEntity",
      "ArkInteractiveObject" };

  public LevelRandomizer(String installDir, ItemSpawnSettings iss) {
    super(installDir, "LevelRandomizer");
    isf = new ItemSpawnFilter(iss);
    ef = new EnemyFilter();
    filterList = new LinkedList<>();
    filterList.add(isf);
    filterList.add(ef);
    Arrays.sort(LONG_ENTITY_CLASSES);
  }

  @Override
  public void install() {
    try {
      makeBackups();
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }

    for (String levelDir : LEVEL_DIRS) {
      Path inputWorkingDir = LOCAL_LEVELS.resolve(levelDir);
      Path tempWorkingDir = tempDirPath.resolve(levelDir);

      // XML file that defines the level
      File missionFile = inputWorkingDir.resolve(MISSION_FILE_NAME).toFile();
      // Pre-made zip file that just needs the level def
      File preMadeZipFile = inputWorkingDir.resolve(TEMP_ZIP_NAME).toFile();
      // Zip file in temp working dir to write results to
      File zipTempFile = tempWorkingDir.resolve(TEMP_ZIP_NAME).toFile();
      try {
        tempWorkingDir.toFile().mkdirs();
        zipTempFile.createNewFile();
      } catch (IOException e1) {
        e1.printStackTrace();
        return;
      }

      try {
        filterLevelFileAndAddToZip(levelDir, missionFile, preMadeZipFile, zipTempFile);
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        return;
      }

      // Copy into install directory
      try {
        Files.copy(zipTempFile.toPath(),
            Paths.get(installDir).resolve(PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
  }

  private void makeBackups() throws IOException {
    for (String levelDir : LEVEL_DIRS) {
      Path levelPak = Paths.get(installDir).resolve(PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakNewName = Paths.get(installDir).resolve(PREFIX).resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);

      // If backup already exists, do not overwrite it!!
      if (!levelPakNewName.toFile().exists()) {
        Files.copy(levelPak, levelPakNewName, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

  // 4MB buffer
  private static final byte[] BUFFER = new byte[4096 * 1024];

  public static void copyStreams(InputStream in, ZipOutputStream out) throws IOException {
    int bytesRead = in.read(BUFFER);
    while (bytesRead != -1) {
      out.write(BUFFER, 0, bytesRead);
      bytesRead = in.read(BUFFER);
    }
  }

  /*
   * Copies level def into temp directory, while filtering.
   */
  private void filterLevelFileAndAddToZip(String levelDir, File missionFile, File preMadeZipFile, File zipTempFile)
      throws IOException {

    logger.info(String.format("filtering: %s, %s, %s", levelDir, missionFile.getName(), preMadeZipFile.getName(),
        zipTempFile.getName()));

    ZipOutputStream newZip = new ZipOutputStream(new FileOutputStream(zipTempFile));
    ZipFile originalZip = new ZipFile(preMadeZipFile.getCanonicalPath());
    // Copy existing entries from original zip into new zip
    Enumeration<? extends ZipEntry> entries = originalZip.entries();
    while (entries.hasMoreElements()) {
      ZipEntry e = entries.nextElement();
      newZip.putNextEntry(new ZipEntry(e.getName()));
      if (!e.isDirectory()) {
        copyStreams(originalZip.getInputStream(e), newZip);
      }
      newZip.closeEntry();
    }

    // Create new entry for the mission file
    ZipEntry e = new ZipEntry(missionFile.getName());
    e.setSize(missionFile.getTotalSpace());
    newZip.putNextEntry(e);

    logger.info(String.format("Creating zip entry %s", missionFile.getName()));

    // Copy individual lines of mission file into new zip entry
    try (BufferedReader br = new BufferedReader(new FileReader(missionFile))) {
      String line = br.readLine();

      while (line != null) {

        // Scan until we see the start of an entity definition
        if (line.contains(ENTITY_NAME_START)) {
          Map<String, String> config = Utils.getKeysFromLine(line);
          // Create a new file to contain this entity
          File entityFile = tempDirPath.resolve(ENTITY_TXT).toFile();
          if (entityFile.exists()) {
            entityFile.delete();
          }
          entityFile.createNewFile();
          logger.info(String.format("Entity found: %s", config.get("name")));

          try (FileOutputStream fos = new FileOutputStream(entityFile)) {
            fos.write(line.getBytes());
            if (!line.endsWith(ENTITY_SINGLELINE_END)) {
              fos.write('\n');

              line = br.readLine();
              while (!line.contains(ENTITY_NAME_END)) {
                fos.write(line.getBytes());
                fos.write('\n');
                line = br.readLine();
              }
              fos.write(line.getBytes());
            }
          }
          filterEntityFile(levelDir, entityFile, config);
          writeFileToZip(entityFile, newZip);
        } else {
          // Pass through all non-entity lines
          byte[] bytes = line.getBytes();
          newZip.write(bytes);
          newZip.write('\n');
        }
        line = br.readLine();
      }
    }
    newZip.closeEntry();
    newZip.close();
    originalZip.close();
  }

  // Filters the file representation of an entity
  private boolean filterEntityFile(String levelDir, File entityFile, Map<String, String> config) {
    boolean mutated = false;
    for (BaseFilter bf : filterList) {
      mutated |= bf.filterEntityFile(levelDir, entityFile, config);
    }
    return mutated;
  }

  private void writeFileToZip(File entityFile, ZipOutputStream zos) throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(entityFile))) {
      String line = br.readLine();
      while (line != null) {
        zos.write(line.getBytes());
        zos.write('\n');
        line = br.readLine();
      }
    }
  }

  @Override
  public boolean uninstall() {
    // Overwrite with backup
    for (String levelDir : LEVEL_DIRS) {
      Path levelPak = Paths.get(installDir).resolve(PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakBackup = Paths.get(installDir).resolve(PREFIX).resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);
      try {
        Files.move(levelPakBackup, levelPak, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
}
