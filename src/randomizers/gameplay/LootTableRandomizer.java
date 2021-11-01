package randomizers.gameplay;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.Lists;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.CustomItemFilterHelper;
import utils.ItemMultiplierHelper;
import utils.ItemMultiplierHelper.Tier;
import utils.Utils;
import utils.ZipHelper;

public class LootTableRandomizer extends BaseRandomizer {

  private static final int ITEMS_PER_SLOT = 10;

  private static final String DO_NOT_TOUCH_PREFIX = "RANDOMIZER_";

  private CustomItemFilterHelper cfh;

  public LootTableRandomizer(TaggedDatabase database, Settings s, Path tempPatchDir,
      ZipHelper zipHelper) throws IOException {
    super(s, zipHelper);
    cfh = new CustomItemFilterHelper(s, database);
  }

  @Override
  public void randomize() {
    // If there are no item spawn settings, copy the loot table file over directly.
    if (settings.getGameplaySettings()
        .getItemSpawnSettings()
        .getFiltersCount() == 0) {
      try {
        zipHelper.copyToPatch(ZipHelper.LOOT_TABLE_FILE, ZipHelper.LOOT_TABLE_FILE);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }

    try {
      Document document = zipHelper.getDocument(ZipHelper.LOOT_TABLE_FILE);
      Element root = document.getRootElement();
      List<Element> lootTables = root.getChild("Tables")
          .getChildren();

      for (Element e : lootTables) {
        filterLootTable(e);
      }

      zipHelper.copyToPatch(document, ZipHelper.LOOT_TABLE_FILE);
    } catch (JDOMException | IOException e1) {
      e1.printStackTrace();
    }
  }

  private void filterLootTable(Element lootTable) {
    if (lootTable.getAttributeValue("Name")
        .contains(DO_NOT_TOUCH_PREFIX)) {
      return;
    }
    List<Element> slots = lootTable.getChild("Slots")
        .getChildren();
    for (Element slot : slots) {
      // Randomize the items
      List<Element> items = slot.getChild("Items")
          .getChildren();
      int slotsToAdd = ITEMS_PER_SLOT;
      List<String> oldArchetypes = Lists.newArrayList();
      
      for (Element item : items) {
        String oldArchetype = item.getAttributeValue("Archetype");
        oldArchetypes.add(oldArchetype);

        Element newArchetype = cfh.getPickupEntity(oldArchetype, null, r);
        if (newArchetype != null) {
          Tier t = ItemMultiplierHelper.getTierForEntity(newArchetype);
          String newArchetypeStr = Utils.getNameForEntity(newArchetype);
          item.setAttribute("Archetype", newArchetypeStr);
          item.setAttribute("CountMin", Integer.toString(ItemMultiplierHelper.getMinForTier(t)));
          item.setAttribute("CountMax", Integer.toString(ItemMultiplierHelper.getMaxForTier(t)));
        }
        slotsToAdd--;
      }

      // Hack to add additional loot slots based on existing old archetypes
      for (int i = 0; i < slotsToAdd; i++) {
        // Pick a random items from the existing slots
        String oldArchetype = Utils.getRandom(oldArchetypes, r);

        Element newItem = new Element("ArkLootItem");
        Element newArchetype = cfh.getPickupEntity(oldArchetype, null, r);
        if (newArchetype != null) {
          Tier t = ItemMultiplierHelper.getTierForEntity(newArchetype);
          String newArchetypeStr = Utils.getNameForEntity(newArchetype);
          newItem.setAttribute("Archetype", newArchetypeStr);
          newItem.setAttribute("CountMin", Integer.toString(ItemMultiplierHelper.getMinForTier(t)));
          newItem.setAttribute("CountMax", Integer.toString(ItemMultiplierHelper.getMaxForTier(t)));
        }
        items.add(newItem);
      }
    }
  }
}
