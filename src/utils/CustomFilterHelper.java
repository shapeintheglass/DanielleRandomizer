package utils;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import json.GenericRuleJson;
import json.SpawnPresetJson;

public class CustomFilterHelper {

  private SpawnPresetJson spawnPreset;
  private TaggedDatabase database;

  public CustomFilterHelper(SpawnPresetJson spawnPreset, TaggedDatabase database) {
    this.spawnPreset = spawnPreset;
    this.database = database;
  }

  public Element getEntity(String entityName, String nameInLevel, Random r) {
    for (GenericRuleJson rule : spawnPreset.getRules()) {
      CustomRuleHelper ruleHelper = new CustomRuleHelper(rule);

      if (ruleHelper.trigger(database, entityName, nameInLevel)) {
        return ruleHelper.getEntityToSwap(database, r);
      }
    }
    return null;
  }

  public String getEntityStr(String entityName, String nameInLevel, Random r) {
    Element entityToSwap = getEntity(entityName, nameInLevel, r);
    if (entityToSwap == null) {
      return null;
    }
    return Utils.getNameForEntity(entityToSwap);
  }

  public boolean trigger(String entityName, String nameInLevel) {
    for (GenericRuleJson rule : spawnPreset.getRules()) {
      CustomRuleHelper crh = new CustomRuleHelper(rule);
      if (crh.trigger(database, entityName, nameInLevel)) {
        return true;
      }
    }
    return false;
  }
}
