package randomizers.cosmetic;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class PlanetRandomizer extends BaseRandomizer {

  private static final String SKY_PATH = "libs/sky/arkspacesettings.xml";

  private static final float MIN_SCALE = 1.0f;
  private static final float MAX_SCALE_EARTH = 10.0f;
  private static final float MAX_SCALE_MOON = 2.0f;
  private static final float MAX_SCALE_SUN = 20.0f;

  public PlanetRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    try {
      Document d = zipHelper.getDocument(SKY_PATH);
      Element root = d.getRootElement();
      setValue(root, "Earth", "Scale", MIN_SCALE, MAX_SCALE_EARTH);
      setValue(root, "Moon", "Scale", MIN_SCALE, MAX_SCALE_MOON);
      setValue(root, "Sun", "Scale", MIN_SCALE, MAX_SCALE_SUN);

      zipHelper.copyToPatch(d, SKY_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private void setValue(Element root, String childName, String valueName, float min, float max) {
    Element e = root.getChild(childName);
    String newScale = getInRange(min, max);
    logger.info(String.format("Setting %s %s to %s", childName, valueName, newScale));
    e.setAttribute(valueName, newScale);
  }


  private String getInRange(float min, float max) {
    return String.format("%.5f", min + ((max - min) * r.nextFloat()));
  }
}
