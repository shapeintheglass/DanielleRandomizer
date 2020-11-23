package randomizers.gameplay.level.filters;

import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import randomizers.gameplay.level.filters.rules.NpcSpawnerSwapRule;
import utils.CustomRuleHelper;
import utils.LevelConsts;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(TaggedDatabase database, SettingsJson s) {
    if (s.getGameplaySettings().getEnemySpawnSettings().getRules() == null) {
      return;
    }
    for (GenericRuleJson grj : s.getGameplaySettings().getEnemySpawnSettings().getRules()) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS);
      grj.addDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rules.add(new NpcSpawnerSwapRule(database, crh));
    }
  }
}
