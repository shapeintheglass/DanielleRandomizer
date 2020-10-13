package filters;

import rules.ArchetypeSwapRule;
import settings.Settings;
import utils.CustomRuleHelper;

public class ItemSpawnFilter extends BaseFilter {
  /**
   * Pre-made combination of rules that specifically filters items in certain
   * settings.
   */
  public ItemSpawnFilter(Settings s) {
    super("ItemSpawnFilter", s);

    for (CustomRuleHelper crh : s.getItemSpawnSettings().getCustomRuleHelpers()) {
      rules.add(new ArchetypeSwapRule(crh));
    }
  }
}
