package filters;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rules.BaseRule;
import utils.ItemSpawnSettings;

public class ItemSpawnFilter extends BaseFilter {

  List<BaseRule> rules;
  ItemSpawnSettings settings;

  public ItemSpawnFilter(ItemSpawnSettings settings) {
    super("ItemSpawnFilter");
    rules = new LinkedList<>();
    this.settings = settings;
  }

  public enum Presets {
    NO_LOGIC, // Free-for-all, use settings to decide ratio of type
    WITHIN_TYPE, // Shuffles items within their type
    ALL_WRENCHES, // Changes all weapons to wrenches and removes non-wrench fab plans
    ALL_JUNK, // Changes all items to junk and removes all fab plans
    NO_ITEMS, // Removes all pickup items
  }

  public enum Options {
    SHUFFLE_KEYCARDS, // Moves non-critical keycards around, modifies in-game notes with hints on
                      // where to find them
    SHITLOADS_OF_WEAPONS_IN_MORGANS_APT, // Adds a shitload of weapons and ammo to Morgan's apartment
  }

  public enum ItemType {
    JUNK, SPARE_PARTS, FOOD, AMMO, GRENADE, PSI_HYPO, MEDKIT, CHIPSET, FAB_PLAN, WEAPON_KIT, WEAPON, NEUROMOD,
  }

  @Override
  public boolean filterEntityFile(String levelDir, File entityFile, Map<String, String> config) {
    
    
    
    
    return false;
  }
}
