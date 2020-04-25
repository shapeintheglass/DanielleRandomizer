package filters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rules.BaseRule;
import utils.ItemSpawnSettings;

public class ItemSpawnFilter extends BaseFilter {

  List<BaseRule> rules;
  ItemSpawnSettings settings;

  public ItemSpawnFilter(ItemSpawnSettings settings, Path tempDir) {
    super("ItemSpawnFilter", tempDir);
    rules = new LinkedList<>();
    this.settings = settings;
  }

  public enum Presets {
    
    WITHIN_TYPE, // Shuffles items within their type
    ALL_WRENCHES, // Changes all weapons to wrenches and removes non-wrench fab plans
    ALL_JUNK, // Changes all items to junk and removes all fab plans
    NO_ITEMS, // Removes all pickup items
  }

  public enum Options {
    NO_LOGIC, // Free-for-all, use settings to decide ratio of type
    SHUFFLE_KEYCARDS, // Moves non-critical keycards around, modifies in-game notes with hints on
                      // where to find them
    SHITLOADS_OF_WEAPONS_IN_MORGANS_APT, // Adds a shitload of weapons and ammo to Morgan's apartment
  }


  @Override
  public boolean filterEntityFile(String levelDir, File entityFile, Map<String, String> config) {
    
    if (!config.get("archetype").toLowerCase().contains("arkpickups")) {
      return false;
    }
    

    File tempFile = tempDir.resolve("temp_entity").toFile();
    
    try(BufferedReader br = new BufferedReader(new FileReader(entityFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
      
      String line = br.readLine();
      
      while (line != null) {
        line = br.readLine();
      }
      
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    
    
    return false;
  }
  
  
}
