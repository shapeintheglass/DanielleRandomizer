package randomizers.gameplay;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

      List<Element> fabPlans = d.getRootElement().getChild("FabricationPlans").getChildren();

      for (Element e : fabPlans) {
        randomizeFabPlanCost(e);
      }

      zipHelper.copyToPatch(d, FAB_PLAN_PATH);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  private void randomizeFabPlanCost(Element fabPlan) {
    FabPlan fp = new FabPlan(fabPlan.getAttributeValue("organicCount"), fabPlan.getAttributeValue("mineralCount"),
        fabPlan.getAttributeValue("syntheticCount"), fabPlan.getAttributeValue("exoticCount"));
    fp.regenCost(r);
    fabPlan.setAttribute("organicCount", fp.getOrganic());
    fabPlan.setAttribute("mineralCount", fp.getMineral());
    fabPlan.setAttribute("syntheticCount", fp.getSynthetic());
    fabPlan.setAttribute("exoticCount", fp.getExotic());
  }

  private enum Material {
    ORGANIC, MINERAL, SYNTHETIC, EXOTIC
  }

  public static class FabPlan {

    private static final int NUM_MATERIALS = 4;
    private static final int NUM_PER_MATERIAL = 3;

    private Map<Material, String> newCount;

    private int numResources;

    public FabPlan(String organic, String mineral, String synthetic, String exotic) {
      this(Integer.parseInt(organic) + Integer.parseInt(mineral) + Integer.parseInt(synthetic) + Integer.parseInt(
          exotic));
    }

    public FabPlan(int numResources) {
      this.numResources = numResources;
      newCount = new HashMap<>();
    }

    // Create a new fab plan with the same total cost as the old one.
    public void regenCost(Random r) {
      // Pick a random category to start at
      int currentCategory = r.nextInt(NUM_MATERIALS);
      int numResourcesRemaining = numResources;

      // Iterate through all material types, or until we are out of resources
      for (int i = 0; i < NUM_MATERIALS; i++) {
        if (numResourcesRemaining <= 0) {
          return;
        }

        Material m = Material.values()[(currentCategory + i) % NUM_MATERIALS];
        // Pick a random number of materials to assign here (at least 1)
        int numMaterials = r.nextInt(Math.min(NUM_PER_MATERIAL, numResourcesRemaining)) + 1;
        numResourcesRemaining -= numMaterials;
        newCount.put(m, Integer.toString(numMaterials));
      }
    }

    public String getOrganic() {
      return newCount.get(Material.ORGANIC);
    }

    public String getMineral() {
      return newCount.get(Material.MINERAL);
    }

    public String getSynthetic() {
      return newCount.get(Material.SYNTHETIC);
    }

    public String getExotic() {
      return newCount.get(Material.EXOTIC);
    }

    public String toString() {
      return newCount.toString();
    }
  }

  public static void main(String[] args) {
    Random r = new Random(0L);
    for (int i = 0; i < 100; i++) {
      FabPlan fp = new FabPlanCostRandomizer.FabPlan(4);
      fp.regenCost(r);
      System.out.println(fp);
    }
  }
}
