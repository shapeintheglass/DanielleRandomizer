package utils.validators;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import json.SettingsJson;
import randomizers.gameplay.filters.FlowgraphFilter;
import randomizers.gameplay.filters.ItemSpawnFilter;
import utils.LevelConsts;
import utils.Utils;
import utils.ZipHelper;

public class CustomSpawnValidator {
  private static final String LEVEL = "research/lobby";

  private static final List<String> TO_PRINT = Lists.newArrayList("Stickynote_Mimic", "MissionItems", "ExplosiveTanks",
      "SuitPatchKit", "WeaponUpgradeKit", "FabricationPlans", "Mods", "SpareParts", "Medical", "Neuromod",
      "Ingredients", "Weapons", "Ammo", "Food", "RecyclerJunk", "_CARRYABLE");

  private ItemSpawnFilter isf;
  private FlowgraphFilter fgf;
  private Random r;
  private int numFilteredByArchetypeSwap;
  private int numFilteredByFlowgraphSwap;

  private Multimap<String, String> startArchetypeToEndArchetype;
  private List<String> sortedKeys;

  private Map<String, Integer> beforeTagsCount;
  private Map<String, Integer> afterTagsCount;
  private Set<String> allTags;

  private TaggedDatabase database;
  private ZipHelper zipHelper;

  private CustomSpawnValidator(TaggedDatabase database, SettingsJson settings, ZipHelper zipHelper) {
    this.database = database;
    this.zipHelper = zipHelper;
    isf = new ItemSpawnFilter(database, settings);
    fgf = new FlowgraphFilter(database, settings);
    r = new Random(settings.getSeed());
    numFilteredByArchetypeSwap = 0;
    numFilteredByFlowgraphSwap = 0;
    startArchetypeToEndArchetype = ArrayListMultimap.create();
    sortedKeys = Lists.newArrayList();
    beforeTagsCount = Maps.newHashMap();
    afterTagsCount = Maps.newHashMap();
    allTags = Sets.newHashSet();
  }

  private boolean filter(Element e) {
    boolean filtered = false;
    if (isf.filterEntity(e, r, LEVEL)) {
      numFilteredByArchetypeSwap++;
      filtered = true;
    }
    if (fgf.filterEntity(e, r, LEVEL)) {
      numFilteredByFlowgraphSwap++;
      filtered = true;
    }
    return filtered;
  }

  private void filterEntity(Element entity) {
    String before = entity.getAttributeValue("Archetype");
    Set<String> beforeTags = Sets.newHashSet();
    if (before != null) {
      Element oldArchetype = database.getEntityByName(Utils.stripPrefix(before));
      if (oldArchetype != null) {
        beforeTags = Utils.getTags(oldArchetype);
      }
    }

    if (filter(entity)) {
      String after = entity.getAttributeValue("Archetype");
      if (after != null) {
        Element newArchetype = database.getEntityByName(Utils.stripPrefix(after));
        Set<String> afterTags = Utils.getTags(newArchetype);
        if (before != null && !startArchetypeToEndArchetype.containsKey(before)) {
          sortedKeys.add(before);
        }
        updateTagsCount(beforeTags, beforeTagsCount);
        updateTagsCount(afterTags, afterTagsCount);
      }

      startArchetypeToEndArchetype.put(before, entity.getAttributeValue("Archetype"));

    }
  }

  private void updateTagsCount(Set<String> tags, Map<String, Integer> tagsCount) {
    allTags.addAll(tags);
    for (String s : tags) {
      if (!tagsCount.containsKey(s)) {
        tagsCount.put(s, 1);
      } else {
        tagsCount.put(s, tagsCount.get(s) + 1);
      }
    }
  }

  private void print() {
    allTags.remove(null);
    List<String> sortedTags = Lists.newArrayList(allTags);
    Collections.sort(sortedTags);
    System.out.println(numFilteredByArchetypeSwap);
    System.out.println("Name,Before,After,Diff");
    //Collections.sort(TO_PRINT);
    for (String tag : sortedTags) {
      int before = beforeTagsCount.containsKey(tag) ? beforeTagsCount.get(tag) : 0;
      int after = afterTagsCount.containsKey(tag) ? afterTagsCount.get(tag) : 0;
      System.out.printf("%s,%d,%d,%d\n", tag, before, after, after - before);
    }
    /*
    Collections.sort(sortedKeys);
    for (String key : sortedKeys) {
      List<String> results = Lists.newArrayList(startArchetypeToEndArchetype.get(key));
      Collections.sort(results);
      System.out.printf("%d,%s,%s\n", results.size(), key, results.toString().replace(',', ';'));
    }*/
  }

  private void processLevel(String level) throws IOException, JDOMException {
    Document d = zipHelper.getDocument(ZipHelper.DATA_LEVELS + "/" + level + "/" + "mission_mission0.xml");
    Collection<Element> entities = d.getRootElement().getChild("Objects").getChildren();
    for (Element e : entities) {
      filterEntity(e);
    }
  }

  public static void main(String[] args) {
    ZipHelper zipHelper = null;
    try {
      zipHelper = new ZipHelper(null, null);
      TaggedDatabase database = EntityDatabase.getInstance(zipHelper);
      SettingsJson settings = new SettingsJson("saved_settings.json");
      CustomSpawnValidator validator = new CustomSpawnValidator(database, settings, zipHelper);

      for (String level : LevelConsts.LEVEL_DIRS) {
        validator.processLevel(level);
      }

      validator.print();

    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    } finally {
      zipHelper.close();
    }
  }
}
