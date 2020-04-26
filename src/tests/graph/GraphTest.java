package tests.graph;

import graph.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {

  
  private static final String NODE_1 = "foo";
  private static final String NODE_2 = "bar";
  private static final String NODE_3 = "baz";
  private static final String NODE_4 = "bop";
  
  
  Graph<String> g;

  @Before
  public void setup() {
    g = new Graph<>();
  }

  @Test
  public void testAddEdge() {
    g.addEdge(NODE_1, NODE_2);
    Set<String> expectedNodes = new HashSet<>();
    expectedNodes.add(NODE_1);
    expectedNodes.add(NODE_2);
    Map<String, Set<String>> expectedEdges = new HashMap<>();
    expectedEdges.put(NODE_1, new HashSet<String>());
    expectedEdges.get(NODE_1).add(String.format("%s <-> %s", NODE_1, NODE_2));
    expectedEdges.put(NODE_2, new HashSet<String>());
    expectedEdges.get(NODE_2).add(String.format("%s <-> %s", NODE_2, NODE_1));
    Map<String, Integer> expectedDegrees = new HashMap<>();
    expectedDegrees.put(NODE_1, 1);
    expectedDegrees.put(NODE_2, 1);
    
    Assert.assertEquals(expectedNodes, g.getNodes());
    Assert.assertTrue(g.areConnected(NODE_1, NODE_2));
    Assert.assertEquals(expectedEdges, g.getEdges());
    Assert.assertEquals(expectedDegrees, g.getDegrees());
  }

  @Test
  public void testIsConnected() {
    g.addEdge(NODE_1, NODE_2);
    g.addEdge(NODE_3, NODE_4);
    
    Assert.assertFalse(g.isConnected());
  }
  
}
