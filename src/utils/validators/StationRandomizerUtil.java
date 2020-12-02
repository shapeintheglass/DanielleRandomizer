package utils.validators;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableNetwork;
import randomizers.gameplay.filters.StationConnectivityFilter;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class StationRandomizerUtil {


  private static final int NUM_ITERATIONS = 1000;

  public static void main(String[] args) {
    Map<Level, Map<Level, Integer>> levelCounts = new HashMap<Level, Map<Level, Integer>>();
    Map<Door, Map<Door, Integer>> doorCounts = new HashMap<Door, Map<Door, Integer>>();

    List<Level> levelsAlphabetized = Lists.newArrayList(Level.values());
    Collections.sort(levelsAlphabetized);
    List<Door> doorsAlphabetized = Lists.newArrayList(Door.values());
    Collections.sort(doorsAlphabetized);

    for (Level l : levelsAlphabetized) {
      levelCounts.put(l, new HashMap<Level, Integer>());
    }

    for (Door d : doorsAlphabetized) {
      doorCounts.put(d, new HashMap<Door, Integer>());
    }

    Random r = new Random();

    for (int i = 0; i < NUM_ITERATIONS; i++) {
      StationConnectivityFilter connectivity = new StationConnectivityFilter(r.nextLong());
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
        Optional<Door> parallelDoor =
            network.edgeConnecting(connection.nodeV(), connection.nodeU());
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
