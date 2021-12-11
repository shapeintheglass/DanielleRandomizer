package randomizers.gameplay.filters;

import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.BrokenObjectRule;

public class BrokenObjectFilter extends BaseFilter {
  public BrokenObjectFilter(Settings settings) {
    rules.add(new BrokenObjectRule(1.0f * settings.getGameplaySettings()
        .getRandomizeBreakables()
        .getSliderValue() / 100.0f));
  }
}
