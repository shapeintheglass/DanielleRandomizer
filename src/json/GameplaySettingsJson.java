package json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;

public class GameplaySettingsJson implements HasOptions {
  public static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";
  public static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";

  public static final String GAME_TOKEN_VALUES = "game_token_values";

  public static final String RANDOMIZE_LOOT = "randomize_loot";
  public static final String ADD_LOOT_TO_APARTMENT = "add_loot_to_apartment";
  public static final String START_ON_2ND_DAY = "start_on_2nd_day";
  public static final String OPEN_STATION = "open_station";
  public static final String RANDOMIZE_NEUROMODS = "randomize_neuromods";
  public static final String UNLOCK_ALL_SCANS = "unlock_all_scans";
  public static final String RANDOMIZE_STATION = "randomize_station";
  public static final String MORE_GUNS = "more_guns";
  public static final String WANDERING_HUMANS = "wandering_humans";
  public static final String START_SELF_DESTRUCT = "start_self_destruct";
  public static final String SELF_DESTRUCT_TIMER = "self_destruct_timer";
  public static final String SELF_DESTRUCT_SHUTTLE_TIMER = "self_destruct_shuttle_timer";

  public static final ImmutableList<String> ALL_OPTIONS = new ImmutableList.Builder<String>().add(RANDOMIZE_LOOT,
      ADD_LOOT_TO_APARTMENT, OPEN_STATION, RANDOMIZE_NEUROMODS, UNLOCK_ALL_SCANS, RANDOMIZE_STATION, START_ON_2ND_DAY,
      MORE_GUNS, WANDERING_HUMANS, START_SELF_DESTRUCT, SELF_DESTRUCT_TIMER, SELF_DESTRUCT_SHUTTLE_TIMER).build();

  private static final boolean DEFAULT_VALUE = false;
  public static final String DEFAULT_SELF_DESTRUCT_TIMER = "120.000000";
  public static final String DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER = "30.000000";

  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private SpawnPresetJson enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private SpawnPresetJson itemSpawnSettings;

  private Map<String, Boolean> booleanSettings;
  private String selfDestructTimer;
  private String selfDestructShuttleTimer;

  @JsonCreator
  public GameplaySettingsJson(@JsonProperty(RANDOMIZE_LOOT) boolean randomizeLoot,
      @JsonProperty(ADD_LOOT_TO_APARTMENT) boolean addLootToApartment, @JsonProperty(OPEN_STATION) boolean openStation,
      @JsonProperty(RANDOMIZE_NEUROMODS) boolean randomizeNeuromods,
      @JsonProperty(UNLOCK_ALL_SCANS) boolean unlockScans, @JsonProperty(RANDOMIZE_STATION) boolean randomizeStation,
      @JsonProperty(START_ON_2ND_DAY) boolean startOnSecondDay, @JsonProperty(MORE_GUNS) boolean moreGuns,
      @JsonProperty(WANDERING_HUMANS) boolean wanderingHumans,
      @JsonProperty(START_SELF_DESTRUCT) boolean startSelfDestruct,
      @JsonProperty(SELF_DESTRUCT_TIMER) String selfDestructTimer,
      @JsonProperty(SELF_DESTRUCT_SHUTTLE_TIMER) String selfDestructShuttleTimer,
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
    booleanSettings.put(MORE_GUNS, moreGuns);
    booleanSettings.put(WANDERING_HUMANS, wanderingHumans);
    booleanSettings.put(START_SELF_DESTRUCT, startSelfDestruct);
    float selfDestructTimerFloat = Float.parseFloat(selfDestructTimer);
    this.selfDestructTimer = String.format("%.6f", selfDestructTimerFloat);
    float selfDestructShuttleTimerFloat = Float.parseFloat(selfDestructShuttleTimer);
    this.selfDestructShuttleTimer = String.format("%.6f", selfDestructShuttleTimerFloat);

    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GameplaySettingsJson(SpawnPresetJson enemySpawnSettings, SpawnPresetJson itemSpawnSettings) {
    booleanSettings = new HashMap<>();
    for (String s : ALL_OPTIONS) {
      booleanSettings.put(s, DEFAULT_VALUE);
    }
    selfDestructTimer = DEFAULT_SELF_DESTRUCT_TIMER;
    selfDestructShuttleTimer = DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER;
    this.enemySpawnSettings = enemySpawnSettings == null ? new SpawnPresetJson("No NPC randomization",
        "Do not randomize items.", new ArrayList<>()) : enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings == null ? new SpawnPresetJson("No item randomization",
        "Do not randomize NPCs.", new ArrayList<>()) : itemSpawnSettings;
  }

  public GameplaySettingsJson(JsonNode node) {
    booleanSettings = new HashMap<>();
    for (String s : ALL_OPTIONS) {
      if (node.has(s)) {
        booleanSettings.put(s, node.get(s).asBoolean());
      }
    }
    this.selfDestructTimer = node.get(SELF_DESTRUCT_TIMER).asText();
    this.selfDestructShuttleTimer = node.get(SELF_DESTRUCT_SHUTTLE_TIMER).asText();

    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      this.enemySpawnSettings = new SpawnPresetJson(node.get(ENEMY_SPAWN_SETTINGS));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      this.itemSpawnSettings = new SpawnPresetJson(node.get(ITEM_SPAWN_SETTINGS));
    }
  }

  public boolean getOption(String name) {
    return booleanSettings.containsKey(name) ? booleanSettings.get(name) : DEFAULT_VALUE;
  }

  public void toggleOption(String name) {
    booleanSettings.put(name, !booleanSettings.get(name));
  }

  @JsonProperty(RANDOMIZE_LOOT)
  public boolean getRandomizeLoot() {
    return getOption(RANDOMIZE_LOOT);
  }

  @JsonProperty(ADD_LOOT_TO_APARTMENT)
  public boolean getAddLootToApartment() {
    return getOption(ADD_LOOT_TO_APARTMENT);
  }

  @JsonProperty(OPEN_STATION)
  public boolean getOpenStation() {
    return getOption(OPEN_STATION);
  }

  @JsonProperty(RANDOMIZE_NEUROMODS)
  public boolean getRandomizeNeuromods() {
    return getOption(RANDOMIZE_NEUROMODS);
  }

  @JsonProperty(UNLOCK_ALL_SCANS)
  public boolean getUnlockAllScans() {
    return getOption(UNLOCK_ALL_SCANS);
  }

  @JsonProperty(RANDOMIZE_STATION)
  public boolean getRandomizeStation() {
    return getOption(RANDOMIZE_STATION);
  }

  @JsonProperty(START_ON_2ND_DAY)
  public boolean getStartOn2ndDay() {
    return getOption(START_ON_2ND_DAY);
  }

  @JsonProperty(MORE_GUNS)
  public boolean getMoreGuns() {
    return getOption(MORE_GUNS);
  }

  @JsonProperty(WANDERING_HUMANS)
  public boolean getWanderingHumans() {
    return getOption(WANDERING_HUMANS);
  }

  @JsonProperty(START_SELF_DESTRUCT)
  public boolean getStartSelfDestruct() {
    return getOption(START_SELF_DESTRUCT);
  }

  @JsonProperty(SELF_DESTRUCT_TIMER)
  public String getSelfDestructTimer() {
    return this.selfDestructTimer;
  }

  @JsonProperty(SELF_DESTRUCT_SHUTTLE_TIMER)
  public String getSelfDestructShuttleTimer() {
    return this.selfDestructShuttleTimer;
  }

  public SpawnPresetJson getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public SpawnPresetJson getItemSpawnSettings() {
    return itemSpawnSettings;
  }
}
