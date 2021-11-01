package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.ContainerSpawnRule;
import utils.CustomItemFilterHelper;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, Settings settings) {
    CustomItemFilterHelper filterHelper = new CustomItemFilterHelper(settings, database);
    rules.add(new ContainerSpawnRule(filterHelper));
  }
}
