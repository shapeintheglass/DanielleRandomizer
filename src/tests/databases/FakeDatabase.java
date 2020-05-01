package tests.databases;

import java.util.ArrayList;
import java.util.HashMap;

import databases.Database;
import databases.EntityDatabase;
import utils.XmlEntity;

public class FakeDatabase extends Database {

  EntityDatabase database;

  private static final String[] JUNK_LIST = { "RecyclerJunk.1", "RecyclerJunk.2", "RecyclerJunk.3" };

  private static final String[] FOOD_LIST = { "Food.1", "Food.2", "Food.3" };

  private static final String[] ALL_DATA = { "RecyclerJunk.1", "RecyclerJunk.2", "RecyclerJunk.3", "Food.1", "Food.2",
      "Food.3" };

  @Override
  protected void populateDatabase() {
    tagToNameList = new HashMap<>();
    nameToXmlEntity = new HashMap<>();

    for (String s : ALL_DATA) {
      nameToXmlEntity.put(s, new XmlEntity(s, null, null, 0));
    }

    tagToNameList.put("JUNK", new ArrayList<>());
    for (String s : JUNK_LIST) {
      tagToNameList.get("JUNK").add(s);
    }

    tagToNameList.put("FOOD", new ArrayList<>());
    for (String s : FOOD_LIST) {
      tagToNameList.get("FOOD").add(s);
    }

  }
}
