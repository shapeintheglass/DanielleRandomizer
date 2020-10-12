package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import databases.TagHelper;
import rules.CustomRuleHelper;

/**
 * Describes how enemy spawns should be applied
 * 
 * @author Kida
 *
 */
public class EnemySettings {

  public enum Preset {
    NONE, NO_LOGIC, // Randomize with no regard for type, including humans
    WITHIN_TYPE, // Randomize within type (robot, alien, human)
    ALL_NIGHTMARES, // Everything is nightmares
    ALL_MIMICS, // Everything is mimics
    CUSTOM, // Use custom settings
  }

  List<CustomRuleHelper> customRuleHelpers;

  /**
   * Describes how enemy spawn rates should be applied
   */
  private EnemySettings(Preset p, Random r) {
    customRuleHelpers = new ArrayList<>();

    switch (p) {
      case NO_LOGIC:
        customRuleHelpers
            .add(new CustomRuleHelper(r).setInputTags(TagHelper.TYPHON_TAG).setOutputTags(TagHelper.TYPHON_TAG));
        break;
      case WITHIN_TYPE:
        break;
      case ALL_NIGHTMARES:
        customRuleHelpers
            .add(new CustomRuleHelper(r).setInputTags(TagHelper.TYPHON_TAG).setOutputTags(TagHelper.NIGHTMARE_TAG));
        break;
      case ALL_MIMICS:
        customRuleHelpers
            .add(new CustomRuleHelper(r).setInputTags(TagHelper.TYPHON_TAG).setOutputTags(TagHelper.BASE_MIMIC_TAG));
        break;
      default:
      case NONE:
        break;
    }
  }

  public List<CustomRuleHelper> getCustomRuleHelpers() {
    return customRuleHelpers;
  }

  public static class Builder {
    Preset p;
    Random r;

    public Builder() {
      p = Preset.NONE;
      r = new Random();
    }

    public Builder setPreset(Preset p) {
      this.p = p;
      return this;
    }

    public Builder setRandom(Random r) {
      this.r = r;
      return this;
    }

    public EnemySettings build() {
      return new EnemySettings(p, r);
    }
  }
}
