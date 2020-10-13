package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rules.CustomRuleHelper;

public class ItemSpawnSettings {
  public enum Preset {
    NONE, NO_LOGIC, // Randomize with no regard for type
    ALL_WRENCHES, ALL_JUNK, WHISKEY_AND_CIGARS,
  }

  private List<CustomRuleHelper> customRuleHelpers;

  private static final String[] TO_FILTER = { "ArkPickups", "ArkPhysicsProps" };

  // To preserve game integrity, try not to overwrite important gameplay items.
  private static final String[] STORY_CRITICAL = { "Data", "MissionItems",
      "Player", "BigNullwaveTransmitterFabPlan", "AlexStationKeyFabPlan",
      "MorganStationKeyFabPlan" };

  private static final String[] BAD_PROPS = { "Architecture",
      "Gameplay", "Industrial", "Light_Fixtures", "Static_ArkLights" };

  /**
   * Describes how enemy spawn rates should be applied
   */
  private ItemSpawnSettings(Preset p, Random r) {
    customRuleHelpers = new ArrayList<>();

    switch (p) {
    case ALL_JUNK:
      customRuleHelpers.add(new CustomRuleHelper(r).setInputTags(TO_FILTER)
          .setOutputTags("RecyclerJunk").setDoNotTouchTags(STORY_CRITICAL));
      break;
    case ALL_WRENCHES:
      customRuleHelpers.add(new CustomRuleHelper(r).setInputTags(TO_FILTER)
          .setOutputTags("Wrench").setDoNotTouchTags(STORY_CRITICAL));
      break;
    case NO_LOGIC:
      customRuleHelpers.add(new CustomRuleHelper(r).setInputTags(TO_FILTER)
          .setOutputTags(TO_FILTER).setDoNotTouchTags(STORY_CRITICAL)
          .setDoNotOutputTags(BAD_PROPS));
      break;
    case WHISKEY_AND_CIGARS:
      customRuleHelpers.add(new CustomRuleHelper(r).setInputTags(TO_FILTER)
          .setOutputTags("Bourbon", "UsedCigar")
          .setDoNotTouchTags(STORY_CRITICAL));
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

    public Builder setRandomizeMode(Preset p) {
      this.p = p;
      return this;
    }

    public Builder setRandom(Random r) {
      this.r = r;
      return this;
    }

    public ItemSpawnSettings build() {
      return new ItemSpawnSettings(p, r);
    }
  }
}
