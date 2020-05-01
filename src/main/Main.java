package main;

import filters.ItemSpawnFilter;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import rules.ShuffleTypeRule;

public class Main {
  private static final String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static void main(String[] args) {
    /**ItemSpawnFilter isf = new ItemSpawnFilter();
    isf.addRule(new ShuffleTypeRule().setTagsToShuffle("WEAPON", "AMMO", "PICKUP", "TYPHON", "ROBOT")
        .setTagsToIgnore("MISSION_ITEM", "KEYCARD"));
    LevelRandomizer lr = new LevelRandomizer(installDir).addFilter(isf);
    lr.install();*/
    
    LootTableRandomizer loot = new LootTableRandomizer(installDir);
    loot.randomize();
  }
}
