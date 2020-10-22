package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;

public class UnlockLobbyRule implements Rule {

  @Override
  public boolean trigger(Element e, Random r) {
    return e.getAttributeValue("Name")
            .equals("ArkNpcSpawner_Technopath1");
  }

  @Override
  public void apply(Element e, Random r) {
    // 318.54999,708.57501,901.27502 --> 348.90982,728.77325,487.64096
    e.setAttribute("Pos", "348.90982,728.77325,487.64096");
    e.getChild("Properties")
     .setAttribute("sNpcArchetype", "ArkNpcs.Custom.FakeTechnopath");
  }

}
