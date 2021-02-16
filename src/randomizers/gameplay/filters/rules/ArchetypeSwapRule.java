package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.ItemMultiplierHelper;
import utils.LevelConsts;
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
      return crh.trigger(database, e.getAttributeValue("Archetype"), filename + LevelConsts.DELIMITER + e
          .getAttributeValue("Name"));
    } else {
      return false;
    }
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    Element toSwap = crh.getEntityToSwap(database, r);
    String newArchetype = Utils.getNameForEntity(toSwap);

    // Adjust multiplier quantity if this entity has a count override
    Element properties2 = e.getChild("Properties2");
    if (properties2 != null) {
      String iCountOverride = properties2.getAttributeValue("iCountOverride");
      if (iCountOverride != null && !iCountOverride.equals("0") && !iCountOverride.equals("1")) {
        int quantity = ItemMultiplierHelper.getMultiplierForEntity(toSwap, r);
        properties2.setAttribute("iCountOverride", Integer.toString(quantity));
      }
    }
    e.setAttribute("Archetype", newArchetype);
  }
}
