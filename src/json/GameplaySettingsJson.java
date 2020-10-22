package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameplaySettingsJson {
  private boolean randomizeLoot;
  private boolean addLootToApartment;
  private boolean openStation;
  private GenericSpawnPresetJson enemySpawnSettings;
  private GenericSpawnPresetJson itemSpawnSettings;

  @JsonCreator
  public GameplaySettingsJson(@JsonProperty("randomize_loot") boolean randomizeLoot,
      @JsonProperty("add_loot_to_apartment") boolean addLootToApartment,
      @JsonProperty("open_station") boolean openStation,
      @JsonProperty("enemy_spawn_settings") GenericSpawnPresetJson enemySpawnSettings,
      @JsonProperty("item_spawn_settings") GenericSpawnPresetJson itemSpawnSettings) {
    this.randomizeLoot = randomizeLoot;
    this.addLootToApartment = addLootToApartment;
    this.openStation = openStation;
    this.enemySpawnSettings = enemySpawnSettings;
    this.itemSpawnSettings = itemSpawnSettings;
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

  public GenericSpawnPresetJson getEnemySpawnSettings() {
    return enemySpawnSettings;
  }

  public void setEnemySpawnSettings(GenericSpawnPresetJson enemySpawnSettings) {
    this.enemySpawnSettings = enemySpawnSettings;
  }

  public GenericSpawnPresetJson getItemSpawnSettings() {
    return itemSpawnSettings;
  }

  public void setItemSpawnSettings(GenericSpawnPresetJson itemSpawnSettings) {
    this.itemSpawnSettings = itemSpawnSettings;
  }
}
