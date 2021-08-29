package utils;

import java.util.Set;
import java.util.Stack;
import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableGraph;
import utils.StationConnectivityConsts.Door;

public class GraphHelper {
  public static boolean isConnected(ImmutableGraph<Door> network, Set<Door> toRemove, Door from,
      Door to) {
    Set<Door> visited = Sets.newHashSet();
    Stack<Door> toVisit = new Stack<>();

    toVisit.push(from);

    while (!toVisit.isEmpty()) {
      Door d = toVisit.pop();
      visited.add(d);
      if (toRemove.contains(d)) {
        continue;
      }
      if (d == to) {
        return true;
      }

      for (Door neighbor : network.adjacentNodes(d)) {
        if (!visited.contains(neighbor) && !toRemove.contains(neighbor)) {
          toVisit.push(neighbor);
        }
      }
    }

    return false;
  }

  public static boolean isConnected(ImmutableGraph<Door> network, Set<Door> toRemove, Door from,
      Set<Door> to) {
    Set<Door> visited = Sets.newHashSet();
    Stack<Door> toVisit = new Stack<>();

    toVisit.push(from);

    while (!toVisit.isEmpty()) {
      Door d = toVisit.pop();
      visited.add(d);
      if (toRemove.contains(d)) {
        continue;
      }
      if (to.contains(d)) {
        return true;
      }

      for (Door neighbor : network.adjacentNodes(d)) {
        if (!visited.contains(neighbor) && !toRemove.contains(neighbor)) {
          toVisit.push(neighbor);
        }
      }
    }

    return false;
  }

  public static boolean isConnected(ImmutableGraph<Door> network, Door start) {
    Set<Door> notVisited = Sets.newHashSet(network.nodes());
    Stack<Door> toVisit = new Stack<>();

    toVisit.push(start);

    while (!toVisit.isEmpty()) {
      Door d = toVisit.pop();
      notVisited.remove(d);
      for (Door neighbor : network.adjacentNodes(d)) {
        if (notVisited.contains(neighbor)) {
          toVisit.push(neighbor);
        }
      }
    }

    return notVisited.isEmpty();
  }
}
