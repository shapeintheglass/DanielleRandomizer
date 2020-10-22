package randomizers.gameplay.level.filters;

import randomizers.gameplay.level.filters.rules.ApartmentLootRule;

public class MorgansApartmentFilter extends BaseFilter {

  public MorgansApartmentFilter() {
    rules.add(new ApartmentLootRule());
  }

}
