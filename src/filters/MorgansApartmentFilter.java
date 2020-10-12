package filters;

import rules.UnlockApartmentRule;
import settings.Settings;

public class MorgansApartmentFilter extends BaseFilter {

  public MorgansApartmentFilter(Settings s) {
    super("MorgansApartmentFilter", s);

    rules.add(new UnlockApartmentRule());
  }

}
