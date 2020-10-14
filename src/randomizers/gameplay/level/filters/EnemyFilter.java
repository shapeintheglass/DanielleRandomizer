package randomizers.gameplay.level.filters;

import databases.TaggedDatabase;
import randomizers.gameplay.level.filters.rules.NpcSpawnerSwapRule;
import settings.Settings;
import utils.CustomRuleHelper;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(TaggedDatabase database, Settings s) {
    super("EnemyFilter", s);
    
    for (CustomRuleHelper crh : s.getEnemySettings().getCustomRuleHelpers()) {
      rules.add(new NpcSpawnerSwapRule(database, s.getRandom(), crh));
    }
  }


}
