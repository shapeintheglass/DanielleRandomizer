package randomizers.gameplay;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.CustomEnemyFilterHelper;
import utils.ZipHelper;

public class NightmareRandomizer extends BaseRandomizer {

  private static final String NIGHTMARE_PATH = "ark/npc/nightmaremanager.xml";

  TaggedDatabase database;

  public NightmareRandomizer(Settings s, ZipHelper zipHelper, TaggedDatabase database) {
    super(s, zipHelper);
    this.database = database;
  }

  @Override
  public void randomize() {
    if (settings.getGameplaySettings().getEnemySpawnSettings().getFiltersCount() == 0) {
      return;
    }

    try {
      Document d = zipHelper.getDocument(NIGHTMARE_PATH);

      CustomEnemyFilterHelper cfh = new CustomEnemyFilterHelper(settings, database);

      Element root = d.getRootElement();
      String archetype = root.getAttributeValue("NightmareArchetypePath");
      String newArchetype = null;
      if (cfh.trigger(archetype, null)) {
        newArchetype = cfh.getEntityStr(archetype, null, r);
      }

      if (newArchetype != null) {
        root.setAttribute("NightmareArchetypePath", newArchetype);
      }

      zipHelper.copyToPatch(d, NIGHTMARE_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

}
