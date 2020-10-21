package json;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericSpawnPresetJson implements NameAndDescription {
  private String name;
  private String desc;
  private GenericFilterJson[] filters;

  @JsonCreator
  public GenericSpawnPresetJson(@JsonProperty("name") String name, @JsonProperty("desc") String desc,
      @JsonProperty("filters") GenericFilterJson[] filters) {
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

  public GenericFilterJson[] getFilters() {
    return filters;
  }

  public void setFilters(GenericFilterJson[] filters) {
    this.filters = filters;
  }

  @Override
  public String toString() {
    return String.format("Name: %s\tDesc: %s\tFilters: %s", name, desc, Arrays.toString(filters));
  }
}
