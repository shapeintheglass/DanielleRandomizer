package filters;

import java.util.ArrayList;
import java.util.List;

import rules.BaseRule;
import utils.XmlEntity;

public abstract class BaseFilter {

  List<BaseRule> rules;

  String name;

  public BaseFilter(String filterName) {
    this.name = filterName;
    rules = new ArrayList<>();
  }

  public void filterEntity(XmlEntity x, String levelDir) {
    for (BaseRule r : rules) {
      if (r.trigger(x, levelDir)) {
        r.apply(x, levelDir);
      }
    }
  }
}
