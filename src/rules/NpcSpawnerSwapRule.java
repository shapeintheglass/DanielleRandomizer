package rules;

import utils.CustomRuleHelper;
import utils.XmlEntity;

/**
 * Changes the archetype of the entity spawned by an ArkNpcSpawner.
 * 
 * @author Kida
 *
 */
public class NpcSpawnerSwapRule implements Rule {

  private CustomRuleHelper crh;

  public NpcSpawnerSwapRule(CustomRuleHelper crh) {
    this.crh = crh;
  }

  @Override
  public boolean trigger(XmlEntity e) {
    // Triggers on ArkNpcSpawners
    if (e.hasKey("EntityClass") && e.getValue("EntityClass").equals("ArkNpcSpawner")) {
      // Parse to get the spawned entity
      XmlEntity properties = e.getSubEntityByTagName("Properties");
      if (properties == null) {
        return false;
      }
      String spawnedEntityName = properties.getValue("sNpcArchetype");
      return crh.trigger(spawnedEntityName);
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    String toSwapStr = crh.getEntityToSwapStr();
    XmlEntity properties = e.getSubEntityByTagName("Properties");
    properties.setValue("sNpcArchetype", toSwapStr);
  }
}
