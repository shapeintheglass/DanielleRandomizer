package utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.guava.MutableNetworkAdapter;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.Graphs;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class StationGenerator {
  
  private static final int UNLOCK_ATTEMPTS = 10;
  private static final int MAX_ATTEMPTS = 100;
  // Map of filename to door name to location id
  private Map<String, Map<String, String>> doorConnectivity;
  // Map of filename to spawn name to destination name
  private Map<String, Map<String, String>> spawnConnectivity;
  // Human readable version of connectivity
  private ImmutableNetwork<Level, Door> network;

  private Logger logger;
  private Random r;
  private long seed;
  
  public StationGenerator(long seed) {
    this.seed = seed;
    r = new Random(seed);
    logger = Logger.getLogger("StationConnectivity");

    for (int numAttempts = 0; numAttempts < MAX_ATTEMPTS; numAttempts++) {
      try {
        logger.info(String.format("Attempt #%d", numAttempts));
        network = createNewConnectivity();
        if (!validate(network)) {
          throw new IllegalStateException("Configuration invalid! (not connected)");
        }
        networkToConnectivity(network);
        logger.info(getDebugData());
        break;
      } catch (IllegalStateException e) {
        logger.info(String.format("Failed to find connection after %s attempts", numAttempts));
        e.printStackTrace();
      }
    }
  }
  
  public ImmutableNetwork<Level, Door> getNetwork() {
    return network;
  }

  public Map<String, Map<String, String>> getDoorConnectivity() {
    return doorConnectivity;
  }

  public Map<String, Map<String, String>> getSpawnConnectivity() {
    return spawnConnectivity;
  }

  public String getDebugData() {
    StringBuilder sb = new StringBuilder();
    sb.append("CONNECTIVITY DEBUG DATA:\n");
    for (Level l : Level.values()) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        if (doorName == null) {
          logger.warning(String.format("Door name not found for door %s", d.toString()));
        }
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        if (spawnName == null) {
          logger.warning(String.format("Spawn name not found for door %s", d.toString()));
        }
        String doorValue = doorConnectivity.get(levelName).get(doorName);
        if (doorValue == null) {
          logger.warning(String.format("Door value not found for door %s, level %s", d.toString(), levelName));
        }
        String doorValueReadable = StationConnectivityConsts.LEVELS_TO_IDS.inverse().get(doorValue).toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d))
            .toString();
        String spawnValue = spawnConnectivity.get(levelName).get(spawnName);
        sb.append(String.format("%s:\tDoor to %s now leads to %s. Debug data: %s --> %s, Spawn %s --> %s\n", l,
            oldDoorValueReadable, doorValueReadable, doorName, doorValue, spawnName, spawnValue));
      }
    }

    return sb.toString();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Connectivity for Deep Storage and the Exterior are not affected.\n");
    for (Level l : Level.values()) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      sb.append(String.format("%s:\n", l));
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        String doorValue = doorConnectivity.get(levelName).get(doorName);
        String doorValueReadable = StationConnectivityConsts.LEVELS_TO_IDS.inverse().get(doorValue).toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d))
            .toString();
        String spawnValue = spawnConnectivity.get(levelName).get(spawnName);
        sb.append(String.format("\tDoor to %s now leads to %s.\n", oldDoorValueReadable, doorValueReadable, doorName,
            doorValue, spawnName, spawnValue));
      }
    }
    sb.append(String.format("Seed: %d", seed));

    return sb.toString();
  }

  private void networkToConnectivity(ImmutableNetwork<Level, Door> network) {
    doorConnectivity = new HashMap<String, Map<String, String>>();
    spawnConnectivity = new HashMap<String, Map<String, String>>();
    // Initialize with blank maps
    for (Level l : Level.values()) {
      doorConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
      spawnConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
    }

    for (Level fromLevel : Level.values()) {
      // Destination name coming from this level
      String fromDestName = StationConnectivityConsts.LEVELS_TO_DESTINATIONS.get(fromLevel);
      // Location ID of this level
      String fromLocationId = StationConnectivityConsts.LEVELS_TO_IDS.get(fromLevel);

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
        String toLocationId = StationConnectivityConsts.LEVELS_TO_IDS.get(toLevel);

        // Spawn point name in this level
        String fromLevelSpawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(fromDoor);
        // Spawn point name in the neighbor
        String toLevelSpawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(toDoor);

        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel)).put(fromDoorName, toLocationId);
        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel)).put(toDoorName, fromLocationId);

        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel))
            .put(fromLevelSpawnName, toDestName);
        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel))
            .put(toLevelSpawnName, fromDestName);
      }
    }
  }

  private ImmutableNetwork<Level, Door> createNewConnectivity() throws IllegalStateException {
    MutableNetwork<Level, Door> station = NetworkBuilder.directed()
        .allowsParallelEdges(true)
        .allowsSelfLoops(false)
        .build();

    for (Level l : Level.values()) {
      station.addNode(l);
    }

    ArrayDeque<Door> connectionsToProcess = new ArrayDeque<Door>();
    // Enqueue special cases first
    connectionsToProcess.addAll(StationConnectivityConsts.LIFT_LOBBY_SIDE);
    connectionsToProcess.addAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
    connectionsToProcess.addAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    Arrays.stream(Door.values()).forEach(door -> {
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
        throw new IllegalStateException("Could not find a valid connection!");
      }
      connectionsToProcess.remove(toDoor);

      Level fromLevel = Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(fromDoor));
      Level toLevel = Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(toDoor));

      station.addEdge(fromLevel, toLevel, fromDoor);
      station.addEdge(toLevel, fromLevel, toDoor);
    }

    return ImmutableNetwork.copyOf(station);
  }

  private List<Door> getValidConnections(Door door, ImmutableSet<Door> remainingConnections,
      MutableNetwork<Level, Door> station) {
    // Start with special cases that can only be matched with each other
    if (StationConnectivityConsts.LIFT_LOBBY_SIDE.contains(door)) {
      List<Door> toReturn = Lists.newArrayList();
      for (Door d : remainingConnections) {
        if (StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE.contains(d)) {
          toReturn.add(d);
        }
      }
      return toReturn;
    }
    if (StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE.contains(door)) {
      List<Door> toReturn = Lists.newArrayList();
      for (Door d : remainingConnections) {
        if (StationConnectivityConsts.LIFT_LOBBY_SIDE.contains(d)) {
          toReturn.add(d);
        }
      }
      return toReturn;
    }

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

    Set<Door> validConnections = Sets.newHashSet(remainingConnections);
    validConnections.removeAll(StationConnectivityConsts.LIFT_LOBBY_SIDE);
    validConnections.removeAll(StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE);
    validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
    validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE);

    // If this is a "single" type connection, invalidate all other single type
    // connections
    if (StationConnectivityConsts.SINGLE_CONNECTIONS.contains(door)) {
      validConnections.removeAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    }

    // Remove all connections leading from itself
    Level fromLevel = Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(door));
    validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(fromLevel));
    // Remove all connections to levels that this is already connected to
    Set<Level> existingConnections = station.adjacentNodes(fromLevel);
    for (Level l : existingConnections) {
      validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(l));
    }

    return Lists.newArrayList(validConnections);
  }

  private boolean validate(ImmutableNetwork<Level, Door> network) {
    // Assert that everything can be unlocked
    boolean hasGeneralKeycard = false;
    boolean hasFuelStorageKeycard = false;
    boolean unlockedLift = false;
    int numAttempts = 0;

    // Remove locked connections
    Set<Door> lockedDoors = new HashSet<>();
    lockedDoors.addAll(StationConnectivityConsts.LIFT_DOORS);
    lockedDoors.addAll(StationConnectivityConsts.GENERAL_ACCESS_DOORS);
    lockedDoors.addAll(StationConnectivityConsts.FUEL_STORAGE_DOORS);

    // Find out which level has the lift technopath
    Level liftTechnopathLevel = getLiftTechnopathLevel(network);

    // See how much of the station we can unlock w/o getting the lift
    while (numAttempts++ < UNLOCK_ATTEMPTS && !(hasGeneralKeycard && hasFuelStorageKeycard)) {
      // If we can get to the Lobby and Hardware Labs, we can get the General Access
      // keycard.
      if (!hasGeneralKeycard && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, Level.LOBBY) && isConnected(
          network, lockedDoors, Level.LOBBY, Level.HARDWARE_LABS)) {
        lockedDoors.removeAll(StationConnectivityConsts.GENERAL_ACCESS_DOORS);
        hasGeneralKeycard = true;
      }
      // If we can get to the GUTS, we can get the Fuel Storage keycard.
      if (!hasFuelStorageKeycard && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, Level.GUTS)) {
        lockedDoors.removeAll(StationConnectivityConsts.FUEL_STORAGE_DOORS);
        hasFuelStorageKeycard = true;
      }
      // If we get to the lift technopath, we can unlock the lift.
      if (!unlockedLift && isConnected(network, lockedDoors, Level.NEUROMOD_DIVISION, liftTechnopathLevel)) {
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

    return isConnected(network, lockedDoors);
  }

  private Level getLiftTechnopathLevel(ImmutableNetwork<Level, Door> network) {
    // Find the level that is connected to the LOBBY_ARBORETUM_EXIT.
    Set<Door> doors = network.adjacentEdges(Door.LOBBY_ARBORETUM_EXIT);
    if (doors.contains(Door.LIFE_SUPPORT_LOBBY_EXIT)) {
      return Level.LIFE_SUPPORT;
    } else {
      return Level.ARBORETUM;
    }
  }

  private boolean isConnected(ImmutableNetwork<Level, Door> networkToCopy, Set<Door> toRemove, Level from, Level to) {
    MutableNetwork<Level, Door> network = Graphs.copyOf(networkToCopy);
    toRemove.stream().forEach(network::removeEdge);

    MutableNetworkAdapter<Level, Door> ina = new MutableNetworkAdapter<>(network);
    ConnectivityInspector<Level, Door> connectivityInspector = new ConnectivityInspector<>(ina);
    return connectivityInspector.pathExists(from, to);
  }

  private boolean isConnected(ImmutableNetwork<Level, Door> networkToCopy, Set<Door> toRemove) {
    MutableNetwork<Level, Door> network = Graphs.copyOf(networkToCopy);
    toRemove.stream().forEach(network::removeEdge);

    MutableNetworkAdapter<Level, Door> ina = new MutableNetworkAdapter<>(network);
    ConnectivityInspector<Level, Door> connectivityInspector = new ConnectivityInspector<>(ina);
    return connectivityInspector.isConnected();
  }

  public static void main(String[] args) {

    long seed = 0L;
    StationGenerator sg = new StationGenerator(seed);
    String station1 = sg.toString();
    
    StationGenerator sg2 = new StationGenerator(seed);
    String station2 = sg2.toString();
    System.out.println(station1);
    System.out.println(station2);
    System.out.println(station1.equals(station2));

  }
}
