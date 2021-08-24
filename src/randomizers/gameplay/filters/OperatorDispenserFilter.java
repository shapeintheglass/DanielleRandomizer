package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.OperatorDispenserRule;

public class OperatorDispenserFilter extends BaseFilter {
  public OperatorDispenserFilter() {
    rules.add(new OperatorDispenserRule());
  }
}
