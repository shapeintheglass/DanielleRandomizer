package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import databases.TaggedDatabase;
import json.SettingsJson;
import utils.CustomFilterHelper;

public class NightmareHelper {
  private static final String INPUT_FILE = "data/ark/NightmareManager.xml";
  private static final String OUTPUT_FILE = "ark/npc/nightmaremanager.xml";

  public static void install(TaggedDatabase database, SettingsJson settings, Path tempPatchDir) {
    File in = new File(INPUT_FILE);
    File out = tempPatchDir.resolve(OUTPUT_FILE).toFile();
    out.getParentFile().mkdirs();

    Random r = new Random(settings.getSeed());
    try {
      SAXBuilder saxBuilder = new SAXBuilder();

      Document document = saxBuilder.build(in);
      Element root = document.getRootElement();

      String currentType = root.getAttributeValue("NightmareArchetypePath");
      String newArchetype = new CustomFilterHelper(settings.getGameplaySettings().getEnemySpawnSettings(), database)
          .getEntityStr(currentType, null, r);
      if (newArchetype != null) {
        root.setAttribute("NightmareArchetypePath", newArchetype);
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }
}
