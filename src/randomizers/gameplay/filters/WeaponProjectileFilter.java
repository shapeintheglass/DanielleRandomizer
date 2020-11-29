package randomizers.gameplay.filters;

import randomizers.gameplay.filters.rules.WeaponProjectileRule;

public class WeaponProjectileFilter extends BaseFilter {

  public WeaponProjectileFilter() {
    super();
    addRule(new WeaponProjectileRule());
  }
}
