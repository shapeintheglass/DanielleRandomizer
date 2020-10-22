package randomizers.gameplay.level.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import randomizers.gameplay.level.filters.rules.Rule;

/**
 * Represents the implementation for a filter, which is a pre-made combination
 * of rules.
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
  public void filterEntity(Element e, Random r) {
    for (Rule rule : rules) {
      if (rule.trigger(e, r)) {
        rule.apply(e, r);
        return;
      }
    }
  }

  public BaseFilter addRule(Rule rule) {
    rules.add(rule);
    return this;
  }
}
