package databases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.XmlEntity;

/**
 * 
 * Rough "database" implementation that can ingest entities from their
 * entityprototype definition files, tag them, and allow retrieval by tag.
 * 
 * The database is also mutable, so entities within a particular tag can be
 * modified. This allows features such as overriding the "typhon" category to
 * only contain nightmares.
 *
 */
public class EntityDatabase extends Database {

  // Singleton
  private static EntityDatabase database;

  private static final int READ_AHEAD = 10000;

  // Creates or returns singleton instance.
  public static EntityDatabase getInstance() {
    if (database == null) {
      database = new EntityDatabase();
    }
    return database;
  }

  // Initializes with a specific entity database.
  // Visible for testing.
  public EntityDatabase(Map<String, List<String>> tagToNameList, Map<String, XmlEntity> nameToXmlEntity) {
    super(tagToNameList, nameToXmlEntity);
  }

  public EntityDatabase() {
    super();
  }

  protected void populateDatabase() {
    tagToNameList = new HashMap<>();
    nameToXmlEntity = new HashMap<>();
    // Pre-populate valid tags
    for (String[] tagList : TagHelper.TAG_LIST_LIST) {
      for (String tag : tagList) {
        tagToNameList.put(tag, new ArrayList<String>());
      }
    }
    for (Archetype a : Archetype.values()) {
      getEntitiesFromFile(a);
    }
  }

  private void getEntitiesFromFile(Archetype a) {
    String file = FileConsts.getFileForArchetype(a);
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      br.mark(READ_AHEAD);
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains("<EntityPrototype ")) {
          br.reset();

          XmlEntity x = new XmlEntity(br).setArchetype(a);
          String name = x.getKey("Name");
          Set<String> tags = TagHelper.getTags(name, a);
          for (String tag : tags) {
            if (!tagToNameList.containsKey(tag)) {
              throw new IllegalAccessError("Unknown tag: " + tag);
            }

            tagToNameList.get(tag).add(name);
          }
          if (nameToXmlEntity.containsKey(name)) {
            throw new IllegalAccessError("Duplicate name: " + name);
          }
          nameToXmlEntity.put(name, x);
        }

        br.mark(READ_AHEAD);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
