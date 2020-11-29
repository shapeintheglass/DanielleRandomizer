package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.ApartmentLootRule;

public class MorgansApartmentFilter extends BaseFilter {

  public MorgansApartmentFilter() {
    rules.add(new ApartmentLootRule());
  }

}
