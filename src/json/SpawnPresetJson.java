package json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class SpawnPresetJson implements NameAndDescription {
  private static final String FILTERS = "filters";
  private static final String DESC2 = "desc";
  private static final String NAME2 = "name";

  @JsonProperty(NAME2)
  private String name;
  @JsonProperty(DESC2)
  private String desc;
  @JsonProperty(FILTERS)
  private List<GenericRuleJson> rules;

  @JsonCreator
  public SpawnPresetJson(@JsonProperty(NAME2) String name, @JsonProperty(DESC2) String desc,
      @JsonProperty(FILTERS) List<GenericRuleJson> rules) {
    this.name = name;
    this.desc = desc;
    this.rules = rules;
  }

  public SpawnPresetJson(JsonNode node) {
    this.name = node.has(NAME2) ? node.get(NAME2).textValue() : "";
    this.desc = node.has(DESC2) ? node.get(DESC2).textValue() : "";
    this.rules = new ArrayList<>();
    if (node.has(FILTERS)) {
      node.get(FILTERS).forEach(rule -> rules.add(new GenericRuleJson(rule)));
    }
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

  public List<GenericRuleJson> getRules() {
    return rules;
  }

  public void setRules(List<GenericRuleJson> rules) {
    this.rules = rules;
  }

  @Override
  public String toString() {
    return String.format("Name: %s\tDesc: %s\tFilters: %s", name, desc, rules);
  }
}
