package randomizers.gameplay;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.CustomRuleHelper;
import utils.ItemMultiplierHelper;
import utils.ItemMultiplierHelper.Tier;
import utils.Utils;
import utils.ZipHelper;

public class LootTableRandomizer extends BaseRandomizer {

  private static final int MAX_ATTEMPTS = 10;

  private static final String DO_NOT_TOUCH_PREFIX = "RANDOMIZER_";

  private TaggedDatabase database;

  public LootTableRandomizer(TaggedDatabase database, Settings s, Path tempPatchDir,
      ZipHelper zipHelper) throws IOException {
    super(s, zipHelper);
    this.database = database;
  }

  @Override
  public void randomize() {
    // If there are no item spawn settings, copy the loot table file over directly.
    if (settings.getItemSettings()
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
      for (Element item : items) {
        String oldArchetype = item.getAttributeValue("Archetype");

        for (GenericSpawnPresetRule grj : settings.getItemSettings()
            .getItemSpawnSettings()
            .getFiltersList()) {
          CustomRuleHelper crh = new CustomRuleHelper(grj);

          // Explicitly prevent non-pickup items from appearing in loot tables
          for (int i = 0; i < MAX_ATTEMPTS; i++) {
            if (crh.trigger(database, oldArchetype, null)) {
              Element newArchetype = crh.getEntityToSwap(database, r);
              Set<String> tags = Utils.getTags(newArchetype);
              if (tags.contains("ArkPickups")) {
                item.setAttribute("Archetype", Utils.getNameForEntity(newArchetype));

                Tier t = ItemMultiplierHelper.getTierForEntity(newArchetype);
                if (item.getAttribute("CountMax") != null && t == Tier.FUCK_IT) {
                  item.setAttribute("CountMin",
                      Integer.toString(ItemMultiplierHelper.getMinForTier(t)));
                  item.setAttribute("CountMax",
                      Integer.toString(ItemMultiplierHelper.getMaxForTier(t)));
                }
                break;
              }
            }
          }
        }
      }
    }
  }
}
