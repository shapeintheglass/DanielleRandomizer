package tests.databases;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.XmlEntity;
import databases.EntityDatabase;

public class EntityDatabaseTest {

  EntityDatabase database;

  private static final String[] JUNK_LIST = { "JUNK1", "JUNK2", "JUNK3" };

  private static final String[] FOOD_LIST = { "FOOD1", "FOOD2", "FOOD3" };

  private static final String[] ALL_DATA = { "JUNK1", "JUNK2", "JUNK3",
      "FOOD1", "FOOD2", "FOOD3" };

  @Before
  public void setup() {
    Arrays.sort(ALL_DATA);
    Arrays.sort(JUNK_LIST);
    Arrays.sort(FOOD_LIST);
    Map<String, List<String>> fakeTagMap = new HashMap<>();
    fakeTagMap.put("JUNK", Arrays.asList(JUNK_LIST));
    fakeTagMap.put("FOOD", Arrays.asList(FOOD_LIST));
    fakeTagMap.put("GLOBAL", Arrays.asList(ALL_DATA));
    Map<String, XmlEntity> nameToEntityMap = new HashMap<>();
    for (String s : ALL_DATA) {
      nameToEntityMap.put(s, new XmlEntity(s, null, null, 0));
    }

    database = new EntityDatabase(fakeTagMap, nameToEntityMap);
  }

  @Test
  public void testGetRandomItem() {
    XmlEntity e = database.getRandomEntity();
    Assert.assertTrue(Arrays.binarySearch(ALL_DATA, e.getTagName()) >= 0);
  }

  @Test
  public void testGetRandomItemByType() {
    XmlEntity e = database.getRandomEntityByTag("FOOD");
    Assert.assertTrue(Arrays.binarySearch(FOOD_LIST, e.getTagName()) >= 0);
  }

  @Test
  public void testGetRandomItemByType_notInDatabase() {
    XmlEntity e = database.getRandomEntityByTag("OTHER");
    Assert.assertNull(e);
  }
}
