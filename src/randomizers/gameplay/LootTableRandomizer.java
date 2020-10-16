package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import databases.TaggedDatabase;
import randomizers.BaseRandomizer;
import settings.Settings;
import utils.CustomRuleHelper;
import utils.Utils;

public class LootTableRandomizer extends BaseRandomizer {

  private static final String SOURCE = "data/ark/loottables.xml";
  private static final String OUTPUT = "ark/items/loottables.xml";

  private static final String DO_NOT_TOUCH_PREFIX = "RANDOMIZER_";

  TaggedDatabase database;

  public LootTableRandomizer(TaggedDatabase database, Settings s) {
    super("LootTableRandomizer", s);
    this.database = database;
  }

  @Override
  public void randomize() {
    File in = new File(SOURCE);
    File out = settings.getTempPatchDir().resolve(OUTPUT).toFile();
    out.getParentFile().mkdirs();
    try {
      out.createNewFile();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(in);
      Element root = document.getRootElement();
      List<Element> lootTables = root.getChild("Tables").getChildren();

      for (Element e : lootTables) {
        filterLootTable(e);
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (JDOMException | IOException e1) {
      // TODO Auto-generated catch block
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
      int percent = settings.getRandom().nextInt(100);
      slot.setAttribute("Percent", Integer.toString(percent));

      // Randomize the items
      List<Element> items = slot.getChild("Items").getChildren();
      for (Element item : items) {
        String oldArchetype = item.getAttributeValue("Archetype");
        List<CustomRuleHelper> rules = settings.getItemSpawnSettings().getCustomRuleHelpers();
        for (CustomRuleHelper crh : rules) {
          // Explicitly prevent physics props from appearing in loot tables
          crh.addDoNotOutputTags("ArkPhysicsProps");
          if (crh.trigger(database, oldArchetype)) {
            String newArchetype = crh.getEntityToSwapStr(database, settings.getRandom());
            item.setAttribute("Archetype", newArchetype);
          }
        }
      }
    }
  }
}
