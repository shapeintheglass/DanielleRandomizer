package json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class GenericRuleJson {

  private static final String OUTPUT_WEIGHTS = "output_weights";
  private static final String DO_NOT_OUTPUT_TAGS = "do_not_output_tags";
  private static final String DO_NOT_TOUCH_TAGS = "do_not_touch_tags";
  private static final String OUTPUT_TAGS = "output_tags";
  private static final String INPUT_TAGS = "input_tags";

  @JsonProperty(INPUT_TAGS)
  private List<String> inputTags;
  @JsonProperty(OUTPUT_TAGS)
  private List<String> outputTags;
  @JsonProperty(OUTPUT_WEIGHTS)
  private List<Integer> outputWeights;
  @JsonProperty(DO_NOT_TOUCH_TAGS)
  private List<String> doNotTouchTags;
  @JsonProperty(DO_NOT_OUTPUT_TAGS)
  private List<String> doNotOutputTags;

  @JsonCreator
  public GenericRuleJson(@JsonProperty(INPUT_TAGS) List<String> inputTags,
      @JsonProperty(OUTPUT_TAGS) List<String> outputTags,
      @JsonProperty(OUTPUT_WEIGHTS) List<Integer> outputWeights,
      @JsonProperty(DO_NOT_TOUCH_TAGS) List<String> doNotTouchTags,
      @JsonProperty(DO_NOT_OUTPUT_TAGS) List<String> doNotOutputTags) {
    this.inputTags = inputTags;
    this.outputTags = outputTags;
    this.outputWeights = outputWeights;
    this.doNotTouchTags = doNotTouchTags;
    this.doNotOutputTags = doNotOutputTags;
  }

  public GenericRuleJson(JsonNode node) {
    this.inputTags = new ArrayList<>();
    this.outputTags = new ArrayList<>();
    this.outputWeights = new ArrayList<>();
    this.doNotTouchTags = new ArrayList<>();
    this.doNotOutputTags = new ArrayList<>();

    if (node.has(INPUT_TAGS)) {
      node.get(INPUT_TAGS).forEach(s -> inputTags.add(s.textValue()));
    }
    if (node.has(OUTPUT_TAGS)) {
      node.get(OUTPUT_TAGS).forEach(s -> outputTags.add(s.textValue()));
    }
    if (node.has(OUTPUT_WEIGHTS)) {
      node.get(OUTPUT_WEIGHTS).forEach(s -> outputWeights.add(s.asInt(1)));
    }
    if (node.has(DO_NOT_TOUCH_TAGS)) {
      node.get(DO_NOT_TOUCH_TAGS).forEach(s -> doNotTouchTags.add(s.textValue()));
    }
    if (node.has(DO_NOT_OUTPUT_TAGS)) {
      node.get(DO_NOT_OUTPUT_TAGS).forEach(s -> doNotOutputTags.add(s.textValue()));
    }
  }

  public List<String> getInputTags() {
    return inputTags;
  }

  public void setInputTags(List<String> inputTags) {
    this.inputTags = inputTags;
  }

  public List<String> getOutputTags() {
    return outputTags;
  }

  public void setOutputTags(List<String> outputTags) {
    this.outputTags = outputTags;
  }

  public List<Integer> getOutputWeights() {
    return outputWeights;
  }

  public void setOutputWeights(List<Integer> outputWeights) {
    this.outputWeights = outputWeights;
  }

  public void addDoNotTouchTags(List<String> doNotTouch) {
    if (doNotTouchTags != null) {
      doNotTouchTags.addAll(doNotTouch);
    }
  }

  public List<String> getDoNotTouchTags() {
    return doNotTouchTags;
  }

  public void setDoNotTouchTags(List<String> doNotTouchTags) {
    this.doNotTouchTags = doNotTouchTags;
  }

  public void addDoNotOutputTags(List<String> doNotOutput) {
    if (doNotOutputTags != null) {
      doNotOutputTags.addAll(doNotOutput);
    }
  }

  public List<String> getDoNotOutputTags() {
    return doNotOutputTags;
  }

  public void setDoNotOutputTags(List<String> doNotOutputTags) {
    this.doNotOutputTags = doNotOutputTags;
  }

  public String toString() {
    return String.format(
        "input tags: %s\noutput tags: %s\noutput weights: %s\ndo not touch: %s\ndo not output: %s",
        inputTags, outputTags, outputWeights, doNotTouchTags, doNotOutputTags);
  }
}
