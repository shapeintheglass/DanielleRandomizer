package randomizers.gameplay;

import java.io.IOException;
import java.util.List;

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
          Element newArchetype = cfh.getPickupEntity(archetype, null, r);
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
}
