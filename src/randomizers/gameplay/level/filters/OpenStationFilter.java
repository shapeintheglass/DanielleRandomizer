package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.UnlockApartmentRule;
import randomizers.gameplay.level.filters.rules.UnlockLobbyRule;
import randomizers.gameplay.level.filters.rules.UnlockPsychotronicsRule;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter() {
    rules.add(new UnlockApartmentRule());
    rules.add(new UnlockPsychotronicsRule());
    rules.add(new UnlockLobbyRule());
  }

}
