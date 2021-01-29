package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.StartOutsideLobbyRule;

public class StartOutsideApartmentFilter extends BaseFilter {
  public StartOutsideApartmentFilter() {
    rules.add(new StartOutsideLobbyRule());
  }
}
