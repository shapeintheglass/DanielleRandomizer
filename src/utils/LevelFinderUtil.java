package utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * Helper utility for locating entities near a certain coordinate in a level.
 *
 */
public class LevelFinderUtil {

  private static final String MISSION_FILE = "data/levels/station/exterior/mission_mission0.xml";
  private static final String POS = "Pos";

  private static Map<String, Vector> readLevel(String levelFile) throws JDOMException, IOException {
    File level = new File(levelFile);
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(level);
    Element root = document.getRootElement().getChild("Objects");
    Map<String, Vector> nameToPos = Maps.newHashMap();

    for (Element e : root.getChildren()) {
      if (e.getAttributeValue(POS) != null && e.getAttributeValue("Archetype") != null) {
        Vector c = new Vector(e.getAttributeValue(POS));
        String name = e.getAttributeValue("Name") + " " + e.getAttributeValue("Archetype");
        nameToPos.put(name, c);
      }
    }
    return nameToPos;
  }

  public static void printCloseMatches(Map<String, Vector> nameToPos, Vector c, float r) {
    List<Double> sortedKeys = Lists.newArrayList();
    Map<Double, String> map = Maps.newHashMap();
    Map<Double, Vector> magToDiff = Maps.newHashMap();
    for (String s : nameToPos.keySet()) {
      Vector otherCoord = nameToPos.get(s);
      Vector diff = otherCoord.subtract(c);
      double mag = diff.magnitude();
      if (mag < r) {
        if (!map.containsKey(mag)) {
          map.put(mag, s);
        } else {
          while (!map.containsKey(mag)) {
            mag += 0.01;
          }
          map.put(mag, s);
        }
        sortedKeys.add(mag);
        magToDiff.put(mag, diff);
      }
    }
    Collections.sort(sortedKeys);
    for (Double dist : sortedKeys) {
      System.out.printf("%.5f (%s)\t%s\n", dist, magToDiff.get(dist), map.get(dist));
    }
  }


  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {

      Map<String, Vector> nameToPos = readLevel(MISSION_FILE);

      while (s.hasNextLine()) {
        // x y z r
        String line = s.nextLine();
        String[] tokens = line.split(",");
        Vector c = new Vector(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
            Float.parseFloat(tokens[2]));
        printCloseMatches(nameToPos, c, Float.parseFloat(tokens[3]));
      }
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }

  public static class Vector {
    float x, y, z;

    public Vector(String pos) {
      String[] tokens = pos.split(",");
      x = Float.parseFloat(tokens[0]);
      y = Float.parseFloat(tokens[1]);
      z = Float.parseFloat(tokens[2]);
    }

    public Vector(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public float getX() {
      return x;
    }

    public float getY() {
      return y;
    }

    public float getZ() {
      return z;
    }

    public Vector subtract(Vector other) {
      return new Vector(this.x - other.getX(), this.y - other.getY(), this.z - other.getZ());
    }

    public double magnitude() {
      return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    @Override
    public String toString() {
      return String.format("%.2f, %.2f, %.2f", x, y, z);
    }
  }
}
