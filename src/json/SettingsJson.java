package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsJson {
  private String releaseVersion;
  private String installDir;
  private long seed;
  private CosmeticSettingsJson cosmeticSettings;
  private GameplaySettingsJson gameplaySettings;

  @JsonCreator
  public SettingsJson(@JsonProperty("release_version") String releaseVersion,
      @JsonProperty("install_dir") String installDir, @JsonProperty("seed") long seed,
      @JsonProperty("cosmetic_settings") CosmeticSettingsJson cosmeticSettings,
      @JsonProperty("gameplay_settings") GameplaySettingsJson gameplaySettings) {
    this.releaseVersion = releaseVersion;
    this.installDir = installDir;
    this.seed = seed;
    this.cosmeticSettings = cosmeticSettings;
    this.gameplaySettings = gameplaySettings;
  }

  public String getReleaseVersion() {
    return releaseVersion;
  }

  public String getInstallDir() {
    return installDir;
  }

  public long getSeed() {
    return seed;
  }

  public CosmeticSettingsJson getCosmeticSettings() {
    return cosmeticSettings;
  }

  public GameplaySettingsJson getGameplaySettings() {
    return gameplaySettings;
  }
  
  public void setReleaseVersion(String releaseVersion) {
    this.releaseVersion = releaseVersion;
  }

  public void setInstallDir(String installDir) {
    this.installDir = installDir;
  }
  
  public void setSeed(long seed) {
    this.seed = seed;
  }

  public void setCosmeticSettings(CosmeticSettingsJson cosmeticSettings) {
    this.cosmeticSettings = cosmeticSettings;
  }

  public void setGameplaySettings(GameplaySettingsJson gameplaySettings) {
    this.gameplaySettings = gameplaySettings;
  }
}
