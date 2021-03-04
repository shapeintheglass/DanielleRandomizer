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

public class GameEffectsRandomizer extends BaseRandomizer {

  private static final String GAME_EFFECTS_PATH = "ark/npc/npcgameeffects.xml";
  
  TaggedDatabase database;
  
  /**
   * Randomizes cystoids.
   * @param s
   * @param zipHelper
   * @param database
   */
  public GameEffectsRandomizer(Settings s, ZipHelper zipHelper, TaggedDatabase database) {
    super(s, zipHelper);
    this.database = database;
  }

  @Override
  public void randomize() {
    try {
      Document d = zipHelper.getDocument(GAME_EFFECTS_PATH);
      
      CustomEnemyFilterHelper cfh = new CustomEnemyFilterHelper(settings, database);
      Element root = d.getRootElement().getChild("ArkNpcGameEffect_ExcreteCystoids");
      String archetype = root.getAttributeValue("cystoidArchetypeName");
      String newArchetype = null;
      if (cfh.trigger(archetype, null)) {
        newArchetype = cfh.getEntityStr(archetype, null, r);
      }
      
      if (newArchetype != null) {
        root.setAttribute("cystoidArchetypeName", newArchetype);
      }
      
      zipHelper.copyToPatch(d, GAME_EFFECTS_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

}
