package randomizers.gameplay.filters;

import java.util.Map;
import randomizers.gameplay.filters.rules.KeyItemsInBridgeRule;
import randomizers.gameplay.filters.rules.StationConnectivityRule;
import randomizers.gameplay.filters.rules.UnlockMaintenanceAccessRule;
import randomizers.gameplay.filters.rules.UnlockPsychotronicsRule;

public class StationConnectivityFilter extends BaseFilter {

  public StationConnectivityFilter(Map<String, Map<String, String>> doorConnectivity,
      Map<String, Map<String, String>> spawnConnectivity) {

    rules.add(new StationConnectivityRule(doorConnectivity, spawnConnectivity));
    rules.add(new UnlockPsychotronicsRule());
    rules.add(new UnlockMaintenanceAccessRule());
    //rules.add(new UnlockPowerPlantRule());
    rules.add(new KeyItemsInBridgeRule());
  }
}
