package randomizers.gameplay;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import graph.SimpleGraph;
import json.SettingsJson;
import randomizers.BaseRandomizer;

public class StationRandomizer extends BaseRandomizer {

  public SimpleGraph<Level> station;

  public enum Level {
    LOB, NDIV, PSY, HWLB, SBAY, ARB, LIFE, POWR, CBAY, EXT, GUTS, DSTO, CREW, BRDG,
  }

  public StationRandomizer(SettingsJson s) {
    super(s);
    station = new SimpleGraph<>();

    connect(Level.LOB, Level.NDIV);
    connect(Level.LOB, Level.PSY);
    connect(Level.LOB, Level.HWLB);
    connect(Level.LOB, Level.SBAY);
    connect(Level.LOB, Level.ARB);
    connect(Level.LOB, Level.LIFE);
    connect(Level.EXT, Level.PSY);
    connect(Level.EXT, Level.HWLB);
    connect(Level.EXT, Level.POWR);
    connect(Level.EXT, Level.CBAY);
    connect(Level.EXT, Level.SBAY);
    connect(Level.EXT, Level.ARB);
    connect(Level.ARB, Level.GUTS);
    connect(Level.ARB, Level.DSTO);
    connect(Level.ARB, Level.CREW);
    connect(Level.ARB, Level.BRDG);
    connect(Level.GUTS, Level.PSY);
    connect(Level.GUTS, Level.CBAY);
    connect(Level.GUTS, Level.SBAY);
    connect(Level.CBAY, Level.LIFE);
    connect(Level.LIFE, Level.POWR);
  }

  public boolean randomizeConnections() {
    // Save the degrees for each part of the station
    Map<Level, Set<String>> edges = station.getEdgesAsStrings();
    logger.info(edges.toString());
    station.clear();

    List<Level> availableNodes = new LinkedList<>();
    for (Level l : edges.keySet()) {
      int degree = edges.get(l)
                        .size();
      for (int i = 0; i < degree; i++) {
        availableNodes.add(l);
      }
    }
    Collections.shuffle(availableNodes);

    while (!availableNodes.isEmpty()) {
      Level n1 = availableNodes.remove(0);

      ListIterator<Level> iterator = availableNodes.listIterator();
      Level n2 = iterator.next();
      // Avoid connecting a node to itself or adding a duplicate edge
      while (iterator.hasNext() && (n1 == n2 || station.areConnected(n1, n2))) {
        n2 = iterator.next();
      }
      iterator.remove();

      if (n1 == n2 || station.areConnected(n1, n2)) {
        logger.info(String.format("Adding a duplicate connection! %s <--> %s", n1, n2));
        return false;
      }

      station.addEdge(n1, n2);
    }
    return true;
  }

  private void connect(Level m1, Level m2) {
    station.addEdge(m1, m2);
  }

  @Override
  public void randomize() {
    // TODO Auto-generated method stub

  }
}
