package randomizers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;

public abstract class BaseRandomizer {
  protected static final String INSTALL_LOCATION = "GameSDK\\Precache";

  private static final String PATCH_PATTERN = "patch_%s.pak";

  protected Logger logger;

  protected String installDir;
  protected String patchName;

  protected Path outPatchDirPath;
  protected Path tempDirPath;

  Random r;

  boolean removeTempFilesOnExit;

  public BaseRandomizer(String installDir, String outPatchName) {
    this(installDir, outPatchName, false);
  }

  public BaseRandomizer(String installDir, String outPatchName, boolean removeTempFilesOnExit) {
    r = new Random();
    logger = Logger.getLogger(outPatchName);
    this.installDir = installDir;
    this.patchName = String.format(PATCH_PATTERN, outPatchName);
    this.removeTempFilesOnExit = removeTempFilesOnExit;
    outPatchDirPath = new File(installDir).toPath().resolve(INSTALL_LOCATION);

    // Create a temp directory for temp work
    tempDirPath = Paths.get(String.format("temp_%8d_%s", r.nextInt(Integer.MAX_VALUE), outPatchName));
    if (tempDirPath.resolve(outPatchName).toFile().exists()) {
      tempDirPath.resolve(outPatchName).toFile().delete();
    }
    if (tempDirPath.toFile().exists()) {
      tempDirPath.toFile().delete();
    }
    tempDirPath.toFile().mkdirs();
    if (removeTempFilesOnExit) {
      tempDirPath.toFile().deleteOnExit();
    }
  }
  
  public BaseRandomizer removeTempFilesOnExit() {
    this.removeTempFilesOnExit = true;
    return this;
  }

  public abstract void randomize();

  public boolean uninstall() {
    Path patchDir = new File(installDir).toPath().resolve(INSTALL_LOCATION);

    if (patchDir.resolve(patchName).toFile().exists()) {
      return patchDir.resolve(patchName).toFile().delete();
    }
    return false;
  }

}
