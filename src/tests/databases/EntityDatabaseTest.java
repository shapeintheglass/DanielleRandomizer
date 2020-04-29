package tests.databases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.Entity;
import databases.EntityDatabase;
import databases.EntityDatabase.EntityType;

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
    Map<EntityType, List<Entity>> fakeDatabase = new HashMap<>();
    fakeDatabase.put(EntityType.JUNK, createEntityList(JUNK_LIST));
    fakeDatabase.put(EntityType.FOOD, createEntityList(FOOD_LIST));

    database = new EntityDatabase(fakeDatabase);
  }

  @Test
  public void testGetRandomItem() {
    Entity e = database.getRandomItem();
    Assert.assertTrue(Arrays.binarySearch(ALL_DATA, e.getName()) >= 0);
  }
  
  @Test
  public void testGetRandomItemByType() {
    Entity e = database.getRandomItemByType(EntityType.FOOD);
    Assert.assertTrue(Arrays.binarySearch(FOOD_LIST, e.getName()) >= 0);
  }

  @Test
  public void testGetRandomItemByType_notInDatabase() {
    Entity e = database.getRandomItemByType(EntityType.OTHER);
    Assert.assertNull(e);
  }

  private List<Entity> createEntityList(String[] names) {
    List<Entity> list = new ArrayList<>();

    Arrays.stream(names).forEach((s) -> {
      Entity e = Entity.builder().setName(s).build();
      list.add(e);
    });
    return list;
  }

}
