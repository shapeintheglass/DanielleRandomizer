package filters;

import utils.FileConsts.Archetype;
import utils.XmlEntity;
import databases.EntityDatabase;
import databases.EntityDatabase.EntityType;

public class ItemSpawnFilter extends BaseFilter {

  private EntityDatabase database;

  private Archetype[] archetypes = { Archetype.PICKUPS };

  public ItemSpawnFilter() {
    super("ItemSpawnFilter");

    database = new EntityDatabase(archetypes);
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

  @Override
  public boolean filterEntity(XmlEntity x, String levelDir) {
    String archetype = x.getKey("Archetype");

    if (archetype != null && archetype.startsWith("ArkPickups.")) {
      EntityType type = EntityDatabase.getEntityType(x);
      if (type != EntityType.OTHER && type != EntityType.ENTITY_MISSING) {
        XmlEntity newEntity = database.getRandomItemByType(type);
        x.setKey("Archetype",
            String.format("ArkPickups.%s", newEntity.getKey("Name")));
      }
    }
    return false;
  }
}
