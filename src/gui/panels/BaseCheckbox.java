package gui.panels;

import json.NameAndDescription;

public class BaseCheckbox implements NameAndDescription {
  private String name;
  private String desc;
  private boolean defaultValue;

  public BaseCheckbox(String name, String desc, boolean defaultValue) {
    this.name = name;
    this.desc = desc;
    this.defaultValue = defaultValue;
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public boolean getDefaultValue() {
    return defaultValue;
  }
}
