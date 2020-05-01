package settings;

import java.util.List;
import java.util.Set;

// Global settings context so that we can coordinate changes across multiple files
public class Settings {
  
  private String installDir;
  
  private List<String> tagsToShuffle;
  private Set<String> tagsToIgnore;
  
  
  
  private Settings() {
    
  }
  
  
  
  
}
