package rules;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.XmlEntity;

/**
 * Changes the archetype of the entity spawned by an ArkNpcSpawner.
 * 
 * @author Kida
 *
 */
public class NpcSpawnerSwapRule implements Rule {

  TaggedDatabase database;
  private CustomRuleHelper crh;

  public NpcSpawnerSwapRule(TaggedDatabase database, CustomRuleHelper crh) {
    this.database = database;
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
      return crh.trigger(database, spawnedEntityName);
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    String toSwapStr = crh.getEntityToSwapStr(database);
    XmlEntity properties = e.getSubEntityByTagName("Properties");
    properties.setValue("sNpcArchetype", toSwapStr);
  }
}
