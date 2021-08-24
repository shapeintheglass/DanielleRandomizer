package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.BrokenObjectRule;

public class BrokenObjectFilter extends BaseFilter {
  public BrokenObjectFilter() {
    rules.add(new BrokenObjectRule());
  }
}
