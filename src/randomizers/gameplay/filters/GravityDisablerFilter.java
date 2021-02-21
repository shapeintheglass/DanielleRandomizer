package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.DisableGravityRule;

public class GravityDisablerFilter extends BaseFilter {
  public GravityDisablerFilter() {
    rules.add(new DisableGravityRule());
  }
}
