package flowgraph;

import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;

public class Node {
  private static final String POS = "0,0,0";
  
  public String id;
  
  private Element e;
  
  public Node(String id, String classType, ImmutableMap<String, String> inputs) {
    e = new Element("Node")
        .setAttribute("Id", id)
        .setAttribute("Class", classType)
        .setAttribute("pos", POS);
    Element in = new Element("Inputs");
    for (String key : inputs.keySet()) {
      in.setAttribute(key, inputs.get(key));
    }
    e.addContent(in);
  }
  
  public Element get() {
    return e;
  }
}
