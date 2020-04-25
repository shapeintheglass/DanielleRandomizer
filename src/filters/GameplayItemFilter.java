package filters;

import java.nio.file.Path;

public class GameplayItemFilter extends BaseFilter {
  

  
  
  public GameplayItemFilter(String name, Path tempDir) {
    super(name, tempDir);
    // TODO Auto-generated constructor stub
  }

  public enum Options {
    
  }
  
  public enum GameplayItem {
    RECYCLER,
    FABRICATOR, // Won't touch the one in Morgan's office
    O2_STATION,
    
  }
}
