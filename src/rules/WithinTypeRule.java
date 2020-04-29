package rules;

import java.util.Arrays;
import java.util.Set;

import utils.FileConsts;
import utils.XmlEntity;
import databases.EntityDatabase;
import databases.TagHelper;

public class WithinTypeRule extends BaseRule {

  // Tags to be shuffled, in priority order
  private static final String[] DEFAULT_SUPPORTED_TAGS = { "PICKUP" };

  private String[] supportedTags;
  EntityDatabase database;

  public WithinTypeRule() {
    this(EntityDatabase.getInstance());
  }

  // Visible for testing.
  public WithinTypeRule(EntityDatabase database) {
    this.database = database;
    this.supportedTags = DEFAULT_SUPPORTED_TAGS;
  }

  // Used to override the types that are supported
  public WithinTypeRule setSupportedTypes(String[] supportedTags) {
    this.supportedTags = supportedTags;
    return this;
  }

  @Override
  public boolean trigger(XmlEntity x, String levelDir) {
    // Return true if this archetype is supported
    return (getSupportedTag(x) != null);
  }

  @Override
  public void apply(XmlEntity x, String levelDir) {
    String tag = getSupportedTag(x);

    // Swap with a random entity in the same tag
    XmlEntity toSwap = database.getRandomEntityByTag(tag);
    String prefix = FileConsts.archetypeToPrefix(toSwap.getArchetype());
    x.setKey("Archetype", String.format("%s.%s", prefix, toSwap.getKey("Name")));
  }

  //
  private String getSupportedTag(XmlEntity x) {
    Set<String> tags = TagHelper.getTags(x.getKey("Archetype"), null);
    for (String s : supportedTags) {
      if (tags.contains(s)) {
        return s;
      }
    }
    return null;
  }
}
