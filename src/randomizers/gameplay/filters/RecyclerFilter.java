package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.RecyclerRule;

public class RecyclerFilter extends BaseFilter {
  public RecyclerFilter() {
    rules.add(new RecyclerRule());
  }
}
