package filters;

import java.util.ArrayList;
import java.util.List;

import rules.Rule;
import settings.Settings;
import utils.XmlEntity;

/**
 * Represents the implementation for a filter, which is a pre-made combination of rules.
 * @author Kida
 *
 */
public abstract class BaseFilter {

  List<Rule> rules;

  String name;
  Settings settings;

  /**
   * Initializes a filter with the given name and settings
   * @param filterName
   * @param s
   */
  public BaseFilter(String filterName, Settings s) {
    this.name = filterName;
    this.settings = s;
    rules = new ArrayList<>();
  }

  /**
   * Modifies the given entity in-place using a pre-built combination of rules.
   * @param x
   * @param levelDir
   */
  public void filterEntity(XmlEntity x) {
    for (Rule r : rules) {
      if (r.trigger(x)) {
        r.apply(x);
        return;
      }
    }
  }
  
  public BaseFilter addRule(Rule r) {
    rules.add(r);
    return this;
  }
}
