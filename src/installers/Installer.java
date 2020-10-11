package installers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import settings.Settings;
import utils.FileConsts;
import utils.LevelConsts;

/**
 * 
 * Central place for installing all mod files. Handles all logistics of zipping
 * and paking so that randomizers only have to worry about filtering.
 *
 */
public class Installer {

  private static final String LEVEL_PAK_NAME = "level.pak";
  private static final String LEVEL_ZIP_NAME = "level.zip";
  private static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  protected static final String INSTALL_LOCATION = "GameSDK\\Precache";

  private static final String PATCH_ZIP_NAME = "patch_randomizer.zip";
  private static final String PATCH_NAME = "patch_randomizer.pak";
  private static final String MISSION_FILE_NAME = "mission_mission0.xml";

  private Settings s;
  private File patchFile;

  /**
   * Initialize the installer.
   * 
   * @param installDir Prey install location
   * @param tempDir    Where to store temporary files
   */
  public Installer(Settings s) {
    this.s = s;
    patchFile = s.getInstallDir().resolve(INSTALL_LOCATION).resolve(PATCH_NAME).toFile();
  }

  public void install() throws IOException {
    if (!s.getInstallDir().toFile().exists()) {
      s.getInstallDir().toFile().mkdirs();
    }

    installPatchDir();
    backupLevelFiles();
    installLevelFiles();
  }

  private void installPatchDir() throws IOException {
    // Create temporary zip file in temp dir to store patch as a zip
    Path tempPatchFileAsZip = s.getTempDir().resolve(PATCH_ZIP_NAME);

    // Zip to temp zip file location
    if (zipFilesInDir(s.getTempPatchDir(), tempPatchFileAsZip)) {
      // Copy to pak destination as a pak
      Files.copy(tempPatchFileAsZip, patchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }

  /**
   * Create a backup of each level.pak within the same directory in the install
   * version. This allows us to switch back easily if needed to.
   * 
   * @throws IOException
   */
  private void backupLevelFiles() throws IOException {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      Path levelPak = s.getInstallDir().resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakNewName = s.getInstallDir().resolve(LevelConsts.PREFIX).resolve(levelDir)
          .resolve(BACKUP_LEVEL_PAK_NAME);

      // Create backup dir if necessary
      levelPak.toFile().mkdirs();

      // If original does not exist, do nothing
      if (!levelPak.toFile().exists()) {
        continue;
      }

      // If backup already exists, do not overwrite it!!
      if (!levelPakNewName.toFile().exists()) {
        Files.copy(levelPak, levelPakNewName, StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

  private void installLevelFiles() throws IOException {
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      // Premade zip file containing everything but the main mission def
      Path preMadeZipFile = FileConsts.DATA_LEVELS.resolve(levelDir).resolve(LEVEL_ZIP_NAME);

      // Location to copy into for combined zip
      s.getTempLevelDir().resolve(levelDir).toFile().mkdirs();
      Path tempZipFile = s.getTempLevelDir().resolve(levelDir).resolve(LEVEL_ZIP_NAME);

      // Location of the mission file
      Path missionFile = s.getTempLevelDir().resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(MISSION_FILE_NAME);

      if (!missionFile.toFile().exists()) {
        continue;
      }

      try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZipFile.toString()));
          ZipFile originalZip = new ZipFile(preMadeZipFile.toFile().getCanonicalPath());
          FileInputStream missionFileStream = new FileInputStream(missionFile.toFile())) {

        // Copy existing entries from original zip into new zip
        Enumeration<? extends ZipEntry> entries = originalZip.entries();
        while (entries.hasMoreElements()) {
          ZipEntry e = entries.nextElement();
          zos.putNextEntry(new ZipEntry(e.getName()));
          if (!e.isDirectory()) {
            copyStreams(originalZip.getInputStream(e), zos);
          }
          zos.closeEntry();
        }

        // Insert mission file
        zos.putNextEntry(new ZipEntry(MISSION_FILE_NAME));
        copyStreams(missionFileStream, zos);
        zos.closeEntry();
      }

      // Copy zip over to final destination
      Path levelPak = s.getInstallDir().resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Files.copy(tempZipFile, levelPak, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  // Copies contents of the input stream to the output stream
  private void copyStreams(InputStream zin, ZipOutputStream out) throws IOException {
    byte[] buf = new byte[1024];
    int len;
    while ((len = zin.read(buf)) > 0) {
      out.write(buf, 0, len);
    }
  }

  // Remove files created by this installer
  public void uninstall() {
    if (patchFile.exists()) {
      patchFile.delete();
    }

    // Overwrite level files with backup
    for (String levelDir : LevelConsts.LEVEL_DIRS) {
      Path levelPak = s.getInstallDir().resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakBackup = s.getInstallDir().resolve(LevelConsts.PREFIX).resolve(levelDir)
          .resolve(BACKUP_LEVEL_PAK_NAME);
      try {
        Files.move(levelPakBackup, levelPak, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Zips all files in the given directory and outputs to the given zip file.
   * 
   * @param dirToZip
   * @param zipFileDest
   * @throws IOException
   */
  private static boolean zipFilesInDir(Path dirToZip, Path zipFileDest) throws IOException {
    // Return failure if there is nothing to zip
    if (!dirToZip.toFile().exists() || (dirToZip.toFile().isDirectory() && dirToZip.toFile().listFiles().length == 0)) {
      return false;
    }

    // Create the output zip file if it does not already exist
    zipFileDest.toFile().createNewFile();
    // Zip up all files in dir
    try (ZipOutputStream zos = new ZipOutputStream(
        new BufferedOutputStream(new FileOutputStream(zipFileDest.toString())))) {

      Stack<File> dirsToSearch = new Stack<File>();
      dirsToSearch.push(dirToZip.toFile());

      while (!dirsToSearch.isEmpty()) {
        File nextDir = dirsToSearch.pop();
        for (File f : nextDir.listFiles()) {
          if (f.isFile()) {
            zipFile(f, dirToZip.relativize(f.toPath()).toString(), zos);
          } else if (f.isDirectory()) {
            dirsToSearch.push(f);
          }
        }
      }

    } catch (Exception e) {
      throw (e);
    }
    return true;
  }

  /**
   * Zips the given file to a zip under a given filename.
   * 
   * @param in             File to zip
   * @param outputFilename Name to give it in the zip
   * @param zos            Output stream of the zip file
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void zipFile(File in, String outputFilename, ZipOutputStream zos)
      throws FileNotFoundException, IOException {
    StringBuilder buffer = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(in));) {
      String line = br.readLine();

      while (line != null) {
        buffer.append(line);
        buffer.append('\n');
        line = br.readLine();
      }
    }

    byte[] bytes = buffer.toString().getBytes();
    zos.putNextEntry(new ZipEntry(outputFilename));
    zos.write(bytes, 0, bytes.length);
    zos.closeEntry();
  }

}
