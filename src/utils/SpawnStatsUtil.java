package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import databases.EntityDatabase;
import databases.TaggedDatabase;
import json.AllPresetsJson;
import json.GenericRuleJson;
import json.SpawnPresetJson;
import randomizers.gameplay.level.filters.rules.ArchetypeSwapRule;
import randomizers.gameplay.level.filters.rules.NpcSpawnerSwapRule;
import randomizers.gameplay.level.filters.rules.Rule;

/**
 * 
 * Helper utility for sanity checking generated spawn rates.
 *
 */
public class SpawnStatsUtil {

  public static final String LEVEL_DIR = "data/levels";
  public static final String LEVEL_FILE = "research/lobby";
  public static final String LEVEL_FILE_NAME = "mission_mission0.xml";
  public static final String PRESETS_FILE = "settings.json";

  private static TaggedDatabase database;

  public static Element getEntitiesFromLevel(File level) throws JDOMException, IOException {
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(level);
    return document.getRootElement().getChild("Objects");
  }

  public static List<Rule> createItemRulesList(List<GenericRuleJson> rules) {
    List<Rule> rulesList = Lists.newArrayList();
    for (GenericRuleJson grj : rules) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS);
      grj.addDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rulesList.add(new ArchetypeSwapRule(database, crh));
    }
    return rulesList;
  }

  public static List<Rule> createNpcRulesList(List<GenericRuleJson> rules) {
    List<Rule> rulesList = Lists.newArrayList();
    for (GenericRuleJson grj : rules) {
      grj.addDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS);
      grj.addDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS);
      CustomRuleHelper crh = new CustomRuleHelper(grj);
      rulesList.add(new NpcSpawnerSwapRule(database, crh));
    }
    return rulesList;
  }

  private static List<String> randomizeEntity(Element e, List<Rule> rulesList, Random r) {
    boolean modified = false;
    for (Rule rule : rulesList) {
      if (rule.trigger(e, r, LEVEL_FILE)) {
        modified = true;
        rule.apply(e, r, LEVEL_FILE);
        break;
      }
    }
    if (modified) {
      return getTagsForEntity(e);
    }
    return null;
  }

  public static List<String> getTagsForEntity(Element e) {
    String tag = null;
    // Handle npc spawner differently
    if (e.getAttributeValue("EntityClass") != null
        && e.getAttributeValue("EntityClass").equals("ArkNpcSpawner")) {
      tag = e.getChild("Properties").getAttributeValue("sNpcArchetype");
    } else {
      tag = e.getAttributeValue("Archetype") != null ? e.getAttributeValue("Archetype")
          : e.getAttributeValue("EntityClass");
    }

    List<String> toReturn = Lists.newArrayList();

    if (tag == null) {
      return toReturn;
    }

    String[] tokens = tag.split("\\.");
    StringBuilder builder = new StringBuilder();
    builder.append(tokens[0]);
    toReturn.add(builder.toString());
    for (int i = 1; i < tokens.length; i++) {
      builder.append('.').append(tokens[i]);
      toReturn.add(builder.toString());
    }
    return toReturn;
  }

  public static void describeRandomization(SpawnPresetJson j, List<Rule> rulesList,
      Multiset<String> originalTags) throws JDOMException, IOException {
    Element root = getEntitiesFromLevel(
        Paths.get(LEVEL_DIR).resolve(LEVEL_FILE).resolve(LEVEL_FILE_NAME).toFile());
    System.out.println(j.getName());
    System.out.println(j.getDesc());

    Multiset<String> tagsSet = HashMultiset.create();

    Random r = new Random();
    long seed = r.nextLong();
    System.out.printf("Seed: %d\n", seed);
    r.setSeed(seed);
    int numModified = 0;
    int total = 0;
    for (Element e : root.getChildren()) {
      List<String> tags = randomizeEntity(e, rulesList, r);
      if (tags != null) {
        tagsSet.addAll(tags);
        numModified++;
      }
      total++;
    }

    List<String> tags = Lists.newArrayList();
    tags.addAll(tagsSet.elementSet());
    Collections.sort(tags);

    System.out.printf("%d/%d entities modified.\n", numModified, total);
    System.out.println("Tag,count,percent,count diff,percent diff");
    for (String s : tags) {
      int count = tagsSet.count(s);
      float percent = (float) count / numModified * 100;
      int countDiff = count - originalTags.count(s);
      float percentDiff = (float) countDiff / originalTags.count(s) * 100;
      System.out.printf("%s,%d,%.2f%%,%d,%.2f%%\n", s, count, percent, countDiff, percentDiff);
    }
  }

  public static Multiset<String> getOriginalState() throws JDOMException, IOException {
    Element root = getEntitiesFromLevel(
        Paths.get(LEVEL_DIR).resolve(LEVEL_FILE).resolve(LEVEL_FILE_NAME).toFile());

    Multiset<String> tagsSet = HashMultiset.create();

    Random r = new Random();
    long seed = r.nextLong();
    System.out.printf("Seed: %d\n", seed);
    r.setSeed(seed);
    for (Element e : root.getChildren()) {
      List<String> tags = getTagsForEntity(e);
      if (tags != null) {
        tagsSet.addAll(tags);
      }
    }
    return tagsSet;
  }


  public static void main(String[] args) {
    try {
      database = EntityDatabase.getInstance();
      AllPresetsJson allPresets = new AllPresetsJson(PRESETS_FILE);

      Multiset<String> originalTags = getOriginalState();

      for (SpawnPresetJson j : allPresets.getItemSpawnSettings()) {
        List<Rule> rulesList = createItemRulesList(j.getRules());
        describeRandomization(j, rulesList, originalTags);
      }

      for (SpawnPresetJson j : allPresets.getEnemySpawnSettings()) {
        List<Rule> rulesList = createNpcRulesList(j.getRules());
        describeRandomization(j, rulesList, originalTags);
      }
    } catch (IOException | JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
