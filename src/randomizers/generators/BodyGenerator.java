package randomizers.generators;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableCollection;

import proto.Body.Human;
import utils.BodyConsts;
import utils.BodyConsts.BodyType;
import utils.BodyConsts.FemaleBody;
import utils.BodyConsts.FemaleHair;
import utils.BodyConsts.FemaleHairColor;
import utils.BodyConsts.FemaleHairType;
import utils.BodyConsts.FemaleHead;
import utils.BodyConsts.HairColorType;
import utils.BodyConsts.HairType;
import utils.BodyConsts.HeadType;
import utils.BodyConsts.LargeMaleHair;
import utils.BodyConsts.LargeMaleHead;
import utils.BodyConsts.MaleBody;
import utils.BodyConsts.MaleHair;
import utils.BodyConsts.MaleHairColor;
import utils.BodyConsts.MaleHairType;
import utils.BodyConsts.MaleHead;
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
    MALE, FEMALE, LARGE_MALE, UNKNOWN,
  }

  private Human human;
  private Element attachmentList;
  private Random r;

  private boolean addJetpack;
  private boolean addHelmet;
  private boolean isBald;
  
  private BodyType body;
  private HeadType head;
  private HairType hair;
  private HairColorType color;

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
    BodyType bodyModel = addBodyAttribute(g);
    // Accessories
    addAccessoriesAttribute(bodyModel, g);
    // Head
    HeadType headModel = addHeadAttribute(bodyModel, g);
    // Hair
    HairType hairModel = addHairAttribute(headModel, g);
    
    body = bodyModel;
    head = headModel;
    hair = hairModel;

    return attachmentList;
  }

  private BodyType addBodyAttribute(Gender g) {
    BodyType bodyType;
    String bodyModel = "";
    String bodyMtl = "";
    switch (g) {
      case FEMALE:
        bodyType = Utils.getRandomWeighted(BodyConsts.SUPPORTED_FEMALE_BODIES, BodyConsts.FEMALE_BODIES_WEIGHTS, r);
        bodyModel = BodyConsts.FEMALE_BODIES_MAP.get(bodyType);
        if (bodyType == FemaleBody.GLOVELESS) {
          bodyMtl = BodyConsts.MORGAN_FEMALE_GLOVELESS_MTL;
        } else {
          bodyMtl = bodyModel;
        }
        break;
      case MALE:
      case UNKNOWN:
      default:
        bodyType = Utils.getRandomWeighted(BodyConsts.SUPPORTED_MALE_BODIES, BodyConsts.MALE_BODIES_WEIGHTS, r);
        bodyModel = BodyConsts.MALE_BODIES_MAP.get(bodyType);
        if (bodyType == MaleBody.GLOVELESS) {
          bodyMtl = BodyConsts.MORGAN_MALE_GLOVELESS_MTL;
        } else {
          bodyMtl = bodyModel;
        }
        break;
      case LARGE_MALE:
        // There's only one body model for large male
        bodyType = Utils.getRandom(BodyConsts.SUPPORTED_LARGE_MALE_BODIES, r);
        bodyModel = BodyConsts.LARGE_MALE_BODY_MODEL;
        bodyMtl = Utils.getRandom(BodyConsts.LARGE_MALE_MTLS, r);
        break;
    }

    addAttachment("body_skin", bodyModel, bodyMtl);

    // Some bodies need additional attachments
    if (bodyType == MaleBody.LABCOAT) {
      // Legs/arms for lab coat body
      addAttachment("legs", BodyConsts.MALE_LABCOAT_LEGS_MODEL, BodyConsts.MALE_LABCOAT_LIMBS_MTL);
      addAttachment("hands", BodyConsts.MALE_LABCOAT_HANDS_MODEL, BodyConsts.MALE_LABCOAT_LIMBS_MTL);
    }

    if (bodyType == FemaleBody.PLUMBER) {
      // Hat for plumber body
      // TODO: Make this hair dependent, not body dependent
      addAttachment("hat_skin", BodyConsts.PLUMBER_HAT, BodyConsts.PLUMBER_HAT_MTL);
    }

    return bodyType;
  }

  private void addAccessoriesAttribute(BodyType bodyModel, Gender g) {
    if (addJetpack) {
      if (g == Gender.FEMALE && BodyConsts.ACCESSORY_COMPATIBLE_FEMALE_BODIES.contains(bodyModel)) {
        String cdf = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE, r);
        String mtl = Utils.getRandom(BodyConsts.ACCESSORIES_FEMALE_MTL, r);
        addAttachmentVerbatim("0", "propulsion_skin", cdf + ".skin", mtl + ".mtl", "0");
      }

      if (g == Gender.MALE && BodyConsts.ACCESSORY_COMPATIBLE_MALE_BODIES.contains(bodyModel)) {
        int index = r.nextInt(BodyConsts.ACCESSORIES_MALE.length);
        String cdf = BodyConsts.ACCESSORIES_MALE[index];
        String mtl = BodyConsts.ACCESSORIES_MALE_MTL[index];
        addAttachmentVerbatim("0", "propulsion_skin", cdf + ".skin", mtl + ".mtl", "0");
      }
    }
  }

  private HeadType addHeadAttribute(BodyType bodyModel, Gender g) {
    HeadType headType = getHeadModel(g, r);

    // Re-roll if head and hands are mismatched
    if (BodyConsts.BODIES_WITH_BARE_HANDS.contains(bodyModel)) {
      int numAttempts = 0;
      while (BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS.contains(headType) && numAttempts++ < 10) {
        headType = getHeadModel(g, r);
      }
    }
    
    String headModel = "";
    String headMtl = "";
    switch (g) {
      case FEMALE:
        // If female, chance of giving a helmet
        if (addHelmet) {
          headModel = BodyConsts.FEMALE_HEAD_HELMET;
          headMtl = BodyConsts.FEMALE_HEAD_HELMET_MTL;
          if (!addJetpack) {
            // Add a jetpack if we weren't giving one already
            addJetpack = true;
          }
          Element head = addAttachmentVerbatim("1", "head_skin", headModel + ".skin", headMtl + ".mtl", "0");
          head.setAttribute("SkinJointsOverrideSkeleton", "1");
          return FemaleHead.HELMET;
        } else {
          headModel = BodyConsts.FEMALE_HEADS_MAP.get(headType);
          headMtl = headModel;          
        }
        break;
      case MALE:
      case UNKNOWN:
      default:
        headModel = BodyConsts.MALE_HEADS_MAP.get(headType);
        headMtl = headModel;
        break;
      case LARGE_MALE:
        headModel = BodyConsts.LARGE_MALE_HEADS_MAP.get(headType);
        headMtl = headModel;
        break;
    }

    Element head = createAttachment("head_skin", headModel, headMtl, "0");
    head.setAttribute("SkinJointsOverrideSkeleton", "1");

    return headType;
  }
  
  private static HeadType getHeadModel(Gender g, Random r) {
    switch (g) {
      case FEMALE:
        return Utils.getRandom(BodyConsts.SUPPORTED_FEMALE_HEADS, r);
      case UNKNOWN:
      default:
      case MALE:
        return Utils.getRandom(BodyConsts.SUPPORTED_MALE_HEADS, r);
      case LARGE_MALE:
        return Utils.getRandom(BodyConsts.SUPPORTED_LARGE_MALE_HEADS, r);
    }
  }

  private HairType addHairAttribute(HeadType headModel, Gender g) {
    if (BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR.contains(headModel)) {
      return null;
    }

    switch (g) {
      case FEMALE:
        FemaleHead fh = (FemaleHead) headModel;
        return generateHairForHeadFemale(fh, r);
      case MALE:
        MaleHead mh = (MaleHead) headModel;
        return generateHairForHeadMale(mh, r);
      case LARGE_MALE:
        LargeMaleHead lmh = (LargeMaleHead) headModel;
        return generateHairForHeadLargeMale(lmh, r);
      default:
        return null;
    }
  }

  private Element addAttachmentVerbatim(String inheritable, String aName, String cdf, String mtl, String flags) {
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

  private HairType generateHairForHeadFemale(FemaleHead headModel, Random r) {
    ImmutableCollection<FemaleHair> compatibleHairs = BodyConsts.FEMALE_HEAD_HAIR_COMPATIBILITY.get(headModel);
    FemaleHair hairType = Utils.getRandom(compatibleHairs, r);
    String hairModel = BodyConsts.FEMALE_HAIRS_MAP.get(hairType);
    FemaleHairType genericHairType = BodyConsts.FEMALE_HAIR_TO_HAIR_TYPE.get(hairType); 
    ImmutableCollection<FemaleHairColor> compatibleColors = BodyConsts.FEMALE_HAIR_COLOR_COMPATIBILITY.get(genericHairType);
    FemaleHairColor hairColor = Utils.getRandom(compatibleColors, r);
    String hairColorMtl = BodyConsts.FEMALE_HAIR_COLORS_MAP.get(hairColor);
    addAttachment("hair_skin", hairModel, hairColorMtl);
    
    color = hairColor;
    
    return hairType;
  }
  
  private HairType generateHairForHeadMale(MaleHead headModel, Random r) {
    if (isBald && BodyConsts.HEADS_THAT_CAN_BE_BALD.contains(headModel)) {
      return null;
    }
    
    ImmutableCollection<MaleHair> compatibleHairs = BodyConsts.MALE_HEAD_HAIR_COMPATIBILITY.get(headModel);
    MaleHair hairType = Utils.getRandom(compatibleHairs, r);
    String hairModel = BodyConsts.MALE_HAIRS_MAP.get(hairType);
    MaleHairType genericHairType = BodyConsts.MALE_HAIR_TO_HAIR_TYPE.get(hairType); 
    ImmutableCollection<MaleHairColor> compatibleColors = BodyConsts.MALE_HAIR_COLOR_COMPATIBILITY.get(genericHairType);
    MaleHairColor hairColor = Utils.getRandom(compatibleColors, r);
    String hairColorMtl = BodyConsts.MALE_HAIR_COLORS_MAP.get(hairColor);
    addAttachment("hair_skin", hairModel, hairColorMtl);
    
    color = hairColor;
    
    return hairType;
  }

  private HairType generateHairForHeadLargeMale(LargeMaleHead headModel, Random r) {
    if (isBald) {
      return null;
    }
    
    LargeMaleHair largeMaleHair;
    
    switch (headModel) {
      case LUKA:
        largeMaleHair = LargeMaleHair.LUKA;
        break;
      default:
        largeMaleHair = Utils.getRandom(BodyConsts.SUPPORTED_ALEX_HAIRS, r);
        break;
    }
    
    String hairModel = BodyConsts.LARGE_MALE_HAIRS_MAP.get(largeMaleHair);
    
    addAttachment("hair_skin", hairModel, hairModel);
    return largeMaleHair;
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
      default:
      case MALE:
        return BodyConsts.MALE_ATTACHMENTS;
      case FEMALE:
        return BodyConsts.FEMALE_ATTACHMENTS;
    }
  }
  
  @Override
  public String toString() {
    return String.format("Body: %s, Head: %s, Hair: %s, Color: %s", body, head, hair, color);
  }
  
  public static void main(String[] args) {
    Human h = Human.newBuilder()
        .setName("foo")
        .setSkeleton(BodyConsts.LARGE_MALE_BODY_TYPE)  
        .build();
    
    Random r = new Random(0L);
    
    for (int i = 0; i < 100; i++) {
      BodyGenerator bg = new BodyGenerator(h, r);
      Element e = bg.getAttachmentList();
      System.out.println(bg);   
      System.out.println(e.getText());
    }
  }
}
