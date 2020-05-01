package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.FileConsts.Archetype;

public class XmlEntity {
  // Name of the tag defining this xml entity
  private String tagName = "";
  // Key value pairs in the header
  private Map<String, String> keys = new HashMap<>();
  // Used to preserve order of keys
  private List<String> keyOrder = new ArrayList<>();
  // Sub entities, in order
  private List<XmlEntity> subEntityOrder = new ArrayList<>();
  private int indentation = 0;
  private Archetype archetype;

  private static final String END_TAG_PATTERN = "</%s>";

  public XmlEntity(BufferedReader br) throws IOException {
    String line = br.readLine();
    // If line is blank, skip until content is reached
    while (line.trim().length() == 0) {
      line = br.readLine();
    }

    this.indentation = getIndentation(line);
    this.tagName = Utils.getTagName(line);
    this.keys = Utils.getKeysFromLine(line);
    this.keyOrder = Utils.getKeyOrder(line);

    if (Utils.isSingleLine(line)) {
      return;
    }

    String terminator = String.format(END_TAG_PATTERN, this.tagName);

    XmlEntity subEntity;
    while ((subEntity = parseFromStream(br, terminator)) != null) {
      addSubEntity(subEntity);
    }
  }

  public XmlEntity(String tagName, int indentation) {
    this(tagName, new HashMap<>(), new ArrayList<>(), indentation);
  }

  public XmlEntity(String tagName, Map<String, String> keys, List<String> keyOrder, int indentation) {
    this.tagName = tagName;
    this.keys = keys;
    this.indentation = indentation;
    this.keyOrder = keyOrder;
  }

  // Used to remember file archetype, if applicable
  public XmlEntity setArchetype(Archetype a) {
    this.archetype = a;
    return this;
  }

  public Archetype getArchetype() {
    return archetype;
  }

  public XmlEntity addSubEntity(XmlEntity subEntity) {
    subEntityOrder.add(subEntity);
    return this;
  }

  private static XmlEntity parseFromStream(BufferedReader br, String terminator) throws IOException {
    // get tag name and keys from first line
    String line = br.readLine();

    if (line == null || (terminator != null && line.contains(terminator))) {
      return null;
    }

    int indentation = getIndentation(line);
    String tagName = Utils.getTagName(line);

    if (tagName == null) {
      return null;
    }

    Map<String, String> keys = Utils.getKeysFromLine(line);
    List<String> keyOrder = Utils.getKeyOrder(line);
    XmlEntity newEntity = new XmlEntity(tagName, keys, keyOrder, indentation);

    if (Utils.isSingleLine(line)) {
      return newEntity;
    }

    // Get subentities until they come up null
    XmlEntity subEntity = parseFromStream(br, String.format(END_TAG_PATTERN, tagName));
    while (subEntity != null) {
      newEntity.addSubEntity(subEntity);
      subEntity = parseFromStream(br, String.format(END_TAG_PATTERN, tagName));
    }

    return newEntity;
  }

  private static int getIndentation(String line) {
    if (line.length() == 0) {
      return 0;
    }
    int index = 0;
    while (line.charAt(index) == ' ') {
      index++;
    }
    return index;
  }

  public String getTagName() {
    return tagName;
  }

  public Map<String, String> getKeys() {
    return keys;
  }

  public String getKey(String key) {
    return keys.get(key);
  }

  public void setKey(String key, String value) {
    keys.put(key, value);
  }
  
  public List<XmlEntity> getSubEntities() {
    return subEntityOrder;
  }

  public XmlEntity getSubEntityByTagName(String name) {
    for (XmlEntity x : subEntityOrder) {
      if (x.getTagName().equals(name)) {
        return x;
      }
    }
    return null;
  }

  public XmlEntity getSubEntityByKeyValue(String key, String value) {
    for (XmlEntity x : subEntityOrder) {
      if (x.getKey(key).equals(value)) {
        return x;
      }
    }
    return null;
  }

  public boolean writeToFile(File f) {
    return false;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    // Restore indentation
    String spaceBuffer = getSpaceBuffer(indentation);
    builder.append(spaceBuffer);

    // Start line header
    builder.append(String.format("<%s", tagName));
    for (String s : keyOrder) {
      builder.append(String.format(" %s=\"%s\"", s, keys.get(s)));
    }

    // Add subentities if applicable
    if (subEntityOrder.isEmpty()) {
      // Terminate here, add a space if there are no keys
      if (keys.isEmpty()) {
        builder.append(" />");
      } else {
        builder.append("/>");
      }
    } else {
      // Add close tag and add all sub entities
      builder.append(">\n");
      for (XmlEntity s : subEntityOrder) {
        builder.append(String.format("%s\n", s.toString()));
      }
      // Terminate here
      builder.append(spaceBuffer);
      builder.append(String.format(END_TAG_PATTERN, tagName));
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    XmlEntity other = (XmlEntity) o;
    return toString().equals(other.toString());
  }

  private String getSpaceBuffer(int indentation) {
    char[] spaceBuffer = new char[indentation];
    Arrays.fill(spaceBuffer, ' ');
    return String.copyValueOf(spaceBuffer);
  }
}
