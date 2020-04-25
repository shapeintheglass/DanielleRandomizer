package main;

import java.util.Collections;
import java.util.List;

import databases.ItemDatabase;
import databases.ItemDatabase.ItemType;
import utils.Entity;
import utils.ItemSpawnSettings;

public class Main {
  // private static final String installDir = "C:\\Program Files
  // (x86)\\Steam\\steamapps\\common\\Prey";

  public static void main(String[] args) {
    ItemType[] items = { ItemType.JUNK, ItemType.SPARE_PARTS, ItemType.FOOD, ItemType.AMMO, ItemType.GRENADE,
        ItemType.PSI_HYPO, ItemType.MEDKIT, ItemType.CHIPSET_SCOPE, ItemType.FAB_PLAN, ItemType.WEAPON_KIT,
        ItemType.WEAPON, ItemType.NEUROMOD };
    int[] percents = { 40, 16, 8, 8, 7, 5, 5, 5, 3, 3, 3, 3 };

    ItemDatabase id = new ItemDatabase(
        ItemSpawnSettings.builder().setItems(items).setPercents(percents).setIncludePhysicsProps(false).build());

    for (ItemType it : ItemType.values()) {
      System.out.println(it);
      List<Entity> eList = id.getAllByType(it);
      Collections.sort(eList);
      for (Entity e : eList) {
        System.out.println('\t' + e.getName());
      }
    }
  }
}
