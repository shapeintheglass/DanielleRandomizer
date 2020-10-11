package databases;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import utils.Utils;
import utils.XmlEntity;

/**
 * Interface for a basic database of xml entities.
 * @author Kida
 *
 */
public abstract class Database {

  // Map of string tag to a list of entities
  protected Map<String, List<String>> tagToNameList;
  protected Map<String, XmlEntity> nameToXmlEntity;
  private Random r;
  
  // Initializes with a specific entity database.
  // Visible for testing.
  public Database(Map<String, List<String>> tagToNameList,
      Map<String, XmlEntity> nameToXmlEntity) {
    // TODO: Have a single source of randomness
    r = new Random();
    this.tagToNameList = tagToNameList;
    this.nameToXmlEntity = nameToXmlEntity;
  }
  
  public Database() {
    r = new Random();
    populateDatabase();
  }
  
  protected abstract void populateDatabase();
  
  public XmlEntity getEntityByName(String name) {
    return nameToXmlEntity.get(name);
  }

  /*
   * Returns a random item from the entire database
   */
  public XmlEntity getRandomEntity() {
    return getRandomEntityFromList(tagToNameList.get("GLOBAL"));
  }

  /*
   * Returns a random item with the given tag
   */
  public XmlEntity getRandomEntityByTag(String tag) {
    return getRandomEntityFromList(tagToNameList.get(tag));
  }

  /*
   * Returns a random item within the given tags
   */
  public XmlEntity getRandomEntityByTag(String... tags) {
    // Choose a random tag, weighted by the number of elements in that tag
    int[] weights = new int[tags.length];
    for (int i = 0; i < tags.length; i++) {
      weights[i] = tagToNameList.get(tags[i]).size();
    }
    String tag = Utils.getRandomWeighted(tags, weights, r);
    return getRandomEntityByTag(tag);
  }

  private XmlEntity getRandomEntityFromList(List<String> list) {
    if (list == null || list.isEmpty()) {
      return null;
    }
    int index = r.nextInt(list.size());
    String name = list.get(index);
    return nameToXmlEntity.get(name);
  }

  /*
   * Removes a set of entities from the global pool
   */
  public void removeItemsFromPool(Set<String> toRemove) {
    for (String name : toRemove) {
      removeFromPool(name);
    }
  }

  // Removes a single entity from the pool
  public void removeFromPool(String toRemove) {
    // Find all tags for this entity
    Set<String> tags = TagHelper.getTags(toRemove, null);
    // Remove entity from the tag
    for (String tag : tags) {
      tagToNameList.get(tag).remove(toRemove);
    }
  }

  /*
   * Removes all entities from a tag list, except for the specified
   */
  public void removeAllItemsFromTagExcept(String tag, Set<XmlEntity> toKeep) {

  }

  public List<String> getAllForTag(String tag) {
    return tagToNameList.get(tag);
  }

}
