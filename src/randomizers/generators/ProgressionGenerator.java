package randomizers.generators;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.graph.ImmutableNetwork;
import proto.RandomizerSettings.Settings;
import utils.KeycardConnectivityConsts;
import utils.KeycardConnectivityConsts.Keycard;
import utils.KeycardConnectivityConsts.Location;
import utils.ProgressionGraph;
import utils.ProgressionGraph.LevelProgression;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;
import utils.Utils;

public class ProgressionGenerator {

  private static final int MAX_ATTEMPTS = 10;

  // Map of filename to door name to location id
  private Map<String, Map<String, String>> doorConnectivity;
  // Map of filename to spawn name to destination name
  private Map<String, Map<String, String>> spawnConnectivity;
  // Map of level name (level;name) to keycard ID
  private Map<String, String> keycardConnectivity;
  private ImmutableMultimap<Location, Keycard> keycardPlacement;
  private Settings s;

  private ProgressionGraph progressionGraph;
  private Random r;

  public ProgressionGenerator(Settings s, ImmutableNetwork<Level, Door> stationConnectivity,
      Random r) {
    this.s = s;
    this.r = r;
    doorConnectivity = Maps.newHashMap();
    spawnConnectivity = Maps.newHashMap();
    keycardConnectivity = Maps.newHashMap();

    int numAttempts = 0;
    // Attempt to generate a station
    for (; numAttempts < MAX_ATTEMPTS; numAttempts++) {
      keycardPlacement = generate();
      if (verify()) {
        break;
      }
    }
  }

  public Map<String, Map<String, String>> getDoorConnectivity() {
    return doorConnectivity;
  }

  public Map<String, Map<String, String>> getSpawnConnectivity() {
    return spawnConnectivity;
  }

  public Map<String, String> getKeycardConnectivity() {
    return keycardConnectivity;
  }

  public ImmutableMultimap<Location, Keycard> getKeycardPlacement() {
    return keycardPlacement;
  }

  public ProgressionGraph getProgressionGraph() {
    return progressionGraph;
  }

  private ImmutableMultimap<Location, Keycard> generate() {
    // Map of location to keycard
    ListMultimap<Location, Keycard> keycardPlacement = ArrayListMultimap.create();

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
    locationsToUse.addAll(KeycardConnectivityConsts.LOCATIONS_DUPE_ONLY);

    Collections.shuffle(lowPriority, r);
    placeKeycard(lowPriority, locationsToUse, keycardPlacement);

    // Fill remaining locations with random keycards
    fillLocations(Keycard.values(), locationsToUse, keycardPlacement);

    return ImmutableMultimap.copyOf(keycardPlacement);
  }

  private void placeKeycard(List<Keycard> keycardsToPlace, List<Location> locationsToUse,
      Multimap<Location, Keycard> keycardPlacement) {
    for (int j = 0; j < keycardsToPlace.size(); j++) {
      Keycard k = keycardsToPlace.get(j);
      for (int i = 0; i < MAX_ATTEMPTS; i++) {
        int locationIndex = locationsToUse.size() > 0 ? r.nextInt(locationsToUse.size()) : 0;
        Location l = locationsToUse.get(locationIndex);
        if (!keycardPlacement.containsKey(l)) {
          keycardPlacement.put(l, k);
          locationsToUse.remove(locationIndex);
          System.out.printf("[%s] --> [%s]\n", k, l);
          break;
        }
      }
    }
  }

  private void fillLocations(Keycard[] keycardsToPlace, List<Location> locationsToUse,
      Multimap<Location, Keycard> keycardPlacement) {
    for (Location l : locationsToUse) {
      Keycard k = Utils.getRandom(keycardsToPlace, r);
      keycardPlacement.put(l, k);
    }
  }

  private boolean verify() {
    ImmutableNetwork<Level, Door> stationConnectivity =
        StationConnectivityConsts.getDefaultNetwork();
    ProgressionGraph pg = new ProgressionGraph(stationConnectivity, keycardPlacement, 0L);
    return pg.verify();
  }

  public static void main(String[] args) {
    Random r = new Random(0L);
    ProgressionGenerator pg =
        new ProgressionGenerator(null, StationConnectivityConsts.getDefaultNetwork(), r);
    System.out.println(pg.getKeycardPlacement());
  }
}
