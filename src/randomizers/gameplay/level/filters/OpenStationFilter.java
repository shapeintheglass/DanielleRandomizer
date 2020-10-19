package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.UnlockApartmentRule;
import randomizers.gameplay.level.filters.rules.UnlockPsychotronicsRule;
import settings.Settings;

public class OpenStationFilter extends BaseFilter {

  public OpenStationFilter(Settings s) {
    super("OpenStationFilter", s);

    rules.add(new UnlockApartmentRule());
    rules.add(new UnlockPsychotronicsRule());
  }

}
