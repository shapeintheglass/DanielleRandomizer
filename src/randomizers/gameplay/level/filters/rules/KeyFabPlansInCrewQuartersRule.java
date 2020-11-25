package randomizers.gameplay.level.filters.rules;

import java.util.Random;
import java.util.logging.Logger;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;

/**
 * Places key items in specific locations
 */
public class KeyFabPlansInCrewQuartersRule implements Rule {
  private static final String LEVEL_REQ = "executive/crewfacilities";

  private static final ImmutableMap<String, String> TO_SWAP =
      new ImmutableMap.Builder<String, String>()
          .put("Books.ArkBk_SalesFolder1",
              "ArkPickups.Crafting.FabricationPlans.BigNullwaveTransmitterFabPlan")
          .put("Books.ArkBk_BonusNeuromods1",
              "ArkPickups.Crafting.FabricationPlans.AlexStationKeyFabPlan")
          .put("Books.ArkBk_SL_FermiParadox1",
              "ArkPickups.Crafting.FabricationPlans.MorganStationKeyFabPlan")
          .build();


  @Override
  public boolean trigger(Element e, Random r, String filename) {
    if (!filename.equals(LEVEL_REQ)) {
      return false;
    }
    String elementName = e.getAttributeValue("Name");
    for (String name : TO_SWAP.keySet()) {
      if (elementName.equals(name)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String name = e.getAttributeValue("Name");
    e.setAttribute("Archetype", TO_SWAP.get(name));
    Logger.getGlobal().info(String.format("Spawning %s in %s", TO_SWAP.get(name), name));
  }

}
