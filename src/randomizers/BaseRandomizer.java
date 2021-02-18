package randomizers;

import java.util.Random;
import java.util.logging.Logger;

import json.SettingsJson;
import utils.ZipHelper;

/**
 * Represents the interface for a randomizer.
 * 
 * @author Kida
 *
 */
public abstract class BaseRandomizer {
  String name;

  protected SettingsJson settings;
  protected ZipHelper zipHelper;
  
  protected Random r;
  protected Logger logger;
  

  public BaseRandomizer(SettingsJson s, ZipHelper zipHelper) {
    this.settings = s;
    this.zipHelper = zipHelper;
    this.r = new Random(s.getSeed());
    this.logger = Logger.getLogger("randomizer");
  }

  /**
   * Do randomization and write output into temp directory.
   */
  public abstract void randomize();

}
