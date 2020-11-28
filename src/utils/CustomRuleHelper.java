package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jdom2.Element;
import databases.TaggedDatabase;
import json.GenericRuleJson;

/**
 * Low-level rule that allows getting a random entity given a series of input/output tags.
 * 
 * @author Kida
 *
 */
public class CustomRuleHelper {

  // Number of attempts to make at getting a valid tag before giving up.
  private static final int MAX_ATTEMPTS = 10;

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

  public CustomRuleHelper(GenericRuleJson gfj) {
    this.inputTags = gfj.getInputTags();
    this.outputTags = gfj.getOutputTags();
    if (gfj.getOutputWeights() != null) {
      this.outputTagsWeights = gfj.getOutputWeights();
    }
    if (gfj.getDoNotTouchTags() != null) {
      this.doNotTouchTags = gfj.getDoNotTouchTags();
    }
    if (gfj.getDoNotOutputTags() != null) {
      this.doNotOutputTags = gfj.getDoNotOutputTags();
    }
  }

  /**
   * Whether this rule should apply to the given entity
   * 
   * @param entityName
   * @return
   */
  public boolean trigger(TaggedDatabase database, String entityName, String nameInLevel) {
    // TODO: Intelligently detect if this has a library prefix
    entityName = Utils.stripPrefix(entityName);

    List<String> tags = getTagsForEntity(database, entityName);
    if (tags == null) {
      return false;
    }

    boolean entityInAllowlist = Utils.getCommonElement(tags, inputTags) != null;
    boolean entityInBlocklist = Utils.getCommonElement(tags, doNotTouchTags) != null;
    boolean nameInBlocklist = nameInLevel != null && doNotTouchTags.contains(nameInLevel);

    return entityInAllowlist && !(entityInBlocklist || nameInBlocklist);
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
      toSwap = DatabaseUtils.getRandomEntityByTags(database, r, outputTags, outputTagsWeights);

      // Check that this doesn't match one of the "do not output" tags
      if (generatedElementIsValid(toSwap, doNotOutputTags)) {
        return toSwap;
      }
    }
    return toSwap;
  }

  public static boolean generatedElementIsValid(Element toSwap, List<String> doNotOutputTags) {
    Set<String> tags = Utils.getTags(toSwap);
    return Utils.getCommonElement(tags, doNotOutputTags) == null;
  }

  private static ArrayList<String> getTagsForEntity(TaggedDatabase database, String entityName) {
    Element fullEntity = database.getEntityByName(entityName);
    if (fullEntity == null) {
      return null;
    }

    ArrayList<String> tags = new ArrayList<>();
    tags.addAll(Utils.getTags(fullEntity));
    return tags;
  }
}
