package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import json.SpawnPresetJson;
import randomizers.gameplay.filters.rules.ContainerSpawnRule;
import utils.CustomFilterHelper;
import utils.LevelConsts;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, SettingsJson settings) {

    if (settings.getGameplaySettings() == null || settings.getGameplaySettings().getItemSpawnSettings() == null
        || settings.getGameplaySettings().getItemSpawnSettings().getRules() == null) {
      return;
    }

    SpawnPresetJson spawnPreset = settings.getGameplaySettings().getItemSpawnSettings();
    for (GenericRuleJson rule : spawnPreset.getRules()) {
      rule.addDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
      rule.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS);
      if (!settings.getGameplaySettings().getMoreGuns()) {
        rule.addDoNotOutputTag("Randomizer");
      }
    }

    CustomFilterHelper filterHelper = new CustomFilterHelper(settings.getGameplaySettings().getItemSpawnSettings(),
        database);
    rules.add(new ContainerSpawnRule(filterHelper));
  }
}
