package settings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;

// Global settings context so that we can coordinate changes across multiple files
public class Settings {
  private Path tempDir;
  private Path tempLevelDir;
  private Path tempPatchDir;
  private Path installDir;
  
  Random rand;
  long seed;
  
  ItemSpawnSettings iss;
  EnemySettings es;
  
  private Settings(Path installDir, Path tempDir, Random r, long seed, EnemySettings es, ItemSpawnSettings iss) {
    this.installDir = installDir;
    this.tempDir = tempDir;
    this.es = es;
    this.iss = iss;
    rand = new Random();
    seed = rand.nextLong();
    rand.setSeed(seed);
    tempLevelDir = createTempDir(tempDir, "level");
    tempPatchDir = createTempDir(tempDir, "patch");
  }
  
  private Path createTempDir(Path tempDir, String name) {
    long now = new Date().getTime();
    Path newTempDir = tempDir.resolve(String.format("temp_%8d_%s",
          now, name));
    newTempDir.toFile().mkdirs();
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
  
  public EnemySettings getEnemySettings() {
    return es;
  }
  
  public ItemSpawnSettings getItemSpawnSettings() {
    return iss;
  }
  
  public static class Builder {
    private Path installDir;
    private Path tempDir;
    Random rand;
    long seed;
    EnemySettings es;
    ItemSpawnSettings iss;
    
    public Builder() {
      installDir = Paths.get(".");
      tempDir = Paths.get(".");
      rand = new Random();
      seed = rand.nextLong();
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
    
    public Builder setEnemySettings(EnemySettings es) {
      this.es = es;
      return this;
    }
    
    public Builder setItemSpawnSettings(ItemSpawnSettings iss) {
      this.iss = iss;
      return this;
    }
    
    public Settings build() {
      return new Settings(installDir, tempDir, rand, seed, es, iss);
    }
  }
}
