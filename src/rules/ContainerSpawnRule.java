package rules;

import java.util.List;
import java.util.Random;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.Utils;
import utils.XmlEntity;

/**
 * Helper utility for modifying items that are spawned in containers.
 * 
 * @author Kida
 *
 */
public class ContainerSpawnRule implements Rule {

  private static final String ITEM_ADD_KEYWORD = "Inventory:ItemAdd";

  CustomRuleHelper crh;
  TaggedDatabase database;

  public ContainerSpawnRule(CustomRuleHelper crh, Random r) {
    this.crh = crh;
    database = EntityDatabase.getInstance(r);
  }

  public boolean trigger(XmlEntity e) {
    // Broadly apply to all flowgraph scripts
    List<XmlEntity> nodes = getNodes(e);
    if (nodes == null) {
      return false;
    }
    for (XmlEntity n : nodes) {
      if (n.getValue("Class").equals(ITEM_ADD_KEYWORD)) {
        return true;
      }
    }
    return false;
  }

  public void apply(XmlEntity e) {
    // Iterate through nodes until we find an item add node
    List<XmlEntity> nodes = getNodes(e);
    for (XmlEntity n : nodes) {
      if (n.getValue("Class").equals(ITEM_ADD_KEYWORD)) {
        XmlEntity inputs = n.getSubEntityByTagName("Inputs");
        String archetypeStr = inputs.getValue("archetype");
        if (archetypeStr == null || archetypeStr.isEmpty()) {
          return;
        }
        XmlEntity fullEntity = database.getEntityByName(Utils.stripPrefix(archetypeStr));
        if (fullEntity == null) {
          return;
        }
        // Replace with something of the same type
        String tag = fullEntity.getValue("Class");
        XmlEntity toSwap = database.getRandomEntityByTag(tag);
        
        inputs.setValue("archetype", Utils.getNameForEntity(toSwap));
        inputs.setValue("quantity", "1");
      }
    }
  }

  private List<XmlEntity> getNodes(XmlEntity e) {
    XmlEntity flowGraph = e.getSubEntityByTagName("FlowGraph");
    if (flowGraph == null) {
      return null;
    }
    XmlEntity nodesContainer = flowGraph.getSubEntityByTagName("Nodes");
    if (nodesContainer == null) {
      return null;
    }
    return nodesContainer.getSubEntities();
  }
}
