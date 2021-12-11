package randomizers.gameplay.filters;

import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.LockedObjectRule;

public class LockedObjectFilter extends BaseFilter {
  public LockedObjectFilter(Settings settings) {
    rules.add(new LockedObjectRule(1.0f * settings.getGameplaySettings()
        .getRandomizeHackables()
        .getSliderValue() / 100.0f));
  }
}
