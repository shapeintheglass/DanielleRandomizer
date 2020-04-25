import filters.ItemSpawnFilter.ItemType;
import randomizers.gameplay.LevelRandomizer;
import utils.ItemSpawnSettings;

public class Main {
  private static final String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static ItemType[] DEFAULT_ITEM_ORDER = { ItemType.JUNK, ItemType.SPARE_PARTS, ItemType.FOOD, ItemType.AMMO, ItemType.GRENADE,
      ItemType.PSI_HYPO, ItemType.MEDKIT, ItemType.CHIPSET, ItemType.FAB_PLAN, ItemType.WEAPON_KIT, ItemType.WEAPON,
      ItemType.NEUROMOD };
  public static int[] DEFAULT_PERCENT_CHANCE = {40, 16, 8, 8, 7, 5, 5, 5, 3, 3, 3, 3};
  
  
  public static void main(String[] args) {
    LevelRandomizer lr = new LevelRandomizer(installDir, 
        ItemSpawnSettings.builder()
            .setItems(DEFAULT_ITEM_ORDER)
            .setPercents(DEFAULT_PERCENT_CHANCE)
            .build());
    lr.install();
  }
}
