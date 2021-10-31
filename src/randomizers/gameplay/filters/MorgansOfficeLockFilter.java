package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.MorgansOfficeLockRule;

public class MorgansOfficeLockFilter extends BaseFilter {
  public MorgansOfficeLockFilter() {
    rules.add(new MorgansOfficeLockRule());
  }
}
