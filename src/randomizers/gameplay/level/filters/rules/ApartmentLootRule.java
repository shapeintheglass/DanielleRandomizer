package randomizers.gameplay.level.filters.rules;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class ApartmentLootRule implements Rule {

  private static final String LOOTTABLE_CONTAINER_ATTR = "loottable_ContainerLootTable";
  private static final String PROPERTIES_ATTR = "Properties2";
  private static final String NIGHTSTAND_A_DAY_1 = "Containers/Small.Nightstand_A_Day1";
  private static final String NIGHTSTAND_A2_DAY_1 = "Containers/Small.Nightstand_A2_Day1";
  private static final String FRIDGE_DAY_1 = "Containers/Medium.Refridgerator_A1_Day1";
  private static final String CONGRATS_NOTE_DAY_1 = "Data.Notes.Memo4";

  private static final String NIGHTSTAND_A_LOOT_TABLE = "RANDOMIZER_MorganApartmentWeaponsNightstand";
  private static final String NIGHTSTAND_A2_LOOT_TABLE = "RANDOMIZER_MorganApartmentWeaponsNightstand2";
  private static final String FRIDGE_LOOT_TABLE = "RANDOMIZER_MorganApartmentFridge";

  
  Map<String, String> NAME_TO_LOOT_TABLE = Stream.of(new String[][] {
    { NIGHTSTAND_A_DAY_1, NIGHTSTAND_A_LOOT_TABLE }, 
    { NIGHTSTAND_A2_DAY_1, NIGHTSTAND_A2_LOOT_TABLE },
    { FRIDGE_DAY_1, FRIDGE_LOOT_TABLE },
  }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

  @Override
  public boolean trigger(Element e) {
    String name = e.getAttributeValue("Name");
    return name.equals(CONGRATS_NOTE_DAY_1) || NAME_TO_LOOT_TABLE.containsKey(name);
  }

  @Override
  public void apply(Element e) {
    String name = e.getAttributeValue("Name");
    if (NAME_TO_LOOT_TABLE.containsKey(name)) {
      e.getChild(PROPERTIES_ATTR)
       .setAttribute(LOOTTABLE_CONTAINER_ATTR, NAME_TO_LOOT_TABLE.get(name));
    } else if (name.equals(CONGRATS_NOTE_DAY_1)) {
      // Change to the general access keycard
      e.setAttribute("Archetype", "ArkPickups.Data.Keycard");
      e.getChild(PROPERTIES_ATTR).setAttribute("keycard_Keycard", "15659330456309530410");
    }
    
  }

}
