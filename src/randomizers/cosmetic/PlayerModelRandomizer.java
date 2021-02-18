package randomizers.cosmetic;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class PlayerModelRandomizer extends BaseRandomizer {

  private static final String OUT_DIR = "objects/characters/player/";

  private static final ImmutableList<String> PLAYER_FILES = ImmutableList.of("player.cdf");

  private static final ImmutableList<String> PAJAMA_FILES = ImmutableList.of();

  private static final ImmutableSet<String> PLAYER_TAGS = ImmutableSet.of("arms", "legs");
  private static final ImmutableSet<String> PAJAMA_TAGS = ImmutableSet.of("pajama_arms", "pajama_legs");

  private static enum BodyType {
    Volunteer, Security, Engineer, Scientist, Corporate, LabCoat, Dahl, Etheric, Cosmonaut, Phantom, PajamaMale,
    PajamaFemale, GlovelessMale
  }

  private static final ImmutableList<BodyType> PAJAMA_COMPATIBLE_BODIES = ImmutableList.of(BodyType.Volunteer,
      BodyType.Security, BodyType.Engineer, BodyType.Scientist, BodyType.Corporate, BodyType.Dahl);

  private static final ImmutableMap<BodyType, String> BODY_SKIN = new ImmutableMap.Builder<BodyType, String>().put(
      BodyType.Volunteer, "Objects/characters/Humans/Volunteer/Volunteer_GenMaleBody01.skin")
      .put(BodyType.Security, "Objects/characters/Humans/Security/Security_GenMaleBody01.skin")
      .put(BodyType.Engineer, "Objects/characters/Humans/Mechanic/Mechanic_GenMaleBody01.skin")
      .put(BodyType.Scientist, "Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.skin")
      .put(BodyType.Corporate, "Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.skin")
      .put(BodyType.LabCoat, "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.skin")
      .put(BodyType.Dahl, "Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.skin")
      .put(BodyType.Etheric, "objects/characters/humans/genmale/genmale_mesh.skin")
      .put(BodyType.GlovelessMale, "objects/characters/humans/morgankarl/morgankarlgenderselect_genmale.skin")
      .build();

  private static final ImmutableMap<BodyType, String> BODY_MTL = new ImmutableMap.Builder<BodyType, String>().put(
      BodyType.Volunteer, "Objects/characters/Humans/Volunteer/Volunteer_GenMaleBody01.mtl")
      .put(BodyType.Security, "Objects/characters/Humans/Security/Security_GenMaleBody01.mtl")
      .put(BodyType.Engineer, "Objects/characters/Humans/Mechanic/Mechanic_GenMaleBody01.mtl")
      .put(BodyType.Scientist, "Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.mtl")
      .put(BodyType.Corporate, "Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.mtl")
      .put(BodyType.LabCoat, "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.mtl")
      .put(BodyType.Dahl, "Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.mtl")
      .put(BodyType.Etheric, "Objects/characters/Player/EtherDuplicate_00.mtl")
      .put(BodyType.GlovelessMale, "objects/characters/humans/morgankarl/morgan_genmalebody01_cut_scene.mtl")
      .build();

  private static final String LAB_COAT_HANDS_SKIN = "objects/characters/humans/labcoat/labcoat_genmalehands01.skin";
  private static final String LAB_COAT_HANDS_MTL = "objects/characters/humans/scientist/scientist_genmalebody01.mtl";
  private static final String LAB_COAT_LEGS_SKIN = "objects/characters/humans/labcoat/labcoat_genmalelegs01.skin";
  private static final String LAB_COAT_LEGS_MTL = "objects/characters/humans/scientist/scientist_genmalebody01.mtl";

  private static final String PAJAMA_MALE_ARMS_SKIN = "objects/characters/player/male/player1p_male02_arms.skin";
  private static final String PAJAMA_MALE_ARMS_MTL = "objects/characters/player/male/player1p_male02_arms.mtl";
  private static final String PAJAMA_MALE_LEGS_SKIN = "objects/characters/player/male/player1p_male02_pajamas.skin";
  private static final String PAJAMA_MALE_LEGS_MTL = "objects/characters/player/male/player1p_male02_pajamas.mtl";

  private static final String PAJAMA_FEMALE_ARMS_SKIN = "objects/characters/player/female/player1p_female02_arms.skin";
  private static final String PAJAMA_FEMALE_ARMS_MTL = "objects/characters/player/female/player1p_female02_arms.mtl";
  private static final String PAJAMA_FEMALE_LEGS_SKIN = "objects/characters/player/female/player1p_female02_pajamas.skin";
  private static final String PAJAMA_FEMALE_LEGS_MTL = "objects/characters/player/female/player1p_female02_pajamas.mtl";

  private static final String COSMONAUT_ARMS_SKIN = "objects/characters/player/male/cosmonaut_genmalebody01.skin";
  private static final String COSMONAUT_ARMS_MTL = "objects/characters/player/male/cosmonaut1p_male01.mtl";
  private static final String GENERIC_SUIT_SKIN = "objects/characters/player/male/player1p_male01_legs.skin";
  private static final String GENERIC_SUIT_MTL = "objects/characters/player/male/player1p_male01_legs.mtl";

  private static final String PHANTOM_ARMS_SKIN = "objects/characters/player/male/player1p_male01_armsalien.skin";
  private static final String PHANTOM_ARMS_MTL = "objects/characters/player/male/player1p_male02_arms_alien_cine_02.mtl";

  public PlayerModelRandomizer(SettingsJson s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    for (String file : PLAYER_FILES) {
      String out = OUT_DIR + "/" + file;
      randomizeCdf(ZipHelper.PLAYER_DIR + "/" + file, out, false);
    }

    for (String file : PAJAMA_FILES) {
      String out = OUT_DIR + "/" + file;
      randomizeCdf(ZipHelper.PLAYER_DIR + "/" + file, out, true);
    }
  }

  private void randomizeCdf(String in, String out, boolean isPajamas) {
    try {
      Document document = zipHelper.getDocument(in);
      Element root = document.getRootElement();
      Element attachments = root.getChild("AttachmentList");

      // Replace arms and legs with a random body attachment
      BodyType bodyType = getRandomBodyType(r);
      logger.info("Setting body type " + bodyType);

      BodyType pajamaBodyType = null;
      if (isPajamas) {
        pajamaBodyType = getRandomPajamaBodyType(r);
      }

      Set<Element> toRemove = Sets.newHashSet();
      for (Element attachment : attachments.getChildren()) {
        if (PLAYER_TAGS.contains(attachment.getAttributeValue("AName"))) {
          toRemove.add(attachment);
        }
        if (isPajamas && PAJAMA_TAGS.contains(attachment.getAttributeValue("AName"))) {
          toRemove.add(attachment);
        }
      }

      for (Element e : toRemove) {
        attachments.removeContent(e);
      }

      List<Element> elements = getElementsForBodyType(bodyType, false);
      attachments.addContent(elements);

      if (isPajamas) {
        List<Element> pajamaElements = getElementsForBodyType(pajamaBodyType, true);
        attachments.addContent(pajamaElements);
      }

      zipHelper.copyToPatch(document, out);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private List<Element> getElementsForBodyType(BodyType b, boolean isPajamas) {
    String armName = isPajamas ? "pajama_arms" : "arms";
    String legName = isPajamas ? "pajama_legs" : "legs";

    List<Element> elements = Lists.newArrayList();
    switch (b) {
      case PajamaFemale:
        Element arms = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", armName)
            .setAttribute("Binding", PAJAMA_FEMALE_ARMS_SKIN)
            .setAttribute("Material", PAJAMA_FEMALE_ARMS_MTL)
            .setAttribute("Flags", "0");
        Element legs = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", legName)
            .setAttribute("Binding", PAJAMA_FEMALE_LEGS_SKIN)
            .setAttribute("Material", PAJAMA_FEMALE_LEGS_MTL)
            .setAttribute("Flags", "0");
        elements.add(arms);
        elements.add(legs);
        break;
      case PajamaMale:
        arms = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", armName)
            .setAttribute("Binding", PAJAMA_MALE_ARMS_SKIN)
            .setAttribute("Material", PAJAMA_MALE_ARMS_MTL)
            .setAttribute("Flags", "0");
        legs = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", legName)
            .setAttribute("Binding", PAJAMA_MALE_LEGS_SKIN)
            .setAttribute("Material", PAJAMA_MALE_LEGS_MTL)
            .setAttribute("Flags", "0");
        elements.add(arms);
        elements.add(legs);
        break;
      case Cosmonaut:
        arms = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", armName)
            .setAttribute("Binding", COSMONAUT_ARMS_SKIN)
            .setAttribute("Material", COSMONAUT_ARMS_MTL)
            .setAttribute("Flags", "0");
        legs = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", legName)
            .setAttribute("Binding", GENERIC_SUIT_SKIN)
            .setAttribute("Material", GENERIC_SUIT_MTL)
            .setAttribute("Flags", "0");
        elements.add(arms);
        elements.add(legs);
        break;
      case Phantom:
        arms = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", armName)
            .setAttribute("Binding", PHANTOM_ARMS_SKIN)
            .setAttribute("Material", PHANTOM_ARMS_MTL)
            .setAttribute("Flags", "0");
        legs = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", legName)
            .setAttribute("Binding", GENERIC_SUIT_SKIN)
            .setAttribute("Material", GENERIC_SUIT_MTL)
            .setAttribute("Flags", "0");
        elements.add(arms);
        elements.add(legs);
        break;
      default:
        String bodyName = isPajamas ? "pajama_legs" : "body";

        String skin = BODY_SKIN.get(b);
        String mtl = BODY_MTL.get(b);

        Element body = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", bodyName)
            .setAttribute("Binding", skin)
            .setAttribute("Material", mtl)
            .setAttribute("Flags", "0");
        elements.add(body);

        if (b == BodyType.LabCoat) {
          Element hands = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "hands")
              .setAttribute("Binding", LAB_COAT_HANDS_SKIN)
              .setAttribute("Material", LAB_COAT_HANDS_MTL)
              .setAttribute("Flags", "0");
          legs = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "legs")
              .setAttribute("Binding", LAB_COAT_LEGS_SKIN)
              .setAttribute("Material", LAB_COAT_LEGS_MTL)
              .setAttribute("Flags", "0");
          elements.add(hands);
          elements.add(legs);
        }
        break;
    }
    return elements;
  }

  private BodyType getRandomBodyType(Random r) {
    int index = r.nextInt(BodyType.values().length);
    return BodyType.values()[index];
  }

  private BodyType getRandomPajamaBodyType(Random r) {
    int index = r.nextInt(PAJAMA_COMPATIBLE_BODIES.size());
    return PAJAMA_COMPATIBLE_BODIES.get(index);
  }
}
