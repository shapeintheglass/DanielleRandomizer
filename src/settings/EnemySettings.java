package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import json.GenericRuleJson;
import json.GenericSpawnPresetJson;
import utils.CustomRuleHelper;

/**
 * Describes how enemy spawns should be applied
 * 
 * @author Kida
 *
 */
public class EnemySettings {

  List<CustomRuleHelper> customRuleHelpers;

  public EnemySettings(Random r, GenericSpawnPresetJson jsonSettings) {
    customRuleHelpers = new ArrayList<>();
    if (jsonSettings == null || jsonSettings.getRules() == null) {
      return;
    }
    for (GenericRuleJson filter : jsonSettings.getRules()) {
      customRuleHelpers.add(new CustomRuleHelper.Builder().addInputTags(filter.getInputTags())
                                                          .addOutputTags(filter.getOutputTags())
                                                          .addOutputTagsWeights(filter.getOutputWeights())
                                                          .addDoNotTouchTags(filter.getDoNotTouchTags())
                                                          .addDoNotOutputTags(filter.getDoNotOutputTags())
                                                          .build());
    }
  }

  public List<CustomRuleHelper> getCustomRuleHelpers() {
    return customRuleHelpers;
  }
}
