package databases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

/**
 * Interface for a basic database where objects are assigned string tags
 *
 * @author Kida
 *
 */
public abstract class TaggedDatabase {

  // Map of string tag to a list of entities
  protected Map<String, List<String>> tagToNameList;
  protected Map<String, Element> nameToElement;
  protected Set<String> allTags;
  public static final String GLOBAL_TAG = "GLOBAL";

  public TaggedDatabase() {
    populateDatabase();
  }

  /**
   * Adds all entries to database
   */
  protected abstract void populateDatabase();

  protected void addToDatabase(Element e, Set<String> tags) {
    tags.add(GLOBAL_TAG);
    String name = e.getAttribute("Name");
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Could not find name for element "
          + e.toString());
    }
    for (String tag : tags) {
      if (!allTags.contains(tag)) {
        allTags.add(tag);
        tagToNameList.put(tag, new ArrayList<String>());
      }
      tagToNameList.get(tag).add(name);
    }
    if (nameToElement.containsKey(name)) {
      throw new IllegalArgumentException("Duplicate name: " + name);
    }
    nameToElement.put(name, e);
  }

  public Element getEntityByName(String name) {
    return nameToElement.get(name);
  }

  public List<String> getAllForTag(String tag) {
    return tagToNameList.get(tag);
  }
}
