package randomizers.gameplay.filters;

import java.util.List;
import java.util.logging.Logger;
import com.google.common.collect.Lists;
import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import randomizers.gameplay.filters.rules.ContainerSpawnRule;
import utils.CustomRuleHelper;
import utils.LevelConsts;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, SettingsJson settings) {

    if (settings.getGameplaySettings()
        .getItemSpawnSettings()
        .getRules() == null) {
      return;
    }

    List<String> doNotOutput = Lists.newArrayList();
    doNotOutput.addAll(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
    if (!settings.getGameplaySettings()
        .getMoreGuns()) {
      Logger.getGlobal()
          .info("Removing randomized weapons from containers");
      doNotOutput.add("Randomizer");
    }

    for (GenericRuleJson grj : settings.getGameplaySettings()
        .getItemSpawnSettings()
        .getRules()) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS);
      grj.addDoNotOutputTags(doNotOutput);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rules.add(new ContainerSpawnRule(database, crh));
    }
  }
}
