package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.FruitTreeRule;
import utils.CustomItemFilterHelper;

public class FruitTreeFilter extends BaseFilter {

  public FruitTreeFilter(Settings currentSettings, TaggedDatabase database) {
    if (currentSettings.getItemSettings()
        .getItemSpawnSettings()
        .getFiltersList()
        .size() == 0) {
      return;
    }

    CustomItemFilterHelper filterHelper = new CustomItemFilterHelper(currentSettings, database);
    rules.add(new FruitTreeRule(filterHelper, database));
  }
}
