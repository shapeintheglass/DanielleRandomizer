package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Element;

import databases.TaggedDatabase;

public class DatabaseUtils {

  /*
   * Returns a random item with the given tag.
   */
  public static Element getRandomEntityByTag(TaggedDatabase database, Random r, String tag) {
    List<String> entities = database.getAllForTag(tag);
    String selected = Utils.getRandom(entities, r);
    return database.getEntityByName(selected);
  }

  /*
   * Returns a random item within the given tags, weighted
   */
  public static Element getRandomEntityByTags(TaggedDatabase database, Random r, List<String> tags,
      List<Integer> weights) {
    // Sanity check the weights, generate even distribution if not valid
    if (weights == null || weights.size() != tags.size()) {
      weights = new ArrayList<>(tags.size());
      for (int i = 0; i < tags.size(); i++) {
        weights.add(1);
      }
    }
    String tag = Utils.getRandomWeighted(tags, weights, r);
    return getRandomEntityByTag(database, r, tag);
  }
}
