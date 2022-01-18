package randomizers.gameplay.filters.rules;

public class UnlockMaintenanceAccessRule extends BaseUnlockDoorsRule {
  private static final String[] TO_UNLOCK =
      {
          "Door.BlastDoor_Small6" // Maintenance access hatch
      };

  public UnlockMaintenanceAccessRule() {
    super(TO_UNLOCK, "research/zerog_utilitytunnels");
  }
}
