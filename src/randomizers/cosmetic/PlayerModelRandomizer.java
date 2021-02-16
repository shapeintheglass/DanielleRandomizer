package randomizers.cosmetic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import json.SettingsJson;
import randomizers.BaseRandomizer;

public class PlayerModelRandomizer extends BaseRandomizer {

  private static final String PLAYER_IN = "data/player/player.cdf";
  private static final String PLAYER_OUT = "objects/characters/player/player.cdf";

  private static enum BodyType {
    Volunteer, Security, Engineer, Scientist, Corporate, LabCoat, Dahl, Etheric, Cosmonaut, Phantom, ShiftMale,
    ShiftFemale, Female, PajamaMale, PajamaFemale, GlovelessMale
  }

  private static final ImmutableMap<BodyType, String> BODY_SKIN = new ImmutableMap.Builder<BodyType, String>().put(
      BodyType.Volunteer, "Objects/characters/Humans/Volunteer/Volunteer_GenMaleBody01.skin")
      .put(BodyType.Security, "Objects/characters/Humans/Security/Security_GenMaleBody01.skin")
      .put(BodyType.Engineer, "Objects/characters/Humans/Mechanic/Mechanic_GenMaleBody01.skin")
      .put(BodyType.Scientist, "Objects/characters/Humans/Scientist/Scientist_GenMaleBody01.skin")
      .put(BodyType.Corporate, "Objects/characters/Humans/Corporate/Corporate_GenMaleBody01.skin")
      .put(BodyType.LabCoat, "Objects/characters/Humans/Labcoat/Labcoat_GenMaleBody01.skin")
      .put(BodyType.Dahl, "Objects/characters/Humans/Dahl/Dahl_GenMaleBody01.skin")
      .put(BodyType.Etheric, "objects/characters/humans/genmale/genmale_mesh.skin")
      .put(BodyType.Phantom, "objects/characters/humans/genmale/genmale_mesh.skin")
      .put(BodyType.ShiftMale, "objects/characters/humans/genmale/genmale_mesh.skin")
      .put(BodyType.ShiftFemale, "objects/characters/humans/genmale/genmale_mesh.skin")
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
      .put(BodyType.Etheric, "Objects/characters/Player/EtherDuplicate_00")
      .put(BodyType.Phantom, "objects/characters/aliens/weaver/weaver01")
      .put(BodyType.ShiftMale, "objects/characters/player/shiftpower/playermale_shift.mtl")
      .put(BodyType.ShiftFemale, "objects/characters/player/shiftpower/playerfemale_shift.mtl")
      .put(BodyType.GlovelessMale, "objects/characters/humans/morgankarl/morgan_genmalebody01_cut_scene.mtl")
      .build();

  private static final String LAB_COAT_HANDS_SKIN = "objects/characters/humans/labcoat/labcoat_genmalehands01.skin";
  private static final String LAB_COAT_HANDS_MTL = "objects/characters/humans/scientist/scientist_genmalebody01.mtl";
  private static final String LAB_COAT_LEGS_SKIN = "objects/characters/humans/labcoat/labcoat_genmalelegs01.skin";
  private static final String LAB_COAT_LEGS_MTL = "objects/characters/humans/scientist/scientist_genmalebody01.mtl";

  private static final String FEMALE_ARMS_SKIN = "objects/characters/player/female/player1p_female01_arms.skin";
  private static final String FEMALE_ARMS_MTL = "objects/characters/player/female/player1p_female01_Arms.mtl";
  private static final String FEMALE_LEGS_SKIN = "objects/characters/player/female/player1p_female01_legs.skin";
  private static final String FEMALE_LEGS_MTL = "objects/characters/player/female/Player1P_Female01_Legs.mtl";

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
  private static final String COSMONAUT_SUIT_SKIN = "objects/characters/player/male/player1p_male01_legs.skin";
  private static final String COSMONAUT_SUIT_MTL = "objects/characters/player/male/player1p_male01_legs.mtl";

  private Path tempPatchDir;

  public PlayerModelRandomizer(SettingsJson s, Path tempPatchDir) {
    super(s);
    this.tempPatchDir = tempPatchDir;
  }

  @Override
  public void randomize() {
    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(new File(PLAYER_IN));
      Element root = document.getRootElement();
      Element attachments = root.getChild("AttachmentList");

      Set<Element> toRemove = Sets.newHashSet();
      for (Element attachment : attachments.getChildren()) {
        if (attachment.getAttributeValue("AName").equals("legs") || attachment.getAttributeValue("AName")
            .equals("arms")) {
          toRemove.add(attachment);
        }
      }

      for (Element e : toRemove) {
        attachments.removeContent(e);
      }

      // Replace arms and legs with a random body attachment
      int index = r.nextInt(BodyType.values().length);
      BodyType bodyType = BodyType.values()[index];
      logger.info("Setting body type " + bodyType);

      switch (bodyType) {
        case Female:
          Element arms = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "arms")
              .setAttribute("Binding", FEMALE_ARMS_SKIN)
              .setAttribute("Material", FEMALE_ARMS_MTL)
              .setAttribute("Flags", "0");
          Element legs = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "legs")
              .setAttribute("Binding", FEMALE_LEGS_SKIN)
              .setAttribute("Material", FEMALE_LEGS_MTL)
              .setAttribute("Flags", "0");
          attachments.addContent(arms);
          attachments.addContent(legs);
          break;
        case PajamaFemale:
          arms = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "arms")
              .setAttribute("Binding", PAJAMA_FEMALE_ARMS_SKIN)
              .setAttribute("Material", PAJAMA_FEMALE_ARMS_MTL)
              .setAttribute("Flags", "0");
          legs = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "legs")
              .setAttribute("Binding", PAJAMA_FEMALE_LEGS_SKIN)
              .setAttribute("Material", PAJAMA_FEMALE_LEGS_MTL)
              .setAttribute("Flags", "0");
          attachments.addContent(arms);
          attachments.addContent(legs);
          break;
        case PajamaMale:
          arms = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "arms")
              .setAttribute("Binding", PAJAMA_MALE_ARMS_SKIN)
              .setAttribute("Material", PAJAMA_MALE_ARMS_MTL)
              .setAttribute("Flags", "0");
          legs = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "legs")
              .setAttribute("Binding", PAJAMA_MALE_LEGS_SKIN)
              .setAttribute("Material", PAJAMA_MALE_LEGS_MTL)
              .setAttribute("Flags", "0");
          attachments.addContent(arms);
          attachments.addContent(legs);
          break;
        case Cosmonaut:
          arms = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "arms")
              .setAttribute("Binding", COSMONAUT_ARMS_SKIN)
              .setAttribute("Material", COSMONAUT_ARMS_MTL)
              .setAttribute("Flags", "0");
          legs = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "legs")
              .setAttribute("Binding", COSMONAUT_SUIT_SKIN)
              .setAttribute("Material", COSMONAUT_SUIT_MTL)
              .setAttribute("Flags", "0");
          attachments.addContent(arms);
          attachments.addContent(legs);
          break;
        default:
          String skin = BODY_SKIN.get(bodyType);
          String mtl = BODY_MTL.get(bodyType);

          Element body = new Element("Attachment").setAttribute("Inheritable", "0")
              .setAttribute("Type", "CA_SKIN")
              .setAttribute("AName", "body")
              .setAttribute("Binding", skin)
              .setAttribute("Material", mtl)
              .setAttribute("Flags", "0");
          attachments.addContent(body);

          if (bodyType == BodyType.LabCoat) {
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
            attachments.addContent(hands);
            attachments.addContent(legs);
          }
          break;
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      File outFile = tempPatchDir.resolve(PLAYER_OUT).toFile();
      outFile.getParentFile().mkdirs();
      xmlOutput.output(document, new FileOutputStream(outFile));
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }
}
