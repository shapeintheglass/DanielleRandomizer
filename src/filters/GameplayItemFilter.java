package filters;

public class GameplayItemFilter extends BaseFilter {
  
  public GameplayItemFilter() {
    super("GameplayItemFilter");
  }
  
  
  public enum Options {
    
  }
  
  public enum GameplayItem {
    RECYCLER,
    FABRICATOR, // Won't touch the one in Morgan's office
    O2_STATION,
    
  }
}
