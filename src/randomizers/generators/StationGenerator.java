package randomizers.generators;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import utils.NetworkHelper;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

/**
 * 
 * A helper class that generates randomized station connectivity.
 *
 */
public class StationGenerator {

  public static final ImmutableList<Door> DOORS_TO_PROCESS = ImmutableList.of(
      Door.NEUROMOD_DIVISION_LOBBY_EXIT, Door.LOBBY_NEUROMOD_DIVISION_EXIT,
      Door.LOBBY_SHUTTLE_BAY_EXIT, Door.LOBBY_PSYCHOTRONICS_EXIT, Door.LOBBY_ARBORETUM_EXIT,
      Door.LOBBY_LIFE_SUPPORT_EXIT, Door.PSYCHOTRONICS_LOBBY_EXIT, Door.PSYCHOTRONICS_GUTS_EXIT,
      Door.SHUTTLE_BAY_LOBBY_EXIT, Door.SHUTTLE_BAY_GUTS_EXIT, Door.GUTS_PSYCHOTRONICS_EXIT,
      Door.GUTS_SHUTTLE_BAY_EXIT, Door.GUTS_CARGO_BAY_EXIT, Door.GUTS_ARBORETUM_EXIT,
      Door.ARBORETUM_GUTS_EXIT, Door.ARBORETUM_BRIDGE_EXIT, Door.ARBORETUM_LOBBY_EXIT,
      Door.ARBORETUM_CREW_QUARTERS_EXIT, Door.BRIDGE_ARBORETUM_EXIT,
      Door.CREW_QUARTERS_ARBORETUM_EXIT, Door.CARGO_BAY_GUTS_EXIT, Door.CARGO_BAY_LIFE_SUPPORT_EXIT,
      Door.LIFE_SUPPORT_CARGO_BAY_EXIT, Door.LIFE_SUPPORT_LOBBY_EXIT,
      Door.LIFE_SUPPORT_POWER_PLANT_EXIT, Door.POWER_PLANT_LIFE_SUPPORT_EXIT,
      Door.LOBBY_HARDWARE_LABS_EXIT, Door.HARDWARE_LABS_LOBBY_EXIT,
      Door.DEEP_STORAGE_ARBORETUM_EXIT, Door.ARBORETUM_DEEP_STORAGE_EXIT);
  public static final ImmutableList<Level> LEVELS_TO_PROCESS =
      ImmutableList.of(Level.ARBORETUM, Level.BRIDGE, Level.CARGO_BAY, Level.CREW_QUARTERS,
          Level.GUTS, Level.HARDWARE_LABS, Level.LIFE_SUPPORT, Level.LOBBY, Level.NEUROMOD_DIVISION,
          Level.POWER_PLANT, Level.PSYCHOTRONICS, Level.SHUTTLE_BAY, Level.DEEP_STORAGE);

  private static final int UNLOCK_ATTEMPTS = 10;
  private static final int MAX_ATTEMPTS = 100;
  // Map of filename to door name to location id
  private Map<String, Map<String, String>> doorConnectivity;
  // Map of filename to spawn name to destination name
  private Map<String, Map<String, String>> spawnConnectivity;

  private Logger logger;
  private Random r;
  private long seed;
  ImmutableBiMap<Level, String> levelsToIds;
  private boolean randomizeKeycards;

  public StationGenerator(long seed, ImmutableBiMap<Level, String> levelsToIds,
      boolean randomizeKeycards) {
    this.seed = seed;
    this.levelsToIds = levelsToIds;
    this.randomizeKeycards = randomizeKeycards;
    r = new Random(seed);
    logger = Logger.getLogger("StationConnectivity");

    boolean configurationFound = false;
    int numAttempts = 1;
    for (; numAttempts <= MAX_ATTEMPTS; numAttempts++) {
      logger.info(String.format("Station generation attempt #%d/%d...", numAttempts, MAX_ATTEMPTS));
      boolean success = createNetworkAndConnectivity();
      if (success) {
        configurationFound = true;
        break;
      }
    }
    if (!configurationFound) {
      logger.info(
          String.format("Failed to generate station connectivity after %s attempts", numAttempts));
    } else {
      logger.info(String.format("Successfully generated station connectivity after %s attempts",
          numAttempts));
    }
  }

  private boolean createNetworkAndConnectivity() {
    ImmutableNetwork<Level, Door> network = createNewConnectivity();

    if (network == null || !validate(network)) {
      return false;
    }
    networkToConnectivity(network);
    logger.info(getDebugData());
    return true;
  }

  public Map<String, Map<String, String>> getDoorConnectivity() {
    return doorConnectivity;
  }

  public Map<String, Map<String, String>> getSpawnConnectivity() {
    return spawnConnectivity;
  }

  // Generates a debug string representing the connectivity.
  public String getDebugData() {
    StringBuilder sb = new StringBuilder();
    sb.append("CONNECTIVITY DEBUG DATA:\n");
    for (Level l : LEVELS_TO_PROCESS) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        if (!DOORS_TO_PROCESS.contains(d)) {
          continue;
        }
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        if (doorName == null) {
          logger.warning(String.format("Door name not found for door %s", d.toString()));
        }
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        if (spawnName == null) {
          logger.warning(String.format("Spawn name not found for door %s", d.toString()));
        }
        String doorValue = doorConnectivity.get(levelName)
            .get(doorName);
        if (doorValue == null) {
          logger.warning(
              String.format("Door value not found for door %s, level %s", d.toString(), levelName));
        }
        String doorValueReadable = levelsToIds.inverse()
            .get(doorValue)
            .toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d))
            .toString();
        String spawnValue = spawnConnectivity.get(levelName)
            .get(spawnName);
        sb.append(String.format(
            "%s:\tDoor to %s now leads to %s. Debug data: %s --> %s, Spawn %s --> %s\n", l,
            oldDoorValueReadable, doorValueReadable, doorName, doorValue, spawnName, spawnValue));
      }
    }

    return sb.toString();
  }

  // Generates a human-readable string representing the connectivity.
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Connectivity for the Exterior is not affected.\n");
    for (Level l : LEVELS_TO_PROCESS) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      sb.append(String.format("%s:\n", l));
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        if (!DOORS_TO_PROCESS.contains(d)) {
          continue;
        }
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        String doorValue = doorConnectivity.get(levelName)
            .get(doorName);
        String doorValueReadable = levelsToIds.inverse()
            .get(doorValue)
            .toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d))
            .toString();
        String spawnValue = spawnConnectivity.get(levelName)
            .get(spawnName);
        sb.append(String.format("\tDoor to %s now leads to %s.\n", oldDoorValueReadable,
            doorValueReadable, doorName, doorValue, spawnName, spawnValue));
      }
    }
    sb.append(String.format("Seed: %d", seed));

    return sb.toString();
  }

  // Transforms the network into connectivity information that we can use for the mod.
  private void networkToConnectivity(ImmutableNetwork<Level, Door> network) {
    doorConnectivity = new HashMap<String, Map<String, String>>();
    spawnConnectivity = new HashMap<String, Map<String, String>>();
    // Initialize with blank maps
    for (Level l : LEVELS_TO_PROCESS) {
      doorConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
      spawnConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
    }

    for (Level fromLevel : LEVELS_TO_PROCESS) {
      // Shorthand destination to use name when spawning from this level
      String fromDestName = StationConnectivityConsts.LEVELS_TO_DESTINATIONS.get(fromLevel);
      // Location ID of this level
      String fromLocationId = levelsToIds.get(fromLevel);

      // Iterate through all of its neighbors
      for (Level toLevel : network.successors(fromLevel)) {
        // Door from current level to the neighboring level
        Door fromDoor = network.edgeConnectingOrNull(fromLevel, toLevel);
        String fromDoorName = StationConnectivityConsts.DOORS_TO_NAMES.get(fromDoor);
        // Door from neighboring level, to the current level
        Door toDoor = network.edgeConnectingOrNull(toLevel, fromLevel);
        String toDoorName = StationConnectivityConsts.DOORS_TO_NAMES.get(toDoor);

        // Shorthand destination name to use when spawning from the neighbor
        String toDestName = StationConnectivityConsts.LEVELS_TO_DESTINATIONS.get(toLevel);
        // Location ID of the neighbor
        String toLocationId = levelsToIds.get(toLevel);

        // Spawn point name in this level
        String fromLevelSpawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(fromDoor);
        // Spawn point name in the neighbor
        String toLevelSpawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(toDoor);

        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel))
            .put(fromDoorName, toLocationId);
        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel))
            .put(toDoorName, fromLocationId);

        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel))
            .put(fromLevelSpawnName, toDestName);
        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel))
            .put(toLevelSpawnName, fromDestName);
      }
    }
  }

  // Generates naive two-way connectivity. May or may not be valid.
  private ImmutableNetwork<Level, Door> createNewConnectivity() {
    MutableNetwork<Level, Door> station = NetworkBuilder.directed()
        .allowsParallelEdges(true)
        .allowsSelfLoops(false)
        .build();

    for (Level l : LEVELS_TO_PROCESS) {
      station.addNode(l);
    }

    ArrayDeque<Door> connectionsToProcess = new ArrayDeque<Door>();
    // Enqueue special cases first
    connectionsToProcess.addAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
    connectionsToProcess.addAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    DOORS_TO_PROCESS.stream()
        .forEach(door -> {
          if (!connectionsToProcess.contains(door)) {
            connectionsToProcess.add(door);
          }
        });

    while (!connectionsToProcess.isEmpty()) {
      Door fromDoor = connectionsToProcess.removeFirst();

      ImmutableSet<Door> remainingConnections = ImmutableSet.copyOf(connectionsToProcess);
      List<Door> validConnections = getValidConnections(fromDoor, remainingConnections, station);

      // Pick a random valid connection
      Door toDoor;
      if (validConnections.size() > 1) {
        toDoor = validConnections.get(r.nextInt(validConnections.size()));
      } else if (validConnections.size() == 1) {
        toDoor = validConnections.get(0);
      } else {
        logger.warning(String.format("No valid connections found. %d doors remaining.",
            remainingConnections.size()));
        return null;
      }
      connectionsToProcess.remove(toDoor);

      Level fromLevel = getLevelForDoor(fromDoor);
      Level toLevel = getLevelForDoor(toDoor);

      station.addEdge(fromLevel, toLevel, fromDoor);
      station.addEdge(toLevel, fromLevel, toDoor);
    }

    return ImmutableNetwork.copyOf(station);
  }

  // Given a door, returns a list of valid doors that can connect to it.
  private List<Door> getValidConnections(Door door, ImmutableSet<Door> remainingConnections,
      MutableNetwork<Level, Door> station) {
    // Levels blocked by an apex kill wall can only connect to other blocked levels.
    if (StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE.contains(door)) {
      List<Door> toReturn = Lists.newArrayList();
      for (Door d : remainingConnections) {
        if (StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE.contains(d)) {
          toReturn.add(d);
        }
      }
      return toReturn;
    }
    if (StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE.contains(door)) {
      List<Door> toReturn = Lists.newArrayList();
      for (Door d : remainingConnections) {
        if (StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE.contains(d)) {
          toReturn.add(d);
        }
      }
      return toReturn;
    }

    // For all other types, eliminate the special cases first
    Set<Door> validConnections = Sets.newHashSet(remainingConnections);
    validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
    validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE);

    // If this is a "single" type connection, invalidate all other single type
    // connections
    if (StationConnectivityConsts.SINGLE_CONNECTIONS.contains(door)) {
      validConnections.removeAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    }

    // Remove all connections leading from itself
    Level fromLevel = getLevelForDoor(door);
    validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(fromLevel));
    // Remove all connections to levels that this is already connected to
    Set<Level> existingConnections = station.adjacentNodes(fromLevel);
    for (Level l : existingConnections) {
      validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(l));
    }

    // Put into a deterministic order
    List<Door> remaining = Lists.newArrayList(validConnections);
    Collections.sort(remaining);
    return remaining;
  }

  // Determines whether a generated network is actually playable.
  private boolean validate(ImmutableNetwork<Level, Door> network) {
    // Assert that everything can be unlocked.
    // If we are randomizing keycards, ignore keycards for now.
    boolean hasGeneralKeycard = randomizeKeycards;
    boolean hasFuelStorageKeycard = randomizeKeycards;
    boolean hasCrewQuartersKeycard = randomizeKeycards;
    boolean unlockedLift = false;
    int numAttempts = 0;

    // Find out which level has the lift technopath
    Level liftTechnopathLevel = getLiftTechnopathLevel(network);

    // Remove locked connections
    Set<Door> lockedDoors = new HashSet<>();
    lockedDoors.addAll(StationConnectivityConsts.LIFT_DOORS);
    // If we are randomizing keycards, assume for now that all connectivity is unlocked.
    if (!randomizeKeycards) {
      lockedDoors.addAll(StationConnectivityConsts.GENERAL_ACCESS_DOORS);
      lockedDoors.addAll(StationConnectivityConsts.FUEL_STORAGE_DOORS);
      lockedDoors.addAll(StationConnectivityConsts.CREW_QUARTERS_DOORS);
    }

    // Ensure that we can get from Cargo Bay to the Power Plant w/o the GUTS exit.
    // This prevents softlocks after initiating the lockdown.
    lockedDoors.add(Door.CARGO_BAY_GUTS_EXIT);
    if (!isConnected(network, lockedDoors, Level.CARGO_BAY, Level.POWER_PLANT)) {
      logger.warning("Unable to reach power plant during lockdown.");
      return false;
    }
    lockedDoors.remove(Door.CARGO_BAY_GUTS_EXIT);

    // See how much of the station we can unlock w/o getting the lift
    while (numAttempts++ < UNLOCK_ATTEMPTS && !(hasGeneralKeycard && hasFuelStorageKeycard
        && hasCrewQuartersKeycard && unlockedLift)) {
      // If we can get to the Lobby and Hardware Labs, we can get the General Access
      // keycard.
      if (!hasGeneralKeycard
          && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, Level.LOBBY)
          && isConnected(network, lockedDoors, Level.LOBBY, Level.HARDWARE_LABS)) {
        lockedDoors.removeAll(StationConnectivityConsts.GENERAL_ACCESS_DOORS);
        hasGeneralKeycard = true;
      }
      // If we can get to the GUTS, we can get the Fuel Storage keycard.
      if (!hasFuelStorageKeycard
          && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, Level.GUTS)) {
        lockedDoors.removeAll(StationConnectivityConsts.FUEL_STORAGE_DOORS);
        hasFuelStorageKeycard = true;
      }
      // If we get to the arboretum, we can unlock crew quarters.
      if (!hasCrewQuartersKeycard
          && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, Level.ARBORETUM)) {
        lockedDoors.removeAll(StationConnectivityConsts.CREW_QUARTERS_DOORS);
        hasCrewQuartersKeycard = true;
      }
      // If we can get to the lift technopath, we can unlock the lift and get the crew quarters
      // keycard.
      if (!unlockedLift
          && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, liftTechnopathLevel)) {
        lockedDoors.removeAll(StationConnectivityConsts.LIFT_DOORS);
        unlockedLift = true;
      }
    }

    if (!hasGeneralKeycard) {
      logger.warning("Unable to obtain general access keycard!");
    }

    if (!hasFuelStorageKeycard) {
      logger.warning("Unable to obtain fuel storage keycard!");
    }

    if (!unlockedLift) {
      logger.warning("Unable to unlock the lift!");
    }

    if (!hasCrewQuartersKeycard) {
      logger.warning("Unable to unlock crew quarters!");
    }

    if (!isConnected(network, lockedDoors)) {
      logger.warning("Generated station is not connected.");
      return false;
    }
    return true;
  }

  private Level getLiftTechnopathLevel(ImmutableNetwork<Level, Door> network) {
    // Find the level that is connected to the LOBBY_ARBORETUM_EXIT.
    return network.incidentNodes(Door.LOBBY_ARBORETUM_EXIT)
        .nodeV();
  }

  private boolean isConnected(ImmutableNetwork<Level, Door> networkToCopy, Set<Door> toRemove,
      Level from, Level to) {
    NetworkHelper helper = new NetworkHelper(networkToCopy, toRemove);
    return helper.isConnected(from, to);
  }

  private boolean isConnected(ImmutableNetwork<Level, Door> networkToCopy, Set<Door> toRemove) {
    NetworkHelper helper = new NetworkHelper(networkToCopy, toRemove);
    return helper.isConnected(Level.NEUROMOD_DIVISION);
  }

  private Level getLevelForDoor(Door door) {
    return Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
        .get(door));
  }

  public static void main(String[] args) {

    ImmutableBiMap<Level, String> levelsToIds =
        new ImmutableBiMap.Builder<Level, String>().put(Level.ARBORETUM, "1713490239386284818")
            .put(Level.BRIDGE, "844024417275035158")
            .put(Level.CARGO_BAY, "15659330456296333985")
            .put(Level.CREW_QUARTERS, "844024417252490146")
            .put(Level.DEEP_STORAGE, "1713490239377738413")
            .put(Level.GUTS, "4349723564886052417")
            .put(Level.HARDWARE_LABS, "844024417263019221")
            .put(Level.LIFE_SUPPORT, "4349723564895209499")
            .put(Level.LOBBY, "1713490239377285936")
            .put(Level.NEUROMOD_DIVISION, "12889009724983807463")
            .put(Level.POWER_PLANT, "6732635291182790112")
            .put(Level.PSYCHOTRONICS, "11824555372632688907")
            .put(Level.SHUTTLE_BAY, "1713490239386284988")
            .build();

    // Random r = new Random();
    long seed = 0L;
    StationGenerator sg = new StationGenerator(seed, levelsToIds, false);
    String station1 = sg.getDebugData();
    System.out.println(station1);
    System.out.println(sg);
  }
}
