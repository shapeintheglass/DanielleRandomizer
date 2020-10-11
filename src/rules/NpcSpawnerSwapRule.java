package rules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import databases.EntityDatabase;
import databases.TagHelper;
import utils.FileConsts;
import utils.FileConsts.Archetype;
import utils.Utils;
import utils.XmlEntity;

public class NpcSpawnerSwapRule implements Rule {

  EntityDatabase database;
  
  private Set<String> inputTags;
  private Set<String> outputTags;
  
  public NpcSpawnerSwapRule() {
    database = EntityDatabase.getInstance();
    this.inputTags = new HashSet<>();
    this.outputTags = new HashSet<>();
  }
  
  public NpcSpawnerSwapRule setInputTags(String... inputTags) {
    this.inputTags.addAll(Arrays.asList(inputTags));
    return this;
  }
  
  public NpcSpawnerSwapRule setOutputTags(String... outputTags) {
    this.outputTags.addAll(Arrays.asList(outputTags));
    return this;
  }

  @Override
  public boolean trigger(XmlEntity e) {
    // Triggers on ArkNpcSpawners
    if (e.hasKey("EntityClass") && e.getValue("EntityClass").equals("ArkNpcSpawner")) {
      Set<String> tags = getTagsForEntity(e);
      return Utils.getCommonElement(tags, inputTags) != null;
    } else {
      return false;
    }
  }

  @Override
  public void apply(XmlEntity e) {
    XmlEntity properties = e.getSubEntityByTagName("Properties");
    Optional<String> randomTag = outputTags.stream().findAny();
    XmlEntity toSwap = database.getRandomEntityByTag(randomTag.get());
    String prefix = FileConsts.archetypeToPrefix(toSwap.getArchetype());
    properties.setValue("sNpcArchetype", String.format("%s.%s", prefix, toSwap.getValue("Name")));
  }
  
  private Set<String> getTagsForEntity(XmlEntity e) {
    // Get the entity name
    XmlEntity properties = e.getSubEntityByTagName("Properties");
    String inputArchetype = properties.getValue("sNpcArchetype");
    // Use the entity name to retrieve it from the database
    int dotIndex = inputArchetype.indexOf('.');
    String entityName = inputArchetype.substring(dotIndex + 1);
    XmlEntity fullEntity = database.getEntityByName(entityName);
    Archetype a = fullEntity.getArchetype();
    return TagHelper.getTags(entityName, a);
  }
}
