package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableNetwork;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class NetworkHelper {

  private ImmutableNetwork<Level, Door> network;
  private Set<Door> toRemove;

  public NetworkHelper(ImmutableNetwork<Level, Door> network, Set<Door> toRemove) {
    this.network = network;
    this.toRemove = toRemove;
  }

  public boolean isConnected(Level from, Level to) {
    Set<Level> visited = Sets.newHashSet();
    Stack<Level> toVisit = new Stack<>();
    List<Level> visitOrder = Lists.newArrayList();

    toVisit.push(from);

    while (!toVisit.isEmpty()) {
      Level l = toVisit.pop();
      visitOrder.add(l);
      if (l == to) {
        return true;
      }

      visited.add(l);

      for (Level neighbor : network.adjacentNodes(l)) {
        if (!visited.contains(neighbor)) {
          //Door d = network.edgeConnectingOrNull(l, neighbor);
          Set<Door> connectingDoors = Sets.newHashSet();
          connectingDoors.addAll(network.edgesConnecting(l, neighbor));
          connectingDoors.removeAll(toRemove);
          if (!connectingDoors.isEmpty()) {
            toVisit.push(neighbor);
          }
        }
      }
    }

    return false;
  }

  public boolean isConnected(Level start) {
    Set<Level> notVisited = Sets.newHashSet(network.nodes());
    Stack<Level> toVisit = new Stack<>();
    
    toVisit.push(start);

    while (!toVisit.isEmpty()) {
      Level l = toVisit.pop();

      notVisited.remove(l);

      for (Level neighbor : network.adjacentNodes(l)) {
        if (notVisited.contains(neighbor)) {
          Set<Door> connectingDoors = Sets.newHashSet();
          connectingDoors.addAll(network.edgesConnecting(l, neighbor));
          connectingDoors.removeAll(toRemove);
          if (!connectingDoors.isEmpty()) {
            toVisit.push(neighbor);
          }
        }
      }
    }

    return notVisited.isEmpty();
  }
}
