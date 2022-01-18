package randomizers.gameplay.filters.rules;

import java.util.Map;
import java.util.Random;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class KeycardRule implements Rule {

  private Map<String, Map<String, String>> parsedConnectivity;

  public KeycardRule(ImmutableMap<String, String> keycardConnectivity) {
    // Pre-process the keycard connectivity into an easier-to-use format
    parsedConnectivity = Maps.newHashMap();
    for (String s : keycardConnectivity.keySet()) {
      String[] tokens = s.split(";");
      Map<String, String> levelKeycardConnectivity =
          parsedConnectivity.getOrDefault(tokens[0], Maps.newHashMap());
      levelKeycardConnectivity.put(tokens[1], keycardConnectivity.get(s));
      parsedConnectivity.put(tokens[0], levelKeycardConnectivity);
    }
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Trigger on keycard archetypes
    String archetype = e.getAttributeValue("Archetype");
    return archetype != null && archetype.equals("ArkPickups.Data.Keycard");
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // Check if present in the connectivity
    String newId = parsedConnectivity.get(filename).get(e.getAttributeValue("Name"));
    if (newId != null) {
      e.getChild("Properties2").setAttribute("keycard_Keycard", newId);
    }
  }

}
