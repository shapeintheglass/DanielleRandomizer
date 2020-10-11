package rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import databases.Database;
import databases.EntityDatabase;
import databases.TagHelper;
import settings.Settings;
import utils.FileConsts;
import utils.XmlEntity;

/**
 * 
 * @author Kida
 *
 */
public class ShuffleWithinTypeRule implements Rule {
  // List of tags to be shuffled, in priority order.
  // Ex: If an item has both tag "A" and "B", and this is {"A", "B"}, the item
  // will be shuffled with another item of tag "A".
  private List<String> tagsToShuffle;
  
  // Set of tags to be ignored at all costs. If an item has any tag in this set,
  // it is ignored.
  private Set<String> tagsToIgnore;
  Database database;

  
  public ShuffleWithinTypeRule() {
    this(EntityDatabase.getInstance());
  }

  // Visible for testing.
  public ShuffleWithinTypeRule(Database database) {
    this.database = database;
    this.tagsToShuffle = new ArrayList<>();
    this.tagsToIgnore = new HashSet<>();
  }

  public ShuffleWithinTypeRule setTagsToShuffle(String... tagsToShuffle) {
    this.tagsToShuffle.addAll(Arrays.asList(tagsToShuffle));
    return this;
  }

  public ShuffleWithinTypeRule setTagsToIgnore(String... tagsToIgnore) {
    this.tagsToIgnore.addAll(Arrays.asList(tagsToIgnore));
    return this;
  }

  @Override
  public boolean trigger(XmlEntity x) {
    // Return true if this archetype is supported
    return (getSupportedTag(x) != null);
  }

  @Override
  public void apply(XmlEntity x) {
    String tag = getSupportedTag(x);

    // Swap with a random entity in the same tag, but not in the ignored tags
    XmlEntity toSwap = database.getRandomEntityByTag(tag);
    String prefix = FileConsts.archetypeToPrefix(toSwap.getArchetype());
    x.setValue("Archetype", String.format("%s.%s", prefix, toSwap.getValue("Name")));
  }

  // Gets the first compatible tag
  // Visible for testing.
  public String getSupportedTag(XmlEntity x) {
    Set<String> tags = TagHelper.getTags(x.getValue("Archetype"), null);

    // If this has any tags to be ignored, ignore.
    if (!Collections.disjoint(tags, tagsToIgnore)) {
      return null;
    }

    for (String s : tagsToShuffle) {
      if (tags.contains(s)) {
        return s;
      }
    }
    return null;
  }
}
