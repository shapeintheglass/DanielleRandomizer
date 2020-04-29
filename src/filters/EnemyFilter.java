package filters;


public class EnemyFilter extends BaseFilter {

  
  public EnemyFilter() {
    super("EnemyFilter");
  }

  public enum Options {
    NO_LOGIC, // Randomize with no regard for type, including humans
    WITHIN_TYPE, // Randomize within type (robot, alien, human)
    ALL_NIGHTMARES,
  }
  
  public enum EnemyType {
    NIGHTMARE,
  }
  
  public enum SpawnRarity {
    PLENTIFUL, // 20% chance
    SPARSE,    // 5% chance
    NONE,      // 0% chance
  }
}
