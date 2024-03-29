package utils.validators;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import com.google.protobuf.util.JsonFormat;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import gui2.Gui2Consts;
import proto.RandomizerSettings.AllPresets;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import randomizers.gameplay.filters.rules.ArchetypeSwapRule;
import randomizers.gameplay.filters.rules.NpcSpawnerSwapRule;
import randomizers.gameplay.filters.rules.Rule;
import utils.CustomRuleHelper;
import utils.LevelConsts;
import utils.ZipHelper;

/**
 * 
 * Helper utility for sanity checking generated spawn rates.
 *
 */
public class SpawnStatsUtil {

  private static final long SEED = -1738138057449994609L;
  public static final String LEVEL_FILE = "research/lobby";
  public static final String LEVEL_FILE_NAME = "mission_mission0.xml";
  public static final String PRESETS_FILE = "spawn_presets.json";

  private static TaggedDatabase database;

  public static Element getEntitiesFromLevel(File level) throws JDOMException, IOException {
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(level);
    return document.getRootElement().getChild("Objects");
  }

  public static List<Rule> createItemRulesList(List<GenericSpawnPresetRule> rules) {
    List<Rule> rulesList = Lists.newArrayList();
    for (GenericSpawnPresetRule grj : rules) {
      GenericSpawnPresetRule copy = grj.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS)
          .build();
      CustomRuleHelper crh = new CustomRuleHelper(copy, database);
      rulesList.add(new ArchetypeSwapRule(database, crh));
    }
    return rulesList;
  }

  public static List<Rule> createNpcRulesList(List<GenericSpawnPresetRule> rules) {
    List<Rule> rulesList = Lists.newArrayList();
    for (GenericSpawnPresetRule grj : rules) {
      GenericSpawnPresetRule copy = grj.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS)
          .build();
      CustomRuleHelper crh = new CustomRuleHelper(copy, database);
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
    if (e.getAttributeValue("EntityClass") != null && e.getAttributeValue("EntityClass").equals("ArkNpcSpawner")) {
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

  public static void describeRandomization(GenericSpawnPresetFilter j, List<Rule> rulesList,
      Multiset<String> originalTags, ZipHelper zipHelper) throws JDOMException, IOException {
    Document document = zipHelper.getDocument(ZipHelper.DATA_LEVELS + "/" + LEVEL_FILE + "/" + LEVEL_FILE_NAME);
    Element root = document.getRootElement().getChild("Objects");
    System.out.println(j.getName());
    System.out.println(j.getDesc());

    Multiset<String> tagsSet = HashMultiset.create();

    Random r = new Random();
    long seed = SEED;
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

  public static Multiset<String> getOriginalState(ZipHelper zipHelper) throws JDOMException, IOException {
    Document document = zipHelper.getDocument(ZipHelper.DATA_LEVELS + "/" + LEVEL_FILE + "/" + LEVEL_FILE_NAME);
    Element root = document.getRootElement().getChild("Objects");

    Multiset<String> tagsSet = HashMultiset.create();

    for (Element e : root.getChildren()) {
      List<String> tags = getTagsForEntity(e);
      if (tags != null) {
        tagsSet.addAll(tags);
      }
    }
    return tagsSet;
  }

  public static void main(String[] args) {
    ZipHelper zipHelper = null;
    try {
      zipHelper = new ZipHelper(null, null);
      database = EntityDatabase.getInstance(zipHelper);
      FileReader fr = new FileReader(Gui2Consts.ALL_PRESETS_FILE);
      AllPresets.Builder builder = AllPresets.newBuilder();
      JsonFormat.parser().ignoringUnknownFields().merge(fr, builder);
      AllPresets allPresets = builder.build();
      Multiset<String> originalTags = getOriginalState(zipHelper);

      for (GenericSpawnPresetFilter j : allPresets.getPickupSpawnSettingsList()) {
        List<Rule> rulesList = createItemRulesList(j.getFiltersList());
        describeRandomization(j, rulesList, originalTags, zipHelper);
      }

      for (GenericSpawnPresetFilter j : allPresets.getEnemySpawnSettingsList()) {
        List<Rule> rulesList = createNpcRulesList(j.getFiltersList());
        describeRandomization(j, rulesList, originalTags, zipHelper);
      }
    } catch (IOException | JDOMException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      zipHelper.close();
    }
  }
}
