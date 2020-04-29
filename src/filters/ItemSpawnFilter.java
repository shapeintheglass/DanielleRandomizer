package filters;

import rules.WithinTypeRule;


public class ItemSpawnFilter extends BaseFilter {

  public ItemSpawnFilter() {
    super("ItemSpawnFilter");
    rules.add(new WithinTypeRule());
  }

  public enum Presets {

    WITHIN_TYPE, // Shuffles items within their type
    ALL_WRENCHES, // Changes all weapons to wrenches and removes non-wrench fab
                  // plans
    ALL_JUNK, // Changes all items to junk and removes all fab plans
    NO_ITEMS, // Removes all pickup items
  }

  public enum Options {
    NO_LOGIC, // Free-for-all, use settings to decide ratio of type
    SHUFFLE_KEYCARDS, // Moves non-critical keycards around, modifies in-game
                      // notes with hints on
                      // where to find them
    SHITLOADS_OF_WEAPONS_IN_MORGANS_APT, // Adds a shitload of weapons and ammo
                                         // to Morgan's apartment
  }
}
