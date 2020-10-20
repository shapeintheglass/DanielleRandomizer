package settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

// Global settings context so that we can coordinate changes across multiple files
public class Settings {
  private Path tempDir;
  private Path tempLevelDir;
  private Path tempPatchDir;
  private Path installDir;

  Random rand;
  long seed;

  GenericFilterSettings iss;
  GenericFilterSettings es;
  private Logger logger;

  private Settings(Path installDir, Path tempDir, Random r, long seed, GenericFilterSettings es,
      GenericFilterSettings iss) {
    logger = Logger.getLogger("Settings");
    this.installDir = installDir;
    this.tempDir = tempDir;
    this.es = es;
    this.iss = iss;
    this.rand = r;
    if (rand != null) {
      rand.setSeed(seed);
    }
    tempLevelDir = createTempDir(tempDir, "level");
    tempPatchDir = createTempDir(tempDir, "patch");
  }

  private Path createTempDir(Path tempDir, String name) {
    long now = new Date().getTime();
    Path newTempDir = tempDir.resolve(String.format("temp_%8d_%s", now, name));
    newTempDir.toFile().mkdirs();
    logger.info(String.format("Created temp dir %s.", newTempDir));
    return newTempDir;
  }

  public long getSeed() {
    return seed;
  }

  public Path getTempDir() {
    return tempDir;
  }

  public Path getTempLevelDir() {
    return tempLevelDir;
  }

  public Path getTempPatchDir() {
    return tempPatchDir;
  }

  public Path getInstallDir() {
    return installDir;
  }

  public Random getRandom() {
    return rand;
  }

  public GenericFilterSettings getEnemySettings() {
    return es;
  }

  public GenericFilterSettings getItemSpawnSettings() {
    return iss;
  }

  public static class Builder {
    private Path installDir;
    private Path tempDir;
    Random rand;
    long seed;
    GenericFilterSettings es;
    GenericFilterSettings iss;

    public Builder() {
      installDir = Paths.get(".");
      tempDir = Paths.get(".");
    }

    public Builder setInstallDir(Path installDir) {
      this.installDir = installDir;
      return this;
    }

    public Builder setTempDir(Path tempDir) {
      this.tempDir = tempDir;
      return this;
    }

    public Builder setRand(Random r) {
      this.rand = r;
      return this;
    }

    public Builder setSeed(long seed) {
      this.seed = seed;
      return this;
    }

    public Builder setEnemySettings(GenericFilterSettings es) {
      this.es = es;
      return this;
    }

    public Builder setItemSpawnSettings(GenericFilterSettings iss) {
      this.iss = iss;
      return this;
    }

    public Settings build() {
      return new Settings(installDir, tempDir, rand, seed, es, iss);
    }
  }
}
