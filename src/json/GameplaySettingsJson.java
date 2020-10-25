package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class GameplaySettingsJson {
  private static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";
  private static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";
  private static final String RANDOMIZE_NEUROMODS = "randomize_neuromods";
  private static final String OPEN_STATION = "open_station";
  private static final String ADD_LOOT_TO_APARTMENT = "add_loot_to_apartment";
  private static final String RANDOMIZE_LOOT = "randomize_loot";
  private static final String UNLOCK_ALL_SCANS = "unlock_all_scans";
  private static final String SEPARATE_HUMAN_AND_TYPHON_NEUROMODS = "separate_human_and_typhon_neuromods";

  @JsonProperty(RANDOMIZE_LOOT)
  private boolean randomizeLoot;
  @JsonProperty(ADD_LOOT_TO_APARTMENT)
  private boolean addLootToApartment;
  @JsonProperty(OPEN_STATION)
  private boolean openStation;
  @JsonProperty(RANDOMIZE_NEUROMODS)
  private boolean randomizeNeuromods;
  @JsonProperty(UNLOCK_ALL_SCANS)
  private boolean unlockAllScans;
  @JsonProperty(SEPARATE_HUMAN_AND_TYPHON_NEUROMODS)
  private boolean separateHumanAndTyphonNeuromods;
  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private SpawnPresetJson enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private SpawnPresetJson itemSpawnSettings;

  @JsonCreator
  public GameplaySettingsJson(@JsonProperty(RANDOMIZE_LOOT) boolean randomizeLoot,
      @JsonProperty(ADD_LOOT_TO_APARTMENT) boolean addLootToApartment, @JsonProperty(OPEN_STATION) boolean openStation,
      @JsonProperty(RANDOMIZE_NEUROMODS) boolean randomizeNeuromods, boolean unlockScans,
      @JsonProperty(ENEMY_SPAWN_SETTINGS) SpawnPresetJson enemySpawnSettings,
      @JsonProperty(ITEM_SPAWN_SETTINGS) SpawnPresetJson itemSpawnSettings) {
    this.randomizeLoot = randomizeLoot;
    this.addLootToApartment = addLootToApartment;
    this.openStation = openStation;
    this.randomizeNeuromods = randomizeNeuromods;
    this.unlockAllScans = unlockScans;
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GameplaySettingsJson(JsonNode node) {
    this.randomizeLoot = node.get(RANDOMIZE_LOOT)
                             .asBoolean();
    this.addLootToApartment = node.get(ADD_LOOT_TO_APARTMENT)
                                  .asBoolean();
    this.openStation = node.get(OPEN_STATION)
                           .asBoolean();
    this.randomizeNeuromods = node.get(RANDOMIZE_NEUROMODS)
                                  .asBoolean();
    this.unlockAllScans = node.get(UNLOCK_ALL_SCANS)
                              .asBoolean();
    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      this.enemySpawnSettings = new SpawnPresetJson(node.get(ENEMY_SPAWN_SETTINGS));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      this.itemSpawnSettings = new SpawnPresetJson(node.get(ITEM_SPAWN_SETTINGS));
    }
  }

  public boolean isRandomizeLoot() {
    return randomizeLoot;
  }

  public void setRandomizeLoot(boolean randomizeLoot) {
    this.randomizeLoot = randomizeLoot;
  }

  public void toggleRandomizeLoot() {
    randomizeLoot = !randomizeLoot;
  }

  public boolean isAddLootToApartment() {
    return addLootToApartment;
  }

  public void setAddLootToApartment(boolean addLootToApartment) {
    this.addLootToApartment = addLootToApartment;
  }

  public void toggleAddLootToApartment() {
    addLootToApartment = !addLootToApartment;
  }

  public boolean isOpenStation() {
    return openStation;
  }

  public void setOpenStation(boolean openStation) {
    this.openStation = openStation;
  }

  public void toggleOpenStation() {
    openStation = !openStation;
  }

  public SpawnPresetJson getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public void setEnemySpawnSettings(SpawnPresetJson enemySpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
  }

  public SpawnPresetJson getItemSpawnSettings() {
    return itemSpawnSettings;
  }

  public void setItemSpawnSettings(SpawnPresetJson itemSpawnSettings) {
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public boolean isRandomizeNeuromods() {
    return randomizeNeuromods;
  }

  public void setRandomizeNeuromods(boolean randomizeNeuromods) {
    this.randomizeNeuromods = randomizeNeuromods;
  }

  public void toggleRandomizeNeuromods() {
    this.randomizeNeuromods = !randomizeNeuromods;
  }

  public boolean isUnlockAllScans() {
    return unlockAllScans;
  }

  public void setUnlockAllScans(boolean unlockAllScans) {
    this.unlockAllScans = unlockAllScans;
  }

  public void toggleUnlockAllScans() {
    this.unlockAllScans = !unlockAllScans;
  }

  public boolean isSeparateHumanAndTyphonNeuromods() {
    return separateHumanAndTyphonNeuromods;
  }

  public void setSeparateHumanAndTyphonNeuromods(boolean separateHumanAndTyphonNeuromods) {
    this.separateHumanAndTyphonNeuromods = separateHumanAndTyphonNeuromods;
  }

  public void toggleSeparateHumanAndTyphonNeuromods() {
    this.separateHumanAndTyphonNeuromods = !separateHumanAndTyphonNeuromods;
  }
}
