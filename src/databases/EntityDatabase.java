package databases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.XmlEntity;

/**
 * 
 * Rough "database" implementation that can ingest entities from their
 * entityprototype definition files, tag them, and allow retrieval by tag.
 * 
 * There are three layers of tags- entity type, group type, and super type:
 * 
 * 
 * Entity type - lowest layer of specificity, finest detailed way of requesting
 * a particular entity (ex. a voltaic phantom)
 * 
 * Group type - mid layer of specificity, groups entities with similar roles
 * together (ex. all phantom types are grouped together)
 * 
 * Super type - broadest layer of specificity, groups entities of similar types
 * together (ex. all typhon types are in the same supertype)
 *
 * The database is also mutable, so entities within a particular tag can be
 * modified. This allows features such as overriding the "typhon" category to
 * only contain nightmares.
 *
 */
public class EntityDatabase {

  private static final int READ_AHEAD = 10000;

  Random r;

  // Lowest layer of specificity.
  public enum EntityType {
    // Items
    JUNK, SPARE_PARTS, FOOD, AMMO, GRENADE, PSI_HYPO, MEDKIT,
    // Items
    CHIPSET_SUIT, CHIPSET_SCOPE, WEAPON_KIT, FAB_PLAN, WEAPON,
    // Items
    NEUROMOD, PHARMA, SUIT_REPAIR_KIT, ITEMS_OTHER,
    // Enemies
    MIMIC, GREATER_MIMIC, PHANTOM, POLTERGEIST, CYSTOID, CYSTOID_NEST,
    // Enemies
    PHANTOM_ETHERIC, PHANTOM_VOLTAIC, PHANTOM_THERMAL,
    // Enemies
    WEAVER, TECHNOPATH, TELEPATH, PUPPET, NIGHTMARE, TENTACLE, ENEMIES_OTHER,
    // Robots
    ENGINEERING_OPERATOR, MEDICAL_OPERATOR, SCIENCE_OPERATOR, MILITARY_OPERATOR, TURRET,
    // Robots corrupted
    CORR_ENGINEERING_OPERATOR, CORR_MEDICAL_OPERATOR, CORR_SCIENCE_OPERATOR, CORR_MILITARY_OPERATOR, CORR_TURRET,
    // Gameplay object
    RECYCLER, FABRICATOR, O2_STATION,
    // Misc
    OTHER, ENTITY_MISSING,
  }

  // Mid-level tag used to combine finer entity types that can be logically
  // grouped.
  public enum GroupType {
    HEALING, MIMIC, PHANTOM,
  }

  public EntityType[] GROUP_RESOURCE = { EntityType.JUNK };
  public EntityType[] GROUP_RESTORATIVE = {};

  // Highest level tag that applies to the entity's general purpose.
  public enum SuperType {
    PICKUP, PHYSICS_PROP, ENEMY, HUMAN, ROBOT, OTHER, ENTITY_MISSING
  }

  // Source of truth. A list is used for holding the entities because sets do
  // not allow arbitrary retrieval.
  private Map<EntityType, List<XmlEntity>> typeMap;

  // Automatically populated when the type map is updated
  // All items
  private List<XmlEntity> allItemsList;

  // Initializes with a specific entity database. Intended to be used for
  // testing.
  public EntityDatabase(Map<EntityType, List<XmlEntity>> database) {
    // TODO: Have a single source of randomness
    r = new Random();
    this.typeMap = database;
    allItemsList = new ArrayList<>();
    updateGlobalList();
  }

  // Initializes using specific archetype files.
  public EntityDatabase(Archetype[] archetypes) {
    this(populateDatabase(archetypes));
  }

  // Initializes using every supported archetype file.
  public EntityDatabase() {
    this(populateDatabase());
  }

  // Updates the supporting tables every time the source of truth is changed.
  private void updateGlobalList() {
    allItemsList.clear();
    for (EntityType i : EntityType.values()) {
      if (typeMap.get(i) != null) {
        allItemsList.addAll(typeMap.get(i));
      }
    }
  }

  // Automatically populates with every archetype file
  private static Map<EntityType, List<XmlEntity>> populateDatabase() {
    return populateDatabase(Archetype.values());
  }

  // Retrieves entity archetypes from the given files
  private static Map<EntityType, List<XmlEntity>> populateDatabase(
      Archetype[] archetypes) {
    Map<EntityType, List<XmlEntity>> database = new HashMap<>();
    for (EntityType it : EntityType.values()) {
      database.put(it, new ArrayList<>());
    }

    for (Archetype a : archetypes) {
      getEntitiesFromFile(FileConsts.getFileForArchetype(a), database);
    }

    return database;
  }

  private static void getEntitiesFromFile(String file,
      Map<EntityType, List<XmlEntity>> database) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      br.mark(READ_AHEAD);
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains("<EntityPrototype ")) {
          br.reset();

          XmlEntity x = new XmlEntity(br);
          EntityType type = getEntityType(x);
          database.get(type).add(x);
        }

        br.mark(READ_AHEAD);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Returns a random item from the entire database
   */
  public XmlEntity getRandomItem() {
    return getRandomItemFromList(allItemsList);
  }

  /*
   * Returns a random item with the given entity type
   */
  public XmlEntity getRandomItemByType(EntityType it) {
    return getRandomItemFromList(typeMap.get(it));
  }

  private XmlEntity getRandomItemFromList(List<XmlEntity> list) {
    if (list.isEmpty()) {
      return null;
    }
    int index = r.nextInt(list.size());
    return list.get(index);
  }

  /*
   * Returns the list of all items of this type
   */
  public List<XmlEntity> getAllByType(EntityType it) {
    return typeMap.get(it);
  }

  /*
   * Overwrites the entries of this type with the given list of entities.
   */
  public void overrideItemType(EntityType it, Set<XmlEntity> newEntities) {
    typeMap.get(it).clear();
    typeMap.get(it).addAll(newEntities);
    updateGlobalList();
  }

  /*
   * Removes a set of entities from the global pool
   */

  public void removeItemsFromPool(Set<XmlEntity> toRemove) {
    for (XmlEntity e : toRemove) {
      EntityType t = getEntityType(e);
      typeMap.get(t).remove(e);
      allItemsList.remove(e);
    }
  }

  /*
   * Removes all entities, except for the specified
   */
  public void removeAllItemsFromPoolExcept(Set<XmlEntity> toKeep) {

  }

  /*
   * Removes all entities from the local pool, except for the specified
   */
  public void removeAllItemsFromPoolExcept(EntityType it, Set<XmlEntity> toKeep) {

  }

  public void removeEntity(XmlEntity toRemove) {
    EntityType type = getEntityType(toRemove);
    typeMap.get(type).remove(toRemove);
  }

  // Resolves the tags to apply to an entity
  public static EntityType getEntityType(XmlEntity e) {
    if (e == null) {
      return EntityType.ENTITY_MISSING;
    }
    String name = e.getKey("Name");
    if (name.contains("RecyclerJunk")
        || name.startsWith("Crafting.Ingredients")) {
      return EntityType.JUNK;
    } else if (name.equals("Misc.SpareParts")) {
      return EntityType.SPARE_PARTS;
    } else if (name.startsWith("Food.")) {
      return EntityType.FOOD;
    } else if (name.equals("EMPGrenadeWeapon")
        || name.equals("LureGrenadeWeapon")
        || name.equals("NullwaveTransmitterWeapon")
        || name.equals("RecyclerGrenadeWeapon")) {
      return EntityType.GRENADE;
    } else if (name.equals("Medical.PsiHypo")) {
      return EntityType.PSI_HYPO;
    } else if (name.equals("Medical.MedKit")) {
      return EntityType.MEDKIT;
    } else if (name.startsWith("Mods.Suit")) {
      return EntityType.CHIPSET_SUIT;
    } else if (name.startsWith("Mods.Psychoscope")) {
      return EntityType.CHIPSET_SCOPE;
    } else if (name.equals("Mods.Weapon.WeaponUpgradeKit")) {
      return EntityType.WEAPON_KIT;
    } else if (name.startsWith("Crafting.FabricationPlans")) {
      return EntityType.FAB_PLAN;
    } else if (name.startsWith("Weapons.")) {
      return EntityType.WEAPON;
    } else if (name.startsWith("Ammo.")) {
      return EntityType.AMMO;
    } else if (name.equals("Player.Neuromod")
        || name.equals("Player.Neuromod_Case")
        || name.equals("Player.Neuromod_Cinematic")
        || name.equals("Player.Neuromod_Calibration")) {
      return EntityType.NEUROMOD;
    } else if (name.startsWith("Medical.TraumaPharmas")) {
      return EntityType.PHARMA;
    } else if (name.equals("Player.SuitPatchKit")) {
      return EntityType.SUIT_REPAIR_KIT;
    } else {
      return EntityType.OTHER;
    }
  }
}
