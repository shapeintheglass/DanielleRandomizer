package filters;

import settings.Settings;

public class GameplayItemFilter extends BaseFilter {

  /**
   * Pre-made combination of rules that specifically filters enemies in certain settings.
   */
  public GameplayItemFilter(Settings s) {
    super("GameplayItemFilter", s);
    // TODO Auto-generated constructor stub
  }

  public enum Options {

  }

  public enum GameplayItem {
    RECYCLER, FABRICATOR, // Won't touch the one in Morgan's office
    O2_STATION,

  }
}
