package randomizers.gameplay.level.filters;

import databases.TaggedDatabase;
import randomizers.gameplay.level.filters.rules.ContainerSpawnRule;
import settings.Settings;

public class FlowgraphFilter extends BaseFilter {

  public FlowgraphFilter(TaggedDatabase database, Settings s) {
    super("FlowgraphFilter", s);
    rules.add(new ContainerSpawnRule(database, s.getRandom()));

  }

}
