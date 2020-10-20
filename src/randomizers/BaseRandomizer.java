package randomizers;

import java.util.Random;
import java.util.logging.Logger;

import settings.Settings;

/**
 * Represents the interface for a randomizer.
 * 
 * @author Kida
 *
 */
public abstract class BaseRandomizer {
  String name;

  protected Settings settings;

  protected Random r;
  protected Logger logger;

  public BaseRandomizer(Settings s) {
    this.settings = s;
    this.r = settings.getRandom();
    this.logger = Logger.getGlobal();
  }

  /**
   * Do randomization and write output into temp directory.
   */
  public abstract void randomize();

}
