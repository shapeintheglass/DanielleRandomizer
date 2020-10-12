package filters;

import rules.CustomRuleHelper;
import rules.ArchetypeSwapRule;
import settings.Settings;

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
