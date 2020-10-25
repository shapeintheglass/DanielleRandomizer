package randomizers.gameplay.level.filters.rules;

import java.util.Random;

import org.jdom2.Element;

public class UnlockLobbyRule extends BaseUnlockDoorsRule {

  private static final String TECHNOPATH_COORDS = "348.90982,728.77325,487.64096";
  private static final String ARK_NPC_SPAWNER_TECHNOPATH1 = "ArkNpcSpawner_Technopath1";
  private static final String[] TO_UNLOCK = { "LevelTransition_Psychotronics", "LevelTransition_ShuttleBay" };

  public UnlockLobbyRule() {
    super(TO_UNLOCK, "research/lobby");
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    return super.trigger(e, r, filename);// || e.getAttributeValue("Name")
                                             //.equals(ARK_NPC_SPAWNER_TECHNOPATH1);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    if (super.trigger(e, r, filename)) {
      super.apply(e, r, filename);
    } else {
      //e.setAttribute("Pos", TECHNOPATH_COORDS);
      //e.getChild("Properties")
      // .setAttribute("sNpcArchetype", "ArkNpcs.Custom.FakeTechnopath");
    }
  }

}
