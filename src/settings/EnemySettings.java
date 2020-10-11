package settings;

/**
 * Describes how enemy spawns should be applied
 * @author Kida
 *
 */
public class EnemySettings {
  
  public enum Preset {
    NONE,
    NO_LOGIC, // Randomize with no regard for type, including humans
    WITHIN_TYPE, // Randomize within type (robot, alien, human)
    ALL_NIGHTMARES, // Everything is nightmares
  }

  private Preset preset;
  
  /**
   * Describes how enemy spawn rates should be applied
   */
  private EnemySettings(Preset rm) {
    this.preset = rm;
  }
  
  public Preset getPreset() {
    return preset;
  }
  
  public static class Builder {
    Preset rm;
    
    public Builder() {
      rm = Preset.NONE;
    }
    
    public Builder setRandomizeMode(Preset rm) {
      this.rm = rm;
      return this;
    }
    
    public EnemySettings build() {
      return new EnemySettings(rm);
    }
  }
}
