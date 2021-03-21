package randomizers.generators;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.jdom2.Element;
import proto.Body.Human;
import utils.BodyConsts;
import utils.BodyConsts.FemaleHair;
import utils.BodyConsts.FemaleHead;
import utils.Utils;

/**
 * Represents the body configuration of a human NPC.
 * 
 * @author Kida
 *
 */
public class BodyGenerator {

  private static final int ADD_ACCESSORY_PERCENT_CHANCE = 10; // out of 100

  private static final int ADD_HELMET_PERCENT_CHANCE = 5; // out of 100

  private static final int BALD_PERCENT_CHANCE = 15; // out of 100

  public enum Gender {
    MALE,
    FEMALE,
    LARGE_MALE,
    UNKNOWN,
  }

  private String name;

  private Human human;
  private Element attachmentList;
  private Random r;

  private boolean addJetpack;
  private boolean addHelmet;
  private boolean isBald;

  public BodyGenerator(Human h, Random r) {
    this.human = h;
    this.r = r;
    
    addJetpack = r.nextInt(100) < ADD_ACCESSORY_PERCENT_CHANCE;
    addHelmet = r.nextInt(100) < ADD_HELMET_PERCENT_CHANCE;
    isBald = r.nextInt(100) < BALD_PERCENT_CHANCE;

    Element attachmentList = new Element("AttachmentList");


    this.attachmentList = attachmentList;
  }

  public Element getAttachmentList() {
    Gender g = getGenderForSkeleton(human.getSkeleton());
    Element inherited = new Element("Attachment").setAttribute("Inheritable", "1")
        .setAttribute("Type", "CA_CDF")
        .setAttribute("AName", "Inherit_CDF")
        .setAttribute("CDF", getAttachmentsForGender(g))
        .setAttribute("Flags", "0");

    attachmentList.addContent(inherited);

    // Body
    String bodyModel = addBodyAttribute(g);
    // Accessories
    addAccessoriesAttribute(bodyModel, g);
    // Head
    String headModel = addHeadAttribute(bodyModel, g);
    // Hair
    addHairAttribute(headModel, g);

    return attachmentList;
  }

  private String addBodyAttribute(Gender g) {
    String bodyModel;
    switch (g) {
      case FEMALE:
        bodyModel =
            Utils.getRandomWeighted(BodyConsts.FEMALE_BODIES, BodyConsts.FEMALE_BODIES_WEIGHTS, r);
        break;
      case MALE:
        bodyModel =
            Utils.getRandomWeighted(BodyConsts.MALE_BODIES, BodyConsts.MALE_BODIES_WEIGHTS, r);
        break;
      case LARGE_MALE:
        bodyModel = BodyConsts.LARGE_MALE_BODIES[0];
        break;
      case UNKNOWN:
      default:
        return null;
    }

    String bodyMtl = bodyModel;

    // Override for morgan gender select mtl
    if (bodyModel.contains("morgankarlgenderselect_genfemale")) {
      bodyMtl = "morgankarl/morgan_genfemalebody01_cut_scene";
    }
    if (bodyModel.contains("morgankarlgenderselect_genmale")) {
      bodyMtl = "morgankarl/morgan_genmalebody01_cut_scene";
    }

    // Override for large male mtl
    if (g == Gender.LARGE_MALE) {
      bodyMtl = Utils.getRandom(BodyConsts.LARGE_MALE_MTLS, r);
    }

    addAttachment("body_skin", bodyModel, bodyMtl);

    // Some bodies need additional attachments
    if (bodyModel.contains("labcoat_genmalebody01")) {
      // Legs/arms for lab coat body
      addAttachment("legs", "labcoat/labcoat_genmalelegs01", "scientist/scientist_genmalebody01");
      addAttachment("hands", "labcoat/labcoat_genmalehands01", "scientist/scientist_genmalebody01");
    }
    if (bodyModel.contains("plumber/plumber_genfemalebody01")) {
      // Hat for plumber body
      addAttachment("hat_skin", BodyConsts.PLUMBER_HAT, BodyConsts.PLUMBER_HAT_MTL);
    }
    return bodyModel;
  }

  private void addAccessoriesAttribute(String bodyModel, Gender g) {
    boolean compatibleWithAccessories = BodyConsts.ACCESSORY_COMPATIBLE_BODIES.contains(bodyModel);
    if (addJetpack && compatibleWithAccessories) {
      String cdf = null;
      String mtl = null;
      switch (g) {
        case FEMALE:
          cdf = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE, r);
          mtl = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE_MTL, r);
          break;
        case MALE:
          int index = r.nextInt(BodyConsts.ACCESSORIES_MALE.length);
          cdf = BodyConsts.ACCESSORIES_MALE[index];
          mtl = BodyConsts.ACCESSORIES_MALE_MTL[index];
          break;
        case LARGE_MALE:
        case UNKNOWN:
        default:
          return;
      }
      addAttachmentVerbatim("0", "propulsion_skin", cdf + ".skin", mtl + ".mtl", "0");
    }
  }

  private String addHeadAttribute(String bodyModel, Gender g) {
    boolean hasBareHands = BodyConsts.BODIES_WITH_BARE_HANDS.contains(bodyModel);

    String headModel = getHeadModel(g, r);

    // Reroll if head and hands are mismatched
    if (hasBareHands) {
      // TODO: Add maximum counter
      while (BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS.contains(headModel)) {
        headModel = getHeadModel(g, r);
      }
    }

    String headMtl = headModel;

    // If female, chance of giving a helmet
    if (g == Gender.FEMALE && addHelmet) {
      headModel = BodyConsts.FEMALE_HEAD_HELMET;
      headMtl = BodyConsts.FEMALE_HEAD_HELMET_MTL;
      if (!addJetpack) {
        // Add a jetpack if we weren't giving one already
        addJetpack = true;
      }
      Element head =
          addAttachmentVerbatim("1", "head_skin", headModel + ".skin", headMtl + ".mtl", "0");
      head.setAttribute("SkinJointsOverrideSkeleton", "1");
      return headModel;
    }

    Element head = createAttachment("head_skin", headModel, headMtl, "0");
    head.setAttribute("SkinJointsOverrideSkeleton", "1");

    return headModel;
  }

  private void addHairAttribute(String headModel, Gender g) {
    if (BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR.contains(headModel)
        || headModel.equals(BodyConsts.FEMALE_HEAD_HELMET)) {
      return;
    }

    switch (g) {
      case FEMALE:
        generateHairForHeadFemale(headModel, r);
        break;
      case MALE:
        generateHairForHeadMale(headModel, r);
        break;
      case LARGE_MALE:
        generateHairForHeadLargeMale(headModel, r);
      default:
        break;
    }
  }

  private static String getHeadModel(Gender g, Random r) {
    switch (g) {
      case FEMALE:
        return Utils.getRandom(BodyConsts.FEMALE_HEADS, r);
      case MALE:
        return Utils.getRandom(BodyConsts.MALE_HEADS, r);
      case LARGE_MALE:
        return Utils.getRandom(BodyConsts.LARGE_MALE_HEADS, r);
      case UNKNOWN:
      default:
        return null;
    }
  }

  private Element addAttachmentVerbatim(String inheritable, String aName, String cdf, String mtl,
      String flags) {
    Element e = new Element("Attachment");
    e.setAttribute("Inheritable", inheritable);
    e.setAttribute("Type", "CA_SKIN");
    e.setAttribute("AName", aName);
    e.setAttribute("Binding", cdf);
    e.setAttribute("Material", mtl);
    e.setAttribute("Flags", flags);
    attachmentList.addContent(e);
    return e;
  }

  private Element addAttachment(String aName, String cdf, String mtl) {
    return createAttachment(aName, cdf, mtl, "0");
  }

  private Element createAttachment(String aName, String cdf, String mtl, String flags) {
    String fullCdf = BodyConsts.PREFIX + cdf + ".skin";
    String fullMtl = BodyConsts.PREFIX + mtl + ".mtl";
    Element e = new Element("Attachment");
    e.setAttribute("Inheritable", "1");
    e.setAttribute("Type", "CA_SKIN");
    e.setAttribute("AName", aName);
    e.setAttribute("Binding", fullCdf);
    e.setAttribute("Material", fullMtl);
    e.setAttribute("Flags", flags);
    attachmentList.addContent(e);
    return e;
  }

  private Element generateHairForHeadFemale(String headModel, Random r) {

    String hairModelShort = "";
    switch (headModel) {
      case "genfemale/genfemale_head01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_01, r);
        break;
      case "genfemale/genfemale_head02":
        hairModelShort =
            Utils.getRandom(BodyConsts.FEMALE_HAIRS_02, BodyConsts.FEMALE_HAIR_MORGAN, r);
        break;
      case "genfemale/genfemale_head03":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_03, r);
        break;
      case "genfemale/genfemale_head04":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_04, r);
        break;
      case "genfemale/genfemale_head05":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_05, r);
        break;
      case "genfemale/genfemale_head06":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_06, r);
        break;
      case "mikhailailyushin/mikhailailyushin_genfemalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_02, BodyConsts.FEMALE_HAIR_MORGAN,
            BodyConsts.FEMALE_HAIR_MIKA, BodyConsts.FEMALE_HAIR_SARA, r);
        break;
      case "morgankarl/morgankarl_genfemalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_02, BodyConsts.FEMALE_HAIR_MORGAN,
            BodyConsts.FEMALE_HAIR_SARA, r);
        break;
      case "saraelazar/saraelazar_genfemalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIR_MORGAN, BodyConsts.FEMALE_HAIR_MIKA,
            BodyConsts.FEMALE_HAIR_SARA, r);
        break;
      default:
        Logger.getGlobal()
            .info(name + ": Could not find hair for head " + headModel);
    }
    return addAttachment("hair_skin", hairModelShort, getColorForHairFemale(hairModelShort, r));
  }

  private String getColorForHairFemale(String hairModelShort, Random r) {
    switch (hairModelShort) {
      case "genfemale/genfemale_head01_hair01":
        return Utils.getRandom(BodyConsts.FEMALE_HAIR_COLORS_01, r);
      case "genfemale/genfemale_head02_hair02":
      case "genfemale/genfemale_head03_hair02":
      case "genfemale/genfemale_head06_hair02":
        return Utils.getRandom(BodyConsts.FEMALE_HAIR_COLORS_02, r);
      case "genfemale/genfemale_head01_hair03":
      case "genfemale/genfemale_head02_hair03":
      case "genfemale/genfemale_head03_hair03":
      case "genfemale/genfemale_head04_hair03":
      case "genfemale/genfemale_head05_hair03":
      case "genfemale/genfemale_head06_hair03":
        return Utils.getRandom(BodyConsts.FEMALE_HAIR_COLORS_03, r);
      case "genfemale/genfemale_head02_hair04":
      case "genfemale/genfemale_head03_hair04":
      case "genfemale/genfemale_head04_hair04":
        return Utils.getRandom(BodyConsts.FEMALE_HAIR_COLORS_04, r);
      case "genfemale/genfemale_head01_hair05":
      case "genfemale/genfemale_head02_hair05":
      case "genfemale/genfemale_head03_hair05":
      case "genfemale/genfemale_head04_hair05":
      case "genfemale/genfemale_head05_hair05":
      case "genfemale/genfemale_head06_hair05":
        return Utils.getRandom(BodyConsts.FEMALE_HAIR_COLORS_05, r);
      case "mikhailailyushin/mikhailailyushin_genfemalehead01_hair01":
        return BodyConsts.FEMALE_HAIR_MIKA[0];
      case "morgankarl/morgankarl_genfemalehead01_hair01":
        return BodyConsts.FEMALE_HAIR_MORGAN[0];
      case "saraelazar/saraelazar_genfemalehead01_hair01":
        return BodyConsts.FEMALE_HAIR_SARA[0];
      default:
        break;
    }
    Logger.getGlobal()
        .info(name + ": Could not find a hair color for hair model " + hairModelShort);
    return "";
  }

  private Element generateHairForHeadMale(String headModel, Random r) {
    String hairModelShort = "";
    // Give a chance of some specific types being bald
    switch (headModel) {
      case "genmale/genmale_head01":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_01, r);
        break;
      case "genmale/genmale_head02":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_02, r);
        break;
      case "genmale/genmale_head03":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_03, r);
        break;
      case "genmale/genmale_head04":
        if (isBald) {
          return null;
        }
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_04, r);
        break;
      case "genmale/genmale_head05":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_05, r);
        break;
      case "genmale/genmale_head06":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIRS_06, r);
        break;
      case "morgankarl/morgankarl_genmalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIR_MORGAN, r);
        break;
      case "dahl/dahl_genmalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIR_DAHL, BodyConsts.MALE_HAIR_MORGAN, r);
        break;
      case "drcalvino/drcalvino_genmalehead01":
        hairModelShort = BodyConsts.MALE_HAIR_CALVINO[0];
        break;
      case "drigwe/igwe_genmalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.MALE_HAIR_IGWE, BodyConsts.MALE_HAIRS_02, r);
        break;
      case "aaroningram/aaroningram_genmale_head01":
        if (isBald) {
          return null;
        }
        hairModelShort = BodyConsts.MALE_HAIR_MORGAN[0];
        break;
      case "volunteer/volunteer_genmalehead01":
        if (isBald) {
          return null;
        }
        hairModelShort = BodyConsts.MALE_HAIRS_V01[0];
        break;
      case "volunteer/volunteer_genmalehead02":
        if (isBald) {
          return null;
        }
        hairModelShort = BodyConsts.MALE_HAIRS_V02[0];
        break;
      case "volunteer/volunteer_genmalehead03":
        if (isBald) {
          return null;
        }
        hairModelShort = BodyConsts.MALE_HAIRS_V03[0];
        break;
      default:
        Logger.getGlobal()
            .info(name + ": Could not find hair for head " + headModel);
    }
    return addAttachment("hair_skin", hairModelShort, getColorForHairMale(hairModelShort, r));
  }

  private String getColorForHairMale(String hairModelShort, Random r) {
    switch (hairModelShort) {
      case "genmale/genmale_head01_hair01":
      case "genmale/genmale_head02_hair01":
      case "genmale/genmale_head05_hair01":
      case "genmale/genmale_head06_hair01":
        return Utils.getRandom(BodyConsts.MALE_HAIR_COLORS_01, r);
      case "genmale/genmale_head01_hair03":
      case "genmale/genmale_head02_hair03":
      case "genmale/genmale_head03_hair03":
        return Utils.getRandom(BodyConsts.MALE_HAIR_COLORS_03, r);
      case "genmale/genmale_head04_hair04":
        return Utils.getRandom(BodyConsts.MALE_HAIR_COLORS_04, r);
      case "genmale/genmale_head01_hair05":
      case "genmale/genmale_head02_hair05":
      case "genmale/genmale_head03_hair05":
      case "genmale/genmale_head05_hair05":
      case "genmale/genmale_head06_hair05":
        return Utils.getRandom(BodyConsts.MALE_HAIR_COLORS_05, r);
      case "genmale/genmale_head03_hair06":
      case "genmale/genmale_head05_hair06":
      case "genmale/genmale_head06_hair06":
        return Utils.getRandom(BodyConsts.MALE_HAIR_COLORS_06, r);
      case "volunteer/volunteer_genmalehead01_hair01":
      case "volunteer/volunteer_genmalehead02_hair01":
      case "volunteer/volunteer_genmalehead03_hair01":
        return Utils.getRandom(BodyConsts.MALE_HAIR_VOLUNTEER, r);
      case "dahl/dahl_genmalehead01_hair01":
        return BodyConsts.MALE_HAIR_DAHL[0];
      case "morgankarl/morgankarl_genmalehead01_hair01":
        return BodyConsts.MALE_HAIR_MORGAN[0];
      case "drigwe/igwe_genmalehead01_hair01":
        return BodyConsts.MALE_HAIR_IGWE[0];
      case "drcalvino/drcalvino_genmalehead01_hair01":
        return BodyConsts.MALE_HAIR_CALVINO_COLOR[0];
      default:
        Logger.getGlobal()
            .info(name + ": Could not find a hair color for hair model " + hairModelShort);
    }
    return "";
  }

  private Element generateHairForHeadLargeMale(String headModel, Random r) {
    if (isBald) {
      return null;
    }
    String hairModel = "";
    switch (headModel) {
      case "Luka/Luka_LargeMaleHead01":
        hairModel = "Luka/Luka_LargeMaleHead01_Hair01";
        break;
      case "alexlikarl/alexlikarl_largemalehead01":
      case "alexlikarl/alexlikarl_largemalehead01_variant":
        hairModel = Utils.getRandom(BodyConsts.ALEX_HAIRS, r);
        break;
    }
    return addAttachment("hair_skin", hairModel, hairModel);
  }

  private Gender getGenderForSkeleton(String skeleton) {
    switch (skeleton.toLowerCase()) {
      case BodyConsts.LARGE_MALE_BODY_TYPE:
        return Gender.LARGE_MALE;
      case BodyConsts.MALE_BODY_TYPE:
        return Gender.MALE;
      case BodyConsts.FEMALE_BODY_TYPE:
        return Gender.FEMALE;
      default:
        return Gender.UNKNOWN;
    }
  }

  private String getAttachmentsForGender(Gender g) {
    switch (g) {
      case LARGE_MALE:
        return BodyConsts.LARGE_MALE_ATTACHMENTS;
      case MALE:
        return BodyConsts.MALE_ATTACHMENTS;
      case FEMALE:
        return BodyConsts.FEMALE_ATTACHMENTS;
      default:
        return null;
    }
  }
}
