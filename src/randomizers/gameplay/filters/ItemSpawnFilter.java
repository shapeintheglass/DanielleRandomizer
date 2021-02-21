package randomizers.gameplay.filters;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.ArchetypeSwapRule;
import utils.CustomRuleHelper;
import utils.LevelConsts;

public class ItemSpawnFilter extends BaseFilter {
  /**
   * Pre-made combination of rules that specifically filters items in certain settings.
   */
  public ItemSpawnFilter(TaggedDatabase database, Settings s) {
    if (s.getItemSettings().getItemSpawnSettings().getFiltersList().size() == 0) {
      return;
    }

    List<String> doNotOutput = Lists.newArrayList();
    doNotOutput.addAll(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
    if (!s.getItemSettings().getMoreGuns()) {
      Logger.getGlobal().info("Removing randomized weapons");
      doNotOutput.add("Randomizer");
    }

    for (GenericSpawnPresetRule grj : s.getItemSettings().getItemSpawnSettings().getFiltersList()) {
      GenericSpawnPresetRule copy = grj.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
          .addAllDoNotOutputTags(doNotOutput)
          .build();
      CustomRuleHelper crh = new CustomRuleHelper(copy);
      rules.add(new ArchetypeSwapRule(database, crh));
    }
  }
}
