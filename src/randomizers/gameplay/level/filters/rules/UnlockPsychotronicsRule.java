package randomizers.gameplay.level.filters.rules;

public class UnlockPsychotronicsRule extends BaseUnlockDoorsRule {
  private static final String[] TO_UNLOCK =
      {
          "Door.ArkDoor_Sliding_Double_Default5", // Decontamination airlock, grav shaft side
          "Door.ArkDoor_Sliding_Double_Default4", // Decontamination airlock, locker room side
          "Door.Door_Sliding_Double_Default18", // Live exam morgue
          "Door.Door_Sliding_Double_Default32" // Material extraction
      };

  public UnlockPsychotronicsRule() {
    super(TO_UNLOCK, "research/psychotronics");
  }
}
