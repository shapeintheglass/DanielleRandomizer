package randomizers.gameplay.level.filters.rules;

import java.util.Arrays;

import org.jdom2.Element;

public class UnlockPsychotronicsRule implements Rule {
  /**
   * Door.Door_Decontamination_A1 Door.Door_Decontamination_A2
   * 
   * Door.ArkDoor_Sliding_Double_Default4 Door.ArkDoor_Sliding_Double_Default5
   */

  private static final String NAME_ATTRIBUTE = "Name";
  private static final String[] DOORS = { "Door.Door_Sliding_Double_Default18", "Door.Door_Sliding_Double_Default19",
      "Door.Door_Sliding_Double_Default20", "Door.Door_Sliding_Double_Default22", "Door.Door_Sliding_Double_Default24",
      "Door.Door_Sliding_Double_Default27", "Door.Door_Sliding_Double_Default28", "Door.Door_Sliding_Double_Default32",
      "Door.Door_Sliding_Double_Default33", "Door.Door_Sliding_Double_Large3", "Door.BlastDoor_Medium2",
      "Door.BlastDoor_Medium_NoAuto1" };

  public UnlockPsychotronicsRule() {
    Arrays.sort(DOORS);
  }

  @Override
  public boolean trigger(Element e) {
    return Arrays.binarySearch(DOORS, e.getAttributeValue(NAME_ATTRIBUTE)) >= 0 && e.getAttributeValue("Layer")
                                                                                    .equals(
                                                                                        "Pyschotronics_DataAnalysis");
  }

  @Override
  public void apply(Element e) {
    e.getChild("Properties2")
     .setAttribute("bStartsLocked", "0");
  }

}
