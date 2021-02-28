package randomizers.gameplay;

import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class FabPlanCostRandomizer extends BaseRandomizer {

  private static final String FAB_PLAN_PATH = "ark/items/fabricationplanlibrary.xml";

  public FabPlanCostRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    try {
      Document d = zipHelper.getDocument(FAB_PLAN_PATH);

      List<Element> fabPlans = d.getRootElement()
          .getChild("FabricationPlans")
          .getChildren();

      for (Element e : fabPlans) {
        randomizeFabPlanCost(e);
      }

      zipHelper.copyToPatch(d, FAB_PLAN_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }


  private void randomizeFabPlanCost(Element fabPlan) {
    int organicCount = r.nextInt(4);
    int mineralCount = r.nextInt(4);
    int syntheticCount = r.nextInt(4);
    int exoticCount = r.nextInt(4);
    fabPlan.setAttribute("organicCount", Integer.toString(organicCount));
    fabPlan.setAttribute("mineralCount", Integer.toString(mineralCount));
    fabPlan.setAttribute("syntheticCount", Integer.toString(syntheticCount));
    fabPlan.setAttribute("exoticCount", Integer.toString(exoticCount));
  }
}
