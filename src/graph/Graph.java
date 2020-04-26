package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Graph<T> {

  private T root;
  
  private Map<T, Set<T>> connections;

  public Graph() {
    connections = new HashMap<T, Set<T>>();
    root = null;
  }
  
  public void clear() {
    connections.clear();
  }
  
  public Set<T> getNodes() {
    return connections.keySet();
  }
  
  public Map<T, Set<String>> getEdges() {
    Map<T, Set<String>> edges = new HashMap<>();
    for (T node : connections.keySet()) {
      edges.put(node, new HashSet<String>());
      for (T neighbor : connections.get(node)) {
        edges.get(node).add(String.format("%s <-> %s", node, neighbor));
      }
    }
    return edges;
  }
  
  public Map<T, Integer> getDegrees() {
    Map<T, Integer> degrees = new HashMap<>();
    for (T node : connections.keySet()) {
      degrees.put(node, connections.get(node).size());
    }
    return degrees;
  }

  public void addEdge(T n1, T n2) {
    createNodeIfNotExists(n1);
    createNodeIfNotExists(n2);
    connect(n1, n2);
  }
  
  private void connect(T n1, T n2) {
    connections.get(n1).add(n2);
    connections.get(n2).add(n1);
  }
  
  private void createNodeIfNotExists(T n) {
    if (root == null) {
      root = n;
    }
    if (!connections.containsKey(n)) {
      connections.put(n, new HashSet<T>());
    }
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    
    for (T node : connections.keySet()) {
      Set<T> neighbors = connections.get(node);
      s.append(String.format("%s (%d neighbors):\n\t", node, neighbors.size()));
      for (T neighbor : neighbors) {
        s.append(String.format("%s ", neighbor));
      }
      s.append('\n');
    }
    return s.toString();
  }
  
  public boolean areConnected(T t1, T t2) {
    return connections.containsKey(t1) && connections.get(t1).contains(t2);
  }
  
  public boolean isConnected() {
    Set<T> visited = new HashSet<>();
    Stack<T> toVisit = new Stack<>();
    toVisit.add(root);
    
    while (!toVisit.isEmpty()) {
      if (visited.equals(connections.keySet())) {
        return true;
      }
      T node = toVisit.pop();
      visited.add(node);
      
      Set<T> toAdd = connections.get(node);
      
      for (T neighbor : toAdd) {
        if (!visited.contains(neighbor)) {
          toVisit.add(neighbor);
        }
      }
      
    }

    return false;
  }
}
