package randomizers.gameplay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import databases.EntityDatabase;
import randomizers.BaseRandomizer;
import utils.FileConsts;
import utils.XmlEntity;

public class LootTableRandomizer extends BaseRandomizer {

  EntityDatabase database;
  Map<String, XmlEntity> lootTables;
  XmlEntity root;

  private static final String LOOT_TABLE_DEST = "ark/items/loottables.xml";

  public LootTableRandomizer(String installDir) {
    super(installDir, "LootTable");
    this.database = EntityDatabase.getInstance();
    this.lootTables = new HashMap<>();
    try {
      parseLootTables();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(root);
  }

  @Override
  public void randomize() {
    // Write to zip
    

    // Copy into pak
  }

  private void randomize(XmlEntity arkLootTable) {
    // Replace each slot with a random pickup
    List<XmlEntity> slots = arkLootTable.getSubEntityByTagName("Slots").getSubEntities();

    for (XmlEntity slot : slots) {
      List<XmlEntity> items = slot.getSubEntityByTagName("Items").getSubEntities();
      for (XmlEntity item : items) {
        String archetype = String.format("ArkPickups.%s", database.getRandomEntityByTag("PICKUP").getKey("Name"));
        item.setKey("Archetype", archetype);
      }
    }
  }

  private void parseLootTables() throws FileNotFoundException, IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(FileConsts.LOOT_TABLE_FILE))) {
      root = new XmlEntity(br);
    }
    List<XmlEntity> lt = root.getSubEntityByTagName("Tables").getSubEntities();
    for (XmlEntity x : lt) {
      randomize(x);
    }
  }

}
