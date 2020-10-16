package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import databases.TaggedDatabase;

/**
 * Low-level rule that allows getting a random entity given a series of
 * input/output tags.
 * 
 * @author Kida
 *
 */
public class CustomRuleHelper {

  // Number of attempts to make at getting a valid tag before giving up.
  private static final int MAX_ATTEMPTS = 10;

  // Known prefixes at the start of entity names.
  private static final String[] PREFIXES = { "ArkGameplayArchitecture",
      "ArkHumans", "ArkNpcs", "ArkPhysicsProps", "ArkPickups",
      "ArkProjectiles", "ArkRobots", "ArkSpecialWeapons",
      "ArkInteractiveReadable" };

  // Arbitrary name to assign to this rule.
  private String name;
  // Tags to filter on
  private List<String> inputTags;
  // Tags to pull randomly from
  private List<String> outputTags;
  // Relative weights to assign to the spawn rates for these tags
  private List<Integer> outputTagsWeights;
  // Tags not to filter on. Takes priority.
  private List<String> doNotTouchTags;
  // Tags not to output. Takes priority.
  private List<String> doNotOutputTags;

  GiveUpBehaviour gub = GiveUpBehaviour.RETURN_BEST_MATCH;

  // Defines what to do if we can't find a valid match
  public enum GiveUpBehaviour {
    RETURN_BEST_MATCH, RETURN_NULL
  }

  public CustomRuleHelper() {
    this.inputTags = new ArrayList<>();
    this.outputTags = new ArrayList<>();
    this.outputTagsWeights = new ArrayList<>();
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

  public CustomRuleHelper addInputTags(String... inputTags) {
    if (inputTags == null) {
      return this;
    }
    this.inputTags.addAll(Arrays.asList(inputTags));
    return this;
  }

  public CustomRuleHelper addOutputTags(String... outputTags) {
    if (outputTags == null) {
      return this;
    }
    this.outputTags.addAll(Arrays.asList(outputTags));
    return this;
  }

  public CustomRuleHelper addOutputTagsWeights(Integer... outputTagsWeights) {
    if (outputTagsWeights == null) {
      return this;
    }
    this.outputTagsWeights.addAll(Arrays.asList(outputTagsWeights));
    return this;
  }

  public CustomRuleHelper addOutputTagsWeights(String... outputTagsWeights) {
    if (outputTagsWeights == null) {
      return this;
    }
    Arrays.stream(outputTagsWeights).forEach(
        s -> this.outputTagsWeights.add(Integer.parseInt(s)));
    return this;
  }

  public CustomRuleHelper addDoNotTouchTags(String... doNotTouchTags) {
    if (doNotTouchTags == null) {
      return this;
    }
    this.doNotTouchTags.addAll(Arrays.asList(doNotTouchTags));
    return this;
  }

  public CustomRuleHelper addDoNotOutputTags(String... doNotOutputTags) {
    if (doNotOutputTags == null) {
      return this;
    }
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
  public boolean trigger(TaggedDatabase database, String entityName) {
    // Remove prefix
    int dotIndex = entityName.indexOf('.');
    entityName = entityName.substring(dotIndex + 1);

    List<String> tags = getTagsForEntity(database, entityName);
    if (tags == null) {
      return false;
    }
    return Utils.getCommonElement(tags, inputTags) != null
        && Utils.getCommonElement(tags, doNotTouchTags) == null;
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
   * @return
   */
  public Element getEntityToSwap(TaggedDatabase database, Random r) {
    int numAttempts = 0;
    // Set a maximum on the number of attempts in case the tag block lists are
    // mutually exclusive
    Element toSwap = null;
    while (numAttempts < MAX_ATTEMPTS) {
      toSwap = DatabaseUtils.getRandomEntityByTags(database, r, outputTags,
          outputTagsWeights);
      Set<String> tags = Utils.getTags(toSwap);

      // Check that this doesn't match one of the "do not output" tags
      if (Utils.getCommonElement(tags, doNotOutputTags) == null) {
        return toSwap;
      }
    }

    switch (gub) {
    default:
    case RETURN_BEST_MATCH:
      return toSwap;
    case RETURN_NULL:
      return null;
    }
  }

  @Override
  public String toString() {
    return name;
  }

  private ArrayList<String> getTagsForEntity(TaggedDatabase database,
      String entityName) {
    Element fullEntity = database.getEntityByName(entityName);
    if (fullEntity == null) {
      return null;
    }

    ArrayList<String> tags = new ArrayList<>();
    tags.addAll(Utils.getTags(fullEntity));
    return tags;
  }
}
