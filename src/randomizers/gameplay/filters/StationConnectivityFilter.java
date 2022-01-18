package randomizers.gameplay.filters;

import java.util.Map;

import com.google.common.collect.ImmutableBiMap;

import randomizers.gameplay.filters.rules.KeyItemsInBridgeRule;
import randomizers.gameplay.filters.rules.StationConnectivityRule;
import randomizers.gameplay.filters.rules.UnlockMaintenanceAccessRule;
import randomizers.gameplay.filters.rules.UnlockPowerPlantRule;
import randomizers.gameplay.filters.rules.UnlockPsychotronicsRule;
import utils.StationConnectivityConsts.Level;

public class StationConnectivityFilter extends BaseFilter {

  public StationConnectivityFilter(Map<String, Map<String, String>> doorConnectivity,
      Map<String, Map<String, String>> spawnConnectivity, ImmutableBiMap<Level, String> levelsToIds) {

    rules.add(new StationConnectivityRule(doorConnectivity, spawnConnectivity, levelsToIds));
    rules.add(new UnlockPsychotronicsRule());
    rules.add(new UnlockMaintenanceAccessRule());
    //rules.add(new UnlockPowerPlantRule());
    rules.add(new KeyItemsInBridgeRule());
  }
}
