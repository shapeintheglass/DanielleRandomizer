package utils;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import utils.ProgressionConsts.ProgressionNode;

public final class StationConnectivityConsts {

  public static enum Level implements ProgressionNode {
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

  public static enum Door implements ProgressionNode {
    NEUROMOD_DIVISION_LOBBY_EXIT,
    LOBBY_NEUROMOD_DIVISION_EXIT,
    LOBBY_HARDWARE_LABS_EXIT,
    LOBBY_SHUTTLE_BAY_EXIT,
    LOBBY_PSYCHOTRONICS_EXIT,
    LOBBY_ARBORETUM_EXIT,
    LOBBY_LIFE_SUPPORT_EXIT,
    PSYCHOTRONICS_LOBBY_EXIT,
    PSYCHOTRONICS_GUTS_EXIT,
    PSYCHOTRONICS_AIRLOCK,
    SHUTTLE_BAY_LOBBY_EXIT,
    SHUTTLE_BAY_GUTS_EXIT,
    SHUTTLE_BAY_AIRLOCK,
    GUTS_PSYCHOTRONICS_EXIT,
    GUTS_SHUTTLE_BAY_EXIT,
    GUTS_CARGO_BAY_EXIT,
    GUTS_ARBORETUM_EXIT,
    ARBORETUM_GUTS_EXIT,
    ARBORETUM_BRIDGE_EXIT,
    ARBORETUM_LOBBY_EXIT,
    ARBORETUM_CREW_QUARTERS_EXIT,
    ARBORETUM_DEEP_STORAGE_EXIT,
    ARBORETUM_AIRLOCK,
    BRIDGE_ARBORETUM_EXIT,
    CREW_QUARTERS_ARBORETUM_EXIT,
    DEEP_STORAGE_ARBORETUM_EXIT,
    CARGO_BAY_GUTS_EXIT,
    CARGO_BAY_LIFE_SUPPORT_EXIT,
    LIFE_SUPPORT_CARGO_BAY_EXIT,
    LIFE_SUPPORT_LOBBY_EXIT,
    LIFE_SUPPORT_POWER_PLANT_EXIT,
    POWER_PLANT_LIFE_SUPPORT_EXIT,
    POWER_PLANT_AIRLOCK,
    HARDWARE_LABS_LOBBY_EXIT,
    HARDWARE_LABS_AIRLOCK,
    EXTERIOR_HARDWARE_LABS_AIRLOCK,
    EXTERIOR_PSYCHOTRONICS_AIRLOCK,
    EXTERIOR_SHUTTLE_BAY_AIRLOCK,
    EXTERIOR_ARBORETUM_AIRLOCK,
    EXTERIOR_POWER_PLANT_AIRLOCK
  }

  public static final ImmutableSet<Door> LIFT_DOORS =
      ImmutableSet.of(Door.LOBBY_ARBORETUM_EXIT, Door.LOBBY_LIFE_SUPPORT_EXIT);
  public static final ImmutableSet<Door> DEEP_STORAGE_DOORS =
      ImmutableSet.of(Door.ARBORETUM_DEEP_STORAGE_EXIT);
  public static final ImmutableSet<Door> GENERAL_ACCESS_DOORS =
      ImmutableSet.of(Door.LOBBY_SHUTTLE_BAY_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT);
  public static final ImmutableSet<Door> FUEL_STORAGE_DOORS =
      ImmutableSet.of(Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT);
  public static final ImmutableSet<Door> CREW_QUARTERS_DOORS =
      ImmutableSet.of(Door.ARBORETUM_CREW_QUARTERS_EXIT);
  public static final ImmutableSet<Door> AIRLOCKS =
      ImmutableSet.of(Door.ARBORETUM_AIRLOCK, Door.HARDWARE_LABS_AIRLOCK, Door.POWER_PLANT_AIRLOCK,
          Door.PSYCHOTRONICS_AIRLOCK, Door.SHUTTLE_BAY_AIRLOCK, Door.EXTERIOR_ARBORETUM_AIRLOCK,
          Door.EXTERIOR_HARDWARE_LABS_AIRLOCK, Door.EXTERIOR_POWER_PLANT_AIRLOCK,
          Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK, Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK);
  // Doors that lock in one direction and should likely not connect to each other.
  public static final ImmutableSet<Door> ONE_WAY_LOCKS = ImmutableSet.of(
      Door.ARBORETUM_CREW_QUARTERS_EXIT, Door.ARBORETUM_DEEP_STORAGE_EXIT,
      Door.DEEP_STORAGE_ARBORETUM_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT, Door.LOBBY_SHUTTLE_BAY_EXIT,
      Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT, Door.LOBBY_ARBORETUM_EXIT,
      Door.LOBBY_LIFE_SUPPORT_EXIT, Door.LIFE_SUPPORT_POWER_PLANT_EXIT);
  // These doors cannot connect to each other due to only being a single exit
  public static final ImmutableList<Door> SINGLE_CONNECTIONS =
      ImmutableList.of(Door.BRIDGE_ARBORETUM_EXIT, Door.NEUROMOD_DIVISION_LOBBY_EXIT,
          Door.POWER_PLANT_LIFE_SUPPORT_EXIT, Door.DEEP_STORAGE_ARBORETUM_EXIT,
          Door.HARDWARE_LABS_LOBBY_EXIT, Door.CREW_QUARTERS_ARBORETUM_EXIT,
          Door.CARGO_BAY_LIFE_SUPPORT_EXIT);

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
          .put(Level.GENDER_SELECT, "playergenderselect")
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
          .put(Level.EXTERIOR, "From_Exterior")
          .build();

  // Map of door to door name
  public static final ImmutableMap<Door, String> DOORS_TO_NAMES =
      new ImmutableMap.Builder<Door, String>()
          .put(Door.ARBORETUM_BRIDGE_EXIT, "Door.Door_LevelTransition_Default5")
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, "Door.Door_LevelTransition_Default8")
          .put(Door.ARBORETUM_GUTS_EXIT, "Door.Door_LevelTransition_Default3")
          .put(Door.ARBORETUM_LOBBY_EXIT, "Door.Door_LevelTransition_Default6")
          .put(Door.ARBORETUM_AIRLOCK, "Door.Door_LevelTransition_Exterior2")
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
          .put(Door.HARDWARE_LABS_AIRLOCK, "Door.Door_LevelTransition_Exterior2")
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
          .put(Door.POWER_PLANT_AIRLOCK, "Door.Door_LevelTransition_Exterior2")
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, "Door.Door_LevelTransition_Default2")
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, "Door.Door_LevelTransition_Default1")
          .put(Door.PSYCHOTRONICS_AIRLOCK, "Door.Door_LevelTransition_Exterior4")
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, "LTDoor_ToGUTs")
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, "LTDoor_ToLobby")
          .put(Door.SHUTTLE_BAY_AIRLOCK, "Door.Door_LevelTransition_Exterior3")
          .put(Door.EXTERIOR_ARBORETUM_AIRLOCK, "Door.Door_Transition_Exterior_Arboretum")
          .put(Door.EXTERIOR_HARDWARE_LABS_AIRLOCK, "Door.Door_Transition_Exterior_HardwareLabs")
          .put(Door.EXTERIOR_POWER_PLANT_AIRLOCK, "Door.Door_Transition_Exterior_PowerPlant")
          .put(Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK, "Door.Door_Transition_Exterior_Psychotronics")
          .put(Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK, "Door.Door_Transition_Exterior_ShuttleBay")
          .build();

  public static final ImmutableMap<Level, Door> DEFAULT_SPAWN =
      new ImmutableMap.Builder<Level, Door>().put(Level.ARBORETUM, Door.ARBORETUM_GUTS_EXIT)
          .put(Level.BRIDGE, Door.BRIDGE_ARBORETUM_EXIT)
          .put(Level.CARGO_BAY, Door.CARGO_BAY_LIFE_SUPPORT_EXIT)
          .put(Level.CREW_QUARTERS, Door.CREW_QUARTERS_ARBORETUM_EXIT)
          .put(Level.DEEP_STORAGE, Door.DEEP_STORAGE_ARBORETUM_EXIT)
          .put(Level.GUTS, Door.GUTS_PSYCHOTRONICS_EXIT)
          .put(Level.HARDWARE_LABS, Door.HARDWARE_LABS_LOBBY_EXIT)
          .put(Level.LIFE_SUPPORT, Door.LIFE_SUPPORT_CARGO_BAY_EXIT)
          .put(Level.LOBBY, Door.LOBBY_NEUROMOD_DIVISION_EXIT)
          .put(Level.NEUROMOD_DIVISION, Door.NEUROMOD_DIVISION_LOBBY_EXIT)
          .put(Level.POWER_PLANT, Door.POWER_PLANT_LIFE_SUPPORT_EXIT)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_LOBBY_EXIT)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_LOBBY_EXIT)
          .build();

  public static final ImmutableMap<Door, String> DOORS_TO_SPAWNS =
      new ImmutableMap.Builder<Door, String>().put(Door.ARBORETUM_BRIDGE_EXIT, "SpawnFromLobby2")
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, "SpawnPoint3")
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, "SpawnPoint2")
          .put(Door.ARBORETUM_GUTS_EXIT, "SpawnFromGUTs")
          .put(Door.ARBORETUM_LOBBY_EXIT, "SpawnFromLobby")
          .put(Door.ARBORETUM_AIRLOCK, "SpawnFromExterior")
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
          .put(Door.HARDWARE_LABS_AIRLOCK, "SpawnPoint2")
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
          .put(Door.POWER_PLANT_AIRLOCK, "SpawnPoint2")
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, "SpawnPoint2")
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, "SpawnPoint4")
          .put(Door.PSYCHOTRONICS_AIRLOCK, "SpawnPoint3")
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, "SpawnPoint4")
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, "SpawnPoint3")
          .put(Door.SHUTTLE_BAY_AIRLOCK, "SpawnPoint5")
          .put(Door.EXTERIOR_ARBORETUM_AIRLOCK, "SpawnFromArboretum")
          .put(Door.EXTERIOR_HARDWARE_LABS_AIRLOCK, "SpawnFromHardware")
          .put(Door.EXTERIOR_POWER_PLANT_AIRLOCK, "SpawnFromPowerPlant")
          .put(Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK, "SpawnFromPsychotronics")
          .put(Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK, "SpawnFromShuttle")
          .build();

  // Map of level to doors within them
  public static final ImmutableMultimap<Level, Door> LEVELS_TO_DOORS =
      new ImmutableMultimap.Builder<Level, Door>().put(Level.ARBORETUM, Door.ARBORETUM_BRIDGE_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_CREW_QUARTERS_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_DEEP_STORAGE_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_GUTS_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_LOBBY_EXIT)
          .put(Level.ARBORETUM, Door.ARBORETUM_AIRLOCK)
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
          .put(Level.HARDWARE_LABS, Door.HARDWARE_LABS_AIRLOCK)
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
          .put(Level.POWER_PLANT, Door.POWER_PLANT_AIRLOCK)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_GUTS_EXIT)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_LOBBY_EXIT)
          .put(Level.PSYCHOTRONICS, Door.PSYCHOTRONICS_AIRLOCK)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_GUTS_EXIT)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_LOBBY_EXIT)
          .put(Level.SHUTTLE_BAY, Door.SHUTTLE_BAY_AIRLOCK)
          .put(Level.EXTERIOR, Door.EXTERIOR_ARBORETUM_AIRLOCK)
          .put(Level.EXTERIOR, Door.EXTERIOR_HARDWARE_LABS_AIRLOCK)
          .put(Level.EXTERIOR, Door.EXTERIOR_POWER_PLANT_AIRLOCK)
          .put(Level.EXTERIOR, Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK)
          .put(Level.EXTERIOR, Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK)
          .build();

  // Map of level to doors they lead from
  public static final ImmutableMultimap<Level, Door> LEVELS_TO_CONNECTING_DOORS =
     new ImmutableMultimap.Builder<Level, Door>().put(Level.ARBORETUM, Door.BRIDGE_ARBORETUM_EXIT)
         .put(Level.ARBORETUM, Door.CREW_QUARTERS_ARBORETUM_EXIT)
         .put(Level.ARBORETUM, Door.DEEP_STORAGE_ARBORETUM_EXIT)
         .put(Level.ARBORETUM, Door.GUTS_ARBORETUM_EXIT)
         .put(Level.ARBORETUM, Door.LOBBY_ARBORETUM_EXIT)
         .put(Level.ARBORETUM, Door.EXTERIOR_ARBORETUM_AIRLOCK)
         .put(Level.BRIDGE, Door.ARBORETUM_BRIDGE_EXIT)
         .put(Level.CARGO_BAY, Door.GUTS_CARGO_BAY_EXIT)
         .put(Level.CARGO_BAY, Door.LIFE_SUPPORT_CARGO_BAY_EXIT)
         .put(Level.CREW_QUARTERS, Door.ARBORETUM_CREW_QUARTERS_EXIT)
         .put(Level.DEEP_STORAGE, Door.ARBORETUM_DEEP_STORAGE_EXIT)
         .put(Level.GUTS, Door.ARBORETUM_GUTS_EXIT)
         .put(Level.GUTS, Door.CARGO_BAY_GUTS_EXIT)
         .put(Level.GUTS, Door.PSYCHOTRONICS_GUTS_EXIT)
         .put(Level.GUTS, Door.SHUTTLE_BAY_GUTS_EXIT)
         .put(Level.HARDWARE_LABS, Door.LOBBY_HARDWARE_LABS_EXIT)
         .put(Level.HARDWARE_LABS, Door.EXTERIOR_HARDWARE_LABS_AIRLOCK)
         .put(Level.LIFE_SUPPORT, Door.CARGO_BAY_LIFE_SUPPORT_EXIT)
         .put(Level.LIFE_SUPPORT, Door.LOBBY_LIFE_SUPPORT_EXIT)
         .put(Level.LIFE_SUPPORT, Door.POWER_PLANT_LIFE_SUPPORT_EXIT)
         .put(Level.LOBBY, Door.ARBORETUM_LOBBY_EXIT)
         .put(Level.LOBBY, Door.HARDWARE_LABS_LOBBY_EXIT)
         .put(Level.LOBBY, Door.LIFE_SUPPORT_LOBBY_EXIT)
         .put(Level.LOBBY, Door.NEUROMOD_DIVISION_LOBBY_EXIT)
         .put(Level.LOBBY, Door.PSYCHOTRONICS_LOBBY_EXIT)
         .put(Level.LOBBY, Door.SHUTTLE_BAY_LOBBY_EXIT)
         .put(Level.NEUROMOD_DIVISION, Door.LOBBY_NEUROMOD_DIVISION_EXIT)
         .put(Level.POWER_PLANT, Door.LIFE_SUPPORT_POWER_PLANT_EXIT)
         .put(Level.POWER_PLANT, Door.EXTERIOR_POWER_PLANT_AIRLOCK)
         .put(Level.PSYCHOTRONICS, Door.GUTS_PSYCHOTRONICS_EXIT)
         .put(Level.PSYCHOTRONICS, Door.LOBBY_PSYCHOTRONICS_EXIT)
         .put(Level.PSYCHOTRONICS, Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK)
         .put(Level.SHUTTLE_BAY, Door.GUTS_SHUTTLE_BAY_EXIT)
         .put(Level.SHUTTLE_BAY, Door.LOBBY_SHUTTLE_BAY_EXIT)
         .put(Level.SHUTTLE_BAY, Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK)
         .put(Level.EXTERIOR, Door.ARBORETUM_AIRLOCK)
         .put(Level.EXTERIOR, Door.HARDWARE_LABS_AIRLOCK)
         .put(Level.EXTERIOR, Door.POWER_PLANT_AIRLOCK)
         .put(Level.EXTERIOR, Door.PSYCHOTRONICS_AIRLOCK)
         .put(Level.EXTERIOR, Door.SHUTTLE_BAY_AIRLOCK)
         .build();

  // Map of door to level mapping (old connectivity)
  public static final ImmutableBiMap<Door, Door> DEFAULT_CONNECTIVITY =
      new ImmutableBiMap.Builder<Door, Door>()
          .put(Door.ARBORETUM_BRIDGE_EXIT, Door.BRIDGE_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_CREW_QUARTERS_EXIT, Door.CREW_QUARTERS_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_DEEP_STORAGE_EXIT, Door.DEEP_STORAGE_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_GUTS_EXIT, Door.GUTS_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_LOBBY_EXIT, Door.LOBBY_ARBORETUM_EXIT)
          .put(Door.ARBORETUM_AIRLOCK, Door.EXTERIOR_ARBORETUM_AIRLOCK)
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
          .put(Door.HARDWARE_LABS_AIRLOCK, Door.EXTERIOR_HARDWARE_LABS_AIRLOCK)
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
          .put(Door.POWER_PLANT_AIRLOCK, Door.EXTERIOR_POWER_PLANT_AIRLOCK)
          .put(Door.PSYCHOTRONICS_GUTS_EXIT, Door.GUTS_PSYCHOTRONICS_EXIT)
          .put(Door.PSYCHOTRONICS_LOBBY_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT)
          .put(Door.PSYCHOTRONICS_AIRLOCK, Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK)
          .put(Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT)
          .put(Door.SHUTTLE_BAY_LOBBY_EXIT, Door.LOBBY_SHUTTLE_BAY_EXIT)
          .put(Door.SHUTTLE_BAY_AIRLOCK, Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK)
          .put(Door.EXTERIOR_ARBORETUM_AIRLOCK, Door.ARBORETUM_AIRLOCK)
          .put(Door.EXTERIOR_HARDWARE_LABS_AIRLOCK, Door.HARDWARE_LABS_AIRLOCK)
          .put(Door.EXTERIOR_POWER_PLANT_AIRLOCK, Door.POWER_PLANT_AIRLOCK)
          .put(Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK, Door.PSYCHOTRONICS_AIRLOCK)
          .put(Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK, Door.SHUTTLE_BAY_AIRLOCK)
          .build();

  private static ImmutableNetwork<Level, Door> defaultNetwork = null;

  public static final ImmutableNetwork<Level, Door> getDefaultNetwork() {
    if (defaultNetwork != null) {
      return defaultNetwork;
    }
    MutableNetwork<Level, Door> station = NetworkBuilder.directed()
        .allowsParallelEdges(true)
        .allowsSelfLoops(false)
        .build();

    for (Door d : DEFAULT_CONNECTIVITY.keySet()) {
      Door d2 = DEFAULT_CONNECTIVITY.get(d);
      Level sourceLevel = Iterables.getOnlyElement(LEVELS_TO_DOORS.inverse()
          .get(d));
      Level destLevel = Iterables.getOnlyElement(LEVELS_TO_DOORS.inverse()
          .get(d2));
      station.addEdge(sourceLevel, destLevel, d);
    }

    defaultNetwork = ImmutableNetwork.copyOf(station);
    return defaultNetwork;
  }

  

  // Doors adjacent to the lift
  public static final ImmutableList<Door> LIFT_LOBBY_SIDE =
      ImmutableList.of(Door.LOBBY_LIFE_SUPPORT_EXIT, Door.LOBBY_ARBORETUM_EXIT);
  public static final ImmutableList<Door> LIFT_NOT_LOBBY_SIDE =
      ImmutableList.of(Door.ARBORETUM_LOBBY_EXIT, Door.LIFE_SUPPORT_LOBBY_EXIT);

  // Doors blocked by an apex kill wall
  public static final ImmutableList<Door> APEX_LOCKED_KILL_WALL_SIDE =
      ImmutableList.of(Door.LOBBY_HARDWARE_LABS_EXIT, Door.ARBORETUM_CREW_QUARTERS_EXIT,
          Door.ARBORETUM_DEEP_STORAGE_EXIT);
  public static final ImmutableList<Door> APEX_LOCKED_NO_KILL_WALL_SIDE =
      ImmutableList.of(Door.HARDWARE_LABS_LOBBY_EXIT, Door.CREW_QUARTERS_ARBORETUM_EXIT,
          Door.DEEP_STORAGE_ARBORETUM_EXIT);
}
