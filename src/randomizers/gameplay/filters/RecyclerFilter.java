package randomizers.gameplay.filters;

import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.RecyclerRule;

public class RecyclerFilter extends BaseFilter {
  public RecyclerFilter(Settings settings) {
    rules.add(new RecyclerRule(1.0f * settings.getGameplaySettings()
        .getRandomizeRecyclers()
        .getSliderValue() / 100.0f));
  }
}
