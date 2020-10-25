package randomizers.gameplay.level.filters;

import java.util.Random;

import randomizers.gameplay.level.filters.rules.StationConnectivityRule;

public class StationConnectivityFilter extends BaseFilter {

  public StationConnectivityFilter(Random r) {
    rules.add(new StationConnectivityRule(r));
  }
}
