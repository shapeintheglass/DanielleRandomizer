package randomizers;

import java.util.Random;
import java.util.logging.Logger;

import json.SettingsJson;

/**
 * Represents the interface for a randomizer.
 * 
 * @author Kida
 *
 */
public abstract class BaseRandomizer {
  String name;

  protected SettingsJson settings;

  protected Random r;
  protected Logger logger;

  public BaseRandomizer(SettingsJson s) {
    this.settings = s;
    this.r = new Random(s.getSeed());
    this.logger = Logger.getGlobal();
  }

  /**
   * Do randomization and write output into temp directory.
   */
  public abstract void randomize();

}
