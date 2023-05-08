package utils;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class LevelConsts {
  public static final char DELIMITER = ';';

  public static final String PREFIX = "gamesdk/levels/campaign";

  public static final String NEUROMOD_DIVISION = "research/simulationlabs";
  public static final String LOBBY = "research/lobby";
  public static final String HARDWARE_LABS = "research/prototype";
  public static final String SHUTTLE_BAY = "research/shuttlebay";
  public static final String PSYCHOTRONICS = "research/psychotronics";
  public static final String GUTS = "research/zerog_utilitytunnels";
  public static final String ARBORETUM = "executive/arboretum";
  public static final String CREW_QUARTERS = "executive/crewfacilities";
  public static final String DEEP_STORAGE = "executive/corporateit";
  public static final String BRIDGE = "executive/bridge";
  public static final String CARGO_BAY = "engineering/cargobay";
  public static final String LIFE_SUPPORT = "engineering/lifesupport";
  public static final String POWER_PLANT = "engineering/powersource";
  public static final String EXTERIOR = "station/exterior";
  public static final String ENDGAME = "endgame";
  public static final String PLAYER_GENDER_SELECT = "playergenderselect";

  public static final String[] LEVEL_DIRS =
      {"endgame", "engineering/cargobay", "engineering/lifesupport", "engineering/powersource",
          "executive/arboretum", "executive/bridge", "executive/corporateit",
          "executive/crewfacilities", "research/lobby", "research/prototype",
          "research/psychotronics", "research/shuttlebay", "research/simulationlabs",
          "research/zerog_utilitytunnels", "station/exterior", "playergenderselect"};

  public static final List<String> DO_NOT_TOUCH_ITEM_TAGS =
      Lists.newArrayList("Data", "MissionItems", "_PROGRESSION", "_PLOT_CRITICAL", "ArkLight",
          "research/simulationlabs;Weapons.Wrench1", "ArkContainer", "Light_Fixtures",
          "ArkFruitTree", "TurretFabPlan", "NB_OxygenFuse", "ArkExplosiveTank",
          "executive/arboretum;Office.Desk_Plant2");

  public static final List<String> DO_NOT_OUTPUT_ITEM_TAGS = Lists.newArrayList("Architecture",
      "Gameplay", "Industrial", "Space", "Light_Fixtures", "Static_ArkLights", "Shotgun_Golden",
      "DoubleWrench", "Mimic_Placeholder", "OperatorGrenade", "ShotgunPreorderFabPlan",
      "ArkContainer", "MedKit_Wall_Mounted", "ExplosiveTank_Dynamic");

  // Hard-coded list of tags that must never be randomized to avoid softlocking the game.
  public static final List<String> DO_NOT_TOUCH_NPC_TAGS = Lists.newArrayList(
      // Changing the weavers in Psychotronics will softlock the game at live extraction.
      "research/psychotronics;ArkNpcSpawner_Weaver6",
      "research/psychotronics;ArkNpcSpawner_Weaver2",
      "research/psychotronics;ArkNpcSpawner_Weaver4",
      "executive/arboretum;ArkNpcSpawner_Telepath1",
      "executive/arboretum;Spawner_GreenhouseTelepath",
      // Changing the telepaths in Crew Quarters will softlock the cook's sidequest. 
      "executive/crewfacilities;ArkNpcSpawner16",
      "executive/crewfacilities;ArkNpcSpawner_Telepath2",
      "station/exterior;ArkNpcSpawner_Telepath4",
      "engineering/lifesupport;ArkNpcSpawner_Telepath1",
      "MilitaryOperatorCopycat",
      "Operators\\Named",
      "Kaspar",
      "engineering/cargobay;ArkNpcSpawner_Weaver3",
      "engineering/cargobay;ArkNpcSpawner_Weaver",
      "engineering/cargobay;ArkNpcSpawner_Weaver5",
      // The lift technopath and phantom
      "research/lobby;ArkNpcSpawner_Technopath1",
      "research/lobby;ArkNpcSpawner_BasePhantom5",
      // Life support technopath
      "FakeTechnopath",
      "engineering/lifesupport;ArkNpcSpawner_Technopath1");

  // Hard-coded list of tags that must never be spawned to avoid causing issues with gameplay
  public static final List<String> DO_NOT_OUTPUT_NPC_TAGS =
      Lists.newArrayList("Tentacle_Large_Guard", "Tentacle_Medium_Guard", "Tentacle_Small_Guard",
          "FakeTechnopath",                                                                                                                                                                                                                                                                                                                                                                                                                                                                 "Cystoid_IgnorePlayer", "EthericDoppelganger", "MilitaryOperatorFriendly");

  // Sets global quest states to unlock important areas.
  public static final ImmutableMap<String, String> UNLOCK_QUESTS_GAME_TOKENS =
      new ImmutableMap.Builder<String, String>()
          .put("CrewFacility.VoiceSampleProcessingPercent", "100")
          .build();

  // Opens all exterior airlocks
  public static final ImmutableMap<String, String> UNLOCK_EXTERIOR_GAME_TOKENS =
      new ImmutableMap.Builder<String, String>()
          .put("AirlockStates.AirlockSealedInternally_ShuttleBay", "false")
          .put("AirlockStates.AirlockSealedInternally_HardwareLabs", "false")
          .put("AirlockStates.AirlockSealedInternally_PowerSource", "false")
          .put("AirlockStates.AirlockSealedInternally_Arboretum", "false")
          .put("AirlockStates.AirlockSealedInternally_Psychotronics", "false")
          .build();
  public static final ImmutableMap<String, String> PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS =
      ImmutableMap.of("WorldState.PsychoscopeCalibrationComplete", "true");
  public static final ImmutableMap<String, String> ENABLE_NIGHTMARE_TOKENS =
      ImmutableMap.of("Arboretum.NightmareSpawnedInArboretum", "true");
  public static final String SELF_DESTRUCT_TIMER_TOKEN_NAME = "WorldState.TalosSelfDestructTime";
  public static final ImmutableMap<String, String> START_SELF_DESTRUCT_TOKENS = ImmutableMap.of(
      "CharacterFates.DahlBrainwashed", "true", "AlexAndDahlOutcomes.DahlArrivedInShuttle", "true");
  public static final ImmutableMap<String, String> SKIP_JOVAN_TOKENS =
      ImmutableMap.of("SimLab.Decontamination_SawMimicScene", "true");
  public static final ImmutableMap<String, String> ALLOW_GAME_SAVE_TOKENS =
      ImmutableMap.of("WorldState.AllowSaveGame", "true");
  public static final ImmutableMap<String, String> START_OUTSIDE_APARTMENT_TOKENS =
      ImmutableMap.of("LevelTransition.LevelTransitionDestination", "From_Lobby");
  // Note: These don't work
  public static final ImmutableMap<String, String> UNLOCK_LIFT_TOKENS =
      ImmutableMap.of("Lobby.TechnoPathDeadForElevator", "true", "Lobby.LiftAtTop", "true");
 
}
