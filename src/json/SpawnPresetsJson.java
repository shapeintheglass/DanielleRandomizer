package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpawnPresetsJson {
  private GenericSpawnPresetJson[] enemySpawnSettings;
  private GenericSpawnPresetJson[] itemSpawnSettings;

  @JsonCreator
  public SpawnPresetsJson(@JsonProperty("enemy_spawn_settings") GenericSpawnPresetJson[] enemySpawnSettings,
      @JsonProperty("item_spawn_settings") GenericSpawnPresetJson[] itemSpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GenericSpawnPresetJson[] getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public void setEnemySpawnSettings(GenericSpawnPresetJson[] enemySpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
  }

  public GenericSpawnPresetJson[] getItemSpawnSettings() {
    return itemSpawnSettings;
  }

  public void setItemSpawnSettings(GenericSpawnPresetJson[] itemSpawnSettings) {
    this.itemSpawnSettings = itemSpawnSettings;
  }
}
