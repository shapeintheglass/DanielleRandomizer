package databases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.LinkedListMultimap;

import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.Utils;
import utils.ZipHelper;

/**
 * 
 * Rough "database" implementation that can ingest entities from their
 * entityprototype definition files, tag them, and allow retrieval by tag.
 * 
 */
public class EntityDatabase extends TaggedDatabase {
  // Singleton
  private static EntityDatabase database;

  public EntityDatabase(ZipHelper zipHelper) {
    super(zipHelper);
  }
  
  // Creates or returns singleton instance.
  public static EntityDatabase getInstance(ZipHelper zipHelper) {
    if (database == null) {
      database = new EntityDatabase(zipHelper);
    }
    return database;
  }

  protected void populateDatabase(ZipHelper zipHelper) {
    tagToName = LinkedListMultimap.create();
    nameToElement = new HashMap<>();

    // Iterate through every entity archetype file
    for (Archetype a : Archetype.values()) {
      String file = FileConsts.getFileForArchetype(a);
      try {
        Document document = zipHelper.getDocument(file);
        Element root = document.getRootElement();
        List<Element> entities = root.getChildren("EntityPrototype");

        for (Element e : entities) {
          Set<String> tags = Utils.getTags(e);
          addToDatabase(e, tags);
        }

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (JDOMException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

    }
  }

  private void addToDatabase(Element e, Set<String> tags) {
    tags.add(GLOBAL_TAG);
    String name = e.getAttributeValue("Name");
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Could not find name for element " + e.toString());
    }
    for (String tag : tags) {
      tagToName.put(tag, name);
    }
    if (nameToElement.containsKey(name)) {
      throw new IllegalArgumentException("Duplicate name: " + name);
    }
    nameToElement.put(name, e);
  }

  public static void main(String[] args) {
    EntityDatabase d;
    try {
      d = getInstance(new ZipHelper(null, null));
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }
    List<String> validTags = new ArrayList<>();
    d.tagToName.keySet().stream().forEach(s -> validTags.add(s));
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
      }
      ;

      for (String s : validEntities) {
        Element entity = d.getEntityByName(s);
        List<String> tags = new ArrayList<>();
        tags.addAll(Utils.getTags(entity));
        Collections.sort(tags);
        entitiesWriter.write(String.format("%s,\"%s\"\n", s, tags));
      }
      System.out.printf("Wrote to %s, %s", tagsFileCsv.getCanonicalPath(), entitiesFileCsv.getCanonicalPath());
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
