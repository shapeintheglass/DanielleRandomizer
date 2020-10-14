package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericSpawnSettingsJson {
  private String name;
  private String desc;
  private FilterJson[] filters;


  @JsonCreator
  public GenericSpawnSettingsJson(
      @JsonProperty("name") String name, 
      @JsonProperty("desc") String desc,
      @JsonProperty("filters") FilterJson[] filters) {
    this.name = name;
    this.desc = desc;
    this.filters = filters;
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

  public FilterJson[] getFilters() {
    return filters;
  }

  public void setFilters(FilterJson[] filters) {
    this.filters = filters;
  }
}
