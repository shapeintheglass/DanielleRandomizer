package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class SimpleGraph<T> {

  private T root;

  private Map<T, Set<T>> connections;
  private Set<Edge<T>> edges;

  public static class Edge<T> {
    String name1;
    String name2;
    public T n1;
    public T n2;

    public Edge(String name1, String name2, T n1, T n2) {
      this.name1 = name1;
      this.name2 = name2;
      this.n1 = n1;
      this.n2 = n2;
    }

    @Override
    public boolean equals(Object other) {
      Edge<T> otherEdge = (Edge<T>) other;
      return otherEdge.n1 == this.n1 && otherEdge.n2 == this.n2;
    }

    @Override
    public String toString() {
      return String.format("%s (%s) --> %s (%s)", n1.toString(), name1, n2.toString(), name2);
    }

    public String getNode1() {
      return n1.toString();
    }

    public String getNode2() {
      return n2.toString();
    }

    public String getName1() {
      return name1;
    }

    public String getName2() {
      return name2;
    }
  }

  public SimpleGraph() {
    connections = new HashMap<T, Set<T>>();
    root = null;
    edges = new HashSet<Edge<T>>();
  }

  public void clear() {
    connections.clear();
  }

  public Set<T> getNodes() {
    return connections.keySet();
  }

  public Set<Edge<T>> getEdges() {
    return edges;
  }

  public Map<T, Set<String>> getEdgesAsStrings() {
    Map<T, Set<String>> edges = new HashMap<>();
    for (T node : connections.keySet()) {
      edges.put(node, new HashSet<String>());
      for (T neighbor : connections.get(node)) {
        edges.get(node)
             .add(String.format("%s <-> %s", node, neighbor));
      }
    }
    return edges;
  }

  public Map<T, Integer> getDegrees() {
    Map<T, Integer> degrees = new HashMap<>();
    for (T node : connections.keySet()) {
      degrees.put(node, connections.get(node)
                                   .size());
    }
    return degrees;
  }

  public void addEdge(String name1, String name2, T n1, T n2) {
    createNodeIfNotExists(n1);
    createNodeIfNotExists(n2);
    connect(n1, n2);
    Edge<T> edge = new Edge<>(name1, name2, n1, n2);
    edges.add(edge);
  }

  private void connect(T n1, T n2) {
    connections.get(n1)
               .add(n2);
    connections.get(n2)
               .add(n1);
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
    return connections.containsKey(t1) && connections.get(t1)
                                                     .contains(t2);
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
