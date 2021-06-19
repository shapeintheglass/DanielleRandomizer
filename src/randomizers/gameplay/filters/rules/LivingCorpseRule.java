package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

public class LivingCorpseRule implements Rule {
  
  private static final String IS_CORPSE_ATTRIBUTE = "bSpawnCorpse";

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // Triggers on ArkNpcSpawners for corpses
    if (e.getAttributeValue("EntityClass") != null && e.getAttributeValue("EntityClass").equals("ArkNpcSpawner")) {
      // Parse to get the spawned entity
      Element properties = e.getChild("Properties");
      if (properties == null) {
        return false;
      }
      return properties.getAttributeValue(IS_CORPSE_ATTRIBUTE).equals("1");
    } else {
      return false;
    }
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // Update corpse attribute to false
    e.getChild("Properties").setAttribute(IS_CORPSE_ATTRIBUTE, "0");
  }

}
