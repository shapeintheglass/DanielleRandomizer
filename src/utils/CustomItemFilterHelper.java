package utils;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import com.google.common.collect.Lists;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.ArchetypeSwapRule;

/*
 * Helper for filtering an entity given the randomizer settings.
 */
public class CustomItemFilterHelper {
  private static final int MAX_ATTEMPTS = 100;
  private static final String ARK_PICKUPS_TAG = "ArkPickups";
  private static final String SURVIVAL_MODE_ONLY_TAG = "_SURVIVAL";

  private TaggedDatabase database;
  private List<ArchetypeSwapRule> ruleSet;

  public CustomItemFilterHelper(Settings settings, TaggedDatabase database) {
    this.database = database;

    ruleSet = Lists.newArrayList();

    GenericSpawnPresetFilter spawnPreset = settings.getGameplaySettings().getItemSpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {
      GenericSpawnPresetRule.Builder copy =
          rule.toBuilder().addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
              .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
      if (!settings.getMoreSettings().getMoreGuns()) {
        copy.addDoNotOutputTags("Randomizer");
      }
      if (!settings.getMoreSettings().getPreySoulsGuns()) {
        copy.addDoNotOutputTags("PreySoulsWeapons");
      }
      if (!settings.getMoreSettings().getPreySoulsTurrets()) {
        copy.addDoNotOutputTags("PreySoulsTurrets");
      }
      ruleSet.add(new ArchetypeSwapRule(database, new CustomRuleHelper(copy.build(), database)));
    }
  }

  public List<ArchetypeSwapRule> getRuleSet() {
    return ruleSet;
  }

  /**
   * Specifically gets an entity that contains the tag "ArkPickups".
   * 
   * @param entityName
   * @param nameInLevel
   * @param r
   * @return
   */
  public Element getPickupEntity(String entityName, String nameInLevel, Random r) {
    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      Element e = getEntity(entityName, nameInLevel, r);
      if (e == null) {
        // Item was not filtered, return the original archetype as long as it's a pickup item
        if (entityName != null) {
          Element toReturn =
              database.getEntityByName(Utils.stripLibraryPrefixForEntity(entityName));
          if (Utils.getTags(toReturn).contains("ArkPickups")) {
            return toReturn;
          }
        }
        return null;
      }
      Set<String> tags = Utils.getTags(e);
      if (tags.contains(ARK_PICKUPS_TAG)) {
        return e;
      }
    }
    // No replacement found, return the original archetype as long as it's a pickup item
    if (entityName != null) {
      Element toReturn = database.getEntityByName(entityName);
      if (Utils.getTags(toReturn).contains("ArkPickups")) {
        return toReturn;
      }
    }
    return null;
  }

  /**
   * 
   * Specifically gets an entity that can be in a harvestable object. Some entity types don't show
   * up in the pickup log and may appear bugged.
   * 
   * @param entityName
   * @param nameInLevel
   * @param r
   * @return
   */
  public Element getPickupHarvestableEntity(String entityName, String nameInLevel, Random r) {
    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      Element e = getEntity(entityName, nameInLevel, r);
      Set<String> tags = Utils.getTags(e);
      if (tags.contains(ARK_PICKUPS_TAG) && !tags.contains(SURVIVAL_MODE_ONLY_TAG)
          && !tags.contains("FabricationPlans") && !tags.contains("Mods")) {
        return e;
      }
    }
    return null;
  }

  /**
   * Returns an entity that matches the current settings.
   * 
   * @param entityName Library name of the existing entity to replace.
   * @param nameInLevel Name of the existing entity within its level.
   * @param r
   * @return A new entity to replace the given one, or null if none could be found.
   */
  public Element getEntity(String entityName, String nameInLevel, Random r) {
    for (ArchetypeSwapRule rule : ruleSet) {
      if (rule.getCustomRuleHelper().trigger(database, entityName, nameInLevel)) {
        return rule.getCustomRuleHelper().getEntityToSwap(database, r);
      }
    }
    return null;
  }

  /**
   * Same as getEntity but returns the string form of the entity name instead of the entire entity.
   * 
   * @param entityName
   * @param nameInLevel
   * @param r
   * @return
   */
  public String getEntityStr(String entityName, String nameInLevel, Random r) {
    Element entityToSwap = getEntity(entityName, nameInLevel, r);
    if (entityToSwap == null) {
      return null;
    }
    return Utils.getNameForEntity(entityToSwap);
  }

  /**
   * Determines if we should trigger based on the given settings.
   * 
   * @param entityName
   * @param nameInLevel
   * @return
   */
  public boolean trigger(String entityName, String nameInLevel) {
    for (ArchetypeSwapRule rule : ruleSet) {
      if (rule.getCustomRuleHelper().trigger(database, entityName, nameInLevel)) {
        return true;
      }
    }
    return false;
  }
}
