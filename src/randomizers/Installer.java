package randomizers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import utils.FileConsts;
import utils.LevelConsts;

/**
 * 
 * Central place for installing all mod files. Handles all logistics of zipping
 * and paking so that randomizers only have to worry about filtering.
 *
 */
public class Installer {

  public static final String LEVEL_PAK_NAME = "level.pak";
  public static final String TEMP_ZIP_NAME = "level.zip";
  public static final String BACKUP_LEVEL_PAK_NAME = "level_backup.pak";
  private static final String PATCH_PATTERN = "patch_%s.pak";
  protected static final String INSTALL_LOCATION = "GameSDK\\Precache";

  private static final String PATCH_NAME = "patch_randomizer.pak";

  // Prey install location
  private Path installDir;
  // Temporary dir for holding patch files
  private Path tempPatchDir;
  // Temporary dir for holding level files
  private Path tempLevelDir;

  public Installer(Path installDir, Path tempPatchDir, Path tempLevelDir) {
    this.installDir = installDir;
    this.tempPatchDir = tempPatchDir;
    this.tempLevelDir = tempLevelDir;
  }

  public void install() {
    // Zip up all files in patch dir and install

    // Zip up all files in level dir and install

  }

  public boolean validateInstallDir(Path installDir) {
    // Assert existence of key directories in install directory

    // Binary

    // Precache dir

    // Levels dir

    // Localization dir
    return true;
  }

  public Path prepPatchDir() {
    // Create patch dir
    if (!tempPatchDir.toFile().exists()) {
      tempPatchDir.toFile().mkdirs();
    }
    return tempPatchDir;
  }
  
  private void installPatchDir() {
    
  }

  public Path prepLevelDir() {
    // Create level dir
    if (!tempLevelDir.toFile().exists()) {
      tempLevelDir.toFile().mkdirs();
    }

    // Unzip contents of level files into level dir
    for (Path p : FileConsts.LOCAL_LEVELS) {
      //
    }
    
    return tempLevelDir;
  }
  
  private void installLevelDir() {
    
  }

  private void backupLevelFiles() {
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

  public void uninstallPatchDir() {
    // Remove files created by this installer

    // Replace level patches with the backups created
    Path patchDir = new File(installDir).toPath().resolve(INSTALL_LOCATION);

    if (patchDir.resolve(patchName).toFile().exists()) {
      return patchDir.resolve(patchName).toFile().delete();
    }
    return false;

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
  
  private void uninstallLevelDir() {
    
  }

  private void zipFilesInDir(Path dirToZip, Path zipFileDest) {
    zipFileDest.toFile().createNewFile();
    // Zip up all files in dir

    // Copy to pak destination as a pak
  }

}
