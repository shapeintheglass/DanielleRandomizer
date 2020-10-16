package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;

/**
 * Changes the archetype of the entity spawned by an ArkNpcSpawner.
 * 
 * @author Kida
 *
 */
public class NpcSpawnerSwapRule implements Rule {

  private TaggedDatabase database;
  private Random r;
  private CustomRuleHelper crh;

  public NpcSpawnerSwapRule(TaggedDatabase database, Random r, CustomRuleHelper crh) {
    this.database = database;
    this.r = r;
    this.crh = crh;
  }

  @Override
  public boolean trigger(Element e) {
    // Triggers on ArkNpcSpawners
    if (e.getAttributeValue("EntityClass") != null && e.getAttributeValue("EntityClass").equals("ArkNpcSpawner")) {
      // Parse to get the spawned entity
      Element properties = e.getChild("Properties");
      if (properties == null) {
        return false;
      }
      String spawnedEntityName = properties.getAttributeValue("sNpcArchetype");
      return crh.trigger(database, spawnedEntityName);
    } else {
      return false;
    }
  }

  @Override
  public void apply(Element e) {
    String toSwapStr = crh.getEntityToSwapStr(database, r);
    Element properties = e.getChild("Properties");
    properties.setAttribute("sNpcArchetype", toSwapStr);
  }
}
