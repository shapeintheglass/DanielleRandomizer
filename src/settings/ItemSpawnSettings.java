package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import json.GenericFilterJson;
import json.GenericSpawnPresetJson;
import utils.CustomRuleHelper;

public class ItemSpawnSettings {
  private List<CustomRuleHelper> customRuleHelpers;

  public ItemSpawnSettings(Random r, GenericSpawnPresetJson jsonSettings) {
    customRuleHelpers = new ArrayList<>();
    if (jsonSettings == null || jsonSettings.getFilters() == null) {
      return;
    }
    for (GenericFilterJson filter : jsonSettings.getFilters()) {
      customRuleHelpers.add(new CustomRuleHelper().addInputTags(filter.getInputTags())
          .addOutputTags(filter.getOutputTags()).addOutputTagsWeights(filter.getOutputWeights())
          .addDoNotTouchTags(filter.getDoNotTouchTags()).addDoNotOutputTags(filter.getDoNotOutputTags()));
    }
  }

  public List<CustomRuleHelper> getCustomRuleHelpers() {
    return customRuleHelpers;
  }
}
