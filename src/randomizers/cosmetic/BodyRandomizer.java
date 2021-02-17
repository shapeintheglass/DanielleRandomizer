package randomizers.cosmetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.BodyConfig;
import utils.BodyConfig.Gender;
import utils.BodyConsts;
import utils.ZipHelper;

public class BodyRandomizer extends BaseRandomizer {

  private Path tempPatchDir;

  public BodyRandomizer(SettingsJson s, Path tempPatchDir, ZipHelper zipHelper) {
    super(s, zipHelper);
    this.tempPatchDir = tempPatchDir;
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_BARE_HANDS);
    Arrays.sort(BodyConsts.BODIES_WITH_BARE_HANDS);
    Arrays.sort(BodyConsts.HEADS_THAT_SHOULD_NOT_HAVE_HAIR);
    Arrays.sort(BodyConsts.ACCESSORY_COMPATIBLE_BODIES);
  }

  private static final String HUMANS_FINAL_OUT = "objects\\\\characters\\\\humansfinal";
  private static final String HUMANS_FILE_TEMPLATE = "objects\\characters\\humansfinal\\%s";

  public void randomize() {
    for (String f : zipHelper.listFiles(ZipHelper.HUMANS_FINAL_DIR)) {
      String filename = ZipHelper.getFileName(f);
      Path out = tempPatchDir.resolve(String.format(HUMANS_FILE_TEMPLATE, filename));

      try {
        tempPatchDir.resolve(HUMANS_FINAL_OUT).toFile().mkdirs();
        out.toFile().createNewFile();
        randomizeBody(f, out.toFile());
      } catch (IOException | JDOMException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return;
      }
    }
  }

  private void randomizeBody(String in, File out) throws FileNotFoundException, IOException, JDOMException {
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
      out.delete();
      return;
    }

    Element attachmentList = root.getChild("AttachmentList");

    String filename = ZipHelper.getFileName(in);
    BodyConfig bc = new BodyConfig(filename, gender, attachmentList, r);
    bc.generateAttachmentsForGender();

    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(document, new FileOutputStream(out));
  }
}
