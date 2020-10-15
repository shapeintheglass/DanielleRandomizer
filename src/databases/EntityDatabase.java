package databases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.Utils;

/**
 * 
 * Rough "database" implementation that can ingest entities from their
 * entityprototype definition files, tag them, and allow retrieval by tag.
 * 
 */
public class EntityDatabase extends TaggedDatabase {
  // Singleton
  private static EntityDatabase database;

  // Creates or returns singleton instance.
  public static EntityDatabase getInstance() {

    if (database == null) {
      database = new EntityDatabase();
    }
    return database;
  }

  protected void populateDatabase() {
    tagToNameList = new HashMap<>();
    nameToElement = new HashMap<>();
    allTags = new HashSet<>();

    // Iterate through every entity archetype file
    for (Archetype a : Archetype.values()) {
      String file = FileConsts.getFileForArchetype(a);
      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("EntityPrototype");
        for (int i = 0; i < nodeList.getLength(); i++) {
          Element e = (Element) nodeList.item(i);
          Set<String> tags = Utils.getTags(e);
          addToDatabase(e, tags);
        }

      } catch (ParserConfigurationException | SAXException | IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }

  public static void main(String[] args) {
    EntityDatabase d = getInstance();
    List<String> validTags = new ArrayList<>();
    d.allTags.stream().forEach(s -> validTags.add(s));
    List<String> validEntities = d.getAllForTag(GLOBAL_TAG);
    Collections.sort(validTags);
    Collections.sort(validEntities);

    File tagsFileCsv = new File("tagstoentities.csv");
    File entitiesFileCsv = new File("entitiestotags.csv");

    try (BufferedWriter tagsWriter = new BufferedWriter(new FileWriter(
        tagsFileCsv));
        BufferedWriter entitiesWriter = new BufferedWriter(new FileWriter(
            entitiesFileCsv))) {
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
      System.out.printf("Wrote to %s, %s", tagsFileCsv.getCanonicalPath(),
          entitiesFileCsv.getCanonicalPath());
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
