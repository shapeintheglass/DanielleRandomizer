package utils.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.util.JsonFormat;
import proto.Voice.ActorVoice;
import proto.Voice.Dialog;

public class VoiceConfigGenerator {
  private static final String INPUT_DIR = "_data/ark/dialog/voices";
  private static final String OUTPUT_FILE = "_data/ark/dialog/voices/voices.json";

  // Map of voice actor id to character id to context name to dialog id list
  private static Map<String, List<String>> voicesMap = Maps.newHashMap();


  private static void parseDialogIds(File f) throws JDOMException, IOException {
    SAXBuilder saxBuilder = new SAXBuilder();
    FileInputStream fis = new FileInputStream(f);
    Document d = saxBuilder.build(fis);
    Element root = d.getRootElement();

    String voiceId = f.getName()
        .substring(0, f.getName()
            .length() - 4);
    List<String> dialogList = Lists.newArrayList();

    for (Element dialog : root.getChildren()) {
      dialogList.add(dialog.getAttributeValue("id"));
    }

    voicesMap.put(voiceId, dialogList);
  }

  private static void writeToFile(Dialog dialog) throws IOException {
    File toWrite = new File(OUTPUT_FILE);
    toWrite.createNewFile();
    String serialized = JsonFormat.printer()
        .includingDefaultValueFields()
        .preservingProtoFieldNames()
        .print(dialog);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(toWrite));) {
      bw.write(serialized);
    }
  }

  public static void main(String[] args) {
    File inputDir = new File(INPUT_DIR);

    for (File f : inputDir.listFiles()) {
      try {
        parseDialogIds(f);
      } catch (JDOMException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    Dialog.Builder dialogBuilder = Dialog.newBuilder();

    for (String key : voicesMap.keySet()) {
      ActorVoice.Builder actorVoiceBuilder = ActorVoice.newBuilder()
          .setActorId(key);
      actorVoiceBuilder.addAllVoiceLines(voicesMap.get(key));
      dialogBuilder.addActorVoiceLines(actorVoiceBuilder.build());
    }

    try {
      writeToFile(dialogBuilder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
