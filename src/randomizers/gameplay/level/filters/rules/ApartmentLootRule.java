package randomizers.gameplay.level.filters.rules;

import org.jdom2.Element;

public class ApartmentLootRule implements Rule {

  private static final String NIGHTSTAND_A = "Containers/Small.Nightstand_A_Day1";
  private static final String NIGHTSTAND_A2 = "Containers/Small.Nightstand_A2_Day1";
  private static final String FRIDGE = "Containers/Medium.Refridgerator_A1_Day1";
  
  @Override
  public boolean trigger(Element e) {
    return 
        e.getAttributeValue("Name").equals(NIGHTSTAND_A) 
        || e.getAttributeValue("Name").equals(NIGHTSTAND_A2)
        || e.getAttributeValue("Name").equals(FRIDGE);
  }

  @Override
  public void apply(Element e) {
    switch(e.getAttributeValue("Name")) {
      case NIGHTSTAND_A:
        e.getChild("Properties2").setAttribute("loottable_ContainerLootTable",
            "RANDOMIZER_MorganApartmentWeaponsNightstand");
        break;
      case NIGHTSTAND_A2:
        e.getChild("Properties2").setAttribute("loottable_ContainerLootTable",
            "RANDOMIZER_MorganApartmentWeaponsNightstand2");
        break;
      case FRIDGE:
        e.getChild("Properties2").setAttribute("loottable_ContainerLootTable",
            "RANDOMIZER_MorganApartmentFridge");
    }
  }

}
