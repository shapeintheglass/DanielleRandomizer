package randomizers.gameplay.level.filters;

import java.util.Map;

public class StationConnectivityFilter extends BaseFilter {
  
  // Map of level transition ID to new location
  Map<String, String> connectivity;
  
  private static final String TRANSITION_ARCHETYPE = "ArkLevelTransitionDoor";
  
  public StationConnectivityFilter() {
    // Generate a randomized station
    
    // Apply to rules
    
  }
  
  private void generateRandomStation() {
    
  }
  
  private static class Location {
    String locationId;
    Map<String, String> connections;
    
    private Location() {
      
    }
  }
}
