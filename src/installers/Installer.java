package installers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
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
  private static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  private static final String INSTALL_LOCATION = "GameSDK\\Precache";

  public static final String PATCH_NAME = "patch_randomizer.pak";

  private File patchFile;
  private Logger logger;

  private Path installDir;
  private Path tempPatchDir;
  private Path tempLevelDir;

  private String spawnLocation;

  /**
   * Initialize the installer.
   * 
   * @param installDir Prey install location
   * @param tempDir Where to store temporary files
   */
  public Installer(Path installDir, Path tempLevelDir, Path tempPatchDir, String spawnLocation) {
    this.installDir = installDir;
    this.tempLevelDir = tempLevelDir;
    this.tempPatchDir = tempPatchDir;
    this.spawnLocation = spawnLocation;

    patchFile = installDir.resolve(INSTALL_LOCATION)
        .resolve(PATCH_NAME)
        .toFile();
    logger = Logger.getLogger("Installer");
  }

  public void install() throws IOException, InterruptedException {
    logger.info("Installer has begun!");
    if (!installDir.toFile()
        .exists()) {
      installDir.toFile()
          .mkdirs();
    }

    installPatchDir();
    backupLevelFiles();
    installLevelFiles();

    logger.info("Done installing! Have a nice day.");
  }

  public static boolean verifyInstallDir(Logger logger, Path installDir) {
    logger.info("Verifying install directory...");
    if (!installDir.toFile().exists()) {
      logger.info(String.format("Unable to locate install directory (%s)", installDir));
      return false;
    }
    
    Path installLoc = installDir.resolve(INSTALL_LOCATION);
    if (!installLoc.toFile().exists()) {
      // Create the Precache folder if it does not exist
      boolean success = installLoc.toFile().mkdir();
      if (success) {
        logger.info("Created a Precache directory");
        return true;
      } else {
        logger.info("Unable to create Precache directory!");
        return false;
      }
    }
    Path levelLoc = installDir.resolve(LevelConsts.PREFIX);
    if (!levelLoc.toFile().exists()) {
      logger.info(String.format("Level folder does not exist! (%s)", levelLoc));
      return false;
    }
    return true;
  }

  public static boolean verifyDataExists(Logger logger) {
    logger.info("Verifying existence of data/ folder...");
    return Paths.get(ZipHelper.DATA_PAK)
        .toFile()
        .exists();
  }

  public static boolean testInstall(Logger logger, File patchFile) {
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
    Path tempPatchFileAsZip = tempPatchDir.resolve(PATCH_NAME);

    // Copy to pak destination as a pak
    logger.info(String.format("Copying %s to %s", tempPatchFileAsZip, patchFile.toPath()));
    Files.copy(tempPatchFileAsZip, patchFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

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
      Path levelPak = installDir.resolve(LevelConsts.PREFIX)
          .resolve(levelDir)
          .resolve(LEVEL_PAK_NAME);
      Path levelPakNewName = installDir.resolve(LevelConsts.PREFIX)
          .resolve(levelDir)
          .resolve(BACKUP_LEVEL_PAK_NAME);

      // Create backup dir if necessary
      levelPak.toFile()
          .mkdirs();

      // If original does not exist, do nothing
      if (!levelPak.toFile()
          .exists()) {
        continue;
      }

      // If backup already exists, do not overwrite it!!
      if (!levelPakNewName.toFile()
          .exists()) {
        logger.info(String.format("Backing up %s to %s. (%d/%d)", levelDir, levelPakNewName, i + 1,
            LevelConsts.LEVEL_DIRS.length));
        Files.copy(levelPak, levelPakNewName, StandardCopyOption.REPLACE_EXISTING);
      } else {
        logger.info(String.format("Level backup file %s already exists, not overwriting. (%d/%d)",
            levelPakNewName, i + 1, LevelConsts.LEVEL_DIRS.length));
      }
    }
    logger.info("Finished backing up level files.");
  }

  private void installLevelFiles() throws IOException {
    logger.info("----INSTALLING NEW LEVEL FILES...----");
    for (int i = 0; i < LevelConsts.LEVEL_DIRS.length; i++) {
      String levelDir = LevelConsts.LEVEL_DIRS[i];

      Path sourcePak = tempLevelDir.resolve(levelDir)
          .resolve(ZipHelper.LEVEL_OUTPUT_PAK);

      // Swap spawn location if applicable
      String destLevelDir = levelDir;
      if (spawnLocation != null && levelDir.equals(spawnLocation)) {
        logger.info(String.format("Installing %s in place of sim labs", spawnLocation));
        destLevelDir = "research/simulationlabs";
      } else if (spawnLocation != null && levelDir.equals("research/simulationlabs")) {
        logger.info(String.format("Installing sim labs in place of %s", spawnLocation));
        destLevelDir = spawnLocation;
      }

      // Copy zip over to final destination
      Path levelPak = installDir.resolve(LevelConsts.PREFIX)
          .resolve(destLevelDir)
          .resolve(LEVEL_PAK_NAME);
      Files.copy(sourcePak, levelPak, StandardCopyOption.REPLACE_EXISTING);
      logger.info(String.format("Installed level file %s (%d/%d)", levelPak, i + 1,
          LevelConsts.LEVEL_DIRS.length));
    }

    logger.info("----DONE INSTALLING NEW LEVEL FILES!----");
  }

  // Remove files created by this installer
  public void uninstall() {
    uninstall(installDir, logger);
  }

  public static void uninstall(Path installDir, Logger logger) {
    logger.info("Uninstalling files created by this randomizer...");
    File patchFile = installDir.resolve(INSTALL_LOCATION)
        .resolve(PATCH_NAME)
        .toFile();
    if (patchFile.exists()) {
      logger.info(String.format("Deleting patch file %s", patchFile.getPath()));
      patchFile.delete();
    } else {
      logger
          .info(String.format("Patch file (%s) not found, no need to delete", patchFile.getPath()));
    }

    // Overwrite level files with backup
    for (int i = 0; i < LevelConsts.LEVEL_DIRS.length; i++) {
      String levelDir = LevelConsts.LEVEL_DIRS[i];
      Path levelPak = installDir.resolve(LevelConsts.PREFIX)
          .resolve(levelDir)
          .resolve(LEVEL_PAK_NAME);
      Path levelPakBackup = installDir.resolve(LevelConsts.PREFIX)
          .resolve(levelDir)
          .resolve(BACKUP_LEVEL_PAK_NAME);
      if (levelPakBackup.toFile()
          .exists()) {
        try {
          logger.info(String.format("Replacing level.pak with backup file %s (%d/%d)",
              levelPakBackup, i + 1, LevelConsts.LEVEL_DIRS.length));
          Files.move(levelPakBackup, levelPak, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        logger.info(String.format("No level_backup.pak found for %s, nothing to revert (%d/%d)",
            levelDir, i + 1, LevelConsts.LEVEL_DIRS.length));
      }
    }
    logger.info("Done uninstalling files.");
  }
}
