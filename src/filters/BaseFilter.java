package filters;

import java.util.List;

import rules.BaseRule;
import utils.XmlEntity;

public abstract class BaseFilter {

  List<BaseRule> rules;

  String name;

  public BaseFilter(String filterName) {
    this.name = filterName;
  }

  public boolean filterEntity(XmlEntity x, String levelDir) {
    throw new UnsupportedOperationException("Filter not implemented: " + name);
  }
}
