package rules;

import databases.TaggedDatabase;
import utils.CustomRuleHelper;
import utils.Utils;
import utils.XmlEntity;

/**
 * Changes the archetype of a level entity.
 * @author Kida
 *
 */
public class ArchetypeSwapRule implements Rule {
  private CustomRuleHelper crh;
  
  TaggedDatabase database;
  
  public ArchetypeSwapRule(TaggedDatabase database, CustomRuleHelper crh) {
    this.database = database;
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
    XmlEntity toSwap = crh.getEntityToSwap(database);
    e.setValue("Archetype", Utils.getNameForEntity(toSwap));
  }
}
