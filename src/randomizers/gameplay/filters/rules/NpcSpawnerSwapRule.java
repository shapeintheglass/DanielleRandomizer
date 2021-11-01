package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.LevelConsts;

/**
 * Changes the archetype of the entity spawned by an ArkNpcSpawner.
 * 
 * @author Kida
 *
 */
public class NpcSpawnerSwapRule implements Rule {

  private static final String S_NPC_ARCHETYPE = "sNpcArchetype";
  private TaggedDatabase database;
  private CustomRuleHelper crh;

  public NpcSpawnerSwapRule(TaggedDatabase database, CustomRuleHelper crh) {
    this.database = database;
    this.crh = crh;
  }

  public CustomRuleHelper getCustomRuleHelper() {
    return crh;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Triggers on ArkNpcSpawners
    if (e.getAttributeValue("EntityClass") != null && e.getAttributeValue("EntityClass").equals("ArkNpcSpawner")) {
      // Parse to get the spawned entity
      Element properties = e.getChild("Properties");
      if (properties == null) {
        return false;
      }
      String spawnedEntityName = properties.getAttributeValue(S_NPC_ARCHETYPE);
      String entityLevelName = filename + LevelConsts.DELIMITER + e.getAttributeValue("Name");
      return crh.trigger(database, spawnedEntityName, entityLevelName);
    } else {
      return false;
    }
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String toSwapStr = crh.getEntityToSwapStr(database, r);
    Element properties = e.getChild("Properties");
    properties.setAttribute(S_NPC_ARCHETYPE, toSwapStr);
  }
}
