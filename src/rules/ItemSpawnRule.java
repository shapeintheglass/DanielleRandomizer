package rules;

import java.util.Map;

import filters.ItemSpawnFilter.ItemType;
import utils.ItemSpawnSettings;

public class ItemSpawnRule extends BaseRule {
  
  ItemSpawnSettings iss;
  
  ItemType[] items;
  int[] percents;
  
  
  boolean includePhysicsObjects;
  
  public ItemSpawnRule(ItemSpawnSettings iss) {
    this.iss = iss;
  }
  
  // Apply to any archetype that can be picked up
  public boolean trigger(Map<String, String> config) {
    return config.get("archetype").toLowerCase().contains("arkpickups");
  }
  
  public void apply() {
    
  }
}
