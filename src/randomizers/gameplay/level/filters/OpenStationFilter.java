package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.UnlockApartmentRule;
import randomizers.gameplay.level.filters.rules.UnlockArboretumRule;
import randomizers.gameplay.level.filters.rules.UnlockGutsRule;
import randomizers.gameplay.level.filters.rules.UnlockLobbyRule;
import randomizers.gameplay.level.filters.rules.UnlockPowerPlantRule;
import randomizers.gameplay.level.filters.rules.UnlockPsychotronicsRule;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter() {
    rules.add(new UnlockApartmentRule());
    rules.add(new UnlockPsychotronicsRule());
    rules.add(new UnlockLobbyRule());
    rules.add(new UnlockPowerPlantRule());
    rules.add(new UnlockArboretumRule());
    rules.add(new UnlockGutsRule());
  }

}
