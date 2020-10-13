package databases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import utils.Utils;
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
  protected Random r;

  public TaggedDatabase(Random r) {
    this.r = r;
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
    return getRandomEntityFromList(tagToNameList.get(GLOBAL_TAG));
  }

  /*
   * Returns a random item with the given tag.
   */
  public XmlEntity getRandomEntityByTag(String tag) {
    XmlEntity e = getEntityByName(tag);
    if (e != null) {
      return e;
    }
    return getRandomEntityFromList(tagToNameList.get(tag));
  }

  /*
   * Returns a random item within the given tags
   */
  public XmlEntity getRandomEntityByTag(List<String> tags, List<Integer> weights) {
    // Choose a random tag, weighted by the number of elements in that tag
    if (weights == null || weights.size() != tags.size()) {
      weights = new ArrayList<>(tags.size());
      for (int i = 0; i < tags.size(); i++) {
        weights.add(1);
      }
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

  public List<String> getAllForTag(String tag) {
    return tagToNameList.get(tag);
  }

}
