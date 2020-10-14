package randomizers.gameplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import randomizers.BaseRandomizer;
import settings.Settings;
import utils.CustomRuleHelper;
import utils.Utils;

public class LootTableRandomizer extends BaseRandomizer {

  private static final String SOURCE = "data/ark/loottables.xml";
  private static final String OUTPUT = "ark/items/loottables.xml";
  
  TaggedDatabase database;

  public LootTableRandomizer(Settings s) {
    super("LootTableRandomizer", s);
    this.database = EntityDatabase.getInstance(s.getRandom());
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
    
    try (BufferedReader br = new BufferedReader(new FileReader(in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      
      String line = br.readLine();

      while (line != null) {
        if (line.contains("ArkLootItem")) {
          line = filterArkLootItemLine(line);
        }

        bw.write(line);
        bw.write('\n');
        line = br.readLine();
      }

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private String filterArkLootItemLine(String line) {
    // Parse the item archetype
    Map<String, String> keys = Utils.getKeysFromLine(line);
    String oldArchetype = keys.get("Archetype");
    List<CustomRuleHelper> rules = settings.getItemSpawnSettings().getCustomRuleHelpers();
    for (CustomRuleHelper crh : rules) {
      // Explicitly prevent physics props from appearing in loot tables
      crh.addDoNotOutputTags("ArkPhysicsProps");
      if (crh.trigger(database, oldArchetype)) {
        String newArchetype = crh.getEntityToSwapStr(database);
        return line.replace(oldArchetype, newArchetype);
      }
    }
    return line;
  }
}