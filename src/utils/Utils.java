package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
  private static final String KEY_PATTERN = "([^ ]*?)=\"(.*?)\"";
  private static final String TAG_PATTERN = "<(.*?)[ />]";
  private static final String SINGLE_LINE_TERMINATOR = "/>";

  public static String getTagName(String line) {
    Pattern p = Pattern.compile(TAG_PATTERN);
    Matcher m = p.matcher(line);
    if (m.find()) {
      return m.group(1);
    }
    return null;
  }

  public static boolean isSingleLine(String line) {
    return line.trim().endsWith(SINGLE_LINE_TERMINATOR);
  }

  public static HashMap<String, String> getKeysFromLine(String line) {
    HashMap<String, String> keyPairs = new HashMap<String, String>();
    Pattern p = Pattern.compile(KEY_PATTERN);

    Matcher m = p.matcher(line);
    while (m.find()) {
      keyPairs.put(m.group(1), m.group(2));
    }
    return keyPairs;
  }

  public static List<String> getKeyOrder(String line) {
    List<String> keyOrder = new ArrayList<>();
    Pattern p = Pattern.compile(KEY_PATTERN);

    Matcher m = p.matcher(line);
    while (m.find()) {
      keyOrder.add(m.group(1));
    }
    return keyOrder;
  }

  public static <T> T getRandomWeighted(T[] arr, int[] weights, Random r) {
    int sum = Arrays.stream(weights).reduce(0, Integer::sum);
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
    return arr[arr.length];
  }
  
  public static String getCommonElement(Collection<String> s1, Collection<String> s2) {
    for(String s : s1) {
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
}
