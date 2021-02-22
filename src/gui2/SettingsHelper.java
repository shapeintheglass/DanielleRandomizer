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
    if (settings.getCosmeticSettings().getRandomizeMusic()) {
      builder.append(String.format("\t* Randomize music\n"));
    }
    if (settings.getCosmeticSettings().getRandomizePlayerModel()) {
      builder.append(String.format("\t* Randomize player model\n"));
    }

    if (!settings.getItemSettings().getItemSpawnSettings().getFiltersList().isEmpty()) {
      builder.append(String.format("\t* %s\n", settings.getItemSettings().getItemSpawnSettings().getName()));
    }
    if (settings.getItemSettings().getMoreGuns()) {
      builder.append(String.format("\t* More guns\n"));
    }
    if (settings.getItemSettings().getRandomizeFabPlanCosts()) {
      builder.append(String.format("\t* Randomize fab plan costs\n"));
    }

    if (!settings.getNpcSettings().getEnemySpawnSettings().getFiltersList().isEmpty()) {
      builder.append(String.format("\t* %s\n", settings.getNpcSettings().getEnemySpawnSettings().getName()));
    }
    if (settings.getNpcSettings().getRandomizeCystoidNests()) {
      builder.append(String.format("\t* Randomize cystoid nests\n"));
    }
    if (settings.getNpcSettings().getRandomizeNightmare()) {
      builder.append(String.format("\t* Randomize nightmare\n"));
    }
    if (settings.getNpcSettings().getRandomizeWeaverCystoids()) {
      builder.append(String.format("\t* Randomize weaver cystoids\n"));
    }

    if (settings.getNeuromodSettings().getRandomizeNeuromods()) {
      builder.append(String.format("\t* Randomize neuromods\n"));
    }

    if (settings.getStoryProgressionSettings().getRandomizeStation()) {
      builder.append(String.format("\t* Randomize station\n"));
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
    if (settings.getCheatSettings().getWanderingHumans()) {
      builder.append(String.format("\t* Make humans wander\n"));
    }
    if (settings.getCheatSettings().getZeroGravityEverywhere()) {
      builder.append(String.format("\t* Microgravity everywhere\n"));
    }
    if (settings.getCheatSettings().getEnableGravityInExtAndGuts()) {
      builder.append(String.format("\t* Enable gravity in exterior + GUTS\n"));
    }
    if (settings.getCheatSettings().getStartSelfDestruct()) {
      builder.append(String.format("\t* Start self-destruct sequence\n"));
      builder.append(String.format("\t\t* Self-destruct timer: %s\n", settings.getCheatSettings()
          .getSelfDestructTimer()));
      builder.append(String.format("\t\t* Shuttle timer: %s\n", settings.getCheatSettings()
          .getSelfDestructShuttleTimer()));
    }

    return builder.toString();
  }
}
