package utils.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.google.protobuf.util.JsonFormat;
import proto.Body.Human;
import proto.Body.HumansFinal;

public class BodyDatabaseGenerator {

  private static final String INPUT_DIR = "_otherdata/objects/characters/humansfinal";
  private static final String OUTPUT_FILE = "_data/objects/characters/humansfinal/humansfinal.json";

  private static Human readBody(File f) throws JDOMException, IOException {
    Human.Builder h = Human.newBuilder();

    h.setName(f.getName());

    SAXBuilder saxBuilder = new SAXBuilder();
    FileInputStream fis = new FileInputStream(f);
    Document d = saxBuilder.build(fis);
    Element root = d.getRootElement();
    h.setSkeleton(root.getChild("Model")
        .getAttributeValue("File"));
    Element addlChrparams = root.getChild("AdditionalChrparams");
    if (addlChrparams != null) {
      h.setChrparams(addlChrparams.getAttributeValue("File"));
    }

    return h.build();
  }

  private static void writeToFile(HumansFinal hf) throws IOException {
    File toWrite = new File(OUTPUT_FILE);
    toWrite.createNewFile();
    String serialized = JsonFormat.printer()
        .includingDefaultValueFields()
        .preservingProtoFieldNames()
        .print(hf);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(toWrite));) {
      bw.write(serialized);
    }
  }

  public static void main(String[] args) {
    File inputDir = new File(INPUT_DIR);

    HumansFinal.Builder hf = HumansFinal.newBuilder();

    for (File f : inputDir.listFiles()) {
      try {
        hf.addBody(readBody(f));
      } catch (IOException | JDOMException e) {
        e.printStackTrace();
      }
    }

    try {
      writeToFile(hf.build());
    } catch (IOException e) {
      e.printStackTrace();
    }


  }
}
