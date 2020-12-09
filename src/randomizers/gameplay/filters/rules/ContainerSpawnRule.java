package randomizers.gameplay.filters.rules;

import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jdom2.Element;
import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.LevelConsts;
import utils.Utils;

/**
 * Helper utility for modifying items that are spawned in containers.
 * 
 * @author Kida
 *
 */
public class ContainerSpawnRule implements Rule {

  private static final int MAX_ATTEMPTS = 100;

  private static final String ITEM_ADD_KEYWORD = "Inventory:ItemAdd";

  private TaggedDatabase database;
  private CustomRuleHelper crh;

  public ContainerSpawnRule(TaggedDatabase database, CustomRuleHelper crh) {
    this.database = database;
    this.crh = crh;
  }

  public boolean trigger(Element e, Random r, String filename) {
    // Broadly apply to all flowgraph scripts that spawn an item matching the given
    // rule
    List<Element> nodes = getNodes(e);
    if (nodes == null) {
      return false;
    }
    for (Element n : nodes) {
      if (n.getAttributeValue("Class")
          .equals(ITEM_ADD_KEYWORD)) {
        Element input = n.getChild("Inputs");
        return triggerOnInput(input, e.getAttributeValue("Name"));
      }
    }
    return false;
  }

  public void apply(Element e, Random r, String filename) {
    // Iterate through nodes until we find an item add node
    List<Element> nodes = getNodes(e);
    for (Element n : nodes) {
      if (n.getAttributeValue("Class")
          .equals(ITEM_ADD_KEYWORD)) {
        Element inputs = n.getChild("Inputs");
        String archetypeStr = inputs.getAttributeValue("archetype");
        if (archetypeStr == null || archetypeStr.isEmpty() || !triggerOnInput(inputs,
            filename + LevelConsts.DELIMITER + e.getAttributeValue("Name"))) {
          return;
        }
        Element fullEntity = database.getEntityByName(Utils.stripPrefix(archetypeStr));
        if (fullEntity == null) {
          return;
        }

        // Try different items until we get a valid pickup item
        // TODO: Make this less dumb
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
          Element toSwap = crh.getEntityToSwap(database, r);

          // Ensure that this has the tag "ArkPickups" to represent that it can be added to an
          // inventory
          Set<String> tags = Utils.getTags(toSwap);
          if (tags.contains("ArkPickups")) {
            inputs.setAttribute("archetype", Utils.getNameForEntity(toSwap));
            break;
          }
        }
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

  private boolean triggerOnInput(Element input, String name) {
    String archetype = input.getAttributeValue("archetype");
    return crh.trigger(database, archetype, name);
  }
}
