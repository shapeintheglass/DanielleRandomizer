package randomizers.gameplay.filters;

import com.google.common.collect.ImmutableMap;
import randomizers.gameplay.filters.rules.KeycardRule;

public class KeycardFilter extends BaseFilter {  
  public KeycardFilter(ImmutableMap<String, String> keycardConnectivity) {
    rules.add(new KeycardRule(keycardConnectivity));
  }
}
