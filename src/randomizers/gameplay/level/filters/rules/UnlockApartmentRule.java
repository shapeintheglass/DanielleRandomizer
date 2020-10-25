package randomizers.gameplay.level.filters.rules;

public class UnlockApartmentRule extends BaseUnlockDoorsRule {

  private static final String[] TO_UNLOCK = { /* Fake apartment doors */ "ApartmentDoor_Fake1", "ApartmentDoor_Fake3" };

  public UnlockApartmentRule() {
    super(TO_UNLOCK, "research/simulationlabs");
  }

}
