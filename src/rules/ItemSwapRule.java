package rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import databases.EntityDatabase;
import databases.TagHelper;
import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.Utils;
import utils.XmlEntity;

public class ItemSwapRule implements Rule {

  EntityDatabase database;

  private List<String> inputTags;
  private List<String> outputTags;
  private List<String> doNotTouchTags;
  
  public ItemSwapRule() {
    database = EntityDatabase.getInstance();
    this.inputTags = new ArrayList<>();
    this.outputTags = new ArrayList<>();
    this.doNotTouchTags = new ArrayList<>();
  }
  
  public ItemSwapRule setInputTags(String... inputTags) {
    this.inputTags.addAll(Arrays.asList(inputTags));
    return this;
  }
  
  public ItemSwapRule setOutputTags(String... outputTags) {
    this.outputTags.addAll(Arrays.asList(outputTags));
    return this;
  }
  
  public ItemSwapRule setDoNotTouchTags(String... doNotTouchTags) {
    this.doNotTouchTags.addAll(Arrays.asList(doNotTouchTags));
    return this;
  }

  @Override
  public boolean trigger(XmlEntity e) {
    // Check if input tag matches
    if (e.hasKey("Archetype")) {
      List<String> tags = getTagsForEntity(e);
      if (tags == null) {
        return false;
      }
      return Utils.getCommonElement(tags, inputTags) != null && Utils.getCommonElement(tags, doNotTouchTags) == null;
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    Collections.shuffle(outputTags);
    String randomTag = outputTags.get(0);
    XmlEntity toSwap = database.getRandomEntityByTag(randomTag);
    String prefix = FileConsts.archetypeToPrefix(toSwap.getArchetype());
    e.setValue("Archetype", String.format("%s.%s", prefix, toSwap.getValue("Name")));
  }
  
  private List<String> getTagsForEntity(XmlEntity e) {
    String archetype = e.getValue("Archetype");
    // Use the entity name to retrieve it from the database
    int dotIndex = archetype.indexOf('.');
    String entityName = archetype.substring(dotIndex + 1);
    XmlEntity fullEntity = database.getEntityByName(entityName);
    if (fullEntity == null) {
      return null;
    }
    Archetype a = fullEntity.getArchetype();
    ArrayList<String> tags = new ArrayList<>();
    tags.addAll(TagHelper.getTags(entityName, a));
    return tags;
  }

}
