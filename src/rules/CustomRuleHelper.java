package rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import databases.EntityDatabase;
import databases.TagHelper;
import utils.FileConsts.Archetype;
import utils.Utils;
import utils.XmlEntity;

/**
 * Low-level rule that allows getting a random entity given a series of
 * input/output tags.
 * 
 * @author Kida
 *
 */
public class CustomRuleHelper {

  EntityDatabase database;
  Random r;

  // Number of attempts to make at getting a valid tag before giving up.
  private static final int MAX_ATTEMPTS = 10;

  // Known prefixes at the start of entity names.
  private static final String[] PREFIXES = { "ArkGameplayArchitecture", "ArkHumans", "ArkNpcs", "ArkPhysicsProps",
      "ArkPickups", "ArkProjectiles", "ArkRobots", "ArkSpecialWeapons", "ArkInteractiveReadable" };

  // Arbitrary name to assign to this rule.
  private String name;
  // Tags to filter on
  private List<String> inputTags;
  // Tags to pull randomly from
  private List<String> outputTags;
  // Tags not to filter on. Takes priority.
  private List<String> doNotTouchTags;
  // Tags not to output. Takes priority.
  private List<String> doNotOutputTags;
  
  GiveUpBehaviour gub = GiveUpBehaviour.RETURN_BEST_MATCH;
  
  // Defines what to do if we can't find a valid match
  public enum GiveUpBehaviour {
    RETURN_BEST_MATCH,
    RETURN_NULL
  }

  public CustomRuleHelper(Random r) {
    this.database = EntityDatabase.getInstance(r);
    this.r = r;
    this.inputTags = new ArrayList<>();
    this.outputTags = new ArrayList<>();
    this.doNotTouchTags = new ArrayList<>();
    this.doNotOutputTags = new ArrayList<>();
    Arrays.sort(PREFIXES);
  }

  public CustomRuleHelper setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public CustomRuleHelper setInputTags(String... inputTags) {
    this.inputTags.addAll(Arrays.asList(inputTags));
    return this;
  }

  public CustomRuleHelper setOutputTags(String... outputTags) {
    this.outputTags.addAll(Arrays.asList(outputTags));
    return this;
  }

  public CustomRuleHelper setDoNotTouchTags(String... doNotTouchTags) {
    this.doNotTouchTags.addAll(Arrays.asList(doNotTouchTags));
    return this;
  }

  public CustomRuleHelper setDoNotOutputTags(String... doNotOutputTags) {
    this.doNotOutputTags.addAll(Arrays.asList(doNotOutputTags));
    return this;
  }
  
  public CustomRuleHelper setGiveUpBehaviour(GiveUpBehaviour gub) {
    this.gub = gub;
    return this;
  }

  /**
   * Whether this rule should apply to the given entity
   * 
   * @param entityName
   * @return
   */
  public boolean trigger(String entityName) {
    // Remove prefix
    int dotIndex = entityName.indexOf('.');
    entityName = entityName.substring(dotIndex + 1); 

    List<String> tags = getTagsForEntity(entityName);
    if (tags == null) {
      return false;
    }
    return Utils.getCommonElement(tags, inputTags) != null && Utils.getCommonElement(tags, doNotTouchTags) == null;
  }

  /**
   * Retrieves a random entity name that matches the given conditions.
   * 
   * @return
   */
  public String getEntityToSwap() {
    int numAttempts = 0;
    // Set a maximum on the number of attempts in case the tag block lists are
    // mutually exclusive
    String toSwapStr = null;
    while (numAttempts < MAX_ATTEMPTS) {
      int index = r.nextInt(outputTags.size());
      XmlEntity toSwap = database.getRandomEntityByTag(outputTags.get(index));
      Archetype a = toSwap.getArchetype();
      Set<String> tags = TagHelper.getTags(toSwap.getValue("Name"), a);

      // Check that this doesn't match one of the "do not output" tags
      if (Utils.getCommonElement(tags, doNotOutputTags) == null) {
        // Prepend with prefix
        return String.format("%s.%s", toSwap.getValue("Library"), toSwap.getValue("Name"));
      }
    }

    switch(gub) {
      default:
      case RETURN_BEST_MATCH:
        return toSwapStr;
      case RETURN_NULL:
        return null;
    }
  }

  @Override
  public String toString() {
    return name;
  }

  private ArrayList<String> getTagsForEntity(String entityName) {
    XmlEntity fullEntity = database.getEntityByName(entityName);
    if (fullEntity == null) {
      return null;
    }
    Archetype a = fullEntity.getArchetype();
    
    ArrayList<String> tags = new ArrayList<>();
    tags.addAll(TagHelper.getTags(entityName, a));
    return tags;
  }
}
