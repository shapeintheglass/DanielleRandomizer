package randomizers;

import java.util.Random;
import java.util.logging.Logger;

import proto.RandomizerSettings.Settings;
import utils.Utils;
import utils.ZipHelper;

/**
 * Represents the interface for a randomizer.
 * 
 * @author Kida
 *
 */
public abstract class BaseRandomizer {
  String name;

  protected Settings settings;
  protected ZipHelper zipHelper;
  
  protected Random r;
  protected Logger logger;
  

  public BaseRandomizer(Settings s, ZipHelper zipHelper) {
    this.settings = s;
    this.zipHelper = zipHelper;
    this.r = new Random(Utils.stringToLong(s.getSeed()));
    this.logger = Logger.getLogger("randomizer");
  }

  /**
   * Do randomization and write output into temp directory.
   */
  public abstract void randomize();

}
