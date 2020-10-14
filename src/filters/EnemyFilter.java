package filters;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import rules.NpcSpawnerSwapRule;
import settings.Settings;
import utils.CustomRuleHelper;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(Settings s) {
    super("EnemyFilter", s);
    
    TaggedDatabase database = EntityDatabase.getInstance(s.getRandom());
    
    for (CustomRuleHelper crh : s.getEnemySettings().getCustomRuleHelpers()) {
      rules.add(new NpcSpawnerSwapRule(database, crh));
    }
  }


}
