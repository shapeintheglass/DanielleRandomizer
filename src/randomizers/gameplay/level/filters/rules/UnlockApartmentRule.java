package randomizers.gameplay.level.filters.rules;

import utils.XmlEntity;

public class UnlockApartmentRule implements Rule {
  
  private static final String FAKE_DOOR_1 = "ApartmentDoor_Fake1";
  private static final String FAKE_DOOR_2 = "ApartmentDoor_Fake3";

  @Override
  public boolean trigger(XmlEntity e) {
    return e.getValue("Name").equals(FAKE_DOOR_1) || e.getValue("Name").equals(FAKE_DOOR_2);
  }

  @Override
  public void apply(XmlEntity e) {
    XmlEntity properties = e.getSubEntityByTagName("Properties2");
    properties.setValue("bLocked", "0");
  }

}
