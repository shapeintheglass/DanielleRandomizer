package randomizers.cosmetic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import org.jdom2.Document;
import org.jdom2.Element;
import com.google.common.io.ByteStreams;
import com.google.protobuf.util.JsonFormat;
import proto.Body.Human;
import proto.Body.HumansFinal;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import randomizers.generators.BodyGenerator;
import utils.ZipHelper;

public class BodyRandomizer extends BaseRandomizer {

  private static final String BODIES_FILE = "objects/characters/humansfinal/humansfinal.json";


  private HumansFinal bodies;

  public BodyRandomizer(Settings s, ZipHelper zipHelper) throws IOException {
    super(s, zipHelper);

    bodies = readHumansFinalJson();
  }

  private HumansFinal readHumansFinalJson() throws IOException {
    InputStream is = zipHelper.getInputStream(BODIES_FILE);
    HumansFinal.Builder builder = HumansFinal.newBuilder();

    byte[] bytes = ByteStreams.toByteArray(is);

    JsonFormat.parser()
        .ignoringUnknownFields()
        .merge(new String(bytes), builder);

    return builder.build();
  }

  public void randomize() {
    for (Human h : bodies.getBodyList()) {
      createBodyForHuman(h);
    }
  }

  private void createBodyForHuman(Human h) {
    Element root = new Element("CharacterDefinition");
    Element model = new Element("Model");
    model.setAttribute("File", h.getSkeleton());

    Element addlChrparams = null;
    if (h.getChrparams() != null) {
      addlChrparams = new Element("AdditionalChrparams");
      addlChrparams.setAttribute("File", h.getChrparams());
    }

    BodyGenerator bc = new BodyGenerator(h, r);
    Element attachmentList = bc.getAttachmentList();

    root.addContent(model)
        .addContent(addlChrparams)
        .addContent(attachmentList);

    Document d = new Document(root);
    String outputPath = "objects/characters/humansfinal/" + h.getName();
    try {
      zipHelper.copyToPatch(d, outputPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Settings s = Settings.newBuilder()
        .build();
    try {
      ZipHelper zipHelper = new ZipHelper(null, Paths.get("temp"));
      BodyRandomizer br = new BodyRandomizer(s, zipHelper);
      br.randomize();
      zipHelper.closeOutputZips();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
