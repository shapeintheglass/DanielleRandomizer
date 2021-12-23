package randomizers.generators;

import java.util.Map;

public class KeycardGenerator {
  // Map of level name (level;name) to keycard ID
  private Map<String, String> keycardConnectivity;
  
  // Station connectivity
  private StationGenerator station;
  
  public KeycardGenerator(StationGenerator station) {
    this.station = station;
    
  }
  
  public Map<String, String> getKeycardConnectivity() {
    return keycardConnectivity;
  }
  
  @Override
  public String toString() {
    return "";
  }
  
  private void generateKeycards() {
    // Place critical keycards
    
    
    
  }
}
