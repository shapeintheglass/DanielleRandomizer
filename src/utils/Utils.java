package utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
  private static final String KEY_PATTERN = "([A-Za-z_]*?)=\"([A-Za-z0-9&;,-:_/ ]*?)\"";

  public static HashMap<String, String> getKeysFromLine(String line) {
    HashMap<String, String> keyPairs = new HashMap<String, String>();
    Pattern p = Pattern.compile(KEY_PATTERN);

    Matcher m = p.matcher(line);
    while (m.find()) {
      keyPairs.put(m.group(1).toLowerCase(), m.group(2));
    }
    return keyPairs;
  }
}
