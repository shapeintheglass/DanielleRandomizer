package randomizers.gameplay.level.filters.rules;

import java.util.Arrays;
import java.util.Random;

import org.jdom2.Element;

public class UnlockArboretumRule implements Rule {

  private static final String[] TO_UNLOCK = { "Door.Door_LevelTransition_Close", "Door.Door_LevelTransition_Open" };
  private static final String DEEP_STORAGE_ID = "1713490239377738413";

  public UnlockArboretumRule() {
    Arrays.sort(TO_UNLOCK);
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    return Arrays.binarySearch(TO_UNLOCK, e.getAttributeValue("Name")) >= 0;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    e.getChild("Properties2")
     .setAttribute("location_Destination", DEEP_STORAGE_ID);
  }

}
