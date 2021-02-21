package randomizers.cosmetic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.BodyConfig;
import utils.BodyConfig.Gender;
import utils.BodyConsts;
import utils.ZipHelper;

public class BodyRandomizer extends BaseRandomizer {

  public BodyRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS);
    Arrays.sort(BodyConsts.BODIES_WITH_BARE_HANDS);
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR);
    Arrays.sort(BodyConsts.ACCESSORY_COMPATIBLE_BODIES);
  }

  public void randomize() {
    for (String f : zipHelper.listFiles(ZipHelper.HUMANS_FINAL_DIR)) {
      String filename = ZipHelper.getFileName(f);
      String out = ZipHelper.HUMANS_FINAL_DIR + "/" + filename;

      try {
        randomizeBody(f, out);
      } catch (IOException | JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return;
      }
    }
  }

  private void randomizeBody(String in, String out) throws FileNotFoundException, IOException, JDOMException {
    Document document = zipHelper.getDocument(in);
    Element root = document.getRootElement();

    String modelFile = root.getChild("Model").getAttributeValue("File").toLowerCase();
    Gender gender = Gender.UNKNOWN;
    if (modelFile.contains(BodyConsts.FEMALE_BODY_TYPE)) {
      gender = BodyConfig.Gender.FEMALE;
    } else if (modelFile.contains(BodyConsts.MALE_BODY_TYPE)) {
      gender = BodyConfig.Gender.MALE;
    } else if (modelFile.contains(BodyConsts.LARGE_MALE_BODY_TYPE)) {
      gender = BodyConfig.Gender.LARGE_MALE;
    } else {
      // Body type not supported, do not overwrite
      return;
    }

    Element attachmentList = root.getChild("AttachmentList");

    String filename = ZipHelper.getFileName(in);
    BodyConfig bc = new BodyConfig(filename, gender, attachmentList, r);
    bc.generateAttachmentsForGender();

    zipHelper.copyToPatch(document, out);
  }
}
