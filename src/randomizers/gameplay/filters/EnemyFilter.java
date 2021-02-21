package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.NpcSpawnerSwapRule;
import utils.CustomRuleHelper;
import utils.LevelConsts;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(TaggedDatabase database, Settings s) {
    for (GenericSpawnPresetRule grj : s.getNpcSettings().getEnemySpawnSettings().getFiltersList()) {
      GenericSpawnPresetRule copy = grj.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS)
          .build();
      CustomRuleHelper crh = new CustomRuleHelper(copy);
      rules.add(new NpcSpawnerSwapRule(database, crh));
    }
  }
}
