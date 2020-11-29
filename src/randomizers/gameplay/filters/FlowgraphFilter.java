package randomizers.gameplay.filters;

import databases.TaggedDatabase;
import json.SettingsJson;
import randomizers.gameplay.filters.rules.ContainerSpawnRule;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, SettingsJson settings) {
    rules.add(new ContainerSpawnRule(database, settings));
  }
}
