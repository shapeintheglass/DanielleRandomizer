package randomizers.gameplay.filters;

import com.google.common.collect.Lists;
import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.ContainerSpawnRule;
import utils.CustomItemFilterHelper;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, Settings settings) {

    if (settings.getItemSettings()
        .getItemSpawnSettings()
        .getFiltersList()
        .size() == 0) {
      return;
    }

    CustomItemFilterHelper filterHelper = new CustomItemFilterHelper(settings, database);
    filterHelper.addDoNotOutput(Lists.newArrayList("ArkPhysicsProps"));
    rules.add(new ContainerSpawnRule(filterHelper));
  }
}
