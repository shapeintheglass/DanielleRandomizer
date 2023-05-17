package randomizers.generators;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import utils.KeycardConnectivityConsts;
import utils.ProgressionGraph;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;
import utils.generators.StationRandoConsts;

/**
 * 
 * A helper class that generates randomized station connectivity.
 *
 */
public class StationGenerator {
  private static final int MAX_ATTEMPTS = 500;
  // Map of filename to door name to location id
  private Map<String, Map<String, String>> doorConnectivity;
  // Map of filename to spawn name to destination name
  private Map<String, Map<String, String>> spawnConnectivity;

  private Logger logger;
  private Random r;
  private long seed;
  
  // Limit starting spawns to maps with >1 door so the player always starts with multiple options
  public static final ImmutableList<Door> SUPPORTED_SPAWNS = ImmutableList.of(
      Door.LOBBY_NEUROMOD_DIVISION_EXIT,
      Door.LOBBY_HARDWARE_LABS_EXIT,
      Door.LOBBY_SHUTTLE_BAY_EXIT,
      Door.LOBBY_PSYCHOTRONICS_EXIT,
      Door.LOBBY_ARBORETUM_EXIT,
      Door.PSYCHOTRONICS_LOBBY_EXIT,
      Door.PSYCHOTRONICS_GUTS_EXIT,
      Door.GUTS_SHUTTLE_BAY_EXIT,
      Door.GUTS_CARGO_BAY_EXIT,
      Door.GUTS_ARBORETUM_EXIT,
      Door.ARBORETUM_GUTS_EXIT,
      Door.ARBORETUM_BRIDGE_EXIT,
      Door.ARBORETUM_LOBBY_EXIT,
      Door.ARBORETUM_CREW_QUARTERS_EXIT,
      Door.ARBORETUM_DEEP_STORAGE_EXIT,
      Door.CARGO_BAY_GUTS_EXIT,
      Door.CARGO_BAY_LIFE_SUPPORT_EXIT,
      Door.LIFE_SUPPORT_CARGO_BAY_EXIT,
      Door.LIFE_SUPPORT_LOBBY_EXIT,
      Door.LIFE_SUPPORT_POWER_PLANT_EXIT);

  ImmutableNetwork<Level, Door> network;
  private int numAttempts;

  private boolean successfullyGenerated;
  private Door startingLocation;

  public StationGenerator(long seed, Door startingLocation) {
    this.seed = seed;
    this.successfullyGenerated = false;
    this.startingLocation = startingLocation;
    
    r = new Random(seed);
    logger = Logger.getLogger("StationConnectivity");

    numAttempts = 0;
    for (; numAttempts < MAX_ATTEMPTS; numAttempts++) {
      logger.info(String.format("Station generation attempt #%d/%d...", numAttempts, MAX_ATTEMPTS));
      boolean success = createNetworkAndConnectivity();
      if (success) {
        successfullyGenerated = true;
        break;
      }
    }
    if (!successfullyGenerated) {
      logger.info(
          String.format("Failed to generate station connectivity after %s attempts", numAttempts));
    } else {
      logger.info(String.format("Successfully generated station connectivity after %s attempts",
          numAttempts));
    }
  }
  
  public static Door getStartingLocation(long seed) {
    Random r = new Random(seed);
    return SUPPORTED_SPAWNS.get(r.nextInt(SUPPORTED_SPAWNS.size()));
  }

  private boolean createNetworkAndConnectivity() {
    network = createNewConnectivity();
    if (network == null) {
      return false;
    }
    networkToConnectivity(network);
    ProgressionGraph pg = new ProgressionGraph(network, KeycardConnectivityConsts.DEFAULT_CONNECTIVITY, seed, startingLocation);
    if (network == null || !pg.verify()) {
      logger.warning("Generated network was not valid.");
      return false;
    }
    return true;
  }

  public Map<String, Map<String, String>> getDoorConnectivity() {
    return doorConnectivity;
  }

  public Map<String, Map<String, String>> getSpawnConnectivity() {
    return spawnConnectivity;
  }

  public ImmutableNetwork<Level, Door> getNetwork() {
    return network;
  }

  public int getNumAttempts() {
    return numAttempts;
  }

  public boolean getSuccess() {
    return successfullyGenerated;
  }

  // Generates a debug string representing the connectivity.
  public String getDebugData() {
    StringBuilder sb = new StringBuilder();
    sb.append("CONNECTIVITY DEBUG DATA:\n");
    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        if (!StationRandoConsts.DOORS_TO_PROCESS.contains(d)) {
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
        String doorValue = doorConnectivity.get(levelName).get(doorName);
        if (doorValue == null) {
          logger.warning(
              String.format("Door value not found for door %s, level %s", d.toString(), levelName));
        }
        String doorValueReadable = StationConnectivityConsts.LEVELS_TO_IDS.inverse().get(doorValue).toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d)).toString();
        String spawnValue = spawnConnectivity.get(levelName).get(spawnName);
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
    sb.append("Station Connectivity:\n");
    sb.append("Connectivity for the Exterior is not affected.\n");
    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      sb.append(String.format("%s:\n", l));
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        if (!StationRandoConsts.DOORS_TO_PROCESS.contains(d)) {
          continue;
        }
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        String doorValue = doorConnectivity.get(levelName).get(doorName);
        String doorValueReadable = StationConnectivityConsts.LEVELS_TO_IDS.inverse().get(doorValue).toString();
        String oldDoorValueReadable = StationConnectivityConsts.LEVELS_TO_DOORS.inverse()
            .get(StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(d)).toString();
        String spawnValue = spawnConnectivity.get(levelName).get(spawnName);
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
    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      doorConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
      spawnConnectivity.put(StationConnectivityConsts.LEVELS_TO_NAMES.get(l), new HashMap<>());
    }

    for (Level fromLevel : StationRandoConsts.LEVELS_TO_PROCESS) {
      // Shorthand destination to use name when spawning from this level
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

        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel))
            .put(fromDoorName, toLocationId);
        doorConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel)).put(toDoorName,
            fromLocationId);

        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(fromLevel))
            .put(fromLevelSpawnName, toDestName);
        spawnConnectivity.get(StationConnectivityConsts.LEVELS_TO_NAMES.get(toLevel))
            .put(toLevelSpawnName, fromDestName);
      }
    }
  }

  // Generates naive two-way connectivity. May or may not be valid.
  private ImmutableNetwork<Level, Door> createNewConnectivity() {
    MutableNetwork<Level, Door> station =
        NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(false).build();

    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      station.addNode(l);
    }

    ArrayDeque<Door> connectionsToProcess = new ArrayDeque<Door>();
    StationRandoConsts.DOORS_TO_PROCESS.stream().forEach(door -> {
      if (!connectionsToProcess.contains(door)) {
        connectionsToProcess.add(door);
      }
    });

    while (!connectionsToProcess.isEmpty()) {
      Door fromDoor = connectionsToProcess.removeFirst();

      // Sanity check: To ensure that we did not double add a connection to the list, verify that
      // this door is not present in the remaining connections.
      while (connectionsToProcess.contains(fromDoor)) {
        connectionsToProcess.remove(fromDoor);
      }

      ImmutableSet<Door> remainingConnections = ImmutableSet.copyOf(connectionsToProcess);

      List<Door> validConnections = getValidConnections(fromDoor, remainingConnections, station);

      // Pick a random valid connection
      Door toDoor;
      if (validConnections.size() > 1) {
        toDoor = validConnections.get(r.nextInt(validConnections.size()));
      } else if (validConnections.size() == 1) {
        toDoor = validConnections.get(0);
      } else {
        logger
            .warning(String.format("No valid connections found for door %s. %d doors remaining: %s",
                fromDoor, remainingConnections.size(), remainingConnections));
        return null;
      }
      connectionsToProcess.remove(toDoor);

      Level fromLevel = getLevelForDoor(fromDoor);
      Level toLevel = getLevelForDoor(toDoor);

      station.addEdge(fromLevel, toLevel, fromDoor);
      station.addEdge(toLevel, fromLevel, toDoor);
    }

    // Hard-code exterior airlocks
    station.addNode(Level.EXTERIOR);
    station.addEdge(Level.ARBORETUM, Level.EXTERIOR, Door.ARBORETUM_AIRLOCK);
    station.addEdge(Level.HARDWARE_LABS, Level.EXTERIOR, Door.HARDWARE_LABS_AIRLOCK);
    station.addEdge(Level.POWER_PLANT, Level.EXTERIOR, Door.POWER_PLANT_AIRLOCK);
    station.addEdge(Level.PSYCHOTRONICS, Level.EXTERIOR, Door.PSYCHOTRONICS_AIRLOCK);
    station.addEdge(Level.SHUTTLE_BAY, Level.EXTERIOR, Door.SHUTTLE_BAY_AIRLOCK);
    station.addEdge(Level.EXTERIOR, Level.ARBORETUM, Door.EXTERIOR_ARBORETUM_AIRLOCK);
    station.addEdge(Level.EXTERIOR, Level.HARDWARE_LABS, Door.EXTERIOR_HARDWARE_LABS_AIRLOCK);
    station.addEdge(Level.EXTERIOR, Level.POWER_PLANT, Door.EXTERIOR_POWER_PLANT_AIRLOCK);
    station.addEdge(Level.EXTERIOR, Level.PSYCHOTRONICS, Door.EXTERIOR_PSYCHOTRONICS_AIRLOCK);
    station.addEdge(Level.EXTERIOR, Level.SHUTTLE_BAY, Door.EXTERIOR_SHUTTLE_BAY_AIRLOCK);

    return ImmutableNetwork.copyOf(station);
  }

  // Given a door, returns a list of valid doors that can connect to it.
  private List<Door> getValidConnections(Door door, ImmutableSet<Door> remainingConnections,
      MutableNetwork<Level, Door> station) {
    Set<Door> validConnections = Sets.newHashSet(remainingConnections);

    // Levels with one exit cannot connect to each other.
    if (StationConnectivityConsts.SINGLE_CONNECTIONS.contains(door)) {
      validConnections.removeAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    }

    // Locked doors should not connect to each other.
    // if (StationConnectivityConsts.ONE_WAY_LOCKS.contains(door)) {
    // validConnections.removeAll(StationConnectivityConsts.ONE_WAY_LOCKS);
    // }

    // Remove all connections leading from itself
    Level fromLevel = getLevelForDoor(door);
    validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(fromLevel));
    // Remove all connections to levels that this is already connected to
    Set<Level> existingConnections = station.adjacentNodes(fromLevel);
    for (Level l : existingConnections) {
      validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(l));
    }
    // Remove the door this would be connected to in vanilla
    // Door vanillaDoor = StationConnectivityConsts.DEFAULT_CONNECTIVITY.get(door);
    // validConnections.remove(vanillaDoor);

    // Put into a deterministic order
    List<Door> remaining = Lists.newArrayList(validConnections);
    Collections.sort(remaining);
    return remaining;
  }

  private Level getLevelForDoor(Door door) {
    return Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(door));
  }

  public static void main(String[] args) {
    Map<Level, Map<Level, Integer>> levelStats = Maps.newHashMap();
    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      levelStats.put(l, Maps.newHashMap());
    }

    Map<Door, Map<Door, Integer>> doorStats = Maps.newHashMap();
    for (Door d : StationRandoConsts.DOORS_TO_PROCESS) {
      doorStats.put(d, Maps.newHashMap());
    }

    float avgNumAttempts = 0f;
    int maxAttempts = Integer.MIN_VALUE;
    int minAttempts = Integer.MAX_VALUE;
    float percentSuccess = 0f;

    int numIterations = 20;

    for (int i = 0; i < numIterations; i++) {
      Random r = new Random();
      //long seed = 6025717307696252058L;//r.nextLong();
      StationGenerator sg = new StationGenerator(r.nextLong(), Door.NEUROMOD_DIVISION_LOBBY_EXIT);

      if (sg.successfullyGenerated) {
        percentSuccess++;
      }

      int attempts = sg.getNumAttempts();
      avgNumAttempts += attempts;
      if (attempts > maxAttempts) {
        maxAttempts = attempts;
      }
      if (attempts < minAttempts) {
        minAttempts = attempts;
      }

      ImmutableNetwork<Level, Door> n = sg.network;
      if (n != null) {
        for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
          for (Level l2 : n.adjacentNodes(l)) {
            int prev = levelStats.get(l).getOrDefault(l2, 0);
            levelStats.get(l).put(l2, prev + 1);
          }
        }

        for (Door d : StationRandoConsts.DOORS_TO_PROCESS) {
          EndpointPair<Level> connectingLevels = n.incidentNodes(d);
          Door adjacentDoor =
              n.edgeConnectingOrNull(connectingLevels.nodeV(), connectingLevels.nodeU());
          int prev = doorStats.get(d).getOrDefault(adjacentDoor, 0);
          doorStats.get(d).put(adjacentDoor, prev + 1);
        }
      } else {
        System.out.println("Unable to generate a station!");
      }

    }

    avgNumAttempts /= numIterations;
    percentSuccess /= numIterations;
    percentSuccess *= 100.0;

    for (Level l : StationRandoConsts.LEVELS_TO_PROCESS) {
      System.out.println(l);
      for (Level l2 : StationRandoConsts.LEVELS_TO_PROCESS) {
        if (levelStats.get(l).containsKey(l2)) {
          System.out.printf("\t%s: %.0f%%\n", l2,
              100.0 * levelStats.get(l).get(l2) / numIterations);
        }
      }
    }

    for (Door d : StationRandoConsts.DOORS_TO_PROCESS) {
      System.out.println(d);
      for (Door d2 : StationRandoConsts.DOORS_TO_PROCESS) {
        if (doorStats.get(d).containsKey(d2)) {
          System.out.printf("\t%s: %.0f%%\n", d2, 100.0 * doorStats.get(d).get(d2) / numIterations);
        }
      }
    }

    System.out.printf("Success rate: %.0f%%\n", percentSuccess);
    System.out.printf("Average number of attempts: %.0f\n", avgNumAttempts);
    System.out.printf("Max number of attempts: %d\n", maxAttempts);
    System.out.printf("Min number of attempts: %d\n", minAttempts);

  }
}
