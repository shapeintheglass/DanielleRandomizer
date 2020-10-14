package databases;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.Utils;
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
public class EntityDatabase extends TaggedDatabase {

  // Singleton
  private static EntityDatabase database;

  private static final int READ_AHEAD = 10000;

  // Creates or returns singleton instance.
  public static EntityDatabase getInstance(Random r) {
    if (database == null) {
      database = new EntityDatabase(r);
    }
    return database;
  }

  private EntityDatabase(Random r) {
    super(r);
  }

  protected void populateDatabase() {
    tagToNameList = new HashMap<>();
    nameToXmlEntity = new HashMap<>();
    allTags = new HashSet<>();

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
          Set<String> tags = Utils.getTags(x);
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

        br.mark(READ_AHEAD);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Random r = new Random();
    EntityDatabase d = getInstance(r);
    List<String> validTags = new ArrayList<>();
    d.allTags.stream().forEach(s -> validTags.add(s));
    List<String> validEntities = d.getAllForTag(GLOBAL_TAG);
    Collections.sort(validTags);
    Collections.sort(validEntities);
    
    File tagsFileCsv = new File("tagstoentities.csv");
    File entitiesFileCsv = new File("entitiestotags.csv");

    try (BufferedWriter tagsWriter = new BufferedWriter(new FileWriter(tagsFileCsv));
        BufferedWriter entitiesWriter = new BufferedWriter(new FileWriter(entitiesFileCsv))) {
      tagsWriter.write("tag,entries\n");
      entitiesWriter.write("entity,tags\n");
      
      for (String s : validTags) {
        List<String> entries = d.getAllForTag(s);
        Collections.sort(entries);
        tagsWriter.write(String.format("%s,\"%s\"\n", s, entries));
      };
      
      for (String s : validEntities) {
        XmlEntity entity = d.getEntityByName(s);
        List<String> tags = new ArrayList<>();
        tags.addAll(Utils.getTags(entity));
        Collections.sort(tags);
        entitiesWriter.write(String.format("%s,\"%s\"\n", s, tags));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    Scanner s = new Scanner(System.in);
    while (s.hasNext()) {
      String line = s.nextLine();
      List<String> entries = d.getAllForTag(line);
      if (entries == null) {
        System.out.println("invalid tag");
      } else {
        Collections.sort(entries);
        System.out.println(entries);
      }

    }
    s.close();
  }
}
