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
import utils.FileConsts;
import utils.ItemSpawnSettings;
import utils.Utils;

public class ItemDatabase {

  Random r;

  ItemType[] items;
  int[] percents;

  public enum ItemType {
    JUNK, SPARE_PARTS, FOOD, AMMO, GRENADE, PSI_HYPO, MEDKIT, CHIPSET_SUIT, CHIPSET_SCOPE, WEAPON_KIT, FAB_PLAN, WEAPON, NEUROMOD, PHARMA, SUIT_REPAIR_KIT, OTHER
  }

  List<Entity> allItemsList;
  Map<ItemType, List<Entity>> byItemTypeList;
  boolean includePhysicsProps;
  
  public ItemDatabase(Map<ItemType, List<Entity>> database) {
    this.byItemTypeList = database;
    allItemsList = new ArrayList<>();
    for (ItemType i : ItemType.values()) {
      allItemsList.addAll(byItemTypeList.get(i));
    }
  }

  public ItemDatabase(boolean includePhysicsProps) {
    r = new Random();
    this.includePhysicsProps = includePhysicsProps;
    byItemTypeList = new HashMap<ItemType, List<Entity>>();
    for (ItemType i : ItemType.values()) {
      byItemTypeList.put(i, new ArrayList<Entity>());
    }
    allItemsList = new ArrayList<>();

    populateDatabase();
  }

  private void populateDatabase() {
    getEntitiesFromFile(FileConsts.ARK_PICKUPS_FILE);
    getEntitiesFromFile(FileConsts.ARK_SPECIAL_WEAPONS_FILE);
    if (includePhysicsProps) {
      getEntitiesFromFile(FileConsts.ARK_PHYSICS_PROPS_FILE);
    }
  }

  public void getEntitiesFromFile(String file) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      String line = br.readLine();
      while (line != null) {

        if (line.contains("<EntityPrototype ")) {
          Map<String, String> keys = Utils.getKeysFromLine(line);

          String name = keys.get("name");
          Entity e = Entity.builder().setName(name).setId(keys.get("id"))
              .setLibrary(keys.get("library")).setArkClass(keys.get("class"))
              .setArchetypeId(keys.get("archetypeid")).build();

          allItemsList.add(e);

          ItemType type = getItemTypeForEntity(e);
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
    if (name.contains("RecyclerJunk")
        || name.startsWith("Crafting.Ingredients")) {
      return ItemType.JUNK;
    } else if (name.equals("Misc.SpareParts")) {
      return ItemType.SPARE_PARTS;
    } else if (name.startsWith("Food.")) {
      return ItemType.FOOD;
    } else if (name.equals("EMPGrenadeWeapon")
        || name.equals("LureGrenadeWeapon")
        || name.equals("NullwaveTransmitterWeapon")
        || name.equals("RecyclerGrenadeWeapon")) {
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
    } else if (name.equals("Player.Neuromod")
        || name.equals("Player.Neuromod_Case")
        || name.equals("Player.Neuromod_Cinematic")
        || name.equals("Player.Neuromod_Calibration")) {
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
