package randomizers.gameplay.level.filters.rules;

import org.jdom2.Element;

public class ApartmentLootRule implements Rule {

  @Override
  public boolean trigger(Element e) {
    return e.getAttributeValue("Name").equals("Containers/Small.Nightstand_A_Day1");
  }

  @Override
  public void apply(Element e) {
    e.getChild("Properties2").setAttribute("loottable_ContainerLootTable", "RANDOMIZER_MorganApartmentWeaponsNightstand");
  }

}
