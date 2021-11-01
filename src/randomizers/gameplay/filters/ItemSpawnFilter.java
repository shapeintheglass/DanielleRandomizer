package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import utils.CustomItemFilterHelper;

public class ItemSpawnFilter extends BaseFilter {
  /**
   * Pre-made combination of rules that specifically filters items in certain settings.
   */
  public ItemSpawnFilter(TaggedDatabase database, Settings s) {
    CustomItemFilterHelper cifh = new CustomItemFilterHelper(s, database);
    rules.addAll(cifh.getRuleSet());
  }
}
