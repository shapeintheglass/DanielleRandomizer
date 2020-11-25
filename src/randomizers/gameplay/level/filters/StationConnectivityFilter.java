package randomizers.gameplay.level.filters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.guava.MutableNetworkAdapter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.Graphs;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import json.SettingsJson;
import randomizers.gameplay.level.filters.rules.KeyFabPlansInCrewQuartersRule;
import randomizers.gameplay.level.filters.rules.StationConnectivityRule;
import randomizers.gameplay.level.filters.rules.UnlockPowerPlantRule;
import randomizers.gameplay.level.filters.rules.UnlockPsychotronicsRule;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class StationConnectivityFilter extends BaseFilter {

  private static final int UNLOCK_ATTEMPTS = 10;
  private static final String STATION_CONNECTIVITY_PNG = "station_connectivity.png";
  private static final int MAX_ATTEMPTS = 100;
  // Map of filename to door name to location id
  private Map<String, Map<String, String>> doorConnectivity;
  // Map of filename to spawn name to destination name
  private Map<String, Map<String, String>> spawnConnectivity;

  private Logger logger;

  public StationConnectivityFilter(SettingsJson s) {
    this(s.getSeed());
  }

  public StationConnectivityFilter(long seed) {
    Random r = new Random(seed);
    logger = Logger.getLogger("StationConnectivity");

    int numAttempts = 0;
    while (numAttempts++ < MAX_ATTEMPTS) {
      try {
        logger.info(String.format("Attempt #%d", numAttempts));
        ImmutableNetwork<Level, Door> network = createNewConnectivity(r);
        if (!validate(network)) {
          throw new IllegalStateException("Configuration invalid! (not connected)");
        }
        visualize(network);
        networkToConnectivity(network);
        rules.add(new StationConnectivityRule(doorConnectivity, spawnConnectivity));
        rules.add(new UnlockPsychotronicsRule());
        rules.add(new UnlockPowerPlantRule());
        rules.add(new KeyFabPlansInCrewQuartersRule());
        logger.info(connectivityToString());
        break;
      } catch (IllegalStateException | IOException e) {
        logger.info(String.format("Failed to find connection after %s attempts", numAttempts));
        e.printStackTrace();
      }
    }
  }

  private String connectivityToString() {
    StringBuilder sb = new StringBuilder();
    sb.append("CONNECTIVITY DEBUG DATA:\n");
    for (Level l : Level.values()) {
      String levelName = StationConnectivityConsts.LEVELS_TO_NAMES.get(l);
      for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
        String doorName = StationConnectivityConsts.DOORS_TO_NAMES.get(d);
        String spawnName = StationConnectivityConsts.DOORS_TO_SPAWNS.get(d);
        String doorValue = doorConnectivity.get(levelName).get(doorName);
        String doorValueReadable =
            StationConnectivityConsts.LEVELS_TO_IDS.inverse().get(doorValue).toString();
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

  private ImmutableNetwork<Level, Door> createNewConnectivity(Random r)
      throws IllegalStateException {
    MutableNetwork<Level, Door> station =
        NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(false).build();

    for (Level l : Level.values()) {
      station.addNode(l);
    }

    ArrayDeque<Door> connectionsToProcess = new ArrayDeque<Door>();
    // Enqueue special cases first
    connectionsToProcess.addAll(StationConnectivityConsts.LIFT_LOBBY_SIDE);
    // connectionsToProcess.addAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
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

      Level fromLevel = Iterables
          .getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(fromDoor));
      Level toLevel =
          Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(toDoor));

      station.addEdge(fromLevel, toLevel, fromDoor);
      station.addEdge(toLevel, fromLevel, toDoor);
    }

    return ImmutableNetwork.copyOf(station);
  }

  private List<Door> getValidConnections(Door door, ImmutableSet<Door> remainingConnections,
      MutableNetwork<Level, Door> station) {
    // Start with special cases that can only be matched with each other
    if (StationConnectivityConsts.LIFT_LOBBY_SIDE.contains(door)) {
      return Lists.newArrayList(
          Sets.intersection(remainingConnections, StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE));
    }
    if (StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE.contains(door)) {
      return Lists.newArrayList(
          Sets.intersection(remainingConnections, StationConnectivityConsts.LIFT_LOBBY_SIDE));
    }
    /*
     * if (StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE.contains(door)) { return
     * Lists.newArrayList( Sets.intersection(remainingConnections,
     * StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE)); } if
     * (StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE.contains(door)) { return
     * Lists.newArrayList( Sets.intersection(remainingConnections,
     * StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE)); }
     */

    Set<Door> validConnections = Sets.newHashSet(remainingConnections);
    validConnections.removeAll(StationConnectivityConsts.LIFT_LOBBY_SIDE);
    validConnections.removeAll(StationConnectivityConsts.LIFT_NOT_LOBBY_SIDE);
    // validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_KILL_WALL_SIDE);
    // validConnections.removeAll(StationConnectivityConsts.APEX_LOCKED_NO_KILL_WALL_SIDE);

    // If this is a "single" type connection, invalidate all other single type
    // connections
    if (StationConnectivityConsts.SINGLE_CONNECTIONS.contains(door)) {
      validConnections.removeAll(StationConnectivityConsts.SINGLE_CONNECTIONS);
    }

    // Remove all connections leading from itself
    Level fromLevel =
        Iterables.getOnlyElement(StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(door));
    validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(fromLevel));
    // Remove all connections to levels that this is already connected to
    Set<Level> existingConnections = station.adjacentNodes(fromLevel);
    for (Level l : existingConnections) {
      validConnections.removeAll(StationConnectivityConsts.LEVELS_TO_DOORS.get(l));
    }

    return Lists.newArrayList(validConnections);
  }

  private void visualize(ImmutableNetwork<Level, Door> network) throws IOException {
    Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    for (Level l : network.nodes()) {
      if (!graph.containsVertex(l.name())) {
        graph.addVertex(l.name());
      }
      for (Level neighbor : network.adjacentNodes(l)) {
        if (!graph.containsVertex(neighbor.name())) {
          graph.addVertex(neighbor.name());
        }
        if (!graph.containsEdge(l.name(), neighbor.name())) {
          graph.addEdge(l.name(), neighbor.name());
        }
      }
    }
    graph.addVertex("HARDWARE_LABS");
    graph.addVertex("DEEP_STORAGE");
    graph.addEdge("LOBBY", "HARDWARE_LABS");
    graph.addEdge("ARBORETUM", "DEEP_STORAGE");

    JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
    mxIGraphLayout graphLayout = new mxFastOrganicLayout(graphAdapter);
    graphLayout.execute(graphAdapter.getDefaultParent());

    BufferedImage image =
        mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
    File imgFile = new File(STATION_CONNECTIVITY_PNG);
    imgFile.createNewFile();
    ImageIO.write(image, "PNG", imgFile);
  }

  private boolean validate(ImmutableNetwork<Level, Door> network) {
    // Assert that everything can be unlocked
    boolean hasGeneralKeycard = false;
    boolean hasFuelStorageKeycard = false;
    int numAttempts = 0;

    // Remove locked connections
    Set<Door> toRemove = new HashSet<>();
    StationConnectivityConsts.DOORS_UNLOCKED_BY_LEVEL.values().stream().forEach(toRemove::addAll);

    // See how much of the station we can unlock w/o getting the lift
    while (numAttempts++ < UNLOCK_ATTEMPTS && !(hasGeneralKeycard && hasFuelStorageKeycard)) {
      // If we can get to the Lobby, we can get the General Access keycard.
      if (!hasGeneralKeycard
          && isConnected(network, toRemove, Level.NEUROMOD_DIVISION, Level.LOBBY)) {
        toRemove.removeAll(StationConnectivityConsts.DOORS_UNLOCKED_BY_LEVEL.get(Level.LOBBY));
        hasGeneralKeycard = true;
      }
      // If we can get to the GUTS, we can get the Fuel Storage keycard.
      if (!hasFuelStorageKeycard
          && isConnected(network, toRemove, Level.NEUROMOD_DIVISION, Level.GUTS)) {
        toRemove.removeAll(StationConnectivityConsts.DOORS_UNLOCKED_BY_LEVEL.get(Level.GUTS));
        hasFuelStorageKeycard = true;
      }
    }

    if (!hasGeneralKeycard) {
      logger.warning("Unable to obtain general access keycard!");
    }

    if (!hasFuelStorageKeycard) {
      logger.warning("Unable to obtain fuel storage keycard!");
    }

    return isConnected(network, toRemove);
  }

  private boolean isConnected(ImmutableNetwork<Level, Door> networkToCopy, Set<Door> toRemove,
      Level from, Level to) {
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
    new StationConnectivityFilter(0);
  }
}
