package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import utils.LevelConsts;

public class MorgansOfficeLockRule implements Rule {

  private static final String MORGANS_OFFICE_DOOR_NAME = "MorgansOfficeDoor";

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    return filename.equals(LevelConsts.LOBBY) && e.getAttributeValue("Name").equals(MORGANS_OFFICE_DOOR_NAME);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    e.getChild("Properties2").setAttribute("bUseFreeExitButton", "1");
  }

}
