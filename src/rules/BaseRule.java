package rules;

import java.io.File;
import java.nio.file.Path;

import utils.Entity;

public abstract class BaseRule {
  public BaseRule() {

  }
  
  // Given an entity config, whether this rule should apply
  public boolean trigger(Entity e) {
    return false;
  }
  
  // Make necessary change to this entity file. TempDir is provided for scratch changes.
  public void apply(File entityFile, Path tempDir) {
    
  }
}
