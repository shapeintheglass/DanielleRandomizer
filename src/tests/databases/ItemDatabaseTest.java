package tests.databases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import utils.Entity;
import databases.ItemDatabase;
import databases.ItemDatabase.ItemType;

public class ItemDatabaseTest {

  ItemDatabase database;

  private static final String[] JUNK_LIST = {"JUNK1", "JUNK2", "JUNK3"};
  
  private static final String[] FOOD_LIST = {"FOOD1", "FOOD2", "FOOD3"};
  
  
  @Before
  public void setup() {
    Map<ItemType, List<Entity>> fakeDatabase = new HashMap<>();
    
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
