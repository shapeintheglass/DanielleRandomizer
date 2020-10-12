package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import databases.TagHelper;
import rules.CustomRuleHelper;

public class ItemSpawnSettings {
  public enum Preset {
    NONE, 
    NO_LOGIC, // Randomize with no regard for type
    ALL_WRENCHES, 
    ALL_JUNK, 
    WHISKEY_AND_CIGARS, 
  }

  private List<CustomRuleHelper> customRuleHelpers;
  
  // To preserve game integrity, try not to overwrite important gameplay items.
  private static final String[] DO_NOT_TOUCH = { TagHelper.KEYCARD_TAG, TagHelper.MISSION_ITEM_TAG, TagHelper.NOTE_TAG,
      TagHelper.EQUIPMENT_TAG };
  
  

  /**
   * Describes how enemy spawn rates should be applied
   */
  private ItemSpawnSettings(Preset p, Random r) {
    customRuleHelpers = new ArrayList<>();
    
    switch(p) {
      case ALL_JUNK:
        customRuleHelpers.add(new CustomRuleHelper(r)
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.JUNK_TAG)
            .setDoNotTouchTags(DO_NOT_TOUCH));
        break;
      case ALL_WRENCHES:
        customRuleHelpers.add(new CustomRuleHelper(r)
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.WRENCH_TAG)
            .setDoNotTouchTags(DO_NOT_TOUCH));
        break;
      case NO_LOGIC:
        customRuleHelpers.add(new CustomRuleHelper(r)
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setDoNotTouchTags(DO_NOT_TOUCH));
        break;
      case WHISKEY_AND_CIGARS:
        customRuleHelpers.add(new CustomRuleHelper(r)
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.WHISKEY_TAG, TagHelper.CIGAR_TAG)
            .setDoNotTouchTags(DO_NOT_TOUCH));
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
