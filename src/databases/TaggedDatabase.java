package databases;

import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import com.google.common.collect.LinkedListMultimap;

/**
 * Interface for a basic database where objects are assigned string tags
 *
 * @author Kida
 *
 */
public abstract class TaggedDatabase {

  // Map of string tag to a list of entities
  protected LinkedListMultimap<String, String> tagToName;
  protected Map<String, Element> nameToElement;
  public static final String GLOBAL_TAG = "GLOBAL";

  public TaggedDatabase() {
    populateDatabase();
  }

  /**
   * Adds all entries to database
   */
  protected abstract void populateDatabase();

  public Element getEntityByName(String name) {
    return nameToElement.get(name);
  }

  public List<String> getAllForTag(String tag) {
    return tagToName.get(tag);
  }
}
