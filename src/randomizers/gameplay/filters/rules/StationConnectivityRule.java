package randomizers.gameplay.filters.rules;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import org.jdom2.Element;
import randomizers.generators.StationGenerator;
import utils.StationConnectivityConsts;

public class StationConnectivityRule implements Rule {
  private static final String PROPERTIES = "Properties";
  private static final String DESTINATION_NAME = "destinationName";
  private static final String PROPERTIES2 = "Properties2";
  private static final String LOCATION_DESTINATION = "location_Destination";
  private static final String NAME = "Name";
  private Map<String, Map<String, String>> doorConnectivity;
  private Map<String, Map<String, String>> spawnConnectivity;

  public StationConnectivityRule(Map<String, Map<String, String>> doorConnectivity,
      Map<String, Map<String, String>> spawnConnectivity) {
    this.doorConnectivity = doorConnectivity;
    this.spawnConnectivity = spawnConnectivity;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Trigger if entity is in either filename list
    if (!doorConnectivity.containsKey(filename) && !spawnConnectivity.containsKey(filename)) {
      return false;
    }
    Set<String> validDoors = doorConnectivity.get(filename).keySet();
    Set<String> validSpawns = spawnConnectivity.get(filename).keySet();
    String name = e.getAttributeValue(NAME);
    return validDoors.contains(name) || validSpawns.contains(name);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String name = e.getAttributeValue(NAME);

    String newLocation = doorConnectivity.get(filename).get(name);
    String newSpawn = spawnConnectivity.get(filename).get(name);

    if (newLocation != null) {
      Element properties2Element = e.getChild(PROPERTIES2);
      String originalLocation = properties2Element.getAttributeValue(LOCATION_DESTINATION);
      StationConnectivityConsts.Level originalLevel = StationGenerator.LEVELS_TO_IDS.inverse().get(originalLocation);
      StationConnectivityConsts.Level newLevel = StationGenerator.LEVELS_TO_IDS.inverse().get(newLocation);
      properties2Element.setAttribute(LOCATION_DESTINATION, newLocation);
      Logger.getGlobal()
          .info(String.format("%s: Updating connection to %s to go to %s instead", filename, originalLevel, newLevel));
    } else if (newSpawn != null) {
      Element propertiesElement = e.getChild(PROPERTIES);
      String originalSpawn = propertiesElement.getAttributeValue(DESTINATION_NAME);
      propertiesElement.setAttribute(DESTINATION_NAME, newSpawn);
      Logger.getGlobal().info(String.format("%s: Updating spawn %s to %s", filename, originalSpawn, newSpawn));
    } else {
      Logger.getGlobal().info("Couldn't match to any location or spawn. =[");
    }
  }

}
