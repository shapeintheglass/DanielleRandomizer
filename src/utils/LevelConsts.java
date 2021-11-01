package utils;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class LevelConsts {
  public static final char DELIMITER = ';';

  public static final String PREFIX = "gamesdk/levels/campaign";
  public static final String[] LEVEL_DIRS = {"endgame", "engineering/cargobay",
      "engineering/lifesupport", "engineering/powersource", "executive/arboretum",
      "executive/bridge", "executive/corporateit", "executive/crewfacilities", "research/lobby",
      "research/prototype", "research/psychotronics", "research/shuttlebay",
      "research/simulationlabs", "research/zerog_utilitytunnels", "station/exterior"};
  
  public static final String LOBBY = "research/lobby";

  public static final List<String> DO_NOT_TOUCH_ITEM_TAGS =
      Lists.newArrayList("Data", "MissionItems", "_PROGRESSION", "_PLOT_CRITICAL",
          "ArkLight", "research/simulationlabs;Weapons.Wrench1", "MedKit_Wall_Mounted",
          "ArkContainer", "Light_Fixtures");

  public static final List<String> DO_NOT_OUTPUT_ITEM_TAGS = Lists.newArrayList("Architecture",
      "Gameplay", "Industrial", "Space", "Light_Fixtures", "Static_ArkLights", "Shotgun_Golden",
      "DoubleWrench", "Mimic_Placeholder", "OperatorGrenade", "ShotgunPreorderFabPlan",
      "ArkContainer", "MedKit_Wall_Mounted", "ExplosiveTank_Dynamic");

  public static final List<String> DO_NOT_TOUCH_NPC_TAGS = Lists.newArrayList(
      "research/psychotronics;ArkNpcSpawner_Weaver6",
      "research/psychotronics;ArkNpcSpawner_Weaver2",
      "research/psychotronics;ArkNpcSpawner_Weaver4", /*"research/lobby;ArkNpcSpawner_Technopath1",*/
      /*"research/lobby;ArkNpcSpawner_BasePhantom5",*/ "executive/arboretum;ArkNpcSpawner_Telepath1",
      "executive/arboretum;Spawner_GreenhouseTelepath", "executive/crewfacilities;ArkNpcSpawner16",
      "executive/crewfacilities;ArkNpcSpawner_Telepath2",
      /*"engineering/lifesupport;ArkNpcSpawner_Technopath1",*/
      "station/exterior;ArkNpcSpawner_Telepath4", "engineering/lifesupport;ArkNpcSpawner_Telepath1",
      "MilitaryOperatorCopycat", /*"FakeTechnopath",*/ "Operators\\Named", "Kaspar");

  public static final List<String> DO_NOT_OUTPUT_NPC_TAGS =
      Lists.newArrayList("Tentacle_Large_Guard", "Tentacle_Medium_Guard",
          "Tentacle_Small_Guard", "FakeTechnopath", "Cystoid_IgnorePlayer");

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
