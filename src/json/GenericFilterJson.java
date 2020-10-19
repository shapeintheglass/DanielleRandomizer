package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericFilterJson {

  private String[] inputTags;
  private String[] outputTags;
  private String[] outputWeights;
  private String[] doNotTouchTags;
  private String[] doNotOutputTags;
  
  
  @JsonCreator
  public GenericFilterJson(
      @JsonProperty("input_tags") String[] inputTags, 
      @JsonProperty("output_tags") String[] outputTags,
      @JsonProperty("output_weights") String[] outputWeights, 
      @JsonProperty("do_not_touch_tags") String[] doNotTouchTags, 
      @JsonProperty("do_not_output_tags") String[] doNotOutputTags) {
    this.setInputTags(inputTags);
    this.setOutputTags(outputTags);
    this.setOutputWeights(outputWeights);
    this.setDoNotTouchTags(doNotTouchTags);
    this.setDoNotOutputTags(doNotOutputTags);
  }

  public String[] getInputTags() {
    return inputTags;
  }

  public void setInputTags(String[] inputTags) {
    this.inputTags = inputTags;
  }

  public String[] getOutputTags() {
    return outputTags;
  }

  public void setOutputTags(String[] outputTags) {
    this.outputTags = outputTags;
  }

  public String[] getOutputWeights() {
    return outputWeights;
  }

  public void setOutputWeights(String[] outputWeights) {
    this.outputWeights = outputWeights;
  }

  public String[] getDoNotTouchTags() {
    return doNotTouchTags;
  }

  public void setDoNotTouchTags(String[] doNotTouchTags) {
    this.doNotTouchTags = doNotTouchTags;
  }

  public String[] getDoNotOutputTags() {
    return doNotOutputTags;
  }

  public void setDoNotOutputTags(String[] doNotOutputTags) {
    this.doNotOutputTags = doNotOutputTags;
  }
}
