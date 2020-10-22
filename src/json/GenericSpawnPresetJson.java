package json;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericSpawnPresetJson implements NameAndDescription {
  private String name;
  private String desc;
  private GenericRuleJson[] rules;

  @JsonCreator
  public GenericSpawnPresetJson(@JsonProperty("name") String name, @JsonProperty("desc") String desc,
      @JsonProperty("filters") GenericRuleJson[] rules) {
    this.name = name;
    this.desc = desc;
    this.rules = rules;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public GenericRuleJson[] getRules() {
    return rules;
  }

  public void setRules(GenericRuleJson[] rules) {
    this.rules = rules;
  }

  @Override
  public String toString() {
    return String.format("Name: %s\tDesc: %s\tFilters: %s", name, desc, Arrays.toString(rules));
  }
}
