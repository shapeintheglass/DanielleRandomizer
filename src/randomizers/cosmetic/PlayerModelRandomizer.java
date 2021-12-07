package randomizers.cosmetic;

import java.io.IOException;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.Utils;
import utils.ZipHelper;

public class PlayerModelRandomizer extends BaseRandomizer {

  private static final ImmutableList<String> PLAYER_FILES = ImmutableList.of(ZipHelper.PLAYER_CDF,
      ZipHelper.PLAYER_FEMALE_PAJAMA_CDF, ZipHelper.PLAYER_MALE_PAJAMA_CDF,
      ZipHelper.PLAYER_ALIEN_CDF, ZipHelper.PLAYER_FEMALE_CDF);

  private static final ImmutableSet<String> ARMS =
      ImmutableSet.of("arms", "pajama_arms", "base_arms", "player_CDF");
  private static final ImmutableSet<String> LEGS = ImmutableSet.of("legs", "pajama_legs");

  private static enum BodyType {
    Volunteer,
    Security,
    Engineer,
    Scientist,
    Corporate,
    LabCoat,
    Dahl,
    Etheric,
    Cosmonaut,
    PajamaMale,
    PajamaFemale,
    GlovelessMale,
    Typhon
  }

  private static final ImmutableList<BodyType> BODY_TYPES_FOR_RANDOM =
      ImmutableList.of(BodyType.Volunteer, BodyType.Security, BodyType.Engineer, BodyType.Scientist,
          BodyType.Corporate);

  private static final ImmutableMap<BodyType, String> BODY_SKIN =
      new ImmutableMap.Builder<BodyType, String>()
          .put(BodyType.Volunteer,
              "Objects/characters/Humans/Volunteer/Volunteer_GenMaleBody01.skin")
          .put(BodyType.Security, "Objects/characters/Humans/Security/Security_GenMaleBody01.skin")
          .put(BodyType.Engineer, "Objects/characters/Humans/Mechanic/Mechanic_GenMaleBody01.skin")
          .put(BodyType.Scientist,
              "Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.skin")
          .put(BodyType.Corporate,
              "Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.skin")
          .put(BodyType.LabCoat, "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.skin")
          .put(BodyType.Dahl, "Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.skin")
          .put(BodyType.Etheric, "objects/characters/humans/genmale/genmale_mesh.skin")
          .put(BodyType.Typhon, "objects/characters/humans/genmale/genmale_mesh.skin")
          .put(BodyType.GlovelessMale,
              "objects/characters/humans/morgankarl/morgankarlgenderselect_genmale.skin")
          .build();

  private static final ImmutableMap<BodyType, String> BODY_MTL =
      new ImmutableMap.Builder<BodyType, String>()
          .put(BodyType.Volunteer,
              "Objects/characters/Humans/Volunteer/Volunteer_GenMaleBody01.mtl")
          .put(BodyType.Security, "Objects/characters/Humans/Security/Security_GenMaleBody01.mtl")
          .put(BodyType.Engineer, "Objects/characters/Humans/Mechanic/Mechanic_GenMaleBody01.mtl")
          .put(BodyType.Scientist,
              "Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.mtl")
          .put(BodyType.Corporate,
              "Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.mtl")
          .put(BodyType.LabCoat, "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.mtl")
          .put(BodyType.Dahl, "Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.mtl")
          .put(BodyType.Etheric, "Objects/characters/Player/EtherDuplicate_00.mtl")
          .put(BodyType.GlovelessMale,
              "objects/characters/humans/morgankarl/morgan_genmalebody01_cut_scene.mtl")
          .put(BodyType.Typhon, "Objects/characters/Player/etherskin_inner")
          .build();

  // TODO: Fix the labcoat skin (this has THREE parts- body, hands, legs)
  private static final String LAB_COAT_HANDS_SKIN =
      "objects/characters/humans/labcoat/labcoat_genmalehands01.skin";
  private static final String LAB_COAT_HANDS_MTL =
      "objects/characters/humans/scientist/scientist_genmalebody01.mtl";
  private static final String LAB_COAT_LEGS_SKIN =
      "objects/characters/humans/labcoat/labcoat_genmalelegs01.skin";
  private static final String LAB_COAT_LEGS_MTL =
      "objects/characters/humans/scientist/scientist_genmalebody01.mtl";
  private static final String LAB_COAT_BODY_SKIN =
      "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.skin";
  private static final String LAB_COAT_BODY_MTL =
      "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.mtl";

  private static final String TYPHON_LEGS_OUTER_MTL = "Objects/characters/Player/etherskin_outer";
  private static final String TYPHON_LEGS_SCROLL_MTL = "Objects/characters/Player/etherskin_outer";

  private static final String PAJAMA_MALE_ARMS_SKIN =
      "objects/characters/player/male/player1p_male02_arms.skin";
  private static final String PAJAMA_MALE_ARMS_MTL =
      "objects/characters/player/male/player1p_male02_arms.mtl";
  private static final String PAJAMA_MALE_LEGS_SKIN =
      "objects/characters/player/male/player1p_male02_pajamas.skin";
  private static final String PAJAMA_MALE_LEGS_MTL =
      "objects/characters/player/male/player1p_male02_pajamas.mtl";

  private static final String PAJAMA_FEMALE_ARMS_SKIN =
      "objects/characters/player/female/player1p_female02_arms.skin";
  // Not a typo, the filename is just like this
  private static final String PAJAMA_FEMALE_ARMS_MTL =
      "objects/characters/player/female/player1p_female_02_arms.mtl";
  private static final String PAJAMA_FEMALE_LEGS_SKIN =
      "objects/characters/player/female/player1p_female02_pajamas.skin";
  private static final String PAJAMA_FEMALE_LEGS_MTL =
      "objects/characters/player/female/player1p_female02_pajamas.mtl";

  private static final String COSMONAUT_ARMS_SKIN =
      "objects/characters/player/male/cosmonaut_genmalebody01.skin";
  private static final String COSMONAUT_ARMS_MTL =
      "objects/characters/player/male/cosmonaut1p_male01.mtl";
  private static final String GENERIC_SUIT_SKIN =
      "objects/characters/player/male/player1p_male01_legs.skin";
  private static final String GENERIC_SUIT_MTL =
      "objects/characters/player/male/player1p_male01_legs.mtl";

  private static final ImmutableMap<BodyType, String> BODY_TYPE_TO_ARMS_SKIN =
      new ImmutableMap.Builder<BodyType, String>()
          .put(BodyType.Corporate, BODY_SKIN.get(BodyType.Corporate))
          .put(BodyType.Cosmonaut, COSMONAUT_ARMS_SKIN)
          .put(BodyType.Dahl, BODY_SKIN.get(BodyType.Dahl))
          .put(BodyType.Engineer, BODY_SKIN.get(BodyType.Engineer))
          .put(BodyType.GlovelessMale, BODY_SKIN.get(BodyType.GlovelessMale))
          .put(BodyType.LabCoat, LAB_COAT_HANDS_SKIN)
          .put(BodyType.PajamaFemale, PAJAMA_FEMALE_ARMS_SKIN)
          .put(BodyType.PajamaMale, PAJAMA_MALE_ARMS_SKIN)
          .put(BodyType.Scientist, BODY_SKIN.get(BodyType.Scientist))
          .put(BodyType.Security, BODY_SKIN.get(BodyType.Security))
          .put(BodyType.Volunteer, BODY_SKIN.get(BodyType.Volunteer))
          .put(BodyType.Typhon, BODY_SKIN.get(BodyType.Typhon))
          .build();

  private static final ImmutableMap<BodyType, String> BODY_TYPE_TO_ARMS_MTL =
      new ImmutableMap.Builder<BodyType, String>()
          .put(BodyType.Corporate, BODY_MTL.get(BodyType.Corporate))
          .put(BodyType.Cosmonaut, COSMONAUT_ARMS_MTL)
          .put(BodyType.Dahl, BODY_MTL.get(BodyType.Dahl))
          .put(BodyType.Engineer, BODY_MTL.get(BodyType.Engineer))
          .put(BodyType.GlovelessMale, BODY_MTL.get(BodyType.GlovelessMale))
          .put(BodyType.LabCoat, LAB_COAT_HANDS_MTL)
          .put(BodyType.PajamaFemale, PAJAMA_FEMALE_ARMS_MTL)
          .put(BodyType.PajamaMale, PAJAMA_MALE_ARMS_MTL)
          .put(BodyType.Scientist, BODY_MTL.get(BodyType.Scientist))
          .put(BodyType.Security, BODY_MTL.get(BodyType.Security))
          .put(BodyType.Volunteer, BODY_MTL.get(BodyType.Volunteer))
          .put(BodyType.Typhon, BODY_MTL.get(BodyType.Typhon))
          .build();

  private static final ImmutableMap<BodyType, String> BODY_TYPE_TO_LEGS_SKIN =
      new ImmutableMap.Builder<BodyType, String>().put(BodyType.Cosmonaut, GENERIC_SUIT_SKIN)
          .put(BodyType.LabCoat, LAB_COAT_LEGS_SKIN)
          .put(BodyType.PajamaFemale, PAJAMA_FEMALE_LEGS_SKIN)
          .put(BodyType.PajamaMale, PAJAMA_MALE_LEGS_SKIN)
          .put(BodyType.Typhon, BODY_SKIN.get(BodyType.Typhon))
          .build();

  private static final ImmutableMap<BodyType, String> BODY_TYPE_TO_LEGS_MTL =
      new ImmutableMap.Builder<BodyType, String>().put(BodyType.Cosmonaut, GENERIC_SUIT_MTL)
          .put(BodyType.LabCoat, LAB_COAT_LEGS_MTL)
          .put(BodyType.PajamaFemale, PAJAMA_FEMALE_LEGS_MTL)
          .put(BodyType.PajamaMale, PAJAMA_MALE_LEGS_MTL)
          .put(BodyType.Typhon, TYPHON_LEGS_OUTER_MTL)
          .build();

  public PlayerModelRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    // Choose a body type to apply to the pajama and normal skins
    BodyType bodyType = getRandomBodyType(r);
    logger.info("Setting body type " + bodyType);

    for (String file : PLAYER_FILES) {
      randomizeCdf(file, file, bodyType);
    }
  }

  private void randomizeCdf(String in, String out, BodyType bodyType) {
    try {
      Document document = zipHelper.getDocument(in);
      Element root = document.getRootElement();
      Element attachments = root.getChild("AttachmentList");

      for (Element attachment : attachments.getChildren()) {
        String aNameAttachment = attachment.getAttributeValue("AName");
        if (ARMS.contains(aNameAttachment) && BODY_TYPE_TO_ARMS_SKIN.containsKey(bodyType)) {
          attachment.setAttribute("Binding", BODY_TYPE_TO_ARMS_SKIN.get(bodyType));
          attachment.setAttribute("Material", BODY_TYPE_TO_ARMS_MTL.get(bodyType));
        }
        if (LEGS.contains(aNameAttachment)) {
          if (BODY_TYPE_TO_LEGS_SKIN.containsKey(bodyType)) {
            attachment.setAttribute("Binding", BODY_TYPE_TO_LEGS_SKIN.get(bodyType));
            attachment.setAttribute("Material", BODY_TYPE_TO_LEGS_MTL.get(bodyType));
          } else {
            attachment.setAttribute("Binding", "");
            attachment.setAttribute("Material", "");
          }
        }
      }
      if (bodyType == BodyType.LabCoat) {
        Element newAttachment = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", "labcoat")
            .setAttribute("Binding", LAB_COAT_BODY_SKIN)
            .setAttribute("Material", LAB_COAT_BODY_MTL)
            .setAttribute("Flags", "0");
        attachments.addContent(newAttachment);
      }
      if (bodyType == BodyType.Typhon) {
        Element newAttachment = new Element("Attachment").setAttribute("Inheritable", "0")
            .setAttribute("Type", "CA_SKIN")
            .setAttribute("AName", "scroll")
            .setAttribute("Binding", BODY_SKIN.get(BodyType.Typhon))
            .setAttribute("Material", TYPHON_LEGS_SCROLL_MTL)
            .setAttribute("Flags", "0");
        attachments.addContent(newAttachment);
      }

      zipHelper.copyToPatch(document, out);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private BodyType getRandomBodyType(Random r) {
    return Utils.getRandom(BODY_TYPES_FOR_RANDOM, r);
  }
}
