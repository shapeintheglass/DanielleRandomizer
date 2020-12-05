package randomizers.gameplay.filters;

import java.util.List;
import java.util.logging.Logger;
import com.google.common.collect.Lists;
import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import randomizers.gameplay.filters.rules.ArchetypeSwapRule;
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
        
    List<String> doNotOutput = Lists.newArrayList();
    doNotOutput.addAll(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
    if (!s.getGameplaySettings().getMoreGuns()) {
      Logger.getGlobal().info("Removing randomized weapons");
      doNotOutput.add("Randomizer");
    }

    for (GenericRuleJson grj : s.getGameplaySettings().getItemSpawnSettings().getRules()) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS);
      grj.addDoNotOutputTags(doNotOutput);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rules.add(new ArchetypeSwapRule(database, crh));
    }
  }
}
