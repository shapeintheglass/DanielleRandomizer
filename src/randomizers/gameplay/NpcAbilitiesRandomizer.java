package randomizers.gameplay;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import databases.TaggedDatabase;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.CustomEnemyFilterHelper;
import utils.ZipHelper;

public class NpcAbilitiesRandomizer extends BaseRandomizer {

  private static final String NPC_ABILITIES_PATH = "ark/npc/npcabilities.xml";

  TaggedDatabase database;

  public NpcAbilitiesRandomizer(Settings s, ZipHelper zipHelper, TaggedDatabase database) {
    super(s, zipHelper);
    this.database = database;
  }

  @Override
  public void randomize() {
    if (settings.getGameplaySettings().getEnemySpawnSettings().getFiltersCount() == 0) {
      return;
    }

    try {
      Document d = zipHelper.getDocument(NPC_ABILITIES_PATH);

      CustomEnemyFilterHelper cfh = new CustomEnemyFilterHelper(settings, database);
      Element root = d.getRootElement();

      Element ethericDoppelganger = root.getChild("ArkNpcAbility_EthericDoppelganger");
      randomizeEntity(cfh, ethericDoppelganger, "doppelgangerArchetype");

      List<Element> raisedPhantoms = root.getChildren("ArkNpcAbility_RaisePhantomFromCorpse");
      for (Element e : raisedPhantoms) {
        randomizeEntity(cfh, e, "phantomArchetypeName");
      }

      zipHelper.copyToPatch(d, NPC_ABILITIES_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private void randomizeEntity(CustomEnemyFilterHelper cfh, Element entity, String attributeName) {
    String archetype = entity.getAttributeValue(attributeName);
    String newArchetype = null;
    if (cfh.trigger(archetype, null)) {
      newArchetype = cfh.getEntityStr(archetype, null, r);
    }

    if (newArchetype != null) {
      entity.setAttribute(attributeName, newArchetype);
    }
  }

}
