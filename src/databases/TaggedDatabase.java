package databases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.XmlEntity;

/**
 * Interface for a basic database of xml entities.
 * 
 * @author Kida
 *
 */
public abstract class TaggedDatabase {

  // Map of string tag to a list of entities
  protected Map<String, List<String>> tagToNameList;
  protected Map<String, XmlEntity> nameToXmlEntity;
  protected Set<String> allTags;
  public static final String GLOBAL_TAG = "GLOBAL";

  public TaggedDatabase() {
    populateDatabase();
  }

  protected abstract void populateDatabase();
  
  protected void addToDatabase(XmlEntity x, Set<String> tags) {
    tags.add(GLOBAL_TAG);
    String name = x.getValue("Name");
    for (String tag : tags) {
      if (!allTags.contains(tag)) {
        allTags.add(tag);
        tagToNameList.put(tag, new ArrayList<String>());
      }
      tagToNameList.get(tag).add(name);
    }
    if (nameToXmlEntity.containsKey(name)) {
      throw new IllegalAccessError("Duplicate name: " + name);
    }
    nameToXmlEntity.put(name, x);
  }

  public XmlEntity getEntityByName(String name) {
    return nameToXmlEntity.get(name);
  }
  
  public List<String> getAllForTag(String tag) {
    return tagToNameList.get(tag);
  }
}
