package settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.CustomRuleHelper;

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

  private static final String[] OUTPUT_FRIENDLY_NPCS = {
      /* Easy */
      "Mimic", "BasePhantom", "Cystoids", "ApexTentacle",
      /* Medium */
      "EliteMimic", "ArkPoltergeist", "Operators\\Generic\\Corrupted", "Phantoms", "Named Phantoms",
      /* Hard */
      "ArkNightmare", "Overseers" };
  private static final Integer[] OUTPUT_WEIGHTS = {
      /* Easy */
      10, 10, 10, 10,
      /* Medium */
      5, 5, 5, 5, 5,
      /* Hard */
      1, 3 };

  private static final String[] DO_NOT_OUTPUT = { "Tentacle_Large_Guard", "Tentacle_Medium_Guard",
      "Tentacle_Small_Guard", "FakeTechnopath", "Cystoid_IgnorePlayer" };

  /**
   * Describes how enemy spawn rates should be applied
   */
  private EnemySettings(Preset p, Random r) {
    customRuleHelpers = new ArrayList<>();

    switch (p) {
      case NO_LOGIC:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags("ArkNpcs").addOutputTags(OUTPUT_FRIENDLY_NPCS)
            .addDoNotOutputTags(DO_NOT_OUTPUT).addOutputTagsWeights(OUTPUT_WEIGHTS));
        break;
      case ALL_NIGHTMARES:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags("ArkNpcs").addOutputTags("ArkNightmare"));
        break;
      case ALL_MIMICS:
        customRuleHelpers.add(new CustomRuleHelper(r).addInputTags("ArkNpcs").addOutputTags("Mimics"));
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
