package randomizers.generators;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableNetwork;
import utils.KeycardConnectivityConsts;
import utils.KeycardConnectivityConsts.Keycard;
import utils.KeycardConnectivityConsts.Location;
import utils.NetworkHelper;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;
import utils.Utils;

public class KeycardGenerator {
  private static final int NUM_ATTEMPTS = 1;

  // Map of level name (level;name) to keycard ID
  private Map<String, String> keycardConnectivity;

  // Represents station connectivity
  private ImmutableNetwork<Level, Door> network;
  private Random r;

  private Map<Location, Keycard> internalConnectivity;
  private Set<Location> unusedLocations;
  private Set<Location> usedLocations;

  private boolean successfullyGenerated;

  private enum Priority {
    HIGH,
    MEDIUM,
    LOW
  }

  public KeycardGenerator(ImmutableNetwork<Level, Door> network, long seed) {
    this.network = network;
    this.r = new Random(seed);
    this.successfullyGenerated = false;

    for (int i = 0; i < NUM_ATTEMPTS; i++) {
      if (generateKeycards()) {
        successfullyGenerated = true;
        break;
      }
    }
    if (!successfullyGenerated) {
      System.out.println("Unable to generate keycard connectivity");
    }
  }

  public Map<String, String> getKeycardConnectivity() {
    return keycardConnectivity;
  }

  public Map<Location, Keycard> getInternalConnectivity() {
    return internalConnectivity;
  }

  public boolean getSuccess() {
    return successfullyGenerated;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (Location l : Location.values()) {
      if (internalConnectivity.containsKey(l)) {
        sb.append(String.format("%s:\t\t%s\n", internalConnectivity.get(l), l));
      }
    }

    return sb.toString();
  }

  private boolean placeKeycard(Keycard keycard, List<Level> availableLevels, Random r,
      Priority priority) {
    if (availableLevels.isEmpty()) {
      System.out.println("No available levels!");
      return false;
    }
    int startIndex = r.nextInt(availableLevels.size());
    for (int i = 0; i < availableLevels.size(); i++) {
      // Pick a connected level
      Level candidateLevel = availableLevels.get(startIndex);
      // Pick a keycard in that level
      Set<Location> locationCandidates =
          Sets.newHashSet(KeycardConnectivityConsts.LEVEL_TO_LOCATIONS.get(candidateLevel));

      switch (priority) {
        case HIGH:
          locationCandidates.removeAll(KeycardConnectivityConsts.LOCATIONS_LOW_ACCESSIBILITY);
          locationCandidates.removeAll(KeycardConnectivityConsts.LOCATIONS_DUPE_ONLY);
          locationCandidates.removeAll(KeycardConnectivityConsts.LOCATIONS_MEDIUM_ACCESSIBILITY);
          locationCandidates.removeAll(usedLocations);
          break;
        case MEDIUM:
          locationCandidates.removeAll(KeycardConnectivityConsts.LOCATIONS_LOW_ACCESSIBILITY);
          locationCandidates.removeAll(KeycardConnectivityConsts.LOCATIONS_DUPE_ONLY);
          locationCandidates.removeAll(usedLocations);
          break;
        case LOW:
          locationCandidates.removeAll(usedLocations);
          break;
      }

      if (locationCandidates.size() == 0)  {
        startIndex = (startIndex + 1) % availableLevels.size();
        continue;
      }
      List<Location> keycardList = Lists.newArrayList(locationCandidates);
      // Sort so we can be deterministic
      Collections.sort(keycardList);
      // Pick a valid location
      Location validLocation = Utils.getRandom(keycardList, r);
      usedLocations.add(validLocation);
      unusedLocations.remove(validLocation);
      internalConnectivity.put(validLocation, keycard);
      System.out.printf("%s\t-->\t%s\n", keycard, validLocation);
      return true;
    }
    return false;
  }

  private boolean generateKeycards() {
    usedLocations = Sets.newHashSet();
    unusedLocations = Sets.newHashSet(Location.values());
    internalConnectivity = Maps.newHashMap();

    // Place high priority keycards first.
    List<Keycard> keycardsToProcess =
        Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_HIGH_PRIORITY);
    // To increase odds of success, tackle the high priority keycards in a random order.
    Collections.shuffle(keycardsToProcess, r);

    List<Keycard> medPriority =
        Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_MEDIUM_PRIORITY);
    Collections.shuffle(medPriority, r);

    List<Keycard> lowPriority =
        Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_MEDIUM_PRIORITY);
    Collections.shuffle(lowPriority, r);

    keycardsToProcess.addAll(medPriority);
    keycardsToProcess.addAll(lowPriority);

    // Find out which door is connected to GUTS --> Psychotronics.
    EndpointPair<Level> gutsPsychotronicsNodes =
        network.incidentNodes(Door.GUTS_PSYCHOTRONICS_EXIT);
    Door gutsPsychotronicsDoor = network.edgeConnectingOrNull(gutsPsychotronicsNodes.nodeV(),
        gutsPsychotronicsNodes.nodeU());

    // Find out which door is connected to Lobby --> Life Support (unreachable part of the lift).
    EndpointPair<Level> lobbyLifeSupport = network.incidentNodes(Door.LOBBY_LIFE_SUPPORT_EXIT);
    Door lobbyLifeSupportDoor =
        network.edgeConnectingOrNull(lobbyLifeSupport.nodeV(), lobbyLifeSupport.nodeU());

    // Assume that all connectivity doors are locked.
    ImmutableSet<Door> LOCKED_DOORS = ImmutableSet.of(Door.LOBBY_PSYCHOTRONICS_EXIT,
        Door.LOBBY_SHUTTLE_BAY_EXIT, Door.ARBORETUM_CREW_QUARTERS_EXIT, Door.GUTS_SHUTTLE_BAY_EXIT,
        Door.SHUTTLE_BAY_GUTS_EXIT, Door.LOBBY_ARBORETUM_EXIT, Door.LOBBY_LIFE_SUPPORT_EXIT,
        gutsPsychotronicsDoor, lobbyLifeSupportDoor);

    // Find all levels connected between cargo bay and power plant
    // Find all high availability keycards in those levels
    Set<Door> lockedLockdown = Sets.newHashSet(LOCKED_DOORS);
    lockedLockdown.add(Door.CARGO_BAY_GUTS_EXIT);
    List<Level> powerPlantConnectedDuringLockdown =
        NetworkHelper.getAllConnected(network, lockedLockdown, Level.POWER_PLANT);

    // Precompute the levels connected to every other level
    Map<Level, List<Level>> connected = Maps.newHashMap();
    for (Level l : Level.values()) {
      if (network.nodes()
          .contains(l)) {
        connected.put(l, NetworkHelper.getAllConnected(network, LOCKED_DOORS, l));
      }
    }

    for (Keycard k : keycardsToProcess) {
      List<Level> connectedLevels =
          connected.get(KeycardConnectivityConsts.KEYCARD_TO_LEVEL.get(k));
      boolean success = false;
      switch (k) {
        case GT_FUEL_STORAGE:
          // Because this is locked from both sides, place this keycard twice- once on each side
          success = placeKeycard(k, connected.get(Level.SHUTTLE_BAY), r, Priority.HIGH)
              && placeKeycard(k, connected.get(Level.GUTS), r, Priority.HIGH);
          break;
        case GT_MAINTENANCE_TUNNEL:
          // Maintenance Tunnel - determine which side of the door is unlockable
          Level gutsNeighbor = gutsPsychotronicsNodes.nodeV();
          success = placeKeycard(k, connected.get(gutsNeighbor), r, Priority.HIGH);
          break;
        case PP_COOLANT_CHAMBER:
          success = placeKeycard(k, powerPlantConnectedDuringLockdown, r, Priority.HIGH);
          break;
        case PP_REACTOR:
          success = placeKeycard(k, powerPlantConnectedDuringLockdown, r, Priority.HIGH);
          break;
        default:
          Priority p = Priority.MEDIUM;
          if (KeycardConnectivityConsts.KEYCARDS_HIGH_PRIORITY.contains(k)) {
            p = Priority.HIGH;
          } else if (KeycardConnectivityConsts.KEYCARDS_LOW_PRIORITY.contains(k)) {
            p = Priority.LOW;
          }
          success = placeKeycard(k, connectedLevels, r, p);
          break;
      }
      // Place medium priority keycards
      // Place in medium availability locations

      // Place low priority keycards
      // Place in low priority locations

      // Place duplicate keycards
      // Place in dupe and unused locations

      if (!success) {
        System.out.println("Unable to place keycard " + k);
        System.out.println();
        System.out.println();
        return false;
      }
    }

    return true;
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



    int successCount = 0;
    int numTrials = 1;
    List<Long> failedSeeds = Lists.newArrayList();
    Map<Location, Integer> generalAccessCounts = Maps.newHashMap();

    for (int i = 0; i < numTrials; i++) {
      long seed = 0L;// new Random().nextLong();
      // StationGenerator sg = new StationGenerator(seed, levelsToIds, false);
      // KeycardGenerator kg = new KeycardGenerator(sg.getNetwork(), seed);
      KeycardGenerator kg =
          new KeycardGenerator(StationConnectivityConsts.getDefaultNetwork(), seed);

      if (numTrials == 1) {
        System.out.println(seed);
        System.out.println(kg);
      }

      if (kg.getSuccess()) {
        successCount++;
        for (Location l : kg.getInternalConnectivity()
            .keySet()) {
          if (kg.getInternalConnectivity()
              .get(l) == Keycard.LO_GENERAL_ACCESS) {
            int former = generalAccessCounts.getOrDefault(l, 0);
            generalAccessCounts.put(l, former + 1);
          }
        }

      } else {
        failedSeeds.add(seed);
      }
    }

    for (Location l : Location.values()) {
      if (generalAccessCounts.containsKey(l)) {
        System.out.printf("%s: %d\n", l, generalAccessCounts.get(l));
      }
    }

    System.out.printf("Success rate: %.2f%%\n", 100.0f * successCount / numTrials);
    System.out.printf("Failed seeds: %s\n", failedSeeds.toString());
  }
}
