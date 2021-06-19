package randomizers.gameplay;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.CustomItemFilterHelper;
import utils.ItemMultiplierHelper;
import utils.Utils;
import utils.ZipHelper;

public class PreorderLockerRandomizer extends BaseRandomizer {

  private static final String PREORDER_LOCKER_FILE = "ark/entitlementunlocklibrary.xml";

  private static final int MAX_ATTEMPTS = 10;

  private TaggedDatabase database;

  public PreorderLockerRandomizer(Settings s, ZipHelper zipHelper, TaggedDatabase database) {
    super(s, zipHelper);
    this.database = database;
  }

  @Override
  public void randomize() {
    if (settings.getGameplaySettings().getItemSpawnSettings().getFiltersCount() == 0) {
      return;
    }

    try {
      Document d = zipHelper.getDocument(PREORDER_LOCKER_FILE);

      CustomItemFilterHelper cfh = new CustomItemFilterHelper(settings, database);

      Element root = d.getRootElement();
      List<Element> entitlementUnlocks = root.getChild("EntitlementUnlocks").getChildren();
      for (Element unlock : entitlementUnlocks) {
        List<Element> items = unlock.getChild("Items").getChildren();
        for (Element item : items) {
          String archetype = item.getAttributeValue("Archetype");
          Element newArchetype = getPickup(cfh, archetype, r);
          if (newArchetype != null) {
            int multiplier = ItemMultiplierHelper.getMultiplierForEntity(newArchetype, r);

            String newArchetypeStr = Utils.getNameForEntity(newArchetype);
            item.setAttribute("Archetype", newArchetypeStr);
            item.setAttribute("Quantity", Integer.toString(multiplier));
          }
        }
      }

      zipHelper.copyToPatch(d, PREORDER_LOCKER_FILE);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private Element getPickup(CustomItemFilterHelper cfh, String archetype, Random r) {
    // Try different items until we get a valid pickup item
    // TODO: Make this smarter by just removing non-pickup items from the list first
    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      Element toSwap = cfh.getEntity(archetype, null, r);

      if (toSwap == null) {
        continue;
      }

      // Ensure that this has the tag "ArkPickups" to represent that it can be added
      // to an inventory
      Set<String> tags = Utils.getTags(toSwap);
      if (tags.contains("ArkPickups")) {
        return toSwap;
      }
    }
    return null;
  }

}
