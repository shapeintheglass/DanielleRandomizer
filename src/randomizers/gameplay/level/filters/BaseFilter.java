package randomizers.gameplay.level.filters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.StartElement;

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
  public void filterEntity(StartElement x) {
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
