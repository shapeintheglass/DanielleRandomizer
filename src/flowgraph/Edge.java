package flowgraph;

import org.jdom2.Element;

public class Edge {
  Element e;

  public Edge(String nodeIn, String nodeOut, String portIn, String portOut) {
    e = new Element("Edge")
        .setAttribute("nodeIn", nodeIn)
        .setAttribute("nodeOut", nodeOut)
        .setAttribute("portIn", portIn)
        .setAttribute("portOut", portOut)
        .setAttribute("enabled", "1");
  }
  
  public Element get() {
    return e;
  }
}
