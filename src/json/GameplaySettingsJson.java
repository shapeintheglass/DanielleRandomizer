package json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import gui.BaseCheckbox;

public class GameplaySettingsJson implements HasOptions {
  public static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";
  public static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";

  public static final String GAME_TOKEN_VALUES = "game_token_values";
  private static final String DELIMITER = "=";

  public static final String RANDOMIZE_LOOT = "randomize_loot";
  public static final String ADD_LOOT_TO_APARTMENT = "add_loot_to_apartment";
  public static final String START_ON_2ND_DAY = "start_on_2nd_day";
  public static final String OPEN_STATION = "open_station";
  public static final String RANDOMIZE_NEUROMODS = "randomize_neuromods";
  public static final String UNLOCK_ALL_SCANS = "unlock_all_scans";
  public static final String RANDOMIZE_STATION = "randomize_station";

  public static final ImmutableMap<String, BaseCheckbox> ALL_OPTIONS =
      new ImmutableMap.Builder<String, BaseCheckbox>()
          .put(RANDOMIZE_LOOT,
              new BaseCheckbox("Randomize loot tables",
                  "Randomizes contents of loot tables according to item spawn settings", true))
          .put(ADD_LOOT_TO_APARTMENT,
              new BaseCheckbox("Add loot to Morgan's apartment",
                  "Adds useful equipment in containers around Morgan's apartment", true))
          .put(OPEN_STATION,
              new BaseCheckbox("Open up Talos I (WIP)",
                  "Unlocks various doors around Talos I to make traversal easier.", false))
          .put(RANDOMIZE_NEUROMODS,
              new BaseCheckbox("Randomize Neuromod upgrade tree",
                  "Shuffles the neuromods in the skill upgrade tree", false))
          .put(UNLOCK_ALL_SCANS,
              new BaseCheckbox("Unlock all neuromods scans",
                  "Removes scan requirement for all typhon neuromods", true))
          .put(RANDOMIZE_STATION,
              new BaseCheckbox("Randomize station connections",
                  "Shuffles connections between levels.", false))
          .put(START_ON_2ND_DAY, new BaseCheckbox("Start on 2nd day",
              "Skips to the 2nd day of the intro. HUD may be invisible until you open your transcribe.",
              true))
          .build();

  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private SpawnPresetJson enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private SpawnPresetJson itemSpawnSettings;

  private Map<String, String> gameTokenValues;
  private Map<String, Boolean> booleanSettings;

  @JsonCreator
  public GameplaySettingsJson(@JsonProperty(RANDOMIZE_LOOT) boolean randomizeLoot,
      @JsonProperty(ADD_LOOT_TO_APARTMENT) boolean addLootToApartment,
      @JsonProperty(OPEN_STATION) boolean openStation,
      @JsonProperty(RANDOMIZE_NEUROMODS) boolean randomizeNeuromods,
      @JsonProperty(UNLOCK_ALL_SCANS) boolean unlockScans,
      @JsonProperty(RANDOMIZE_STATION) boolean randomizeStation,
      @JsonProperty(START_ON_2ND_DAY) boolean startOnSecondDay, List<String> gameTokenValues,
      @JsonProperty(ENEMY_SPAWN_SETTINGS) SpawnPresetJson enemySpawnSettings,
      @JsonProperty(ITEM_SPAWN_SETTINGS) SpawnPresetJson itemSpawnSettings) {
    booleanSettings = new HashMap<>();
    booleanSettings.put(RANDOMIZE_LOOT, randomizeLoot);
    booleanSettings.put(ADD_LOOT_TO_APARTMENT, addLootToApartment);
    booleanSettings.put(OPEN_STATION, openStation);
    booleanSettings.put(RANDOMIZE_NEUROMODS, randomizeNeuromods);
    booleanSettings.put(UNLOCK_ALL_SCANS, unlockScans);
    booleanSettings.put(RANDOMIZE_STATION, randomizeStation);
    booleanSettings.put(START_ON_2ND_DAY, startOnSecondDay);

    setGameTokenValues(gameTokenValues);

    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GameplaySettingsJson() {
    booleanSettings = new HashMap<>();
    for (String s : ALL_OPTIONS.keySet()) {
      booleanSettings.put(s, ALL_OPTIONS.get(s).getDefaultValue());
    }
    setGameTokenValues(null);
    this.enemySpawnSettings = new SpawnPresetJson("", "", new ArrayList<>());
    this.itemSpawnSettings = new SpawnPresetJson("", "", new ArrayList<>());
  }

  public GameplaySettingsJson(JsonNode node) {
    booleanSettings = new HashMap<>();
    for (String s : ALL_OPTIONS.keySet()) {
      if (node.has(s)) {
        booleanSettings.put(s, node.get(s).asBoolean());
      }
    }

    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      this.enemySpawnSettings = new SpawnPresetJson(node.get(ENEMY_SPAWN_SETTINGS));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      this.itemSpawnSettings = new SpawnPresetJson(node.get(ITEM_SPAWN_SETTINGS));
    }
  }

  public boolean getOption(String name) {
    return booleanSettings.containsKey(name) ? booleanSettings.get(name)
        : ALL_OPTIONS.get(name).getDefaultValue();
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

  @JsonProperty(START_ON_2ND_DAY)
  public boolean getStartOn2ndDay() {
    return booleanSettings.get(START_ON_2ND_DAY);
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

  @JsonProperty(GAME_TOKEN_VALUES)
  public List<String> getGameTokenValues() {
    if (gameTokenValues == null) {
      return null;
    }

    List<String> stringValues = new ArrayList<>(gameTokenValues.size());
    for (String s : gameTokenValues.keySet()) {
      stringValues.add(String.format("%s%s%s", s, DELIMITER, gameTokenValues.get(s)));
    }
    return stringValues;
  }

  @JsonIgnore
  public Map<String, String> getGameTokenValuesAsMap() {
    return gameTokenValues;
  }

  public void setGameTokenValues(List<String> stringValues) {
    this.gameTokenValues = new HashMap<>();

    if (stringValues == null) {
      return;
    }

    for (String s : stringValues) {
      String[] tokens = s.split(DELIMITER);
      gameTokenValues.put(tokens[0].trim(), tokens[1].trim());
    }
  }
}
