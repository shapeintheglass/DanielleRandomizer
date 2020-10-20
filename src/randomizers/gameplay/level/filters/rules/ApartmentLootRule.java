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
  private static final String CABINET_DAY_1 = "Containers/Large.cabinet_apt_kitchen_a_tall1_Day1";
  private static final String KITCHEN_CABINET_A_DAY_1 = "Containers.Medium.cabinet_apt_kitchen_a_lower1_Day1";
  private static final String KITCHEN_CABINET_B_DAY_1 = "Containers.Medium.cabinet_apt_kitchen_a_lower2_Day1";
  private static final String KITCHEN_CABINET_C_DAY_1 = "Containers.Medium.cabinet_apt_kitchen_a_lower3_Day1";

  private static final String NIGHTSTAND_A_DAY_2 = "Containers/Small.Nightstand_A_Day2";
  private static final String NIGHTSTAND_A2_DAY_2 = "Containers/Small.Nightstand_A2_Day2";
  private static final String FRIDGE_DAY_2 = "Containers/Medium.Refridgerator_A4_Day2";
  private static final String CONGRATS_NOTE_DAY_2 = "Data.Notes.Memo7";
  private static final String CABINET_DAY_2 = "Containers/Large.cabinet_apt_kitchen_a_tall4_Day2";
  private static final String KITCHEN_CABINET_A_DAY_2 = "Containers.Medium.cabinet_apt_kitchen_a_lower10_Day2";
  private static final String KITCHEN_CABINET_B_DAY_2 = "Containers.Medium.cabinet_apt_kitchen_a_lower11_Day2";
  private static final String KITCHEN_CABINET_C_DAY_2 = "Containers.Medium.cabinet_apt_kitchen_a_lower12_Day2";

  private static final String NIGHTSTAND_A_LOOT_TABLE = "RANDOMIZER_MorganApartmentWeaponsNightstand";
  private static final String NIGHTSTAND_A2_LOOT_TABLE = "RANDOMIZER_MorganApartmentWeaponsNightstand2";
  private static final String FRIDGE_LOOT_TABLE = "RANDOMIZER_MorganApartmentFridge";
  private static final String CABINET_LOOT_TABLE = "RANDOMIZER_MorganApartmentCabinet";
  private static final String KITCHEN_CABINET_LOOT_TABLE_A = "RANDOMIZER_MorganApartmentKitchenChipsetsA";
  private static final String KITCHEN_CABINET_LOOT_TABLE_B = "RANDOMIZER_MorganApartmentKitchenChipsetsB";
  private static final String KITCHEN_CABINET_LOOT_TABLE_C = "RANDOMIZER_MorganApartmentKitchenChipsetsC";

  Map<String, String> NAME_TO_LOOT_TABLE = Stream.of(new String[][] { { NIGHTSTAND_A_DAY_1, NIGHTSTAND_A_LOOT_TABLE },
      { NIGHTSTAND_A2_DAY_1, NIGHTSTAND_A2_LOOT_TABLE }, { FRIDGE_DAY_1, FRIDGE_LOOT_TABLE },
      { CABINET_DAY_1, CABINET_LOOT_TABLE }, { KITCHEN_CABINET_A_DAY_1, KITCHEN_CABINET_LOOT_TABLE_A },
      { KITCHEN_CABINET_B_DAY_1, KITCHEN_CABINET_LOOT_TABLE_B },
      { KITCHEN_CABINET_C_DAY_1, KITCHEN_CABINET_LOOT_TABLE_C }, { NIGHTSTAND_A_DAY_2, NIGHTSTAND_A_LOOT_TABLE },
      { NIGHTSTAND_A2_DAY_2, NIGHTSTAND_A2_LOOT_TABLE }, { FRIDGE_DAY_2, FRIDGE_LOOT_TABLE },
      { CABINET_DAY_2, CABINET_LOOT_TABLE }, { KITCHEN_CABINET_A_DAY_2, KITCHEN_CABINET_LOOT_TABLE_A },
      { KITCHEN_CABINET_B_DAY_2, KITCHEN_CABINET_LOOT_TABLE_B },
      { KITCHEN_CABINET_C_DAY_2, KITCHEN_CABINET_LOOT_TABLE_C } })
                                                 .collect(Collectors.toMap(data -> data[0], data -> data[1]));

  @Override
  public boolean trigger(Element e) {
    String name = e.getAttributeValue("Name");
    return name.equals(CONGRATS_NOTE_DAY_1) || name.equals(CONGRATS_NOTE_DAY_2) || NAME_TO_LOOT_TABLE.containsKey(name);
  }

  @Override
  public void apply(Element e) {
    String name = e.getAttributeValue("Name");
    if (NAME_TO_LOOT_TABLE.containsKey(name)) {
      e.getChild(PROPERTIES_ATTR)
       .setAttribute(LOOTTABLE_CONTAINER_ATTR, NAME_TO_LOOT_TABLE.get(name));
    } else if (name.equals(CONGRATS_NOTE_DAY_1) || name.equals(CONGRATS_NOTE_DAY_2)) {
      // Change to the general access keycard
      e.setAttribute("Archetype", "ArkPickups.Data.Keycard");
      e.getChild(PROPERTIES_ATTR)
       .setAttribute("keycard_Keycard", "15659330456309530410");
    }

  }

}
