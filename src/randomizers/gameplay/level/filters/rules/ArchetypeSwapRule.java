package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.Utils;

/**
 * Changes the archetype of a level entity.
 * 
 * @author Kida
 *
 */
public class ArchetypeSwapRule implements Rule {

  private TaggedDatabase database;
  private CustomRuleHelper crh;

  public ArchetypeSwapRule(TaggedDatabase database, CustomRuleHelper crh) {
    this.database = database;
    this.crh = crh;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Check if input tag matches
    if (e.getAttributeValue("Archetype") != null && e.getAttributeValue("EntityClass") != null) {
      return crh.trigger(database, e.getAttributeValue("Archetype"), e.getAttributeValue("Name"));
    } else {
      return false;
    }
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    Element toSwap = crh.getEntityToSwap(database, r);
    String newArchetype = Utils.getNameForEntity(toSwap);
    e.setAttribute("Archetype", newArchetype);
  }
}
