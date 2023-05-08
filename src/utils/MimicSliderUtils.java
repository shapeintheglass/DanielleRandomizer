package utils;

import java.util.List;
import java.util.Set;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MimicSliderUtils {
  // Maximum number of mimics to spawn into a level, for performance reasons
  public static final int MAX_MIMICS = 1000;
  
  // Maximum distance (meters) where we can fudge the position of a mimicked object
  public static final float MIMIC_POSITION_FUDGE = 0.1f;
  
  // Props that contain all of these tags qualify as hidden mimics
  public static final ImmutableSet<String> PROP_MIMICABLE_TAGS =
      ImmutableSet.of("ArkPhysicsProps", "_MIMICABLE", "_CARRYABLE");
  // Props that contain any of these tags should not be mimicked
  public static final ImmutableSet<String> PROP_MIMICABLE_TAGS_BLOCKLIST =
      ImmutableSet.of("_LEVERAGE_I", "_LEVERAGE_II", "_LEVERAGE_III", "Tech", "Space", "_IMPORTANT",
          "_MISSION_ITEMS", "_PLOT_CRITICAL", "_PROGRESSION", "Data", "ArkLight", "ArkFruitTree",
          "TurretFabPlan", "NB_OxygenFuse", "ExplosiveTanks");
  // Pickups that contain all of these tags qualify as hidden mimics
  public static final ImmutableSet<String> PICKUP_MIMICABLE_TAGS =
      ImmutableSet.of("ArkPickups", "_MIMICABLE");
  public static final ImmutableSet<String> PICKUP_MIMICABLE_TAGS_BLOCKLIST =
      ImmutableSet.of("Data", "MissionItems", "_PROGRESSION", "_PLOT_CRITICAL", "ArkLight",
          "research/simulationlabs;Weapons.Wrench1", "ArkContainer", "Light_Fixtures",
          "ArkFruitTree", "TurretFabPlan", "NB_OxygenFuse", "ArkExplosiveTank");
  
  public static final ImmutableMap<String, List<AntiMimicZone>> ANTI_MIMIC_ZONES =
      new ImmutableMap.Builder<String, List<AntiMimicZone>>()
      // Morgan's apartment
      .put(LevelConsts.NEUROMOD_DIVISION, Lists.newArrayList(
          new AntiMimicZone(862.20569, 1586.2552, 6.7267303, 50.0)))
      // Morgan's office
      .put(LevelConsts.LOBBY, Lists.newArrayList(
          new AntiMimicZone(316.9465, 675.49658, 496.59598, 50.0)))
      // Shipping and Receiving Survivors - Put a bubble around some of them
      .put(LevelConsts.CARGO_BAY, Lists.newArrayList(
              // Sarah Elazar
              new AntiMimicZone(148.54999, 146.1898, 694.00763, 30.0),
              // Austin Cool
              new AntiMimicZone(156, 75, 692.75, 30.0),
              // Ekaterina Mulsaev
              new AntiMimicZone(148.45, 89.730003, 693.01001, 30.0),
              // Kevin Hague
              new AntiMimicZone(141, 113.2, 685.5, 30.0),
              // Alfred Rose
              new AntiMimicZone(140.41664, 120.75032, 693.04999, 30.0)))
      // Luka's Kitchen
      .put(LevelConsts.CREW_QUARTERS, 
          Lists.newArrayList(new AntiMimicZone(1228.3523, 878.86896, 216.48232, 50)))
      .build();
  
  public static boolean canBeHiddenMimic(Set<String> tags) {
    boolean isPickupAndMimicable = tags.containsAll(MimicSliderUtils.PICKUP_MIMICABLE_TAGS);
    boolean isPropAndMimicable = tags.containsAll(MimicSliderUtils.PROP_MIMICABLE_TAGS)
        && Sets.intersection(tags, MimicSliderUtils.PROP_MIMICABLE_TAGS_BLOCKLIST).isEmpty();
    return isPickupAndMimicable || isPropAndMimicable;
  }
  
  public static boolean isInAntiMimicZone(Element levelEntity, String levelDir) {
    // Optimization for endgame
    if (levelDir.equals(LevelConsts.ENDGAME)) {
      return true;
    }
    
    if (!ANTI_MIMIC_ZONES.containsKey(levelDir)) {
      return false;
    }
    
    String pos = levelEntity.getAttributeValue("Pos");
    if (pos == null) {
      return false;
    }
    String[] tokens = pos.split(",");
    double x = Double.parseDouble(tokens[0]);
    double y = Double.parseDouble(tokens[1]);
    double z = Double.parseDouble(tokens[2]);
    
    for (AntiMimicZone amz : ANTI_MIMIC_ZONES.get(levelDir)) {
      if (amz.isInAntiMimicZone(x, y, z)) {
        return true;
      }
    }
    return false;
  }
  
  private static class AntiMimicZone {
    double x;
    double y;
    double z;
    double r;
    public AntiMimicZone(double x, double y, double z, double r) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.r = r;
    }
    
    public boolean isInAntiMimicZone(double x1, double y1, double z1) {
      double dist = Math.sqrt(Math.pow(this.x - x1, 2) + Math.pow(this.y - y1, 2) + Math.pow(this.z - z, 2));
      return dist < r;
    }
  }
}
