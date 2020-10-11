package settings;

public class ItemSpawnSettings {
  public enum Preset {
    NONE, 
    NO_LOGIC, // Randomize with no regard for type
    ALL_WRENCHES, 
    ALL_JUNK, 
    WHISKEY_AND_CIGARS, 
    NO_ITEMS, 
    CUSTOM,
  }

  private Preset preset;

  /**
   * Describes how enemy spawn rates should be applied
   */
  private ItemSpawnSettings(Preset p) {
    this.preset = p;
  }

  public Preset getPreset() {
    return preset;
  }

  public static class Builder {
    Preset rm;

    public Builder() {
      rm = Preset.NONE;
    }

    public Builder setRandomizeMode(Preset p) {
      this.rm = p;
      return this;
    }

    public ItemSpawnSettings build() {
      return new ItemSpawnSettings(rm);
    }
  }
}
