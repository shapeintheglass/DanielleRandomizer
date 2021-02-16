package randomizers.gameplay.filters.rules;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.jdom2.Element;

import utils.CustomFilterHelper;
import utils.ItemMultiplierHelper;
import utils.Utils;

/**
 * Helper utility for modifying items that are spawned in containers.
 *
 */
public class ContainerSpawnRule implements Rule {

  private static final int MAX_ATTEMPTS = 100;

  private static final String ITEM_ADD_KEYWORD = "Inventory:ItemAdd";

  private CustomFilterHelper filterHelper;

  public ContainerSpawnRule(CustomFilterHelper filterHelper) {
    this.filterHelper = filterHelper;
  }

  public boolean trigger(Element e, Random r, String filename) {
    // Broadly apply to all flowgraph scripts that spawn an item matching the given
    // rule
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

  public void apply(Element e, Random r, String filename) {
    // Iterate through nodes until we find an item add node
    List<Element> nodes = getNodes(e);

    for (Element n : nodes) {
      if (n.getAttributeValue("Class").equals(ITEM_ADD_KEYWORD)) {
        Element inputs = n.getChild("Inputs");
        String archetypeStr = inputs.getAttributeValue("archetype");
        if (archetypeStr == null || archetypeStr.isEmpty() || !filterHelper.trigger(archetypeStr, null)) {
          continue;
        }

        // Try different items until we get a valid pickup item
        // TODO: Make this smarter by just removing non-pickup items from the list first
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
          Element toSwap = filterHelper.getEntity(archetypeStr, null, r);

          // Ensure that this has the tag "ArkPickups" to represent that it can be added
          // to an inventory
          Set<String> tags = Utils.getTags(toSwap);
          if (tags.contains("ArkPickups")) {
            inputs.setAttribute("archetype", Utils.getNameForEntity(toSwap));
            int multiplier = ItemMultiplierHelper.getMultiplierForEntity(tags, r);
            inputs.setAttribute("quantity", Integer.toString(multiplier));
            System.out.printf("level: %s archetype: %s multiplier %s\n", filename, inputs.getAttributeValue(
                "archetype"), inputs.getAttributeValue("quantity"));
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
}
