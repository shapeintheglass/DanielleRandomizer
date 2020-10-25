package randomizers.gameplay.level.filters.rules;

public class UnlockPsychotronicsRule extends BaseUnlockDoorsRule {

  // TODO: Remove unecessary doors
  private static final String[] TO_UNLOCK = { "Door.Door_Sliding_Double_Default18", "Door.Door_Sliding_Double_Default19",
      "Door.Door_Sliding_Double_Default20", "Door.Door_Sliding_Double_Default22", "Door.Door_Sliding_Double_Default24",
      "Door.Door_Sliding_Double_Default27", "Door.Door_Sliding_Double_Default28", "Door.Door_Sliding_Double_Default32",
      "Door.Door_Sliding_Double_Default33", "Door.Door_Sliding_Double_Large3", "Door.BlastDoor_Medium2",
      "Door.BlastDoor_Medium_NoAuto1" };

  public UnlockPsychotronicsRule() {
    super(TO_UNLOCK, "research/psychotronics");
  }
}
