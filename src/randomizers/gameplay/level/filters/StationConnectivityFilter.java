package randomizers.gameplay.level.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import json.SettingsJson;
import randomizers.gameplay.level.filters.rules.StationConnectivityRule;
import utils.HackyQueue;

public class StationConnectivityFilter extends BaseFilter {

  private static final int MAX_ATTEMPTS = 100;
  private static final String DELIMITER = ";";

  // None of these should connect with any of the others
  private static final String[] SINGLE_CONNECTIONS = { "research/simulationlabs;Door.Door_LevelTransition_Default1",
      "research/prototype;LevelTransitionDoor_ToLobby", "executive/crewfacilities;Door.Door_LevelTransition_Default1",
      "executive/corporateit;Door.Door_LevelTransition_Default2", "executive/bridge;Door.Door_LevelTransition_Default1",
      "engineering/powersource;Door.Door_LevelTransition_Default1" };

  // These can only be matched with lift connections from a non-lobby side
  private static final String[] LIFT_LOBBY_SIDE = { "research/lobby;LevelTransition_LifeSupport",
      "research/lobby;LevelTransition_Arboretum" };
  // These can only be matched with lift connections from the lobby side
  private static final String[] LIFT_NOT_LOBBY_SIDE = { "engineering/lifesupport;Door.Door_LevelTransition_Default5",
      "executive/arboretum;Door.Door_LevelTransition_Default6" };

  private static final String[] APEX_LOCKED_KILL_WALL = { "research/lobby;LevelTransition_Hardware",
      "executive/arboretum;Door.Door_LevelTransition_Default8" };
  private static final String[] APEX_LOCKED_NO_KILL_WALL = { "research/prototype;LevelTransitionDoor_ToLobby",
      "executive/corporateit;Door.Door_LevelTransition_Default2" };

  private static final String[] DEFAULT_CONNECTIVITY = {
      "research/simulationlabs;Door.Door_LevelTransition_Default1;1713490239377285936", // lobby
      "research/lobby;LevelTransition_LifeSupport;4349723564895209499",
      "research/lobby;LevelTransition_Hardware;844024417263019221",
      "research/lobby;LevelTransition_ShuttleBay;1713490239386284988", // locked by 15659330456309530410
      "research/lobby;LevelTransition_Psychotronics;11824555372632688907", // locked by 15659330456309530410
      "research/lobby;LevelTransition_SimLabs;12889009724983807463",
      "research/lobby;LevelTransition_Arboretum;1713490239386284818",
      "research/prototype;LevelTransitionDoor_ToLobby;1713490239377285936", // lobby
      "research/psychotronics;Door.Door_LevelTransition_Default2;4349723564886052417", // guts
      "research/psychotronics;Door.Door_LevelTransition_Default1;1713490239377285936", // lobby
      "research/shuttlebay;LTDoor_ToGUTs;4349723564886052417", // guts, locked by 761057047955908816
      "research/shuttlebay;LTDoor_ToLobby;1713490239377285936", // lobby
      "research/zerog_utilitytunnels;Door.Door_LevelTransition_Default2;1713490239386284988", // shuttle bay, locked by
                                                                                              // 761057047955908816
      "research/zerog_utilitytunnels;Door.Door_LevelTransition_Default1;11824555372632688907", // psychotronics
      "research/zerog_utilitytunnels;Door.Door_LevelTransition_Default3;1713490239386284818", // arboretum
      "research/zerog_utilitytunnels;Door.Door_LevelTransition_Exterior1;15659330456296333985", // cargo bay

      "executive/arboretum;Door.Door_LevelTransition_Default5;844024417275035158", // bridge
      "executive/arboretum;Door.Door_LevelTransition_Default3;4349723564886052417", // guts
      "executive/arboretum;Door.Door_LevelTransition_Default8;1713490239377738413", // deep storage
      "executive/arboretum;Door.Door_LevelTransition_Default1;844024417252490146", // crew quarters, locked by
                                                                                   // 844024417269161838
      "executive/arboretum;Door.Door_LevelTransition_Default6;1713490239377285936", // lobby
      "executive/crewfacilities;Door.Door_LevelTransition_Default1;1713490239386284818", // arboretum
      "executive/corporateit;Door.Door_LevelTransition_Default2;1713490239386284818", // arboretum
      "executive/bridge;Door.Door_LevelTransition_Default1;1713490239386284818", // arboretum
      "engineering/cargobay;Door.Door_LevelTransition_Exterior1;4349723564886052417", // guts
      "engineering/cargobay;Door.Door_LevelTransition_Default2;4349723564895209499", // life support
      "engineering/lifesupport;Door.Door_LevelTransition_Default4;15659330456296333985", // cargo bay
      "engineering/lifesupport;Door.Door_LevelTransition_Default5;1713490239377285936", // lobby
      "engineering/lifesupport;Door.Door_LevelTransition_Default6;6732635291182790112", // power plant
      "engineering/powersource;Door.Door_LevelTransition_Default1;4349723564895209499", // lifesupport
  };

  private static final String[] LEVEL_NAME_TO_LOCATION_ID = { "engineering/cargobay;15659330456296333985",
      "engineering/lifesupport;4349723564895209499", "engineering/powersource;6732635291182790112",
      "executive/arboretum;1713490239386284818", "executive/bridge;844024417275035158",
      "executive/corporateit;1713490239377738413", "executive/crewfacilities;844024417252490146",
      "research/lobby;1713490239377285936", "research/prototype;844024417263019221",
      "research/psychotronics;11824555372632688907", "research/shuttlebay;1713490239386284988",
      "research/simulationlabs;12889009724983807463", "research/zerog_utilitytunnels;4349723564886052417",
      "station/exterior;1713490239386284337" };

  // Map of level name to doors to locations
  private Map<String, Map<String, String>> oldConnectivity;
  private Map<String, Map<String, String>> newConnectivity;
  private Map<String, String> levelNameToId;
  private Map<String, String> idToLevelName;
  private Set<String> allConnections;

  private Graph graph;
  private Random r;

  public StationConnectivityFilter(SettingsJson s) {
    Arrays.sort(SINGLE_CONNECTIONS);
    Arrays.sort(LIFT_LOBBY_SIDE);
    Arrays.sort(LIFT_NOT_LOBBY_SIDE);
    r = new Random(s.getSeed());

    buildLevelToId();
    buildConnectivity();

    int numAttempts = 0;
    while (numAttempts++ < MAX_ATTEMPTS) {
      try {
        graph = new MultiGraph("StationConnectivity");
        createNewConnectivity();
        if (isConnected(newConnectivity)) {
          Logger.getGlobal()
                .info(String.format("Found in %s attempts", numAttempts));
          Logger.getGlobal()
                .info(connectivityToString());
          graph.display();
          break;
        }
      } catch (Exception e) {
        Logger.getGlobal()
              .info(String.format("Failed to find connection after %s attempts", numAttempts));
        e.printStackTrace();
      }
    }

    rules.add(new StationConnectivityRule(newConnectivity));
  }

  private String connectivityToString() {
    StringBuilder result = new StringBuilder();
    for (String filename : newConnectivity.keySet()) {
      result.append(String.format("%s:\n", filename));
      for (String door : newConnectivity.get(filename)
                                        .keySet()) {
        result.append(String.format("\t%s: %s\n", door, idToLevelName.get(newConnectivity.get(filename)
                                                                                         .get(door))));
      }
    }
    return result.toString();
  }

  private void buildConnectivity() {
    oldConnectivity = new HashMap<String, Map<String, String>>();
    allConnections = new HashSet<>();
    for (String connection : DEFAULT_CONNECTIVITY) {
      String[] tokens = connection.split(DELIMITER);

      // Add level (node) if it doesn't already exist
      if (!oldConnectivity.containsKey(tokens[0])) {
        oldConnectivity.put(tokens[0], new HashMap<String, String>());
      }

      // Set door (edge) if it doesn't already exist
      oldConnectivity.get(tokens[0])
                     .put(tokens[1], tokens[2]);
      allConnections.add(String.format("%s;%s", tokens[0], tokens[1]));
    }
  }

  private void buildLevelToId() {
    levelNameToId = new HashMap<String, String>();
    idToLevelName = new HashMap<String, String>();
    for (String entry : LEVEL_NAME_TO_LOCATION_ID) {
      String[] tokens = entry.split(DELIMITER);
      levelNameToId.put(tokens[0], tokens[1]);
      idToLevelName.put(tokens[1], tokens[0]);
    }
  }

  public void createNewConnectivity() throws Exception {
    newConnectivity = new HashMap<String, Map<String, String>>();

    HackyQueue<String> connectionsToProcess = new HackyQueue<String>();
    // Enqueue special cases first
    Arrays.stream(LIFT_LOBBY_SIDE)
          .forEach(connectionsToProcess::add);
    Arrays.stream(LIFT_NOT_LOBBY_SIDE)
          .forEach(connectionsToProcess::add);
    Arrays.stream(SINGLE_CONNECTIONS)
          .forEach(connectionsToProcess::add);
    // Enqueue remaining cases (duplicates are skipped)
    connectionsToProcess.addAll(allConnections);

    while (!connectionsToProcess.isEmpty()) {
      String connection = connectionsToProcess.removeFirst();

      List<String> validConnections = new ArrayList<>();
      validConnections.addAll(getValidConnections(connection, connectionsToProcess, newConnectivity));

      // Pick a random valid connection
      String newConnection;
      if (validConnections.size() > 1) {
        newConnection = validConnections.get(r.nextInt(validConnections.size()));
      } else if (validConnections.size() == 1) {
        newConnection = validConnections.get(0);
      } else {
        throw new Exception("Could not find a valid connection!");
      }
      connectionsToProcess.remove(newConnection);

      connect(connection, newConnection, newConnectivity);
    }
  }

  private Set<String> getValidConnections(String connection, HackyQueue<String> remainingConnections,
      Map<String, Map<String, String>> currentConnectivity) {
    String[] tokens = connection.split(DELIMITER);

    // Figure out what other connections are valid
    Set<String> validConnections = swapBetween(LIFT_LOBBY_SIDE, LIFT_NOT_LOBBY_SIDE, connection, remainingConnections);
    if (validConnections != null) {
      return validConnections;
    }
    validConnections = swapBetween(APEX_LOCKED_KILL_WALL, APEX_LOCKED_NO_KILL_WALL, connection, remainingConnections);
    if (validConnections != null) {
      return validConnections;
    }

    validConnections = new HashSet<>();
    validConnections.addAll(remainingConnections);
    Arrays.stream(LIFT_NOT_LOBBY_SIDE)
          .forEach(validConnections::remove);
    Arrays.stream(LIFT_LOBBY_SIDE)
          .forEach(validConnections::remove);
    Arrays.stream(APEX_LOCKED_KILL_WALL)
          .forEach(validConnections::remove);
    Arrays.stream(APEX_LOCKED_NO_KILL_WALL)
          .forEach(validConnections::remove);

    // If this is a "single" type connection, invalidate all other single type
    // connections
    if (Arrays.binarySearch(SINGLE_CONNECTIONS, connection) >= 0) {
      for (String s : SINGLE_CONNECTIONS) {
        validConnections.remove(s);
      }
    }

    // Remove all connections leading from itself, and from nodes that this node
    // already leads to
    Collection<String> connectedNodes = new ArrayList<>();
    connectedNodes.add(tokens[0]);
    if (currentConnectivity.containsKey(tokens[0])) {
      currentConnectivity.get(tokens[0])
                         .values()
                         .forEach(levelId -> connectedNodes.add(idToLevelName.get(levelId)));
    }

    for (String levelName : connectedNodes) {
      Set<String> connectionsToRemove = oldConnectivity.get(levelName)
                                                       .keySet();
      for (String doorName : connectionsToRemove) {
        validConnections.remove(String.format("%s;%s", levelName, doorName));
      }
    }

    return validConnections;
  }

  private Set<String> swapBetween(String[] sortedArr1, String[] sortedArr2, String connection,
      HackyQueue<String> remainingConnections) {
    Set<String> validConnections = new HashSet<>();
    if (Arrays.binarySearch(sortedArr1, connection) >= 0) {
      Arrays.stream(sortedArr2)
            .forEach(s -> {
              if (remainingConnections.contains(s)) {
                validConnections.add(s);
              }
            });
      return validConnections;
    }
    if (Arrays.binarySearch(sortedArr2, connection) >= 0) {
      Arrays.stream(sortedArr1)
            .forEach(s -> {
              if (remainingConnections.contains(s)) {
                validConnections.add(s);
              }
            });
      return validConnections;
    }
    return null;
  }

  private void connect(String connection1, String connection2, Map<String, Map<String, String>> c) {
    String[] connection1Tokens = connection1.split(DELIMITER);
    String[] connection2Tokens = connection2.split(DELIMITER);

    if (!c.containsKey(connection1Tokens[0])) {
      c.put(connection1Tokens[0], new HashMap<String, String>());
      Node n = graph.addNode(connection1Tokens[0]);
      n.setAttribute("ui.label", connection1Tokens[0]);
    }
    if (!c.containsKey(connection2Tokens[0])) {
      c.put(connection2Tokens[0], new HashMap<String, String>());
      Node n = graph.addNode(connection2Tokens[0]);
      n.setAttribute("ui.label", connection2Tokens[0]);
    }

    String connection1Id = levelNameToId.get(connection1Tokens[0]);
    String connection2Id = levelNameToId.get(connection2Tokens[0]);

    c.get(connection1Tokens[0])
     .put(connection1Tokens[1], connection2Id);
    c.get(connection2Tokens[0])
     .put(connection2Tokens[1], connection1Id);

    Edge e = graph.addEdge(String.format("%s -> %s", connection1, connection2), connection1Tokens[0],
        connection2Tokens[0]);
    String edgeName = String.format("%s -> %s", connection1Tokens[1], connection2Tokens[1]);
    e.setAttribute("ui.label", edgeName);
  }

  // Returns whether entire graph is connected
  private boolean isConnected(Map<String, Map<String, String>> c) {
    Set<String> visited = new HashSet<>();
    Stack<String> toVisit = new Stack<>();
    toVisit.add("research/simulationlabs");

    while (!toVisit.isEmpty()) {
      if (visited.equals(c.keySet())) {
        return true;
      }

      String node = toVisit.pop();
      visited.add(node);

      c.get(node)
       .values()
       .forEach(id -> {
         String levelName = idToLevelName.get(id);
         if (!visited.contains(levelName)) {
           toVisit.add(levelName);
         }
       });
    }

    return false;
  }
}
