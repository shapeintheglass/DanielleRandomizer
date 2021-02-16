package utils;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class LevelConsts {
  public static final char DELIMITER = ';';

  public static final String PREFIX = "gamesdk/levels/campaign";
  public static final String[] LEVEL_DIRS = { "endgame", "engineering/cargobay", "engineering/lifesupport",
      "engineering/powersource", "executive/arboretum", "executive/bridge", "executive/corporateit",
      "executive/crewfacilities", "research/lobby", "research/prototype", "research/psychotronics",
      "research/shuttlebay", "research/simulationlabs", "research/zerog_utilitytunnels", "station/exterior" };

  public static final List<String> DO_NOT_TOUCH_ITEM_TAGS = Lists.newArrayList("Data", "MissionItems",
      "BigNullwaveTransmitterFabPlan", "AlexStationKeyFabPlan", "MorganStationKeyFabPlan", "PropulsionSystemFabPlan",
      "Psychoscope", "ZeroGSuit", "_PLOT_CRITICAL", "ArkRobots", "ArkLight", "research/simulationlabs;Weapons.Wrench1",
      "MedKit_Wall_Mounted", "ArkContainer");

  public static final List<String> DO_NOT_OUTPUT_ITEM_TAGS = Lists.newArrayList("Architecture", "Gameplay",
      "Industrial", "Space", "Light_Fixtures", "Static_ArkLights", "Shotgun_Golden", "DoubleWrench",
      "Mimic_Placeholder", "OperatorGrenade", "ShotgunPreorderFabPlan", "ArkContainer");

  public static final List<String> DO_NOT_TOUCH_NPC_TAGS = Lists.newArrayList(
      "research/psychotronics;ArkNpcSpawner_Weaver6", "research/psychotronics;ArkNpcSpawner_Weaver2",
      "research/psychotronics;ArkNpcSpawner_Weaver4", "research/lobby;ArkNpcSpawner_Technopath1",
      "research/lobby;ArkNpcSpawner_BasePhantom5", "executive/arboretum;ArkNpcSpawner_Telepath1",
      "executive/arboretum;Spawner_GreenhouseTelepath", "executive/crewfacilities;ArkNpcSpawner16",
      "executive/crewfacilities;ArkNpcSpawner_Telepath2", "engineering/lifesupport;ArkNpcSpawner_Technopath1",
      "station/exterior;ArkNpcSpawner_Telepath4", "engineering/lifesupport;ArkNpcSpawner_Telepath1");

  public static final Set<String> CAN_BE_KILLABLE_NPC_TAGS = Sets.newHashSet("research/lobby;ArkNpcSpawner_Technopath1",
      "research/lobby;ArkNpcSpawner_BasePhantom5", "engineering/lifesupport;ArkNpcSpawner_Technopath1");

  public static final List<String> KILLABLE_NPC_ARCHETYPES = Lists.newArrayList("ArkNightmare", "ArkPoltergeist",
      "Mimics.EliteMimic", "Mimics.Mimic", "Named Phantoms.ArkBeta_ArgentenoPero",
      "Named Phantoms.ArkBeta_CliveLawrence", "Named Phantoms.ArkBeta_CrispinBoyer",
      "Named Phantoms.ArkBeta_EnochKouneva", "Named Phantoms.ArkBeta_GarfieldLangley",
      "Named Phantoms.ArkBeta_HelenCombs", "Named Phantoms.ArkBeta_IvyPark", "Named Phantoms.ArkBeta_JeanFaure",
      "Named Phantoms.ArkBeta_JorgenThorstein", "Named Phantoms.ArkBeta_KirkRemmer",
      "Named Phantoms.ArkBeta_LaneCarpenter", "Named Phantoms.ArkBeta_LawrenceBaxter",
      "Named Phantoms.ArkBeta_MaliahFowles", "Named Phantoms.ArkBeta_NicoleHague",
      "Named Phantoms.ArkBeta_ReginaSellers", "Named Phantoms.ArkBeta_YuriKimura",
      "Named Phantoms.Etheric_RandolfHutchinson", "Overseers.Technopath", "Overseers.Telepath", "Overseers.Weaver",
      "Phantoms.BasePhantom", "Phantoms.EthericDoppelganger", "Phantoms.EthericPhantom", "Phantoms.ThermalPhantom",
      "Phantoms.VoltaicPhantom");

  public static final List<String> DO_NOT_OUTPUT_NPC_TAGS = Lists.newArrayList("HumanPlaceholders",
      "Tentacle_Large_Guard", "Tentacle_Medium_Guard", "Tentacle_Small_Guard", "FakeTechnopath",
      "Cystoid_IgnorePlayer");

  // Sets global quest states to unlock important areas.
  public static final ImmutableMap<String, String> UNLOCK_QUESTS_GAME_TOKENS = new ImmutableMap.Builder<String, String>()
      .put("CrewFacility.VoiceSampleProcessingPercent", "100")
      .build();

  // Intended to unlock the main lift.
  public static final ImmutableMap<String, String> UNLOCK_LIFT_GAME_TOKENS = new ImmutableMap.Builder<String, String>()
      .put("Lobby.ElevatorUsedForFirstTime", "true")
      .build();

  // Opens all exterior airlocks
  public static final ImmutableMap<String, String> UNLOCK_EXTERIOR_GAME_TOKENS = new ImmutableMap.Builder<String, String>()
      .put("AirlockStates.AirlockSealedInternally_ShuttleBay", "false")
      .put("AirlockStates.AirlockSealedInternally_HardwareLabs", "false")
      .put("AirlockStates.AirlockSealedInternally_PowerSource", "false")
      .put("AirlockStates.AirlockSealedInternally_Arboretum", "false")
      .put("AirlockStates.AirlockSealedInternally_Psychotronics", "false")
      .build();
  public static final ImmutableMap<String, String> START_2ND_DAY_GAME_TOKENS = ImmutableMap.of(
      "SimLab.Apartment_2ndDay", "true", "SimLab.Apartment_PlayerHasFirstDay2Objective", "true");
  public static final ImmutableMap<String, String> PSYCHOTRONICS_SKIP_CALIBRATION_TOKENS = ImmutableMap.of(
      "WorldState.PsychoscopeCalibrationComplete", "true");
  public static final ImmutableMap<String, String> ENABLE_NIGHTMARE_TOKENS = ImmutableMap.of(
      "Arboretum.NightmareSpawnedInArboretum", "true");
  public static final String SELF_DESTRUCT_TIMER_TOKEN_NAME = "WorldState.TalosSelfDestructTime";
  public static final ImmutableMap<String, String> START_SELF_DESTRUCT_TOKENS = ImmutableMap.of(
      "CharacterFates.DahlBrainwashed", "true", "AlexAndDahlOutcomes.DahlArrivedInShuttle", "true");
  public static final ImmutableMap<String, String> SKIP_JOVAN_TOKENS = ImmutableMap.of(
      "SimLab.Decontamination_SawMimicScene", "true");
}
