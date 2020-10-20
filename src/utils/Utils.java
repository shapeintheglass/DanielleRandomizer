package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jdom2.Element;

public class Utils {
  public static <T> T getRandom(List<T> arr, Random r) {
    int index = r.nextInt(arr.size());
    return arr.get(index);
  }

  public static <T> T getRandom(T[] arr, Random r) {
    int index = r.nextInt(arr.length);
    return arr[index];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, Random r) {
    int index = r.nextInt(arr1.length + arr2.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    return arr2[index - arr1.length];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, T[] arr3, Random r) {
    int index = r.nextInt(arr1.length + arr2.length + arr3.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    return arr3[index - arr1.length - arr2.length];
  }

  public static <T> T getRandom(T[] arr1, T[] arr2, T[] arr3, T[] arr4, Random r) {
    int index = r.nextInt(arr1.length + arr2.length + arr3.length + arr4.length);
    if (index < arr1.length) {
      return arr1[index];
    }
    if (index < arr1.length + arr2.length) {
      return arr2[index - arr1.length];
    }
    if (index < arr1.length + arr2.length + arr3.length) {
      return arr3[index - arr1.length - arr2.length];
    }
    return arr3[index - arr1.length - arr2.length - arr3.length];
  }

  public static <T> T getRandomWeighted(T[] arr, Integer[] weights, Random r) {
    int sum = Arrays.stream(weights)
                    .reduce(0, Integer::sum);
    if (sum == 0) {
      return null;
    }

    int index = r.nextInt(sum);

    int prev = 0;
    for (int i = 0; i < arr.length; i++) {
      if (index >= prev && index < prev + weights[i]) {
        return arr[i];
      }
      prev += weights[i];
    }
    return arr[arr.length - 1];
  }

  public static <T> T getRandomWeighted(List<T> arr, List<Integer> weights, Random r) {
    int sum = weights.stream()
                     .reduce(0, Integer::sum);
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

  public static String getCommonElement(Collection<String> s1, Collection<String> s2) {
    for (String s : s1) {
      if (s2.contains(s)) {
        return s;
      }
    }
    return null;
  }

  public static void copyDirectory(File fromDir, Path toDir) throws IOException {
    if (fromDir.isDirectory()) {
      for (File f : fromDir.listFiles()) {
        toDir.resolve(f.getName()).toFile().mkdir();
        copyDirectory(f, toDir.resolve(f.getName()));
      }
    } else {
      Files.copy(fromDir.toPath(), toDir, StandardCopyOption.REPLACE_EXISTING);
    }
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
    Arrays.stream(nameTags)
          .forEach(s -> tags.add(s));
    tags.add(e.getAttributeValue("Class"));
    tags.add(e.getAttributeValue("Library"));
    return tags;
  }

  public static String getNameForEntity(Element e) {
    return String.format("%s.%s", e.getAttributeValue("Library"), e.getAttributeValue("Name"));
  }

  public static String stripPrefix(String s) {
    int dotIndex = s.indexOf('.');
    return s.substring(dotIndex + 1);
  }
}
