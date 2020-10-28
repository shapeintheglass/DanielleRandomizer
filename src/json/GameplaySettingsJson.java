package json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import gui.BaseCheckbox;

public class GameplaySettingsJson {
  public static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";
  public static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";

  public static final String RANDOMIZE_LOOT = "randomize_loot";
  public static final String ADD_LOOT_TO_APARTMENT = "add_loot_to_apartment";
  public static final String OPEN_STATION = "open_station";
  public static final String RANDOMIZE_NEUROMODS = "randomize_neuromods";
  public static final String UNLOCK_ALL_SCANS = "unlock_all_scans";
  public static final String RANDOMIZE_STATION = "randomize_station";

  public static final ImmutableMap<String, BaseCheckbox> ALL_OPTIONS = new ImmutableMap.Builder<String, BaseCheckbox>()
      .put(RANDOMIZE_LOOT, new BaseCheckbox("Randomize loot tables",
          "Randomizes contents of loot tables according to item spawn settings"))
      .put(ADD_LOOT_TO_APARTMENT, new BaseCheckbox("Add loot to Morgan's apartment",
          "Adds useful equipment in containers around Morgan's apartment"))
      .put(OPEN_STATION, new BaseCheckbox("Open up Talos I (WIP)",
          "Unlocks doors around Talos I to make traversal easier"))
      .put(RANDOMIZE_NEUROMODS, new BaseCheckbox("Randomize Neuromod upgrade tree",
          "Shuffles the neuromods in the skill upgrade tree"))
      .put(UNLOCK_ALL_SCANS, new BaseCheckbox("Unlock all neuromods scans",
          "Removes scan requirement for all typhon neuromods"))
      .put(RANDOMIZE_STATION, new BaseCheckbox("Randomize station connections", "Shuffles connections between levels"))
      .build();

  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private SpawnPresetJson enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private SpawnPresetJson itemSpawnSettings;

  private Map<String, Boolean> booleanSettings;

  @JsonCreator
  public GameplaySettingsJson(@JsonProperty(RANDOMIZE_LOOT) boolean randomizeLoot,
      @JsonProperty(ADD_LOOT_TO_APARTMENT) boolean addLootToApartment, @JsonProperty(OPEN_STATION) boolean openStation,
      @JsonProperty(RANDOMIZE_NEUROMODS) boolean randomizeNeuromods, boolean unlockScans, boolean randomizeStation,
      @JsonProperty(ENEMY_SPAWN_SETTINGS) SpawnPresetJson enemySpawnSettings,
      @JsonProperty(ITEM_SPAWN_SETTINGS) SpawnPresetJson itemSpawnSettings) {
    booleanSettings = new HashMap<>();
    booleanSettings.put(RANDOMIZE_LOOT, randomizeLoot);
    booleanSettings.put(ADD_LOOT_TO_APARTMENT, addLootToApartment);
    booleanSettings.put(OPEN_STATION, openStation);
    booleanSettings.put(RANDOMIZE_NEUROMODS, randomizeNeuromods);
    booleanSettings.put(UNLOCK_ALL_SCANS, unlockScans);
    booleanSettings.put(RANDOMIZE_STATION, randomizeStation);

    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GameplaySettingsJson(JsonNode node) {
    booleanSettings = new HashMap<>();
    booleanSettings.put(RANDOMIZE_LOOT, node.get(RANDOMIZE_LOOT).asBoolean());
    booleanSettings.put(ADD_LOOT_TO_APARTMENT, node.get(ADD_LOOT_TO_APARTMENT).asBoolean());
    booleanSettings.put(OPEN_STATION, node.get(OPEN_STATION).asBoolean());
    booleanSettings.put(RANDOMIZE_NEUROMODS, node.get(RANDOMIZE_NEUROMODS).asBoolean());
    booleanSettings.put(UNLOCK_ALL_SCANS, node.get(UNLOCK_ALL_SCANS).asBoolean());
    booleanSettings.put(RANDOMIZE_STATION, node.get(RANDOMIZE_STATION).asBoolean());

    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      this.enemySpawnSettings = new SpawnPresetJson(node.get(ENEMY_SPAWN_SETTINGS));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      this.itemSpawnSettings = new SpawnPresetJson(node.get(ITEM_SPAWN_SETTINGS));
    }
  }

  public boolean getOption(String name) {
    return booleanSettings.get(name);
  }

  public void toggleOption(String name) {
    booleanSettings.put(name, !booleanSettings.get(name));
  }

  @JsonProperty(RANDOMIZE_LOOT)
  public boolean getRandomizeLoot() {
    return booleanSettings.get(RANDOMIZE_LOOT);
  }

  @JsonProperty(ADD_LOOT_TO_APARTMENT)
  public boolean getAddLootToApartment() {
    return booleanSettings.get(ADD_LOOT_TO_APARTMENT);
  }

  @JsonProperty(OPEN_STATION)
  public boolean getOpenStation() {
    return booleanSettings.get(OPEN_STATION);
  }

  @JsonProperty(RANDOMIZE_NEUROMODS)
  public boolean getRandomizeNeuromods() {
    return booleanSettings.get(RANDOMIZE_NEUROMODS);
  }

  @JsonProperty(UNLOCK_ALL_SCANS)
  public boolean getUnlockAllScans() {
    return booleanSettings.get(UNLOCK_ALL_SCANS);
  }

  @JsonProperty(RANDOMIZE_STATION)
  public boolean getRandomizeStation() {
    return booleanSettings.get(RANDOMIZE_STATION);
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
}
