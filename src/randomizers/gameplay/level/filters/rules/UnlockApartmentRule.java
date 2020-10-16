package randomizers.gameplay.level.filters.rules;

import org.jdom2.Element;

public class UnlockApartmentRule implements Rule {

  private static final String NAME_ATTRIBUTE = "Name";
  private static final String FAKE_DOOR_1 = "ApartmentDoor_Fake1";
  private static final String FAKE_DOOR_2 = "ApartmentDoor_Fake3";

  @Override
  public boolean trigger(Element e) {
    return e.getAttributeValue(NAME_ATTRIBUTE).equals(FAKE_DOOR_1)
        || e.getAttributeValue(NAME_ATTRIBUTE).equals(FAKE_DOOR_2);
  }

  @Override
  public void apply(Element e) {
    e.getChild("Properties2").setAttribute("bLocked", "0");
  }

}
