package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

public class DisableGravityRule implements Rule {

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    if (!filename.equals("station/exterior") && !filename.equals("research/zerog_utilitytunnels")) {
      return false;
    }

    String entityClass = e.getAttributeValue("EntityClass");
    if (entityClass == null) {
      return false;
    }
    return entityClass.equals("GravityBox");
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    e.getChild("Properties").setAttribute("bActive", "0");
  }
}
