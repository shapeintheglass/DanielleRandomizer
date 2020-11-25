package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Blindly unlocks everything that is locked.
 */
public class UnlockEverythingRule implements Rule {

  private static final ImmutableSet<String> DO_NOT_TOUCH =
      ImmutableSet.of("Door.Door_LevelTransition_AlexBunker");

  private static final ImmutableMap<String, String> DESIRED_STATE =
      new ImmutableMap.Builder<String, String>().put("bStartsUnlocked", "1")
          .put("bStartsLocked", "0").put("bLocked", "0").put("bStartsPoweredOn", "1")
          .put("bUseFreeExitButton", "1").put("bStartsOpen", "1").build();

  private static final String PROPERTIES2 = "Properties2";

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Check if Properties2 child exists and contains one of the desired state keys with a
    // mismatched value.
    if (DO_NOT_TOUCH.contains(e.getAttributeValue("Name"))) {
      return false;
    }
    Element properties = e.getChild(PROPERTIES2);
    if (properties == null) {
      return false;
    }

    for (String key : DESIRED_STATE.keySet()) {
      if (properties.getAttributeValue(key) != null
          && !properties.getAttributeValue(key).equals(DESIRED_STATE.get(key))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    Element properties = e.getChild(PROPERTIES2);

    for (String key : DESIRED_STATE.keySet()) {
      if (properties.getAttributeValue(key) != null
          && !properties.getAttributeValue(key).equals(DESIRED_STATE.get(key))) {
        properties.setAttribute(key, DESIRED_STATE.get(key));
      }
    }
  }

}
