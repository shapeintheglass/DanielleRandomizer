package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.CustomRuleHelper;

public class ItemSpawnSettings {
  public enum Preset {
    NONE, NO_LOGIC, // Randomize with no regard for type
    ALL_WRENCHES, ALL_JUNK, WHISKEY_AND_CIGARS,
  }

  private List<CustomRuleHelper> customRuleHelpers;

  private static final String[] OUTPUT_FRIENDLY_ITEMS = {
      /* Very valuable */
      "Neuromod", "MissionItems", "FabricationPlans", "ArkPsychoscope", "ZeroGSuit", "Weapons", "Mods",

      /* Valuable */
      "Medical", "Ammo",

      /* Nice to have */
      "Crafting", "Food", "RecyclerJunk", "SpareParts", "SuitPatchKit",

      /* Nearly useless */
      "Misc", "ArkBreakable", "Science", "Apartment", "Luxury", "Office", "Kitchen", "Chairs", "Bathroom", "Garden",
      "Gym", "Reployer", "RecyclerTrap", "Stickynote_Mimic" };
  private static final Integer[] OUTPUT_WEIGHTS = {
      /* Very valuable */
      1, 1, 1, 1, 1, 1, 1,
      /* Valuable */
      2, 8,
      /* Nice to have */
      10, 5, 30, 2, 2,
      /* Nearly useless */
      15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 1, 1, 1 };

  // To preserve game integrity, try not to overwrite important gameplay items.
  private static final String[] STORY_CRITICAL = { "Data", "MissionItems", "BigNullwaveTransmitterFabPlan",
      "AlexStationKeyFabPlan", "MorganStationKeyFabPlan", "PropulsionSystemFabPlan", "Player.Psychoscope" };

  // Props not to output
  private static final String[] DO_NOT_OUTPUT = { "Architecture", "Gameplay", "Industrial", "Light_Fixtures",
      "Static_ArkLights", "Shotgun_Golden", "DoubleWrench" };

  /**
   * Describes how enemy spawn rates should be applied
   */
  private ItemSpawnSettings(Preset p, Random r) {
    customRuleHelpers = new ArrayList<>();

    switch (p) {
      case ALL_JUNK:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags(OUTPUT_FRIENDLY_ITEMS).addOutputTags("RecyclerJunk")
            .addDoNotTouchTags(STORY_CRITICAL));
        break;
      case ALL_WRENCHES:
        customRuleHelpers.add(
            new CustomRuleHelper(r).addInputTags(OUTPUT_FRIENDLY_ITEMS).addOutputTags("Wrench").addDoNotTouchTags(STORY_CRITICAL));
        break;
      case NO_LOGIC:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags(OUTPUT_FRIENDLY_ITEMS)
            .addOutputTags(OUTPUT_FRIENDLY_ITEMS).addDoNotTouchTags(STORY_CRITICAL).addDoNotOutputTags(DO_NOT_OUTPUT)
            .addOutputTagsWeights(OUTPUT_WEIGHTS));
        break;
      case WHISKEY_AND_CIGARS:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags(OUTPUT_FRIENDLY_ITEMS).addOutputTags("Bourbon", "UsedCigar")
            .addDoNotTouchTags(STORY_CRITICAL));
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
