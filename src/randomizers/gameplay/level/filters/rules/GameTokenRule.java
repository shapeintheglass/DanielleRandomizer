package randomizers.gameplay.level.filters.rules;

import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.jdom2.Element;

/**
 * Update game tokens to the specified state.
 */
public class GameTokenRule implements Rule {

  // Map of key names to desired values.
  Map<String, String> gameTokenValues;

  public GameTokenRule(Map<String, String> values) {
    this.gameTokenValues = values;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    return gameTokenValues.containsKey(e.getAttributeValue("Name"));
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    String name = e.getAttributeValue("Name");
    String value = gameTokenValues.get(name);
    String oldValue = e.getAttributeValue("Value");
    e.setAttribute("Value", value);
    Logger.getGlobal().info(String.format("Updating game token %s from %s to %s", name, oldValue, value));
  }

}
