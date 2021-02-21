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
    JsonNode node = new ObjectMapper().readTree(new File(filename));
    if (node.has(INSTALL_DIR)) {
      this.installDir = node.get(INSTALL_DIR).textValue();
    } else {
      throw new IOException("Install dir not specified in " + filename);
    }
    if (node.has(RELEASE_VERSION)) {
      this.releaseVersion = node.get(RELEASE_VERSION).textValue();
    }
    if (node.has(SEED_NAME)) {
      this.seed = node.get(SEED_NAME).asLong();
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
  
  public String toString() {
    StringBuilder s = new StringBuilder();
    boolean atLeastOneSetting = false;
    // Cosmetic
    if (this.getCosmeticSettings() != null) {
      if (this.getCosmeticSettings().getRandomizeBodies()) {
        s.append(" * Randomize bodies\n");
        atLeastOneSetting = true;
      }
      if (this.getCosmeticSettings().getRandomizeVoiceLines()) {
        s.append(" * Randomize voicelines\n");
        atLeastOneSetting = true;
      }
      if (this.getCosmeticSettings().getRandomizeMusic()) {
        s.append(" * Randomize music\n");
        atLeastOneSetting = true;
      }
      if (this.getCosmeticSettings().getRandomizePlayerModel()) {
        s.append(" * Randomize player model\n");
        atLeastOneSetting = true;
      }
    }

    if (this.getGameplaySettings() != null) {
      GameplaySettingsJson gsj = this.getGameplaySettings();

      // Item
      if (gsj.getItemSpawnSettings() != null) {
        s.append(" * " + gsj.getItemSpawnSettings().getName() + "\n");
        atLeastOneSetting = true;
      }
      if (gsj.getMoreGuns()) {
        s.append(" * More guns\n");
        atLeastOneSetting = true;
      }

      // Enemy
      if (gsj.getEnemySpawnSettings() != null) {
        s.append(" * " + gsj.getEnemySpawnSettings().getName() + "\n");
        atLeastOneSetting = true;
      }
      // Starting equipment
      if (gsj.getStartOn2ndDay()) {
        s.append(" * Start on 2nd day\n");
        atLeastOneSetting = true;
      }
      if (gsj.getAddLootToApartment()) {
        s.append(" * Add loot to Morgan's apartment\n");
        atLeastOneSetting = true;
      }
      if (gsj.getSkipJovanCutscene()) {
        s.append(" * Skip Jovan's cutscene\n");
        atLeastOneSetting = true;
      }
      // Neuromod
      if (gsj.getRandomizeNeuromods()) {
        s.append(" * Randomize neuromods\n");
        atLeastOneSetting = true;
      }
      // Story/progression
      if (gsj.getRandomizeStation()) {
        s.append(" * Randomize station\n");
        atLeastOneSetting = true;
      }
      // Cheats
      if (gsj.getOpenStation()) {
        s.append(" * Unlock all doors/safes/workstations\n");
        atLeastOneSetting = true;
      }
      if (gsj.getUnlockAllScans()) {
        s.append(" * Unlock all typhon neuromods\n");
        atLeastOneSetting = true;
      }
      if (gsj.getWanderingHumans()) {
        s.append(" * Make humans wander\n");
        atLeastOneSetting = true;
      }
      if (gsj.getDisableGravity()) {
        s.append(" * Zero gravity everywhere\n");
        atLeastOneSetting = true;
      }
      if (gsj.getDisableGravity()) {
        s.append(" * Enable gravity in Exterior/GUTS\n");
        atLeastOneSetting = true;
      }
      if (gsj.getStartSelfDestruct()) {
        s.append(" * Self destruct enabled at game start\n");
        s.append(String.format(" * Self destruct timer: %s min\n", gsj.getSelfDestructTimer()));
        s.append(String.format(" * Shuttle timer: %s min\n", gsj.getSelfDestructShuttleTimer()));
        atLeastOneSetting = true;
      }
    }

    if (!atLeastOneSetting) {
      s.append("None\n");
    }
    return s.toString();
  }
}
