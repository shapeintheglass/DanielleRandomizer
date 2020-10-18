package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Element;

public class Utils {
  public static <T> T getRandom(List<T> arr, Random r) {
    int index = r.nextInt(arr.size());
    return arr.get(index);
  }

  public static <T> T getRandomWeighted(List<T> arr, List<Integer> weights,
      Random r) {
    int sum = weights.stream().reduce(0, Integer::sum);
    if (sum == 0) {
      return null;
    }

    int index = r.nextInt(sum);

    int prev = 0;
    for (int i = 0; i < arr.size(); i++) {
      if (index >= prev && index < prev + weights.get(i)) {
        return arr.get(i);
      }
      prev += weights.get(i);
    }
    return arr.get(arr.size() - 1);
  }

  public static String getCommonElement(Collection<String> s1,
      Collection<String> s2) {
    for (String s : s1) {
      if (s2.contains(s)) {
        return s;
      }
    }
    return null;
  }

  public static void deleteDirectory(File dir) {
    if (dir.isDirectory()) {
      for (File f : dir.listFiles()) {
        deleteDirectory(f);
      }
    }
    dir.delete();
  }

  public static Set<String> getTags(Element e) {
    Set<String> tags = new HashSet<>();
    // Split name on dots
    String name = e.getAttributeValue("Name");
    String[] nameTags = name.split("\\.");
    Arrays.stream(nameTags).forEach(s -> tags.add(s));
    tags.add(e.getAttributeValue("Class"));
    tags.add(e.getAttributeValue("Library"));
    return tags;
  }

  public static String getNameForEntity(Element e) {
    return String.format("%s.%s", e.getAttributeValue("Library"),
        e.getAttributeValue("Name"));
  }

  public static String stripPrefix(String s) {
    int dotIndex = s.indexOf('.');
    return s.substring(dotIndex + 1);
  }
}
