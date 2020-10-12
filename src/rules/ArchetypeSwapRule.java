package rules;

import utils.XmlEntity;

/**
 * Changes the archetype of a level entity.
 * @author Kida
 *
 */
public class ArchetypeSwapRule implements Rule {
  private CustomRuleHelper crh;

  public ArchetypeSwapRule(CustomRuleHelper crh) {
    this.crh = crh;
  }

  @Override
  public boolean trigger(XmlEntity e) {
    // Check if input tag matches
    if (e.hasKey("Archetype")) {
      return crh.trigger(e.getValue("Archetype"));
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    e.setValue("Archetype", crh.getEntityToSwap());
  }
}
