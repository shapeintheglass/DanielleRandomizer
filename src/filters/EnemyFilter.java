package filters;

import databases.TagHelper;
import rules.NpcSpawnerSwapRule;
import settings.Settings;

public class EnemyFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public EnemyFilter(Settings s) {
    super("EnemyFilter", s);
    
    switch(s.getEnemySettings().getPreset()) {
      case NO_LOGIC:
        rules.add(new NpcSpawnerSwapRule()
            .setInputTags(TagHelper.TYPHON_TAG)
            .setOutputTags(TagHelper.TYPHON_TAG));
        break;
      case WITHIN_TYPE:
        break;
      case ALL_NIGHTMARES:
        rules.add(new NpcSpawnerSwapRule()
            .setInputTags(TagHelper.TYPHON_TAG)
            .setOutputTags(TagHelper.NIGHTMARE_TAG));
        break;
      case ALL_MIMICS:
        rules.add(new NpcSpawnerSwapRule()
            .setInputTags(TagHelper.TYPHON_TAG)
            .setOutputTags(TagHelper.BASE_MIMIC_TAG));
        break;
      default:
        break;
    }
  }


}
