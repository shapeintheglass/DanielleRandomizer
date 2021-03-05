package randomizers.gameplay.filters.rules;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import org.jdom2.Element;
import databases.TaggedDatabase;
import utils.CustomItemFilterHelper;
import utils.Utils;

public class FruitTreeRule implements Rule {

  CustomItemFilterHelper cfh;
  TaggedDatabase database;

  private static final int MAX_ATTEMPTS = 100;

  public FruitTreeRule(CustomItemFilterHelper cfh, TaggedDatabase database) {
    this.cfh = cfh;
    this.database = database;
  }

  @Override
  public boolean trigger(Element e, Random r, String unused) {
    String entityClass = e.getAttributeValue("Class");
    return entityClass.equals("ArkFruitTree") || entityClass.equals("ArkHarvestable");
  }

  @Override
  public void apply(Element e, Random r, String unused) {
    String entityClass = e.getAttributeValue("Class");
    if (entityClass.equals("ArkFruitTree")) {
      randomizeFruitTree(e, r);
    } else if (entityClass.equals("ArkHarvestable")) {
      randomizeHarvestable(e, r);
    }
  }

  private void randomizeHarvestable(Element e, Random r) {
    Element properties = e.getChild("Properties");
    String archetype = properties.getAttributeValue("archetype_PickupToHarvest");

    if (!archetype.isEmpty() && cfh.trigger(archetype, null)) {
      String newArchetype = getPickup(cfh, archetype, r);

      if (newArchetype != null) {
        Logger.getGlobal()
            .info(String.format("Replacing harvestable archetype %s with %s.", archetype,
                newArchetype));
        properties.setAttribute("archetype_PickupToHarvest", newArchetype);
      }
    }
  }

  private void randomizeFruitTree(Element e, Random r) {
    List<Element> spawns = e.getChild("Properties")
        .getChild("Spawns")
        .getChildren();

    for (Element spawnOption : spawns) {
      String archetype = spawnOption.getAttributeValue("archetype_PickupToSpawn");
      if (!archetype.isEmpty() && cfh.trigger(archetype, null)) {
        String newArchetype = getPickup(cfh, archetype, r);

        if (newArchetype != null) {
          Logger.getGlobal()
              .info(String.format("Replacing fruit tree archetype %s with %s.", archetype,
                  newArchetype));
          spawnOption.setAttribute("archetype_PickupToSpawn", newArchetype);

        }
      }
    }
  }

  private String getPickup(CustomItemFilterHelper cfh, String archetype, Random r) {
    // Try different items until we get a valid pickup item
    // TODO: Make this smarter by just removing non-pickup items from the list first
    for (int i = 0; i < MAX_ATTEMPTS; i++) {
      Element toSwap = cfh.getEntity(archetype, null, r);

      // Ensure that this has the tag "ArkPickups" to represent that it can be added
      // to an inventory
      Set<String> tags = Utils.getTags(toSwap);
      if (tags.contains("ArkPickups")) {
        return Utils.getNameForEntity(toSwap);
      }
    }
    return null;
  }
}
