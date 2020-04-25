package filters;

import java.io.File;
import java.util.Map;

public abstract class BaseFilter {

  String name;
  File tempDir;

  public BaseFilter(String name, File tempDir) {
    this.name = name;
    this.tempDir = tempDir;
  }

  public boolean filterEntityFile(String levelDir, File entityFile, Map<String, String> config) {
    throw new UnsupportedOperationException("Filter not implemented: " + name);
  }
}
