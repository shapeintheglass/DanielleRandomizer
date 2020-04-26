package filters;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import rules.BaseRule;

public abstract class BaseFilter {

  List<BaseRule> rules;

  String name;
  Path tempDir;

  public BaseFilter(String name, Path tempDir) {
    this.name = name;
    this.tempDir = tempDir;
  }

  public boolean filterEntityFile(String levelDir, File entityFile,
      Map<String, String> config) {
    throw new UnsupportedOperationException("Filter not implemented: " + name);
  }
}
