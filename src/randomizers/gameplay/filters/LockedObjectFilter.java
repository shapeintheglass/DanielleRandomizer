package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.LockedObjectRule;

public class LockedObjectFilter extends BaseFilter {
  public LockedObjectFilter() {
    rules.add(new LockedObjectRule());
  }
}
