package utils;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Represents the body configuration of a human NPC.
 * @author Kida
 *
 */
public class BodyConfig {

  Logger logger;

  Random rand;

  public enum Gender {
    MALE, FEMALE, UNKNOWN,
  }

  public BodyConfig(Random r) {
    this.rand = r;
    logger = Logger.getLogger("BodyConfig");
    sort();
  }

  Gender gender = Gender.UNKNOWN;
  String bodyLine = "";
  public String bodyModel = "";
  String headShort = "";
  String headLine = "";
  public String headModel = "";
  String hairLine = "";
  public String hairModel = "";
  public String hairColor = "";

  public void setGender(Gender g) {
    this.gender = g;
  }

  public String generateRandomBody() {
    switch (gender) {
    case FEMALE:
      bodyModel = BodyConsts.PREFIX + getRandom(BodyConsts.FEMALE_BODIES);
      String mtl = bodyModel;

      if (bodyModel.contains("morgankarlgenderselect_genfemale")) {
        mtl = BodyConsts.PREFIX + "morgankarl/morgan_genfemalebody01_cut_scene";
      }

      bodyLine = String.format(BodyConsts.SKIN_PATTERN, "body_skin", bodyModel,
          mtl, 0);
      // If this is a lab coat, add appropriate arms/legs
      // TODO: Fix for female NPCs
      if (bodyLine.contains("labcoat")) {
        bodyLine += BodyConsts.FEMALE_LAB_COAT_LIMBS;
      }
      if (bodyLine.contains("plumber/plumber_genfemalebody01")) {
        bodyLine += String.format(BodyConsts.SKIN_PATTERN, "hat_skin",
            BodyConsts.PLUMBER_HAT, BodyConsts.PLUMBER_HAT_MTL, 0);
      }

      // Chance of adding an accessory if compatible
      String bm = bodyModel.replaceFirst(BodyConsts.PREFIX, "");
      if (rand.nextInt(10) == 0
          && Arrays.binarySearch(BodyConsts.ACCESSORY_COMPATIBLE_BODIES, bm) >= 0) {
        bodyLine += String.format(BodyConsts.SKIN_PATTERN, "propulsion_skin",
            BodyConsts.ACCESSORIES_FEMALE[0],
            BodyConsts.ACCESSORIES_FEMALE_MTL[0], 0);
      }

      break;
    case MALE:
      bodyModel = BodyConsts.PREFIX + getRandom(BodyConsts.MALE_BODIES);
      mtl = bodyModel;
      if (bodyModel.contains("morgankarlgenderselect_genmale")) {
        mtl = BodyConsts.PREFIX + "morgankarl/morgan_genmalebody01_cut_scene";
      }

      bodyLine = String.format(BodyConsts.SKIN_PATTERN, "body_skin", bodyModel,
          mtl, 0);
      // if this is a lab coat, add appropriate arms/legs
      if (bodyLine.contains("labcoat")) {
        bodyLine += BodyConsts.MALE_LAB_COAT_LIMBS;

      }

      // Chance of adding an accessory if compatible
      bm = bodyModel.replaceFirst(BodyConsts.PREFIX, "");
      if (Arrays.binarySearch(BodyConsts.ACCESSORY_COMPATIBLE_BODIES, bm) >= 0) {
        if (rand.nextInt(10) == 0) {
          bodyLine += String.format(BodyConsts.SKIN_PATTERN, "propulsion_skin",
              BodyConsts.ACCESSORIES_MALE[0],
              BodyConsts.ACCESSORIES_MALE_MTL[0], 0);
        } else if (rand.nextInt(10) == 0) {
          bodyLine += String.format(BodyConsts.SKIN_PATTERN, "propulsion_skin",
              BodyConsts.ACCESSORIES_MALE[1],
              BodyConsts.ACCESSORIES_MALE_MTL[1], 0);
        }
      }

      break;
    default:
      break;
    }
    return bodyLine;
  }

  public boolean generateRandomHeadAndHairForBody(String originalLine) {
    // If the original head is a husk, do not modify.
    if (originalLine.toLowerCase().contains("husk")) {
      headLine = originalLine;
      return false;
    }

    String hm = getHeadModelForBody();

    String bm = bodyModel.replaceFirst(BodyConsts.PREFIX, "");
    // If the body type has white hands and the head should not have white
    // hands,
    // re-roll the head.
    while (Arrays.binarySearch(BodyConsts.BODIES_WITH_WHITE_HANDS, bm) >= 0
        && Arrays.binarySearch(
            BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS, hm) >= 0) {
      hm = getHeadModelForBody();
    }

    headModel = BodyConsts.PREFIX + getHeadModelForBody();

    boolean isHelmet = (rand.nextInt(10) == 0);
    if (isHelmet && gender == Gender.FEMALE) {
      headModel = BodyConsts.FEMALE_HEAD_HELMET;
      headLine = String.format(BodyConsts.SKIN_PATTERN, "head_skin", headModel,
          BodyConsts.FEMALE_HEAD_HELMET_MTL, 1);
      // Add the propulsion skin if it isn't present already.
      if (!bodyLine.contains("propulsion_skin")) {
        headLine += String.format(BodyConsts.SKIN_PATTERN, "propulsion_skin",
            BodyConsts.ACCESSORIES_FEMALE[0],
            BodyConsts.ACCESSORIES_FEMALE_MTL[0], 0);
      }

      return true;
    }
    headLine = String.format(BodyConsts.SKIN_PATTERN, "head_skin", headModel,
        headModel, 1);
    // Also generate hair for this head
    generateRandomHairForHead();
    return true;
  }

  public String getHeadAndHair() {
    if (!hairLine.equals("")) {
      return headLine + '\n' + hairLine;
    }
    return headLine;
  }

  private String getHeadModelForBody() {
    switch (gender) {
    case FEMALE:
      return getRandom(BodyConsts.FEMALE_HEADS);
    case MALE:
      return getRandom(BodyConsts.MALE_HEADS);
    default:
      break;
    }
    return "";
  }

  private void generateRandomHairForHead() {
    String hm = headModel.replaceFirst(BodyConsts.PREFIX, "");
    if (Arrays.binarySearch(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR, hm) >= 0
        || headModel.equals(BodyConsts.FEMALE_HEAD_HELMET)) {
      return;
    }

    switch (gender) {
    case FEMALE:
      generateHairForHeadFemale();
      break;
    case MALE:
      generateHairForHeadMale();
      break;
    default:
      break;
    }
  }

  private void generateHairForHeadFemale() {

    String hairModelShort = "";
    switch (headModel) {
    case BodyConsts.PREFIX + "genfemale/genfemale_head01":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_01);
      break;
    case BodyConsts.PREFIX + "genfemale/genfemale_head02":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_02,
          BodyConsts.FEMALE_HAIR_MORGAN);
      break;
    case BodyConsts.PREFIX + "genfemale/genfemale_head03":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_03);
      break;
    case BodyConsts.PREFIX + "genfemale/genfemale_head04":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_04);
      break;
    case BodyConsts.PREFIX + "genfemale/genfemale_head05":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_05);
      break;
    case BodyConsts.PREFIX + "genfemale/genfemale_head06":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_06);
      break;
    case BodyConsts.PREFIX
        + "mikhailailyushin/mikhailailyushin_genfemalehead01":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_02,
          BodyConsts.FEMALE_HAIR_MORGAN, BodyConsts.FEMALE_HAIR_MIKA,
          BodyConsts.FEMALE_HAIR_SARA);
      break;
    case BodyConsts.PREFIX + "morgankarl/morgankarl_genfemalehead01":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIRS_02,
          BodyConsts.FEMALE_HAIR_MORGAN, BodyConsts.FEMALE_HAIR_MIKA,
          BodyConsts.FEMALE_HAIR_SARA);
      break;
    case BodyConsts.PREFIX + "saraelazar/saraelazar_genfemalehead01":
      hairModelShort = getRandom(BodyConsts.FEMALE_HAIR_MORGAN,
          BodyConsts.FEMALE_HAIR_MIKA, BodyConsts.FEMALE_HAIR_SARA);
      break;
    default:
      logger.severe("Could not find hair for head " + headModel);
    }
    hairModel = BodyConsts.PREFIX + hairModelShort;
    hairColor = BodyConsts.PREFIX + getColorForHairFemale(hairModelShort);
    hairLine = String.format(BodyConsts.SKIN_PATTERN, "hair_skin", hairModel,
        hairColor, 0);
  }

  private String getColorForHairFemale(String hairModelShort) {
    switch (hairModelShort) {
    case "genfemale/genfemale_head01_hair01":
      return getRandom(BodyConsts.FEMALE_HAIR_COLORS_01);
    case "genfemale/genfemale_head02_hair02":
    case "genfemale/genfemale_head03_hair02":
    case "genfemale/genfemale_head06_hair02":
      return getRandom(BodyConsts.FEMALE_HAIR_COLORS_02);
    case "genfemale/genfemale_head01_hair03":
    case "genfemale/genfemale_head02_hair03":
    case "genfemale/genfemale_head03_hair03":
    case "genfemale/genfemale_head04_hair03":
    case "genfemale/genfemale_head05_hair03":
    case "genfemale/genfemale_head06_hair03":
      return getRandom(BodyConsts.FEMALE_HAIR_COLORS_03);
    case "genfemale/genfemale_head02_hair04":
    case "genfemale/genfemale_head03_hair04":
    case "genfemale/genfemale_head04_hair04":
      return getRandom(BodyConsts.FEMALE_HAIR_COLORS_04);
    case "genfemale/genfemale_head01_hair05":
    case "genfemale/genfemale_head02_hair05":
    case "genfemale/genfemale_head03_hair05":
    case "genfemale/genfemale_head04_hair05":
    case "genfemale/genfemale_head05_hair05":
    case "genfemale/genfemale_head06_hair05":
      return getRandom(BodyConsts.FEMALE_HAIR_COLORS_05);
    case "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01":
      return BodyConsts.FEMALE_HAIR_MIKA[0];
    case "morgankarl/morgankarl_genfemalehead01_hair01":
      return BodyConsts.FEMALE_HAIR_MORGAN[0];
    case "saraelazar/saraelazar_genfemalehead01_hair01":
      return BodyConsts.FEMALE_HAIR_SARA[0];
    default:
      break;
    }
    logger.severe("Could not find a hair color for hair model "
        + hairModelShort);
    return "";
  }

  private void generateHairForHeadMale() {
    String hairModelShort = "";
    // Give a chance of some specific types being bald
    boolean isBald = rand.nextInt(2) >= 1;
    switch (headModel) {
    case BodyConsts.PREFIX + "genmale/genmale_head01":
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_01);
      break;
    case BodyConsts.PREFIX + "genmale/genmale_head02":
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_02);
      break;
    case BodyConsts.PREFIX + "genmale/genmale_head03":
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_03);
      break;
    case BodyConsts.PREFIX + "genmale/genmale_head04":
      if (isBald) {
        return;
      }
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_04);
      break;
    case BodyConsts.PREFIX + "genmale/genmale_head05":
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_05);
      break;
    case BodyConsts.PREFIX + "genmale/genmale_head06":
      hairModelShort = getRandom(BodyConsts.MALE_HAIRS_06);
      break;
    case BodyConsts.PREFIX + "morgankarl/morgankarl_genmalehead01":
      hairModelShort = getRandom(BodyConsts.MALE_HAIR_MORGAN);
      break;
    case BodyConsts.PREFIX + "dahl/dahl_genmalehead01":
      hairModelShort = getRandom(BodyConsts.MALE_HAIR_DAHL,
          BodyConsts.MALE_HAIR_MORGAN);
      break;
    case BodyConsts.PREFIX + "drcalvino/drcalvino_genmalehead01":
      hairModelShort = BodyConsts.MALE_HAIR_CALVINO[0];
      break;
    case BodyConsts.PREFIX + "drigwe/igwe_genmalehead01":
      hairModelShort = getRandom(BodyConsts.MALE_HAIR_IGWE,
          BodyConsts.MALE_HAIRS_02);
      break;
    case BodyConsts.PREFIX + "aaroningram/aaroningram_genmale_head01":
      if (isBald) {
        return;
      }
      hairModelShort = BodyConsts.MALE_HAIR_MORGAN[0];
      break;
    case BodyConsts.PREFIX + "volunteer/volunteer_genmalehead01":
      if (isBald) {
        return;
      }
      hairModelShort = BodyConsts.MALE_HAIRS_V01[0];
      break;
    case BodyConsts.PREFIX + "volunteer/volunteer_genmalehead02":
      if (isBald) {
        return;
      }
      hairModelShort = BodyConsts.MALE_HAIRS_V02[0];
      break;
    case BodyConsts.PREFIX + "volunteer/volunteer_genmalehead03":
      if (isBald) {
        return;
      }
      hairModelShort = BodyConsts.MALE_HAIRS_V03[0];
      break;
    default:
      new Exception("Could not find a hair for head model " + headModel)
          .printStackTrace();
      System.exit(1);
    }
    hairModel = BodyConsts.PREFIX + hairModelShort;
    hairColor = BodyConsts.PREFIX + getColorForHairMale(hairModelShort);
    hairLine = String.format(BodyConsts.SKIN_PATTERN, "hair_skin", hairModel,
        hairColor, 0);
  }

  private String getColorForHairMale(String hairModelShort) {
    switch (hairModelShort) {
    case "genmale/genmale_head01_hair01":
    case "genmale/genmale_head02_hair01":
    case "genmale/genmale_head05_hair01":
    case "genmale/genmale_head06_hair01":
      return getRandom(BodyConsts.MALE_HAIR_COLORS_01);
    case "genmale/genmale_head01_hair03":
    case "genmale/genmale_head02_hair03":
    case "genmale/genmale_head03_hair03":
      return getRandom(BodyConsts.MALE_HAIR_COLORS_03);
    case "genmale/genmale_head04_hair04":
      return getRandom(BodyConsts.MALE_HAIR_COLORS_04);
    case "genmale/genmale_head01_hair05":
    case "genmale/genmale_head02_hair05":
    case "genmale/genmale_head03_hair05":
    case "genmale/genmale_head05_hair05":
    case "genmale/genmale_head06_hair05":
      return getRandom(BodyConsts.MALE_HAIR_COLORS_05);
    case "genmale/genmale_head03_hair06":
    case "genmale/genmale_head05_hair06":
    case "genmale/genmale_head06_hair06":
      return getRandom(BodyConsts.MALE_HAIR_COLORS_06);
    case "volunteer/volunteer_genmalehead01_hair01":
    case "volunteer/volunteer_genmalehead02_hair01":
    case "volunteer/volunteer_genmalehead03_hair01":
      return getRandom(BodyConsts.MALE_HAIR_VOLUNTEER);
    case "dahl/dahl_genmalehead01_hair01":
      return BodyConsts.MALE_HAIR_DAHL[0];
    case "morgankarl/morgankarl_genmalehead01_hair01":
      return BodyConsts.MALE_HAIR_MORGAN[0];
    case "drigwe/igwe_genmalehead01_hair01":
      return BodyConsts.MALE_HAIR_IGWE[0];
    case "drcalvino/drcalvino_genmalehead01_hair01":
      return BodyConsts.MALE_HAIR_CALVINO_COLOR[0];
    default:
      new Exception("Could not find a hair color for hair model "
          + hairModelShort).printStackTrace();
      System.exit(1);
    }
    return "";
  }

  static void sort() {
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS);
    Arrays.sort(BodyConsts.BODIES_WITH_WHITE_HANDS);
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR);
    Arrays.sort(BodyConsts.ACCESSORY_COMPATIBLE_BODIES);
  }

  private String getRandom(String[] arr) {
    return arr[rand.nextInt(arr.length)];
  }

  private String getRandom(String[] arr1, String[] arr2) {
    int index = rand.nextInt(arr1.length + arr2.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    return arr2[index - arr1.length];
  }

  private String getRandom(String[] arr1, String[] arr2, String[] arr3) {
    int index = rand.nextInt(arr1.length + arr2.length + arr3.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    return arr3[index - arr1.length - arr2.length];
  }

  private String getRandom(String[] arr1, String[] arr2, String[] arr3,
      String[] arr4) {
    int index = rand.nextInt(arr1.length + arr2.length + arr3.length
        + arr4.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    if (index < arr1.length + arr2.length + arr3.length) {
      return arr3[index - arr1.length - arr2.length];
    }
    return arr3[index - arr1.length - arr2.length - arr3.length];
  }

  public String toString() {
    return bodyLine + '\n' + headLine + '\n' + hairLine;
  }
}
