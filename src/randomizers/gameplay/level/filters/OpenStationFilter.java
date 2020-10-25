package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.UnlockApartmentRule;
import randomizers.gameplay.level.filters.rules.UnlockGutsRule;
import randomizers.gameplay.level.filters.rules.UnlockLobbyRule;
import randomizers.gameplay.level.filters.rules.UnlockPowerPlantRule;
import randomizers.gameplay.level.filters.rules.UnlockPsychotronicsRule;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter() {
    rules.add(new UnlockApartmentRule());
    rules.add(new UnlockPsychotronicsRule());
    // TODO: Separate moving the lift technopath out to its own rule
    rules.add(new UnlockLobbyRule());
    rules.add(new UnlockPowerPlantRule());
    // TODO: Find a way to get this to work well with station randomization
    //rules.add(new UnlockArboretumRule());
    rules.add(new UnlockGutsRule());
  }

}
