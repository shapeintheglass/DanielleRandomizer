package filters;

import rules.NpcSpawnerSwapRule;
import settings.Settings;
import utils.CustomRuleHelper;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(Settings s) {
    super("EnemyFilter", s);
    
    for (CustomRuleHelper crh : s.getEnemySettings().getCustomRuleHelpers()) {
      rules.add(new NpcSpawnerSwapRule(crh));
    }
  }


}
