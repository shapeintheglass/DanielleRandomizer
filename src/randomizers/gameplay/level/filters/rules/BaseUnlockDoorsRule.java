package randomizers.gameplay.level.filters.rules;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import org.jdom2.Element;

public abstract class BaseUnlockDoorsRule implements Rule {

  private static final String NAME = "Name";
  private static final String B_STARTS_LOCKED = "bStartsLocked";
  private static final String PROPERTIES2 = "Properties2";
  private String[] toUnlock;
  private String filename;

  public BaseUnlockDoorsRule(String[] toUnlock, String filename) {
    this.toUnlock = toUnlock;
    this.filename = filename;
    Arrays.sort(toUnlock);
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    if (!filename.equals(this.filename)) {
      return false;
    }
    return Arrays.binarySearch(toUnlock, e.getAttributeValue(NAME)) >= 0;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    Logger.getGlobal()
          .info("Unlocking " + e.getAttributeValue(NAME));
    e.getChild(PROPERTIES2)
     .setAttribute(B_STARTS_LOCKED, "0");
  }
}
