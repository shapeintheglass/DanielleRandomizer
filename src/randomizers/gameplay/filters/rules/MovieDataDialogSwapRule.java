package randomizers.gameplay.filters.rules;

import java.util.Map;
import java.util.Random;

import org.jdom2.Element;

public class MovieDataDialogSwapRule implements Rule {
  
  Map<String, String> swappedLinesMap;
  
  public MovieDataDialogSwapRule(Map<String, String> swappedLinesMap) {
    this.swappedLinesMap = swappedLinesMap;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // TODO Auto-generated method stub
    
  }

}
