package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.LivingCorpseRule;

public class LivingCorpseFilter extends BaseFilter {
  public LivingCorpseFilter() {
    rules.add(new LivingCorpseRule());
  }
}
