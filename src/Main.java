

/**
 * 
 * Voice line randomizer.
 * 
 * Offers three options: Randomize within character, randomize within conversation type, or randomize across the board.
 * 
 * 1. Reads in all voice IDs and makes note of which character they belong to.
 * 2. (if randomizing within conversation) Reads in all conversation IDs and makes note of which character they belong to.
 * 3. Iterates through dialogic files line by line and makes an appropriate substitution.
 * 4. Packs the finished mod.
 *  
 * @author Kida
 *
 */
public class Main {

  private static final VoiceRandomizer.RandomizeType type = VoiceRandomizer.RandomizeType.WITHIN_CHARACTER;

  public static void main(String[] args) {
    
  }
}
