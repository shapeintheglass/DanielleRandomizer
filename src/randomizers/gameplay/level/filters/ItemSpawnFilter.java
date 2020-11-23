package randomizers.gameplay.level.filters;

import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import randomizers.gameplay.level.filters.rules.ArchetypeSwapRule;
import utils.CustomRuleHelper;
import utils.LevelConsts;

public class ItemSpawnFilter extends BaseFilter {
  /**
   * Pre-made combination of rules that specifically filters items in certain settings.
   */
  public ItemSpawnFilter(TaggedDatabase database, SettingsJson s) {
    if (s.getGameplaySettings().getItemSpawnSettings().getRules() == null) {
      return;
    }

    for (GenericRuleJson grj : s.getGameplaySettings().getItemSpawnSettings().getRules()) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS);
      grj.addDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rules.add(new ArchetypeSwapRule(database, crh));
    }
  }
}
