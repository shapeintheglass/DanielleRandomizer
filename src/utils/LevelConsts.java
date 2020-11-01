package utils;

import com.google.common.collect.ImmutableMap;

public class LevelConsts {
  public static final String PREFIX = "gamesdk/levels/campaign";
  public static final String[] LEVEL_DIRS = { "endgame", "engineering/cargobay", "engineering/lifesupport",
      "engineering/powersource", "executive/arboretum", "executive/bridge", "executive/corporateit",
      "executive/crewfacilities", "research/lobby", "research/prototype", "research/psychotronics",
      "research/shuttlebay", "research/simulationlabs", "research/zerog_utilitytunnels", "station/exterior" };
  public static final ImmutableMap<String, String> UNLOCK_STATION_GAME_TOKENS = new ImmutableMap.Builder<String, String>()
      .put("WorldState.PsychoscopeCalibrationComplete", "true")
      .put("CrewFacility.VoiceSampleProcessingPercent", "100")
      .put("AirlockStates.AirlockSealedInternally_ShuttleBay", "false")
      .put("AirlockStates.AirlockSealedInternally_HardwareLabs", "false")
      .put("AirlockStates.AirlockSealedInternally_PowerSource", "false")
      .put("AirlockStates.AirlockSealedInternally_Arboretum", "false")
      .put("AirlockStates.AirlockSealedInternally_Psychotronics", "false")
      .build();
  public static final ImmutableMap<String, String> START_2ND_DAY_GAME_TOKENS = ImmutableMap.of(
      "SimLab.Apartment_2ndDay", "true", "SimLab.Apartment_PlayerHasFirstDay2Objective", "true");
  public static final ImmutableMap<String, String> PSYCHOTRONICS_SKIP_GAME_TOKENS = ImmutableMap.of(
      "WorldState.PsychoscopeCalibrationComplete", "true");
}
