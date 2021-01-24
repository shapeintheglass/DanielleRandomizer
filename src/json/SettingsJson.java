package json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SettingsJson {
  private static final String GAMEPLAY_SETTINGS = "gameplay_settings";
  private static final String COSMETIC_SETTINGS = "cosmetic_settings";
  private static final String SEED_NAME = "seed";
  private static final String INSTALL_DIR = "install_dir";
  private static final String RELEASE_VERSION = "release_version";

  @JsonProperty(RELEASE_VERSION)
  private String releaseVersion;
  @JsonProperty(INSTALL_DIR)
  private String installDir;
  @JsonProperty(SEED_NAME)
  private long seed;
  @JsonProperty(COSMETIC_SETTINGS)
  private CosmeticSettingsJson cosmeticSettings;
  @JsonProperty(GAMEPLAY_SETTINGS)
  private GameplaySettingsJson gameplaySettings;

  @JsonCreator
  public SettingsJson(@JsonProperty(RELEASE_VERSION) String releaseVersion,
      @JsonProperty(INSTALL_DIR) String installDir, @JsonProperty(SEED_NAME) long seed,
      @JsonProperty(COSMETIC_SETTINGS) CosmeticSettingsJson cosmeticSettings,
      @JsonProperty(GAMEPLAY_SETTINGS) GameplaySettingsJson gameplaySettings) {
    this.releaseVersion = releaseVersion;
    this.installDir = installDir;
    this.seed = seed;
    this.cosmeticSettings = cosmeticSettings;
    this.gameplaySettings = gameplaySettings;
  }

  public SettingsJson(String filename) throws IOException {
    // TODO: Add validation if these settings are incomplete
    JsonNode node = new ObjectMapper().readTree(new File(filename));
    if (node.has(INSTALL_DIR)) {
      this.installDir = node.get(INSTALL_DIR)
                            .textValue();
    } else {
      throw new IOException("Install dir not specified in " + filename);
    }
    if (node.has(RELEASE_VERSION)) {
      this.releaseVersion = node.get(RELEASE_VERSION)
                                .textValue();
    }
    if (node.has(SEED_NAME)) {
      this.seed = node.get(SEED_NAME)
                      .asLong();
    }
    if (node.has(COSMETIC_SETTINGS)) {
      this.cosmeticSettings = new CosmeticSettingsJson(node.get(COSMETIC_SETTINGS));
    }
    if (node.has(GAMEPLAY_SETTINGS)) {
      this.gameplaySettings = new GameplaySettingsJson(node.get(GAMEPLAY_SETTINGS));
    }
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
}
