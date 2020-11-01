package json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AllPresetsJson {
  private static final String DELIMITER = "=";
  private static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";
  private static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";
  public static final String GAME_TOKEN_VALUES = "game_token_values";

  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private List<SpawnPresetJson> enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private List<SpawnPresetJson> itemSpawnSettings;

  private Map<String, String> gameTokenValues;

  @JsonCreator
  public AllPresetsJson(@JsonProperty(ENEMY_SPAWN_SETTINGS) List<SpawnPresetJson> enemySpawnSettings,
      @JsonProperty(ITEM_SPAWN_SETTINGS) List<SpawnPresetJson> itemSpawnSettings,
      @JsonProperty(GAME_TOKEN_VALUES) List<String> gameTokenValues) {
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
    setGameTokenValues(gameTokenValues);
  }

  public AllPresetsJson(String filename) throws IOException {
    JsonNode node = new ObjectMapper().readTree(new File(filename));
    enemySpawnSettings = new ArrayList<>();
    itemSpawnSettings = new ArrayList<>();
    gameTokenValues = new HashMap<>();

    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      node.get(ENEMY_SPAWN_SETTINGS).forEach(n -> enemySpawnSettings.add(new SpawnPresetJson(n)));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      node.get(ITEM_SPAWN_SETTINGS).forEach(n -> itemSpawnSettings.add(new SpawnPresetJson(n)));
    }
    if (node.has(GAME_TOKEN_VALUES)) {
      node.get(GAME_TOKEN_VALUES).forEach(n -> {
        String[] tokens = n.textValue().split(DELIMITER);
        gameTokenValues.put(tokens[0].trim(), tokens[1].trim());
      });
    }
  }

  public List<SpawnPresetJson> getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public void setEnemySpawnSettings(List<SpawnPresetJson> enemySpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
  }

  public List<SpawnPresetJson> getItemSpawnSettings() {
    return itemSpawnSettings;
  }

  public void setItemSpawnSettings(List<SpawnPresetJson> itemSpawnSettings) {
    this.itemSpawnSettings = itemSpawnSettings;
  }

  @JsonProperty(GAME_TOKEN_VALUES)
  public List<String> getGameTokenValues() {
    List<String> stringValues = new ArrayList<>();
    for (String s : gameTokenValues.keySet()) {
      stringValues.add(String.format("%s%s%s", s, DELIMITER, gameTokenValues.get(s)));
    }
    return stringValues;
  }
  
  public Map<String, String> getGameTokenValuesAsMap() {
    return gameTokenValues;
  }

  public void setGameTokenValues(List<String> stringValues) {
    this.gameTokenValues = new HashMap<>();
    for (String s : stringValues) {
      String[] tokens = s.split(DELIMITER);
      gameTokenValues.put(tokens[0].trim(), tokens[1].trim());
    }
  }
}
