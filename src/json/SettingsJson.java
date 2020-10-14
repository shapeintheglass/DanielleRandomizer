package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsJson {
  private GenericSpawnSettingsJson[] enemySpawnSettings;
  private GenericSpawnSettingsJson[] itemSpawnSettings;

  @JsonCreator
  public SettingsJson(@JsonProperty("enemy_spawn_settings") GenericSpawnSettingsJson[] enemySpawnSettings,
      @JsonProperty("item_spawn_settings") GenericSpawnSettingsJson[] itemSpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
  }

  public GenericSpawnSettingsJson[] getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public void setEnemySpawnSettings(GenericSpawnSettingsJson[] enemySpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
  }

  public GenericSpawnSettingsJson[] getItemSpawnSettings() {
    return itemSpawnSettings;
  }

  public void setItemSpawnSettings(GenericSpawnSettingsJson[] itemSpawnSettings) {
    this.itemSpawnSettings = itemSpawnSettings;
  }
}
