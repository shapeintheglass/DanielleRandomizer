package randomizers.gameplay.level.filters;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import randomizers.gameplay.level.filters.rules.Rule;
import settings.Settings;

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
  public void filterEntity(Element e) {
    for (Rule r : rules) {
      if (r.trigger(e)) {
        r.apply(e);
        return;
      }
    }
  }
  
  public BaseFilter addRule(Rule r) {
    rules.add(r);
    return this;
  }
}
