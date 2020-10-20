package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.jdom2.Element;

/**
 * Represents the body configuration of a human NPC.
 * 
 * @author Kida
 *
 */
public class BodyConfig {

  private static final int ADD_ACCESSORY_PERCENT_CHANCE = 10; // out of 100

  private static final int ADD_HELMET_PERCENT_CHANCE = 5; // out of 100

  private static final int BALD_PERCENT_CHANCE = 15; // out of 100

  public enum Gender {
    MALE, FEMALE, UNKNOWN,
  }

  private String name;

  private String bodyModel;
  private String headModel;
  private Gender g;
  private Element attachmentList;
  private Random r;
  private List<Element> children;

  private boolean addJetpack;
  private boolean addHelmet;
  private boolean isBald;

  private List<Element> toRemove;
  private List<Element> toAdd;

  public BodyConfig(String name, Gender g, Element attachmentList, Random r) {
    this.name = name;
    this.g = g;
    this.attachmentList = attachmentList;
    this.r = r;
    this.children = attachmentList.getChildren("Attachment");

    addJetpack = r.nextInt(100) < ADD_ACCESSORY_PERCENT_CHANCE;
    addHelmet = r.nextInt(100) < ADD_HELMET_PERCENT_CHANCE;
    isBald = r.nextInt(100) < BALD_PERCENT_CHANCE;

    toRemove = new ArrayList<>();
    toAdd = new ArrayList<>();
  }

  public void generateAttachmentsForGender() {
    for (Element child : children) {
      String type = child.getAttributeValue("Type");
      // Remove all "skin" type attachments, unless they're a husk head
      if (type.equals("CA_SKIN")) {
        boolean isHuskHead = child.getAttributeValue("AName")
                                  .contains("husk")
            || child.getAttributeValue("Binding")
                    .toLowerCase()
                    .contains("husk");
        if (isHuskHead) {
          continue;
        } else {
          toRemove.add(child);
        }
      }
      // Process and decide whether to re-add
      String aName = child.getAttributeValue("AName");
      switch (aName) {
        case "body_skin":
        case "body":
          addBodyAttribute(child);
          break;
        case "head_skin":
          addHeadAndHair(child);
          break;
      }
    }

    // Accessories
    addAccessories();

    for (Element e : toRemove) {
      attachmentList.removeContent(e);
    }
    for (Element e : toAdd) {
      attachmentList.addContent(e);
    }
  }

  private void addBodyAttribute(Element originalBody) {
    switch (g) {
      case FEMALE:
        bodyModel = Utils.getRandomWeighted(BodyConsts.FEMALE_BODIES, BodyConsts.FEMALE_BODIES_WEIGHTS, r);
        break;
      case MALE:
        bodyModel = Utils.getRandomWeighted(BodyConsts.MALE_BODIES, BodyConsts.MALE_BODIES_WEIGHTS, r);
        break;
      case UNKNOWN:
      default:
        return;
    }

    String bodyMtl = bodyModel;

    // Override for morgan gender select mtl
    if (bodyModel.contains("morgankarlgenderselect_genfemale")) {
      bodyMtl = "morgankarl/morgan_genfemalebody01_cut_scene";
    }
    if (bodyModel.contains("morgankarlgenderselect_genmale")) {
      bodyMtl = "morgankarl/morgan_genmalebody01_cut_scene";
    }

    toAdd.add(createAttachment("body_skin", bodyModel, bodyMtl));

    // Some bodies need additional attachments
    if (bodyModel.contains("labcoat_genmalebody01")) {
      // Legs/arms for lab coat body
      toAdd.add(createAttachment("legs", "labcoat/labcoat_genmalelegs01", "scientist/scientist_genmalebody01"));
      toAdd.add(createAttachment("hands", "labcoat/labcoat_genmalehands01", "scientist/scientist_genmalebody01"));
    }
    if (bodyModel.contains("plumber/plumber_genfemalebody01")) {
      // Hat for plumber body
      toAdd.add(createAttachment("hat_skin", BodyConsts.PLUMBER_HAT, BodyConsts.PLUMBER_HAT_MTL));
    }
  }

  private void addAccessories() {
    boolean compatibleWithAccessories = Arrays.binarySearch(BodyConsts.ACCESSORY_COMPATIBLE_BODIES, bodyModel) >= 0;
    if (addJetpack && compatibleWithAccessories) {
      String cdf = null;
      String mtl = null;
      switch (g) {
        case FEMALE:
          cdf = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE, r);
          mtl = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE_MTL, r);
          break;
        case MALE:
          cdf = Utils.getRandom(BodyConsts.ACCESSORIES_MALE, r);
          mtl = Utils.getRandom(BodyConsts.ACCESSORIES_MALE_MTL, r);
          break;
        case UNKNOWN:
        default:
          return;
      }
      toAdd.add(createAttachmentVerbatim("0", "propulsion_skin", cdf + ".skin", mtl + ".mtl", "0"));
    }
  }

  private void addHeadAndHair(Element originalHead) {
    // Return husked head unchanged
    if (originalHead.getAttributeValue("Binding")
                    .toLowerCase()
                    .contains("husk")) {
      return;
    }

    boolean hasBareHands = Arrays.binarySearch(BodyConsts.BODIES_WITH_BARE_HANDS, bodyModel) >= 0;

    headModel = getHeadModel(g, r);

    // Reroll if head and hands are mismatched
    if (hasBareHands) {
      while (Arrays.binarySearch(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS, headModel) >= 0) {
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
      Element head = createAttachmentVerbatim("1", "head_skin", headModel + ".skin", headMtl + ".mtl", "0");
      head.setAttribute("SkinJointsOverrideSkeleton", "1");
      toAdd.add(head);
      return;
    }

    Element head = createAttachment("head_skin", headModel, headMtl, "0");
    head.setAttribute("SkinJointsOverrideSkeleton", "1");
    toAdd.add(head);

    addHairForHead();
  }

  private void addHairForHead() {
    if (Arrays.binarySearch(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR, headModel) >= 0
        || headModel.equals(BodyConsts.FEMALE_HEAD_HELMET)) {
      return;
    }

    switch (g) {
      case FEMALE:
        toAdd.add(generateHairForHeadFemale(headModel, r));
        break;
      case MALE:
        Element maleHairOrNull = generateHairForHeadMale(headModel, r);
        if (maleHairOrNull != null) {
          toAdd.add(maleHairOrNull);
        }
        break;
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
      case UNKNOWN:
      default:
        return null;
    }
  }

  private static Element createAttachmentVerbatim(String inheritable, String aName, String cdf, String mtl,
      String flags) {
    Element e = new Element("Attachment");
    e.setAttribute("Inheritable", inheritable);
    e.setAttribute("Type", "CA_SKIN");
    e.setAttribute("AName", aName);
    e.setAttribute("Binding", cdf);
    e.setAttribute("Material", mtl);
    e.setAttribute("Flags", flags);
    return e;
  }

  private static Element createAttachment(String aName, String cdf, String mtl) {
    return createAttachment(aName, cdf, mtl, "0");
  }

  private static Element createAttachment(String aName, String cdf, String mtl, String flags) {
    String fullCdf = BodyConsts.PREFIX + cdf + ".skin";
    String fullMtl = BodyConsts.PREFIX + mtl + ".mtl";
    Element e = new Element("Attachment");
    e.setAttribute("Inheritable", "1");
    e.setAttribute("Type", "CA_SKIN");
    e.setAttribute("AName", aName);
    e.setAttribute("Binding", fullCdf);
    e.setAttribute("Material", fullMtl);
    e.setAttribute("Flags", flags);
    return e;
  }

  private Element generateHairForHeadFemale(String headModel, Random r) {

    String hairModelShort = "";
    switch (headModel) {
      case "genfemale/genfemale_head01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_01, r);
        break;
      case "genfemale/genfemale_head02":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIRS_02, BodyConsts.FEMALE_HAIR_MORGAN, r);
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
            BodyConsts.FEMALE_HAIR_MIKA, BodyConsts.FEMALE_HAIR_SARA, r);
        break;
      case "saraelazar/saraelazar_genfemalehead01":
        hairModelShort = Utils.getRandom(BodyConsts.FEMALE_HAIR_MORGAN, BodyConsts.FEMALE_HAIR_MIKA,
            BodyConsts.FEMALE_HAIR_SARA, r);
        break;
      default:
        Logger.getGlobal()
              .info(name + ": Could not find hair for head " + headModel);
    }
    return createAttachment("hair_skin", hairModelShort, getColorForHairFemale(hairModelShort, r));
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
    return createAttachment("hair_skin", hairModelShort, getColorForHairMale(hairModelShort, r));
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

}
