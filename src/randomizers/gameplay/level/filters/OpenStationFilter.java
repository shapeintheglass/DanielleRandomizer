package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.UnlockEverythingRule;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter() {
    rules.add(new UnlockEverythingRule());
  }

}
