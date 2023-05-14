package utils.generators;

import com.google.common.collect.ImmutableList;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class StationRandoConsts {
  public static final ImmutableList<Door> DOORS_TO_PROCESS = ImmutableList.of(
      Door.NEUROMOD_DIVISION_LOBBY_EXIT,
      Door.LOBBY_NEUROMOD_DIVISION_EXIT,
      Door.LOBBY_HARDWARE_LABS_EXIT,
      Door.LOBBY_SHUTTLE_BAY_EXIT,
      Door.LOBBY_PSYCHOTRONICS_EXIT,
      Door.LOBBY_ARBORETUM_EXIT,
      Door.LOBBY_LIFE_SUPPORT_EXIT,
      Door.PSYCHOTRONICS_LOBBY_EXIT,
      Door.PSYCHOTRONICS_GUTS_EXIT,
      Door.SHUTTLE_BAY_LOBBY_EXIT,
      Door.SHUTTLE_BAY_GUTS_EXIT,
      Door.GUTS_PSYCHOTRONICS_EXIT,
      Door.GUTS_SHUTTLE_BAY_EXIT,
      Door.GUTS_CARGO_BAY_EXIT,
      Door.GUTS_ARBORETUM_EXIT,
      Door.ARBORETUM_GUTS_EXIT,
      Door.ARBORETUM_BRIDGE_EXIT,
      Door.ARBORETUM_LOBBY_EXIT,
      Door.ARBORETUM_CREW_QUARTERS_EXIT,
      Door.ARBORETUM_DEEP_STORAGE_EXIT,
      Door.BRIDGE_ARBORETUM_EXIT,
      Door.CREW_QUARTERS_ARBORETUM_EXIT,
      Door.DEEP_STORAGE_ARBORETUM_EXIT,
      Door.CARGO_BAY_GUTS_EXIT,
      Door.CARGO_BAY_LIFE_SUPPORT_EXIT,
      Door.LIFE_SUPPORT_CARGO_BAY_EXIT,
      Door.LIFE_SUPPORT_LOBBY_EXIT,
      Door.LIFE_SUPPORT_POWER_PLANT_EXIT,
      Door.POWER_PLANT_LIFE_SUPPORT_EXIT,
      Door.HARDWARE_LABS_LOBBY_EXIT);
  public static final ImmutableList<Level> LEVELS_TO_PROCESS = ImmutableList.of(
      Level.ARBORETUM,
      Level.BRIDGE,
      Level.CARGO_BAY,
      Level.CREW_QUARTERS,
      Level.GUTS,
      Level.HARDWARE_LABS,
      Level.LIFE_SUPPORT,
      Level.LOBBY,
      Level.NEUROMOD_DIVISION,
      Level.POWER_PLANT,
      Level.PSYCHOTRONICS,
      Level.SHUTTLE_BAY,
      Level.DEEP_STORAGE,
      Level.EXTERIOR);
}
