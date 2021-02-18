package randomizers.gameplay.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;
import randomizers.gameplay.filters.rules.Rule;

/**
 * Represents the implementation for a filter, which is a pre-made combination of rules.
 * 
 * @author Kida
 *
 */
public abstract class BaseFilter {

  protected List<Rule> rules;

  public BaseFilter() {
    rules = new ArrayList<>();
  }

  /**
   * Modifies the given entity in-place using a pre-built combination of rules.
   */
  public boolean filterEntity(Element e, Random r, String filename) {
    for (Rule rule : rules) {
      if (rule.trigger(e, r, filename)) {
        rule.apply(e, r, filename);
        return true;
      }
    }
    return false;
  }

  public BaseFilter addRule(Rule rule) {
    rules.add(rule);
    return this;
  }
}
