package installers;

import java.io.BufferedOutputStream;
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
import java.util.Collection;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import json.SettingsJson;
import utils.LevelConsts;
import utils.ZipHelper;

/**
 * 
 * Central place for installing all mod files. Handles all logistics of zipping and paking so that
 * randomizers only have to worry about filtering and copying dependencies over.
 *
 */
public class Installer {
  private static final String LEVEL_PAK_NAME = "level.pak";
  private static final String LEVEL_ZIP_NAME = "level.zip";
  private static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  private static final String INSTALL_LOCATION = "GameSDK\\Precache";

  private static final String PATCH_ZIP_NAME = "patch_randomizer.zip";
  private static final String PATCH_NAME = "patch_randomizer.pak";

  private File patchFile;
  private Logger logger;

  private Path installDir;
  private Path tempDir;
  private Path tempPatchDir;
  private Path tempLevelDir;

  private ZipHelper zipHelper;

  /**
   * Initialize the installer.
   * 
   * @param installDir Prey install location
   * @param tempDir Where to store temporary files
   */
  public Installer(Path installDir, Path tempDir, Path tempLevelDir, Path tempPatchDir, SettingsJson settings,
      ZipHelper zipHelper) {
    this.installDir = installDir;
    this.tempDir = tempDir;
    this.tempLevelDir = tempLevelDir;
    this.tempPatchDir = tempPatchDir;
    this.zipHelper = zipHelper;

    patchFile = installDir.resolve(INSTALL_LOCATION).resolve(PATCH_NAME).toFile();
    logger = Logger.getLogger("Installer");
  }

  public void install() throws IOException, InterruptedException {
    logger.info("Installer has begun!");
    if (!installDir.toFile().exists()) {
      installDir.toFile().mkdirs();
    }

    installPatchDir();
    backupLevelFiles();
    installLevelFiles();

    logger.info("Done installing! Have a nice day.");
  }

  public boolean verifyInstallDir() {
    logger.info("Verifying install directory...");
    return installDir.resolve(INSTALL_LOCATION).toFile().exists() && installDir.resolve(LevelConsts.PREFIX)
        .toFile()
        .exists();
  }

  public boolean verifyDataExists() {
    logger.info("Verifying existence of data/ folder...");
    return Paths.get(ZipHelper.DATA_PAK).toFile().exists();
  }

  public boolean testInstall() {
    logger.info("Verifying ability to install randomizer mod...");
    try {
      patchFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private void installPatchDir() throws IOException {
    logger.info("----INSTALLING PATCH FILE...----");

    // Create temporary zip file in temp dir to store patch as a zip
    Path tempPatchFileAsZip = tempDir.resolve(PATCH_ZIP_NAME);

    // Zip to temp zip file location
    if (zipFilesInDir(tempPatchDir, tempPatchFileAsZip)) {
      // Copy to pak destination as a pak
      logger.info(String.format("Copying %s to %s", tempPatchFileAsZip, patchFile.toPath()));
      Files.copy(tempPatchFileAsZip, patchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    logger.info("----DONE INSTALLING PATCH FILE!----");
  }

  /**
   * Create a backup of each level.pak within the same directory in the install version. This allows
   * us to switch back easily if needed to.
   * 
   * @throws IOException
   */
  private void backupLevelFiles() throws IOException {
    logger.info("Backing up existing level files as level_backup.pak...");
    for (int i = 0; i < LevelConsts.LEVEL_DIRS.length; i++) {
      String levelDir = LevelConsts.LEVEL_DIRS[i];
      Path levelPak = installDir.resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakNewName = installDir.resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);

      // Create backup dir if necessary
      levelPak.toFile().mkdirs();

      // If original does not exist, do nothing
      if (!levelPak.toFile().exists()) {
        continue;
      }

      // If backup already exists, do not overwrite it!!
      if (!levelPakNewName.toFile().exists()) {
        logger.info(String.format("Backing up %s to %s. (%d/%d)", levelDir, levelPakNewName, i + 1,
            LevelConsts.LEVEL_DIRS.length));
        Files.copy(levelPak, levelPakNewName, StandardCopyOption.REPLACE_EXISTING);
      } else {
        logger.info(String.format("Level backup file %s already exists, not overwriting. (%d/%d)", levelPakNewName, i
            + 1, LevelConsts.LEVEL_DIRS.length));
      }
    }
    logger.info("Finished backing up level files.");
  }

  private void installLevelFiles() throws IOException {
    logger.info("----INSTALLING NEW LEVEL FILES...----");
    for (int i = 0; i < LevelConsts.LEVEL_DIRS.length; i++) {
      String levelDir = LevelConsts.LEVEL_DIRS[i];
      // Premade zip file containing everything but the main mission def
      String preMadeZipFilePath = ZipHelper.DATA_LEVELS + "/" + levelDir;

      // Location to copy into for combined zip
      tempLevelDir.resolve(levelDir).toFile().mkdirs();
      Path tempZipFile = tempLevelDir.resolve(levelDir).resolve(LEVEL_ZIP_NAME);

      Stack<String> toZip = new Stack<>();
      toZip.add(preMadeZipFilePath);

      try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZipFile.toString()))) {
        while (!toZip.isEmpty()) {
          Collection<String> files = zipHelper.listFiles(toZip.pop());
          for (String file : files) {
            if (zipHelper.isDirectory(file)) {
              toZip.add(file);
            } else {
              InputStream is = zipHelper.getInputStream(file);
              // Insert files
              zos.putNextEntry(new ZipEntry(file));
              copyStreams(is, zos);
              zos.closeEntry();
            }
          }
        }

      }

      // Copy zip over to final destination
      Path levelPak = installDir.resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Files.copy(tempZipFile, levelPak, StandardCopyOption.REPLACE_EXISTING);
      logger.info(String.format("Installed level file %s (%d/%d)", levelPak, i + 1, LevelConsts.LEVEL_DIRS.length));
    }

    logger.info("----DONE INSTALLING NEW LEVEL FILES!----");
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
    uninstall(installDir, logger);
  }

  public static void uninstall(Path installDir, Logger logger) {
    logger.info("Uninstalling files created by this randomizer...");
    File patchFile = installDir.resolve(INSTALL_LOCATION).resolve(PATCH_NAME).toFile();
    if (patchFile.exists()) {
      logger.info(String.format("Deleting patch file %s", patchFile.getPath()));
      patchFile.delete();
    } else {
      logger.info(String.format("Patch file (%s) not found, no need to delete", patchFile.getPath()));
    }

    // Overwrite level files with backup
    for (int i = 0; i < LevelConsts.LEVEL_DIRS.length; i++) {
      String levelDir = LevelConsts.LEVEL_DIRS[i];
      Path levelPak = installDir.resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(LEVEL_PAK_NAME);
      Path levelPakBackup = installDir.resolve(LevelConsts.PREFIX).resolve(levelDir).resolve(BACKUP_LEVEL_PAK_NAME);
      if (levelPakBackup.toFile().exists()) {
        try {
          logger.info(String.format("Replacing level.pak with backup file %s (%d/%d)", levelPakBackup, i + 1,
              LevelConsts.LEVEL_DIRS.length));
          Files.move(levelPakBackup, levelPak, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        logger.info(String.format("No level_backup.pak found for %s, nothing to revert (%d/%d)", levelDir, i + 1,
            LevelConsts.LEVEL_DIRS.length));
      }
    }
    logger.info("Done uninstalling files.");
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
    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileDest
        .toString())))) {

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
   * @param in File to zip
   * @param outputFilename Name to give it in the zip
   * @param zos Output stream of the zip file
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void zipFile(File in, String outputFilename, ZipOutputStream zos) throws FileNotFoundException,
      IOException {
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
