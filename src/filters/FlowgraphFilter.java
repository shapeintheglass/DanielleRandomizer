package filters;

import rules.ContainerSpawnRule;
import settings.Settings;
import utils.CustomRuleHelper;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(Settings s) {
    super("FlowgraphFilter", s);
    for (CustomRuleHelper crh : s.getItemSpawnSettings().getCustomRuleHelpers()) {
      rules.add(new ContainerSpawnRule(crh, settings.getRandom()));
    }
  }

}
