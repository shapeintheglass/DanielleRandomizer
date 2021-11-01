package utils;

import java.util.List;
import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.Lists;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;
import randomizers.gameplay.filters.rules.NpcSpawnerSwapRule;

public class CustomEnemyFilterHelper {

  private TaggedDatabase database;
  private List<NpcSpawnerSwapRule> ruleSet;

  public CustomEnemyFilterHelper(Settings settings, TaggedDatabase database) {
    this.database = database;

    ruleSet = Lists.newArrayList();
    GenericSpawnPresetFilter spawnPreset = settings.getGameplaySettings().getEnemySpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {
      GenericSpawnPresetRule.Builder copy = rule.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS);
      if (!settings.getMoreSettings().getPreySoulsEnemies()) {
        copy.addDoNotOutputTags("PreySoulsEnemies");
      }
      ruleSet.add(new NpcSpawnerSwapRule(database, new CustomRuleHelper(copy.build(), database)));
    }
  }

  public Element getEntity(String entityName, String nameInLevel, Random r) {
    for (NpcSpawnerSwapRule rule : ruleSet) {
      if (rule.getCustomRuleHelper().trigger(database, entityName, nameInLevel)) {
        return rule.getCustomRuleHelper().getEntityToSwap(database, r);
      }
    }
    return null;
  }

  public String getEntityStr(String entityName, String nameInLevel, Random r) {
    Element entityToSwap = getEntity(entityName, nameInLevel, r);
    if (entityToSwap == null) {
      return null;
    }
    return Utils.getNameForEntity(entityToSwap);
  }

  public boolean trigger(String entityName, String nameInLevel) {
    for (NpcSpawnerSwapRule rule : ruleSet) {
      if (rule.getCustomRuleHelper().trigger(database, entityName, nameInLevel)) {
        return true;
      }
    }
    return false;
  }
}
