package randomizers.gameplay;

import java.io.IOException;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import databases.TaggedDatabase;
import json.SettingsJson;
import utils.CustomFilterHelper;
import utils.ZipHelper;

public class NightmareHelper {
  public static void install(TaggedDatabase database, SettingsJson settings, ZipHelper zipHelper) {
    Random r = new Random(settings.getSeed());
    try {
      Document document = zipHelper.getDocument(ZipHelper.NIGHTMARE_MANAGER);
      Element root = document.getRootElement();

      String currentType = root.getAttributeValue("NightmareArchetypePath");
      String newArchetype = new CustomFilterHelper(settings.getGameplaySettings().getEnemySpawnSettings(), database)
          .getEntityStr(currentType, null, r);
      if (newArchetype != null) {
        root.setAttribute("NightmareArchetypePath", newArchetype);
      }

      zipHelper.copyToPatch(document, ZipHelper.NIGHTMARE_MANAGER);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }
}
