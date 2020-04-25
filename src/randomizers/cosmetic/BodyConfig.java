package randomizers.cosmetic;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class BodyConfig {

  Logger logger;

  Random rand;

  public enum Gender {
    MALE, FEMALE, UNKNOWN,
  }

  public static final String PREFIX = "objects/characters/humans/";
  public static final String MALE_BODY_TYPE = "genmale/genmale.chr";
  public static final String FEMALE_BODY_TYPE = "genfemale/genfemale.chr";
  public static final String LARGE_MALE_BODY_TYPE = "largemale/largemale.chr";

  public static final String[] FEMALE_BODIES = { "corporate/corporate_genfemalebody01",
      "mechanic/mechanic_genfemalebody01", "plumber/plumber_genfemalebody01", "scientist/scientist_genfemalebody01",
      "security/security_genfemalebody01", "morgankarl/morgankarlgenderselect_genfemale" };
  public static final String[] MALE_BODIES = { "corporate/corporate_genmalebody01", "dahl/dahl_genmalebody01",
      "labcoat/labcoat_genmalebody01", "mechanic/mechanic_genmalebody01", "scientist/scientist_genmalebody01",
      "security/security_genmalebody01", "volunteer/volunteer_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale" };

  private static final String[] ACCESSORY_COMPATIBLE_BODIES = { "corporate/corporate_genfemalebody01",
      "mechanic/mechanic_genfemalebody01", "scientist/scientist_genfemalebody01", "security/security_genfemalebody01",
      "morgankarl/morgankarlgenderselect_genfemale", "corporate/corporate_genmalebody01", "dahl/dahl_genmalebody01",
      "mechanic/mechanic_genmalebody01", "scientist/scientist_genmalebody01", "security/security_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale" };

  public static final String[] FEMALE_HEADS = { "genfemale/genfemale_head01", "genfemale/genfemale_head02",
      "genfemale/genfemale_head02_psychoscope", "genfemale/genfemale_head03", "genfemale/genfemale_head04",
      "genfemale/genfemale_head05", "genfemale/genfemale_head06", "mikhailailyushin/mikhailailyushin_genfemalehead01",
      "morgankarl/morgankarl_genfemalehead01", "saraelazar/saraelazar_genfemalehead01",
      "scientist/scientist_genfemalehead01" };
  public static final String FEMALE_HEAD_HELMET = "objects/accessories/breather/breather3p/breather3p_genfemale01";
  public static final String FEMALE_HEAD_HELMET_MTL = "objects/accessories/breather/breather3p/breather1";

  public static final String[] MALE_HEADS = { "aaroningram/aaroningram_genmale_head01", "dahl/dahl_genmalehead01",
      "drcalvino/drcalvino_genmalehead01", "drigwe/igwe_genmalehead01", "genmale/genmale_head01",
      "genmale/genmale_head01_psychoscope", "genmale/genmale_head01_psycho", "genmale/genmale_head02",
      "genmale/genmale_head03", "genmale/genmale_head03_psychoscope", "genmale/genmale_head04",
      "genmale/genmale_head05", "genmale/genmale_head06", "morgankarl/morgankarl_genmalehead01",
      "volunteer/volunteer_genmalehead01", "volunteer/volunteer_genmalehead02", "volunteer/volunteer_genmalehead03",
      "scientist/scientist_genmalehead01" };

  public static final String[] HEADS_NOT_TO_MODIFY = { "husk/husk_genfemalehead01", "husk/husk_genmalehead01", };
  // Used to make sure that hands aren't mismatched with heads
  public static final String[] HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS = { "drigwe/igwe_genmalehead01",
      "genmale/genmale_head04", "husk/husk_genmalehead01", "saraelazar/saraelazar_genfemalehead01",
      "genfemale/genfemale_head04" };
  public static final String[] BODIES_WITH_WHITE_HANDS = { "volunteer/volunteer_genmalebody01",
      "morgankarl/morgankarlgenderselect_genmale", "morgankarl/morgankarlgenderselect_genfemale" };

  // Used to make sure that "complete" heads do not have hair
  public static final String[] HEADS_THAT_SHOULD_NOT_HAVE_HAIR = { "genfemale/genfemale_head02_psychoscope",
      "genmale/genmale_head01_psychoscope", "genmale/genmale_head01_psycho", "husk/husk_genmalehead01",
      "husk/husk_genfemalehead01", "genmale/genmale_head01_husk", "genmale/genmale_head02_husk",
      "genmale/genmale_head03_psychoscope", "scientist/scientist_genmalehead01",
      "scientist/scientist_genfemalehead01" };

  public static final String[] FEMALE_HAIRS_01 = { "genfemale/genfemale_head01_hair01",
      "genfemale/genfemale_head01_hair03", "genfemale/genfemale_head01_hair05" };
  public static final String[] FEMALE_HAIRS_02 = { "genfemale/genfemale_head02_hair02",
      "genfemale/genfemale_head02_hair03", "genfemale/genfemale_head02_hair04", "genfemale/genfemale_head02_hair05" };
  public static final String[] FEMALE_HAIRS_03 = { "genfemale/genfemale_head03_hair02",
      "genfemale/genfemale_head03_hair03", "genfemale/genfemale_head03_hair04", "genfemale/genfemale_head03_hair05" };
  public static final String[] FEMALE_HAIRS_04 = { "genfemale/genfemale_head04_hair03",
      "genfemale/genfemale_head04_hair04", "genfemale/genfemale_head04_hair05" };
  public static final String[] FEMALE_HAIRS_05 = { "genfemale/genfemale_head05_hair03",
      "genfemale/genfemale_head05_hair05" };
  public static final String[] FEMALE_HAIRS_06 = { "genfemale/genfemale_head06_hair02",
      "genfemale/genfemale_head06_hair03", "genfemale/genfemale_head06_hair05" };
  // Sarah's hair is compatible with Morgan's and Mikhaila's. Morgan and Mikhaila
  // can use hair type 2.
  public static final String[] FEMALE_HAIR_MIKA = { "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01" };
  public static final String[] FEMALE_HAIR_MORGAN = { "morgankarl/morgankarl_genfemalehead01_hair01" };
  public static final String[] FEMALE_HAIR_SARA = { "saraelazar/saraelazar_genfemalehead01_hair01" };

  public static final String[] FEMALE_HAIR_COLORS_01 = { "genfemale/genfemale_hair01_black" };
  public static final String[] FEMALE_HAIR_COLORS_02 = { "genfemale/genfemale_hair02_black",
      "genfemale/genfemale_hair02_blonde", "genfemale/genfemale_hair02_brown", "genfemale/genfemale_hair02_gray",
      "genfemale/genfemale_hair02_red" };
  public static final String[] FEMALE_HAIR_COLORS_03 = { "genfemale/genfemale_hair03",
      "genfemale/genfemale_hair03_black", "genfemale/genfemale_hair03_blonde", "genfemale/genfemale_hair03_brown",
      "genfemale/genfemale_hair03_red" };
  public static final String[] FEMALE_HAIR_COLORS_04 = { "genfemale/genfemale_hair04_black",
      "genfemale/genfemale_hair04_blonde", "genfemale/genfemale_hair04_brown", "genfemale/genfemale_hair04_red" };
  public static final String[] FEMALE_HAIR_COLORS_05 = { "genfemale/genfemale_hair05_black",
      "genfemale/genfemale_hair05_blonde", "genfemale/genfemale_hair05_brown", "genfemale/genfemale_hair05_red" };
  public static final String[] FEMALE_HAIR_COLORS_06 = { "genfemale/genfemale_hair06" };

  public static final String[] MALE_HAIRS_01 = { "genmale/genmale_head01_hair01", "genmale/genmale_head01_hair03",
      "genmale/genmale_head01_hair05" };
  public static final String[] MALE_HAIRS_02 = { "genmale/genmale_head02_hair01", "genmale/genmale_head02_hair03",
      "genmale/genmale_head02_hair05" };
  public static final String[] MALE_HAIRS_03 = { "genmale/genmale_head03_hair03", "genmale/genmale_head03_hair05",
      "genmale/genmale_head03_hair06" };
  public static final String[] MALE_HAIRS_04 = { "genmale/genmale_head04_hair04" };
  public static final String[] MALE_HAIRS_05 = { "genmale/genmale_head05_hair01", "genmale/genmale_head05_hair05",
      "genmale/genmale_head05_hair06" };
  public static final String[] MALE_HAIRS_06 = { "genmale/genmale_head06_hair01", "genmale/genmale_head06_hair05",
      "genmale/genmale_head06_hair06" };
  public static final String[] MALE_HAIRS_V01 = { "volunteer/volunteer_genmalehead01_hair01" };
  public static final String[] MALE_HAIRS_V02 = { "volunteer/volunteer_genmalehead02_hair01" };
  public static final String[] MALE_HAIRS_V03 = { "volunteer/volunteer_genmalehead03_hair01" };
  public static final String[] MALE_HAIR_CALVINO = { "drcalvino/drcalvino_genmalehead01_hair01" };

  public static final String[] MALE_HAIR_COLORS_01 = { "genmale/genmale_hair01", "genmale/genmale_hair01_black",
      "genmale/genmale_hair01_blonde", "genmale/genmale_hair01_brown", "genmale/genmale_hair01_red" };
  public static final String[] MALE_HAIR_COLORS_03 = { "genmale/genmale_hair03", "genmale/genmale_hair03_black",
      "genmale/genmale_hair03_blonde", "genmale/genmale_hair03_brown", "genmale/genmale_hair03_red" };
  public static final String[] MALE_HAIR_COLORS_04 = { "genmale/genmale_hair04" };
  public static final String[] MALE_HAIR_COLORS_05 = { "genmale/genmale_hair05", "genmale/genmale_hair05_black",
      "genmale/genmale_hair05_blonde", "genmale/genmale_hair05_brown", "genmale/genmale_hair05_red" };
  public static final String[] MALE_HAIR_COLORS_06 = { "genmale/genmale_hair06", "genmale/genmale_hair06_black",
      "genmale/genmale_hair06_blonde" };
  public static final String[] MALE_HAIR_MORGAN = { "morgankarl/morgankarl_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_DAHL = { "dahl/dahl_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_IGWE = { "drigwe/igwe_genmalehead01_hair01" };
  public static final String[] MALE_HAIR_CALVINO_COLOR = { "drcalvino/dr_calvino_genmalehead01_hair" };
  public static final String[] MALE_HAIR_VOLUNTEER = { "volunteer/volunteer_hair01_brown" };

  // TODO: Bellamy's labcoat seems to be broken, female limbs as well
  public static final String MALE_LAB_COAT_LIMBS = "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalelegs01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>\n"
      + "  <Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"hands\" Binding=\"objects/characters/humans/labcoat/labcoat_genmalehands01.skin\" Material=\"objects/characters/humans/scientist/scientist_genmalebody01.mtl\" Flags=\"0\"/>";
  public static final String FEMALE_LAB_COAT_LIMBS = "<Attachment Inheritable=\"0\" Type=\"CA_SKIN\" AName=\"legs_arms\" Binding=\"objects/characters/humans/labcoat/labcoat_genfemalelegsarms01.skin\" Material=\"objects/characters/humans/scientist/scientist_genfemalebody01.mtl\" Flags=\"0\"/>";

  // (body_skin | head_skin | hair_skin), binding.skin, material.mtl, (0 | 1)
  private static final String SKIN_PATTERN = "  <Attachment Inheritable=\"1\" Type=\"CA_SKIN\" AName=\"%s\" Binding=\"%s.skin\" Material=\"%s.mtl\" SkinJointsOverrideSkeleton=\"%d\" Flags=\"0\"/>";

  private static final String PLUMBER_HAT = "objects/characters/humans/plumber/plumber_genfemalehat01";
  private static final String PLUMBER_HAT_MTL = "objects/characters/humans/plumber/plumber_genfemalebody01";

  public static final String[] ACCESSORIES_FEMALE = {
      "objects/accessories/propulsionpack/propulsionpack3p_genfemale01" };
  public static final String[] ACCESSORIES_FEMALE_MTL = { "objects/accessories/propulsionpack/propulsionpack_3p_01" };
  public static final String[] ACCESSORIES_MALE = { "objects/characters/humans/dahl/dahl_genmalebackpack01.skin",
      "objects/accessories/propulsionpack/propulsionpack3p_genmale01" };
  public static final String[] ACCESSORIES_MALE_MTL = { "objects/characters/humans/dahl/dahl_genmalebody01.mtl",
      "objects/accessories/propulsionpack/propulsionpack_3p_01" };

  public BodyConfig() {
    rand = new Random();
    logger = Logger.getLogger("BodyConfig");
    sort();
  }

  Gender gender = Gender.UNKNOWN;
  String bodyLine = "";
  String bodyModel = "";
  String headShort = "";
  String headLine = "";
  String headModel = "";
  String hairLine = "";
  String hairModel = "";
  String hairColor = "";

  public void setGender(Gender g) {
    this.gender = g;
  }

  public String generateRandomBody() {
    switch (gender) {
      case FEMALE:
        bodyModel = PREFIX + getRandom(FEMALE_BODIES);
        String mtl = bodyModel;

        if (bodyModel.contains("morgankarlgenderselect_genfemale")) {
          mtl = PREFIX + "morgankarl/morgan_genfemalebody01_cut_scene";
        }

        bodyLine = String.format(SKIN_PATTERN, "body_skin", bodyModel, mtl, 0);
        // If this is a lab coat, add appropriate arms/legs
        // TODO: Fix for female NPCs
        if (bodyLine.contains("labcoat")) {
          bodyLine += FEMALE_LAB_COAT_LIMBS;
        }
        if (bodyLine.contains("plumber/plumber_genfemalebody01")) {
          bodyLine += String.format(SKIN_PATTERN, "hat_skin", PLUMBER_HAT, PLUMBER_HAT_MTL, 0);
        }

        // Chance of adding an accessory if compatible
        String bm = bodyModel.replaceFirst(PREFIX, "");
        if (rand.nextInt(10) == 0 && Arrays.binarySearch(ACCESSORY_COMPATIBLE_BODIES, bm) >= 0) {
          bodyLine += String.format(SKIN_PATTERN, "propulsion_skin", ACCESSORIES_FEMALE[0], ACCESSORIES_FEMALE_MTL[0],
              0);
        }

        break;
      case MALE:
        bodyModel = PREFIX + getRandom(MALE_BODIES);
        mtl = bodyModel;
        if (bodyModel.contains("morgankarlgenderselect_genmale")) {
          mtl = PREFIX + "morgankarl/morgan_genmalebody01_cut_scene";
        }

        bodyLine = String.format(SKIN_PATTERN, "body_skin", bodyModel, mtl, 0);
        // if this is a lab coat, add appropriate arms/legs
        if (bodyLine.contains("labcoat")) {
          bodyLine += MALE_LAB_COAT_LIMBS;

        }

        // Chance of adding an accessory if compatible
        bm = bodyModel.replaceFirst(PREFIX, "");
        if (Arrays.binarySearch(ACCESSORY_COMPATIBLE_BODIES, bm) >= 0) {
          if (rand.nextInt(10) == 0) {
            bodyLine += String.format(SKIN_PATTERN, "propulsion_skin", ACCESSORIES_MALE[0], ACCESSORIES_MALE_MTL[0], 0);
          } else if (rand.nextInt(10) == 0) {
            bodyLine += String.format(SKIN_PATTERN, "propulsion_skin", ACCESSORIES_MALE[1], ACCESSORIES_MALE_MTL[1], 0);
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

    String bm = bodyModel.replaceFirst(PREFIX, "");
    // If the body type has white hands and the head should not have white hands,
    // re-roll the head.
    while (Arrays.binarySearch(BODIES_WITH_WHITE_HANDS, bm) >= 0
        && Arrays.binarySearch(HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS, hm) >= 0) {
      hm = getHeadModelForBody();
    }

    headModel = PREFIX + getHeadModelForBody();

    boolean isHelmet = (rand.nextInt(10) == 0);
    if (isHelmet && gender == Gender.FEMALE) {
      headModel = FEMALE_HEAD_HELMET;
      headLine = String.format(SKIN_PATTERN, "head_skin", headModel, FEMALE_HEAD_HELMET_MTL, 1);
      // Add the propulsion skin if it isn't present already.
      if (!bodyLine.contains("propulsion_skin")) {
        headLine += String.format(SKIN_PATTERN, "propulsion_skin", ACCESSORIES_FEMALE[0], ACCESSORIES_FEMALE_MTL[0],
            0);
      }
      
      return true;
    }
    headLine = String.format(SKIN_PATTERN, "head_skin", headModel, headModel, 1);
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
        return getRandom(BodyConfig.FEMALE_HEADS);
      case MALE:
        return getRandom(BodyConfig.MALE_HEADS);
      default:
        break;
    }
    return "";
  }

  private void generateRandomHairForHead() {
    String hm = headModel.replaceFirst(PREFIX, "");
    if (Arrays.binarySearch(HEADS_THAT_SHOULD_NOT_HAVE_HAIR, hm) >= 0 || headModel.equals(FEMALE_HEAD_HELMET)) {
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
      case PREFIX + "genfemale/genfemale_head01":
        hairModelShort = getRandom(FEMALE_HAIRS_01);
        break;
      case PREFIX + "genfemale/genfemale_head02":
        hairModelShort = getRandom(FEMALE_HAIRS_02, FEMALE_HAIR_MORGAN);
        break;
      case PREFIX + "genfemale/genfemale_head03":
        hairModelShort = getRandom(FEMALE_HAIRS_03);
        break;
      case PREFIX + "genfemale/genfemale_head04":
        hairModelShort = getRandom(FEMALE_HAIRS_04);
        break;
      case PREFIX + "genfemale/genfemale_head05":
        hairModelShort = getRandom(FEMALE_HAIRS_05);
        break;
      case PREFIX + "genfemale/genfemale_head06":
        hairModelShort = getRandom(FEMALE_HAIRS_06);
        break;
      case PREFIX + "mikhailailyushin/mikhailailyushin_genfemalehead01":
        hairModelShort = getRandom(FEMALE_HAIRS_02, FEMALE_HAIR_MORGAN, FEMALE_HAIR_MIKA, FEMALE_HAIR_SARA);
        break;
      case PREFIX + "morgankarl/morgankarl_genfemalehead01":
        hairModelShort = getRandom(FEMALE_HAIRS_02, FEMALE_HAIR_MORGAN, FEMALE_HAIR_MIKA, FEMALE_HAIR_SARA);
        break;
      case PREFIX + "saraelazar/saraelazar_genfemalehead01":
        hairModelShort = getRandom(FEMALE_HAIR_MORGAN, FEMALE_HAIR_MIKA, FEMALE_HAIR_SARA);
        break;
      default:
        logger.severe("Could not find hair for head " + headModel);
    }
    hairModel = PREFIX + hairModelShort;
    hairColor = PREFIX + getColorForHairFemale(hairModelShort);
    hairLine = String.format(SKIN_PATTERN, "hair_skin", hairModel, hairColor, 0);
  }

  private String getColorForHairFemale(String hairModelShort) {
    switch (hairModelShort) {
      case "genfemale/genfemale_head01_hair01":
        return getRandom(FEMALE_HAIR_COLORS_01);
      case "genfemale/genfemale_head02_hair02":
      case "genfemale/genfemale_head03_hair02":
      case "genfemale/genfemale_head06_hair02":
        return getRandom(FEMALE_HAIR_COLORS_02);
      case "genfemale/genfemale_head01_hair03":
      case "genfemale/genfemale_head02_hair03":
      case "genfemale/genfemale_head03_hair03":
      case "genfemale/genfemale_head04_hair03":
      case "genfemale/genfemale_head05_hair03":
      case "genfemale/genfemale_head06_hair03":
        return getRandom(FEMALE_HAIR_COLORS_03);
      case "genfemale/genfemale_head02_hair04":
      case "genfemale/genfemale_head03_hair04":
      case "genfemale/genfemale_head04_hair04":
        return getRandom(FEMALE_HAIR_COLORS_04);
      case "genfemale/genfemale_head01_hair05":
      case "genfemale/genfemale_head02_hair05":
      case "genfemale/genfemale_head03_hair05":
      case "genfemale/genfemale_head04_hair05":
      case "genfemale/genfemale_head05_hair05":
      case "genfemale/genfemale_head06_hair05":
        return getRandom(FEMALE_HAIR_COLORS_05);
      case "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01":
        return FEMALE_HAIR_MIKA[0];
      case "morgankarl/morgankarl_genfemalehead01_hair01":
        return FEMALE_HAIR_MORGAN[0];
      case "saraelazar/saraelazar_genfemalehead01_hair01":
        return FEMALE_HAIR_SARA[0];
      default:
        break;
    }
    logger.severe("Could not find a hair color for hair model " + hairModelShort);
    return "";
  }

  private void generateHairForHeadMale() {
    String hairModelShort = "";
    // Give a chance of some specific types being bald
    boolean isBald = rand.nextInt(2) >= 1;
    switch (headModel) {
      case PREFIX + "genmale/genmale_head01":
        hairModelShort = getRandom(MALE_HAIRS_01);
        break;
      case PREFIX + "genmale/genmale_head02":
        hairModelShort = getRandom(MALE_HAIRS_02);
        break;
      case PREFIX + "genmale/genmale_head03":
        hairModelShort = getRandom(MALE_HAIRS_03);
        break;
      case PREFIX + "genmale/genmale_head04":
        if (isBald) {
          return;
        }
        hairModelShort = getRandom(MALE_HAIRS_04);
        break;
      case PREFIX + "genmale/genmale_head05":
        hairModelShort = getRandom(MALE_HAIRS_05);
        break;
      case PREFIX + "genmale/genmale_head06":
        hairModelShort = getRandom(MALE_HAIRS_06);
        break;
      case PREFIX + "morgankarl/morgankarl_genmalehead01":
        hairModelShort = getRandom(MALE_HAIR_MORGAN);
        break;
      case PREFIX + "dahl/dahl_genmalehead01":
        hairModelShort = getRandom(MALE_HAIR_DAHL, MALE_HAIR_MORGAN);
        break;
      case PREFIX + "drcalvino/drcalvino_genmalehead01":
        hairModelShort = MALE_HAIR_CALVINO[0];
        break;
      case PREFIX + "drigwe/igwe_genmalehead01":
        hairModelShort = getRandom(MALE_HAIR_IGWE, MALE_HAIRS_02);
        break;
      case PREFIX + "aaroningram/aaroningram_genmale_head01":
        if (isBald) {
          return;
        }
        hairModelShort = MALE_HAIR_MORGAN[0];
        break;
      case PREFIX + "volunteer/volunteer_genmalehead01":
        if (isBald) {
          return;
        }
        hairModelShort = MALE_HAIRS_V01[0];
        break;
      case PREFIX + "volunteer/volunteer_genmalehead02":
        if (isBald) {
          return;
        }
        hairModelShort = MALE_HAIRS_V02[0];
        break;
      case PREFIX + "volunteer/volunteer_genmalehead03":
        if (isBald) {
          return;
        }
        hairModelShort = MALE_HAIRS_V03[0];
        break;
      default:
        new Exception("Could not find a hair for head model " + headModel).printStackTrace();
        System.exit(1);
    }
    hairModel = PREFIX + hairModelShort;
    hairColor = PREFIX + getColorForHairMale(hairModelShort);
    hairLine = String.format(SKIN_PATTERN, "hair_skin", hairModel, hairColor, 0);
  }

  private String getColorForHairMale(String hairModelShort) {
    switch (hairModelShort) {
      case "genmale/genmale_head01_hair01":
      case "genmale/genmale_head02_hair01":
      case "genmale/genmale_head05_hair01":
      case "genmale/genmale_head06_hair01":
        return getRandom(MALE_HAIR_COLORS_01);
      case "genmale/genmale_head01_hair03":
      case "genmale/genmale_head02_hair03":
      case "genmale/genmale_head03_hair03":
        return getRandom(MALE_HAIR_COLORS_03);
      case "genmale/genmale_head04_hair04":
        return getRandom(MALE_HAIR_COLORS_04);
      case "genmale/genmale_head01_hair05":
      case "genmale/genmale_head02_hair05":
      case "genmale/genmale_head03_hair05":
      case "genmale/genmale_head05_hair05":
      case "genmale/genmale_head06_hair05":
        return getRandom(MALE_HAIR_COLORS_05);
      case "genmale/genmale_head03_hair06":
      case "genmale/genmale_head05_hair06":
      case "genmale/genmale_head06_hair06":
        return getRandom(MALE_HAIR_COLORS_06);
      case "volunteer/volunteer_genmalehead01_hair01":
      case "volunteer/volunteer_genmalehead02_hair01":
      case "volunteer/volunteer_genmalehead03_hair01":
        return getRandom(MALE_HAIR_VOLUNTEER);
      case "dahl/dahl_genmalehead01_hair01":
        return MALE_HAIR_DAHL[0];
      case "morgankarl/morgankarl_genmalehead01_hair01":
        return MALE_HAIR_MORGAN[0];
      case "drigwe/igwe_genmalehead01_hair01":
        return MALE_HAIR_IGWE[0];
      case "drcalvino/drcalvino_genmalehead01_hair01":
        return MALE_HAIR_CALVINO_COLOR[0];
      default:
        new Exception("Could not find a hair color for hair model " + hairModelShort).printStackTrace();
        System.exit(1);
    }
    return "";
  }

  static void sort() {
    Arrays.sort(HEADS_THAT_SHOULD_NOT_HAVE_WHITE_HANDS);
    Arrays.sort(BODIES_WITH_WHITE_HANDS);
    Arrays.sort(HEADS_THAT_SHOULD_NOT_HAVE_HAIR);
    Arrays.sort(ACCESSORY_COMPATIBLE_BODIES);
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

  private String getRandom(String[] arr1, String[] arr2, String[] arr3, String[] arr4) {
    int index = rand.nextInt(arr1.length + arr2.length + arr3.length + arr4.length);
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
