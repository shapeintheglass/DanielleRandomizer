package gui2;

import proto.RandomizerSettings.Settings;

public class SettingsHelper {
  public static String settingsToString(Settings settings) {
    StringBuilder builder = new StringBuilder();

    builder.append(String.format("Seed: %s\n", settings.getSeed()));

    if (settings.getCosmeticSettings().getRandomizeBodies()) {
      builder.append(String.format("\t* Randomize bodies\n"));
    }
    if (settings.getCosmeticSettings().getRandomizeVoicelines()) {
      builder.append(String.format("\t* Randomize voicelines\n"));
    }
    if (settings.getCosmeticSettings().getRandomizeEmotions()) {
      builder.append(String.format("\t* Randomize emotions\n"));
    }
    if (settings.getCosmeticSettings().getRandomizeMusic()) {
      builder.append(String.format("\t* Randomize music\n"));
    }
    if (settings.getCosmeticSettings().getRandomizePlayerModel()) {
      builder.append(String.format("\t* Randomize player model\n"));
    }
    if (settings.getCosmeticSettings().getRandomizePlanetSize()) {
      builder.append(String.format("\t* Randomize Earth/Moon/Sun\n"));
    }

    if (!settings.getGameplaySettings().getItemSpawnSettings().getFiltersList().isEmpty()) {
      builder.append(String.format("\t* %s\n", settings.getGameplaySettings().getItemSpawnSettings().getName()));
    }
    if (settings.getMoreSettings().getMoreGuns()) {
      builder.append(String.format("\t* More guns\n"));
    }
    if (settings.getGameplaySettings().getRandomizeFabPlanCosts()) {
      builder.append(String.format("\t* Randomize fab plan costs\n"));
    }

    if (!settings.getGameplaySettings().getEnemySpawnSettings().getFiltersList().isEmpty()) {
      builder.append(String.format("\t* %s\n", settings.getGameplaySettings().getEnemySpawnSettings().getName()));
    }

    if (settings.getGameplaySettings().getRandomizeNeuromods()) {
      builder.append(String.format("\t* Randomize neuromods\n"));
    }

    if (settings.getGameplaySettings().getRandomizeStation()) {
      builder.append(String.format("\t* Randomize station\n"));
    }
    if (settings.getCheatSettings().getUseCustomSpawn()) {
      builder.append(String.format("\t* Start location: %s\n", settings.getCheatSettings()
          .getCustomSpawnLocation()
          .name()));
    }

    if (settings.getGameStartSettings().getStartOnSecondDay()) {
      builder.append(String.format("\t* Start on second day\n"));
    }
    if (settings.getGameStartSettings().getAddLootToApartment()) {
      builder.append(String.format("\t* Add loot to apartment\n"));
    }
    if (settings.getGameStartSettings().getSkipJovanCutscene()) {
      builder.append(String.format("\t* Skip Jovan cutscene\n"));
    }

    if (settings.getCheatSettings().getOpenStation()) {
      builder.append(String.format("\t* Unlock all doors/safes/workstations\n"));
    }
    if (settings.getCheatSettings().getUnlockAllScans()) {
      builder.append(String.format("\t* Unlock all typhon scans\n"));
    }
    if (settings.getExpSettings().getWanderingHumans()) {
      builder.append(String.format("\t* Make humans wander\n"));
    }
    if (settings.getExpSettings().getZeroGravityEverywhere()) {
      builder.append(String.format("\t* Microgravity everywhere\n"));
    }
    if (settings.getExpSettings().getEnableGravityInExtAndGuts()) {
      builder.append(String.format("\t* Enable gravity in exterior + GUTS\n"));
    }
    if (settings.getExpSettings().getStartSelfDestruct()) {
      builder.append(String.format("\t* Start self-destruct sequence\n"));
      builder.append(String.format("\t\t* Self-destruct timer: %s\n", settings.getExpSettings()
          .getSelfDestructTimer()));
      builder.append(String.format("\t\t* Shuttle timer: %s\n", settings.getExpSettings()
          .getSelfDestructShuttleTimer()));
    }

    return builder.toString();
  }
}
