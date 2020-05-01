package randomizers.gameplay;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import filters.BaseFilter;
import filters.EnemyFilter;
import filters.ItemSpawnFilter;
import randomizers.BaseRandomizer;
import utils.FileConsts;
import utils.LevelConsts;
import utils.XmlEntity;

public class LevelRandomizer extends BaseRandomizer {

  private static final int READ_AHEAD_LIMIT = 10000;
  private static final String ENTITY_NAME_START = "<Entity Name=";
  ItemSpawnFilter isf;
  EnemyFilter ef;

  List<BaseFilter> filterList;

  public static final String LEVEL_PAK_NAME = "level.pak";
  public static final String TEMP_ZIP_NAME = "level.zip";
  public static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  public static final String MISSION_FILE_NAME = "mission_mission0.xml";

  public static final String[] LONG_ENTITY_CLASSES = { "TagPoint", "ArkMarker",
      "FlowgraphEntity", "GeomEntity", "ArkInteractiveObject" };

  public LevelRandomizer(String installDir) {
    super(installDir, "LevelRandomizer");
    filterList = new LinkedList<>();
    Arrays.sort(LONG_ENTITY_CLASSES);
  }
  
  public LevelRandomizer addFilter(BaseFilter f) {
    filterList.add(f);
    return this;
  }

  @Override
  public void randomize() {
    try {
      makeBackups();
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }

    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      Path inputWorkingDir = FileConsts.LOCAL_LEVELS.resolve(levelDir);
      Path tempWorkingDir = tempDirPath.resolve(levelDir);

      // XML file that defines the level
      File missionFile = inputWorkingDir.resolve(MISSION_FILE_NAME).toFile();
      // Pre-made zip file that just needs the level def
      File preMadeZipFile = inputWorkingDir.resolve(TEMP_ZIP_NAME).toFile();
      // Zip file in temp working dir to write results to
      File zipTempFile = tempWorkingDir.resolve(TEMP_ZIP_NAME).toFile();

      try {
        // Create temporary file for filtered zip
        tempWorkingDir.toFile().mkdirs();
        zipTempFile.createNewFile();

        // Add level file into zip
        filterLevelFileAndAddToZip(levelDir, missionFile, preMadeZipFile,
            zipTempFile);

        // Copy zip into install dir as a pak
        Files.copy(zipTempFile.toPath(),
            Paths.get(installDir).resolve(LevelConsts.PREFIX).resolve(levelDir)
                .resolve(LEVEL_PAK_NAME), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e1) {
        e1.printStackTrace();
        return;
      }
    }
  }

  private void makeBackups() throws IOException {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      Path levelPak = Paths.get(installDir).resolve(LevelConsts.PREFIX)
          .resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakNewName = Paths.get(installDir).resolve(LevelConsts.PREFIX)
          .resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);

      // Create backup dir if necessary
      levelPak.toFile().mkdirs();

      // If original does not exist, do nothing
      if (!levelPak.toFile().exists()) {
        continue;
      }

      // If backup already exists, do not overwrite it!!
      if (!levelPakNewName.toFile().exists()) {
        Files.copy(levelPak, levelPakNewName,
            StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

  // 4MB buffer
  private static final byte[] BUFFER = new byte[4096 * 1024];

  private static void copyStreams(InputStream in, ZipOutputStream out)
      throws IOException {
    int bytesRead = in.read(BUFFER);
    while (bytesRead != -1) {
      out.write(BUFFER, 0, bytesRead);
      bytesRead = in.read(BUFFER);
    }
  }

  /*
   * Copies level def into temp directory, while filtering.
   */
  private void filterLevelFileAndAddToZip(String levelDir, File missionFile,
      File preMadeZipFile, File zipTempFile) throws IOException {

    logger
        .info(String.format("filtering: %s, %s, %s", levelDir,
            missionFile.getName(), preMadeZipFile.getName(),
            zipTempFile.getName()));

    ZipOutputStream newZip = new ZipOutputStream(new FileOutputStream(
        zipTempFile));
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

      String line = "";
      br.mark(READ_AHEAD_LIMIT);
      while ((line = br.readLine()) != null) {
        // Scan until we see the start of an entity definition
        byte[] toWrite;
        if (line.contains(ENTITY_NAME_START)) {
          // Rewind to the mark
          br.reset();
          // Parse into an xml entity
          XmlEntity x = new XmlEntity(br);
          // Perform filtering
          filterEntityXml(x, levelDir);
          toWrite = x.toString().getBytes();
        } else {
          // Pass through all non-entity lines
          toWrite = line.getBytes();
        }
        newZip.write(toWrite);
        newZip.write('\n');
        br.mark(READ_AHEAD_LIMIT);
      }
    }
    newZip.closeEntry();
    newZip.close();
    originalZip.close();
  }

  // Filters the xml representation of an entity
  private void filterEntityXml(XmlEntity x, String levelDir) {
    for (BaseFilter filter : filterList) {
      filter.filterEntity(x, levelDir);
    }
  }

  @Override
  public boolean uninstall() {
    // Overwrite with backup
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      Path levelPak = Paths.get(installDir).resolve(LevelConsts.PREFIX)
          .resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakBackup = Paths.get(installDir).resolve(LevelConsts.PREFIX)
          .resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);
      try {
        Files.move(levelPakBackup, levelPak,
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
}
