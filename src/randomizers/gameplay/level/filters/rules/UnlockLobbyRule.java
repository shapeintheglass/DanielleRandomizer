package randomizers.gameplay.level.filters.rules;

import org.jdom2.Element;

public class UnlockLobbyRule implements Rule {

  @Override
  public boolean trigger(Element e) {
    return e.getAttributeValue("Name").equals("ArkNpcSpawner_Technopath1");
  }

  @Override
  public void apply(Element e) {
    // 318.54999,708.57501,901.27502 --> 307.85001,738.42499,480
    e.setAttribute("Pos", "307.85001,738.42499,480");
  }

}
