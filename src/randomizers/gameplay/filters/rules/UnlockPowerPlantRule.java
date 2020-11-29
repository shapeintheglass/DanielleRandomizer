package randomizers.gameplay.filters.rules;

public class UnlockPowerPlantRule extends BaseUnlockDoorsRule {

  private static final String[] TO_UNLOCK = { /* Reactor door */ "Door.BlastDoor_Medium4" };

  public UnlockPowerPlantRule() {
    super(TO_UNLOCK, "engineering/powersource");
  }
}
