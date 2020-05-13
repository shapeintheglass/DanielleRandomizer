package randomizers;

import java.util.Random;
import java.util.logging.Logger;

public abstract class BaseRandomizer {

  protected Logger logger;

  protected Random r;

  public BaseRandomizer(Random r) {
    this.r = r;
    this.logger = Logger.getGlobal();
  }
  
  public abstract void randomize();

}
