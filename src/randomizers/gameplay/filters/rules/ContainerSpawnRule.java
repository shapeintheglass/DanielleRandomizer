package randomizers.gameplay.filters.rules;

import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import utils.CustomItemFilterHelper;
import utils.ItemMultiplierHelper;
import utils.Utils;

/**
 * Helper utility for modifying items that are spawned in containers.
 *
 */
public class ContainerSpawnRule implements Rule {

  private static final String ITEM_ADD_KEYWORD = "Inventory:ItemAdd";
  private static final String SPAWN_ARCHETYPE_KEYWORD = "Entity:SpawnArchetype";

  private CustomItemFilterHelper filterHelper;

  public ContainerSpawnRule(CustomItemFilterHelper filterHelper) {
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
      String classAttr = n.getAttributeValue("Class"); 
      if (classAttr.equals(ITEM_ADD_KEYWORD) || classAttr.equals(SPAWN_ARCHETYPE_KEYWORD)) {
        return true;
      }
    }
    return false;
  }

  public void apply(Element e, Random r, String filename) {
    // Iterate through nodes until we find an item add node
    List<Element> nodes = getNodes(e);

    for (Element n : nodes) {
      String classAttr = n.getAttributeValue("Class"); 
      if (classAttr.equals(ITEM_ADD_KEYWORD)) {
        Element inputs = n.getChild("Inputs");
        String archetypeStr = inputs.getAttributeValue("archetype");
        if (archetypeStr == null || archetypeStr.isEmpty() || !filterHelper.trigger(archetypeStr, null)) {
          continue;
        }

        // Replace with a valid pickup item
        Element toSwap = filterHelper.getPickupEntity(archetypeStr, null, r);
        if (toSwap != null) {
          inputs.setAttribute("archetype", Utils.getNameForEntity(toSwap));
          int multiplier = ItemMultiplierHelper.getMultiplierForEntity(toSwap, r);
          inputs.setAttribute("quantity", Integer.toString(multiplier));
        }
      } else if (classAttr.equals(SPAWN_ARCHETYPE_KEYWORD)) {
        Element inputs = n.getChild("Inputs");
        String archetypeStr = inputs.getAttributeValue("Archetype");
        if (archetypeStr == null || archetypeStr.isEmpty() || !filterHelper.trigger(archetypeStr, null)) {
          continue;
        }

        // Replace with an appropriate item
        Element toSwap = filterHelper.getEntity(archetypeStr, filename, r);
        if (toSwap != null) {
          inputs.setAttribute("Archetype", Utils.getNameForEntity(toSwap));
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
