package gui;

import json.NameAndDescription;

public class BaseCheckbox implements NameAndDescription {
  private String name;
  private String desc;

  public BaseCheckbox(String name, String desc) {
    this.name = name;
    this.desc = desc;
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }
}
