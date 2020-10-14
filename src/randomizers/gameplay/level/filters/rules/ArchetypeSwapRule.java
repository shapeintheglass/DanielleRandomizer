package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.Utils;
import utils.XmlEntity;

/**
 * Changes the archetype of a level entity.
 * 
 * @author Kida
 *
 */
public class ArchetypeSwapRule implements Rule {

  private TaggedDatabase database;
  private Random r;
  private CustomRuleHelper crh;

  public ArchetypeSwapRule(TaggedDatabase database, Random r, CustomRuleHelper crh) {
    this.database = database;
    this.r = r;
    this.crh = crh;
  }

  @Override
  public boolean trigger(XmlEntity e) {
    // Check if input tag matches
    if (e.hasKey("Archetype") && e.hasKey("EntityClass")) {
      return crh.trigger(database, e.getValue("Archetype"));
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    XmlEntity toSwap = crh.getEntityToSwap(database, r);
    e.setValue("Archetype", Utils.getNameForEntity(toSwap));
  }
}
