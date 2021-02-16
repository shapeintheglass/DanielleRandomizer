package utils;

import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

public class ItemMultiplierHelper {
  public enum Tier {
    ONLY_ONE, MAYBE_A_FEW, MAYBE_A_LOT, FUCK_IT
  }

  public static final int MAYBE_A_FEW_MIN_BOUND = 1;
  public static final int MAYBE_A_FEW_MAX_BOUND = 5;

  public static final int MAYBE_A_LOT_MIN_BOUND = 1;
  public static final int MAYBE_A_LOT_MAX_BOUND = 50;

  public static final int FUCK_IT_MIN_BOUND = 1;
  public static final int FUCK_IT_MAX_BOUND = 100;

  private static ImmutableSet<String> FUCK_IT_TIER = ImmutableSet.of("Ingredients");
  private static ImmutableSet<String> MAYBE_A_LOT_TIER = ImmutableSet.of("Ammo");
  private static ImmutableSet<String> MAYBE_A_FEW_TIER = ImmutableSet.of("Food", "EMPGrenades", "LureGrenades",
      "RecyclerGrenades", "NullwaveTransmitter", "RecyclerJunk");
  private static ImmutableSet<String> ONLY_ONE_TIER = ImmutableSet.of("FabricationPlans", "Weapons");

  public static Tier getTierForEntity(Element e) {
    Set<String> tags = Utils.getTags(e);
    return getTierForTags(tags);
  }

  public static Tier getTierForTags(Set<String> tags) {
    if (tags.isEmpty() || Utils.getCommonElement(tags, ONLY_ONE_TIER) != null) {
      return Tier.ONLY_ONE;
    }
    if (Utils.getCommonElement(tags, MAYBE_A_FEW_TIER) != null) {
      return Tier.MAYBE_A_FEW;
    }
    if (Utils.getCommonElement(tags, MAYBE_A_LOT_TIER) != null) {
      return Tier.MAYBE_A_LOT;
    }
    if (Utils.getCommonElement(tags, FUCK_IT_TIER) != null) {
      return Tier.FUCK_IT;
    }
    return Tier.ONLY_ONE;
  }

  public static int getMinForTier(Tier t) {
    switch (t) {
      case MAYBE_A_FEW:
        return MAYBE_A_FEW_MIN_BOUND;
      case MAYBE_A_LOT:
        return MAYBE_A_LOT_MIN_BOUND;
      case FUCK_IT:
        return FUCK_IT_MIN_BOUND;
      case ONLY_ONE:
      default:
        return 1;
    }
  }

  public static int getMaxForTier(Tier t) {
    switch (t) {
      case MAYBE_A_FEW:
        return MAYBE_A_FEW_MAX_BOUND;
      case MAYBE_A_LOT:
        return MAYBE_A_LOT_MAX_BOUND;
      case FUCK_IT:
        return FUCK_IT_MAX_BOUND;
      case ONLY_ONE:
      default:
        return 1;
    }
  }

  public static int getMultiplierForEntity(Element e, Random r) {
    Tier t = getTierForEntity(e);

    switch (t) {
      case MAYBE_A_FEW:
        return Utils.getRandomBetween(MAYBE_A_FEW_MIN_BOUND, MAYBE_A_FEW_MAX_BOUND, r);
      case MAYBE_A_LOT:
        return Utils.getRandomBetween(MAYBE_A_LOT_MIN_BOUND, MAYBE_A_LOT_MAX_BOUND, r);
      case FUCK_IT:
        return Utils.getRandomBetween(FUCK_IT_MIN_BOUND, FUCK_IT_MAX_BOUND, r);
      case ONLY_ONE:
      default:
        return 1;
    }
  }

  public static int getMultiplierForEntity(Set<String> tags, Random r) {
    Tier t = getTierForTags(tags);

    switch (t) {
      case MAYBE_A_FEW:
        return Utils.getRandomBetween(MAYBE_A_FEW_MIN_BOUND, MAYBE_A_FEW_MAX_BOUND, r);
      case MAYBE_A_LOT:
        return Utils.getRandomBetween(MAYBE_A_LOT_MIN_BOUND, MAYBE_A_LOT_MAX_BOUND, r);
      case FUCK_IT:
        return Utils.getRandomBetween(FUCK_IT_MIN_BOUND, FUCK_IT_MAX_BOUND, r);
      case ONLY_ONE:
      default:
        return 1;
    }
  }
}
