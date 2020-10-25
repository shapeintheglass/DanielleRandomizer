package randomizers.gameplay.level.filters.rules;

public class UnlockGutsRule extends BaseUnlockDoorsRule {

  private static final String[] TO_UNLOCK = { /* Maintenance tunnel door */ "Door.BlastDoor_Small6",
      "KeycardReader.KeycardReader_Default2" };

  public UnlockGutsRule() {
    super(TO_UNLOCK, "research/zerog_utilitytunnels");
  }
}
