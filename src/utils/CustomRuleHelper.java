package utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetRule;

/**
 * Low-level rule that allows getting a random entity given a series of input/output tags.
 * 
 * @author Kida
 *
 */
public class CustomRuleHelper {

  // Number of attempts to make at getting a valid tag before giving up.
  private static final int MAX_ATTEMPTS = 10;

  // Map of randomizable tags to the list of random classes that support them.
  // If a given tag matches one of these, use the in-game random class instead of
  // our scuffed fixed randomization.
  private static final ImmutableMap<String, ImmutableList<String>> TAGS_TO_RANDOM_CLASS = new ImmutableMap.Builder<String, ImmutableList<String>>()
      .put("Ingredients", ImmutableList.of("Crafting.Ingredients.Random"))
      .put("Food", ImmutableList.of("Food.Random"))
      .put("RecyclerJunk", ImmutableList.of("Crafting.RecyclerJunk.Random"))
      .put("Mods", ImmutableList.of("Mods.Psychoscope.Random", "Mods.Suit.Random"))
      .put("FabricationPlans", ImmutableList.of("Crafting.FabricationPlans.Ammo.Random",
          "Crafting.FabricationPlans.Weapon.Random", "Crafting.FabricationPlans.Charge.Random",
          "Crafting.FabricationPlans.SuitMaint.Random", "Crafting.FabricationPlans.Pharma.Random"))
      .put("Alcohol", ImmutableList.of("Food.Alcohol.Random"))
      .put("TraumaPharmas", ImmutableList.of("Medical.TraumaPharmas.Random"))
      .build();

  // Weights to use for fabrication plans randomization
  private static final ImmutableList<Integer> FAB_PLAN_RANDOM_WEIGHTS = ImmutableList.of(5, 5, 5, 1, 1);

  // Tags not to affect, includes specific names of entities in specific levels (in level;name format)
  private List<String> doNotTouchTags;
  // Tags to pull randomly from
  private List<String> outputTags;
  // Relative weights to assign to the spawn rates for these tags
  private List<Integer> outputTagsWeights;

  // Set of valid entities that can be swapped
  private Set<String> validEntities = Sets.newHashSet();
  // Map of output tag to list of valid entities to swap in
  private Map<String, List<String>> availableEntities = Maps.newHashMap();

  public CustomRuleHelper(GenericSpawnPresetRule gfj, TaggedDatabase database) {
    this.doNotTouchTags = gfj.getDoNotTouchTagsList();
    this.outputTags = gfj.getOutputTagsList();
    this.outputTagsWeights = gfj.getOutputWeightsList();

    // Make a list of all valid entities that can be used as input
    for (String s : gfj.getInputTagsList()) {
      validEntities.addAll(database.getAllForTag(s));
    }
    for (String s : gfj.getDoNotTouchTagsList()) {
      validEntities.removeAll(database.getAllForTag(s));
    }
    // Make a map of all valid entities for output, keyed by the output tag
    Set<String> doNotOutput = Sets.newHashSet();
    for (String s : gfj.getDoNotOutputTagsList()) {
      doNotOutput.addAll(database.getAllForTag(s));
    }
    for (String s : outputTags) {
      Set<String> availableEntitiesForTag = Sets.newHashSet();
      availableEntitiesForTag.addAll(database.getAllForTag(s));
      availableEntitiesForTag.removeAll(doNotOutput);
      if (!availableEntitiesForTag.isEmpty()) {
        List<String> outputTagsList = Lists.newArrayList(availableEntitiesForTag);
        // Sort the output tags list to ensure this is still deterministic
        Collections.sort(outputTagsList);
        availableEntities.put(s, outputTagsList);
      }
    }
  }

  /**
   * Whether this rule should apply to the given entity
   * 
   * @param entityName
   * @return
   */
  public boolean trigger(TaggedDatabase database, String entityName, String nameInLevel) {
    entityName = Utils.stripLibraryPrefixForEntity(entityName);
    boolean nameInBlocklist = nameInLevel != null && doNotTouchTags != null && doNotTouchTags.contains(nameInLevel);
    return !nameInBlocklist && validEntities.contains(entityName);
  }

  /**
   * Retrieves a random entity name that matches the given conditions.
   * 
   * @return
   */
  public String getEntityToSwapStr(TaggedDatabase database, Random r) {
    return Utils.getNameForEntity(getEntityToSwap(database, r));
  }

  /**
   * Retrieves a random entity name that matches the given conditions.
   * 
   * @return a new element to sub in, or null if none could be found.
   */
  public Element getEntityToSwap(TaggedDatabase database, Random r) {
    // Re-roll entity until we find a valid one
    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      String tag = Utils.getRandomWeighted(outputTags, outputTagsWeights, r);

      // If this tag exactly matches a tag that's already supported by an in-game
      // randomization, use the built-in random type instead.
      if (TAGS_TO_RANDOM_CLASS.containsKey(tag)) {
        String newClass = "";
        if (tag.equals("FabricationPlans")) {
          newClass = Utils.getRandomWeighted(TAGS_TO_RANDOM_CLASS.get(tag), FAB_PLAN_RANDOM_WEIGHTS, r);
        } else {
          newClass = Utils.getRandom(TAGS_TO_RANDOM_CLASS.get(tag), r);
        }
        return database.getEntityByName(newClass);
      }

      if (availableEntities.containsKey(tag)) {
        return database.getEntityByName(Utils.getRandom(availableEntities.get(tag), r));
      } else {
        // There are no valid entity archetypes for this tag, continue
      }
    }
    return null;
  }
}
