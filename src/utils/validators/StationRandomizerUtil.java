package utils.validators;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableNetwork;

import randomizers.generators.StationGenerator;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class StationRandomizerUtil {

  private static final int NUM_ITERATIONS = 1000;

  public static void main(String[] args) {
    Map<Level, Map<Level, Integer>> levelCounts = new HashMap<Level, Map<Level, Integer>>();
    Map<Door, Map<Door, Integer>> doorCounts = new HashMap<Door, Map<Door, Integer>>();

    List<Level> levelsAlphabetized = Lists.newArrayList(StationGenerator.LEVELS_TO_PROCESS);
    Collections.sort(levelsAlphabetized);
    List<Door> doorsAlphabetized = Lists.newArrayList(StationGenerator.DOORS_TO_PROCESS);
    Collections.sort(doorsAlphabetized);

    for (Level l : levelsAlphabetized) {
      levelCounts.put(l, new HashMap<Level, Integer>());
    }

    for (Door d : doorsAlphabetized) {
      doorCounts.put(d, new HashMap<Door, Integer>());
    }

    Random r = new Random();

    
    ImmutableBiMap<Level, String> levelsToIds = new ImmutableBiMap.Builder<Level, String>()
        .put(Level.ARBORETUM, "1713490239386284818")
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

    for (int i = 0; i < NUM_ITERATIONS; i++) {
      StationGenerator connectivity = new StationGenerator(r.nextLong(), levelsToIds);
      ImmutableNetwork<Level, Door> network = connectivity.getNetwork();

      for (Level l : levelsAlphabetized) {
        Set<Level> neighbors = network.adjacentNodes(l);
        for (Level n : neighbors) {
          int initialCount = levelCounts.get(l).containsKey(n) ? levelCounts.get(l).get(n) : 0;
          levelCounts.get(l).put(n, initialCount + 1);
        }
      }

      for (Door d : doorsAlphabetized) {
        EndpointPair<Level> connection = network.incidentNodes(d);
        Optional<Door> parallelDoor = network.edgeConnecting(connection.nodeV(), connection.nodeU());
        if (parallelDoor.isPresent()) {
          Door n = parallelDoor.get();
          int initialCount = doorCounts.get(d).containsKey(n) ? doorCounts.get(d).get(n) : 0;
          doorCounts.get(d).put(n, initialCount + 1);
        }
      }
    }

    System.out.println("LEVEL CONNECTIVITY STATS\n");

    for (Level l : levelsAlphabetized) {
      System.out.printf("%s\n", l.toString());
      Map<Level, Integer> neighborCounts = levelCounts.get(l);

      for (Level n : levelsAlphabetized) {
        if (!neighborCounts.containsKey(n)) {
          continue;
        }
        int count = neighborCounts.get(n);
        float percent = (float) (100.00 * count / NUM_ITERATIONS);
        System.out.printf("\t%.2f%% (%d)\t\t%s\n", percent, count, n.toString());
      }
    }

    System.out.println("DOOR CONNECTIVITY STATS\n");

    for (Door d : doorsAlphabetized) {
      System.out.printf("%s\n", d.toString());
      Map<Door, Integer> parallelDoors = doorCounts.get(d);

      for (Door n : doorsAlphabetized) {
        if (!parallelDoors.containsKey(n)) {
          continue;
        }
        int count = parallelDoors.get(n);
        float percent = (float) (100.00 * count / NUM_ITERATIONS);
        System.out.printf("\t%.2f%% (%d)\t\t%s\n", percent, count, n.toString());
      }
    }
  }
}
