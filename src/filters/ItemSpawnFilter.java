package filters;

import databases.EntityDatabase;
import databases.TaggedDatabase;
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
    
    TaggedDatabase database = EntityDatabase.getInstance(s.getRandom());

    for (CustomRuleHelper crh : s.getItemSpawnSettings().getCustomRuleHelpers()) {
      rules.add(new ArchetypeSwapRule(database, crh));
    }
  }
}
