package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import databases.TaggedDatabase;
import json.GenericRuleJson;

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
    this.inputTags = Arrays.asList(gfj.getInputTags());
    this.outputTags = Arrays.asList(gfj.getOutputTags());
    this.outputTagsWeights = new ArrayList<>(gfj.getOutputWeights().length);
    Arrays.stream(gfj.getOutputWeights())
          .forEach(s -> {
            outputTagsWeights.add(Integer.parseInt(s));
          });
    this.doNotTouchTags = Arrays.asList(gfj.getDoNotTouchTags());
    this.doNotOutputTags = Arrays.asList(gfj.getDoNotOutputTags());
  }

  public CustomRuleHelper(String name, List<String> inputTags, List<String> outputTags, List<Integer> outputTagsWeights,
      List<String> doNotTouchTags, List<String> doNotOutputTags) {
    this.inputTags = inputTags;
    this.outputTags = outputTags;
    this.outputTagsWeights = outputTagsWeights;
    this.doNotTouchTags = doNotTouchTags;
    this.doNotOutputTags = doNotOutputTags;
  }

  /**
   * Whether this rule should apply to the given entity
   * 
   * @param entityName
   * @return
   */
  public boolean trigger(TaggedDatabase database, String entityName) {
    return trigger(database, entityName, null);
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
      Set<String> tags = Utils.getTags(toSwap);

      // Check that this doesn't match one of the "do not output" tags
      if (Utils.getCommonElement(tags, doNotOutputTags) == null) {
        return toSwap;
      }
    }

    return toSwap;
  }

  private ArrayList<String> getTagsForEntity(TaggedDatabase database, String entityName) {
    Element fullEntity = database.getEntityByName(entityName);
    if (fullEntity == null) {
      return null;
    }

    ArrayList<String> tags = new ArrayList<>();
    tags.addAll(Utils.getTags(fullEntity));
    return tags;
  }

  public static class Builder {
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

    public Builder() {
      inputTags = new ArrayList<>();
      outputTags = new ArrayList<>();
      outputTagsWeights = new ArrayList<>();
      doNotTouchTags = new ArrayList<>();
      doNotOutputTags = new ArrayList<>();
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder addInputTags(String... inputTags) {
      if (inputTags == null) {
        return this;
      }
      this.inputTags.addAll(Arrays.asList(inputTags));
      return this;
    }

    public Builder addOutputTags(String... outputTags) {
      if (outputTags == null) {
        return this;
      }
      this.outputTags.addAll(Arrays.asList(outputTags));
      return this;
    }

    public Builder addOutputTagsWeights(Integer... outputTagsWeights) {
      if (outputTagsWeights == null) {
        return this;
      }
      this.outputTagsWeights.addAll(Arrays.asList(outputTagsWeights));
      return this;
    }

    public Builder addOutputTagsWeights(String... outputTagsWeights) {
      if (outputTagsWeights == null) {
        return this;
      }
      Arrays.stream(outputTagsWeights)
            .forEach(s -> this.outputTagsWeights.add(Integer.parseInt(s)));
      return this;
    }

    public Builder addDoNotTouchTags(String... doNotTouchTags) {
      if (doNotTouchTags == null) {
        return this;
      }
      this.doNotTouchTags.addAll(Arrays.asList(doNotTouchTags));
      return this;
    }

    public Builder addDoNotOutputTags(String... doNotOutputTags) {
      if (doNotOutputTags == null) {
        return this;
      }
      this.doNotOutputTags.addAll(Arrays.asList(doNotOutputTags));
      return this;
    }

    public CustomRuleHelper build() {
      return new CustomRuleHelper(name, inputTags, outputTags, outputTagsWeights, doNotTouchTags, doNotOutputTags);
    }
  }
}
