package randomizers.gameplay.filters.rules;

import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import utils.CustomItemFilterHelper;
import utils.Utils;

public class FruitTreeRule implements Rule {

  private CustomItemFilterHelper cfh;

  public FruitTreeRule(CustomItemFilterHelper cfh) {
    this.cfh = cfh;
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
      String newArchetype = Utils.getNameForEntity(cfh.getPickupHarvestableEntity(archetype, null, r));

      if (newArchetype != null) {
        properties.setAttribute("archetype_PickupToHarvest", newArchetype);
      }
    }
  }

  private void randomizeFruitTree(Element e, Random r) {
    List<Element> spawns = e.getChild("Properties").getChild("Spawns").getChildren();

    for (Element spawnOption : spawns) {
      String archetype = spawnOption.getAttributeValue("archetype_PickupToSpawn");
      if (!archetype.isEmpty() && cfh.trigger(archetype, null)) {
        String newArchetype = Utils.getNameForEntity(cfh.getPickupHarvestableEntity(archetype, null, r));

        if (newArchetype != null) {
          spawnOption.setAttribute("archetype_PickupToSpawn", newArchetype);
        }
      }
    }
  }
}
