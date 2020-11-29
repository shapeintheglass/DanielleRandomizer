package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.UnlockEverythingRule;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter() {
    rules.add(new UnlockEverythingRule());
  }

}
