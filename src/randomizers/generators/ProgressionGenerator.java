package randomizers.generators;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.graph.ImmutableNetwork;
import utils.KeycardConnectivityConsts;
import utils.KeycardConnectivityConsts.Keycard;
import utils.KeycardConnectivityConsts.Location;
import utils.ProgressionGraph;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;
import utils.Utils;

public class ProgressionGenerator {

  private static final int MAX_ATTEMPTS = 10;

  // Map of level name (level;name) to keycard ID
  private ImmutableMap<String, String> keycardConnectivity;
  private ImmutableNetwork<Level, Door> stationConnectivity;
  private ImmutableMap<Location, Keycard> locationsToKeycards;
  private Multimap<Keycard, String> keycardsToReadableLocation;

  private ProgressionGraph progressionGraph;
  private long seed;
  private Random r;

  public ProgressionGenerator(ImmutableNetwork<Level, Door> stationConnectivity, long seed) {
    this.stationConnectivity = stationConnectivity;
    this.seed = seed;
    this.r = new Random(seed);

    int numAttempts = 0;
    // Attempt to generate a station
    for (; numAttempts < MAX_ATTEMPTS; numAttempts++) {
      locationsToKeycards = generate();
      if (verify()) {
        break;
      }
    }

    multimapToConnectivity();
  }

  public ImmutableMap<String, String> getKeycardConnectivity() {
    return keycardConnectivity;
  }

  public ImmutableMap<Location, Keycard> getKeycardPlacement() {
    return locationsToKeycards;
  }

  public ProgressionGraph getProgressionGraph() {
    return progressionGraph;
  }

  private void multimapToConnectivity() {
    Map<String, String> map = Maps.newHashMap();
    keycardsToReadableLocation = HashMultimap.create();
    for (Location l : Location.values()) {
      Keycard k = locationsToKeycards.get(l);
      map.put(KeycardConnectivityConsts.LOCATION_TO_LEVEL_NAME.get(l),
          KeycardConnectivityConsts.KEYCARD_TO_ID.get(k));
      keycardsToReadableLocation.put(k, l.toString());
    }
    keycardConnectivity = ImmutableMap.copyOf(map);
  }

  private ImmutableMap<Location, Keycard> generate() {
    // Map of location to keycard
    Map<Location, Keycard> keycardPlacement = Maps.newHashMap();

    // Place high priority keycards first.
    List<Keycard> highPriority =
        Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_HIGH_PRIORITY);
    List<Location> locationsToUse =
        Lists.newArrayList(KeycardConnectivityConsts.LOCATIONS_HIGH_ACCESSIBILITY);

    Collections.shuffle(highPriority, r);
    placeKeycard(highPriority, locationsToUse, keycardPlacement);


    // Process medium priority keycards.
    List<Keycard> medPriority =
        Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_MEDIUM_PRIORITY);
    locationsToUse.addAll(KeycardConnectivityConsts.LOCATIONS_MEDIUM_ACCESSIBILITY);

    Collections.shuffle(medPriority, r);
    placeKeycard(medPriority, locationsToUse, keycardPlacement);


    // Process low priority keycards.
    List<Keycard> lowPriority = Lists.newArrayList(KeycardConnectivityConsts.KEYCARDS_LOW_PRIORITY);
    locationsToUse.addAll(KeycardConnectivityConsts.LOCATIONS_LOW_ACCESSIBILITY);

    Collections.shuffle(lowPriority, r);
    placeKeycard(lowPriority, locationsToUse, keycardPlacement);

    // Fill remaining locations with random keycards
    fillLocations(Keycard.values(), locationsToUse, keycardPlacement);

    return ImmutableMap.copyOf(keycardPlacement);
  }

  private void placeKeycard(List<Keycard> keycardsToPlace, List<Location> locationsToUse,
      Map<Location, Keycard> keycardPlacement) {
    for (int j = 0; j < keycardsToPlace.size(); j++) {
      Keycard k = keycardsToPlace.get(j);
      for (int i = 0; i < MAX_ATTEMPTS; i++) {
        int locationIndex = locationsToUse.size() > 0 ? r.nextInt(locationsToUse.size()) : 0;
        Location l = locationsToUse.get(locationIndex);
        // Ensure this keycard is not already placed and that this is not the same as its vanilla
        // placement.
        if (!keycardPlacement.containsKey(l)
            && KeycardConnectivityConsts.DEFAULT_CONNECTIVITY.get(l) != k) {
          keycardPlacement.put(l, k);
          locationsToUse.remove(locationIndex);
          break;
        }
      }
    }
  }

  private void fillLocations(Keycard[] keycardsToPlace, List<Location> locationsToUse,
      Map<Location, Keycard> keycardPlacement) {
    for (Location l : locationsToUse) {
      for (int i = 0; i < MAX_ATTEMPTS; i++) {
        Keycard k = Utils.getRandom(keycardsToPlace, r);
        // Ensure that this keycard is not the same as its vanilla placement.
        if (KeycardConnectivityConsts.DEFAULT_CONNECTIVITY.get(l) != k) {
          keycardPlacement.put(l, k);
        }
      }
    }
  }

  private boolean verify() {
    ProgressionGraph pg = new ProgressionGraph(stationConnectivity, locationsToKeycards, seed);
    return pg.verify();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Keycard Connectivity:\n");
    for (Keycard k : Keycard.values()) {
      sb.append(String.format("[%s] --> [%s]\n", k, keycardsToReadableLocation.get(k)));
    }

    return sb.toString();
  }

  public static void main(String[] args) {
    Map<Keycard, Map<Location, Integer>> keycardStats = Maps.newHashMap();
    for (Keycard k : Keycard.values()) {
      keycardStats.put(k, Maps.newHashMap());
    }

    int numIterations = 1;

    for (int i = 0; i < numIterations; i++) {
      Random r = new Random();
      long seed = 6025717307696252058L;//r.nextLong();

      ImmutableBiMap<Level, String> levelsToIds = new ImmutableBiMap.Builder<Level, String>()
          .put(Level.ARBORETUM, "1713490239386284818").put(Level.BRIDGE, "844024417275035158")
          .put(Level.CARGO_BAY, "15659330456296333985")
          .put(Level.CREW_QUARTERS, "844024417252490146")
          .put(Level.DEEP_STORAGE, "1713490239377738413").put(Level.GUTS, "4349723564886052417")
          .put(Level.HARDWARE_LABS, "844024417263019221")
          .put(Level.LIFE_SUPPORT, "4349723564895209499").put(Level.LOBBY, "1713490239377285936")
          .put(Level.NEUROMOD_DIVISION, "12889009724983807463")
          .put(Level.POWER_PLANT, "6732635291182790112")
          .put(Level.PSYCHOTRONICS, "11824555372632688907")
          .put(Level.SHUTTLE_BAY, "1713490239386284988").put(Level.EXTERIOR, "1713490239386284337")
          .build();

      StationGenerator sg = new StationGenerator(seed, levelsToIds);
      ImmutableNetwork<Level, Door> stationConnectivity = sg.getNetwork();

      ProgressionGenerator pg = new ProgressionGenerator(stationConnectivity, seed);
      ImmutableMap<Location, Keycard> placement = pg.getKeycardPlacement();

      for (Location l : placement.keySet()) {
        Keycard k = placement.get(l);
        int prev = keycardStats.get(k).getOrDefault(l, 0);
        keycardStats.get(k).put(l, prev + 1);
      }
      System.out.println(i);
      
      if (numIterations == 1)
      {
        System.out.println(sg.toString());
        System.out.println(pg.toString());
      }
    }

    // for (Keycard k : Keycard.values()) {
    Keycard k = Keycard.LO_GENERAL_ACCESS;
    System.out.println(k);
    for (Location l : Location.values()) {
      if (!keycardStats.get(k).containsKey(l)) {
        continue;
      }
      System.out.printf("%s,%.2f%%\n", l,
          100.0f * (float) keycardStats.get(k).get(l) / numIterations);
    }
    // }

  }
}
