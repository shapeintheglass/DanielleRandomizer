package json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AllPresetsJson {
  private static final String ITEM_SPAWN_SETTINGS = "item_spawn_settings";

  private static final String ENEMY_SPAWN_SETTINGS = "enemy_spawn_settings";

  @JsonProperty(ENEMY_SPAWN_SETTINGS)
  private List<SpawnPresetJson> enemySpawnSettings;
  @JsonProperty(ITEM_SPAWN_SETTINGS)
  private List<SpawnPresetJson> itemSpawnSettings;

  @JsonCreator
  public AllPresetsJson(@JsonProperty(ENEMY_SPAWN_SETTINGS) List<SpawnPresetJson> enemySpawnSettings,
      @JsonProperty(ITEM_SPAWN_SETTINGS) List<SpawnPresetJson> itemSpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public AllPresetsJson(String filename) throws IOException {
    JsonNode node = new ObjectMapper().readTree(new File(filename));
    enemySpawnSettings = new ArrayList<>();
    itemSpawnSettings = new ArrayList<>();

    if (node.has(ENEMY_SPAWN_SETTINGS)) {
      node.get(ENEMY_SPAWN_SETTINGS)
          .forEach(n -> enemySpawnSettings.add(new SpawnPresetJson(n)));
    }
    if (node.has(ITEM_SPAWN_SETTINGS)) {
      node.get(ITEM_SPAWN_SETTINGS)
          .forEach(n -> itemSpawnSettings.add(new SpawnPresetJson(n)));
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
}
