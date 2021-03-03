package utils;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

public final class StationConnectivityConsts {

  public static enum Level {
    ARBORETUM,
    BRIDGE,
    CARGO_BAY,
    CREW_QUARTERS,
    GUTS,
    HARDWARE_LABS,
    LIFE_SUPPORT,
    LOBBY,
    NEUROMOD_DIVISION,
    POWER_PLANT,
    PSYCHOTRONICS,
    SHUTTLE_BAY,
    DEEP_STORAGE,
    EXTERIOR,
    ENDGAME,
    GENDER_SELECT
  }

  public static enum Door {
    NEUROMOD_DIVISION_LOBBY_EXIT,
    LOBBY_NEUROMOD_DIVISION_EXIT,
    LOBBY_SHUTTLE_BAY_EXIT,
    LOBBY_PSYCHOTRONICS_EXIT,
    LOBBY_ARBORETUM_EXIT,
    LOBBY_LIFE_SUPPORT_EXIT,
    PSYCHOTRONICS_LOBBY_EXIT,
    PSYCHOTRONICS_GUTS_EXIT,
    SHUTTLE_BAY_LOBBY_EXIT,
    SHUTTLE_BAY_GUTS_EXIT,
    GUTS_PSYCHOTRONICS_EXIT,
    GUTS_SHUTTLE_BAY_EXIT,
    GUTS_CARGO_BAY_EXIT,
    GUTS_ARBORETUM_EXIT,
    ARBORETUM_GUTS_EXIT,
    ARBORETUM_BRIDGE_EXIT,
    ARBORETUM_LOBBY_EXIT,
    ARBORETUM_CREW_QUARTERS_EXIT,
    ARBORETUM_DEEP_STORAGE_EXIT,
    BRIDGE_ARBORETUM_EXIT,
    CREW_QUARTERS_ARBORETUM_EXIT,
    DEEP_STORAGE_ARBORETUM_EXIT,
    CARGO_BAY_GUTS_EXIT,
    CARGO_BAY_LIFE_SUPPORT_EXIT,
    LIFE_SUPPORT_CARGO_BAY_EXIT,
    LIFE_SUPPORT_LOBBY_EXIT,
    LIFE_SUPPORT_POWER_PLANT_EXIT,
    POWER_PLANT_LIFE_SUPPORT_EXIT,
    LOBBY_HARDWARE_LABS_EXIT,
    HARDWARE_LABS_LOBBY_EXIT
  }
  
  public static final ImmutableSet<Door> LIFT_DOORS = 
      ImmutableSet.of(Door.LOBBY_ARBORETUM_EXIT, Door.LOBBY_LIFE_SUPPORT_EXIT,
      Door.LIFE_SUPPORT_LOBBY_EXIT, Door.ARBORETUM_LOBBY_EXIT);
  public static final ImmutableSet<Door> GENERAL_ACCESS_DOORS = 
      ImmutableSet.of(Door.LOBBY_SHUTTLE_BAY_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT);
  public static final ImmutableSet<Door> FUEL_STORAGE_DOORS = 
      ImmutableSet.of(Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT);

  // Map of level to level id
  public static final ImmutableBiMap<Level, String> LEVELS_TO_NAMES =
      new ImmutableBiMap.Builder<Level, String>().put(Level.ARBORETUM, "executive/arboretum")
          .put(Level.BRIDGE, "executive/bridge")
          .put(Level.CARGO_BAY, "engineering/cargobay")
          .put(Level.CREW_QUARTERS, "executive/crewfacilities")
          .put(Level.DEEP_STORAGE, "executive/corporateit")
          .put(Level.GUTS, "research/zerog_utilitytunnels")
          .put(Level.HARDWARE_LABS, "research/prototype")
          .put(Level.LIFE_SUPPORT, "engineering/lifesupport")
          .put(Level.LOBBY, "research/lobby")
          .put(Level.NEUROMOD_DIVISION, "research/simulationlabs")
          .put(Level.POWER_PLANT, "engineering/powersource")
          .put(Level.PSYCHOTRONICS, "research/psychotronics")
          .put(Level.SHUTTLE_BAY, "research/shuttlebay")
          .put(Level.EXTERIOR, "station/exterior")
          .build();

  // Map of level to associated spawn name
  public static final ImmutableBiMap<Level, String> LEVELS_TO_DESTINATIONS =
      new ImmutableBiMap.Builder<Level, String>().put(Level.ARBORETUM, "From_Arboretum")
          .put(Level.BRIDGE, "From_Bridge")
          .put(Level.CARGO_BAY, "From_CargoBay")
          .put(Level.CREW_QUARTERS, "From_CrewFacilities")
          .put(Level.DEEP_STORAGE, "From_CorporateIT")
          .put(Level.GUTS, "From_ZeroG_UtilityTunnels")
          .put(Level.HARDWARE_LABS, "From_Prototype")
          .put(Level.LIFE_SUPPORT, "From_LifeSupport")
          .put(Level.LOBBY, "From_Lobby")
          .put(Level.NEUROMOD_DIVISION, "From_Apartment")
          .put(Level.POWER_PLANT, "From_PowerSource")
          .put(Level.PSYCHOTRONICS, "From_Psychotronics")
          .put(Level.SHUTTLE_BAY, "From_ShuttleBay")
          .build();

  // Map of door to door name
  public static final ImmutableMap<Door, String> DOORS_TO_NAMES =
      new ImmutableMap.Builder<Door, String>()
          .put(Door.ARBORETUM_BRIDGE_EXIT, "Door.Door_LevelTransition_Default5")
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, "Door.Door_LevelTransition_Default8")
          .put(Door.ARBORETUM_GUTS_EXIT, "Door.Door_LevelTransition_Default3")
          .put(Door.ARBORETUM_LOBBY_EXIT, "Door.Door_LevelTransition_Default6")
          .put(Door.BRIDGE_ARBORETUM_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.CARGO_BAY_GUTS_EXIT, "Door.Door_LevelTransition_Exterior1")
          .put(Door.CARGO_BAY_LIFE_SUPPORT_EXIT, "Door.Door_LevelTransition_Default2")
          .put(Door.CREW_QUARTERS_ARBORETUM_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.DEEP_STORAGE_ARBORETUM_EXIT, "Door.Door_LevelTransition_Default2")
          .put(Door.GUTS_ARBORETUM_EXIT, "Door.Door_LevelTransition_Default3")
          .put(Door.GUTS_CARGO_BAY_EXIT, "Door.Door_LevelTransition_Exterior1")
          .put(Door.GUTS_PSYCHOTRONICS_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.GUTS_SHUTTLE_BAY_EXIT, "Door.Door_LevelTransition_Default2")
          .put(Door.HARDWARE_LABS_LOBBY_EXIT, "LevelTransitionDoor_ToLobby")
          .put(Door.LIFE_SUPPORT_CARGO_BAY_EXIT, "Door.Door_LevelTransition_Default4")
          .put(Door.LIFE_SUPPORT_LOBBY_EXIT, "Door.Door_LevelTransition_Default5")
          .put(Door.LIFE_SUPPORT_POWER_PLANT_EXIT, "Door.Door_LevelTransition_Default6")
          .put(Door.LOBBY_ARBORETUM_EXIT, "LevelTransition_Arboretum")
          .put(Door.LOBBY_HARDWARE_LABS_EXIT, "LevelTransition_Hardware")
          .put(Door.LOBBY_LIFE_SUPPORT_EXIT, "LevelTransition_LifeSupport")
          .put(Door.LOBBY_NEUROMOD_DIVISION_EXIT, "LevelTransition_SimLabs")
          .put(Door.LOBBY_PSYCHOTRONICS_EXIT, "LevelTransition_Psychotronics")
          .put(Door.LOBBY_SHUTTLE_BAY_EXIT, "LevelTransition_ShuttleBay")
          .put(Door.NEUROMOD_DIVISION_LOBBY_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.POWER_PLANT_LIFE_SUPPORT_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, "Door.Door_LevelTransition_Default2")
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, "LTDoor_ToGUTs")
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, "LTDoor_ToLobby")
          .build();

  public static final ImmutableMap<Door, String> DOORS_TO_SPAWNS =
      new ImmutableMap.Builder<Door, String>().put(Door.ARBORETUM_BRIDGE_EXIT, "SpawnFromLobby2")
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, "SpawnPoint3")
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, "SpawnPoint2")
          .put(Door.ARBORETUM_GUTS_EXIT, "SpawnFromGUTs")
          .put(Door.ARBORETUM_LOBBY_EXIT, "SpawnFromLobby")
          .put(Door.BRIDGE_ARBORETUM_EXIT, "SpawnPoint1")
          .put(Door.CARGO_BAY_GUTS_EXIT, "SpawnPoint11")
          .put(Door.CARGO_BAY_LIFE_SUPPORT_EXIT, "SpawnPoint3")
          .put(Door.CREW_QUARTERS_ARBORETUM_EXIT, "SpawnPoint1")
          .put(Door.DEEP_STORAGE_ARBORETUM_EXIT, "switch")
          .put(Door.GUTS_ARBORETUM_EXIT, "SpawnPoint2")
          .put(Door.GUTS_CARGO_BAY_EXIT, "SpawnPoint4")
          .put(Door.GUTS_PSYCHOTRONICS_EXIT, "SpawnPoint5")
          .put(Door.GUTS_SHUTTLE_BAY_EXIT, "SpawnPoint8")
          .put(Door.HARDWARE_LABS_LOBBY_EXIT, "SpawnPoint1")
          .put(Door.LIFE_SUPPORT_CARGO_BAY_EXIT, "SpawnPoint6")
          .put(Door.LIFE_SUPPORT_LOBBY_EXIT, "SpawnPoint4")
          .put(Door.LIFE_SUPPORT_POWER_PLANT_EXIT, "SpawnPoint3")
          .put(Door.LOBBY_ARBORETUM_EXIT, "SpawnPoint7")
          .put(Door.LOBBY_HARDWARE_LABS_EXIT, "SpawnPoint2")
          .put(Door.LOBBY_LIFE_SUPPORT_EXIT, "SpawnPoint9")
          .put(Door.LOBBY_NEUROMOD_DIVISION_EXIT, "SpawnPoint8")
          .put(Door.LOBBY_PSYCHOTRONICS_EXIT, "SpawnPoint4")
          .put(Door.LOBBY_SHUTTLE_BAY_EXIT, "SpawnPoint6")
          .put(Door.NEUROMOD_DIVISION_LOBBY_EXIT, "SpawnPoint5")
          .put(Door.POWER_PLANT_LIFE_SUPPORT_EXIT, "SpawnPoint1")
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, "SpawnPoint2")
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, "SpawnPoint4")
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, "SpawnPoint4")
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, "SpawnPoint3")
          .build();

  // Map of level to doors within them
  public static final ImmutableMultimap<Level, Door> LEVELS_TO_DOORS =
      new ImmutableMultimap.Builder<Level, Door>().put(Level.ARBORETUM, Door.ARBORETUM_BRIDGE_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_CREW_QUARTERS_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_DEEP_STORAGE_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_GUTS_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_LOBBY_EXIT)
          .put(Level.BRIDGE, Door.BRIDGE_ARBORETUM_EXIT)
          .put(Level.CARGO_BAY, Door.CARGO_BAY_GUTS_EXIT)
          .put(Level.CARGO_BAY, Door.CARGO_BAY_LIFE_SUPPORT_EXIT)
          .put(Level.CREW_QUARTERS, Door.CREW_QUARTERS_ARBORETUM_EXIT)
          .put(Level.DEEP_STORAGE, Door.DEEP_STORAGE_ARBORETUM_EXIT)
          .put(Level.GUTS, Door.GUTS_ARBORETUM_EXIT)
          .put(Level.GUTS, Door.GUTS_CARGO_BAY_EXIT)
          .put(Level.GUTS, Door.GUTS_PSYCHOTRONICS_EXIT)
          .put(Level.GUTS, Door.GUTS_SHUTTLE_BAY_EXIT)
          .put(Level.HARDWARE_LABS, Door.HARDWARE_LABS_LOBBY_EXIT)
          .put(Level.LIFE_SUPPORT, Door.LIFE_SUPPORT_CARGO_BAY_EXIT)
          .put(Level.LIFE_SUPPORT, Door.LIFE_SUPPORT_LOBBY_EXIT)
          .put(Level.LIFE_SUPPORT, Door.LIFE_SUPPORT_POWER_PLANT_EXIT)
          .put(Level.LOBBY, Door.LOBBY_ARBORETUM_EXIT)
          .put(Level.LOBBY, Door.LOBBY_HARDWARE_LABS_EXIT)
          .put(Level.LOBBY, Door.LOBBY_LIFE_SUPPORT_EXIT)
          .put(Level.LOBBY, Door.LOBBY_NEUROMOD_DIVISION_EXIT)
          .put(Level.LOBBY, Door.LOBBY_PSYCHOTRONICS_EXIT)
          .put(Level.LOBBY, Door.LOBBY_SHUTTLE_BAY_EXIT)
          .put(Level.NEUROMOD_DIVISION, Door.NEUROMOD_DIVISION_LOBBY_EXIT)
          .put(Level.POWER_PLANT, Door.POWER_PLANT_LIFE_SUPPORT_EXIT)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_GUTS_EXIT)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_LOBBY_EXIT)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_GUTS_EXIT)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_LOBBY_EXIT)
          .build();

  // Map of door to level mapping (old connectivity)
  public static final ImmutableBiMap<Door, Door> DEFAULT_CONNECTIVITY =
      new ImmutableBiMap.Builder<Door, Door>()
          .put(Door.ARBORETUM_BRIDGE_EXIT, Door.BRIDGE_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, Door.CREW_QUARTERS_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, Door.DEEP_STORAGE_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_GUTS_EXIT, Door.GUTS_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_LOBBY_EXIT, Door.LOBBY_ARBORETUM_EXIT)
          .put(Door.BRIDGE_ARBORETUM_EXIT, Door.ARBORETUM_BRIDGE_EXIT)
          .put(Door.CARGO_BAY_GUTS_EXIT, Door.GUTS_CARGO_BAY_EXIT)
          .put(Door.CARGO_BAY_LIFE_SUPPORT_EXIT, Door.LIFE_SUPPORT_CARGO_BAY_EXIT)
          .put(Door.CREW_QUARTERS_ARBORETUM_EXIT, Door.ARBORETUM_CREW_QUARTERS_EXIT)
          .put(Door.DEEP_STORAGE_ARBORETUM_EXIT, Door.ARBORETUM_DEEP_STORAGE_EXIT)
          .put(Door.GUTS_ARBORETUM_EXIT, Door.ARBORETUM_GUTS_EXIT)
          .put(Door.GUTS_CARGO_BAY_EXIT, Door.CARGO_BAY_GUTS_EXIT)
          .put(Door.GUTS_PSYCHOTRONICS_EXIT, Door.PSYCHOTRONICS_GUTS_EXIT)
          .put(Door.GUTS_SHUTTLE_BAY_EXIT, Door.SHUTTLE_BAY_GUTS_EXIT)
          .put(Door.HARDWARE_LABS_LOBBY_EXIT, Door.LOBBY_HARDWARE_LABS_EXIT)
          .put(Door.LIFE_SUPPORT_CARGO_BAY_EXIT, Door.CARGO_BAY_LIFE_SUPPORT_EXIT)
          .put(Door.LIFE_SUPPORT_LOBBY_EXIT, Door.LOBBY_LIFE_SUPPORT_EXIT)
          .put(Door.LIFE_SUPPORT_POWER_PLANT_EXIT, Door.POWER_PLANT_LIFE_SUPPORT_EXIT)
          .put(Door.LOBBY_ARBORETUM_EXIT, Door.ARBORETUM_LOBBY_EXIT)
          .put(Door.LOBBY_HARDWARE_LABS_EXIT, Door.HARDWARE_LABS_LOBBY_EXIT)
          .put(Door.LOBBY_LIFE_SUPPORT_EXIT, Door.LIFE_SUPPORT_LOBBY_EXIT)
          .put(Door.LOBBY_NEUROMOD_DIVISION_EXIT, Door.NEUROMOD_DIVISION_LOBBY_EXIT)
          .put(Door.LOBBY_PSYCHOTRONICS_EXIT, Door.PSYCHOTRONICS_LOBBY_EXIT)
          .put(Door.LOBBY_SHUTTLE_BAY_EXIT, Door.SHUTTLE_BAY_LOBBY_EXIT)
          .put(Door.NEUROMOD_DIVISION_LOBBY_EXIT, Door.LOBBY_NEUROMOD_DIVISION_EXIT)
          .put(Door.POWER_PLANT_LIFE_SUPPORT_EXIT, Door.LIFE_SUPPORT_POWER_PLANT_EXIT)
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, Door.GUTS_PSYCHOTRONICS_EXIT)
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT)
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT)
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, Door.LOBBY_SHUTTLE_BAY_EXIT)
          .build();

  public static final ImmutableList<Door> SINGLE_CONNECTIONS =
      ImmutableList.of(Door.BRIDGE_ARBORETUM_EXIT, Door.NEUROMOD_DIVISION_LOBBY_EXIT,
          Door.POWER_PLANT_LIFE_SUPPORT_EXIT);
  
  public static final ImmutableList<Door> LIFT_LOBBY_SIDE =
      ImmutableList.of(Door.LOBBY_LIFE_SUPPORT_EXIT, Door.LOBBY_ARBORETUM_EXIT);
  public static final ImmutableList<Door> LIFT_NOT_LOBBY_SIDE =
      ImmutableList.of(Door.ARBORETUM_LOBBY_EXIT, Door.LIFE_SUPPORT_LOBBY_EXIT);
  public static final ImmutableList<Door> APEX_LOCKED_KILL_WALL_SIDE =
      ImmutableList.of(Door.LOBBY_HARDWARE_LABS_EXIT, Door.ARBORETUM_CREW_QUARTERS_EXIT);
  public static final ImmutableList<Door> APEX_LOCKED_NO_KILL_WALL_SIDE =
      ImmutableList.of(Door.HARDWARE_LABS_LOBBY_EXIT, Door.CREW_QUARTERS_ARBORETUM_EXIT);
}
