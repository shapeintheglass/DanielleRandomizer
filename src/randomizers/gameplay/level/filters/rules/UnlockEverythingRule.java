package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;

/**
 * Blindly unlocks everything that is locked.
 */
public class UnlockEverythingRule implements Rule {

  private static final ImmutableMap<String, String> DESIRED_STATE =
      new ImmutableMap.Builder<String, String>().put("bStartsUnlocked", "1")
          .put("bStartsLocked", "0").put("bLocked", "0").put("bStartsPoweredOn", "1")
          .put("ability_HackRequirementOverride", "").put("keycard_UnlockKeycard", "")
          .put("keycode_UnlockCode", "").build();

  private static final String PROPERTIES2 = "Properties2";

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Check if Properties2 child exists and contains one of the desired state keys with a
    // mismatched value.
    Element properties = e.getChild(PROPERTIES2);
    if (properties == null) {
      return false;
    }

    for (String key : DESIRED_STATE.keySet()) {
      if (!properties.getAttributeValue(key).equals(DESIRED_STATE.get(key))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    Element properties = e.getChild(PROPERTIES2);

    for (String key : DESIRED_STATE.keySet()) {
      if (!properties.getAttributeValue(key).equals(DESIRED_STATE.get(key))) {
        properties.setAttribute(key, DESIRED_STATE.get(key));
      }
    }
  }

}
