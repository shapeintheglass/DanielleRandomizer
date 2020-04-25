package filters;

import java.nio.file.Path;

public class EnemyFilter extends BaseFilter {

  
  public EnemyFilter(String name, Path tempDir) {
    super(name, tempDir);
    // TODO Auto-generated constructor stub
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
