package databases;

import java.util.HashSet;
import java.util.Set;

import utils.FileConsts.Archetype;

public class TagHelper {

  // All valid tags
  public static final String TYPHON_TAG = "TYPHON";
  public static final String NIGHTMARE_TAG = "NIGHTMARE";
  public static final String BASE_MIMIC_TAG = "BASE_MIMIC";

  public static final String PHYSICS_PROP_TAG = "PHYSICS_PROP";
  public static final String PICKUP_TAG = "PICKUP";
  public static final String JUNK_TAG = "JUNK";

  public static final String WRENCH_TAG = "WRENCH";
  public static final String WHISKEY_TAG = "WHISKEY";
  public static final String CIGAR_TAG = "CIGAR";
  
  public static final String KEYCARD_TAG = "KEYCARD";
  public static final String MISSION_ITEM_TAG = "MISSION_ITEM";
  public static final String NOTE_TAG = "NOTE";
  public static final String EQUIPMENT_TAG = "EQUIPMENT";

  public static final String[] ITEM_TAGS = { JUNK_TAG, "SPARE_PARTS", "FOOD", "AMMO", "GRENADE", "PSI_HYPO", "MEDKIT",
      "CHIPSET_SUIT", "CHIPSET_SCOPE", "WEAPON_KIT", "FAB_PLAN", "WEAPON", "NEUROMOD", "PHARMA", "SUIT_REPAIR_KIT",
      "ITEM_OTHER", KEYCARD_TAG, NOTE_TAG, EQUIPMENT_TAG, MISSION_ITEM_TAG, "ALCOHOL", "GUN", WRENCH_TAG, WHISKEY_TAG, CIGAR_TAG };
  public static final String[] ITEM_GROUP_TAGS = { "HEALING", "CHIPSET" };
  public static final String[] ENEMY_TAGS = { BASE_MIMIC_TAG, "GREATER_MIMIC", "POLTERGEIST", "CYSTOID", "CYSTOID_NEST",
      "PHANTOM_ETHERIC", "PHANTOM_VOLTAIC", "PHANTOM_THERMAL", "WEAVER", "TECHNOPATH", "TELEPATH", "PHANTOM_NAMED",
      NIGHTMARE_TAG, "TENTACLE", "TYPHON_OTHER", "PHANTOM_BASE" };
  public static final String[] ENEMY_GROUP_TAGS = { "MIMIC", "PHANTOM" };
  public static final String[] ROBOT_TAGS = { "ENG_OP", "MED_OP", "SCI_OP", "MIL_OP", "TURRET", "ENG_OP_CORR",
      "MED_OP_CORR", "SCI_OP_CORR", "MIL_OP_CORR", "ROBOT_OTHER" };
  public static final String[] ROBOT_GROUP_TAGS = { "CORRUPTED", "FRIENDLY_ROBOT" };
  public static final String[] GAMEPLAY_TAGS = { "RECYCLER", "FABRICATOR", "O2_STATION" };
  public static final String[] OTHER_TAG = { "OTHER" };
  public static final String[] ENTITY_MISSING_TAG = { "ENTITY_MISSING" };
  // Every supported item will have this tag
  public static final String[] GLOBAL_TAG = { "GLOBAL" };
  // Every item will have a super type
  public static final String[] SUPER_TYPE = { PICKUP_TAG, PHYSICS_PROP_TAG, TYPHON_TAG, "HUMAN", "ROBOT", "PROJECTILE",
      "SUPER_TYPE_OTHER" };

  public static final String[][] TAG_LIST_LIST = { ITEM_TAGS, ITEM_GROUP_TAGS, ENEMY_TAGS, ENEMY_GROUP_TAGS, ROBOT_TAGS,
      ROBOT_GROUP_TAGS, GAMEPLAY_TAGS, OTHER_TAG, ENTITY_MISSING_TAG, GLOBAL_TAG, SUPER_TYPE };

  public static Set<String> getTags(String name, Archetype a) {
    Set<String> tags = new HashSet<>();
    if (name == null) {
      tags.add("ENTITY_MISSING");
      return tags;
    }
    tags.add("GLOBAL");

    // If archetype is null, use name to determine archetype
    if (a == null) {
      a = getArchetypeFromInvocationName(name);
      // If archetype still cannot be determined, return tags
      if (a == null) {
        return tags;
      }
    }

    switch (a) {
      case PICKUPS:
      case SPECIAL_WEAPONS:
        tags.add("PICKUP");
        addTagsForPickups(name, tags);
        break;
      case TYPHON:
        tags.add("TYPHON");
        addTagsForTyphon(name, tags);
        break;
      case ROBOTS:
        tags.add("ROBOT");
        addTagsForRobots(name, tags);
        break;
      case HUMANS:
        tags.add("HUMAN");
        break;
      case PHYSICS_PROPS:
        tags.add("PHYSICS_PROP");
        break;
      case PROJECTILES:
        tags.add("PROJECTILE");
        break;
      default:
        tags.add("SUPER_TYPE_OTHER");
        break;
    }

    return tags;
  }

  private static Archetype getArchetypeFromInvocationName(String name) {
    if (name.startsWith("ArkPickups")) {
      return Archetype.PICKUPS;
    } else if (name.startsWith("ArkPhysicsProps")) {
      return Archetype.PHYSICS_PROPS;
    } else if (name.startsWith("ArkNpcs")) {
      return Archetype.TYPHON;
    } else if (name.startsWith("ArkHumans")) {
      return Archetype.HUMANS;
    } else if (name.startsWith("ArkRobots")) {
      return Archetype.ROBOTS;
    } else if (name.startsWith("ArkProjectiles")) {
      return Archetype.PROJECTILES;
    }
    return null;
  }

  private static void addTagsForPickups(String name, Set<String> tags) {
    if (name.contains("RecyclerJunk") || name.startsWith("Crafting.Ingredients")) {
      tags.add("JUNK");
    } else if (name.equals("Misc.SpareParts")) {
      tags.add("SPARE_PARTS");
    } else if (name.startsWith("Food.")) {
      tags.add("FOOD");
      tags.add("HEALING");
      if (name.startsWith("Food.Alcohol")) {
        tags.add("ALCOHOL");
      }
    } else if (name.equals("EMPGrenadeWeapon") || name.equals("LureGrenadeWeapon")
        || name.equals("NullwaveTransmitterWeapon") || name.equals("RecyclerGrenadeWeapon")) {
      tags.add("GRENADE");
      tags.add("WEAPON");
    } else if (name.equals("Medical.PsiHypo")) {
      tags.add("PSI_HYPO");
    } else if (name.startsWith("Medical.MedKit")) {
      tags.add("MEDKIT");
      tags.add("HEALING");
    } else if (name.startsWith("Mods.Suit")) {
      tags.add("CHIPSET_SUIT");
      tags.add("CHIPSET");
    } else if (name.startsWith("Mods.Psychoscope")) {
      tags.add("CHIPSET_SCOPE");
      tags.add("CHIPSET");
    } else if (name.equals("Mods.Weapon.WeaponUpgradeKit")) {
      tags.add("WEAPON_KIT");
    } else if (name.startsWith("Crafting.FabricationPlans")) {
      tags.add("FAB_PLAN");
    } else if (name.startsWith("Weapons.")) {
      tags.add("WEAPON");
      tags.add("GUN");
    } else if (name.startsWith("Ammo.")) {
      tags.add("AMMO");
    } else if (name.equals("Player.Neuromod") || name.equals("Player.Neuromod_Case")
        || name.equals("Player.Neuromod_Cinematic") || name.equals("Player.Neuromod_Calibration")) {
      tags.add("NEUROMOD");
    } else if (name.startsWith("Medical.TraumaPharmas")) {
      tags.add("PHARMA");
    } else if (name.equals("Player.SuitPatchKit")) {
      tags.add("SUIT_REPAIR_KIT");
    } else if (name.startsWith("Data.Keycard")) {
      tags.add("KEYCARD");
    } else if (name.startsWith("Data.Notes")) {
      tags.add("NOTE");
    } else if (name.startsWith("MissionItems")) {
      tags.add("MISSION_ITEM");
    } else if (name.startsWith("Player.Psychoscope") || name.startsWith("Player.ZeroGSuit")) {
      tags.add("EQUIPMENT");
    } else {
      tags.add("ITEM_OTHER");
    }

    if (name.equals("Weapons.Wrench")) {
      tags.add("WRENCH");
    }
    if (name.equals("Food.Alcohol.Bourbon")) {
      tags.add("WHISKEY");
    }
    if (name.equals("Crafting.RecyclerJunk.UsedCigar")) {
      tags.add("CIGAR");
    }
  }

  private static void addTagsForTyphon(String name, Set<String> tags) {
    switch (name) {
      case "ArkNightmare":
        tags.add("NIGHTMARE");
        break;
      case "ArkPoltergeist":
        tags.add("POLTERGEIST");
        break;
      case "Mimics.Mimic":
        tags.add("MIMIC");
        tags.add("BASE_MIMIC");
        break;
      case "Mimics.EliteMimic":
        tags.add("MIMIC");
        tags.add("GREATER_MIMIC");
        break;
      case "Overseers.Technopath":
        tags.add("TECHNOPATH");
        break;
      case "Overseers.Telepath":
        tags.add("TELEPATH");
        break;
      case "Overseers.Weaver":
        tags.add("WEAVER");
        break;
      case "ApexTentacle.Tentacle_Large":
      case "ApexTentacle.Tentacle_Large_Guard":
      case "ApexTentacle.Tentacle_Medium":
      case "ApexTentacle.Tentacle_Medium_Guard":
      case "ApexTentacle.Tentacle_Small":
      case "ApexTentacle.Tentacle_Small_Guard":
        tags.add("TENTACLE");
        break;
      case "Cystoids.Cystoid":
        tags.add("CYSTOID");
        break;
      case "Cystoids.CystoidNest_Ceiling":
      case "Cystoids.CystoidNest_Floor":
      case "Cystoids.CystoidNest_Wall":
        tags.add("CYSTOID_NEST");
        break;
      case "Phantoms.BasePhantom":
        tags.add("PHANTOM_BASE");
        tags.add("PHANTOM");
        break;
      case "Phantoms.EthericPhantom":
        tags.add("PHANTOM_ETHERIC");
        tags.add("PHANTOM");
        break;
      case "Phantoms.ThermalPhantom":
        tags.add("PHANTOM_THERMAL");
        tags.add("PHANTOM");
        break;
      case "Phantoms.VoltaicPhantom":
        tags.add("PHANTOM_VOLTAIC");
        tags.add("PHANTOM");
        break;
      default:
        if (name.startsWith("Named Phantoms")) {
          tags.add("PHANTOM_NAMED");
          tags.add("PHANTOM");
        } else {
          tags.add("TYPHON_OTHER");
        }
    }

  }

  private static void addTagsForRobots(String name, Set<String> tags) {
    switch (name) {
      case "Operators\\Generic.EngineeringOperator":
        tags.add("ENG_OP");
        tags.add("FRIENDLY_ROBOT");
        break;
      case "Operators\\Generic.MedicalOperator":
        tags.add("MED_OP");
        tags.add("FRIENDLY_ROBOT");
        break;
      case "Operators\\Generic.MilitaryOperator":
        tags.add("MIL_OP");
        tags.add("FRIENDLY_ROBOT");
        break;
      case "Operators\\Generic.ScienceOperator":
        tags.add("SCI_OP");
        tags.add("FRIENDLY_ROBOT");
        break;
      case "Operators\\Generic\\Corrupted.EngineeringOperatorCorrupted":
        tags.add("ENG_OP_CORR");
        tags.add("CORRUPTED");
        break;
      case "Operators\\Generic\\Corrupted.MedicalOperatorCorrupted":
        tags.add("MED_OP_CORR");
        tags.add("CORRUPTED");
        break;
      case "Operators\\Generic\\Corrupted.MilitaryOperatorCorrupted":
        tags.add("MIL_OP_CORR");
        tags.add("CORRUPTED");
        break;
      case "Operators\\Generic\\Corrupted.ScienceOperatorCorrupted":
        tags.add("SCI_OP_CORR");
        tags.add("CORRUPTED");
        break;
      case "Turrets.Turret_Default":
        tags.add("TURRET");
        break;
      default:
        tags.add("ROBOT_OTHER");
    }
  }
}
