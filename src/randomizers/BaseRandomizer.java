package randomizers;

import java.util.logging.Logger;

import settings.Settings;

/**
 * Represents the interface for a randomizer.
 * @author Kida
 *
 */
public abstract class BaseRandomizer {
  // Logger
  protected Logger logger;

  String name;
  
  protected Settings settings;

  public BaseRandomizer(String name, Settings s) {
    this.settings = s;
    this.logger = Logger.getGlobal();
    this.name = name;
  }
  
  /**
   * Do randomization and write output into temp directory.
   */
  public abstract void randomize();

}
