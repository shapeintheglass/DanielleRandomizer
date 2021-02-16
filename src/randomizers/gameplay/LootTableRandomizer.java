package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.CustomRuleHelper;
import utils.ItemMultiplierHelper;
import utils.ItemMultiplierHelper.Tier;
import utils.Utils;

public class LootTableRandomizer extends BaseRandomizer {

  private static final String SOURCE = "data/ark/loottables.xml";
  private static final String OUTPUT_DIR = "ark/items";
  private static final String OUTPUT_NAME = "loottables.xml";

  // Offset to add to spawn chance, just so that spawns are more likely
  private static final int PERCENT_OFFSET = 50;

  private static final int MAX_ATTEMPTS = 10;

  private static final String DO_NOT_TOUCH_PREFIX = "RANDOMIZER_";

  private TaggedDatabase database;

  private File inputFile;
  private File outputFile;

  public LootTableRandomizer(TaggedDatabase database, SettingsJson s, Path tempPatchDir) throws IOException {
    super(s);
    this.database = database;

    File outputDir = tempPatchDir.resolve(OUTPUT_DIR).toFile();
    outputDir.mkdirs();

    inputFile = new File(SOURCE);
    outputFile = outputDir.toPath().resolve(OUTPUT_NAME).toFile();
    outputFile.createNewFile();
  }

  public void copyFile() throws IOException {
    Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void randomize() {
    outputFile.getParentFile().mkdirs();
    try {
      outputFile.createNewFile();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(inputFile);
      Element root = document.getRootElement();
      List<Element> lootTables = root.getChild("Tables").getChildren();

      for (Element e : lootTables) {
        filterLootTable(e);
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(outputFile));
    } catch (JDOMException | IOException e1) {
      e1.printStackTrace();
    }
  }

  private void filterLootTable(Element lootTable) {
    if (lootTable.getAttributeValue("Name").contains(DO_NOT_TOUCH_PREFIX)) {
      return;
    }
    List<Element> slots = lootTable.getChild("Slots").getChildren();
    for (Element slot : slots) {
      // Randomize the percent
      //int percent = r.nextInt(100 - PERCENT_OFFSET) + PERCENT_OFFSET;
      //slot.setAttribute("Percent", Integer.toString(percent));

      // Randomize the items
      List<Element> items = slot.getChild("Items").getChildren();
      for (Element item : items) {
        String oldArchetype = item.getAttributeValue("Archetype");

        for (GenericRuleJson grj : settings.getGameplaySettings().getItemSpawnSettings().getRules()) {
          CustomRuleHelper crh = new CustomRuleHelper(grj);

          // Explicitly prevent non-pickup items from appearing in loot tables
          for (int i = 0; i < MAX_ATTEMPTS; i++) {
            if (crh.trigger(database, oldArchetype, null)) {
              Element newArchetype = crh.getEntityToSwap(database, r);
              Set<String> tags = Utils.getTags(newArchetype);
              if (tags.contains("ArkPickups")) {
                item.setAttribute("Archetype", Utils.getNameForEntity(newArchetype));

                Tier t = ItemMultiplierHelper.getTierForEntity(newArchetype);
                if (t == Tier.FUCK_IT) {
                  item.setAttribute("CountMin", Integer.toString(ItemMultiplierHelper.getMinForTier(t)));
                  item.setAttribute("CountMax", Integer.toString(ItemMultiplierHelper.getMaxForTier(t)));
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
