package gui2;

import proto.RandomizerSettings.Settings;

public class SettingsHelper {
  public static String settingsToString(Settings settings) {
    StringBuilder builder = new StringBuilder();

    builder.append(String.format("Seed: %s\n", settings.getSeed()));

    if (settings.getCosmeticSettings()
        .getRandomizeBodies()) {
      addOptionToBuilder(builder, "Randomize bodies");
    }
    if (settings.getCosmeticSettings()
        .getRandomizeVoicelines()) {
      addOptionToBuilder(builder, "Randomize voice lines");
    }
    if (settings.getCosmeticSettings()
        .getRandomizeMusic()) {
      addOptionToBuilder(builder, "Randomize music");
    }
    if (settings.getCosmeticSettings()
        .getRandomizePlayerModel()) {
      addOptionToBuilder(builder, "Randomize player model");
    }
    if (settings.getCosmeticSettings()
        .getRandomizePlanetSize()) {
      addOptionToBuilder(builder, "Randomize Earth/Moon/Sun");
    }

    if (!settings.getGameplaySettings()
        .getItemSpawnSettings()
        .getFiltersList()
        .isEmpty()) {
      addOptionToBuilder(builder, settings.getGameplaySettings()
          .getItemSpawnSettings()
          .getName());
    }

    if (!settings.getGameplaySettings()
        .getEnemySpawnSettings()
        .getFiltersList()
        .isEmpty()) {
      addOptionToBuilder(builder, settings.getGameplaySettings()
          .getEnemySpawnSettings()
          .getName());
    }

    if (settings.getGameplaySettings()
        .getRandomizeStation()) {
      addOptionToBuilder(builder, "Randomize station");
    }

    if (settings.getGameplaySettings()
        .getRandomizeNeuromods()) {
      addOptionToBuilder(builder, "Randomize neuromods");
    }

    if (settings.getGameplaySettings()
        .getRandomizeFabPlanCosts()) {
      addOptionToBuilder(builder, "Randomize fab plan costs");
    }

    if (settings.getGameplaySettings()
        .getRandomizeRecyclers()
        .getIsEnabled()) {
      addOptionToBuilder(builder, "Randomize recyclers");
    }

    if (settings.getGameplaySettings()
        .getRandomizeDispensers()
        .getIsEnabled()) {
      addOptionToBuilder(builder, "Randomize operator dispensers");
    }

    if (settings.getGameplaySettings()
        .getRandomizeBreakables()
        .getIsEnabled()) {
      addOptionToBuilder(builder, "Randomize breakable objects");
    }

    if (settings.getGameplaySettings()
        .getRandomizeHackables()
        .getIsEnabled()) {
      addOptionToBuilder(builder, "Randomize hackable objects");
    }

    if (settings.getGameStartSettings()
        .getAddJetpack()) {
      addOptionToBuilder(builder, "Add ARTAX Jetpack");
    }

    if (settings.getGameStartSettings()
        .getAddLootToApartment()) {
      addOptionToBuilder(builder, "Add loot to apartment");
    }

    if (settings.getGameStartSettings()
        .getAddPsychoscope()) {
      addOptionToBuilder(builder, "Add Psychoscope");
    }

    if (settings.getGameStartSettings()
        .getStartOutsideApartment()) {
      addOptionToBuilder(builder, "Start outside apartment");
    }

    if (settings.getMoreSettings()
        .getMoreGuns()) {
      addOptionToBuilder(builder, "More guns");
    }

    if (settings.getMoreSettings()
        .getPreySoulsGuns()) {
      addOptionToBuilder(builder, "Prey For Death - Guns");
    }

    if (settings.getMoreSettings()
        .getPreySoulsChipsets()) {
      addOptionToBuilder(builder, "Prey For Death - Chipsets");
    }

    if (settings.getMoreSettings()
        .getPreySoulsEnemies()) {
      addOptionToBuilder(builder, "Prey For Death - Enemies");
    }

    if (settings.getMoreSettings()
        .getPreySoulsTurrets()) {
      addOptionToBuilder(builder, "Prey For Death - Turrets");
    }



    if (settings.getCheatSettings()
        .getOpenStation()) {
      addOptionToBuilder(builder, "Unlock all doors/safes/workstations");
    }
    if (settings.getCheatSettings()
        .getUnlockAllScans()) {
      addOptionToBuilder(builder, "Unlock all typhon scans");
    }
    
    if (settings.getCheatSettings()
        .getUseCustomSpawn()) {
      addOptionToBuilder(builder, String.format("Start location: %s", settings.getCheatSettings()
          .getCustomSpawnLocation()
          .name()));
    }
    
    if (settings.getExpSettings()
        .getWanderingHumans()) {
      addOptionToBuilder(builder, "Make humans wander");
    }
    
    if (settings.getExpSettings()
        .getLivingCorpses()) {
      addOptionToBuilder(builder, "Living corpses");
    }
    
    if (settings.getExpSettings()
        .getZeroGravityEverywhere()) {
      addOptionToBuilder(builder, "Microgravity everywhere");
    }
    if (settings.getExpSettings()
        .getEnableGravityInExtAndGuts()) {
      addOptionToBuilder(builder, "Gravity in Exterior + GUTS");
    }
    if (settings.getExpSettings()
        .getStartSelfDestruct()) {
      addOptionToBuilder(builder, "Start self-destruct sequence");
      builder.append(String.format("\t\t* Self-destruct timer: %s\n", settings.getExpSettings()
          .getSelfDestructTimer()));
      builder.append(String.format("\t\t* Shuttle timer: %s\n", settings.getExpSettings()
          .getSelfDestructShuttleTimer()));
    }

    return builder.toString();
  }

  private static void addOptionToBuilder(StringBuilder builder, String text) {
    builder.append(String.format("\t* %s\n", text));
  }
}
