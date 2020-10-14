package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import json.FilterJson;
import json.GenericSpawnSettingsJson;
import utils.CustomRuleHelper;

/**
 * Describes how randomized spawns should be applied
 * 
 * @author Kida
 *
 */
public class GenericFilterSettings {

  List<CustomRuleHelper> customRuleHelpers;

  public GenericFilterSettings(Random r, GenericSpawnSettingsJson jsonSettings) {
    customRuleHelpers = new ArrayList<>();
    if (jsonSettings == null || jsonSettings.getFilters() == null) {
      return;
    }
    for (FilterJson filter : jsonSettings.getFilters()) {
      customRuleHelpers.add(new CustomRuleHelper(r).addInputTags(filter.getInputTags())
          .addOutputTags(filter.getOutputTags()).addOutputTagsWeights(filter.getOutputWeights())
          .addDoNotTouchTags(filter.getDoNotTouchTags()).addDoNotOutputTags(filter.getDoNotOutputTags()));
    }
  }

  public List<CustomRuleHelper> getCustomRuleHelpers() {
    return customRuleHelpers;
  }
}
