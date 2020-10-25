package randomizers.gameplay.level.filters.rules;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.jdom2.Element;

public class StationConnectivityRule implements Rule {
  Map<String, Map<String, String>> connectivity;

  public StationConnectivityRule(Map<String, Map<String, String>> connectivity) {
    this.connectivity = connectivity;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Trigger if door is in filename list
    if (!connectivity.containsKey(filename)) {
      return false;
    }
    Set<String> validDoors = connectivity.get(filename)
                                         .keySet();
    return validDoors.contains(e.getAttributeValue("Name"));
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // Substitute door from connectivity list
    String newLocation = connectivity.get(filename)
                                     .get(e.getAttributeValue("Name"));
    Element properties = e.getChild("Properties2");
    String originalLocation = properties.getAttributeValue("location_Destination");
    properties.setAttribute("location_Destination", newLocation);
    Logger.getGlobal()
          .info(String.format("%s: Updating transition %s to %s", filename, originalLocation, newLocation));
  }

}
