package randomizers.gameplay.level.filters.rules;

import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import utils.DatabaseUtils;
import utils.Utils;

/**
 * Helper utility for modifying items that are spawned in containers.
 * 
 * @author Kida
 *
 */
public class ContainerSpawnRule implements Rule {

  private static final String ITEM_ADD_KEYWORD = "Inventory:ItemAdd";

  private TaggedDatabase database;
  private Random r;

  public ContainerSpawnRule(TaggedDatabase database, Random r) {
    this.database = database;
    this.r = r;
  }

  public boolean trigger(Element e) {
    // Broadly apply to all flowgraph scripts
    List<Element> nodes = getNodes(e);
    if (nodes == null) {
      return false;
    }
    for (Element n : nodes) {
      if (n.getAttributeValue("Class").equals(ITEM_ADD_KEYWORD)) {
        return true;
      }
    }
    return false;
  }

  public void apply(Element e) {
    // Iterate through nodes until we find an item add node
    List<Element> nodes = getNodes(e);
    for (Element n : nodes) {
      if (n.getAttributeValue("Class").equals(ITEM_ADD_KEYWORD)) {
        Element inputs = n.getChild("Inputs");
        String archetypeStr = inputs.getAttributeValue("archetype");
        if (archetypeStr == null || archetypeStr.isEmpty()) {
          return;
        }
        Element fullEntity = database.getEntityByName(Utils.stripPrefix(archetypeStr));
        if (fullEntity == null) {
          return;
        }
        // Replace with something of the same type
        String tag = fullEntity.getAttributeValue("Class");
        Element toSwap = DatabaseUtils.getRandomEntityByTag(database, r, tag);

        inputs.setAttribute("archetype", Utils.getNameForEntity(toSwap));
        inputs.setAttribute("quantity", "1");
      }
    }
  }

  private List<Element> getNodes(Element e) {
    Element flowGraph = e.getChild("FlowGraph");
    if (flowGraph == null) {
      return null;
    }
    Element nodesContainer = flowGraph.getChild("Nodes");
    if (nodesContainer == null) {
      return null;
    }
    return nodesContainer.getChildren();
  }
}
