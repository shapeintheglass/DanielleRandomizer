package randomizers.gameplay.filters.rules;

import java.util.Random;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;

/**
 * Places key items in specific locations
 */
public class KeyItemsInBridgeRule implements Rule {
  private static final String LEVEL_REQ = "executive/bridge";

  private static final String KEY_ITEMS_LOOT_TABLE = "RANDOMIZER_BridgeKeyItems";

  ImmutableMap<String, String> NAME_TO_LOOT_TABLE =
      ImmutableMap.of("Containers/NeoDeco.Boss_Desk_R1", KEY_ITEMS_LOOT_TABLE);

  private static final String LOOTTABLE_CONTAINER_ATTR = "loottable_ContainerLootTable";
  private static final String PROPERTIES_ATTR = "Properties2";

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    if (!filename.equals(LEVEL_REQ)) {
      return false;
    }
    String elementName = e.getAttributeValue("Name");

    return NAME_TO_LOOT_TABLE.containsKey(elementName);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String name = e.getAttributeValue("Name");
    e.getChild(PROPERTIES_ATTR).setAttribute(LOOTTABLE_CONTAINER_ATTR,
        NAME_TO_LOOT_TABLE.get(name));
  }

}
