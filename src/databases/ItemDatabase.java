package databases;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import utils.Entity;
import utils.ItemSpawnSettings;
import utils.Utils;

public class ItemDatabase {

  Random r;

  private static final String ARK_PICKUPS_FILE = "entityarchetypes/arkpickups.xml";
  private static final String ARK_SPECIAL_WEAPONS_FILE = "entityarchetypes/arkspecialweapons.xml";
  private static final String ARK_PHYSICS_PROPS_FILE = "entityarchetypes/arkphysicsprops.xml";

  ItemSpawnSettings iss;

  ItemType[] items;
  int[] percents;

  public enum ItemType {
    JUNK, SPARE_PARTS, FOOD, AMMO, GRENADE, PSI_HYPO, MEDKIT, CHIPSET_SUIT, CHIPSET_SCOPE, WEAPON_KIT, FAB_PLAN, WEAPON,
    NEUROMOD, PHARMA, SUIT_REPAIR_KIT, OTHER
  }

  // All items, keyed by name
  Map<String, Entity> allItems;
  List<Entity> allItemsList;

  // Keyed by item type
  Map<ItemType, Map<String, Entity>> byItemType;
  Map<ItemType, List<Entity>> byItemTypeList;

  public ItemDatabase(ItemSpawnSettings iss) {
    r = new Random();
    this.iss = iss;
    allItems = new HashMap<String, Entity>();
    byItemType = new HashMap<ItemType, Map<String, Entity>>();
    byItemTypeList = new HashMap<ItemType, List<Entity>>();
    for (ItemType i : ItemType.values()) {
      byItemType.put(i, new HashMap<String, Entity>());
      byItemTypeList.put(i, new ArrayList<Entity>());
    }
    allItemsList = new ArrayList<>();

    populateDatabase();
  }

  private void populateDatabase() {
    getEntitiesFromFile(ARK_PICKUPS_FILE);
    getEntitiesFromFile(ARK_SPECIAL_WEAPONS_FILE);
    if (iss.getIncludePhysicsProps()) {
      getEntitiesFromFile(ARK_PHYSICS_PROPS_FILE);
    }
  }

  public void getEntitiesFromFile(String file) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      String line = br.readLine();
      while (line != null) {

        if (line.contains("<EntityPrototype ")) {
          Map<String, String> keys = Utils.getKeysFromLine(line);

          String name = keys.get("name");
          Entity e = Entity.builder().setName(name).setId(keys.get("id")).setLibrary(keys.get("library"))
              .setArkClass(keys.get("class")).setArchetypeId(keys.get("archetypeid")).build();

          allItems.put(name, e);
          allItemsList.add(e);

          ItemType type = getItemTypeForEntity(e);
          byItemType.get(type).put(name, e);
          byItemTypeList.get(type).add(e);
        }

        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Entity getRandomItem() {
    return getRandomItemFromList(allItemsList);
  }

  public Entity getRandomItemByType(ItemType it) {
    return getRandomItemFromList(byItemTypeList.get(it));
  }

  private Entity getRandomItemFromList(List<Entity> list) {
    int index = r.nextInt(list.size());
    return list.get(index);
  }
  
  public List<Entity> getAllByType(ItemType it) {
    return byItemTypeList.get(it);
  }
  
  public void overrideItemType(ItemType it, Set<Entity> newEntities) {
    
  }
  
  public void removeItemsFromPool(Set<Entity> toRemove) {
    
  }
  
  public void removeAllItemsFromPoolExcept(Set<Entity> toKeep) {
    
  }

  private ItemType getItemTypeForEntity(Entity e) {
    String name = e.getName();
    if (name.contains("RecyclerJunk") || name.startsWith("Crafting.Ingredients")) {
      return ItemType.JUNK;
    } else if (name.equals("Misc.SpareParts")) {
      return ItemType.SPARE_PARTS;
    } else if (name.startsWith("Food.")) {
      return ItemType.FOOD;
    } else if (name.equals("EMPGrenadeWeapon") || name.equals("LureGrenadeWeapon")
        || name.equals("NullwaveTransmitterWeapon") || name.equals("RecyclerGrenadeWeapon")) {
      return ItemType.GRENADE;
    } else if (name.equals("Medical.PsiHypo")) {
      return ItemType.PSI_HYPO;
    } else if (name.equals("Medical.MedKit")) {
      return ItemType.MEDKIT;
    } else if (name.startsWith("Mods.Suit")) {
      return ItemType.CHIPSET_SUIT;
    } else if (name.startsWith("Mods.Psychoscope")) {
      return ItemType.CHIPSET_SCOPE;
    } else if (name.equals("Mods.Weapon.WeaponUpgradeKit")) {
      return ItemType.WEAPON_KIT;
    } else if (name.startsWith("Crafting.FabricationPlans")) {
      return ItemType.FAB_PLAN;
    } else if (name.startsWith("Weapons.")) {
      return ItemType.WEAPON;
    } else if (name.startsWith("Ammo.")) {
      return ItemType.AMMO;
    } else if (name.equals("Player.Neuromod") || name.equals("Player.Neuromod_Case")
        || name.equals("Player.Neuromod_Cinematic") || name.equals("Player.Neuromod_Calibration")) {
      return ItemType.NEUROMOD;
    } else if (name.startsWith("Medical.TraumaPharmas")) {
      return ItemType.PHARMA;
    } else if (name.equals("Player.SuitPatchKit")) {
      return ItemType.SUIT_REPAIR_KIT;
    } else {
      return ItemType.OTHER;
    }
  }
}
